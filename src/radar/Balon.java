package radar;

import javax.swing.*;

public class Balon extends Statek {

	private static final int MIN_PREDKOSC_KMH = 30,
			MAX_PREDKOSC_KMH = 100,
			MIN_WYSOKOSC_M = 100,
			MAX_WYSOKOSC_M = 300;

	public Balon() {
		symbol = new ImageIcon("img/balon.png").getImage();
	}

    public Balon(Trasa trasa) {
		this.trasa = new Trasa(trasa);
		this.wspolrzedne = new Punkt(
				trasa.getPunktTrasy(0)          /*Zakladam, ze poczatkowymi wpolrzedymi statku beda
                                                         wspolrzedne pierwszego punkt jego trasy*/
		);
		symbol = new ImageIcon("img/balon.png").getImage();
	}

	public static Balon wygenerujLosowyBalon () {
		Trasa trasa = Trasa.wygenerujLosowaTrase(
				MIN_PREDKOSC_KMH,
				MAX_PREDKOSC_KMH,
				MIN_WYSOKOSC_M,
				MAX_WYSOKOSC_M
		);
		return new Balon(trasa);
	}
}
