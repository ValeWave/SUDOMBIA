package com.sudombia.util;

import com.sudombia.model.Ecosystem;

import java.util.List;

/**
 * Datos reales de los 5 ecosistemas colombianos de SUDOMBIA.
 * Fuente: IDEAM 2023.
 */

public class EcosystemData {
    private EcosystemData() {
        // Clase utilitaria, no instanciable
    }

    public static List<Ecosystem> getAll() {
        return List.of(
            sierraNevada(),
            amazonia(),
            tatacoa(),
            paramo(),
            costa()
        );
    }

    public static Ecosystem sierraNevada() {
        return new Ecosystem(
            "Sierra Nevada de Santa Marta",
            "La montaña costera más alta del mundo. Hogar de comunidades indígenas y especies únicas.",
            6779,
            -5, 5,
            List.of("Frailejón", "Pino romerón", "Musgo de páramo"),
            List.of("Oso andino", "Cóndor andino", "Danta de montaña"),
            "images/ecosystems/sierra_nevada.png",
            true  // primer ecosistema desbloqueado
        );
    }

    public static Ecosystem amazonia() {
        return new Ecosystem(
            "Amazonía",
            "La selva tropical más grande del mundo. Mayor biodiversidad del planeta.",
            200,
            25, 35,
            List.of("Victoria regia", "Ceiba", "Heliconia"),
            List.of("Jaguar", "Anaconda", "Delfín rosado"),
            "images/ecosystems/amazonia.png",
            false
        );
    }

    public static Ecosystem tatacoa() {
        return new Ecosystem(
            "Desierto de la Tatacoa",
            "Segundo desierto más grande de Colombia. Cielos despejados ideales para astronomía.",
            300,
            20, 43,
            List.of("Cactus columnar", "Cardón", "Tuna"),
            List.of("Iguana", "Cuyo", "Buitre americano"),
            "images/ecosystems/tatacoa.png",
            false
        );
    }

    public static Ecosystem paramo() {
        return new Ecosystem(
            "Páramo Andino",
            "Ecosistema exclusivo de los Andes. Regula el agua dulce de toda la región.",
            3500,
            0, 10,
            List.of("Espeletia", "Puya", "Musgo blanco"),
            List.of("Oso andino", "Curiquingue", "Venado de cola blanca"),
            "images/ecosystems/paramo.png",
            false
        );
    }

    public static Ecosystem costa() {
        return new Ecosystem(
            "Costa Caribe / Pacífico",
            "Manglares, arrecifes de coral y playas. Alta diversidad marina.",
            0,
            27, 32,
            List.of("Mangle rojo", "Palma de coco", "Seagrass"),
            List.of("Tortuga marina", "Fragata", "Manatí"),
            "images/ecosystems/costa.png",
            false
        );
    }
}
