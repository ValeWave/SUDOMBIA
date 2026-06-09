package com.sudombia.logic;

/**
 * Gestiona la cuadrícula 9x9 del Sudoku.
 * Contiene la solución, el estado actual del jugador
 * y la lógica de validación y pistas.
 */

public class SudokuBoard {
    public static final int SIZE = 9;
    public static final int EMPTY = 0;

    // Tablero que ve y edita el jugador
    private final int[][] playerGrid;

    // Solución completa generada
    private final int[][] solutionGrid;

    // Celdas que vinieron predefinidas (no editables)
    private final boolean[][] fixed;

    // Celdas con error detectado
    private final boolean[][] errors;

    public SudokuBoard(int[][] solutionGrid, int[][] playerGrid) {
        this.solutionGrid = copyGrid(solutionGrid);
        this.playerGrid = copyGrid(playerGrid);
        this.fixed = new boolean[SIZE][SIZE];
        this.errors = new boolean[SIZE][SIZE];
        markFixedCells();
    }

    /**
     * Marca como fijas las celdas que ya vienen con valor en el tablero inicial.
     */
    private void markFixedCells() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (playerGrid[row][col] != EMPTY) {
                    fixed[row][col] = true;
                }
            }
        }
    }

    /**
     * Intenta colocar un valor en una celda.
     * @return false si la celda es fija y no se puede editar.
     */
    public boolean setValue(int row, int col, int value) {
        if (fixed[row][col]) return false;
        playerGrid[row][col] = value;
        errors[row][col] = false;
        return true;
    }

    /**
     * Comprueba el tablero completo contra la solución.
     * Marca en errors[][] las celdas incorrectas.
     * @return true si el tablero está completamente correcto.
     */
    public boolean checkBoard() {
        boolean allCorrect = true;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (playerGrid[row][col] == EMPTY ||
                    playerGrid[row][col] != solutionGrid[row][col]) {
                    errors[row][col] = true;
                    allCorrect = false;
                } else {
                    errors[row][col] = false;
                }
            }
        }
        return allCorrect;
    }

    /**
     * Revela el valor correcto de una celda vacía o incorrecta como pista.
     * @return true si se aplicó la pista, false si la celda ya era correcta.
     */
    public boolean applyHint(int row, int col) {
        if (playerGrid[row][col] == solutionGrid[row][col]) return false;
        playerGrid[row][col] = solutionGrid[row][col];
        fixed[row][col] = true;
        errors[row][col] = false;
        return true;
    }

    /**
     * Verifica si el tablero está completamente resuelto sin comprobar errores.
     */
    public boolean isSolved() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (playerGrid[row][col] != solutionGrid[row][col]) return false;
            }
        }
        return true;
    }

    // Utilidad: copia profunda de una matriz 9x9
    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    // Getters
    public int getValue(int row, int col) { return playerGrid[row][col]; }
    public boolean isFixed(int row, int col) { return fixed[row][col]; }
    public boolean hasError(int row, int col) { return errors[row][col]; }
    public int[][] getPlayerGrid() { return copyGrid(playerGrid); }
    public int[][] getSolutionGrid() { return copyGrid(solutionGrid); }
}
