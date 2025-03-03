package com.parking.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.parking.Models.Car;
import com.parking.Service.CarService;

public class CarRepositoryImpl implements CarService {
    private final String url = "jdbc:postgresql://localhost:5432/parking";
    private final String user = "postgres";
    private final String password = "rout";

    @Override
    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (license_plate) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getLicensePlate());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCar(String licensePlate) {
        String sql = "DELETE FROM cars WHERE license_plate = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Car car = new Car(rs.getString("license_plate"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public int getAvailableSpaces(int totalSpaces) {
        String sql = "SELECT COUNT(*) FROM cars";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return totalSpaces - rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSpaces;
    }
}