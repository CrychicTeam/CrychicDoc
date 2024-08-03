package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;

public class TagParser {

    public static final SimpleCommandExceptionType ERROR_TRAILING_DATA = new SimpleCommandExceptionType(Component.translatable("argument.nbt.trailing"));

    public static final SimpleCommandExceptionType ERROR_EXPECTED_KEY = new SimpleCommandExceptionType(Component.translatable("argument.nbt.expected.key"));

    public static final SimpleCommandExceptionType ERROR_EXPECTED_VALUE = new SimpleCommandExceptionType(Component.translatable("argument.nbt.expected.value"));

    public static final Dynamic2CommandExceptionType ERROR_INSERT_MIXED_LIST = new Dynamic2CommandExceptionType((p_129366_, p_129367_) -> Component.translatable("argument.nbt.list.mixed", p_129366_, p_129367_));

    public static final Dynamic2CommandExceptionType ERROR_INSERT_MIXED_ARRAY = new Dynamic2CommandExceptionType((p_129357_, p_129358_) -> Component.translatable("argument.nbt.array.mixed", p_129357_, p_129358_));

    public static final DynamicCommandExceptionType ERROR_INVALID_ARRAY = new DynamicCommandExceptionType(p_129355_ -> Component.translatable("argument.nbt.array.invalid", p_129355_));

    public static final char ELEMENT_SEPARATOR = ',';

    public static final char NAME_VALUE_SEPARATOR = ':';

    private static final char LIST_OPEN = '[';

    private static final char LIST_CLOSE = ']';

    private static final char STRUCT_CLOSE = '}';

