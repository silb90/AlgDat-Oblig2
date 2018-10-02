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


//        // Vil samle inn indeksene til alle elementene i a som ikke er nullpointere
//        // Samtidig legge 1 til antallet for hver gang man ikke har en nullpointer
//        ArrayList<Integer> index = new ArrayList<>();
//        for (int i = 0; i < a.length; i++) {
//            if (a[i] != null) {
//                index.add(i);
//                antall++;
//            }
//        }

//        // Hvis det kun finnes nullpointer objekter, returner
//        if (antall == 0) return;
//
//        // Videre lages en Node for hode og hale
//        hode = hale = new Node<>(a[index.get(index.size() - 1)], null, null);
//
//        // Vil saa iterere gjennom og lage et nyte hode for hver gang, som samtidig peker til neste og forrige
//        for (int i = index.size() - 2; i >= 0; i--) {
//            Node<T> tmp = hode;
//            hode = new Node<>(a[index.get(i)], null, tmp);
//            tmp.forrige = hode;
//        }

        //LinkedList<String> liste = new LinkedList<>();

//        String[] s = {toString()};
//        LinkedList<String> liste = new DobbeltLenketListe<>(s);
//        for (int i = 0; i < a.length; ++i){
//
//            if (a[i] != null){
//                liste.add(a[i].toString());
//            }
//        }
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
            hode = new Node<>(verdi, null, hode);
            if (antall == 0) hale = hode;
        } else if (indeks == antall) {
            hale = hale.neste = new Node<>(verdi, hale, null);
        } else {
            // Trenger mer jobb, får en feil
            Node<T> p = hode;
            for (int i = 1; i < indeks; i++) p = p.neste;
            p.neste = new Node<>(verdi, p, p.neste);
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
    public boolean fjern(T verdi)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public T fjern(int indeks)
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }

    @Override
    public void nullstill()
    {
        throw new UnsupportedOperationException("Ikke laget ennå!");
    }


    public String toString(){
        StringJoiner joiner = new StringJoiner(" ", "[", "]");
        Node<T> p = hode;
        while (p != null){
            joiner.add(String.valueOf(p.verdi));
            p = p.neste;
        }
        return joiner.toString();
    }

    public String omvendtString()
    {
        StringBuilder s = new StringBuilder();

        s.append('[');

        if (!tom())
        {
            Node<T> p = hale;
            s.append(p.verdi);

            p = p.forrige;

            while (p != null)  // tar med resten hvis det er noe mer
            {
                s.append(',').append(' ').append(p.verdi);
                p = p.forrige;
            }
        }

        s.append(']');

        return s.toString();
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
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Ikke laget ennå!");
        }

    } // DobbeltLenketListeIterator

} // DobbeltLenketListe