package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EndTag implements Tag {

    private static final int SELF_SIZE_IN_BYTES = 8;

    public static final TagType<EndTag> TYPE = new TagType<EndTag>() {

        public EndTag load(DataInput p_128550_, int p_128551_, NbtAccounter p_128552_) {
            p_128552_.accountBytes(8L);
            return EndTag.INSTANCE;
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197465_, StreamTagVisitor p_197466_) {
            return p_197466_.visitEnd();
        }

        @Override
        public void skip(DataInput p_197462_, int p_197463_) {
        }

        @Override
        public void skip(DataInput p_197460_) {
        }

        @Override
        public String getName() {
            return "END";
        }

        @Override
        public String getPrettyName() {
            return "TAG_End";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    public static final EndTag INSTANCE = new EndTag();

    private EndTag() {
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
    }

    @Override
    public int sizeInBytes() {
        return 8;
    }

    @Override
    public byte getId() {
        return 0;
    }

    @Override
    public TagType<EndTag> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.m_7916_();
    }

    public EndTag copy() {
        return this;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitEnd(this);
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        return streamTagVisitor0.visitEnd();
    }
}