package net.minecraftforge.common.util;

import io.netty.buffer.ByteBuf;

public class HexDumper {

    public static String dump(ByteBuf data) {
        int current = data.readerIndex();
        data.readerIndex(0);
        HexDumper.Instance inst = new HexDumper.Instance(current, data.readableBytes());
        data.forEachByte(b -> {
            inst.add(b);
            return true;
        });
        data.readerIndex(current);
        return inst.finish();
    }

    public static String dump(byte[] data) {
        return dump(data, -1);
    }

    public static String dump(byte[] data, int marker) {
        HexDumper.Instance inst = new HexDumper.Instance(marker, data.length);
        for (int x = 0; x < data.length; x++) {
            inst.add(data[x]);
        }
        return inst.finish();
    }

    private static class Instance {

        private static final String HEX = "0123456789ABCDEF";

        private final int marked;

        private final StringBuilder buf;

        private char[] ascii = new char[16];

        private int index = 0;

        private Instance(int marked, int size) {
            this.marked = marked;
            int lines = (size + 15) / 16;
            this.buf = new StringBuilder(size * 3 + size + lines * 2 + (marked == -1 ? 0 : lines));
            for (int x = 0; x < this.ascii.length; x++) {
                this.ascii[x] = ' ';
            }
        }

        public void add(byte data) {
            if (this.index == 0 && this.marked != -1) {
                this.buf.append((char) (this.index == this.marked ? '<' : ' '));
            }
            if (this.index != 0 && this.index % 16 == 0) {
                this.buf.append('\t');
                for (int x = 0; x < 16; x++) {
                    this.buf.append(this.ascii[x]);
                    this.ascii[x] = ' ';
                }
                this.buf.append('\n');
                if (this.marked != -1) {
                    this.buf.append((char) (this.index == this.marked ? '<' : ' '));
                }
            }
            this.ascii[this.index % 16] = data >= 32 && data <= 126 ? (char) data : 46;
            this.buf.append("0123456789ABCDEF".charAt((data & 240) >> 4));
            this.buf.append("0123456789ABCDEF".charAt(data & 15));
            if (this.index + 1 == this.marked) {
                this.buf.append((char) (this.marked % 16 == 0 ? ' ' : '<'));
            } else {
                this.buf.append((char) (this.marked == this.index ? '>' : ' '));
            }
            this.index++;
        }

        public String finish() {
            int padding = 16 - this.index % 16;
            if (padding > 0) {
                for (int x = 0; x < padding * 3; x++) {
                    this.buf.append(' ');
                }
                this.buf.append('\t');
                this.buf.append(this.ascii);
            }
            this.buf.append('\n');
            this.buf.append("Length: ").append(this.index);
            if (this.marked != -1) {
                this.buf.append(" Mark: ").append(this.marked);
            }
            return this.buf.toString();
        }
    }
}