    private static final char STRUCT_OPEN = '{';

    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);

    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);

    private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);

    private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);

    private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);

    private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);

    private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

    private final StringReader reader;

    public static CompoundTag parseTag(String string0) throws CommandSyntaxException {
        return new TagParser(new StringReader(string0)).readSingleStruct();
    }

    @VisibleForTesting
    CompoundTag readSingleStruct() throws CommandSyntaxException {
        CompoundTag $$0 = this.readStruct();
        this.reader.skipWhitespace();
        if (this.reader.canRead()) {
            throw ERROR_TRAILING_DATA.createWithContext(this.reader);
        } else {
            return $$0;
        }
    }

    public TagParser(StringReader stringReader0) {
        this.reader = stringReader0;
    }

    protected String readKey() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw ERROR_EXPECTED_KEY.createWithContext(this.reader);
        } else {
            return this.reader.readString();
        }
    }

    protected Tag readTypedValue() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        int $$0 = this.reader.getCursor();
        if (StringReader.isQuotedStringStart(this.reader.peek())) {
            return StringTag.valueOf(this.reader.readQuotedString());
        } else {
            String $$1 = this.reader.readUnquotedString();
            if ($$1.isEmpty()) {
                this.reader.setCursor($$0);
                throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
            } else {
                return this.type($$1);
            }
        }
    }

    private Tag type(String string0) {
        try {
            if (FLOAT_PATTERN.matcher(string0).matches()) {
                return FloatTag.valueOf(Float.parseFloat(string0.substring(0, string0.length() - 1)));
            }
            if (BYTE_PATTERN.matcher(string0).matches()) {
                return ByteTag.valueOf(Byte.parseByte(string0.substring(0, string0.length() - 1)));
            }
            if (LONG_PATTERN.matcher(string0).matches()) {
                return LongTag.valueOf(Long.parseLong(string0.substring(0, string0.length() - 1)));
            }
            if (SHORT_PATTERN.matcher(string0).matches()) {
                return ShortTag.valueOf(Short.parseShort(string0.substring(0, string0.length() - 1)));
            }
            if (INT_PATTERN.matcher(string0).matches()) {
                return IntTag.valueOf(Integer.parseInt(string0));
            }
            if (DOUBLE_PATTERN.matcher(string0).matches()) {
                return DoubleTag.valueOf(Double.parseDouble(string0.substring(0, string0.length() - 1)));
            }
            if (DOUBLE_PATTERN_NOSUFFIX.matcher(string0).matches()) {
                return DoubleTag.valueOf(Double.parseDouble(string0));
            }
            if ("true".equalsIgnoreCase(string0)) {
                return ByteTag.ONE;
            }
            if ("false".equalsIgnoreCase(string0)) {
                return ByteTag.ZERO;
            }
        } catch (NumberFormatException var3) {
        }
        return StringTag.valueOf(string0);
    }

    public Tag readValue() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
        } else {
            char $$0 = this.reader.peek();
            if ($$0 == '{') {
                return this.readStruct();
            } else {
                return $$0 == '[' ? this.readList() : this.readTypedValue();
            }
        }
    }

    protected Tag readList() throws CommandSyntaxException {
        return this.reader.canRead(3) && !StringReader.isQuotedStringStart(this.reader.peek(1)) && this.reader.peek(2) == ';' ? this.readArrayTag() : this.readListTag();
    }

    public CompoundTag readStruct() throws CommandSyntaxException {
        this.expect('{');
        CompoundTag $$0 = new CompoundTag();
        this.reader.skipWhitespace();
        while (this.reader.canRead() && this.reader.peek() != '}') {
            int $$1 = this.reader.getCursor();
            String $$2 = this.readKey();
            if ($$2.isEmpty()) {
                this.reader.setCursor($$1);
                throw ERROR_EXPECTED_KEY.createWithContext(this.reader);
            }
            this.expect(':');
            $$0.put($$2, this.readValue());
            if (!this.hasElementSeparator()) {
                break;
            }
            if (!this.reader.canRead()) {
                throw ERROR_EXPECTED_KEY.createWithContext(this.reader);
            }
        }
        this.expect('}');
        return $$0;
    }

    private Tag readListTag() throws CommandSyntaxException {
        this.expect('[');
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
        } else {
            ListTag $$0 = new ListTag();
            TagType<?> $$1 = null;
            while (this.reader.peek() != ']') {
                int $$2 = this.reader.getCursor();
                Tag $$3 = this.readValue();
                TagType<?> $$4 = $$3.getType();
                if ($$1 == null) {
                    $$1 = $$4;
                } else if ($$4 != $$1) {
                    this.reader.setCursor($$2);
                    throw ERROR_INSERT_MIXED_LIST.createWithContext(this.reader, $$4.getPrettyName(), $$1.getPrettyName());
                }
                $$0.add($$3);
                if (!this.hasElementSeparator()) {
                    break;
                }
                if (!this.reader.canRead()) {
                    throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
                }
            }
            this.expect(']');
            return $$0;
        }
    }

    private Tag readArrayTag() throws CommandSyntaxException {
        this.expect('[');
        int $$0 = this.reader.getCursor();
        char $$1 = this.reader.read();
        this.reader.read();
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
        } else if ($$1 == 'B') {
            return new ByteArrayTag(this.readArray(ByteArrayTag.TYPE, ByteTag.TYPE));
        } else if ($$1 == 'L') {
            return new LongArrayTag(this.readArray(LongArrayTag.TYPE, LongTag.TYPE));
        } else if ($$1 == 'I') {
            return new IntArrayTag(this.readArray(IntArrayTag.TYPE, IntTag.TYPE));
        } else {
            this.reader.setCursor($$0);
            throw ERROR_INVALID_ARRAY.createWithContext(this.reader, String.valueOf($$1));
        }
    }

    private <T extends Number> List<T> readArray(TagType<?> tagType0, TagType<?> tagType1) throws CommandSyntaxException {
        List<T> $$2 = Lists.newArrayList();
        while (this.reader.peek() != ']') {
            int $$3 = this.reader.getCursor();
            Tag $$4 = this.readValue();
            TagType<?> $$5 = $$4.getType();
            if ($$5 != tagType1) {
                this.reader.setCursor($$3);
                throw ERROR_INSERT_MIXED_ARRAY.createWithContext(this.reader, $$5.getPrettyName(), tagType0.getPrettyName());
            }
            if (tagType1 == ByteTag.TYPE) {
                $$2.add(((NumericTag) $$4).getAsByte());
            } else if (tagType1 == LongTag.TYPE) {
                $$2.add(((NumericTag) $$4).getAsLong());
            } else {
                $$2.add(((NumericTag) $$4).getAsInt());
            }
            if (!this.hasElementSeparator()) {
                break;
            }
            if (!this.reader.canRead()) {
                throw ERROR_EXPECTED_VALUE.createWithContext(this.reader);
            }
        }
        this.expect(']');
        return $$2;
    }

    private boolean hasElementSeparator() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == ',') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    private void expect(char char0) throws CommandSyntaxException {
        this.reader.skipWhitespace();
        this.reader.expect(char0);
    }
}