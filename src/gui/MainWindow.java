package gui;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import risk.Risiko;

public class MainWindow extends Application {

    private TableView<Risiko> risikoTable;
    private ObservableList<Risiko> risikoList;
    private Controller controller;
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        this.risikoList = FXCollections.observableArrayList();
        this.controller = new Controller(primaryStage, this);

        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(createRisikoTable());

        primaryStage.setTitle("Risiko Management");
        primaryStage.setScene(new Scene(root, 800, 600));

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            String confirm = pk1.gui.util.InputView.create(
                primaryStage, "Beenden", "Möchten Sie das Programm wirklich beenden?", "Ja/Nein"
            ).showView();
            if (confirm != null && confirm.toLowerCase().startsWith("j")) {
                primaryStage.close();
            }
        });

        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu dateiMenu = new Menu("Datei");
        MenuItem ladenItem = new MenuItem("Laden");
        MenuItem speichernItem = new MenuItem("Speichern");
        MenuItem listeInDateiItem = new MenuItem("Risikoliste in Datei");

        ladenItem.setOnAction(e -> controller.ladeRisiken());
        speichernItem.setOnAction(e -> controller.speichereRisiken());
        listeInDateiItem.setOnAction(e -> controller.schreibeRisikolisteInDatei());

        dateiMenu.getItems().addAll(ladenItem, speichernItem, listeInDateiItem);

        Menu risikoMenu = new Menu("Risiko");
        MenuItem neuesRisikoItem = new MenuItem("Neues Risiko");
        neuesRisikoItem.setOnAction(e -> controller.showRisikoDialog(primaryStage));
        risikoMenu.getItems().add(neuesRisikoItem);

        Menu analyseMenu = new Menu("Anzeige");
        MenuItem maxRueckstellungItem = new MenuItem("Risiko mit max. Rückstellung");
        MenuItem summeRueckstellungItem = new MenuItem("Summe aller Rückstellungen");

        maxRueckstellungItem.setOnAction(e -> controller.zeigeRisikoMitMaxRueckstellung());
        summeRueckstellungItem.setOnAction(e -> controller.zeigeSummeAllerRueckstellungen());

        analyseMenu.getItems().addAll(maxRueckstellungItem, summeRueckstellungItem);

        menuBar.getMenus().addAll(dateiMenu, risikoMenu, analyseMenu);
        return menuBar;
    }

    private TableView<Risiko> createRisikoTable() {
        risikoTable = new TableView<>();
        risikoTable.setItems(risikoList);

        TableColumn<Risiko, String> bezeichnungCol = new TableColumn<>("Risiko");
        bezeichnungCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().toString()));

        risikoTable.getColumns().add(bezeichnungCol);
        risikoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return risikoTable;
    }

    public void refreshRisikoList(java.util.List<Risiko> neueListe) {
        risikoList.setAll(neueListe);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
