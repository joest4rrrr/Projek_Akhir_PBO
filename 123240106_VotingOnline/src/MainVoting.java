
public class MainVoting {

    public static void main(String[] args) {

        User user1 = new User("Jeje");
        User user2 = new User("Budi");
        User user3 = new User("Salsa");

        Kandidat kandidat1 = new Kandidat("Bahlil");
        Kandidat kandidat2 = new Kandidat("Purbaya");

        VoteService voteService = new VoteService();

        voteService.voting(user1, kandidat1);
        voteService.voting(user2, kandidat2);
        voteService.voting(user3, kandidat1);

        Kandidat[] daftarKandidat = {
            kandidat1,
            kandidat2
        };

        voteService.tampilkanHasil(daftarKandidat);
    }
}