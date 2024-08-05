package noppes.npcs.shared.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import noppes.npcs.shared.SharedReferences;

public class LogWriter {

    private static final String name = SharedReferences.modid();

    private static final Logger logger = Logger.getLogger(name);

    private static final SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");

    private static Handler handler;

    public static void info(Object msg) {
        logger.log(Level.FINE, String.valueOf(msg));
        handler.flush();
    }

    public static void warn(Object msg) {
        logger.log(Level.WARNING, String.valueOf(msg));
        handler.flush();
    }

    public static void error(Object msg) {
        logger.log(Level.SEVERE, String.valueOf(msg));
        handler.flush();
    }

    public static void error(Object msg, Throwable e) {
        logger.log(Level.SEVERE, String.valueOf(msg));
        logger.log(Level.SEVERE, e.getMessage(), e);
        handler.flush();
    }

    public static void except(Throwable e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
        handler.flush();
    }

    public static void debug(Object msg) {
        if (SharedReferences.VerboseDebug()) {
            logger.log(Level.INFO, String.valueOf(msg));
            handler.flush();
        }
    }

    static {
        try {
            File dir = new File("logs");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, name + "-latest.log");
            File lock = new File(dir, name + "-latest.log.lck");
            File file1 = new File(dir, name + "-1.log");
            File file2 = new File(dir, name + "-2.log");
            File file3 = new File(dir, name + "-3.log");
            if (lock.exists()) {
                lock.delete();
            }
            if (file3.exists()) {
                file3.delete();
            }
            if (file2.exists()) {
                file2.renameTo(file3);
            }
            if (file1.exists()) {
                file1.renameTo(file2);
            }
            if (file.exists()) {
                file.renameTo(file1);
            }
            handler = new StreamHandler(new FileOutputStream(file), new Formatter() {

                public String format(LogRecord record) {
                    StackTraceElement element = Thread.currentThread().getStackTrace()[8];
                    String line = "[" + element.getClassName() + ":" + element.getLineNumber() + "] ";
                    String time = "[" + LogWriter.dateformat.format(new Date(record.getMillis())) + "][" + record.getLevel() + "/" + LogWriter.name + "]" + line;
                    if (record.getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        record.getThrown().printStackTrace(pw);
                        return time + sw.toString();
                    } else {
                        return time + record.getMessage() + System.getProperty("line.separator");
                    }
                }
            });
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(handler.getFormatter());
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
            logger.setLevel(Level.ALL);
            info(new Date().toString());
        } catch (SecurityException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }
}