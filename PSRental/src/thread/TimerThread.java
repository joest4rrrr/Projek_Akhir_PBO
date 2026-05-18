package thread;

import java.time.Duration;
import java.time.LocalDateTime;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class TimerThread extends Thread {

    private LocalDateTime startTime;
    private DefaultTableModel tableModel;
    private int rowIndex;
    private volatile boolean running = true;

    public TimerThread(LocalDateTime startTime, DefaultTableModel tableModel, int rowIndex) {
        this.startTime  = startTime;
        this.tableModel = tableModel;
        this.rowIndex   = rowIndex;
        setDaemon(true); // otomatis mati kalau program ditutup
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);

                if (!running) break; // cek lagi setelah sleep

                long seconds = Duration.between(startTime, LocalDateTime.now()).getSeconds();
                long h = seconds / 3600;
                long m = (seconds % 3600) / 60;
                long s = seconds % 60;
                String waktu = String.format("%02d:%02d:%02d", h, m, s);

                int row = rowIndex;
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (running && row < tableModel.getRowCount()) {
                            tableModel.setValueAt(waktu, row, 4);
                        }
                    } catch (Exception ignored) {}
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopTimer() {
        running = false;
        // langsung set kolom durasi jadi "-" saat stop
        int row = rowIndex;
        SwingUtilities.invokeLater(() -> {
            try {
                if (row < tableModel.getRowCount()) {
                    tableModel.setValueAt("-", row, 4);
                }
            } catch (Exception ignored) {}
        });
    }
}