package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class StringTagVisitor implements TagVisitor {

    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

    private final StringBuilder builder = new StringBuilder();

    public String visit(Tag tag0) {
        tag0.accept(this);
        return this.builder.toString();
    }

    @Override
    public void visitString(StringTag stringTag0) {
        this.builder.append(StringTag.quoteAndEscape(stringTag0.getAsString()));
    }

    @Override
    public void visitByte(ByteTag byteTag0) {
        this.builder.append(byteTag0.getAsNumber()).append('b');
    }

    @Override
    public void visitShort(ShortTag shortTag0) {
        this.builder.append(shortTag0.getAsNumber()).append('s');
    }

    @Override
    public void visitInt(IntTag intTag0) {
        this.builder.append(intTag0.getAsNumber());
    }

    @Override
    public void visitLong(LongTag longTag0) {
        this.builder.append(longTag0.getAsNumber()).append('L');
    }

    @Override
    public void visitFloat(FloatTag floatTag0) {
        this.builder.append(floatTag0.getAsFloat()).append('f');
    }

    @Override
    public void visitDouble(DoubleTag doubleTag0) {
        this.builder.append(doubleTag0.getAsDouble()).append('d');
    }

    @Override
    public void visitByteArray(ByteArrayTag byteArrayTag0) {
        this.builder.append("[B;");
        byte[] $$1 = byteArrayTag0.getAsByteArray();
        for (int $$2 = 0; $$2 < $$1.length; $$2++) {
            if ($$2 != 0) {
                this.builder.append(',');
            }
            this.builder.append($$1[$$2]).append('B');
        }
        this.builder.append(']');
    }

    @Override
    public void visitIntArray(IntArrayTag intArrayTag0) {
        this.builder.append("[I;");
        int[] $$1 = intArrayTag0.getAsIntArray();
        for (int $$2 = 0; $$2 < $$1.length; $$2++) {
            if ($$2 != 0) {
                this.builder.append(',');
            }
            this.builder.append($$1[$$2]);
        }
        this.builder.append(']');
    }

    @Override
    public void visitLongArray(LongArrayTag longArrayTag0) {
        this.builder.append("[L;");
        long[] $$1 = longArrayTag0.getAsLongArray();
        for (int $$2 = 0; $$2 < $$1.length; $$2++) {
            if ($$2 != 0) {
                this.builder.append(',');
            }
            this.builder.append($$1[$$2]).append('L');
        }
        this.builder.append(']');
    }

    @Override
    public void visitList(ListTag listTag0) {
        this.builder.append('[');
        for (int $$1 = 0; $$1 < listTag0.size(); $$1++) {
            if ($$1 != 0) {
                this.builder.append(',');
            }
            this.builder.append(new StringTagVisitor().visit(listTag0.get($$1)));
        }
        this.builder.append(']');
    }

    @Override
    public void visitCompound(CompoundTag compoundTag0) {
        this.builder.append('{');
        List<String> $$1 = Lists.newArrayList(compoundTag0.getAllKeys());
        Collections.sort($$1);
        for (String $$2 : $$1) {
            if (this.builder.length() != 1) {
                this.builder.append(',');
            }
            this.builder.append(handleEscape($$2)).append(':').append(new StringTagVisitor().visit(compoundTag0.get($$2)));
        }
        this.builder.append('}');
    }

    protected static String handleEscape(String string0) {
        return SIMPLE_VALUE.matcher(string0).matches() ? string0 : StringTag.quoteAndEscape(string0);
    }

    @Override
    public void visitEnd(EndTag endTag0) {
        this.builder.append("END");
    }
}