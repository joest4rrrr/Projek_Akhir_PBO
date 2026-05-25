package database;

import model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // ----------------------------------------------------------------
    // Simpan booking baru, return id yang di-generate
    // ----------------------------------------------------------------
    public int saveBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (device_id, customer_name, booking_date, " +
                     "start_time, end_time, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, booking.getDeviceId());
            ps.setString(2, booking.getCustomerName());
            ps.setDate(3, Date.valueOf(booking.getBookingDate()));
            ps.setTime(4, Time.valueOf(booking.getStartTime()));
            ps.setTime(5, Time.valueOf(booking.getEndTime()));
            ps.setString(6, booking.getStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    // ----------------------------------------------------------------
    // Cek apakah slot waktu bentrok untuk device & tanggal tertentu
    // ----------------------------------------------------------------
    public boolean isSlotConflict(String deviceId, LocalDate date,
                                  LocalTime start, LocalTime end, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings " +
                     "WHERE device_id = ? AND booking_date = ? " +
                     "AND status NOT IN ('done', 'cancelled') " +
                     "AND id != ? " +
                     "AND start_time < ? AND end_time > ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, deviceId);
            ps.setDate(2, Date.valueOf(date));
            ps.setInt(3, excludeId);
            ps.setTime(4, Time.valueOf(end));
            ps.setTime(5, Time.valueOf(start));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    // Overload tanpa excludeId (untuk booking baru)
    public boolean isSlotConflict(String deviceId, LocalDate date,
                                  LocalTime start, LocalTime end) throws SQLException {
        return isSlotConflict(deviceId, date, start, end, -1);
    }

    // ----------------------------------------------------------------
    // Ambil semua booking untuk hari ini
    // ----------------------------------------------------------------
    public List<Booking> getBookingsByDate(LocalDate date) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE booking_date = ? ORDER BY start_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ----------------------------------------------------------------
    // Ambil semua booking (untuk tabel history booking)
    // ----------------------------------------------------------------
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY booking_date DESC, start_time DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ----------------------------------------------------------------
    // Update status booking
    // ----------------------------------------------------------------
    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    // Hapus booking by id
    // ----------------------------------------------------------------
    public void deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ----------------------------------------------------------------
    // Cek apakah ada booking aktif untuk device tertentu saat ini
    // ----------------------------------------------------------------
    public Booking getActiveBookingForDevice(String deviceId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE device_id = ? " +
                     "AND booking_date = ? AND status = 'pending' " +
                     "AND start_time <= ? AND end_time > ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            LocalDate today = LocalDate.now();
            LocalTime now   = LocalTime.now();
            ps.setString(1, deviceId);
            ps.setDate(2, Date.valueOf(today));
            ps.setTime(3, Time.valueOf(now));
            ps.setTime(4, Time.valueOf(now));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    // ----------------------------------------------------------------
    // Helper mapping ResultSet -> Booking
    // ----------------------------------------------------------------
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setDeviceId(rs.getString("device_id"));
        b.setCustomerName(rs.getString("customer_name"));
        b.setBookingDate(rs.getDate("booking_date").toLocalDate());
        b.setStartTime(rs.getTime("start_time").toLocalTime());
        b.setEndTime(rs.getTime("end_time").toLocalTime());
        b.setStatus(rs.getString("status"));
        return b;
    }
}
