package radar;

import java.util.Random;

public class Punkt {
	private double x, y; // zmienilem int-y na double zeby niecalkowita zmiana przemieszczenia nie byla zapominana
	private static int XMAX = 850, YMAX = 850;

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Punkt() {}

	public Punkt(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Punkt(Punkt punkt) {
		this.x = punkt.x;
		this.y = punkt.y;
	}

	public static Punkt wygenerujLosowyPunkt() {
		Random random = new Random();
		return new Punkt(
				random.nextInt(XMAX), random.nextInt(YMAX)
		);
	}
}
