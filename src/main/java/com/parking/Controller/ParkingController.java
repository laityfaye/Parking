package com.parking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parking.Models.Car;
import com.parking.Models.ParkingLogEntry;
import com.parking.Service.ParkingService;
import com.parking.Service.ParkingLogService;
import com.parking.Repository.CarRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ParkingController {
    private final ParkingService parkingService;
    private final ParkingLogService parkingLogService;
    private final CarRepositoryImpl carRepository;
    private final int totalSpaces = 10; // Nombre total de places

    @Autowired
    public ParkingController(
        CarRepositoryImpl carRepository, 
        ParkingLogService parkingLogService
    ) {
        this.carRepository = carRepository;
        this.parkingLogService = parkingLogService;
        this.parkingService = new ParkingService(carRepository, totalSpaces);
    }

    @GetMapping("/parking")
    public String showParking(Model model) {
        model.addAttribute("totalSpaces", totalSpaces);
        model.addAttribute("availableSpaces", parkingService.getAvailableSpaces());
        model.addAttribute("occupiedSpaces", totalSpaces - parkingService.getAvailableSpaces());
        model.addAttribute("cars", parkingService.getCars());
        
        // Récupération des logs récents
        List<ParkingLogEntry> recentLogs = parkingLogService.getRecentParkingLogs(50);
        model.addAttribute("parkingLogs", recentLogs);
        
        return "parking";
    }
    @GetMapping("/parking-logs")
    public String showParkingLogs(Model model) {
        // Récupérer les logs récents (par exemple, les 50 derniers)
        model.addAttribute("parkingLogs", parkingLogService.getRecentParkingLogs(50));
        return "parking-logs"; // Nouvelle vue pour afficher les logs
    }

    @PostMapping("/add-car")
    public String addCar(@RequestParam String licensePlate, Model model) {
        Car car = new Car(licensePlate);
        
        // Vérifier si la voiture existe déjà
        if (carRepository.carExists(licensePlate)) {
            model.addAttribute("message", "Cette voiture est déjà dans le parking !");
        } else {
            // Utiliser le service pour ajouter la voiture
            boolean added = parkingService.enterCar(car);
            
            if (added) {
                model.addAttribute("message", "Voiture ajoutée avec succès !");
            } else {
                model.addAttribute("message", "Impossible d'ajouter la voiture (parking plein ou erreur).");
            }
        }
        
        return "redirect:/parking"; // Redirige vers la page du parking
    }

    @PostMapping("/remove-car")
    public String removeCar(@RequestParam String licensePlate, Model model) {
        // Trouver la voiture avant de la supprimer pour le log
        Car carToRemove = parkingService.getCars().stream()
            .filter(car -> car.getLicensePlate().equals(licensePlate))
            .findFirst()
            .orElse(null);
        
        boolean removed = parkingService.exitCar(licensePlate);
        
        if (removed && carToRemove != null) {
            // Créer une entrée de log lorsque la voiture sort
            parkingLogService.logParkingExit(carToRemove, LocalDateTime.now());
            model.addAttribute("message", "Voiture retirée avec succès !");
        } else {
            model.addAttribute("message", "Voiture non trouvée !");
        }
        
        return "redirect:/parking"; // Redirige vers la page du parking
    }
}