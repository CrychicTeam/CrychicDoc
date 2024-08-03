package dev.ftb.mods.ftblibrary.snbt;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

class SNBTParser {

    private final char[] buffer;

    private int position;

    static SNBTCompoundTag read(List<String> lines) {
        SNBTParser parser = new SNBTParser(lines);
        return (SNBTCompoundTag) SpecialTag.unwrap(parser.readTag(parser.nextNS()));
    }

    private SNBTParser(List<String> lines) {
        StringBuilder bufferBuilder = new StringBuilder();
        for (String line : lines) {
            String tline = line.trim();
            if (!tline.startsWith("//") && !tline.startsWith("#")) {
                bufferBuilder.append(line);
            }
            bufferBuilder.append('\n');
        }
        this.buffer = bufferBuilder.toString().toCharArray();
        if (this.buffer.length < 2) {
            throw new SNBTSyntaxException("File has to have at least two characters!");
        } else {
            this.position = 0;
        }
    }

    private String posString() {
        return this.posString(this.position);
    }

    private String posString(int p) {
        if (p >= this.buffer.length) {
            return "EOF";
        } else {
            int row = 0;
            int col = 0;
            for (int i = 0; i < p; i++) {
                if (this.buffer[i] == '\n') {
                    row++;
                    col = 0;
                } else {
                    col++;
                }
            }
            return row + 1 + ":" + (col + 1);
        }
    }

    private char next() {
        if (this.position >= this.buffer.length) {
            throw new SNBTEOFException();
        } else {
            char c = this.buffer[this.position];
            this.position++;
            return c;
        }
    }

    private char nextNS() {
        char c;
        do {
            c = this.next();
        } while (c <= ' ');
        return c;
    }

    private Tag readTag(char first) {
        switch(first) {
            case '"':
                return StringTag.valueOf(this.readQuotedString('"'));
            case '\'':
                return StringTag.valueOf(this.readQuotedString('\''));
            case '[':
                return this.readCollection();
            case '{':
                return this.readCompound();
            default:
                String s = this.readWordString(first);
                ???;
        }
    }

    private SNBTCompoundTag readCompound() {
        SNBTCompoundTag tag = new SNBTCompoundTag();
        while (true) {
            char c = this.nextNS();
            if (c == '}') {
                return tag;
            }
            if (c != ',' && c != '\n') {
                String key;
                if (c == '"') {
                    key = this.readQuotedString('"');
                } else if (c == '\'') {
                    key = this.readQuotedString('\'');
                } else {
                    key = this.readWordString(c);
                }
                char n = this.nextNS();
                if (n != ':' && n != '=') {
                    throw new SNBTSyntaxException("Expected ':', got '" + n + "' @ " + this.posString());
                }
                Tag t = this.readTag(this.nextNS());
                if (t == SpecialTag.TRUE) {
                    tag.getOrCreateProperties(key).valueType = 2;
                } else if (t == SpecialTag.FALSE) {
                    tag.getOrCreateProperties(key).valueType = 1;
                }
                tag.m_128365_(key, SpecialTag.unwrap(t));
            }
        }
    }

    private CollectionTag<?> readCollection() {
        int prevPos = this.position;
        char next1 = this.nextNS();
        char next2 = this.nextNS();
        if (next2 != ';' || next1 != 'I' && next1 != 'i' && next1 != 'L' && next1 != 'l' && next1 != 'B' && next1 != 'b') {
            this.position = prevPos;
            return this.readList();
        } else {
            return this.readArray(prevPos, next1);
        }
    }

    private ListTag readList() {
        ListTag tag = new ListTag();
        while (true) {
            int prevPos = this.position;
            char c = this.nextNS();
            if (c == ']') {
                return tag;
            }
            if (c != ',') {
                Tag t = SpecialTag.unwrap(this.readTag(c));
                try {
                    tag.add(t);
                } catch (UnsupportedOperationException var6) {
                    throw new SNBTSyntaxException("Unexpected tag '" + t + "' in list @ " + this.posString(prevPos) + " - can't mix two different tag types in a list!");
                }
            }
        }
    }

    private CollectionTag<?> readArray(int pos, char type) {
        List<Integer> intList = new ArrayList();
        List<Long> longList = new ArrayList();
        List<Byte> byteList = new ArrayList();
        type = Character.toLowerCase(type);
        while (true) {
            char c = this.nextNS();
            if (c == ']') {
                return (CollectionTag<?>) (switch(type) {
                    case 'b' ->
                        new ByteArrayTag(byteList);
                    case 'i' ->
                        new IntArrayTag(intList);
                    case 'l' ->
                        new LongArrayTag(longList);
                    default ->
                        throw new SNBTSyntaxException("Unknown array type: " + type + " @ " + this.posString(pos));
                });
            }
            if (c != ',') {
                Tag tag = SpecialTag.unwrap(this.readTag(c));
                if (!(tag instanceof NumericTag numericTag)) {
                    throw new SNBTSyntaxException("Unexpected tag '" + tag + "' in list @ " + this.posString() + " - expected a numeric tag!");
                }
                switch(type) {
                    case 'b':
                        byteList.add(numericTag.getAsByte());
                        break;
                    case 'i':
                        intList.add(numericTag.getAsInt());
                        break;
                    case 'l':
                        longList.add(numericTag.getAsLong());
                }
            }
        }
    }

    private String readWordString(char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        while (true) {
            char c = this.next();
            if (!SNBTUtils.isSimpleCharacter(c)) {
                this.position--;
                return sb.toString();
            }
            sb.append(c);
        }
    }

    private String readQuotedString(char stop) {
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        while (true) {
            char c = this.next();
            if (c == '\n') {
                throw new SNBTSyntaxException("New line without closing string with " + stop + " @ " + this.posString(this.position - 1) + "!");
            }
            if (escape) {
                escape = false;
                if (SNBTUtils.REVERSE_ESCAPE_CHARS[c] != 0) {
                    sb.append(SNBTUtils.REVERSE_ESCAPE_CHARS[c]);
                }
            } else if (c == '\\') {
                escape = true;
            } else {
                if (c == stop) {
                    return sb.toString();
                }
                sb.append(c);
            }
        }
    }
}