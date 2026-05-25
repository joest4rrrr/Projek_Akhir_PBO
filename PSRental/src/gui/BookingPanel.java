package gui;

import database.BookingDAO;
import database.RentalDAO;
import model.Booking;
import model.Device;
import model.RentalSession;
import model.PS3;
import model.PS4;
import model.PS5;
import java.time.LocalDate;
import java.time.LocalDateTime;
import model.RentalSession;
import model.PS3;
import model.PS4;
import model.PS5;

import java.time.LocalDateTime;
import java.time.Duration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingPanel extends JPanel {

    // ---- warna konsisten dengan RentalPanel ----
    private static final Color PRIMARY    = new Color(41, 128, 185);
    private static final Color SUCCESS    = new Color(39, 174, 96);
    private static final Color DANGER     = new Color(192, 57, 43);
    private static final Color WARNING    = new Color(211, 84, 0);
    private static final Color BG         = Color.WHITE;
    private static final Color BG_ROW     = new Color(248, 249, 250);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT       = new Color(50, 50, 50);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private BookingDAO bookingDAO = new BookingDAO();
    private RentalDAO  rentalDAO  = new RentalDAO();

    private JComboBox<String> cbDevice;
    private JTextField txtCustomer, txtStart, txtEnd;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalBooking, lblPending, lblDone;

    public BookingPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildStats(),   BorderLayout.NORTH);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildBottom(),  BorderLayout.SOUTH);

        loadBookings();
    }

    // ================================================================ STATS
    private JPanel buildStats() {
        JPanel wrap = new JPanel(new BorderLayout(0, 10));
        wrap.setBackground(BG);

        // -- kartu statistik --
        JPanel stats = new JPanel(new GridLayout(1, 3, 10, 0));
        stats.setBackground(BG);
        stats.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblTotalBooking = new JLabel("0", SwingConstants.CENTER);
        lblPending      = new JLabel("0", SwingConstants.CENTER);
        lblDone         = new JLabel("0", SwingConstants.CENTER);

        stats.add(makeCard("Total Booking", lblTotalBooking, new Color(52, 73, 94)));
        stats.add(makeCard("Menunggu",      lblPending,      WARNING));
        stats.add(makeCard("Selesai/Batal", lblDone,         SUCCESS));

        // -- form input: baris 1 (field input) --
        JPanel formFields = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        formFields.setBackground(BG);

        // dropdown device — isi dari DB
        cbDevice = new JComboBox<>();
        cbDevice.setFont(new Font("Arial", Font.PLAIN, 13));
        cbDevice.setPreferredSize(new Dimension(130, 30));
        List<Device> devices = rentalDAO.getAllDevices();
        for (Device d : devices) cbDevice.addItem(d.getId());

        txtCustomer = makeField(18, "Nama pelanggan");
        txtStart    = makeField(7,  "HH:mm");
        txtEnd      = makeField(7,  "HH:mm");

        formFields.add(label("Perangkat:"));  formFields.add(cbDevice);
        formFields.add(label("Pelanggan:"));  formFields.add(txtCustomer);
        formFields.add(label("Mulai:"));      formFields.add(txtStart);
        formFields.add(label("Selesai:"));    formFields.add(txtEnd);

        // -- form input: baris 2 (tombol) --
        JPanel formBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        formBtn.setBackground(BG);

        JButton btnTambah = makeBtn("+ Tambah Booking", PRIMARY);
        btnTambah.setFont(new Font("Arial", Font.BOLD, 13));
        btnTambah.setPreferredSize(new Dimension(200, 36));
        btnTambah.addActionListener(e -> tambahBooking());
        formBtn.add(btnTambah);

        JLabel hint = new JLabel("  Isi semua field di atas lalu klik tombol ini");
        hint.setFont(new Font("Arial", Font.ITALIC, 11));
        hint.setForeground(TEXT_MUTED);
        formBtn.add(hint);

        // -- gabungkan field + tombol dalam satu panel --
        JPanel form = new JPanel(new BorderLayout());
        form.setBackground(BG);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(6, 0, 6, 0)
        ));
        form.add(formFields, BorderLayout.NORTH);
        form.add(formBtn,    BorderLayout.SOUTH);

        wrap.add(stats, BorderLayout.NORTH);
        wrap.add(form,  BorderLayout.CENTER);
        return wrap;
    }

    // ================================================================ TABLE
    private JScrollPane buildTable() {
        String[] cols = {"ID", "Perangkat", "Pelanggan", "Mulai", "Selesai", "Status"};
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
        table.setRowHeight(32);
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

        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        // renderer warna status
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String v = val == null ? "" : val.toString().toLowerCase();
                switch (v) {
                    case "pending":   lbl.setForeground(WARNING);  lbl.setText("Menunggu");  break;
                    case "active":    lbl.setForeground(PRIMARY);  lbl.setText("Aktif");     break;
                    case "done":      lbl.setForeground(SUCCESS);  lbl.setText("Selesai");   break;
                    case "cancelled": lbl.setForeground(DANGER);   lbl.setText("Dibatal");   break;
                    default:          lbl.setForeground(TEXT);
                }
                lbl.setFont(new Font("Arial", Font.BOLD, 12));
                if (!sel) lbl.setBackground(row % 2 == 0 ? BG : BG_ROW);
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(BG);
        return scroll;
    }

    // ================================================================ BOTTOM BUTTONS
    private JPanel buildBottom() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JButton btnRefresh  = makeBtn("Muat Ulang",    PRIMARY);
        JButton btnBatal    = makeBtn("Batalkan",       DANGER);
        JButton btnSelesai  = makeBtn("Tandai Selesai", SUCCESS);

        panel.add(btnRefresh);
        panel.add(btnBatal);
        panel.add(btnSelesai);

        btnRefresh.addActionListener(e -> loadBookings());

        btnBatal.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { warn("Pilih booking yang ingin dibatalkan!"); return; }
            int id     = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String sts = tableModel.getValueAt(row, 5).toString();
            if (sts.equals("done") || sts.equals("cancelled")) {
                warn("Booking ini sudah selesai / sudah dibatalkan."); return;
            }
            if (confirm("Batalkan booking ini?")) {
                try {
                    bookingDAO.updateStatus(id, "cancelled");
                    loadBookings();
                    info("Booking berhasil dibatalkan.");
                } catch (SQLException ex) { errDB(ex); }
            }
        });

        btnSelesai.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { warn("Pilih booking yang ingin ditandai selesai!"); return; }

            int    id       = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String deviceId = tableModel.getValueAt(row, 1).toString();
            String customer = tableModel.getValueAt(row, 2).toString();
            String startStr = tableModel.getValueAt(row, 3).toString();
            String endStr   = tableModel.getValueAt(row, 4).toString();
            String sts      = tableModel.getValueAt(row, 5).toString();

            if (!sts.equals("pending") && !sts.equals("active")) {
                warn("Hanya booking berstatus Menunggu/Aktif yang bisa diselesaikan."); return;
            }

            if (confirm("Tandai booking ini sebagai selesai?\nData akan masuk ke Riwayat.")) {
                try {
                    // 1. Update status booking -> done
                    bookingDAO.updateStatus(id, "done");

                    // 2. Buat Device sesuai tipe
                    Device device;
                    if (deviceId.startsWith("PS3"))      device = new PS3(deviceId);
                    else if (deviceId.startsWith("PS4")) device = new PS4(deviceId);
                    else                                  device = new PS5(deviceId);

                    // 3. Hitung durasi dari jam booking
                    LocalTime tStart = LocalTime.parse(startStr, TIME_FMT);
                    LocalTime tEnd   = LocalTime.parse(endStr,   TIME_FMT);
                    LocalDate today  = LocalDate.now();
                    LocalDateTime startDT = LocalDateTime.of(today, tStart);
                    LocalDateTime endDT   = LocalDateTime.of(today, tEnd);
                    long diffMinutes  = java.time.Duration.between(startDT, endDT).toMinutes();
                    int  durMenit     = (int)(diffMinutes <= 0 ? 1 : diffMinutes);
                    double totalCost  = device.calculateCost(durMenit);

                    // 4. Simpan ke tabel rentals
                    RentalSession session = new RentalSession(device, customer);
                    session.forceEnd(startDT, endDT, durMenit, totalCost);
                    rentalDAO.saveRental(session);

                    loadBookings();
                    info("Booking selesai!\n" +
                         "Pelanggan : " + customer +
                         "\nPS        : " + deviceId +
                         "\nDurasi    : " + durMenit + " menit" +
                         "\nTotal     : Rp " + String.format("%,.0f", totalCost) +
                         "\n\nData sudah masuk ke tab Riwayat.");

                } catch (SQLException ex) { errDB(ex); }
            }
        });

        return panel;
    }

    // ================================================================ ACTIONS

    private void tambahBooking() {
        // --- validasi input ---
        String deviceId  = cbDevice.getSelectedItem() == null ? "" :
                           cbDevice.getSelectedItem().toString().trim();
        String customer  = txtCustomer.getText().trim();
        String startStr  = txtStart.getText().trim();
        String endStr    = txtEnd.getText().trim();

        if (customer.isEmpty()) { warn("Nama pelanggan wajib diisi!"); return; }
        if (startStr.isEmpty() || endStr.isEmpty()) { warn("Waktu mulai dan selesai wajib diisi! (format HH:mm)"); return; }

        LocalTime start, end;
        try {
            start = LocalTime.parse(startStr, TIME_FMT);
            end   = LocalTime.parse(endStr,   TIME_FMT);
        } catch (DateTimeParseException ex) {
            warn("Format waktu salah! Gunakan format HH:mm (contoh: 14:30)");
            return;
        }

        if (!end.isAfter(start)) { warn("Waktu selesai harus lebih dari waktu mulai!"); return; }

        LocalDate today = LocalDate.now();

        // --- cek conflict ---
        try {
            if (bookingDAO.isSlotConflict(deviceId, today, start, end)) {
                warn("Slot waktu ini sudah dibooking untuk " + deviceId + "!\n" +
                     "Pilih jam yang berbeda.");
                return;
            }

            Booking booking = new Booking(deviceId, customer, today, start, end);
            int newId = bookingDAO.saveBooking(booking);

            if (newId > 0) {
                txtCustomer.setText("");
                txtStart.setText("");
                txtEnd.setText("");
                loadBookings();
                info("Booking berhasil ditambahkan!\n" +
                     "ID     : " + newId +
                     "\nPS     : " + deviceId +
                     "\nNama   : " + customer +
                     "\nJadwal : " + startStr + " - " + endStr);
            }
        } catch (SQLException ex) { errDB(ex); }
    }

    // ================================================================ LOAD DATA
    private void loadBookings() {
        tableModel.setRowCount(0);
        int pending = 0, done = 0;
        try {
            List<Booking> list = bookingDAO.getBookingsByDate(LocalDate.now());
            for (Booking b : list) {
                tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getDeviceId(),
                    b.getCustomerName(),
                    b.getStartTime().format(TIME_FMT),
                    b.getEndTime().format(TIME_FMT),
                    b.getStatus()
                });
                if (b.getStatus().equals("pending") || b.getStatus().equals("active")) pending++;
                else done++;
            }
            lblTotalBooking.setText(String.valueOf(list.size()));
            lblPending.setText(String.valueOf(pending));
            lblDone.setText(String.valueOf(done));
        } catch (SQLException ex) { errDB(ex); }
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

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(TEXT);
        return l;
    }

    private JTextField makeField(int cols, String tooltip) {
        JTextField tf = new JTextField(cols);
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setToolTipText(tooltip);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
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

    private void warn(String msg)    { JOptionPane.showMessageDialog(this, msg, "Peringatan",  JOptionPane.WARNING_MESSAGE); }
    private void info(String msg)    { JOptionPane.showMessageDialog(this, msg, "Info",         JOptionPane.INFORMATION_MESSAGE); }
    private void errDB(Exception ex) { JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Konfirmasi",
               JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Dipanggil dari luar (misal MonitoringPanel refresh)
    public void refresh() { loadBookings(); }
}
