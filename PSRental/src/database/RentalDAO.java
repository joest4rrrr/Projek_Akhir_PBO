package database;

import model.Device;
import model.PS3;
import model.PS4;
import model.PS5;
import model.RentalSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAO {

    public List<Device> getAllDevices() {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT * FROM devices";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String id     = rs.getString("id");
                boolean avail = rs.getBoolean("is_available");
                Device d;
                if (id.startsWith("PS3"))      d = new PS3(id);
                else if (id.startsWith("PS4")) d = new PS4(id);
                else                           d = new PS5(id);
                d.setAvailable(avail);
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateDeviceStatus(String deviceId, boolean available) {
        String sql = "UPDATE devices SET is_available=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setString(2, deviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRental(RentalSession session) {
        String sql = "INSERT INTO rentals(device_id, customer_name, " +
                     "start_time, end_time, duration_minutes, total_cost) " +
                     "VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, session.getDevice().getId());
            ps.setString(2, session.getCustomerName());
            ps.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            ps.setTimestamp(4, Timestamp.valueOf(session.getEndTime()));
            ps.setInt(5, session.getDurationMinutes());
            ps.setDouble(6, session.getTotalCost());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getRentalHistory() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT id, device_id, customer_name, " +
                     "duration_minutes, total_cost FROM rentals ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("device_id"),
                    rs.getString("customer_name"),
                    rs.getInt("duration_minutes") + " menit",
                    "Rp " + String.format("%,.0f", rs.getDouble("total_cost"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteRentalById(int id) {
        String sql = "DELETE FROM rentals WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllRentals() {
        String sql = "DELETE FROM rentals";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}