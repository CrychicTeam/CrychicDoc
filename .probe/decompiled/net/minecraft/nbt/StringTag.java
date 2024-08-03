package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Objects;
import net.minecraft.Util;

public class StringTag implements Tag {

    private static final int SELF_SIZE_IN_BYTES = 36;

    public static final TagType<StringTag> TYPE = new TagType.VariableSize<StringTag>() {

        public StringTag load(DataInput p_129315_, int p_129316_, NbtAccounter p_129317_) throws IOException {
            p_129317_.accountBytes(36L);
            String $$3 = p_129315_.readUTF();
            p_129317_.accountBytes((long) (2 * $$3.length()));
            return StringTag.valueOf($$3);
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197570_, StreamTagVisitor p_197571_) throws IOException {
            return p_197571_.visit(p_197570_.readUTF());
        }

        @Override
        public void skip(DataInput p_197568_) throws IOException {
            StringTag.skipString(p_197568_);
        }

        @Override
        public String getName() {
            return "STRING";
        }

        @Override
        public String getPrettyName() {
            return "TAG_String";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    private static final StringTag EMPTY = new StringTag("");

    private static final char DOUBLE_QUOTE = '"';

    private static final char SINGLE_QUOTE = '\'';

    private static final char ESCAPE = '\\';

    private static final char NOT_SET = '\u0000';

    private final String data;

    public static void skipString(DataInput dataInput0) throws IOException {
        dataInput0.skipBytes(dataInput0.readUnsignedShort());
    }

    private StringTag(String string0) {
        Objects.requireNonNull(string0, "Null string not allowed");
        this.data = string0;
    }

    public static StringTag valueOf(String string0) {
        return string0.isEmpty() ? EMPTY : new StringTag(string0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        try {
            dataOutput0.writeUTF(this.data);
        } catch (UTFDataFormatException var3) {
            Util.logAndPauseIfInIde("Failed to write NBT String", var3);
            dataOutput0.writeUTF("");
        }
    }

    @Override
    public int sizeInBytes() {
        return 36 + 2 * this.data.length();
    }

    @Override
    public byte getId() {
        return 8;
    }

    @Override
    public TagType<StringTag> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return Tag.super.getAsString();
    }

    public StringTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof StringTag && Objects.equals(this.data, ((StringTag) object0).data);
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    @Override
    public String getAsString() {
        return this.data;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitString(this);
    }

    public static String quoteAndEscape(String string0) {
        StringBuilder $$1 = new StringBuilder(" ");
        char $$2 = 0;
        for (int $$3 = 0; $$3 < string0.length(); $$3++) {
            char $$4 = string0.charAt($$3);
            if ($$4 == '\\') {
                $$1.append('\\');
            } else if ($$4 == '"' || $$4 == '\'') {
                if ($$2 == 0) {
                    $$2 = (char) ($$4 == '"' ? 39 : 34);
                }
                if ($$2 == $$4) {
                    $$1.append('\\');
                }
            }
            $$1.append($$4);
        }
        if ($$2 == 0) {
            $$2 = '"';
        }
        $$1.setCharAt(0, $$2);
        $$1.append($$2);
        return $$1.toString();
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        return streamTagVisitor0.visit(this.data);
    }
}