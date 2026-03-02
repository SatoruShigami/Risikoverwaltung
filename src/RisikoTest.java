


import gui.MainWindow;
import risk.ExtremesRisiko;
import risk.InakzeptablesRisiko;
import risk.Risikoverwaltung;
import risk.akzeptablesRisiko;
import risk.Risiko;

public class RisikoTest{
    public static void main(String[] args) {
        // Test the equals and hashCode methods
        testEqualsAndHashCode();
        
        // Create a risk management system with capacity for 10 risks
        Risikoverwaltung verwaltung = new Risikoverwaltung();
        
        // Add some example risks
        verwaltung.aufnehmen(new akzeptablesRisiko("Lizenzkosten der IDE steigt", 0.4f, 10000.0f));
        verwaltung.aufnehmen(new ExtremesRisiko("Hauptauftraggeber meldet Insolvenz an", 0.05f, 
                                             2000000.0f, "Versicherung abschließen", 50000.0f));
        verwaltung.aufnehmen(new InakzeptablesRisiko("DB Experte verlässt das Projekt", 0.8f, 
                                                  20000.0f, "Ersatz bei Dienstleister reservieren"));
        
        // Show the menu
        Menu menu = new Menu(verwaltung);
        menu.showMenu();
        MainWindow.launch(MainWindow.class, args);
    }     
    private static void testEqualsAndHashCode() {
        Risiko r1 = new akzeptablesRisiko("Test Risiko", 0.5f, 1000.0f);
        Risiko r2 = new akzeptablesRisiko("Test Risiko", 0.5f, 1000.0f);
        Risiko r3 = new akzeptablesRisiko("Anderes Risiko", 0.3f, 2000.0f);
        
        System.out.println("Die Objekte mit Id " + r1.getId() + " und Id " + r2.getId() + 
                         " sind " + (r1.equals(r2) ? "" : "nicht ") + "(fachlich) gleich");
        System.out.println("Die gleichen Objekte haben den Hashcode " + r1.hashCode());
        
        System.out.println("Die Objekte mit Id " + r1.getId() + " und Id " + r3.getId() + 
                         " sind " + (r1.equals(r3) ? "" : "nicht ") + "gleich");
        System.out.println("Die unterschiedlichen Objekte haben die Hashcodes " + 
                         r1.hashCode() + " und " + r3.hashCode());
    }
   
        
   
        
    }

