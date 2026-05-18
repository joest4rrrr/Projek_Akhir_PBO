package model;

public class PS4 extends Device {
    private static final double RATE = 5000;

    public PS4(String id) { super(id, "PlayStation 4"); }

    @Override
    public double calculateCost(int durationMinutes) {
        return Math.ceil(durationMinutes / 30.0) * RATE;
    }
}