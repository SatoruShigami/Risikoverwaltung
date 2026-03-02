package risk;
import java.time.LocalDate;
import javafx.beans.property.*;

public abstract class Risiko {
    private static int nextId = 0;
    
    private final int id;
    private String bezeichnung;
    private float eintrittswahrscheinlichkeit;
    private float kostenImSchadensfall;
    private LocalDate erstellungsdatum;
   
    
    public Risiko(String bezeichnung, float eintrittswahrscheinlichkeit, float kostenImSchadensfall) {
        this.id = nextId++;
        this.bezeichnung = bezeichnung;
        this.eintrittswahrscheinlichkeit = eintrittswahrscheinlichkeit;
        this.kostenImSchadensfall = kostenImSchadensfall;
        this.erstellungsdatum = LocalDate.now();
    }
    
    public float berechneRisikowert() {
        return eintrittswahrscheinlichkeit * kostenImSchadensfall;
    }
    
    public abstract float ermittleRueckstellung();
    public abstract void druckeDaten(java.io.OutputStream stream);
    // setters
    public void setBezeichnung(String bez) {
    	this.bezeichnung = bez;
    }
    public void seteintrittswahrscheinlichkeit(float eintrittswahrscheinlichkeit) {
    	this.eintrittswahrscheinlichkeit = eintrittswahrscheinlichkeit;
    }
    public void setkostenImSchadensfall(float kostenImSchadensfall) {
    	this.kostenImSchadensfall = kostenImSchadensfall;
    }
    // Getters
    public int getId() { return id; }
    public String getBezeichnung() { return bezeichnung; }
    public float getEintrittswahrscheinlichkeit() { return eintrittswahrscheinlichkeit; }
    public float getKostenImSchadensfall() { return kostenImSchadensfall; }
    public LocalDate getErstellungsdatum() { return erstellungsdatum; }
    public static int getIdCounter() {
        return nextId;
    }

    public static void setIdCounter(int id) {
        nextId = id;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Risiko other = (Risiko) obj;
        return Float.compare(other.eintrittswahrscheinlichkeit, eintrittswahrscheinlichkeit) == 0 &&
               Float.compare(other.kostenImSchadensfall, kostenImSchadensfall) == 0 &&
               bezeichnung.equals(other.bezeichnung);
    }
    protected String getFormattedData() {
        return String.format("Id %d \"%s\" aus %f/%f",
        		getId(),getBezeichnung(),getEintrittswahrscheinlichkeit(),getKostenImSchadensfall(),getErstellungsdatum());
    }
    @Override
    public int hashCode() {
        int result = bezeichnung.hashCode();
        result = 31 * result + Float.hashCode(eintrittswahrscheinlichkeit);
        result = 31 * result + Float.hashCode(kostenImSchadensfall);
        return result;
    }
}