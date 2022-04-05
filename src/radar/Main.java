package radar;


public class Main {

    public static void main(String[] args) {

        Radar radar = new Radar();
        radar.dodajStatek(
                Statek.wygenerujLosowyStatek()
        );
        /*radar.dodajStatek(
                Statek.wygenerujLosowyStatek()
        );
        radar.dodajStatek(
                Statek.wygenerujLosowyStatek()
        );*/
    }
}
