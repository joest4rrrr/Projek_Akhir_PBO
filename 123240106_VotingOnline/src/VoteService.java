
public class VoteService {

    public void voting(User user, Kandidat kandidat) {

        kandidat.tambahSuara();

        System.out.println(
                user.getNama()
                + " memilih "
                + kandidat.getNama()
        );
    }

    public void tampilkanHasil(Kandidat[] kandidatList) {

        System.out.println("\n===== HASIL VOTING =====");

        for (Kandidat kandidat : kandidatList) {

            System.out.println(
                    kandidat.getNama()
                    + " : "
                    + kandidat.getJumlahSuara()
                    + " suara"
            );
        }
    }
}