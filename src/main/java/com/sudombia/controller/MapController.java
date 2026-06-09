package com.sudombia.controller;

import com.sudombia.App;
import com.sudombia.controller.SudokuController;
import com.sudombia.logic.SudokuGenerator;
import com.sudombia.model.Ecosystem;
import com.sudombia.model.Player;
import com.sudombia.util.EcosystemData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controlador del mapa de ecosistemas.
 * Muestra los 5 ecosistemas y gestiona cuáles están desbloqueados.
 */

public class MapController {
    @FXML private Label titleLabel;
    @FXML private VBox ecosystemsContainer;

    private Player player;
    private List<Ecosystem> ecosystems;

    @FXML
    public void initialize() {
        ecosystems = EcosystemData.getAll();
        titleLabel.setText("Elige tu ecosistema");
        renderEcosystems();
    }

    /**
     * Recibe el jugador desde el controlador anterior.
     */
    public void setPlayer(Player player) {
        this.player = player;
        renderEcosystems();
    }

    /**
     * Dibuja los botones de cada ecosistema según su estado.
     */
    private void renderEcosystems() {
        ecosystemsContainer.getChildren().clear();

        for (int i = 0; i < ecosystems.size(); i++) {
            Ecosystem eco = ecosystems.get(i);
            Button btn = createEcosystemButton(eco, i);
            ecosystemsContainer.getChildren().add(btn);
        }
    }

    /**
     * Crea el botón de un ecosistema con su estado visual.
     */
    private Button createEcosystemButton(Ecosystem eco, int index) {
        Button btn = new Button();
        btn.setPrefWidth(400);
        btn.setPrefHeight(60);

        if (eco.isUnlocked()) {
            int pieces = eco.getObtainedPiecesCount();
            btn.setText(eco.getName() + "  [" + pieces + "/4 piezas]");
            btn.setStyle(
                "-fx-background-color: #2E7D32;" +
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
            sudokuController.setup(player, ecosystem, SudokuGenerator.Difficulty.EASY);
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/sudombia/fxml/menu.fxml")
            );
            App.setScene(new Scene(loader.load(), 900, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
