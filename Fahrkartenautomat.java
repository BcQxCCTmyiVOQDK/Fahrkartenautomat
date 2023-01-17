package FahrkartenAutomat;
import java.util.Scanner;

// test commit 1

class Fahrkartenautomat
{
    public static void main(String[] args) {
        Scanner tastatur = new Scanner(System.in);
        double zuZahlenderBetrag;
        double rueckgabeBetrag;
        do {
            zuZahlenderBetrag = fahrkartenbestellungErfassen(tastatur);
            rueckgabeBetrag = fahrkartenBezahlen(zuZahlenderBetrag, tastatur);
            fahrkartenAusgeben();
            rueckgeldAusgeben(rueckgabeBetrag);
            printGruss();
        } while (ticketKaufen(tastatur));

        System.exit(0);
    }

    public static boolean ticketKaufen(Scanner sc) {
        final String message = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Wählen Sie 1 um ein Ticket zu kaufen
                1: Ticket kaufen
                0: Programm beenden
                ++++++++++++++++++++++++++++++++++++++++++++
                """;
        System.out.print(message);
        int antwort = sc.nextInt();
        return (antwort == 1);
    }

    // die Wahl kontrollieren
    private static boolean wahlSchlecht (int wahl,int max) {
        return wahl <= 0 ||
               wahl > max;
    }

    // anzahlTickets muss 1-10 sein
    private static boolean anzahlTicketsSchlecht (int i) {
        return i <= 0 ||
               i > 10;
    }

    // ticket waehlen und ticketAnzahl einlesen
    public static double fahrkartenbestellungErfassen(Scanner tastatur){
        final String ticketWaehlen = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Waehlen Sie ein Ticket:
                ++++++++++++++++++++++++++++++++++++++++++++
                """;
        final String ihreWahl = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Ihre Wahl (1-%1$d):
                """;
        final String anzahlTicketsEingeben = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Anzahl der Tickets eingeben (1-10):
                """;
        final String ungueltigeEingabe = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Ungueltige Eingabe !!!
                ++++++++++++++++++++++++++++++++++++++++++++
                """;
        final String zwischenSummenString = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Zwischensumme: %.2f EUR
                ++++++++++++++++++++++++++++++++++++++++++++
                """;

        double[] preise = { // Ticketpreise in EUR
                2.9, 3.3, 3.6, 1.9, 8.6, 9.0, 9.6, 23.5, 24.3, 24.9};
        String[] tickets = { // Ticketarten
                "Einzelfahrschein Berlin AB",
                "Einzelfahrschein Berlin BC",
                "Einzelfahrschein Berlin ABC",
                "Kurzstrecke",
                "Tageskarte Berlin AB",
                "Tageskarte Berlin BC",
                "Tageskarte Berlin BC",
                "Kleingruppen-Tageskarte Berlin AB",
                "Kleingruppen-Tageskarte Berlin BC",
                "Kleingruppen-Tageskarte Berlin ABC",
                "Bezahlen"
        };
        // Nummer zu wählen um zu bezahlen
        int bezahlenNummer = tickets.length;

        double zwischenSumme = 0;
        while (true) {
            int wahl;
            do {
                System.out.print(ticketWaehlen);
                for (int i = 0; i < tickets.length; i++) {
                    int last = tickets.length - 1;
                    // last ist besonders: "Bezahlen"
                    if (i == last) {
                        System.out.printf("%-6s%-40s\n", String.format("(%d)", i + 1), tickets[i]);
                    } else {
                        System.out.printf("%-6s%-40s [%.2f EUR]\n", String.format("(%d)", i + 1), tickets[i], preise[i]);
                    }
                }
                System.out.printf(ihreWahl,bezahlenNummer);
                wahl = tastatur.nextInt();
                if (wahlSchlecht(wahl,bezahlenNummer)) {
                    System.out.print(ungueltigeEingabe);
                    warte(2000);
                }
            } while (wahlSchlecht(wahl,bezahlenNummer));

            // endlose Schleife beenden
            if (wahl==bezahlenNummer) break;

            int anzahlTickets;
            do {
                System.out.print(anzahlTicketsEingeben);
                anzahlTickets = tastatur.nextInt();
                if (anzahlTicketsSchlecht(anzahlTickets)) {
                    System.out.print(ungueltigeEingabe);
                    warte(2000);
                }
            } while (anzahlTicketsSchlecht(anzahlTickets));

            zwischenSumme += preise[wahl-1] * anzahlTickets;
            System.out.printf(zwischenSummenString,zwischenSumme);
            warte(2000);
        }

        return zwischenSumme;
    }

