package edu.monash;

import java.util.Scanner;
import java.util.logging.Logger;

import edu.monash.adapters.ConsoleOutputAdapter;
import edu.monash.application.usecases.GameUseCase;
import edu.monash.application.usecases.LoadLabyrinthUseCase;
import edu.monash.domain.entities.Labyrinth;
import edu.monash.domain.services.CombatService;
import edu.monash.domain.services.NavigationService;
import edu.monash.infrastructure.repositories.LabyrinthRepository;

public class Main {
    private static final Logger logger = LoggerSingleton.getLogger();

    public static void main(String[] args) {
        ConsoleOutputAdapter outputAdapter = new ConsoleOutputAdapter();
        LabyrinthRepository labyrinthRepository = new LabyrinthRepository(logger);
        LoadLabyrinthUseCase loadLabyrinthUseCase = new LoadLabyrinthUseCase(labyrinthRepository);

        // Load the labyrinth from the file
        Labyrinth labyrinth = loadLabyrinthUseCase.execute("app/src/main/resources/labyrinth.txt");

        NavigationService navigationService = new NavigationService(labyrinth);

        // Initialize CombatService
        CombatService combatService = new CombatService();

        // Start the game
        try (Scanner scanner = new Scanner(System.in)) {
            // Start the game
            GameUseCase gameUseCase = new GameUseCase(outputAdapter, navigationService, combatService, scanner);
            gameUseCase.startGame();
            // Close the scanner
        }
    }
}
