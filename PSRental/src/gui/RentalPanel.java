package gui;

import database.RentalDAO;
import exception.RentalException;
import model.Device;
import model.RentalSession;
import thread.TimerThread;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentalPanel extends JPanel {

    private static final Color PRIMARY    = new Color(41, 128, 185);
    private static final Color SUCCESS    = new Color(39, 174, 96);
    private static final Color DANGER     = new Color(192, 57, 43);
    private static final Color BG         = Color.WHITE;
    private static final Color BG_ROW     = new Color(248, 249, 250);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT       = new Color(50, 50, 50);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private RentalDAO dao = new RentalDAO();
    private List<Device> devices;
    private Map<String, RentalSession> activeSessions = new HashMap<>();
    private Map<String, TimerThread>   activeTimers   = new HashMap<>();

    private JTable deviceTable;
    private DefaultTableModel tableModel;
    private JTextField txtCustomer;
    private JLabel lblTotal, lblDisewa, lblTersedia;

    public RentalPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildForm(),    BorderLayout.NORTH);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    // ------------------------------------------------------------------ FORM
    private JPanel buildForm() {
        JPanel wrap = new JPanel(new BorderLayout(0, 10));
        wrap.setBackground(BG);

        JPanel stats = new JPanel(new GridLayout(1, 3, 10, 0));
        stats.setBackground(BG);
        stats.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblTotal    = new JLabel("0", SwingConstants.CENTER);
        lblDisewa   = new JLabel("0", SwingConstants.CENTER);
        lblTersedia = new JLabel("0", SwingConstants.CENTER);

        stats.add(makeCard("Total PS",  lblTotal,    new Color(52, 73, 94)));
        stats.add(makeCard("Disewa",    lblDisewa,   DANGER));
        stats.add(makeCard("Tersedia",  lblTersedia, SUCCESS));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        form.setBackground(BG);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));

        JLabel lbl = new JLabel("Nama Pelanggan:");
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(TEXT);

        txtCustomer = new JTextField(22);
        txtCustomer.setFont(new Font("Arial", Font.PLAIN, 13));
        txtCustomer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        form.add(lbl);
        form.add(txtCustomer);

        wrap.add(stats, BorderLayout.NORTH);
        wrap.add(form,  BorderLayout.CENTER);
        return wrap;
    }

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

    // ----------------------------------------------------------------- TABLE
    private JScrollPane buildTable() {
        String[] cols = {"ID", "Perangkat", "Pelanggan", "Status", "Durasi"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        deviceTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? BG : BG_ROW);
                    c.setForeground(TEXT);
                }
                return c;
            }
        };

        deviceTable.setFont(new Font("Arial", Font.PLAIN, 13));
        deviceTable.setRowHeight(34);
        deviceTable.setShowVerticalLines(false);
        deviceTable.setGridColor(new Color(235, 235, 235));
        deviceTable.setSelectionBackground(new Color(213, 234, 254));
        deviceTable.setSelectionForeground(TEXT);
        deviceTable.setFocusable(false);
        deviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = deviceTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(TEXT_MUTED);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setReorderingAllowed(false);

        deviceTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        deviceTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        deviceTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        deviceTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        deviceTable.getColumnModel().getColumn(4).setPreferredWidth(110);

        deviceTable.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        deviceTable.getColumnModel().getColumn(4).setCellRenderer(new TimerRenderer());

        JScrollPane scroll = new JScrollPane(deviceTable);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(BG);

        refreshTable();
        return scroll;
    }

    // --------------------------------------------------------------- BUTTONS
    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JButton btnSewa    = makeBtn("Mulai Sewa",   PRIMARY);
        JButton btnSelesai = makeBtn("Selesai Sewa", DANGER);

        panel.add(btnSewa);
        panel.add(btnSelesai);

        btnSewa.addActionListener(e -> mulaiSewa());
        btnSelesai.addActionListener(e -> selesaiSewa());

        return panel;
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --------------------------------------------------------------- ACTIONS
    private void mulaiSewa() {
        try {
            int row = deviceTable.getSelectedRow();
            if (row < 0)
                throw new RentalException("Pilih perangkat PS terlebih dahulu!");

            String customer = txtCustomer.getText().trim();
            if (customer.isEmpty())
                throw new RentalException("Nama pelanggan wajib diisi!");

            Device device = devices.get(row);
            if (!device.isAvailable())
                throw new RentalException("PS ini sedang disewa!");

            device.setAvailable(false);
            dao.updateDeviceStatus(device.getId(), false);

            RentalSession session = new RentalSession(device, customer);
            activeSessions.put(device.getId(), session);

            tableModel.setValueAt(customer,             row, 2);
            tableModel.setValueAt("DISEWA:" + customer, row, 3);
            tableModel.setValueAt("00:00:00",            row, 4);

            TimerThread timer = new TimerThread(session.getStartTime(), tableModel, row);
            timer.start();
            activeTimers.put(device.getId(), timer);

            txtCustomer.setText("");
            updateStats();

            JOptionPane.showMessageDialog(this,
                "Sewa dimulai!\nPelanggan : " + customer +
                "\nPS        : " + device.getName(),
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (RentalException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void selesaiSewa() {
        try {
            int row = deviceTable.getSelectedRow();
            if (row < 0)
                throw new RentalException("Pilih PS yang ingin diselesaikan!");

            Device device = devices.get(row);
            RentalSession session = activeSessions.get(device.getId());
            if (session == null)
                throw new RentalException("PS ini tidak sedang disewa!");

            session.endSession();
            dao.saveRental(session);
            dao.updateDeviceStatus(device.getId(), true);

            TimerThread timer = activeTimers.get(device.getId());
            if (timer != null) timer.stopTimer();
            activeTimers.remove(device.getId());
            activeSessions.remove(device.getId());

            tableModel.setValueAt("-",        row, 2);
            tableModel.setValueAt("TERSEDIA", row, 3);
            tableModel.setValueAt("-",        row, 4);
            device.setAvailable(true);

            updateStats();
            JOptionPane.showMessageDialog(this,
                "Sewa selesai!\n" +
                "Pelanggan : " + session.getCustomerName() +
                "\nPS        : " + device.getName() +
                "\nDurasi    : " + session.getDurationMinutes() + " menit" +
                "\nTotal     : Rp " + String.format("%,.0f", session.getTotalCost()),
                "Selesai", JOptionPane.INFORMATION_MESSAGE);

        } catch (RentalException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    // -------------------------------------------------------------- HELPERS
    private void refreshTable() {
        devices = dao.getAllDevices();

        // hentikan timer untuk device yang tidak punya sesi aktif
        for (Device d : devices) {
            if (!activeSessions.containsKey(d.getId())) {
                TimerThread t = activeTimers.get(d.getId());
                if (t != null) {
                    t.stopTimer();
                    activeTimers.remove(d.getId());
                }
                if (!d.isAvailable()) {
                    d.setAvailable(true);
                    dao.updateDeviceStatus(d.getId(), true);
                }
            }
        }

        tableModel.setRowCount(0);
        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);
            RentalSession sesi = activeSessions.get(d.getId());
            tableModel.addRow(new Object[]{
                d.getId(),
                d.getName(),
                sesi != null ? sesi.getCustomerName() : "-",
                sesi != null ? "DISEWA:" + sesi.getCustomerName() : "TERSEDIA",
                sesi != null ? "00:00:00" : "-"
            });
            if (sesi != null) {
                TimerThread t = activeTimers.get(d.getId());
                if (t != null) t.setRowIndex(i);
            }
        }
        updateStats();
    }

    private void updateStats() {
        long disewa   = devices.stream().filter(d -> !d.isAvailable()).count();
        long tersedia = devices.stream().filter(Device::isAvailable).count();
        lblTotal.setText(String.valueOf(devices.size()));
        lblDisewa.setText(String.valueOf(disewa));
        lblTersedia.setText(String.valueOf(tersedia));
    }

    // ------------------------------------------------------------ RENDERERS
    static class StatusRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                t, val, sel, foc, row, col);
            String v = val == null ? "" : val.toString();
            if (v.startsWith("DISEWA")) {
                lbl.setText("Disewa");
                lbl.setForeground(new Color(192, 57, 43));
            } else {
                lbl.setText("Tersedia");
                lbl.setForeground(new Color(39, 174, 96));
            }
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            if (!sel) lbl.setBackground(
                row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
            return lbl;
        }
    }

    static class TimerRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int row, int col) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                t, val, sel, foc, row, col);
            lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
            lbl.setForeground(val != null && !val.toString().equals("-")
                ? new Color(211, 84, 0)
                : new Color(150, 150, 150));
            if (!sel) lbl.setBackground(
                row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
            return lbl;
        }
    }
}