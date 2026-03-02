package risk;

import java.io.OutputStream;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Risikoverwaltung implements Iterable<Risiko> {
    public static final float LIMIT = 10000.0f;
    public static final float KOSTENLIMIT = 1000000.0f;

    private List<Risiko> risiken;

    public Risikoverwaltung() {
        this.risiken = new ArrayList<>();
    }

    public boolean aufnehmen(Risiko risiko) {
        if (risiko == null) {
            System.out.println("Ungültiges Risiko!");
            return false;
        }
        return risiken.add(risiko);
    }
    public List<Risiko> getRisikoListe() {
        return risiken;
    }
    public void setRisikoListe(List<Risiko> liste) {
        this.risiken = liste;
    }

    public void zeigeRisiken(OutputStream stream) {
        risiken.stream()
            .sorted(Comparator.comparing(Risiko::berechneRisikowert))
            .forEach(r -> r.druckeDaten(stream));
    }
    public Iterator<Risiko> iterator1() {
        return risiken.iterator();
    }

    public Risiko sucheRisikoMitMaxRueckstellung() {
        if (risiken.isEmpty()) {
            return null;
        }

        return risiken.stream()
            .max(Comparator.comparing(Risiko::ermittleRueckstellung))
            .orElse(null);
    }

    public float berechneSummeRueckstellungen() {
        return (float) risiken.stream()
            .mapToDouble(Risiko::ermittleRueckstellung)
            .sum();
    }

    @Override
    public Iterator<Risiko> iterator() {
        return risiken.iterator();
    }

    public static Risiko createRisiko(String bezeichnung, float eintrittswahrscheinlichkeit,
                                      float kostenImSchadensfall, String massnahme) {
        if (kostenImSchadensfall > KOSTENLIMIT) {
            float versicherungsbeitrag = eintrittswahrscheinlichkeit * kostenImSchadensfall;
            return new ExtremesRisiko(bezeichnung, eintrittswahrscheinlichkeit,
                                      kostenImSchadensfall, massnahme, versicherungsbeitrag);
        } else if (eintrittswahrscheinlichkeit * kostenImSchadensfall > LIMIT) {
            return new InakzeptablesRisiko(bezeichnung, eintrittswahrscheinlichkeit,
                                           kostenImSchadensfall, massnahme);
        } else {
            return new akzeptablesRisiko(bezeichnung, eintrittswahrscheinlichkeit,
                                         kostenImSchadensfall);
        }
    }
}
