package com.sudombia.controller;

import com.sudombia.model.Player;

import java.io.IOException;
import java.util.List;

import com.sudombia.App;
import com.sudombia.model.Ecosystem;
import com.sudombia.model.Logbook;
import com.sudombia.util.EcosystemData;
import com.sudombia.controller.LogbookController;
import com.sudombia.controller.MapController;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controlador del menú principal de SUDOMBIA.
 * Gestiona los botones Jugar, Bitácora y Opciones.
 */
public class MenuController {

    @FXML private Label titleLabel;
    @FXML private Button playButton;
    @FXML private Button logbookButton;
    @FXML private Button optionsButton;

    private Player player;
    private Logbook logbook;
    private List<Ecosystem> ecosystems;
    /**
     * Se ejecuta automáticamente al cargar el FXML.
     */
    @FXML
    public void initialize() {
        player = new Player("Explorador");
        logbook = new Logbook();
        ecosystems = EcosystemData.getAll();
        titleLabel.setText("SUDOMBIA");
        updateLogbookButton();
    }

    public void setup(Player player, Logbook logbook, List<Ecosystem> ecosystems) {
        this.player = player;
        this.logbook = logbook;
        this.ecosystems = ecosystems;
        updateLogbookButton();
    }

    /**
     * Muestra cuántos stickers tiene el jugador en el botón de bitácora.
     */
    private void updateLogbookButton() {
        int count = logbook.getStickerCount();
        logbookButton.setText("Bitácora (" + count + "/5)");
    }

    @FXML
    private void onPlayClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/sudombia/fxml/map.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            MapController mapController = loader.getController();
            mapController.setup(player, logbook, ecosystems);
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void onLogbookClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/sudombia/fxml/logbook.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            LogbookController logbookController = loader.getController();
            logbookController.setup(player, logbook, ecosystems);
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onOptionsClicked() {
        System.out.println("Abrir opciones");
        // TODO: navegar a OptionsView
    }
}