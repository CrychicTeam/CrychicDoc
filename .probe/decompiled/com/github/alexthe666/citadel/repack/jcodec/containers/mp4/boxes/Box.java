package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.IBoxFactory;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public abstract class Box {

    public Header header;

    public static final int MAX_BOX_SIZE = 134217728;

    @UsedViaReflection
    public Box(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return this.header;
    }

    public abstract void parse(ByteBuffer var1);

    public void write(ByteBuffer buf) {
        ByteBuffer dup = buf.duplicate();
        NIOUtils.skip(buf, 8);
        this.doWrite(buf);
        this.header.setBodySize(buf.position() - dup.position() - 8);
        Preconditions.checkState(this.header.headerSize() == 8L);
        this.header.write(dup);
    }

    protected abstract void doWrite(ByteBuffer var1);

    public abstract int estimateSize();

    public String getFourcc() {
        return this.header.getFourcc();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.dump(sb);
        return sb.toString();
    }

    protected void dump(StringBuilder sb) {
        sb.append("{\"tag\":\"" + this.header.getFourcc() + "\"}");
    }

    public static Box terminatorAtom() {
        return createLeafBox(new Header(Platform.stringFromBytes(new byte[4])), ByteBuffer.allocate(0));
    }

    public static String[] path(String path) {
        return StringUtils.splitC(path, '.');
    }

    public static Box.LeafBox createLeafBox(Header atom, ByteBuffer data) {
        Box.LeafBox leaf = new Box.LeafBox(atom);
        leaf.data = data;
        return leaf;
    }

    public static Box parseBox(ByteBuffer input, Header childAtom, IBoxFactory factory) {
        Box box = factory.newBox(childAtom);
        if (childAtom.getBodySize() < 134217728L) {
            box.parse(input);
            return box;
        } else {
            return new Box.LeafBox(Header.createHeader("free", 8L));
        }
    }

    public static <T extends Box> T asBox(Class<T> class1, Box box) {
        try {
            T res = (T) Platform.newInstance(class1, new Object[] { box.getHeader() });
            ByteBuffer buffer = ByteBuffer.allocate((int) box.getHeader().getBodySize());
            box.doWrite(buffer);
            buffer.flip();
            res.parse(buffer);
            return res;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static class LeafBox extends Box {

        ByteBuffer data;

        public LeafBox(Header atom) {
            super(atom);
        }

        @Override
        public void parse(ByteBuffer input) {
            this.data = NIOUtils.read(input, (int) this.header.getBodySize());
        }

        public ByteBuffer getData() {
            return this.data.duplicate();
        }

        @Override
        protected void doWrite(ByteBuffer out) {
            NIOUtils.write(out, this.data);
        }

        @Override
        public int estimateSize() {
            return this.data.remaining() + Header.estimateHeaderSize(this.data.remaining());
        }
    }
}