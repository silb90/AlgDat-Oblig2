import java.util.*;

public class DobbeltLenketListe<T> implements Liste<T>
{
    private static final class Node<T>   // en indre nodeklasse
    {
        // instansvariabler
        private T verdi;
        private Node<T> forrige, neste;

        private Node(T verdi, Node<T> forrige, Node<T> neste)  // konstruktør
        {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        protected Node(T verdi)  // konstruktør
        {
            this(verdi, null, null);
        }

    } // Node

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;   // antall endringer i listen

    // hjelpemetode
    private Node<T> finnNode(int indeks) {
        if(indeks < antall / 2) {
            Node<T> p = hode;
            for (int i = 0; i < indeks; i++) p = p.neste;
            return p;
        } else {
            Node<T> p = hale;
            for (int i = antall - 1; i > indeks; i--) p = p.forrige;
            return p;
        }
    }

    private static void fratilKontroll(int antall, int fra, int til) {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall(" + antall + ")");

        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }

    // konstruktør
    public DobbeltLenketListe()
    {
        hode = hale = null;
        antall = 0;
        endringer = 0;
    }

    // konstruktør
    public DobbeltLenketListe(T[] a)
    {
        // Setter verdiene via konstruktoeren over
        this();

        //Sjekker om a eksisterer.
        Objects.requireNonNull(a, "Ikkje Lov! -->  Tabellen a er null!");

        // Sjekker om a har objekter / har innhold
        if (a.length == 0) return;


    /**
        hode = new Node<>(a[0], null, null);
        antall++;

        Node hode_tmp = hode;
        Node q = null;

        for (int i = 1; i < a.length; i++){
            if (a[i] != null){
                q= new Node<>(a[i], hode_tmp, null);

                hode_tmp.neste = q;
                q.forrige = hode_tmp;
                hode_tmp = q;
                q = q.neste;

                antall++;
            }
        }
     **/


        // Vil samle inn indeksene til alle elementene i a som ikke er nullpointere
        // Samtidig legge 1 til antallet for hver gang man ikke har en nullpointer
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            if (a[i] != null) {
                index.add(i);
                antall++;
            }
        }

        // Hvis det kun finnes nullpointer objekter, returner
        if (antall == 0) return;

        // Videre lages en Node for hode og hale
        hode = hale = new Node<>(a[index.get(index.size() - 1)], null, null);

        // Vil saa iterere gjennom og lage et nyte hode for hver gang, som samtidig peker til neste og forrige
        for (int i = index.size() - 2; i >= 0; i--) {
            Node<T> tmp = hode;
            hode = new Node<>(a[index.get(i)], null, tmp);
            tmp.forrige = hode;
        }

    }

    // subliste
    public Liste<T> subliste(int fra, int til) {
        fratilKontroll(antall, fra, til);
        Liste<T> tmpListe = new DobbeltLenketListe<>();
        for (int i = fra; i < til; i++) {
            tmpListe.leggInn(finnNode(i).verdi);
        }
        return tmpListe;
    }

