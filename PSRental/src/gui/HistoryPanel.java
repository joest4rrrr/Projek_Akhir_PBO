package gui;

import database.RentalDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {

    private RentalDAO dao = new RentalDAO();
    private DefaultTableModel tableModel;
    private JLabel lblTotalPenghasilan;
    private JLabel lblTotalTransaksi;
    private JTable table;

    public HistoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildStats(),   BorderLayout.NORTH);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        loadHistory();
    }

    // ------------------------------------------------------------ STATS ATAS
    private JPanel buildStats() {
        JPanel wrap = new JPanel(new GridLayout(1, 2, 10, 0));
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblTotalTransaksi   = new JLabel("0", SwingConstants.CENTER);
        lblTotalPenghasilan = new JLabel("Rp 0", SwingConstants.CENTER);

        wrap.add(makeCard("Total Transaksi",   lblTotalTransaksi,  new Color(41, 128, 185)));
        wrap.add(makeCard("Total Penghasilan", lblTotalPenghasilan, new Color(39, 174, 96)));

        return wrap;
    }

    private JPanel makeCard(String label, JLabel valLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(new Color(130, 130, 130));
        valLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valLabel.setForeground(accent);
        card.add(lbl,      BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    // ------------------------------------------------------------- TABEL
    private JScrollPane buildTable() {
        // kolom "No" menyimpan id dari database (untuk keperluan hapus)
        String[] columns = {"No", "Perangkat", "Pelanggan", "Durasi", "Total Biaya"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 235, 235));
        table.setSelectionBackground(new Color(213, 234, 254));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.getTableHeader().setForeground(new Color(130, 130, 130));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return scroll;
    }

    // ------------------------------------------------------------ TOMBOL
    private JPanel buildButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JButton btnRefresh   = makeBtn("Muat Ulang",  new Color(41, 128, 185));
        JButton btnHapusBaris = makeBtn("Hapus Dipilih", new Color(211, 84, 0));
        JButton btnHapusSemua = makeBtn("Hapus Semua", new Color(192, 57, 43));

        panel.add(btnRefresh);
        panel.add(btnHapusBaris);
        panel.add(btnHapusSemua);

        // ---- action refresh ----
        btnRefresh.addActionListener(e -> loadHistory());

        // ---- action hapus baris yang dipilih ----
        btnHapusBaris.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this,
                    "Pilih riwayat yang ingin dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Hapus riwayat ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (konfirmasi == JOptionPane.YES_OPTION) {
                // ambil id dari kolom pertama
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                dao.deleteRentalById(id);
                loadHistory();
                JOptionPane.showMessageDialog(this, "Riwayat berhasil dihapus.");
            }
        });

        // ---- action hapus semua ----
        btnHapusSemua.addActionListener(e -> {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada riwayat untuk dihapus.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Hapus SEMUA riwayat? Tindakan ini tidak bisa dibatalkan!",
                "Konfirmasi", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (konfirmasi == JOptionPane.YES_OPTION) {
                dao.deleteAllRentals();
                loadHistory();
                JOptionPane.showMessageDialog(this, "Semua riwayat berhasil dihapus.");
            }
        });

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

    // ------------------------------------------------------------ LOAD DATA
    private void loadHistory() {
        tableModel.setRowCount(0);
        List<String[]> data = dao.getRentalHistory();
        double totalPenghasilan = 0;

        for (String[] row : data) {
            tableModel.addRow(row);
            try {
                String angka = row[4].replace("Rp ", "").replace(",", "").trim();
                totalPenghasilan += Double.parseDouble(angka);
            } catch (NumberFormatException ignored) {}
        }

        lblTotalTransaksi.setText(String.valueOf(data.size()));
        lblTotalPenghasilan.setText("Rp " + String.format("%,.0f", totalPenghasilan));
    }
}