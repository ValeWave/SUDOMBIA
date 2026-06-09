package com.sudombia.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa al jugador y almacena su progreso global en SUDOMBIA.
 */ 

public class Player {
    private static final int MAX_HINTS_PER_SUDOKU = 3;

    private String name;

    // Cuántos Sudokus completó por ecosistema (clave = nombre del ecosistema)
    private final Map<String, Integer> completedSudokus;

    // Pistas restantes para el Sudoku actual
    private int hintsRemaining;

    public Player(String name) {
        this.name = name;
        this.completedSudokus = new HashMap<>();
        this.hintsRemaining = MAX_HINTS_PER_SUDOKU;
    }

    /**
     * Registra un Sudoku completado en el ecosistema dado.
     */
    public void completeSudoku(String ecosystemName) {
        int current = completedSudokus.getOrDefault(ecosystemName, 0);
        completedSudokus.put(ecosystemName, current + 1);
    }

    /**
     * Devuelve cuántos Sudokus completó el jugador en un ecosistema.
     */
    public int getCompletedSudokus(String ecosystemName) {
        return completedSudokus.getOrDefault(ecosystemName, 0);
    }

    /**
     * Verifica si un ecosistema está completamente terminado (4 Sudokus).
     */
    public boolean isEcosystemComplete(String ecosystemName) {
        return getCompletedSudokus(ecosystemName) >= 4;
    }

    /**
     * Usa una pista. Devuelve true si había pistas disponibles.
     */
    public boolean useHint() {
        if (hintsRemaining > 0) {
            hintsRemaining--;
            return true;
        }
        return false;
    }

    /**
     * Reinicia las pistas al comenzar un nuevo Sudoku.
     */
    public void resetHints() {
        hintsRemaining = MAX_HINTS_PER_SUDOKU;
    }

    // Getters y setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHintsRemaining() { return hintsRemaining; }
    
}
