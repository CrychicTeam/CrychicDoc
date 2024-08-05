package net.minecraftforge.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class TablePrinter<T> {

    private final List<TablePrinter.Header<T>> headers = new ArrayList();

    private final List<T> rows = new ArrayList();

    public TablePrinter<T> header(String name, Function<T, String> supplier) {
        return this.header(name, supplier, false);
    }

    public TablePrinter<T> header(String name, Function<T, String> supplier, boolean right) {
        this.headers.add(new TablePrinter.Header(name, supplier, right));
        return this;
    }

    public void clearRows() {
        this.rows.clear();
    }

    public TablePrinter<T> add(T row) {
        this.rows.add(row);
        return this;
    }

    public TablePrinter<T> add(T row, T... more) {
        this.add(row);
        for (T t : more) {
            this.add(t);
        }
        return this;
    }

    public TablePrinter<T> add(Collection<? extends T> rows) {
        rows.forEach(this::add);
        return this;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        this.build(buf);
        return buf.toString();
    }

    public void build(StringBuilder buf) {
        int[] count = new int[this.headers.size()];
        int width = 1;
        for (int x = 0; x < count.length; x++) {
            count[x] = ((TablePrinter.Header) this.headers.get(x)).name.length() + 2;
        }
        for (T row : this.rows) {
            for (int x = 0; x < count.length; x++) {
                count[x] = Math.max(count[x], ((String) ((TablePrinter.Header) this.headers.get(x)).supplier.apply(row)).length() + 2);
            }
        }
        for (int x = 0; x < count.length; x++) {
            width += count[x] + 3;
        }
        line(buf, width);
        for (int x = 0; x < count.length; x++) {
            TablePrinter.Header<T> header = (TablePrinter.Header<T>) this.headers.get(x);
            int left = (count[x] - header.name.length()) / 2;
            int right = count[x] - header.name.length() - left;
            buf.append('|').append(' ');
            pad(buf, left);
            buf.append(header.name);
            pad(buf, right);
            buf.append(' ');
        }
        buf.append('|').append('\n');
        line(buf, width);
        for (T row : this.rows) {
            for (int x = 0; x < count.length; x++) {
                TablePrinter.Header<T> header = (TablePrinter.Header<T>) this.headers.get(x);
                String data = (String) header.supplier.apply(row);
                int padding = count[x] - data.length();
                buf.append('|').append(' ');
                if (header.right) {
                    pad(buf, padding);
                }
                buf.append(data);
                if (!header.right) {
                    pad(buf, padding);
                }
                buf.append(' ');
            }
            buf.append('|').append('\n');
        }
        line(buf, width);
    }

    private static void line(StringBuilder buf, int size) {
        buf.append('|');
        for (int x = 0; x < size - 2; x++) {
            buf.append('-');
        }
        buf.append('|').append('\n');
    }

    private static void pad(StringBuilder buf, int size) {
        for (int y = 0; y < size; y++) {
            buf.append(' ');
        }
    }

    private static class Header<T> {

        private final String name;

        private final Function<T, String> supplier;

        private final boolean right;

        private Header(String name, Function<T, String> supplier, boolean right) {
            this.name = name;
            this.supplier = supplier;
            this.right = right;
        }
    }
}