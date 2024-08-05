package net.minecraft.server;

import com.mojang.logging.LogUtils;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public class LoggedPrintStream extends PrintStream {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final String name;

    public LoggedPrintStream(String string0, OutputStream outputStream1) {
        super(outputStream1);
        this.name = string0;
    }

    public void println(@Nullable String string0) {
        this.logLine(string0);
    }

    public void println(Object object0) {
        this.logLine(String.valueOf(object0));
    }

    protected void logLine(@Nullable String string0) {
        LOGGER.info("[{}]: {}", this.name, string0);
    }
}