package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private int id;
    private String deviceId;
    private String customerName;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // pending, active, done, cancelled

    public Booking() {}

    public Booking(String deviceId, String customerName,
                   LocalDate bookingDate, LocalTime startTime, LocalTime endTime) {
        this.deviceId     = deviceId;
        this.customerName = customerName;
        this.bookingDate  = bookingDate;
        this.startTime    = startTime;
        this.endTime      = endTime;
        this.status       = "pending";
    }

    // ---------- getters & setters ----------
    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }

    public String getDeviceId()               { return deviceId; }
    public void setDeviceId(String deviceId)  { this.deviceId = deviceId; }

    public String getCustomerName()                       { return customerName; }
    public void setCustomerName(String customerName)      { this.customerName = customerName; }

    public LocalDate getBookingDate()                     { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate)     { this.bookingDate = bookingDate; }

    public LocalTime getStartTime()                       { return startTime; }
    public void setStartTime(LocalTime startTime)         { this.startTime = startTime; }

    public LocalTime getEndTime()                         { return endTime; }
    public void setEndTime(LocalTime endTime)             { this.endTime = endTime; }

    public String getStatus()                             { return status; }
    public void setStatus(String status)                  { this.status = status; }

    @Override
    public String toString() {
        return deviceId + " | " + customerName + " | " +
               startTime + " - " + endTime + " | " + status;
    }
}
