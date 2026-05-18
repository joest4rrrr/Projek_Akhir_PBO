
public class MainPlaylist {

    public static void main(String[] args) {

        Lagu lagu1 = new Lagu("Monolog", "Pamungkas");
        Lagu lagu2 = new Lagu("Sempurna", "Andra and The Backbone");
        Lagu lagu3 = new Lagu("Menjaga Hati", "Yovie & Nuno");

        Playlist playlist = new Playlist();

        playlist.tambahLagu(lagu1);
        playlist.tambahLagu(lagu2);
        playlist.tambahLagu(lagu3);

        playlist.tampilkanPlaylist();

        PlayerService player = new PlayerService();

        player.putar(playlist);
    }
}