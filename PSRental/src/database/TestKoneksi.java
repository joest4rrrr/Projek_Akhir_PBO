package database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestKoneksi {

    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Koneksi berhasil: " + conn);
            conn.close();
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }
}