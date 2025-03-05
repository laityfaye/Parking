package com.parking.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Car {
    private Long id;
    private String licensePlate; // Plaque d'immatriculation
    private LocalDateTime entryTime;

    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = LocalDateTime.now();
    }

    public Car(Long id, String licensePlate, LocalDateTime entryTime) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    
    public String getFormattedEntryTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return entryTime.format(formatter);
        }
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", entryTime=" + entryTime +
                '}';
    }
}