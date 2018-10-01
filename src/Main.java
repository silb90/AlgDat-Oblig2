import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Oppgave 1
        Liste<String> liste1 = new DobbeltLenketListe<>();
        System.out.println(liste1.antall() + " " + liste1.tom());

        // Oppgave 2
        String[] s = {"Ole", null, "Per", "Kari", null};
        Liste<String> liste2 = new DobbeltLenketListe<>(s);
        System.out.println(liste2.antall() + " " + liste2.tom());
        Integer[] integers = {1, 2, null, 3, 4, null, 55};
        Liste<Integer> liste2Alt = new DobbeltLenketListe<>(integers);

        System.out.println(liste2.toString());
        System.out.println(liste2Alt.toString());

        String[] s1 = {}, s2 = {"A"}, s3 = {null, "A", null, "B", null};
        DobbeltLenketListe<String> l1 = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> l2 = new DobbeltLenketListe<>(s2);
        DobbeltLenketListe<String> l3 = new DobbeltLenketListe<>(s3);
        System.out.println(l1.toString() + " " + l2.toString()
                + " " + l3.toString() + " " + l1.omvendtString() + " "
                + l2.omvendtString() + " " + l3.omvendtString());

        // Oppgave 3
        DobbeltLenketListe<Integer> liste3 = new DobbeltLenketListe<>();
        System.out.println(liste3.toString() + " " + liste3.omvendtString());
        for (int i = 1; i <= 3; i++) {
            liste3.leggInn(i);
            System.out.println(liste3.toString() + " " + liste3.omvendtString());
        }
    }
}
