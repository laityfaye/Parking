package com.parking.Service;

import java.util.List;

import com.parking.Models.Car;
import com.parking.Repository.CarRepositoryImpl;

public class ParkingService {
    private final CarService carRepository;
    private final int totalSpaces;

    public ParkingService(CarService carRepository, int totalSpaces) {
        this.carRepository = carRepository;
        this.totalSpaces = totalSpaces;
    }

    public boolean enterCar(Car car) {
        // Vérifier si la plaque est valide (format de plaque française par exemple)
        if (!isValidLicensePlate(car.getLicensePlate())) {
            return false;
        }
        
        // Vérifier s'il y a des places disponibles
        if (carRepository.getAvailableSpaces(totalSpaces) > 0) {
            return carRepository.addCar(car);
        }
        return false; // Parking plein
    }

    public boolean exitCar(String licensePlate) {
        return carRepository.removeCar(licensePlate);
    }

    public List<Car> getCars() {
        return carRepository.getAllCars();
    }

    public int getAvailableSpaces() {
        return carRepository.getAvailableSpaces(totalSpaces);
    }
    
    // Méthode utilitaire pour valider le format de plaque d'immatriculation
    private boolean isValidLicensePlate(String licensePlate) {
        // Format basique (à adapter selon le pays)
        return licensePlate != null && 
               licensePlate.matches("[A-Z0-9-]{2,10}");
    }
}