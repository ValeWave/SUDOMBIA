package com.sudombia.controller;

import com.sudombia.App;
import com.sudombia.logic.SudokuBoard;
import com.sudombia.logic.SudokuGenerator;
import com.sudombia.model.Ecosystem;
import com.sudombia.model.Logbook;
import com.sudombia.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.List;

/**
 * Controlador del tablero de Sudoku.
 * Gestiona la cuadrícula, selección de celdas, validación y pistas.
 */

public class SudokuController {
    @FXML private GridPane sudokuGrid;
    @FXML private Label ecosystemLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label hintsLabel;
    @FXML private Label messageLabel;
    @FXML private Button checkButton;
    @FXML private Button hintButton;

    private SudokuBoard board;
    private Player player;
    private Logbook logbook;
    private Ecosystem ecosystem;
    private SudokuGenerator.Difficulty difficulty;
    private List<Ecosystem> ecosystems;

    // Celda actualmente seleccionada
    private int selectedRow = -1;
    private int selectedCol = -1;

    // Referencia a los StackPane de cada celda
    private StackPane[][] cells;

    // Colores
    private static final String COLOR_BACKGROUND   = "#1A1A2E";
    private static final String COLOR_FIXED        = "#2A2A4A";
    private static final String COLOR_EMPTY        = "#16213E";
    private static final String COLOR_SELECTED     = "#0F3460";
    private static final String COLOR_ERROR        = "#7B1A1A";
    private static final String COLOR_BORDER_THICK = "#E8C84A";
    private static final String COLOR_BORDER_THIN  = "#3A3A5A";

    @FXML
    public void initialize() {
        cells = new StackPane[SudokuBoard.SIZE][SudokuBoard.SIZE];
    }

    /**
     * Recibe los datos desde MapController y construye el tablero.
     */
    public void setup(Player player, Logbook logbook, List<Ecosystem> ecosystems,
                    Ecosystem ecosystem, SudokuGenerator.Difficulty difficulty) {
        this.player = player;
        this.logbook = logbook;
        this.ecosystems = ecosystems;
        this.ecosystem = ecosystem;
        this.difficulty = difficulty;

        ecosystemLabel.setText(ecosystem.getName());
        difficultyLabel.setText("Dificultad: " + difficulty.name());
        player.resetHints();
        updateHintsLabel();
        messageLabel.setText("");

        SudokuGenerator generator = new SudokuGenerator();
        board = generator.generate(difficulty);
        buildGrid();
    }

