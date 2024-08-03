package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.io.Writer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public class LogWriter extends Writer {

    private static final int FORCE_WRAP_LENGTH = 960;

    private final Logger mLogger;

    private final Level mLevel;

    private final Marker mMarker;

    private final StringBuilder mBuilder = new StringBuilder(120);

    public LogWriter(@NonNull Logger logger) {
        this(logger, Level.DEBUG, null);
    }

    public LogWriter(@NonNull Logger logger, @NonNull Level level, @Nullable Marker marker) {
        this.mLogger = logger;
        this.mLevel = level;
        this.mMarker = marker;
    }

    public void close() {
        this.flushBuilder();
    }

    public void flush() {
        this.flushBuilder();
    }

    public void write(@NonNull char[] buf, int offset, int count) {
        this.mBuilder.ensureCapacity(Math.min(this.mBuilder.length() + count, 962));
        for (int i = 0; i < count; i++) {
            char c = buf[offset + i];
            if (c == '\n') {
                this.flushBuilder();
            } else {
                this.mBuilder.append(c);
                if (this.mBuilder.length() >= 960) {
                    this.flushBuilder();
                }
            }
        }
    }

    private void flushBuilder() {
        if (this.mBuilder.length() != 0) {
            String msg = this.mBuilder.toString();
            if (this.mMarker != null) {
                this.mLogger.log(this.mLevel, this.mMarker, msg);
            } else {
                this.mLogger.log(this.mLevel, msg);
            }
            this.mBuilder.setLength(0);
        }
    }
}