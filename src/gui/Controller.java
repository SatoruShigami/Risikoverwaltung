package gui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Datenhaltung.DaoSerialImpl;
import Datenhaltung.PersistenzException;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pk1.gui.util.InputView;
import pk1.gui.util.MessageView;
import risk.*;

public class Controller {
    private final Risikoverwaltung verwaltung;
    private final DaoSerialImpl dao;
    private final Stage primaryStage;
    private final MainWindow mainWindow;

    public Controller(Stage primaryStage, MainWindow mainWindow) {
        this.primaryStage = primaryStage;
        this.mainWindow = mainWindow;
        this.verwaltung = new Risikoverwaltung();
        this.dao = new DaoSerialImpl();
    }

    public void ladeRisiken() {
        try {
            List<Risiko> geladene = dao.laden();
            verwaltung.setRisikoListe(geladene);
            mainWindow.refreshRisikoList(geladene);
            showMessage("Risiken erfolgreich geladen");
        } catch (PersistenzException e) {
            showError("Fehler beim Laden: " + e.getMessage());
        }
    }

    public void speichereRisiken() {
        try {
            dao.speichern(verwaltung.getRisikoListe());
            showMessage("Risiken erfolgreich gespeichert");
        } catch (PersistenzException e) {
            showError("Fehler beim Speichern: " + e.getMessage());
        }
    }

    public void schreibeRisikolisteInDatei() {
        String dateiname = InputView.create(primaryStage, "Export", "Dateiname für Export eingeben:", null).showView();
        if (dateiname == null || dateiname.isEmpty()) return;
        try (FileWriter writer = new FileWriter(dateiname)) {
            for (Risiko risiko : verwaltung.getRisikoListe()) {
                writer.write(risiko.toString() + System.lineSeparator());
            }
            MessageView.create(primaryStage, "Info", "Risikoliste wurde erfolgreich exportiert.").showView();
        } catch (IOException e) {
            MessageView.create(primaryStage, "Fehler", "Fehler beim Schreiben der Datei: " + e.getMessage()).showView();
        }
    }

    public void zeigeRisikoMitMaxRueckstellung() {
        try {
            Risiko maxRisiko = verwaltung.sucheRisikoMitMaxRueckstellung();
            if (maxRisiko != null) {
                MessageView.create(primaryStage, "Max. Rückstellung", "Max Risiko ist :" + maxRisiko.toString()).showView();
            } else {
                MessageView.create(primaryStage, "Info", "Kein Risiko gefunden.").showView();
            }
        } catch (Exception e) {
            MessageView.create(primaryStage, "Fehler", "Fehler bei der Analyse: " + e.getMessage()).showView();
        }
    }

    public void zeigeSummeAllerRueckstellungen() {
        float summe = verwaltung.berechneSummeRueckstellungen();
        MessageView.create(primaryStage, "Summe Rückstellungen", "Summe: " + summe).showView();
    }

    public void showRisikoDialog(Stage owner) {
        Risiko risiko = createRiskBasedOnInput(owner);
        if (risiko == null) return;

        RisikoErfassungView dialog = new RisikoErfassungView(owner, risiko);
        dialog.showAndWait();

        if (dialog.isConfirmed()) {
            verwaltung.aufnehmen(risiko);
            mainWindow.refreshRisikoList(verwaltung.getRisikoListe());
        }
    }

    private Risiko createRiskBasedOnInput(Stage owner) {
        final float LIMIT = 10000.0f;
        final float KOSTENLIMIT = 1000000.0f;

        Map<String, String> input = showBasicInputDialog(owner);
        if (input == null) return null;

        try {
            String bez = input.get("bezeichnung");
            float wkeit = Float.parseFloat(input.get("wahrscheinlichkeit"));
            float kosten = Float.parseFloat(input.get("kosten"));

            if (kosten > KOSTENLIMIT) {
                Map<String, String> extreme = showExtremeRiskDialog(owner, kosten * wkeit);
                if (extreme == null) return null;
                return new ExtremesRisiko(bez, wkeit, kosten, extreme.get("massnahme"),
                        Float.parseFloat(extreme.get("versicherung")));
            } else if (kosten * wkeit > LIMIT) {
                String massnahme = showInakzeptabelDialog(owner);
                if (massnahme == null) return null;
                return new InakzeptablesRisiko(bez, wkeit, kosten, massnahme);
            } else {
                return new akzeptablesRisiko(bez, wkeit, kosten);
            }
        } catch (NumberFormatException e) {
            MessageView.create(owner, "Fehler", "Ungültige Eingaben!").showView();
            return null;
        }
    }

    private Map<String, String> showBasicInputDialog(Stage owner) {
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Basisdaten Risiko");

        GridPane grid = new GridPane();
        TextField bezField = new TextField();
        TextField wkeitField = new TextField();
        TextField kostenField = new TextField();

        grid.addRow(0, new Label("Bezeichnung:"), bezField);
        grid.addRow(1, new Label("Wahrscheinlichkeit (0-1):"), wkeitField);
        grid.addRow(2, new Label("Kosten:"), kostenField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Map<String, String> map = new HashMap<>();
                map.put("bezeichnung", bezField.getText());
                map.put("wahrscheinlichkeit", wkeitField.getText());
                map.put("kosten", kostenField.getText());
                return map;
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private Map<String, String> showExtremeRiskDialog(Stage owner, float beitrag) {
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Extremes Risiko");

        GridPane grid = new GridPane();
        TextField massnahmeField = new TextField("Versicherung abschließen");
        TextField versicherungField = new TextField(String.format("%.2f", beitrag));

        grid.addRow(0, new Label("Maßnahme:"), massnahmeField);
        grid.addRow(1, new Label("Versicherungsbeitrag:"), versicherungField);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Map<String, String> result = new HashMap<>();
                result.put("massnahme", massnahmeField.getText());
                result.put("versicherung", versicherungField.getText());
                return result;
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private String showInakzeptabelDialog(Stage owner) {
        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("Maßnahme eingeben");

        TextField field = new TextField();
        dialog.getDialogPane().setContent(new VBox(new Label("Maßnahme:"), field));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> btn == ButtonType.OK ? field.getText() : null);
        return dialog.showAndWait().orElse(null);
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
