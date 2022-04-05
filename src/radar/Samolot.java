package radar;

import javax.swing.*;

public class Samolot extends Statek {

    private static final int MIN_PREDKOSC_KMH = 700,
            MAX_PREDKOSC_KMH = 1000,
            MIN_WYSOKOSC_M = 10,
            MAX_WYSOKOSC_M = 50;

    public Samolot() {
		symbol = new ImageIcon("img/samolot.png").getImage();
	}

	public Samolot(Trasa trasa) {
        this.trasa = new Trasa(trasa);
        this.wspolrzedne = new Punkt(
                trasa.getPunktTrasy(0)          /*Zakladam, ze poczatkowymi wpolrzedymi statku beda
                                                         wspolrzedne pierwszego punkt jego trasy*/
        );
        symbol = new ImageIcon("img/samolot.png").getImage();
    }

    public static Samolot wygenerujLosowySamolot () {
	    Trasa trasa = Trasa.wygenerujLosowaTrase(
                MIN_PREDKOSC_KMH,
                MAX_PREDKOSC_KMH,
                MIN_WYSOKOSC_M,
                MAX_WYSOKOSC_M
        );
	    return new Samolot(trasa);
    }

}
