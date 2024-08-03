package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.Context;
import it.unimi.dsi.fastutil.ints.IntArrays;

class WordBuffer implements Writer {

    private int[] a = new int[16];

    private int size;

    public int size() {
        return this.size;
    }

    public int[] elements() {
        return this.a;
    }

    public void clear() {
        this.size = 0;
    }

    @Override
    public void writeWord(int word) {
        int s = this.size;
        this.grow(s + 1)[s] = word;
        this.size = s + 1;
    }

    @Override
    public void writeWords(int[] words, int n) {
        if (n != 0) {
            int newSize = this.size + n;
            System.arraycopy(words, 0, this.grow(newSize), this.size, n);
            this.size = newSize;
        }
    }

    @Override
    public void writeString8(Context context, String s) {
        int p = this.size;
        int len = s.length();
        int[] a = this.grow(p + (len + 4 >> 2));
        int word = 0;
        int shift = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == 0 || c >= 128) {
                context.error(-1, "unexpected character '" + c + "'");
            }
            word |= c << shift;
            shift += 8;
            if (shift == 32) {
                a[p++] = word;
                word = 0;
                shift = 0;
            }
        }
        a[p++] = word;
        this.size = p;
    }

    private int[] grow(int minCapacity) {
        if (minCapacity > this.a.length) {
            int oldCapacity = this.a.length;
            int newCapacity = Math.max(minCapacity, oldCapacity < 1024 ? oldCapacity << 1 : oldCapacity + (oldCapacity >> 1));
            this.a = IntArrays.forceCapacity(this.a, newCapacity, this.size);
        }
        return this.a;
    }
}