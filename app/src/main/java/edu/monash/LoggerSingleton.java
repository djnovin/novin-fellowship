package edu.monash;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerSingleton {

    private LoggerSingleton() {
    }

    private static class LoggerHolder {
        private static final Logger INSTANCE = Logger.getLogger(LoggerHolder.class.getName());

        static {
            INSTANCE.setLevel(Level.ALL);
        }
    }

    public static Logger getLogger() {
        return LoggerHolder.INSTANCE;
    }
}
