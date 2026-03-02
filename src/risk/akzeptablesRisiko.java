package risk;
import java.io.OutputStream;
import java.io.PrintStream;

public class akzeptablesRisiko extends Risiko {
    public akzeptablesRisiko(String bezeichnung, float eintrittswahrscheinlichkeit, float kostenImSchadensfall) {
        super(bezeichnung, eintrittswahrscheinlichkeit, kostenImSchadensfall);
    }
    
    @Override
    public float ermittleRueckstellung() {
        return 0.0f;
    }
    @Override
    public String toString() {
        return String.format("Akzeptables Risiko %s; Risikowert %.2f; Rückstellung %.2f",
            getFormattedData(),
            berechneRisikowert(),
            ermittleRueckstellung());
    }
    @Override
    public void druckeDaten(OutputStream stream) {
        PrintStream ps = new PrintStream(stream);
        ps.printf("Id %d Akzeptables Risiko \"%s\" aus %d/%d; Risikowert %.2f; Rückstellung %.2f%n",
                getId(), getBezeichnung(), 
                getErstellungsdatum().getMonthValue(), getErstellungsdatum().getYear(),
                berechneRisikowert(), ermittleRueckstellung());
    }
}