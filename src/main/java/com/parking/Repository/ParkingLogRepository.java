package com.parking.Repository;

import com.parking.Models.ParkingLogEntry;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ParkingLogRepository {
    private final String url = "jdbc:postgresql://localhost:5432/parking";
    private final String user = "postgres";
    private final String password = "rout";

    // Créer la table de journalisation si elle n'existe pas
    public void createLogTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS parking_logs (" +
                     "id SERIAL PRIMARY KEY, " +
                     "license_plate VARCHAR(20) NOT NULL, " +
                     "entry_time TIMESTAMP NOT NULL, " +
                     "exit_time TIMESTAMP NOT NULL, " +
                     "duration_minutes BIGINT NOT NULL)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table de logs : " + e.getMessage());
        }
    }

    // Obtenir une connexion à la base de données
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Ajouter une entrée de log
    public void addLogEntry(ParkingLogEntry logEntry) {
        String sql = "INSERT INTO parking_logs (license_plate, entry_time, exit_time, duration_minutes) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, logEntry.getLicensePlate());
            stmt.setTimestamp(2, Timestamp.valueOf(logEntry.getEntryTime()));
            stmt.setTimestamp(3, Timestamp.valueOf(logEntry.getExitTime()));
            stmt.setLong(4, logEntry.getDurationMinutes());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une entrée de log : " + e.getMessage());
        }
    }

    // Récupérer tous les logs
    public List<ParkingLogEntry> getAllLogs() {
        List<ParkingLogEntry> logs = new ArrayList<>();
        String sql = "SELECT id, license_plate, entry_time, exit_time, duration_minutes FROM parking_logs ORDER BY entry_time DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ParkingLogEntry logEntry = new ParkingLogEntry();
                logEntry.setId(rs.getLong("id"));
                logEntry.setLicensePlate(rs.getString("license_plate"));
                logEntry.setEntryTime(rs.getTimestamp("entry_time").toLocalDateTime());
                logEntry.setExitTime(rs.getTimestamp("exit_time").toLocalDateTime());
                logEntry.setDurationMinutes(rs.getLong("duration_minutes"));
                
                logs.add(logEntry);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des logs : " + e.getMessage());
        }
        
        return logs;
    }

    // Récupérer les logs récents (par exemple, les 50 dernières entrées)
    public List<ParkingLogEntry> getRecentLogs(int limit) {
        List<ParkingLogEntry> logs = new ArrayList<>();
        String sql = "SELECT id, license_plate, entry_time, exit_time, duration_minutes " +
                     "FROM parking_logs ORDER BY entry_time DESC LIMIT ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ParkingLogEntry logEntry = new ParkingLogEntry();
                    logEntry.setId(rs.getLong("id"));
                    logEntry.setLicensePlate(rs.getString("license_plate"));
                    logEntry.setEntryTime(rs.getTimestamp("entry_time").toLocalDateTime());
                    logEntry.setExitTime(rs.getTimestamp("exit_time").toLocalDateTime());
                    logEntry.setDurationMinutes(rs.getLong("duration_minutes"));
                    
                    logs.add(logEntry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des logs récents : " + e.getMessage());
        }
        
        return logs;
    }
}