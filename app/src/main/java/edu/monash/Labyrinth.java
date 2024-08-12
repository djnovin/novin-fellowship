package edu.monash;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Labyrinth {
    private static final Logger logger = LoggerSingleton.getLogger();
    private final Map<Integer, Cave> caves;

    public Labyrinth() {
        this.caves = new HashMap<>();
    }

    // Load the labyrinth structure from a file
    public void loadFromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int caveId = Integer.parseInt(parts[0]);
                int northId = Integer.parseInt(parts[1]);
                int eastId = Integer.parseInt(parts[2]);
                int southId = Integer.parseInt(parts[3]);
                int westId = Integer.parseInt(parts[4]);

                Cave cave = caves.computeIfAbsent(caveId, Cave::new);
                if (northId != 0) {
                    cave.setNorth(caves.computeIfAbsent(northId, Cave::new));
                }
                if (eastId != 0) {
                    cave.setEast(caves.computeIfAbsent(eastId, Cave::new));
                }
                if (southId != 0) {
                    cave.setSouth(caves.computeIfAbsent(southId, Cave::new));
                }
                if (westId != 0) {
                    cave.setWest(caves.computeIfAbsent(westId, Cave::new));
                }
            }
            logger.info("Labyrinth loaded successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading labyrinth from file: {0}", e.getMessage());
            throw new IOException("Error loading labyrinth from file: " + e.getMessage(), e);
        }
    }

    // Get a specific cave by its ID
    public Cave getCave(int id) {
        return caves.get(id);
    }

    // Get the starting cave (assumed to be cave with ID 1)
    public Cave getStartingCave() {
        return caves.get(1); // Assuming cave 1 is the starting point
    }

    // Log the labyrinth structure
    public void logLabyrinthStructure() {
        for (Cave cave : caves.values()) {
            logger.log(Level.INFO, "Cave {0} - North: {1}, East: {2}, South: {3}, West: {4}", new Object[]{cave.getId(), cave.getNorth() != null ? cave.getNorth().getId() : "None", cave.getEast() != null ? cave.getEast().getId() : "None", cave.getSouth() != null ? cave.getSouth().getId() : "None", cave.getWest() != null ? cave.getWest().getId() : "None"});
        }
    }
}
