package com.parking.Models;

import java.time.LocalDateTime;

public class ParkingLogEntry {
    private Long id;
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long durationMinutes;

    // Constructeurs
    public ParkingLogEntry() {}

    public ParkingLogEntry(String licensePlate, LocalDateTime entryTime, LocalDateTime exitTime) {
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.durationMinutes = java.time.Duration.between(entryTime, exitTime).toMinutes();
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    @Override
    public String toString() {
        return "ParkingLogEntry{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}