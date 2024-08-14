package edu.monash;

import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.monash.adapters.presenters.ConsoleOutputAdapter;
import edu.monash.application.usecases.GameUseCase;
import edu.monash.application.usecases.LoadLabyrinthUseCase;
import edu.monash.domain.entities.Cave;
import edu.monash.domain.entities.Fellowship;
import edu.monash.domain.entities.Labyrinth;
import edu.monash.domain.services.NavigationService;
import edu.monash.infrastructure.repositories.LabyrinthRepository;

public class Main {
    private static final Logger logger = LoggerSingleton.getLogger();

    public static void main(String[] args) {
        ConsoleOutputAdapter outputAdapter = new ConsoleOutputAdapter();
        LabyrinthRepository labyrinthRepository = new LabyrinthRepository(logger);
        LoadLabyrinthUseCase loadLabyrinthUseCase = new LoadLabyrinthUseCase(labyrinthRepository);
        Labyrinth labyrinth = loadLabyrinthUseCase.execute("app/src/main/resources/labyrinth.txt");
        NavigationService navigationService = new NavigationService(labyrinth);

        GameUseCase gameUseCase = new GameUseCase(outputAdapter);
        Fellowship fellowship = gameUseCase.startGame();

        outputAdapter.displayWelcomeMessage();

        if (labyrinth == null) {
            logger.log(Level.SEVERE, "Labyrinth failed to load.");
            return;
        }
        outputAdapter.displayMessage("Labyrinth loaded successfully.");

        try (Scanner scanner = new Scanner(System.in)) {
            Cave currentCave = labyrinth.getStartingCave();

            while (fellowship.isFellowshipAlive()) {
                outputAdapter.displayMessage("You are currently in cave " + currentCave.getId());
                navigationService.checkForCreature(fellowship, currentCave);

                outputAdapter.displayMessage("Choose a direction to move (north, east, south, west):");

                String direction = scanner.nextLine().toLowerCase();

                // Attempt to move to the next cave
                Optional<Cave> nextCave = navigationService.move(fellowship, currentCave, direction);

                if (nextCave.isPresent()) {
                    currentCave = nextCave.get();
                } else {
                    outputAdapter.displayMessage("Invalid direction. Try again.");
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred during the game: {0}", e.getMessage());
        }

        outputAdapter.displayMessage("Game over. The Fellowship has perished.");
    }
}
