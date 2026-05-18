package model;

public interface Rentable {
    double calculateCost(int durationMinutes);
    String getDeviceInfo();
    boolean isAvailable();
}