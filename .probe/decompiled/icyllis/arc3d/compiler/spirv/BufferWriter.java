package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.Context;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

class BufferWriter implements Writer {

    private final ByteBuffer mBuffer;

    public BufferWriter(int size) {
        this.mBuffer = BufferUtils.createByteBuffer(size);
    }

    public ByteBuffer detach() {
        return this.mBuffer.flip();
    }

    @Override
    public void writeWord(int word) {
        this.mBuffer.putInt(word);
    }

    @Override
    public void writeWords(int[] words, int n) {
        if (n != 0) {
            ByteBuffer buffer = this.mBuffer;
            buffer.asIntBuffer().put(words, 0, n);
            buffer.position(buffer.position() + (n << 2));
        }
    }

    @Override
    public void writeString8(Context context, String s) {
        int len = s.length();
        ByteBuffer buffer = this.mBuffer;
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
                buffer.putInt(word);
                word = 0;
                shift = 0;
            }
        }
        buffer.putInt(word);
    }
}