    // eingezahlterGesamtbetrag berechnen
    public static double fahrkartenBezahlen(double zuZahlenderBetrag, Scanner muenzen) {
        double eingezahlterGesamtbetrag = 0.0;
        double eingeworfeneMuenze;

        while (eingezahlterGesamtbetrag < zuZahlenderBetrag) {
            System.out.format("Noch zu zahlen: %4.2f €%n", (zuZahlenderBetrag - eingezahlterGesamtbetrag));
            System.out.print("Muenze einwerfen (mind. 5Ct, höchstens 2 Euro): ");
            eingeworfeneMuenze = muenzen.nextDouble();
            eingezahlterGesamtbetrag += eingeworfeneMuenze;
        }
        // rueckgabeBetrag
        return eingezahlterGesamtbetrag - zuZahlenderBetrag;
    }

    // Fahrkarten-Ausgabe
    public static void fahrkartenAusgeben() {
        System.out.println("\nFahrscheine werden ausgegeben");
        for (int i = 0; i < 8; i++) {
            System.out.print("=");
            warte();
        }
        System.out.println("\n\n");
    }

    private static void warte(int zeit) {
        try {
            Thread.sleep(zeit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void warte() {
        warte(250);
    }

        // Rückgeldberechnung und -Ausgabe
    public static void rueckgeldAusgeben (double rueckgabeBetrag){
        if(rueckgabeBetrag > 0.0) {
            System.out.format("Der Rückgabebetrag in Höhe von %4.2f € %n" , rueckgabeBetrag );
            System.out.println("wird in Muenzen  ausgezahlt:");

            while(rueckgabeBetrag >= 2.0) // 2 EURO-Münzen
            {
                System.out.println("2 EURO");
                rueckgabeBetrag -= 2.0;
            }
            while(rueckgabeBetrag >= 1.0) // 1 EURO-Münzen
            {
                System.out.println("1 EURO");
                rueckgabeBetrag -= 1.0;
            }
            while(rueckgabeBetrag >= 0.5) // 50 CENT-Münzen
            {
                System.out.println("50 CENT");
                rueckgabeBetrag -= 0.5;
            }
            while(rueckgabeBetrag >= 0.2) // 20 CENT-Münzen
            {
                System.out.println("20 CENT");
                rueckgabeBetrag -= 0.2;
            }
            while(rueckgabeBetrag >= 0.1) // 10 CENT-Münzen
            {
                System.out.println("10 CENT");
                rueckgabeBetrag -= 0.1;
            }
            while(rueckgabeBetrag >= 0.05)// 5 CENT-Münzen
            {
                System.out.println("5 CENT");
                rueckgabeBetrag -= 0.05;
            }
        }
    }

    // Gruss ausgeben
    public static void printGruss() {
        final String gruss = """
                ++++++++++++++++++++++++++++++++++++++++++++
                Vergessen Sie nicht, den Fahrschein
                vor Fahrtantritt entwerten zu lassen!
                Wir wünschen Ihnen eine gute Fahrt.
                ++++++++++++++++++++++++++++++++++++++++++++
                """;
        System.out.print(gruss);
        warte(2000);
    }
}