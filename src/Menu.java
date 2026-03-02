
import javax.swing.JOptionPane;

import risk.Risikoverwaltung;
import risk.Risiko;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Menu {
    private Risikoverwaltung verwaltung;
    
    public Menu(Risikoverwaltung verwaltung) {
        this.verwaltung = verwaltung;
    }
    
    public void showMenu() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                "Risikoverwaltung\n\n" +
                "1. Risiko aufnehmen\n" +
                "2. Zeige alle Risiken\n" +
                "3. Risikoliste in Datei schreiben\n" +
                "4. Zeige Risiko mit maximaler Rückstellung\n" +
                "5. Berechne Summe aller Rückstellungen\n" +
                "6. Beenden\n\n" +
                "Bitte Menüpunkt wählen:");
            
            if (input == null) {
                // User clicked cancel
            	return;
            }
            
            try {
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        aufnehmenRisiko();
                        break;
                    case 2:
                        verwaltung.zeigeRisiken(System.out);
                        break;
                    case 3:
                        risikolisteInDateiSchreiben();
                        break;
                    case 4:
                        zeigeMaxRueckstellung();
                        break;
                    case 5:
                        zeigeSummeRueckstellungen();
                        break;
                    case 6:
                        return;
                    default:
                        JOptionPane.showMessageDialog(null, "Ungültige Eingabe! Bitte 1-6 wählen.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Bitte eine Zahl eingeben!");
            }
        }
    }
    
    private void aufnehmenRisiko() {
        try {
            String bezeichnung = JOptionPane.showInputDialog("Bezeichnung des Risikos:");
            if (bezeichnung == null) return;
            
            float eintrittsW = Float.parseFloat(JOptionPane.showInputDialog("Eintrittswahrscheinlichkeit (0-1):"));
            float kosten = Float.parseFloat(JOptionPane.showInputDialog("Kosten im Schadensfall:"));
            
            String massnahme = "";
            if (eintrittsW * kosten > Risikoverwaltung.LIMIT || kosten > Risikoverwaltung.KOSTENLIMIT) {
                massnahme = JOptionPane.showInputDialog("Maßnahme:");
                if (massnahme == null) return;
            }
            
            Risiko risiko = Risikoverwaltung.createRisiko(bezeichnung, eintrittsW, kosten, massnahme);
            verwaltung.aufnehmen(risiko);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ungültige Zahleneingabe!");
        }
    }
    
    private void risikolisteInDateiSchreiben() {
        String dateiname;
        while (true) {
            dateiname = JOptionPane.showInputDialog("Dateiname eingeben:");
            if (dateiname == null) return; // User clicked cancel
            
            if (dateiname.trim().isEmpty()) {
                int option = JOptionPane.showConfirmDialog(null, 
                    "Dateiname ist leer! Neuen Dateinamen wählen?", 
                    "Warnung", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    return;
                }
            } else {
                break;
            }
        }
        
        try (OutputStream fos = new FileOutputStream(dateiname)) {
            verwaltung.zeigeRisiken(fos);
            JOptionPane.showMessageDialog(null, "Risikoliste erfolgreich gespeichert.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Speichern: " + e.getMessage());
        }
    }
    
    private void zeigeMaxRueckstellung() {
        Risiko risiko = verwaltung.sucheRisikoMitMaxRueckstellung();
        if (risiko == null) {
            JOptionPane.showMessageDialog(null, "Keine Risiken vorhanden.");
        } else {
            risiko.druckeDaten(System.out);
        }
    }
    
    private void zeigeSummeRueckstellungen() {
        float summe = verwaltung.berechneSummeRueckstellungen();
        JOptionPane.showMessageDialog(null, "Summe aller Rückstellungen: " + summe);
    }
}