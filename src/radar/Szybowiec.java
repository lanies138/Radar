package radar;

import javax.swing.ImageIcon;

public class Szybowiec extends Statek{

	private static final int MIN_PREDKOSC_KMH = 70,
			MAX_PREDKOSC_KMH = 300,
			MIN_WYSOKOSC_M = 100,
			MAX_WYSOKOSC_M = 1000;

	public Szybowiec() {
		symbol = new ImageIcon("img/szybowiec.png").getImage();
	}

    public Szybowiec(Trasa trasa) {
			this.trasa = new Trasa(trasa);
			this.wspolrzedne = new Punkt(
					trasa.getPunktTrasy(0)          /*Zakladam, ze poczatkowymi wpolrzedymi statku beda
                                                         wspolrzedne pierwszego punkt jego trasy*/
			);
			symbol = new ImageIcon("img/szybowiec.png").getImage();
		}

	public static Szybowiec wygenerujLosowySzybowiec () {
		Trasa trasa = Trasa.wygenerujLosowaTrase(
				MIN_PREDKOSC_KMH,
				MAX_PREDKOSC_KMH,
				MIN_WYSOKOSC_M,
				MAX_WYSOKOSC_M
		);
		return new Szybowiec(trasa);
	}
}
