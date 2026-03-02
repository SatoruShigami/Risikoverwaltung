package Datenhaltung;

import java.util.List;

import risk.Risiko;

public interface IDao {
	  void speichern(List<Risiko> liste) throws PersistenzException;
	    List<Risiko> laden() throws PersistenzException;
}
