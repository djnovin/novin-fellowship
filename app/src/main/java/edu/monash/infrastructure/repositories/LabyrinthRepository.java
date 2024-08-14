package edu.monash.infrastructure.repositories;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.monash.domain.entities.Cave;
import edu.monash.domain.entities.Labyrinth;

/**
 * Handles the loading of a Labyrinth from a file and logging its structure.
 * This class is part of the infrastructure layer, managing external concerns
 * such as file I/O and logging.
 */
public class LabyrinthRepository {
    private final Logger logger;

    /**
     * Constructs a LabyrinthRepository with the provided logger.
     *
     * @param logger The logger used to log messages.
     */
    public LabyrinthRepository(Logger logger) {
        this.logger = logger;
    }

    /**
     * Loads a Labyrinth from a specified file.
     * 
     * @param fileName The path to the file containing the labyrinth structure.
     * @return A Labyrinth object representing the loaded labyrinth.
     * @throws IOException If an error occurs while reading the file.
     */
    public Labyrinth loadFromFile(String fileName) throws IOException {
        Labyrinth labyrinth = new Labyrinth();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isEmpty = true;

            while ((line = reader.readLine()) != null) {
                isEmpty = false;
                logger.log(Level.INFO, "Reading line: {0}", line);

                String[] parts = line.split(",");
                if (parts.length != 5) {
                    logger.log(Level.SEVERE, "Invalid line format in file: {0}", line);
                    continue;
                }

                int caveId = Integer.parseInt(parts[0].trim());
                int northId = Integer.parseInt(parts[1].trim());
                int eastId = Integer.parseInt(parts[2].trim());
                int southId = Integer.parseInt(parts[3].trim());
                int westId = Integer.parseInt(parts[4].trim());

                labyrinth.addCave(caveId, northId, eastId, southId, westId);
            }

            if (isEmpty) {
                logger.severe("The labyrinth.txt file is empty.");
            } else {
                logger.info("Labyrinth successfully loaded.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading labyrinth.txt file: {0}", e.getMessage());
            throw e;
        }

        return labyrinth;
    }

    /**
     * Logs the structure of the given Labyrinth.
     *
     * @param labyrinth The Labyrinth object whose structure is to be logged.
     */
    public void logLabyrinthStructure(Labyrinth labyrinth) {
        for (Cave cave : labyrinth.getCaves().values()) {
            logger.log(Level.INFO, "Cave {0} - North: {1}, East: {2}, South: {3}, West: {4}", new Object[] {
                    cave.getId(),
                    cave.getNorth() != null ? cave.getNorth().getId() : "None",
                    cave.getEast() != null ? cave.getEast().getId() : "None",
                    cave.getSouth() != null ? cave.getSouth().getId() : "None",
                    cave.getWest() != null ? cave.getWest().getId() : "None" });
        }
    }
}
