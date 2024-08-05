package net.minecraft.nbt;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteOpenHashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;

public class TextComponentTagVisitor implements TagVisitor {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int INLINE_LIST_THRESHOLD = 8;

    private static final ByteCollection INLINE_ELEMENT_TYPES = new ByteOpenHashSet(Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6));

    private static final ChatFormatting SYNTAX_HIGHLIGHTING_KEY = ChatFormatting.AQUA;

    private static final ChatFormatting SYNTAX_HIGHLIGHTING_STRING = ChatFormatting.GREEN;

    private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER = ChatFormatting.GOLD;

    private static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER_TYPE = ChatFormatting.RED;

    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");

    private static final String NAME_VALUE_SEPARATOR = String.valueOf(':');

    private static final String ELEMENT_SEPARATOR = String.valueOf(',');

    private static final String LIST_OPEN = "[";

    private static final String LIST_CLOSE = "]";

    private static final String LIST_TYPE_SEPARATOR = ";";

    private static final String ELEMENT_SPACING = " ";

    private static final String STRUCT_OPEN = "{";

    private static final String STRUCT_CLOSE = "}";

    private static final String NEWLINE = "\n";

    private final String indentation;

    private final int depth;

    private Component result = CommonComponents.EMPTY;

    public TextComponentTagVisitor(String string0, int int1) {
        this.indentation = string0;
        this.depth = int1;
    }

    public Component visit(Tag tag0) {
        tag0.accept(this);
        return this.result;
    }

    @Override
    public void visitString(StringTag stringTag0) {
        String $$1 = StringTag.quoteAndEscape(stringTag0.getAsString());
        String $$2 = $$1.substring(0, 1);
        Component $$3 = Component.literal($$1.substring(1, $$1.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_STRING);
        this.result = Component.literal($$2).append($$3).append($$2);
    }

    @Override
    public void visitByte(ByteTag byteTag0) {
        Component $$1 = Component.literal("b").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        this.result = Component.literal(String.valueOf(byteTag0.getAsNumber())).append($$1).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    @Override
    public void visitShort(ShortTag shortTag0) {
        Component $$1 = Component.literal("s").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        this.result = Component.literal(String.valueOf(shortTag0.getAsNumber())).append($$1).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    @Override
    public void visitInt(IntTag intTag0) {
        this.result = Component.literal(String.valueOf(intTag0.getAsNumber())).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    @Override
    public void visitLong(LongTag longTag0) {
        Component $$1 = Component.literal("L").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        this.result = Component.literal(String.valueOf(longTag0.getAsNumber())).append($$1).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    @Override
    public void visitFloat(FloatTag floatTag0) {
        Component $$1 = Component.literal("f").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        this.result = Component.literal(String.valueOf(floatTag0.getAsFloat())).append($$1).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    @Override
    public void visitDouble(DoubleTag doubleTag0) {
        Component $$1 = Component.literal("d").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        this.result = Component.literal(String.valueOf(doubleTag0.getAsDouble())).append($$1).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    @Override
    public void visitByteArray(ByteArrayTag byteArrayTag0) {
        Component $$1 = Component.literal("B").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        MutableComponent $$2 = Component.literal("[").append($$1).append(";");
        byte[] $$3 = byteArrayTag0.getAsByteArray();
        for (int $$4 = 0; $$4 < $$3.length; $$4++) {
            MutableComponent $$5 = Component.literal(String.valueOf($$3[$$4])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
            $$2.append(" ").append($$5).append($$1);
            if ($$4 != $$3.length - 1) {
                $$2.append(ELEMENT_SEPARATOR);
            }
        }
        $$2.append("]");
        this.result = $$2;
    }

    @Override
    public void visitIntArray(IntArrayTag intArrayTag0) {
        Component $$1 = Component.literal("I").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        MutableComponent $$2 = Component.literal("[").append($$1).append(";");
        int[] $$3 = intArrayTag0.getAsIntArray();
        for (int $$4 = 0; $$4 < $$3.length; $$4++) {
            $$2.append(" ").append(Component.literal(String.valueOf($$3[$$4])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER));
            if ($$4 != $$3.length - 1) {
                $$2.append(ELEMENT_SEPARATOR);
            }
        }
        $$2.append("]");
        this.result = $$2;
    }

    @Override
    public void visitLongArray(LongArrayTag longArrayTag0) {
        Component $$1 = Component.literal("L").withStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        MutableComponent $$2 = Component.literal("[").append($$1).append(";");
        long[] $$3 = longArrayTag0.getAsLongArray();
        for (int $$4 = 0; $$4 < $$3.length; $$4++) {
            Component $$5 = Component.literal(String.valueOf($$3[$$4])).withStyle(SYNTAX_HIGHLIGHTING_NUMBER);
            $$2.append(" ").append($$5).append($$1);
            if ($$4 != $$3.length - 1) {
                $$2.append(ELEMENT_SEPARATOR);
            }
        }
        $$2.append("]");
        this.result = $$2;
    }

    @Override
    public void visitList(ListTag listTag0) {
        if (listTag0.isEmpty()) {
            this.result = Component.literal("[]");
        } else if (INLINE_ELEMENT_TYPES.contains(listTag0.getElementType()) && listTag0.size() <= 8) {
            String $$1 = ELEMENT_SEPARATOR + " ";
            MutableComponent $$2 = Component.literal("[");
            for (int $$3 = 0; $$3 < listTag0.size(); $$3++) {
                if ($$3 != 0) {
                    $$2.append($$1);
                }
                $$2.append(new TextComponentTagVisitor(this.indentation, this.depth).visit(listTag0.get($$3)));
            }
            $$2.append("]");
            this.result = $$2;
        } else {
            MutableComponent $$4 = Component.literal("[");
            if (!this.indentation.isEmpty()) {
                $$4.append("\n");
            }
            for (int $$5 = 0; $$5 < listTag0.size(); $$5++) {
                MutableComponent $$6 = Component.literal(Strings.repeat(this.indentation, this.depth + 1));
                $$6.append(new TextComponentTagVisitor(this.indentation, this.depth + 1).visit(listTag0.get($$5)));
                if ($$5 != listTag0.size() - 1) {
                    $$6.append(ELEMENT_SEPARATOR).append(this.indentation.isEmpty() ? " " : "\n");
                }
                $$4.append($$6);
            }
            if (!this.indentation.isEmpty()) {
                $$4.append("\n").append(Strings.repeat(this.indentation, this.depth));
            }
            $$4.append("]");
            this.result = $$4;
        }
    }

    @Override
    public void visitCompound(CompoundTag compoundTag0) {
        if (compoundTag0.isEmpty()) {
            this.result = Component.literal("{}");
        } else {
            MutableComponent $$1 = Component.literal("{");
            Collection<String> $$2 = compoundTag0.getAllKeys();
            if (LOGGER.isDebugEnabled()) {
                List<String> $$3 = Lists.newArrayList(compoundTag0.getAllKeys());
                Collections.sort($$3);
                $$2 = $$3;
            }
            if (!this.indentation.isEmpty()) {
                $$1.append("\n");
            }
            Iterator<String> $$4 = $$2.iterator();
            while ($$4.hasNext()) {
                String $$5 = (String) $$4.next();
                MutableComponent $$6 = Component.literal(Strings.repeat(this.indentation, this.depth + 1)).append(handleEscapePretty($$5)).append(NAME_VALUE_SEPARATOR).append(" ").append(new TextComponentTagVisitor(this.indentation, this.depth + 1).visit(compoundTag0.get($$5)));
                if ($$4.hasNext()) {
                    $$6.append(ELEMENT_SEPARATOR).append(this.indentation.isEmpty() ? " " : "\n");
                }
                $$1.append($$6);
            }
            if (!this.indentation.isEmpty()) {
                $$1.append("\n").append(Strings.repeat(this.indentation, this.depth));
            }
            $$1.append("}");
            this.result = $$1;
        }
    }

    protected static Component handleEscapePretty(String string0) {
        if (SIMPLE_VALUE.matcher(string0).matches()) {
            return Component.literal(string0).withStyle(SYNTAX_HIGHLIGHTING_KEY);
        } else {
            String $$1 = StringTag.quoteAndEscape(string0);
            String $$2 = $$1.substring(0, 1);
            Component $$3 = Component.literal($$1.substring(1, $$1.length() - 1)).withStyle(SYNTAX_HIGHLIGHTING_KEY);
            return Component.literal($$2).append($$3).append($$2);
        }
    }

    @Override
    public void visitEnd(EndTag endTag0) {
        this.result = CommonComponents.EMPTY;
    }
}