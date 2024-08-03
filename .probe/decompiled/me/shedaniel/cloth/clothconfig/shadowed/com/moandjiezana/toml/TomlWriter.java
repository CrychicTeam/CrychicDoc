package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.TimeZone;

public class TomlWriter {

    private final IndentationPolicy indentationPolicy;

    private final DatePolicy datePolicy;

    public TomlWriter() {
        this(0, 0, 0, TimeZone.getTimeZone("UTC"), false);
    }

    private TomlWriter(int keyIndentation, int tableIndentation, int arrayDelimiterPadding, TimeZone timeZone, boolean showFractionalSeconds) {
        this.indentationPolicy = new IndentationPolicy(keyIndentation, tableIndentation, arrayDelimiterPadding);
        this.datePolicy = new DatePolicy(timeZone, showFractionalSeconds);
    }

    public String write(Object from) {
        try {
            StringWriter output = new StringWriter();
            this.write(from, output);
            return output.toString();
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void write(Object from, File target) throws IOException {
        OutputStream outputStream = new FileOutputStream(target);
        try {
            this.write(from, outputStream);
        } finally {
            outputStream.close();
        }
    }

    public void write(Object from, OutputStream target) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(target, "UTF-8");
        this.write(from, writer);
        writer.flush();
    }

    public void write(Object from, Writer target) throws IOException {
        ValueWriter valueWriter = ValueWriters.WRITERS.findWriterFor(from);
        if (valueWriter != MapValueWriter.MAP_VALUE_WRITER && valueWriter != ObjectValueWriter.OBJECT_VALUE_WRITER) {
            throw new IllegalArgumentException("An object of class " + from.getClass().getSimpleName() + " cannot produce valid TOML. Please pass in a Map or a custom type.");
        } else {
            WriterContext context = new WriterContext(this.indentationPolicy, this.datePolicy, target);
            valueWriter.write(from, context);
        }
    }

    public static class Builder {

        private int keyIndentation;

        private int tableIndentation;

        private int arrayDelimiterPadding = 0;

        private TimeZone timeZone = TimeZone.getTimeZone("UTC");

        private boolean showFractionalSeconds = false;

        public TomlWriter.Builder indentValuesBy(int spaces) {
            this.keyIndentation = spaces;
            return this;
        }

        public TomlWriter.Builder indentTablesBy(int spaces) {
            this.tableIndentation = spaces;
            return this;
        }

        public TomlWriter.Builder timeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public TomlWriter.Builder padArrayDelimitersBy(int spaces) {
            this.arrayDelimiterPadding = spaces;
            return this;
        }

        public TomlWriter build() {
            return new TomlWriter(this.keyIndentation, this.tableIndentation, this.arrayDelimiterPadding, this.timeZone, this.showFractionalSeconds);
        }

        public TomlWriter.Builder showFractionalSeconds() {
            this.showFractionalSeconds = true;
            return this;
        }
    }
}