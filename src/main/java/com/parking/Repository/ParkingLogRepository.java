package com.parking.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLogRepository {
    private final String url = "jdbc:postgresql://localhost:5432/parking";
    private final String user = "postgres";
    private final String password = "rout";
    
    // Structure pour stocker les données du log
    public static class ParkingLogEntry {
        private Long id;
        private String licensePlate;
        private LocalDateTime entryTime;
        private LocalDateTime exitTime;
        private int durationMinutes;
        
        // Constructeurs, getters, setters...
    }
    
    public List<ParkingLogEntry> getRecentLogs(int limit) {
        List<ParkingLogEntry> logs = new ArrayList<>();
        String sql = "SELECT * FROM parking_log ORDER BY exit_time DESC LIMIT ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ParkingLogEntry entry = new ParkingLogEntry();
                    // Remplir l'objet avec les données de la base
                    logs.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return logs;
    }
}