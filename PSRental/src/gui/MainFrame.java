package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Sistem Penyewaan PlayStation");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MonitoringPanel monitoringPanel = new MonitoringPanel();
        RentalPanel     rentalPanel     = new RentalPanel(monitoringPanel);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("Sewa PS",    rentalPanel);
        tabs.addTab("Monitoring", monitoringPanel);
        tabs.addTab("Booking",    new BookingPanel());
        tabs.addTab("Riwayat",    new HistoryPanel());

        add(tabs);
        setVisible(true);
    }
}
