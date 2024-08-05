package se.mickelus.tetra.module.schematic;

import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class InvalidSchematicException extends Exception {

    private static final Logger logger = LogManager.getLogger();

    private final String key;

    private final String[] faultyModules;

    public InvalidSchematicException(String key, String[] faultyModules) {
        this.key = key;
        this.faultyModules = faultyModules;
    }

    public void printMessage() {
        logger.warn("Skipping schematic '{}' due to faulty module keys:", this.key);
        for (String faultyKey : this.faultyModules) {
            logger.warn("\t" + faultyKey);
        }
    }
}