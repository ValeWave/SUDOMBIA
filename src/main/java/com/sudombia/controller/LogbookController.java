package com.sudombia.controller;

import com.sudombia.App;
import com.sudombia.model.Ecosystem;
import com.sudombia.model.Logbook;
import com.sudombia.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controlador de la Bitácora del jugador.
 * Muestra los stickers coleccionables con datos educativos
 * de cada ecosistema completado.
 */

public class LogbookController {
    @FXML private Label titleLabel;
    @FXML private Label progressLabel;
    @FXML private VBox stickersContainer;

    private Player player;
    private Logbook logbook;
    private List<Ecosystem> ecosystems;

    @FXML
    public void initialize() {
        titleLabel.setText("Bitácora del Explorador");
    }

    /**
     * Recibe player y logbook desde el controlador anterior.
     */
    public void setup(Player player, Logbook logbook, List<Ecosystem> ecosystems) {
        this.player = player;
        this.logbook = logbook;
        this.ecosystems = ecosystems;
        System.out.println("Logbook recibido con stickers: " + logbook.getStickerCount());
        renderStickers();
    }

    /**
     * Dibuja los stickers obtenidos y los slots vacíos pendientes.
     */
    private void renderStickers() {
        stickersContainer.getChildren().clear();

        int count = logbook.getStickerCount();
        progressLabel.setText("Ecosistemas completados: " + count + " / 5");

        List<Ecosystem> stickers = logbook.getCollectedStickers();

        if (stickers.isEmpty()) {
            Label empty = new Label("Aún no has completado ningún ecosistema.\n¡Explora y resuelve los Sudokus!");
            empty.setStyle("-fx-text-fill: #A0A0C0; -fx-font-size: 15px;");
            empty.setWrapText(true);
            empty.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            stickersContainer.getChildren().add(empty);
            return;
        }

        for (Ecosystem eco : stickers) {
            VBox card = createStickerCard(eco);
            stickersContainer.getChildren().add(card);
        }

        if (logbook.isGameComplete()) {
            Label congrats = new Label("🌿 ¡Felicitaciones! Completaste todos los ecosistemas de Colombia.");
            congrats.setStyle("-fx-text-fill: #69F0AE; -fx-font-size: 16px; -fx-font-weight: bold;");
            congrats.setWrapText(true);
            congrats.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            stickersContainer.getChildren().add(congrats);
        }
    }

    /**
     * Crea la tarjeta visual de un ecosistema completado.
     */
    private VBox createStickerCard(Ecosystem eco) {
        VBox card = new VBox(8);
        card.setStyle(
            "-fx-background-color: #16213E;" +
            "-fx-border-color: #E8C84A;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 16;"
        );
        card.setMaxWidth(600);

        // Nombre del ecosistema
        Label name = new Label("🌍  " + eco.getName());
        name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E8C84A;");

        // Descripción
        Label desc = new Label(eco.getDescription());
        desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #C0C0D0;");
        desc.setWrapText(true);

        // Datos ambientales
        Label altitude = new Label("Altitud: " + (int) eco.getAltitudeMeters() + " m s.n.m.");
        Label temp = new Label("Temperatura: " + (int) eco.getMinTemp() +
                               " °C a " + (int) eco.getMaxTemp() + " °C");
        altitude.setStyle("-fx-font-size: 13px; -fx-text-fill: #A0C4FF;");
        temp.setStyle("-fx-font-size: 13px; -fx-text-fill: #A0C4FF;");

        // Flora
        Label floraTitle = new Label("Flora:");
        floraTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #69F0AE;");
        Label floraList = new Label(String.join(", ", eco.getFlora()));
        floraList.setStyle("-fx-font-size: 13px; -fx-text-fill: #C0C0D0;");

        // Fauna
        Label faunaTitle = new Label("Fauna:");
        faunaTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #FF9E80;");
        Label faunaList = new Label(String.join(", ", eco.getFauna()));
        faunaList.setStyle("-fx-font-size: 13px; -fx-text-fill: #C0C0D0;");

        card.getChildren().addAll(
            name, desc, altitude, temp,
            floraTitle, floraList,
            faunaTitle, faunaList
        );

        return card;
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
