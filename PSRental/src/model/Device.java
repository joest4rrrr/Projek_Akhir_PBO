package model;

public abstract class Device implements Rentable {
    private String id;
    private String name;
    private boolean available;

    public Device(String id, String name) {
        this.id        = id;
        this.name      = name;
        this.available = true;
    }

    @Override
    public abstract double calculateCost(int durationMinutes);

    @Override
    public boolean isAvailable() { return available; }

    @Override
    public String getDeviceInfo() {
        return name + " [" + id + "] - " + (available ? "Tersedia" : "Disewa");
    }

    public String getId()   { return id; }
    public String getName() { return name; }
    public void setAvailable(boolean available) { this.available = available; }
}