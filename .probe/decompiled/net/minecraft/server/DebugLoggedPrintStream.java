package net.minecraft.server;

import com.mojang.logging.LogUtils;
import java.io.OutputStream;
import org.slf4j.Logger;

public class DebugLoggedPrintStream extends LoggedPrintStream {

    private static final Logger LOGGER = LogUtils.getLogger();

    public DebugLoggedPrintStream(String string0, OutputStream outputStream1) {
        super(string0, outputStream1);
    }

    @Override
    protected void logLine(String string0) {
        StackTraceElement[] $$1 = Thread.currentThread().getStackTrace();
        StackTraceElement $$2 = $$1[Math.min(3, $$1.length)];
        LOGGER.info("[{}]@.({}:{}): {}", new Object[] { this.f_135948_, $$2.getFileName(), $$2.getLineNumber(), string0 });
    }
}