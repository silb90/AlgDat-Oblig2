import java.util.List;

public class Main {
    public static void main(String[] args) {
        /**
         * Oppgave 0
         */
        Liste<String> liste1 = new DobbeltLenketListe<>();
        System.out.println(liste1.antall() + " " + liste1.tom());

        /**
         * Oppgave 1
         */
        String[] s = {"Ole", null, "Per", "Kari", null};
        Liste<String> liste2 = new DobbeltLenketListe<>(s);
        System.out.println(liste2.antall() + " " + liste2.tom());
        System.out.println(liste2.toString());
        ((DobbeltLenketListe<String>) liste2).toString();
        // Utskrift: 3 false

        Integer[] integers = {1, 2, null, 3, 4, null, 55};
        Liste<Integer> liste2Alt = new DobbeltLenketListe<>(integers);
        System.out.println(liste2Alt.toString());


        /**
         * Oppgave 2a
         */
        String[] s1 = {}, s2 = {"A"}, s3 = {null, "A", null, "B", null};
        DobbeltLenketListe<String> l1 = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> l2 = new DobbeltLenketListe<>(s2);
        DobbeltLenketListe<String> l3 = new DobbeltLenketListe<>(s3);
        System.out.println(l1.toString() + " " + l2.toString()
                + " " + l3.toString() + " " + l1.omvendtString() + " "
                + l2.omvendtString() + " " + l3.omvendtString());
        // Utskrift: [] [A] [A, B] [] [A] [B, A]

        /**
         * Oppgave 2b
         */
        DobbeltLenketListe<Integer> liste3 = new DobbeltLenketListe<>();
        System.out.println(liste3.toString() + " " + liste3.omvendtString());
        for (int i = 1; i <= 3; i++) {
            liste3.leggInn(i);
            System.out.println(liste3.toString() + " " + liste3.omvendtString());
        }
        // Utskrift:
        // [] []
        // [1] [1]
        // [1, 2] [2, 1]
        // [1, 2, 3] [3, 2, 1]

        /**
         * Oppgave 3
         */
        Character[] c = {'A','B','C','D','E','F','G','H','I','J',};
        DobbeltLenketListe<Character> liste = new DobbeltLenketListe<>(c);
        System.out.println(liste.subliste(3,8)); // [D, E, F, G, H]
        System.out.println(liste.subliste(5,5)); // []
        System.out.println(liste.subliste(8,liste.antall())); // [I, J]
        //System.out.println(liste.subliste(0,11)); // skal kaste unntak

        /**
         * Oppgave 5
         */
        DobbeltLenketListe<Integer> liste5 = new DobbeltLenketListe<>();

        liste5.leggInn(0, 4);  // ny verdi i tom liste
        liste5.leggInn(0, 2);  // ny verdi legges forrest
        liste5.leggInn(2, 6);  // ny verdi legges bakerst
        liste5.leggInn(1, 3);  // ny verdi nest forrest
        liste5.leggInn(3, 5);  // ny verdi nest bakerst
        liste5.leggInn(0, 1);  // ny verdi forrest
        liste5.leggInn(6, 7);  // ny verdi legges bakerst

        System.out.println(liste5.toString());
        System.out.println(liste5.omvendtString());
    }
}
