package radar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Statek {

    ArrayList<JLabel> lista_label = new ArrayList<JLabel>();
    protected Trasa trasa;
    protected Punkt wspolrzedne;
    protected Image symbol;

    public Punkt getWspolrzedne() {
        return wspolrzedne;
    }


    public static Statek wygenerujLosowyStatek() {
        Statek statek = null;
        Trasa trasa = null;
        Random random = new Random();

        int wylosowanyStatek = random.nextInt(4);
        wylosowanyStatek=0;
        switch (wylosowanyStatek) {
            case 0 : statek = Samolot.wygenerujLosowySamolot();
                break;
            case 1 : statek = Smiglowiec.wygenerujLosowySmiglowiec();
                break;
            case 2 : statek = Balon.wygenerujLosowyBalon();
                break;
            case 3 : statek = Szybowiec.wygenerujLosowySzybowiec();
                break;
        }

        return statek;
    }


    public Trasa getTrasa() {
        return trasa;
    }

    public Image getObraz() {
    	return symbol;
    }
}
