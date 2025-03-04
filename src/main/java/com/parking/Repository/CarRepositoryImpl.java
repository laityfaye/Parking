package com.parking.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.parking.Models.Car;
import com.parking.Service.CarService;

public class CarRepositoryImpl implements CarService {
    private final String url = "jdbc:postgresql://localhost:5432/parking";
    private final String user = "postgres";
    private final String password = "rout";
    
    // Initialiser la connexion et créer la table si nécessaire
    public CarRepositoryImpl() {
        createTableIfNotExists();
    }
    
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS cars (" +
                     "id SERIAL PRIMARY KEY, " +
                     "license_plate VARCHAR(20) NOT NULL UNIQUE, " +
                     "entry_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                     
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table: " + e.getMessage());
        }
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (license_plate, entry_time) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, car.getLicensePlate());
            stmt.setTimestamp(2, Timestamp.valueOf(car.getEntryTime()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        car.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une voiture: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeCar(String licensePlate) {
        String sql = "DELETE FROM cars WHERE license_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, licensePlate);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'une voiture: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT id, license_plate, entry_time FROM cars";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Long id = rs.getLong("id");
                String licensePlate = rs.getString("license_plate");
                LocalDateTime entryTime = rs.getTimestamp("entry_time").toLocalDateTime();
                
                Car car = new Car(id, licensePlate, entryTime);
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des voitures: " + e.getMessage());
        }
        return cars;
    }

    @Override
    public int getAvailableSpaces(int totalSpaces) {
        String sql = "SELECT COUNT(*) FROM cars";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return totalSpaces - rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul des places disponibles: " + e.getMessage());
        }
        return totalSpaces;
    }
    
    // Méthode utile pour vérifier si une voiture existe déjà
    public boolean carExists(String licensePlate) {
        String sql = "SELECT COUNT(*) FROM cars WHERE license_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, licensePlate);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification d'existence: " + e.getMessage());
        }
        return false;
    }
}