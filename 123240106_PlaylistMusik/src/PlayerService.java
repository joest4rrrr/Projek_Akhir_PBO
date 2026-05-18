
public class PlayerService {

    public void putar(Playlist playlist) {

        System.out.println("\n===== MEMUTAR PLAYLIST =====");

        for (Lagu lagu : playlist.getDaftarLagu()) {

            System.out.println(
                    "Memutar : "
                    + lagu.getJudul()
                    + " - "
                    + lagu.getPenyanyi()
            );
        }
    }
}