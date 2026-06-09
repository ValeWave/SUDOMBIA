package com.sudombia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bitácora del jugador. Almacena los ecosistemas completados
 * como stickers coleccionables con sus datos educativos.
 */
public class Logbook {

    private final List<Ecosystem> collectedStickers;

    public Logbook() {
        this.collectedStickers = new ArrayList<>();
    }

    /**
     * Agrega un ecosistema completado a la bitácora.
     * No permite duplicados.
     */
    public void addSticker(Ecosystem ecosystem) {
        boolean alreadyAdded = collectedStickers.stream()
                .anyMatch(e -> e.getName().equals(ecosystem.getName()));

        if (!alreadyAdded) {
            collectedStickers.add(ecosystem);
        }
    }

    /**
     * Verifica si un ecosistema ya tiene sticker en la bitácora.
     */
    public boolean hasSticker(String ecosystemName) {
        return collectedStickers.stream()
                .anyMatch(e -> e.getName().equals(ecosystemName));
    }

    /**
     * Devuelve cuántos stickers se han recolectado.
     */
    public int getStickerCount() {
        return collectedStickers.size();
    }

    /**
     * Devuelve la lista de stickers como no modificable.
     */
    public List<Ecosystem> getCollectedStickers() {
        return Collections.unmodifiableList(collectedStickers);
    }

    /**
     * Verifica si se completaron los 5 ecosistemas (juego terminado).
     */
    public boolean isGameComplete() {
        return collectedStickers.size() >= 5;
    }
}