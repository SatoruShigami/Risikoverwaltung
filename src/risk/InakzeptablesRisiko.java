package risk;
import java.io.OutputStream;

import java.io.PrintStream;


public class InakzeptablesRisiko extends Risiko {
    protected String massnahme;
    
    public InakzeptablesRisiko(String bezeichnung, float eintrittswahrscheinlichkeit, 
                              float kostenImSchadensfall, String massnahme) {
        super(bezeichnung, eintrittswahrscheinlichkeit, kostenImSchadensfall);
        this.massnahme = massnahme;
    }
    
    @Override
    public float ermittleRueckstellung() {
        return berechneRisikowert();
    }
    public void setMassnahme(String massnahme) {
    	this.massnahme = massnahme;
    }
    @Override
    public void druckeDaten(OutputStream stream) {
        PrintStream ps = new PrintStream(stream);
        ps.printf("Id %d Inakzeptables Risiko \"%s\" aus %d/%d; Risikowert %.2f; Rückstellung %.2f; Maßnahme \"%s\"%n",
                getId(), getBezeichnung(), 
                getErstellungsdatum().getMonthValue(), getErstellungsdatum().getYear(),
                berechneRisikowert(), ermittleRueckstellung(), massnahme);
    }
    @Override
    public String toString() {
        return String.format("Inakzeptables Risiko %s; Risikowert %.2f; Rückstellung %.2f; Maßnahme \"%s\"",
            getFormattedData(),
            berechneRisikowert(),
            ermittleRueckstellung(),
            getMassnahme());
    }
    public String getMassnahme() {
        return massnahme;
    }
}