    /**
     * Construye visualmente la cuadrícula 9x9.
     */
    private void buildGrid() {
        sudokuGrid.getChildren().clear();

        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int col = 0; col < SudokuBoard.SIZE; col++) {
                StackPane cell = createCell(row, col);
                cells[row][col] = cell;
                sudokuGrid.add(cell, col, row);
            }
        }
    }

    /**
     * Crea una celda individual del tablero.
     */
    private StackPane createCell(int row, int col) {
    StackPane cell = new StackPane();
    cell.setPrefSize(44, 44);

    Rectangle bg = new Rectangle(44, 44);

    // Subcuadrícula 3x3 alterna entre dos colores
    boolean isAlternateBox = ((row / 3) + (col / 3)) % 2 == 0;
    if (board.isFixed(row, col)) {
        bg.setFill(Color.web(isAlternateBox ? "#2A2A4A" : "#1E3A2A"));
    } else {
        bg.setFill(Color.web(isAlternateBox ? "#16213E" : "#0D2A1E"));
    }

    bg.setStroke(Color.web("#3A3A5A"));
    bg.setStrokeWidth(0.5);

    Label label = new Label();
    int value = board.getValue(row, col);
    if (value != SudokuBoard.EMPTY) {
        label.setText(String.valueOf(value));
        label.setStyle(
            board.isFixed(row, col)
            ? "-fx-font-size: 16px; -fx-text-fill: #E8C84A; -fx-font-weight: bold;"
            : "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;"
        );
    }

    cell.getChildren().addAll(bg, label);
    StackPane.setAlignment(label, Pos.CENTER);

    final int r = row;
    final int c = col;
    cell.setOnMouseClicked(e -> selectCell(r, c));

    return cell;
}

    /**
     * Selecciona una celda y la resalta.
     */
    private void selectCell(int row, int col) {
        if (board.isFixed(row, col)) return;

        selectedRow = row;
        selectedCol = col;
        refreshGrid();

        // Captura teclado
        sudokuGrid.getScene().setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Maneja la entrada del teclado para escribir números.
     */
    private void handleKeyPress(KeyEvent event) {
        if (selectedRow == -1 || selectedCol == -1) return;

        String key = event.getText();
        if (key.matches("[1-9]")) {
            int value = Integer.parseInt(key);
            board.setValue(selectedRow, selectedCol, value);
            messageLabel.setText("");
            refreshGrid();

            if (board.isSolved()) {
                onPuzzleSolved();
            }
        } else if (event.getCode().toString().equals("BACK_SPACE") ||
                   event.getCode().toString().equals("DELETE")) {
            board.setValue(selectedRow, selectedCol, SudokuBoard.EMPTY);
            refreshGrid();
        }
    }

    /**
     * Refresca visualmente todas las celdas.
     */
    private void refreshGrid() {
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int col = 0; col < SudokuBoard.SIZE; col++) {
                StackPane cell = cells[row][col];
                Rectangle bg = (Rectangle) cell.getChildren().get(0);
                Label label = (Label) cell.getChildren().get(1);

                // Color de fondo
                boolean isAlternateBox = ((row / 3) + (col / 3)) % 2 == 0;
                if (row == selectedRow && col == selectedCol) {
                    bg.setFill(Color.web(COLOR_SELECTED));
                } else if (board.hasError(row, col)) {
                    bg.setFill(Color.web(COLOR_ERROR));
                } else if (board.isFixed(row, col)) {
                    bg.setFill(Color.web(isAlternateBox ? "#2A2A4A" : "#1E3A2A"));
                } else {
                    bg.setFill(Color.web(isAlternateBox ? "#16213E" : "#0D2A1E"));
                }

                // Valor
                int value = board.getValue(row, col);
                if (value != SudokuBoard.EMPTY) {
                    label.setText(String.valueOf(value));
                    label.setStyle(
                        board.isFixed(row, col)
                        ? "-fx-font-size: 16px; -fx-text-fill: #E8C84A; -fx-font-weight: bold;"
                        : "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;"
                    );
                } else {
                    label.setText("");
                }
            }
        }
    }

    /**
     * Devuelve el color del borde según la posición (borde grueso cada 3 celdas).
     */
    private Color getBorderColor(int row, int col) {
        return Color.web(COLOR_BORDER_THICK);
    }

    /**
     * Borde más grueso en los límites de subcuadrículas 3x3.
     */
    private double getBorderWidth(int row, int col) {
        boolean thickRight  = (col + 1) % 3 == 0 && col != 8;
        boolean thickBottom = (row + 1) % 3 == 0 && row != 8;
        if (thickRight || thickBottom) return 2.0;
        return 0.5;
    }

    @FXML
    private void onCheckClicked() {
        boolean solved = board.checkBoard();
        if (solved) {
            onPuzzleSolved();
        } else {
            messageLabel.setText("Hay errores marcados en rojo. ¡Sigue intentando!");
            messageLabel.setStyle("-fx-text-fill: #FF5252; -fx-font-size: 13px;");
            refreshGrid();
        }
    }

    @FXML
    private void onHintClicked() {
        if (!player.useHint()) {
            messageLabel.setText("No te quedan pistas.");
            messageLabel.setStyle("-fx-text-fill: #FF9800; -fx-font-size: 13px;");
            return;
        }

        // Busca la primera celda vacía o incorrecta
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int col = 0; col < SudokuBoard.SIZE; col++) {
                if (board.applyHint(row, col)) {
                    updateHintsLabel();
                    refreshGrid();
                    messageLabel.setText("Pista aplicada.");
                    messageLabel.setStyle("-fx-text-fill: #69F0AE; -fx-font-size: 13px;");
                    return;
                }
            }
        }
    }

    private void onPuzzleSolved() {
        int sudokuIndex = ecosystem.getObtainedPiecesCount();
        ecosystem.unlockPiece(sudokuIndex);
        player.completeSudoku(ecosystem.getName());

        checkButton.setDisable(true);
        hintButton.setDisable(true);

        if (ecosystem.isComplete()) {
            logbook.addSticker(ecosystem);
            System.out.println("Stickers en logbook: " + logbook.getStickerCount());
            System.out.println("Ecosistema completo: " + ecosystem.isComplete());
            showPieceDialog(true);
        } else {
            showPieceDialog(false);
        }
    }

    /**
     * Muestra la ventana emergente al ganar una pieza.
     */
    private void showPieceDialog(boolean ecosystemComplete) {
        // Overlay oscuro
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
        overlay.setPrefSize(900, 600);

        // Tarjeta central
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(380);
        card.setMaxHeight(320);
        card.setStyle(
            "-fx-background-color: #16213E;" +
            "-fx-border-color: #E8C84A;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 32;"
        );

        // Título
        Label title = new Label(ecosystemComplete
            ? "¡Ecosistema completo!"
            : "¡Pieza obtenida!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #E8C84A;");

        // Pieza visual (placeholder hasta tener pixel art)
        Label piece = new Label("[ PIEZA " + ecosystem.getObtainedPiecesCount() + " ]");
        piece.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #E8C84A;" +
            "-fx-background-color: #0F3460;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 16 24;"
        );

        // Info
        Label info = new Label(ecosystemComplete
            ? "Sticker agregado a tu Bitácora.\n¡Desbloqueaste el siguiente ecosistema!"
            : "Pieza " + ecosystem.getObtainedPiecesCount() + "/4 obtenida.\n" + ecosystem.getName());
        info.setStyle("-fx-font-size: 14px; -fx-text-fill: #C0C0D0;");
        info.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        info.setWrapText(true);

        // Botones
        HBox buttons = new HBox(12);
        buttons.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("✕ Cerrar");
        closeBtn.setStyle(
            "-fx-background-color: #607D8B;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 20;"
        );

        Button nextBtn = new Button("Siguiente →");
        nextBtn.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8 20;"
        );

        buttons.getChildren().addAll(closeBtn, nextBtn);
        card.getChildren().addAll(title, piece, info, buttons);
        overlay.getChildren().add(card);

        // Agregar overlay encima del layout actual
        StackPane root = (StackPane) sudokuGrid.getScene().getRoot();
        root.getChildren().add(overlay);

        // Cerrar overlay
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        // Siguiente sudoku o ir al mapa
        nextBtn.setOnAction(e -> {
            root.getChildren().remove(overlay);
            if (ecosystemComplete) {
                goToMap();
            } else {
                loadNextSudoku();
            }
        });
    }

    private void loadNextSudoku() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/sudombia/fxml/sudoku.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            SudokuController sudokuController = loader.getController();
            sudokuController.setup(player, logbook, ecosystems, ecosystem,
                getDifficultyForEcosystem());
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SudokuGenerator.Difficulty getDifficultyForEcosystem() {
        switch (ecosystem.getObtainedPiecesCount()) {
            case 1: return SudokuGenerator.Difficulty.MEDIUM;
            case 2: return SudokuGenerator.Difficulty.HARD;
            case 3: return SudokuGenerator.Difficulty.EXPERT;
            default: return SudokuGenerator.Difficulty.EASY;
        }
    }

    private void goToMap() {
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

    private void updateHintsLabel() {
        hintsLabel.setText("Pistas: " + player.getHintsRemaining());
    }

    @FXML
    private void onBackClicked() {
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
}
