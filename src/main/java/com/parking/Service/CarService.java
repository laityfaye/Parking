package com.parking.Service;

import com.parking.Models.Car;

import java.util.List;

public interface CarService {
    boolean addCar(Car car);
    boolean removeCar(String licensePlate);
    List<Car> getAllCars();
    int getAvailableSpaces(int totalSpaces);
}