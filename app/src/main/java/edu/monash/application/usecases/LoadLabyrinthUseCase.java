package edu.monash.application.usecases;

import java.io.IOException;

import edu.monash.domain.entities.Labyrinth;
import edu.monash.infrastructure.repositories.LabyrinthRepository;

/**
 * Use case for loading a Labyrinth.
 * This class is part of the application layer and coordinates the process of loading a labyrinth.
 */
public class LoadLabyrinthUseCase {
    private final LabyrinthRepository labyrinthRepository;

    /**
     * Constructs a LoadLabyrinthUseCase with the provided LabyrinthRepository.
     *
     * @param labyrinthRepository The repository responsible for loading the labyrinth.
     */
    public LoadLabyrinthUseCase(LabyrinthRepository labyrinthRepository) {
        this.labyrinthRepository = labyrinthRepository;
    }

    /**
     * Executes the use case of loading a labyrinth from a specified file.
     *
     * @param fileName The path to the file containing the labyrinth structure.
     * @return The loaded Labyrinth object.
     * @throws RuntimeException If loading the labyrinth fails.
     */
    public Labyrinth execute(String fileName) {
        try {
            return labyrinthRepository.loadFromFile(fileName);
        } catch (IOException e) {
            // Handle the exception (e.g., log it, rethrow it, etc.)
            throw new RuntimeException("Failed to load labyrinth", e);
        }
    }
}
