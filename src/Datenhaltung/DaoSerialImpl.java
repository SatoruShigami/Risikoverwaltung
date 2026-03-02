package Datenhaltung;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import risk.Risiko;

public class DaoSerialImpl {
	
	    private static final String DATEINAME = "risiken.ser";

	    public void speichern(List<Risiko> liste) throws PersistenzException {
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATEINAME))) {
	            oos.writeObject(liste);
	            oos.writeInt(Risiko.getIdCounter());  // Save static counter
	        } catch (IOException e) {
	            throw new PersistenzException("Fehler beim Speichern", e);
	        }
	    }

	    public List<Risiko> laden() throws PersistenzException {
	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATEINAME))) {
	            List<Risiko> liste = (List<Risiko>) ois.readObject();
	            int idCounter = ois.readInt();
	            Risiko.setIdCounter(idCounter);  // Reset static counter
	            return liste;
	        } catch (IOException | ClassNotFoundException e) {
	            throw new PersistenzException("Fehler beim Laden", e);
	        }
	    }
	

}
