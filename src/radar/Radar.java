package radar;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Radar extends JPanel {

    private LinkedList<Statek> statki;
    private Image mapa;
    private final ImageIcon symbolPunktu = new ImageIcon("img/punkt.png");
    private Image dangerSymbol;
    private Timer timer;
    private ActionListener actionListener;
    private MouseAdapter mouseAdapter;
    private ObiektyNieporuszajace obiektyNp; // dodanie panelu obiektow nieporuszajacych

    public Radar() {
        obiektyNp = new ObiektyNieporuszajace("dane/dane_obiekty_nieporuszajace.txt");

        actionListener = wygenerujActionListener();

        timer = new Timer(500, actionListener);            //timer wywolujacy co 0.5s metode actionPerformed()
        timer.start();

        mouseAdapter = wygenerujMouseAdapter();

        ustawPrametryPanelu();
        JFrame okno = ustawParametryOkna();

        statki = new LinkedList<Statek>();
   }

    private ActionListener wygenerujActionListener(){
        return new ActionListener() {                  //W odpowiedzi na okreslona akcje (w tym przypadku wzbudzenie timera wystepujace co 1s) wykonuje zawarte w nim instrukcje
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<Statek> iterator = statki.iterator();

                while (iterator.hasNext()) {              //petla for each dla listy statkow powietrznych
                    Statek s = iterator.next();
                    System.out.println("S = (" + (int) s.getWspolrzedne().getX() + ", " + (int) s.getWspolrzedne().getY() + ")"); //Tymczasowy kod
                    s.wspolrzedne = s.getTrasa().obliczAktualneWspolrzedneStatku(s.wspolrzedne);
                    if(s.wspolrzedne==null) { //sprawdza czy statek dolecial do ostatniego punktu
                        usunObiekty(s); //usuwa statkek gdy doleci do ostatniego punktu
                    }
                }
                repaint();
            }
        };
    }

    private double odleglosc(Punkt p1, Punkt p2){
        double a = p1.getX() - p2.getX();
        double b = p1.getY() - p2.getY();
        return Math.sqrt(a*a + b*b);
    }

    private MouseAdapter wygenerujMouseAdapter() {
        return new MouseAdapter() {
            int xPrzedPrzesunieciem, yPrzedPrzesunieciem, xPoPrzesunieciu, yPoPrzesunieciu, indexStatku, indexPunktuTrasyStatku;

            public void mousePressed(MouseEvent e) {
                xPrzedPrzesunieciem = e.getX();
                yPrzedPrzesunieciem = e.getY();

                indexStatku = Character.getNumericValue(
                        e.getComponent().getName().charAt(0)                         //Pobiera pierwszy znak z nazwy punku JLabel na mapie
                );

                indexPunktuTrasyStatku = Character.getNumericValue(
                        e.getComponent().getName().charAt(2)                         //Pobiera trzeci znak z nazwy punku JLabel na mapie
                );

                if(indexPunktuTrasyStatku < statki.get(indexStatku).getTrasa().indeksOdcinka) {
                    System.out.println("Nie można zmienić odcinka trasy, ktory zostal pokonany");
                }
                if(indexPunktuTrasyStatku == statki.get(indexStatku).getTrasa().indeksOdcinka || indexPunktuTrasyStatku == (statki.get(indexStatku).getTrasa().indeksOdcinka+1)) {
                    System.out.println("Nie można zmienić odcinka trasy, na którym aktualnie znajduje sie statek");
                }
                /*Zakladam, ze statek zawsze startuje z pierwszego punktu
			      pierwszego odcinka trasy i że nie można zmieniać odcinka trasy,
			      na którym aktualnie się znajduje. Komunikat wyswietlany w konsoli
			      tymczasowo, potem mozna pomyslec o wyswietlaniu na ekranie glownym.*/
            }

            public void mouseDragged(MouseEvent e) {
                if(indexPunktuTrasyStatku > (statki.get(indexStatku).getTrasa().indeksOdcinka+1)) {
                    int x = e.getComponent().getX() + e.getX() - xPrzedPrzesunieciem;  //Pozycja poczatkowa punktu + aktualna pozycja kursora - pozycja kursora w momencie klikniecia
                    int y = e.getComponent().getY() + e.getY() - yPrzedPrzesunieciem;  //Pozycja poczatkowa punktu + aktualna pozycja kursora - pozycja kursora w momencie klikniecia

                    e.getComponent().setLocation(x, y);

                    xPoPrzesunieciu = e.getComponent().getX() + 10;                     //Dodaje 10 zeby srodek graficznego punktu pokryl sie ze wspolrzednymi faktycznego punktu
                    yPoPrzesunieciu = e.getComponent().getY() + 10;                     //Dodaje 10 zeby srodek graficznego punktu pokryl sie ze wspolrzednymi faktycznego punktu

                    statki.get(indexStatku).
                            getTrasa().zmienWspolrzednePunkuTrasy(
                            indexPunktuTrasyStatku, new Punkt(xPoPrzesunieciu, yPoPrzesunieciu)
                    );

                    repaint();
                }
            }
        };
    }

    private void ustawPrametryPanelu() {
        this.setPreferredSize(new Dimension(1300, 750));
        this.setLayout(null);
        mapa = new ImageIcon("img/mapaeuropy.png").getImage();
        dangerSymbol = new ImageIcon("img/danger.png").getImage();
    }

    private JFrame ustawParametryOkna() {
        JFrame okno = new JFrame();
        okno.add(this);
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.pack();                                            //dopasowanie rozmiaru okna do romiaru elementow jakie zawiera
        okno.setLocationRelativeTo(null);                       //po starcie programu ustawia okno na srodku ekranu
        okno.setResizable(true);
        okno.setVisible(true);
        okno.setLayout(null);
        return okno;
    }

    public void dodajStatek(Statek statek) {
        statki.add(statek);
        umiescPunktyTrasyStatkuNaMapie(statek);
    }

    /**
     * Nadpisana metoda klasy Component.
     * Metoda ta przyjmuje jako argument obiekt typu Graphics (klasa Graphics jest klasa abstrakcyjna),
     * ktory jest w rzeczywistosci obiektem typu Graphics2D zrzutowanym w góre (Graphics2D dziedziczy z Graphics).
     * Metoda jest wywoływana automatycznie kiedy tworzymy instacje obiketu typu graficznego np. JFrame
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);          /*Odwoluje sie do metody paintComponent klasy wyzszej, niezbedne do
                                           narysowania innych komponentow graficznych na zdjeciu tla np. JLabel*/
        Graphics2D g2D = (Graphics2D) g;  //Rrzutowanie w doł obiektu typu graphics na obiekt typu graphics2D, poniewaz g2D ma wiecej funkcjonalnosci
        g.drawImage(mapa, 0,0,null);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Ostrzeganie o zblizeniach i kolizje
        ListIterator<Statek> iterator1 = statki.listIterator();//Wprowadzilem iterator, aby walczyc z bugiem crashujacym
        ListIterator<Statek> iterator2;
        while (iterator1.hasNext()) {                    //Dla kazdego statku sprawdzamy odleglosc od pozostalych oraz od obiektow nieruchomych
            Statek s1 = iterator1.next();
            Punkt wsp = s1.getWspolrzedne();
            int wys = s1.getTrasa().getWysokosc();
            System.out.println(wys);
            iterator2 = statki.listIterator(iterator1.nextIndex());
            while (iterator2.hasNext()) {                                     //petla dla statkow
                Statek s2 = iterator2.next();
                if (Math.abs(wys - s2.getTrasa().getWysokosc()) < 100) {
                    if (odleglosc(wsp, s2.getWspolrzedne()) < 100) {
                        if (odleglosc(wsp, s2.getWspolrzedne()) < 25) {
                            System.out.println("Kolizja");
                            g.drawImage(dangerSymbol,(int)s1.getWspolrzedne().getX()-25,(int)s1.getWspolrzedne().getY()-25,null);
                            g.drawImage(dangerSymbol,(int)s2.getWspolrzedne().getX()-25,(int)s2.getWspolrzedne().getY()-25,null);
                            usunObiekty(s1);
                            usunObiekty(s2);
                            break;
                        } else {
                            System.out.println("Niebezpieczne zblizenie");
                            g.drawImage(dangerSymbol,(int)s1.getWspolrzedne().getX()-25,(int)s1.getWspolrzedne().getY()-25,null);
                        }
                    }
                }
            }
            for (Map.Entry<Punkt, Integer> entry : obiektyNp.getKwadratyMap().entrySet()) {    //petla ob. nier. kwadraty
                if (entry.getValue()+100 >= wys) {
                    if (odleglosc(wsp, entry.getKey()) < 100) {
                        if (odleglosc(wsp, entry.getKey()) < 25 && entry.getValue() >= wys) {
                            System.out.println("Kolizja");
                            usunObiekty(s1);
                            break;
                        } else {
                            System.out.println("Niebezpieczne zblizenie");
                        }
                        g.drawImage(dangerSymbol,(int)s1.getWspolrzedne().getX()-25,(int)s1.getWspolrzedne().getY()-25,null);
                    }
                }
            }
            for (Map.Entry<Punkt, Integer> entry : obiektyNp.getKolaMap().entrySet()) {        //petla dla obiektow nieruch. kola
                if (entry.getValue()+100 >= wys) {
                    if (odleglosc(wsp, entry.getKey()) < 100) {
                        if (odleglosc(wsp, entry.getKey()) < 25 && entry.getValue() >= wys) {
                            System.out.println("Kolizja");
                            usunObiekty(s1);
                            break;
                        } else {
                            System.out.println("Niebezpieczne zblizenie");
                            g.drawImage(dangerSymbol,(int)s1.getWspolrzedne().getX()-25,(int)s1.getWspolrzedne().getY()-25,null);
                        }
                    }
                }
            }
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        obiektyNp.paint(g);

        iterator1 = statki.listIterator();
        while(iterator1.hasNext()){                   //rysowanie statków i odcinkow tras
            Statek s = iterator1.next();
            g.drawImage(s.getObraz(),(int)s.getWspolrzedne().getX()-25,(int)s.getWspolrzedne().getY()-25,null);
            g2D.drawString(s.getTrasa().getWysokosc() +"m",(int)s.getWspolrzedne().getX()+10, (int)s.getWspolrzedne().getY()-10);
            Trasa trasa = s.getTrasa();
            for(Odcinek o: trasa.getOdcinki()){
                g2D.setColor(Color.RED);
                g2D.setStroke(new BasicStroke(2));
                g.drawLine((int)o.getP1().getX(), (int)o.getP1().getY(), (int)o.getP2().getX(), (int)o.getP2().getY());
            }
        }
    }


    private void umiescPunktyTrasyStatkuNaMapie(Statek statek) {
        int iloscPunktowTrasy = statek.getTrasa().getOdcinki().size() + 1;  //Punktow jest zawsze o 1 wiecej niz odcinkow
        int x, y;

        for(int i=0; i<iloscPunktowTrasy; i++) {
            JLabel label = new JLabel();
            label.setIcon(symbolPunktu);
            label.setSize(
                    new Dimension(20, 20)
            );
            x = (int) statek.getTrasa().getPunktTrasy(i).getX() - 10;      //Odejmuje 10 zeby srodek graficznego punktu pokryl sie ze wspolrzednymi faktycznego punktu
            y = (int) statek.getTrasa().getPunktTrasy(i).getY() - 10;      //Odejmuje 10 zeby srodek graficznego punktu pokryl sie ze wspolrzednymi faktycznego punktu
            label.setLocation(x, y);
            label.setName(statki.size() - 1 + "." + i);                    /*Ustawiam nazwe graficznego punktu w celu skojarzenia go z faktycznym punktem trasy danego samolotu
                                                                             nazwa = index_statku.numer_punktu (pierwszy punkt ma numer 0)*/
            label.addMouseListener(mouseAdapter);
            label.addMouseMotionListener(mouseAdapter);

            this.add(label);
            statek.lista_label.add(label);
        }
    }

    private void usunObiekty(Statek statek) {
        for (JLabel p : statek.lista_label){
            this.remove(p);
        }
        statki.remove(statek);
    }

}