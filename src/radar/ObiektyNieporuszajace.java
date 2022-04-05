package radar;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ObiektyNieporuszajace extends JPanel{

    private HashMap<Punkt, Integer> kolaMap = new HashMap<Punkt, Integer>();
    private HashMap<Punkt, Integer> kwadratyMap = new HashMap<Punkt, Integer>();

    public static final int ILOSC = 5; // ustalona liczba obiektow
    public static final int PROMIEN = 25; // staly promien
    public static final int MAX = 10000; // maksymalna wysokosc obiektu nieporuszajacego

    ObiektyNieporuszajace(String filename) {
        this.setPreferredSize(new Dimension(850,850));
        try {
            Scanner sc = new Scanner(new File(filename));
            while(sc.hasNextLine()) {       // wczytywanie linia po linii z pliku
                int wartoscX = sc.nextInt(); // pobieram pierwsza wartosc odpowiadajaca wspolrzednej X obiektu nieporuszajacego
                int wartoscY = sc.nextInt(); // pobieram druga wartosc odpowiadajaca wspolrzednej Y obiektu nieporuszajacego
                int wysokosc = sc.nextInt(); // pobieram trzecia wartosc odpowiadajaca wyskosci obiektu nieporuszajacego
                String rodzaj = sc.next();
                // w zaleznosci od rodzaju obiektu dodaje go do odpowiedniej mapy
                if(rodzaj.equals("Circle")) {
                    kolaMap.put(new Punkt(wartoscX, wartoscY), wysokosc);
                }
                else if(rodzaj.equals("Square")) {
                    kwadratyMap.put(new Punkt(wartoscX, wartoscY), wysokosc);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) { //rysowanie obiektow nieporuszajacych
        Graphics2D g2D = (Graphics2D) g;
        for (Punkt punkt : kolaMap.keySet()) {
            g2D.setColor(Color.orange);
             g2D.fillOval((int)punkt.getX(), (int)punkt.getY(), PROMIEN,PROMIEN);
             g2D.drawString(kolaMap.get(punkt).toString()+"m",(int)punkt.getX()+10, (int)punkt.getY()-10);
        }
        for (Punkt punkt : kwadratyMap.keySet()) {
            g2D.setColor(Color.cyan);
            g2D.fillRect((int)punkt.getX(), (int)punkt.getY(), PROMIEN,PROMIEN);
            g2D.drawString(kwadratyMap.get(punkt).toString()+"m",(int)punkt.getX()+10, (int)punkt.getY()-10);
        }
    }

    public HashMap<Punkt, Integer> getKolaMap() {
        return kolaMap;
    }

    public HashMap<Punkt, Integer> getKwadratyMap() {
        return kwadratyMap;
    }
}

