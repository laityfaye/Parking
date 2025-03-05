package com.parking.Service;

import com.parking.Models.Car;
import com.parking.Models.ParkingLogEntry;
import com.parking.Repository.ParkingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingLogService {
    private final ParkingLogRepository parkingLogRepository;

    @Autowired
    public ParkingLogService(ParkingLogRepository parkingLogRepository) {
        this.parkingLogRepository = parkingLogRepository;
        parkingLogRepository.createLogTableIfNotExists();
    }

    // Créer une entrée de log lorsqu'une voiture sort du parking
    public void logParkingExit(Car car, LocalDateTime exitTime) {
        ParkingLogEntry logEntry = new ParkingLogEntry(
            car.getLicensePlate(), 
            car.getEntryTime(), 
            exitTime
        );
        
        parkingLogRepository.addLogEntry(logEntry);
    }

    // Récupérer tous les logs
    public List<ParkingLogEntry> getAllParkingLogs() {
        return parkingLogRepository.getAllLogs();
    }

    // Récupérer les logs récents
    public List<ParkingLogEntry> getRecentParkingLogs(int limit) {
        return parkingLogRepository.getRecentLogs(limit);
    }
}