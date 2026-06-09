package com.sudombia.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Genera tableros de Sudoku válidos y únicos según el nivel de dificultad.
 * Usa backtracking aleatorizado para crear la solución completa,
 * luego elimina celdas según la dificultad para formar el puzzle.
 */

public class SudokuGenerator {
    public enum Difficulty {
        EASY(36),      // 36 celdas visibles
        MEDIUM(28),    // 28 celdas visibles
        HARD(22),      // 22 celdas visibles
        EXPERT(17);    // 17 celdas visibles (mínimo teórico válido)

        private final int givens;

        Difficulty(int givens) {
            this.givens = givens;
        }

        public int getGivens() {
            return givens;
        }
    }

    private final Random random;

    public SudokuGenerator() {
        this.random = new Random();
    }

    /**
     * Genera un SudokuBoard completo según la dificultad indicada.
     */
    public SudokuBoard generate(Difficulty difficulty) {
        int[][] solution = new int[SudokuBoard.SIZE][SudokuBoard.SIZE];
        fillGrid(solution);

        int[][] puzzle = copyGrid(solution);
        removeCells(puzzle, difficulty.getGivens());

        return new SudokuBoard(solution, puzzle);
    }

    /**
     * Rellena la cuadrícula completa con backtracking aleatorizado.
     */
    private boolean fillGrid(int[][] grid) {
        for (int row = 0; row < SudokuBoard.SIZE; row++) {
            for (int col = 0; col < SudokuBoard.SIZE; col++) {
                if (grid[row][col] == SudokuBoard.EMPTY) {
                    List<Integer> numbers = shuffledNumbers();
                    for (int num : numbers) {
                        if (isValidPlacement(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (fillGrid(grid)) return true;
                            grid[row][col] = SudokuBoard.EMPTY;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Elimina celdas del tablero completo para crear el puzzle.
     * Deja exactamente 'givens' celdas visibles.
     */
    private void removeCells(int[][] grid, int givens) {
        int cellsToRemove = (SudokuBoard.SIZE * SudokuBoard.SIZE) - givens;

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < SudokuBoard.SIZE * SudokuBoard.SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions, random);

        int removed = 0;
        for (int pos : positions) {
            if (removed >= cellsToRemove) break;
            int row = pos / SudokuBoard.SIZE;
            int col = pos % SudokuBoard.SIZE;
            grid[row][col] = SudokuBoard.EMPTY;
            removed++;
        }
    }

    /**
     * Verifica si un número puede colocarse en (row, col) sin violar reglas.
     */
    private boolean isValidPlacement(int[][] grid, int row, int col, int num) {
        // Verificar fila
        for (int c = 0; c < SudokuBoard.SIZE; c++) {
            if (grid[row][c] == num) return false;
        }

        // Verificar columna
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            if (grid[r][col] == num) return false;
        }

        // Verificar subcuadrícula 3x3
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == num) return false;
            }
        }

        return true;
    }

    /**
     * Retorna una lista del 1 al 9 en orden aleatorio.
     */
    private List<Integer> shuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SudokuBoard.SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers, random);
        return numbers;
    }

    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[SudokuBoard.SIZE][SudokuBoard.SIZE];
        for (int i = 0; i < SudokuBoard.SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, SudokuBoard.SIZE);
        }
        return copy;
    }
}
