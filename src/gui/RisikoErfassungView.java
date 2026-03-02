package gui;


import javafx.geometry.Insets;
import pk1.gui.util.InputView;
import pk1.gui.util.MessageView;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.*;

import risk.ExtremesRisiko;
import risk.InakzeptablesRisiko;
import risk.Risiko;
public class RisikoErfassungView extends Stage {
	
	private final Risiko risiko;
    private boolean confirmed = false;

    // Common controls
    private final Label bezeichnungField = new Label();
    private final Label wahrscheinlichkeitSpinner = 
        new Label("");
    private final Label kostenSpinner = 
        new Label("");

    // Special controls
    private final Label massnahmeField = new Label("");
    private final Label versicherungSpinner = 
        new Label("");

    public RisikoErfassungView(Stage owner, Risiko risiko) {
        this.risiko = risiko;
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        setTitle("Risiko erfassen: " + risiko.getClass().getSimpleName());
        
        // Initialize UI with the provided risk data
        setupUI();
        
        // Pre-fill fields from the risk object
        bezeichnungField.setText(risiko.getBezeichnung());
        wahrscheinlichkeitSpinner.setText(String.valueOf(risiko.getEintrittswahrscheinlichkeit()));
        kostenSpinner.setText(String.valueOf(risiko.getKostenImSchadensfall()));
       
        if (risiko instanceof InakzeptablesRisiko) {
            massnahmeField.setText(((InakzeptablesRisiko)risiko).getMassnahme());
        }
        if (risiko instanceof ExtremesRisiko) {
        	versicherungSpinner.setText(
        		    String.valueOf(((ExtremesRisiko) risiko).getVersicherungsbeitrag())
        		);;
        }
    }

   

	private void setupUI() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);

        // Basic fields
        grid.addRow(0, new Label("Bezeichnung:"), bezeichnungField);
        grid.addRow(1, new Label("Eintrittswahrscheinlichkeit:"), wahrscheinlichkeitSpinner);
        grid.addRow(2, new Label("Kosten im Schadensfall:"), kostenSpinner);

        // Dynamic fields
        int row = 3;
        if (risiko instanceof InakzeptablesRisiko) {
            grid.addRow(row++, new Label("Maßnahme:"), massnahmeField);
        }
        if (risiko instanceof ExtremesRisiko) {
            grid.addRow(row++, new Label("Versicherungsbeitrag:"), versicherungSpinner);
        }

        // Buttons
        Button neuButton = new Button("Neu");
        neuButton.setOnAction(e -> handleConfirm());

        Button abbrechenButton = new Button("Abbrechen");
        abbrechenButton.setOnAction(e -> close());

        grid.add(neuButton, 0, row);
        grid.add(abbrechenButton, 1, row);

        setScene(new Scene(grid));
    }

    private void handleConfirm() {
        if (!validateInput()) return;

        risiko.setBezeichnung(bezeichnungField.getText());

        try {
            float wahrscheinlichkeit = Float.parseFloat(wahrscheinlichkeitSpinner.getText());
            float kosten = Float.parseFloat(kostenSpinner.getText());

            risiko.seteintrittswahrscheinlichkeit(wahrscheinlichkeit);
            risiko.setkostenImSchadensfall(kosten);

            if (risiko instanceof InakzeptablesRisiko) {
                ((InakzeptablesRisiko) risiko).setMassnahme(massnahmeField.getText());
            }

            if (risiko instanceof ExtremesRisiko) {
                float beitrag = Float.parseFloat(versicherungSpinner.getText());
                ((ExtremesRisiko) risiko).setversicherungbeitrag(beitrag);
            }

            confirmed = true;
            close();

        } catch (NumberFormatException e) {
            showError("Bitte geben Sie gültige Zahlen ein.");
        }
    }

    private void showError(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}

	private boolean validateInput() {
        if (bezeichnungField.getText().isBlank()) {
            showAlert("Fehlende Bezeichnung");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
	}

	        
	        // Show/hide fields based on

