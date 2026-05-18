
import java.util.ArrayList;

public class Playlist {

    private ArrayList<Lagu> daftarLagu;

    public Playlist() {
        daftarLagu = new ArrayList<>();
    }

    public void tambahLagu(Lagu lagu) {
        daftarLagu.add(lagu);
    }

    public ArrayList<Lagu> getDaftarLagu() {
        return daftarLagu;
    }

    public void tampilkanPlaylist() {

        System.out.println("===== PLAYLIST MUSIK =====");

        for (int i = 0; i < daftarLagu.size(); i++) {

            Lagu lagu = daftarLagu.get(i);

            System.out.println(
                    (i + 1) + ". "
                    + lagu.getJudul()
                    + " - "
                    + lagu.getPenyanyi()
            );
        }
    }
}