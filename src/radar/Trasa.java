package radar;

import java.util.LinkedList;
import java.util.Random;

public class Trasa {


	LinkedList<Odcinek> odcinki = new LinkedList<Odcinek>();
	private int wysokosc;
	int indeksOdcinka = 0;

	private static int MIN_LICZBA_ODCINKOW = 4;
	private static int MAX_LICZBA_ODCINKOW = 5;

	public Trasa(LinkedList<Odcinek> odcinki, int wysokosc) {
		this.odcinki = odcinki;
		this.wysokosc = wysokosc;
	}

	public Trasa(Trasa trasa) {               //Tymczasowy, probny konstruktor kopiujacy
		this.odcinki = trasa.odcinki;
		this.wysokosc = trasa.wysokosc;
	}

	public static Trasa wygenerujLosowaTrase(int minPredkosc, int maxPredkosc, int minWysokosc, int maxWysokosc) {
		LinkedList<Odcinek> odcinki = new LinkedList<Odcinek>();
		Punkt p1 = null, p2 = null;
		Random random = new Random();

		int liczbaOdcninkow = random.nextInt(MAX_LICZBA_ODCINKOW - MIN_LICZBA_ODCINKOW) + MIN_LICZBA_ODCINKOW;
		int liczbaPunktow = liczbaOdcninkow + 1;
		int predkosc;

		for (int i = 0; i < liczbaPunktow; i++) {
			predkosc = random.nextInt(maxPredkosc - minPredkosc) + minPredkosc;

			if (i == 0) {
				p1 = new Punkt(Punkt.wygenerujLosowyPunkt());
				p2 = new Punkt(Punkt.wygenerujLosowyPunkt());

			} else {
				p1 = new Punkt(p2);
				p2 = new Punkt(Punkt.wygenerujLosowyPunkt());
			}

			double stosunek = ((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
			double kierunek = Math.atan(stosunek) * (180 / Math.PI);
			if ((p2.getY() > p1.getY()) && (p2.getX() < p1.getX())) kierunek -= 180;
			if ((p2.getY() < p1.getY()) && (p2.getX() < p1.getX())) kierunek -= 180;


			System.out.println(p1.getX() + " " + p1.getY() + " " + p2.getX() + " " + p2.getY());

			odcinki.add(
					new Odcinek(p1, p2, predkosc, (int) kierunek)
			);
		}

		int wysokosc = random.nextInt(maxWysokosc - minWysokosc) + minWysokosc;

		return new Trasa(odcinki, wysokosc);
	}

	public void zmienWspolrzednePunkuTrasy(int indexPunku, Punkt nowyPunkt) {   //Patrz: Radar -> wygenerujMouseAdapter -> mousePressed
		if (indexPunku == (getOdcinki().size())) {
			odcinki.get(getOdcinki().size() - 1).setP2(nowyPunkt);
		} else {
			odcinki.get(indexPunku - 1).setP2(nowyPunkt);
			odcinki.get(indexPunku).setP1(nowyPunkt);
		}
	}

	/**
	 * Zwraca aktulane wspolrzedne statku
	 * x = dx + x0
	 * y = dy + y0,
	 * gdzie x0 i y0 to poprzednie wspolrzedne statku (sprzed sekundy)
	 */


	public Punkt obliczAktualneWspolrzedneStatku(Punkt wspolrzedne) {
		Punkt p1 = odcinki.get(indeksOdcinka).getP1();
		Punkt p2 = odcinki.get(indeksOdcinka).getP2();


		int predkosc = odcinki.get(indeksOdcinka).getPredkosc();
		double kierunek = odcinki.get(indeksOdcinka).getKierunek();

		double x = obliczDeltaX(predkosc, kierunek) + wspolrzedne.getX();
		double y = obliczY(x, p1, p2);

		while ((x > p1.getX() && x > p2.getX()) || (x < p1.getX() && x < p2.getX()) ||
				(y > p1.getY() && y > p2.getY()) || (y < p1.getY() && y < p2.getY())) { //Sprawdzamy wyjscie poza obecny odcinek

			if (++indeksOdcinka == (odcinki.size())) {
				return null;
			}
			double dx = wspolrzedne.getX() - x;
			double dy = wspolrzedne.getY() - y;
			double len = Math.sqrt(dx * dx + dy * dy);
			dx = p2.getX() - x;
			dy = p2.getY() - y;
			kierunek = odcinki.get(indeksOdcinka).getKierunek();
			predkosc = odcinki.get(indeksOdcinka).getPredkosc();
			double dlen = Math.sqrt(dx * dx + dy * dy);
			x = p2.getX() + dlen / len * obliczDeltaX(predkosc, kierunek);

			p1 = odcinki.get(indeksOdcinka).getP1();
			p2 = odcinki.get(indeksOdcinka).getP2();

			y = obliczY(x, p1, p2);

		}//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		return new Punkt(x, y);
	}

	/**
	 * Zwraca zmiane x-owej skladowej przemieszczenia w ciagu 1s
	 * dx = V/3600 * cos(α) * 1s
	 */
	private double obliczDeltaX(int predkosc, double kierunek) {
		return ((predkosc * Math.cos(zamienStopnieNaRadiany(kierunek)) * 1) / 35);
	}


	private double obliczY(double x, Punkt p1, Punkt p2) {  //Funkcja liniowa od x rozpieta na p1 i p2
		return ((p1.getY() - p2.getY()) / (p1.getX() - p2.getX()) * (x - p1.getX()) + p1.getY());
	}

	private double zamienStopnieNaRadiany(double kierunek) {
		return kierunek * (Math.PI / 180);
	}

	public LinkedList<Odcinek> getOdcinki() {
		return odcinki;
	}

	/**
	 * Zwraca n-ty punkt trasy, pierwszy punkt ma numer 0
	 */
	public Punkt getPunktTrasy(int numerPunktu) {
		Odcinek odcinek = odcinki.get(numerPunktu / 2);

		if (numerPunktu / 2 == 0) {
			if (numerPunktu % 2 == 0)
				return odcinek.getP1();
			else
				return odcinek.getP2();
		} else {
			odcinek = odcinki.get(numerPunktu - 1);
			return odcinek.getP2();
		}
	}

	public int getWysokosc() {
		return wysokosc;
	}
}