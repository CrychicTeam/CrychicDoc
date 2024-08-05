package com.github.alexthe666.citadel.repack.jaad.mp4.od;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Descriptor {

    public static final int TYPE_OBJECT_DESCRIPTOR = 1;

    public static final int TYPE_INITIAL_OBJECT_DESCRIPTOR = 2;

    public static final int TYPE_ES_DESCRIPTOR = 3;

    public static final int TYPE_DECODER_CONFIG_DESCRIPTOR = 4;

    public static final int TYPE_DECODER_SPECIFIC_INFO = 5;

    public static final int TYPE_SL_CONFIG_DESCRIPTOR = 6;

    public static final int TYPE_ES_ID_INC = 14;

    public static final int TYPE_MP4_INITIAL_OBJECT_DESCRIPTOR = 16;

    protected int type;

    protected int size;

    protected long start;

    private List<Descriptor> children = new ArrayList();

    public static Descriptor createDescriptor(MP4InputStream in) throws IOException {
        int type = in.read();
        int read = 1;
        int size = 0;
        int b = 0;
        do {
            b = in.read();
            size <<= 7;
            size |= b & 127;
            read++;
        } while ((b & 128) == 128);
        Descriptor desc = forTag(type);
        desc.type = type;
        desc.size = size;
        desc.start = in.getOffset();
        desc.decode(in);
        long remaining = (long) size - (in.getOffset() - desc.start);
        if (remaining > 0L) {
            Logger.getLogger("MP4 Boxes").log(Level.INFO, "Descriptor: bytes left: {0}, offset: {1}", new Long[] { remaining, in.getOffset() });
            in.skipBytes(remaining);
        }
        desc.size += read;
        return desc;
    }

    private static Descriptor forTag(int tag) {
        return switch(tag) {
            case 1 ->
                new ObjectDescriptor();
            case 2, 16 ->
                new InitialObjectDescriptor();
            case 3 ->
                new ESDescriptor();
            case 4 ->
                new DecoderConfigDescriptor();
            case 5 ->
                new DecoderSpecificInfo();
            default ->
                {
                }
        };
    }

    protected Descriptor() {
    }

    abstract void decode(MP4InputStream var1) throws IOException;

    protected void readChildren(MP4InputStream in) throws IOException {
        while ((long) this.size - (in.getOffset() - this.start) > 0L) {
            Descriptor desc = createDescriptor(in);
            this.children.add(desc);
        }
    }

    public List<Descriptor> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public int getType() {
        return this.type;
    }

    public int getSize() {
        return this.size;
    }
}