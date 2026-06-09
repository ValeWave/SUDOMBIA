package com.sudombia.controller;

import com.sudombia.App;
import com.sudombia.controller.MenuController;
import com.sudombia.model.Ecosystem;
import com.sudombia.model.Logbook;
import com.sudombia.model.Player;
import com.sudombia.logic.SudokuGenerator;
import com.sudombia.util.EcosystemData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MapController {

    @FXML private Label titleLabel;
    @FXML private VBox ecosystemsContainer;

    private Player player;
    private Logbook logbook;
    private List<Ecosystem> ecosystems;

    @FXML
    public void initialize() {
        titleLabel.setText("Elige tu ecosistema");
    }

    /**
     * Recibe player y logbook desde MenuController.
     */
    public void setup(Player player, Logbook logbook, List<Ecosystem> ecosystems) {
        this.player = player;
        this.logbook = logbook;
        this.ecosystems = ecosystems;
        unlockEcosystems();
        renderEcosystems();
    }   

    /**
     * Desbloquea el siguiente ecosistema si el anterior está completo.
     */
    private void unlockEcosystems() {
        for (int i = 1; i < ecosystems.size(); i++) {
            Ecosystem previous = ecosystems.get(i - 1);
            Ecosystem current = ecosystems.get(i);
            if (previous.isComplete() && !current.isUnlocked()) {
                current.setUnlocked(true);
            }
        }
    }

    private void renderEcosystems() {
        ecosystemsContainer.getChildren().clear();

        for (Ecosystem eco : ecosystems) {
            Button btn = createEcosystemButton(eco);
            ecosystemsContainer.getChildren().add(btn);
        }
    }

    private Button createEcosystemButton(Ecosystem eco) {
        Button btn = new Button();
        btn.setPrefWidth(400);
        btn.setPrefHeight(60);

        if (eco.isUnlocked()) {
            int pieces = eco.getObtainedPiecesCount();
            String status = eco.isComplete() ? "✅ Completo" : pieces + "/4 piezas";
            btn.setText(eco.getName() + "  [" + status + "]");
            btn.setStyle(
                "-fx-background-color: " + (eco.isComplete() ? "#1B5E20" : "#2E7D32") + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-background-radius: 8;"
            );
            btn.setOnAction(e -> onEcosystemSelected(eco));
        } else {
            btn.setText("🔒  " + eco.getName());
            btn.setStyle(
                "-fx-background-color: #37474F;" +
                "-fx-text-fill: #78909C;" +
                "-fx-font-size: 15px;" +
                "-fx-background-radius: 8;"
            );
            btn.setDisable(true);
        }

        return btn;
    }

    private void onEcosystemSelected(Ecosystem ecosystem) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/sudombia/fxml/sudoku.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            SudokuController sudokuController = loader.getController();
            sudokuController.setup(player, logbook, ecosystems, ecosystem,
                getDifficulty(ecosystem.getObtainedPiecesCount()));
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dificultad progresiva según cuántas piezas tiene el ecosistema.
     */
    private SudokuGenerator.Difficulty getDifficulty(int piecesObtained) {
        switch (piecesObtained) {
            case 1: return SudokuGenerator.Difficulty.MEDIUM;
            case 2: return SudokuGenerator.Difficulty.HARD;
            case 3: return SudokuGenerator.Difficulty.EXPERT;
            default: return SudokuGenerator.Difficulty.EASY;
        }
    }

    @FXML
    private void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/sudombia/fxml/menu.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            MenuController menuController = loader.getController();
            menuController.setup(player, logbook, ecosystems);
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}