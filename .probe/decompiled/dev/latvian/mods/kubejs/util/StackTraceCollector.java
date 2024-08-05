package dev.latvian.mods.kubejs.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class StackTraceCollector extends PrintStream {

    private static final OutputStream OUTPUT_SINK = new OutputStream() {

        public void write(int b) {
        }

        public void write(byte[] b) {
        }

        public void write(byte[] b, int off, int len) {
        }
    };

    private final Collection<String> stackTrace;

    private final Pattern exitPattern;

    private final Function<String, String> reduce;

    private boolean exit;

    public StackTraceCollector(Collection<String> stackTrace, @Nullable Pattern exitPattern, Function<String, String> reduce) {
        super(OUTPUT_SINK);
        this.stackTrace = stackTrace;
        this.exitPattern = exitPattern;
        this.reduce = reduce;
        this.exit = false;
    }

    public void print(@Nullable String s) {
        if (s != null && !s.isEmpty()) {
            for (String str : s.split("\n")) {
                this.println(str);
            }
        }
    }

    public void println(@Nullable Object x) {
        this.println(String.valueOf(x));
    }

    public void println(@Nullable String x) {
        if (!this.exit && x != null && !x.isEmpty()) {
            boolean isAt = x.startsWith("\tat ");
            if (isAt) {
                x = x.substring(4);
            }
            x = x.trim();
            if (x.startsWith("java.base/")) {
                x = x.substring(10);
            }
            x = (String) this.reduce.apply(x);
            if (x != null && !x.isEmpty()) {
                if (this.exitPattern != null && this.exitPattern.matcher(x).find()) {
                    this.exit = true;
                } else if (isAt) {
                    this.stackTrace.add("  at " + x);
                } else {
                    this.stackTrace.add(x);
                }
            }
        }
    }
}