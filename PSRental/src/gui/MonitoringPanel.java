package gui;

import database.RentalDAO;
import database.BookingDAO;
import model.Device;
import model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MonitoringPanel extends JPanel {

    private static final Color BG         = Color.WHITE;
    private static final Color BG_ROW     = new Color(248, 249, 250);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT       = new Color(50, 50, 50);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);
    private static final Color PRIMARY    = new Color(41, 128, 185);
    private static final Color SUCCESS    = new Color(39, 174, 96);
    private static final Color DANGER     = new Color(192, 57, 43);
    private static final Color WARNING    = new Color(211, 84, 0);
    private static final Color NEUTRAL    = new Color(127, 140, 141);

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private RentalDAO  rentalDAO  = new RentalDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblTersedia, lblDisewa, lblBookedHariIni, lblWaktuUpdate;

    // epoch milidetik start sewa per device_id, dikirim dari RentalPanel
    private Map<String, Long> activeSessionStartEpoch = new HashMap<>();

    private Timer autoRefreshTimer;

    public MonitoringPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildStats(),  BorderLayout.NORTH);
        add(buildTable(),  BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        // auto-refresh setiap 5 detik
        autoRefreshTimer = new Timer(5000, e -> refresh());
        autoRefreshTimer.start();

        refresh();
    }

    // ================================================================ STATS
    private JPanel buildStats() {
        JPanel wrap = new JPanel(new BorderLayout(0, 10));
        wrap.setBackground(BG);

        JPanel stats = new JPanel(new GridLayout(1, 3, 10, 0));
        stats.setBackground(BG);
        stats.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblTersedia      = new JLabel("0", SwingConstants.CENTER);
        lblDisewa        = new JLabel("0", SwingConstants.CENTER);
        lblBookedHariIni = new JLabel("0", SwingConstants.CENTER);

        stats.add(makeCard("Tersedia",         lblTersedia,       SUCCESS));
        stats.add(makeCard("Sedang Disewa",    lblDisewa,         DANGER));
        stats.add(makeCard("Booking Hari Ini", lblBookedHariIni,  WARNING));

        JPanel infoBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoBar.setBackground(BG);
        lblWaktuUpdate = new JLabel("Terakhir diperbarui: -");
        lblWaktuUpdate.setFont(new Font("Arial", Font.PLAIN, 11));
        lblWaktuUpdate.setForeground(TEXT_MUTED);
        infoBar.add(lblWaktuUpdate);

        wrap.add(stats,   BorderLayout.NORTH);
        wrap.add(infoBar, BorderLayout.SOUTH);
        return wrap;
    }

    // ================================================================ TABLE
    private JScrollPane buildTable() {
        String[] cols = {"Perangkat", "Tipe", "Status", "Durasi Sewa", "Booking Berikutnya"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? BG : BG_ROW);
                    c.setForeground(TEXT);
                }
                return c;
            }
        };

        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(34);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 235, 235));
        table.setSelectionBackground(new Color(213, 234, 254));
        table.setSelectionForeground(TEXT);
        table.setFocusable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(TEXT_MUTED);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(220);

        // renderer Status
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String v = val == null ? "" : val.toString();
                lbl.setForeground(v.equals("Disewa") ? DANGER : SUCCESS);
                lbl.setFont(new Font("Arial", Font.BOLD, 12));
                if (!sel) lbl.setBackground(row % 2 == 0 ? BG : BG_ROW);
                return lbl;
            }
        });

        // renderer Durasi — merah kalau >= 1 jam
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
                String v = val == null ? "-" : val.toString();
                if (v.equals("-")) {
                    lbl.setForeground(new Color(180, 180, 180));
                } else {
                    try {
                        int hours = Integer.parseInt(v.split(":")[0]);
                        lbl.setForeground(hours >= 1 ? DANGER : WARNING);
                    } catch (Exception ignored) {
                        lbl.setForeground(WARNING);
                    }
                }
                if (!sel) lbl.setBackground(row % 2 == 0 ? BG : BG_ROW);
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(BG);
        return scroll;
    }

    // ================================================================ BOTTOM
    private JPanel buildBottom() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JButton btnRefresh = makeBtn("Refresh Manual", PRIMARY);
        btnRefresh.addActionListener(e -> refresh());

        JLabel hint = new JLabel("  \u23F1 Auto-refresh setiap 5 detik");
        hint.setFont(new Font("Arial", Font.PLAIN, 11));
        hint.setForeground(TEXT_MUTED);

        panel.add(btnRefresh);
        panel.add(hint);
        return panel;
    }

    // ================================================================ REFRESH
    public void refresh() {
        List<Device> devices = rentalDAO.getAllDevices();
        tableModel.setRowCount(0);

        long tersedia = 0, disewa = 0, booked = 0;
        LocalDate today = LocalDate.now();
        LocalTime now   = LocalTime.now();

        for (Device d : devices) {
            String status  = d.isAvailable() ? "Tersedia" : "Disewa";
            String durasi  = "-";
            String nextBooking = "-";

            if (!d.isAvailable()) {
                disewa++;
                Long epochStart = activeSessionStartEpoch.get(d.getId());
                if (epochStart != null) {
                    long secs = (System.currentTimeMillis() - epochStart) / 1000;
                    long h = secs / 3600;
                    long m = (secs % 3600) / 60;
                    long s = secs % 60;
                    durasi = String.format("%02d:%02d:%02d", h, m, s);
                }
            } else {
                tersedia++;
            }

            // booking berikutnya hari ini
            try {
                List<Booking> bookings = bookingDAO.getBookingsByDate(today);
                Booking next = null;
                for (Booking b : bookings) {
                    if (!b.getDeviceId().equals(d.getId())) continue;
                    if (!b.getStatus().equals("pending") && !b.getStatus().equals("active")) continue;
                    if (b.getEndTime().isBefore(now)) continue;
                    if (next == null || b.getStartTime().isBefore(next.getStartTime())) next = b;
                }
                if (next != null) {
                    booked++;
                    nextBooking = next.getStartTime().format(TIME_FMT) + " - " +
                                  next.getEndTime().format(TIME_FMT) +
                                  "  (" + next.getCustomerName() + ")";
                }
            } catch (SQLException ignored) {}

            tableModel.addRow(new Object[]{
                d.getId(),
                d.getName(),
                status,
                durasi,
                nextBooking
            });
        }

        lblTersedia.setText(String.valueOf(tersedia));
        lblDisewa.setText(String.valueOf(disewa));
        lblBookedHariIni.setText(String.valueOf(booked));
        lblWaktuUpdate.setText("Terakhir diperbarui: " +
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    /**
     * Dipanggil RentalPanel setiap mulai/selesai sewa.
     * Key = device_id, Value = System.currentTimeMillis() saat sewa mulai.
     */
    public void updateSessionData(Map<String, Long> sessionStartEpochs) {
        this.activeSessionStartEpoch = new HashMap<>(sessionStartEpochs);
    }

    public void stopAutoRefresh() {
        if (autoRefreshTimer != null) autoRefreshTimer.stop();
    }

    // ================================================================ HELPERS
    private JPanel makeCard(String label, JLabel valLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        valLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valLabel.setForeground(accent);
        card.add(lbl,      BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
