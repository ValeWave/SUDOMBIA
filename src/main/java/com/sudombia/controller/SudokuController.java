package com.sudombia.controller;

import com.sudombia.App;
import com.sudombia.logic.SudokuBoard;
import com.sudombia.logic.SudokuGenerator;
import com.sudombia.model.Ecosystem;
import com.sudombia.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

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
    private Ecosystem ecosystem;
    private SudokuGenerator.Difficulty difficulty;

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
    public void setup(Player player, Ecosystem ecosystem,
                      SudokuGenerator.Difficulty difficulty) {
        this.player = player;
        this.ecosystem = ecosystem;
        this.difficulty = difficulty;

        ecosystemLabel.setText(ecosystem.getName());
        difficultyLabel.setText("Dificultad: " + difficulty.name());
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
        int sudokuIndex = getSudokuIndex();
        ecosystem.unlockPiece(sudokuIndex);
        player.completeSudoku(ecosystem.getName());

        messageLabel.setText("¡Sudoku completado! Pieza " +
            ecosystem.getObtainedPiecesCount() + "/4 obtenida.");
        messageLabel.setStyle("-fx-text-fill: #69F0AE; -fx-font-size: 14px;");

        checkButton.setDisable(true);
        hintButton.setDisable(true);
    }

    private int getSudokuIndex() {
        return ecosystem.getObtainedPiecesCount();
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
            mapController.setPlayer(player);
            App.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
