package com.sudombia.model;

/**
 * Representa una pieza del rompecabezas fotográfico de un ecosistema.
 * Cada ecosistema tiene 4 piezas, una por Sudoku completado.
 */

public class PuzzlePiece {
    // Posición en el rompecabezas (0 = superior izquierda, sentido horario)
    private final int position;

    private boolean obtained;

    public PuzzlePiece(int position, boolean obtained) {
        this.position = position;
        this.obtained = obtained;
    }

    // Getters y setters
    public int getPosition() { return position; }
    public boolean isObtained() { return obtained; }
    public void setObtained(boolean obtained) { this.obtained = obtained; }

    @Override
    public String toString() {
        return "PuzzlePiece{position=" + position + ", obtained=" + obtained + "}";
    }
}
