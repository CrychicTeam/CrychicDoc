package net.minecraft.nbt;

import java.io.DataInput;
import java.io.IOException;

public interface TagType<T extends Tag> {

    T load(DataInput var1, int var2, NbtAccounter var3) throws IOException;

    StreamTagVisitor.ValueResult parse(DataInput var1, StreamTagVisitor var2) throws IOException;

    default void parseRoot(DataInput dataInput0, StreamTagVisitor streamTagVisitor1) throws IOException {
        switch(streamTagVisitor1.visitRootEntry(this)) {
            case CONTINUE:
                this.parse(dataInput0, streamTagVisitor1);
            case HALT:
            default:
                break;
            case BREAK:
                this.skip(dataInput0);
        }
    }

    void skip(DataInput var1, int var2) throws IOException;

    void skip(DataInput var1) throws IOException;

    default boolean isValue() {
        return false;
    }

    String getName();

    String getPrettyName();

    static TagType<EndTag> createInvalid(final int int0) {
        return new TagType<EndTag>() {

            private IOException createException() {
                return new IOException("Invalid tag id: " + int0);
            }

            public EndTag load(DataInput p_129387_, int p_129388_, NbtAccounter p_129389_) throws IOException {
                throw this.createException();
            }

            @Override
            public StreamTagVisitor.ValueResult parse(DataInput p_197589_, StreamTagVisitor p_197590_) throws IOException {
                throw this.createException();
            }

            @Override
            public void skip(DataInput p_197586_, int p_197587_) throws IOException {
                throw this.createException();
            }

            @Override
            public void skip(DataInput p_197584_) throws IOException {
                throw this.createException();
            }

            @Override
            public String getName() {
                return "INVALID[" + int0 + "]";
            }

            @Override
            public String getPrettyName() {
                return "UNKNOWN_" + int0;
            }
        };
    }

    public interface StaticSize<T extends Tag> extends TagType<T> {

        @Override
        default void skip(DataInput dataInput0) throws IOException {
            dataInput0.skipBytes(this.size());
        }

        @Override
        default void skip(DataInput dataInput0, int int1) throws IOException {
            dataInput0.skipBytes(this.size() * int1);
        }

        int size();
    }

    public interface VariableSize<T extends Tag> extends TagType<T> {

        @Override
        default void skip(DataInput dataInput0, int int1) throws IOException {
            for (int $$2 = 0; $$2 < int1; $$2++) {
                this.m_196159_(dataInput0);
            }
        }
    }
}