package com.sudombia.model;

import java.util.List;

/**
 * Representa un ecosistema colombiano con sus datos ambientales
 * y el estado de progreso del jugador en él.
 */

public class Ecosystem {
    private final String name;
    private final String description;
    private final double altitudeMeters;
    private final double minTemp;
    private final double maxTemp;
    private final List<String> flora;
    private final List<String> fauna;
    private final String imagePath;

    // Las 4 piezas del rompecabezas fotográfico
    private final PuzzlePiece[] pieces;

    private boolean unlocked;

    public Ecosystem(String name,
                     String description,
                     double altitudeMeters,
                     double minTemp,
                     double maxTemp,
                     List<String> flora,
                     List<String> fauna,
                     String imagePath,
                     boolean unlocked) {
        this.name = name;
        this.description = description;
        this.altitudeMeters = altitudeMeters;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.flora = List.copyOf(flora);
        this.fauna = List.copyOf(fauna);
        this.imagePath = imagePath;
        this.unlocked = unlocked;
        this.pieces = new PuzzlePiece[4];

        initPieces();
    }

    /**
     * Inicializa las 4 piezas del rompecabezas como no obtenidas.
     */
    private void initPieces() {
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = new PuzzlePiece(i, false);
        }
    }

    /**
     * Marca la pieza correspondiente al Sudoku completado como obtenida.
     * @param sudokuIndex 0 a 3
     */
    public void unlockPiece(int sudokuIndex) {
        if (sudokuIndex >= 0 && sudokuIndex < pieces.length) {
            pieces[sudokuIndex].setObtained(true);
        }
    }

    /**
     * Verifica si las 4 piezas fueron obtenidas (ecosistema completo).
     */
    public boolean isComplete() {
        for (PuzzlePiece piece : pieces) {
            if (!piece.isObtained()) return false;
        }
        return true;
    }

    /**
     * Cuenta cuántas piezas fueron obtenidas hasta ahora.
     */
    public int getObtainedPiecesCount() {
        int count = 0;
        for (PuzzlePiece piece : pieces) {
            if (piece.isObtained()) count++;
        }
        return count;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getAltitudeMeters() { return altitudeMeters; }
    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public List<String> getFlora() { return flora; }
    public List<String> getFauna() { return fauna; }
    public String getImagePath() { return imagePath; }
    public PuzzlePiece[] getPieces() { return pieces; }
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
}
