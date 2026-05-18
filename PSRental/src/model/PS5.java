package model;

public class PS5 extends Device {
    private static final double RATE = 8000;

    public PS5(String id) { super(id, "PlayStation 5"); }

    @Override
    public double calculateCost(int durationMinutes) {
        return Math.ceil(durationMinutes / 30.0) * RATE;
    }
}