    @Override
    public int antall()
    {
        return antall;
    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi)
    {
        Objects.requireNonNull(verdi, "Null-verdi");

        if (tom()) {
            hode = hale = new Node<>(verdi, null,null);
        } else {
            hale = hale.neste = new Node<>(verdi, hale, null);
        }

        antall++;
        endringer++;
        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        Objects.requireNonNull(verdi, "Nullverdi er ikke tillatt");
        indeksKontroll(indeks, true);

        if (tom()) {
            hode = hale = new Node<>(verdi, null,null);
        } else if (indeks == 0) {
            hode = hode.forrige = new Node<>(verdi, null, hode);
        } else if (indeks == antall) {
            hale = hale.neste = new Node<>(verdi, hale, null);
        } else {
            Node<T> p = hode;
            for (int i = 1; i < indeks; i++) p = p.neste;
            p = new Node<>(verdi, p, p.neste);
            p.neste.forrige = p;
            p.forrige.neste = p;
        }

        antall++;
        endringer++;
    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if (verdi == null) return -1;

        Node<T> p = hode;

        // Starter fra hodet og gaar gjennom lista fram til verdien er funnet
        // Saa fort den er funnet, blir den returnert, dvs. foerste fra venstre
        // Kan ta tid om lista er lang
        for (int i = 0; i < antall; i++) {
            if (p.verdi.equals(verdi))
                return i;
            p = p.neste;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Kan ikke ha nullverdi");
        indeksKontroll(indeks, false);

        Node<T> tmp = finnNode(indeks);
        T tmpVerdi = tmp.verdi;
        tmp.verdi = nyverdi;

        endringer++;
        return tmpVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) return false;

        Node<T> q = hode;

        while (q != null) {
            if (q.verdi.equals(verdi)) break;
            q = q.neste;
        }

        if (q == null) {
            return false;
        } else if (q == hode) {
            hode = hode.neste;
            hode.forrige = null;
        } else if (q == hale) {
            hale = hale.forrige;
            hale.neste = null;
        } else {
            q.forrige.neste = q.neste;
            q.neste.forrige = q.forrige;
            q.neste = null;
            q.forrige = null;
        }

        q.verdi = null;

        endringer++;
        antall--;

        return true;
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);

        T temp;

        if (indeks == 0) {
            temp = hode.verdi;
            hode = hode.neste;
            hode.forrige = null;

            if (antall == 1) {
                hale = null;
                hode.neste = null;
            }
        } else if (indeks == antall - 1) {
            temp = hale.verdi;
            Node<T> tempHale = hale;
            hale = hale.forrige;
            tempHale.forrige = null;
            hale.neste = null;
        } else {
            Node<T> p = finnNode(indeks);
            temp = p.verdi;
            p.forrige.neste = p.neste;
            p.neste.forrige = p.forrige;
            p.neste = null;
            p.forrige = null;
        }

        endringer++;
        antall--;
        return temp;
    }

    @Override
    public void nullstill()
    {

        Node<T> p = hode, q;

        while (p != null)
        {
            q = p.neste;
            p.neste = null;
            p.verdi = null;
            p.forrige = null;
            p = q;
        }

        hode = hale = null;

        endringer++;    // nullstilling er en endring
        antall = 0;           // antall lik 0 i en tom liste

        //throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    // Bruker av og til over 20ms uavhengig av om man bruker metoden under, som er like metoden toString()
    // fra EnkeltLenketListe, eller metoden omvendtString() under, som bruker StringJoiner
    public String toString(){
        StringBuilder s = new StringBuilder();

        s.append('[');

        if (!tom())
        {
            Node<T> p = hode;
            s.append(p.verdi);

            p = p.neste;

            while (p != null)  // tar med resten hvis det er noe mer
            {
                s.append(',').append(' ').append(p.verdi);
                p = p.neste;
            }
        }

        s.append(']');

        return s.toString();
    }

    public String omvendtString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        Node<T> p = hale;
        while (p != null){
            joiner.add(String.valueOf(p.verdi));
            p = p.forrige;
        }
        return joiner.toString();
    }

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public Iterator<T> iterator()
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    public Iterator<T> iterator(int indeks)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator()
        {
            denne = hode;     // denne starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks)
        {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

        @Override
        public boolean hasNext()
        {
            return denne != null;  // denne koden skal ikke endres!
        }

        @Override
        public T next()
        {
            if (iteratorendringer != endringer) {
                throw new ConcurrentModificationException("iteratorendringer samsvarer ikke med endringer");
            }
            else if (!hasNext()){
                throw new NoSuchElementException("Ikke flere elementer i lista!");
            }

            fjernOK = true;

            T tmp = hode.verdi;
            denne = denne.neste;

            return tmp;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

    } // DobbeltLenketListeIterator

} // DobbeltLenketListe