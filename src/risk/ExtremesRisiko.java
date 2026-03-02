package risk;
import java.io.OutputStream;
import java.io.PrintStream;

public class ExtremesRisiko extends InakzeptablesRisiko {
    private float versicherungsbeitrag;
    
    public ExtremesRisiko(String bezeichnung, float eintrittswahrscheinlichkeit, 
                         float kostenImSchadensfall, String massnahme, float versicherungsbeitrag) {
        super(bezeichnung, eintrittswahrscheinlichkeit, kostenImSchadensfall, massnahme);
        this.versicherungsbeitrag = versicherungsbeitrag;
    }
    
    @Override
    public float ermittleRueckstellung() {
        return versicherungsbeitrag;
    }
    public void setversicherungbeitrag(float versicherungsbeitrag) {
    	this.versicherungsbeitrag = versicherungsbeitrag;
    }
    public void setMassnahme(String massnahme) {
    	this.massnahme = massnahme;
    }
    public String getMassnahme() {
    	return this.massnahme;
    }
    @Override
    public void druckeDaten(OutputStream stream) {
        PrintStream ps = new PrintStream(stream);
        ps.printf("Id %d Extremes Risiko \"%s\" aus %f/%f; Versicherungsbeitrag %.2f; Maßnahme \"%s\"%n",
                getId(), getBezeichnung(), 
                getErstellungsdatum().getMonthValue(), getErstellungsdatum().getYear(),
                versicherungsbeitrag, getMassnahme());
    }
    @Override
    public String toString() {
        return String.format("Extremes Risiko %s; Versicherungsbeitrag %.2f; Maßnahme \"%s\"",
            getFormattedData(),
            getVersicherungsbeitrag(),
            getMassnahme());
    }
    public float getVersicherungsbeitrag() {
        return versicherungsbeitrag;
    }
}