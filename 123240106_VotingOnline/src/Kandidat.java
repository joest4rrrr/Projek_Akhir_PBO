 
public class Kandidat {

    private String nama;
    private int jumlahSuara;

    public Kandidat(String nama) {
        this.nama = nama;
        this.jumlahSuara = 0;
    }

    public String getNama() {
        return nama;
    }

    public int getJumlahSuara() {
        return jumlahSuara;
    }

    public void tambahSuara() {
        jumlahSuara++;
    }
}