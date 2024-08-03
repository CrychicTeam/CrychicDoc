package net.minecraftforge.common.util;

import com.google.common.collect.Streams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class TextTable {

    private final List<TextTable.Column> columns;

    private final List<TextTable.Row> rows = new ArrayList();

    public static TextTable.Column column(String header) {
        return new TextTable.Column(header);
    }

    public static TextTable.Column column(String header, TextTable.Alignment alignment) {
        return new TextTable.Column(header, alignment);
    }

    public TextTable(List<TextTable.Column> columns) {
        this.columns = columns;
    }

    public String build(String lineEnding) {
        StringBuilder destination = new StringBuilder();
        this.append(destination, lineEnding);
        return destination.toString();
    }

    public void append(StringBuilder destination, String lineEnding) {
        List<String> headers = (List<String>) this.columns.stream().map(c -> c.formatHeader(" ")).collect(Collectors.toList());
        this.printRow(destination, headers);
        destination.append(lineEnding);
        this.printSeparators(destination);
        for (TextTable.Row row : this.rows) {
            destination.append(lineEnding);
            this.printRow(destination, row.format(this.columns, " "));
        }
    }

    private void printSeparators(StringBuilder destination) {
        destination.append('|');
        for (TextTable.Column column : this.columns) {
            destination.append((char) (column.alignment != TextTable.Alignment.RIGHT ? ':' : ' '));
            destination.append(column.getSeparator('-'));
            destination.append((char) (column.alignment != TextTable.Alignment.LEFT ? ':' : ' '));
            destination.append('|');
        }
    }

    private void printRow(StringBuilder destination, List<String> values) {
        destination.append('|');
        for (String value : values) {
            destination.append(' ');
            destination.append(value);
            destination.append(' ');
            destination.append('|');
        }
    }

    public void add(@NotNull Object... values) {
        if (values.length != this.columns.size()) {
            throw new IllegalArgumentException("Received wrong amount of values for table row, expected " + this.columns.size() + ", received " + this.columns.size() + ".");
        } else {
            TextTable.Row row = new TextTable.Row();
            for (int i = 0; i < values.length; i++) {
                String value = Objects.toString(values[i]);
                row.values.add(value);
                ((TextTable.Column) this.columns.get(i)).fit(value);
            }
            this.rows.add(row);
        }
    }

    public void clear() {
        for (TextTable.Column column : this.columns) {
            column.resetWidth();
        }
        this.rows.clear();
    }

    public List<TextTable.Column> getColumns() {
        return Collections.unmodifiableList(this.columns);
    }

    public static enum Alignment {

        LEFT, CENTER, RIGHT
    }

    public static class Column {

        private String header;

        private int width;

        private TextTable.Alignment alignment;

        public Column(String header) {
            this(header, TextTable.Alignment.LEFT);
        }

        public Column(String header, TextTable.Alignment alignment) {
            this.header = header;
            this.width = header.length();
            this.alignment = alignment;
        }

        public String formatHeader(String padding) {
            return this.format(this.header, padding);
        }

        public String format(String value, String padding) {
            switch(this.alignment) {
                case LEFT:
                    return StringUtils.rightPad(value, this.width, padding);
                case RIGHT:
                    return StringUtils.leftPad(value, this.width, padding);
                default:
                    int length = value.length();
                    int left = (this.width - length) / 2;
                    int leftWidth = left + length;
                    return StringUtils.rightPad(StringUtils.leftPad(value, leftWidth, padding), this.width, padding);
            }
        }

        public String getSeparator(char character) {
            return StringUtils.leftPad("", this.width, character);
        }

        public void fit(String value) {
            if (value.length() > this.width) {
                this.width = value.length();
            }
        }

        public void resetWidth() {
            this.width = this.header.length();
        }

        public int getWidth() {
            return this.width;
        }
    }

    public static class Row {

        private final ArrayList<String> values = new ArrayList();

        public List<String> format(List<TextTable.Column> columns, String padding) {
            if (columns.size() != this.values.size()) {
                throw new IllegalArgumentException("Received wrong amount of columns for table row, expected " + columns.size() + ", received " + columns.size() + ".");
            } else {
                return (List<String>) Streams.zip(this.values.stream(), columns.stream(), (v, c) -> c.format(v, padding)).collect(Collectors.toList());
            }
        }
    }
}