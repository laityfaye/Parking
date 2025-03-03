package com.parking.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parking.Models.Car;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ParkingController {

    private List<Car> cars = new ArrayList<>(); // Liste des voitures dans le parking
    private final int totalSpaces = 10; // Nombre total de places

    @GetMapping("/parking")
    public String showParking(Model model) {
        model.addAttribute("totalSpaces", totalSpaces);
        model.addAttribute("availableSpaces", totalSpaces - cars.size());
        model.addAttribute("occupiedSpaces", cars.size());
        model.addAttribute("cars", cars);
        return "parking"; // Retourne la vue JSP
    }

    @PostMapping("/add-car")
    public String addCar(@RequestParam String licensePlate, Model model) {
        // Vérifier si la plaque d'immatriculation est déjà présente
        boolean carExists = cars.stream()
                .anyMatch(car -> car.getLicensePlate().equals(licensePlate));

        if (carExists) {
            model.addAttribute("message", "Cette voiture est déjà dans le parking !");
        } else if (cars.size() < totalSpaces) {
            Car car = new Car(licensePlate);
            cars.add(car);
            model.addAttribute("message", "Voiture ajoutée avec succès !");
        } else {
            model.addAttribute("message", "Parking plein !");
        }
        return "redirect:/parking"; // Redirige vers la page du parking
    }

    @PostMapping("/remove-car")
    public String removeCar(@RequestParam String licensePlate, Model model) {
        Car carToRemove = null;
        for (Car car : cars) {
            if (car.getLicensePlate().equals(licensePlate)) {
                carToRemove = car;
                break;
            }
        }
        if (carToRemove != null) {
            cars.remove(carToRemove);
            model.addAttribute("message", "Voiture retirée avec succès !");
        } else {
            model.addAttribute("message", "Voiture non trouvée !");
        }
        return "redirect:/parking"; // Redirige vers la page du parking
    }
}