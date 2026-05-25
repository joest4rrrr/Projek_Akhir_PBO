package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class RentalSession {
    private int id;
    private Device device;
    private String customerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private double totalCost;

    public RentalSession(Device device, String customerName) {
        this.device       = device;
        this.customerName = customerName;
        this.startTime    = LocalDateTime.now();
    }

    public void endSession() {
        this.endTime         = LocalDateTime.now();
        long diff            = Duration.between(startTime, endTime).toMinutes();
        this.durationMinutes = (int) (diff == 0 ? 1 : diff);
        this.totalCost       = device.calculateCost(durationMinutes);
        device.setAvailable(true);
    }

    /**
     * Digunakan BookingPanel: set semua field secara manual
     * tanpa bergantung pada LocalDateTime.now()
     */
    public void forceEnd(LocalDateTime start, LocalDateTime end,
                         int durationMinutes, double totalCost) {
        this.startTime       = start;
        this.endTime         = end;
        this.durationMinutes = durationMinutes;
        this.totalCost       = totalCost;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }
    public Device getDevice()            { return device; }
    public String getCustomerName()      { return customerName; }
    public LocalDateTime getStartTime()  { return startTime; }
    public LocalDateTime getEndTime()    { return endTime; }
    public int getDurationMinutes()      { return durationMinutes; }
    public double getTotalCost()         { return totalCost; }
}