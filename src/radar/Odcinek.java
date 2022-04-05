package radar;

public class Odcinek {
	private Punkt p1,p2;
	private int predkosc;
	private double kierunek;
	
	public Odcinek() {}

	public Odcinek(Punkt p1, Punkt p2, int predkosc, double kierunek) {
		this.p1 = p1;
		this.p2 = p2;
		this.predkosc = predkosc;
		this.kierunek = kierunek;
	}

	public void setP1(Punkt p1) {
		this.p1 = p1;
	}

	public void setP2(Punkt p2) {
		this.p2 = p2;
	}

	public Punkt getP1() {
		return p1;
	}

	public Punkt getP2() {
		return p2;
	}

	public int getPredkosc() {
		return predkosc;
	}

	public double getKierunek() {
		return kierunek;
	}
}
