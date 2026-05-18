package model;

public class PS3 extends Device {
    private static final double RATE = 3000;

    public PS3(String id) { super(id, "PlayStation 3"); }

    @Override
    public double calculateCost(int durationMinutes) {
        return Math.ceil(durationMinutes / 30.0) * RATE;
    }
}