package radar;

import javax.swing.ImageIcon;

public class Smiglowiec extends Statek{

	private static final int MIN_PREDKOSC_KMH = 30,
			MAX_PREDKOSC_KMH = 300,
			MIN_WYSOKOSC_M = 80,
			MAX_WYSOKOSC_M = 3000;

	public Smiglowiec() {
		symbol = new ImageIcon("img/smiglowiec.png").getImage();
	}

	public Smiglowiec(Trasa trasa) {
		this.trasa = new Trasa(trasa);
		this.wspolrzedne = new Punkt(
				trasa.getPunktTrasy(0)          /*Zakladam, ze poczatkowymi wpolrzedymi statku beda
                                                         wspolrzedne pierwszego punkt jego trasy*/
		);
		symbol = new ImageIcon("img/smiglowiec.png").getImage();
	}

	public static Smiglowiec wygenerujLosowySmiglowiec () {
		Trasa trasa = Trasa.wygenerujLosowaTrase(
				MIN_PREDKOSC_KMH,
				MAX_PREDKOSC_KMH,
				MIN_WYSOKOSC_M,
				MAX_WYSOKOSC_M
		);
		return new Smiglowiec(trasa);
	}

}
