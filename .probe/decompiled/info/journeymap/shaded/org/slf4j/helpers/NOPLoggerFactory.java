package info.journeymap.shaded.org.slf4j.helpers;

import info.journeymap.shaded.org.slf4j.ILoggerFactory;
import info.journeymap.shaded.org.slf4j.Logger;

public class NOPLoggerFactory implements ILoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return NOPLogger.NOP_LOGGER;
    }
}