package com.parking.Service;

import java.util.List;

import com.parking.Models.Car;

public class ParkingService {
    private final CarService carRepository;
    private final int totalSpaces;

    public ParkingService(CarService carRepository, int totalSpaces) {
        this.carRepository = carRepository;
        this.totalSpaces = totalSpaces;
    }

    public boolean enterCar(Car car) {
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
}