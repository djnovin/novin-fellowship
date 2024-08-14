package edu.monash;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerSingleton {

    // Private constructor to prevent instantiation
    private LoggerSingleton() {
    }

    // Inner static class responsible for holding the single instance of Logger
    private static class LoggerHolder {
        private static final Logger INSTANCE = createLogger();

        // Static initializer to configure the logger
        private static Logger createLogger() {
            Logger logger = Logger.getLogger(LoggerHolder.class.getName());

            // Set the logging level
            logger.setLevel(Level.ALL);

            // Remove the default console handler
            Logger rootLogger = Logger.getLogger("");
            rootLogger.removeHandler(rootLogger.getHandlers()[0]);

            // Add a custom console handler with a simple formatter
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            return logger;
        }
    }

    // Public method to access the single instance of Logger
    public static Logger getLogger() {
        return LoggerHolder.INSTANCE;
    }
}
