package noppes.npcs.util;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import noppes.npcs.mixin.ListNBTMixin;
import org.apache.commons.io.Charsets;

public class NBTJsonUtil {

    public static String Convert(CompoundTag compound) {
        List<NBTJsonUtil.JsonLine> list = new ArrayList();
        NBTJsonUtil.JsonLine line = ReadTag("", compound, list);
        line.removeComma();
        return ConvertList(list);
    }

    public static CompoundTag Convert(String json) throws NBTJsonUtil.JsonException {
        json = json.trim();
        NBTJsonUtil.JsonFile file = new NBTJsonUtil.JsonFile(json);
        if (json.startsWith("{") && json.endsWith("}")) {
            CompoundTag compound = new CompoundTag();
            FillCompound(compound, file);
            return compound;
        } else {
            throw new NBTJsonUtil.JsonException("Not properly incapsulated between { }", file);
        }
    }

    public static void FillCompound(CompoundTag compound, NBTJsonUtil.JsonFile json) throws NBTJsonUtil.JsonException {
        if (json.startsWith("{") || json.startsWith(",")) {
            json.cut(1);
        }
        if (!json.startsWith("}")) {
            int index = json.keyIndex();
            if (index < 1) {
                throw new NBTJsonUtil.JsonException("Expected key after ,", json);
            } else {
                String key = json.substring(0, index);
                json.cut(index + 1);
                Tag base = ReadValue(json);
                if (base == null) {
                    base = StringTag.valueOf("");
                }
                if (key.startsWith("\"")) {
                    key = key.substring(1);
                }
                if (key.endsWith("\"")) {
                    key = key.substring(0, key.length() - 1);
                }
                compound.put(key, base);
                if (json.startsWith(",")) {
                    FillCompound(compound, json);
                }
            }
        }
    }

    public static Tag ReadValue(NBTJsonUtil.JsonFile json) throws NBTJsonUtil.JsonException {
        if (json.startsWith("{")) {
            CompoundTag compound = new CompoundTag();
            FillCompound(compound, json);
            if (!json.startsWith("}")) {
                throw new NBTJsonUtil.JsonException("Expected }", json);
            } else {
                json.cut(1);
                return compound;
            }
        } else if (json.startsWith("[")) {
            json.cut(1);
            ListTag list = new ListTag();
            if (json.startsWith("B;") || json.startsWith("I;") || json.startsWith("L;")) {
                json.cut(2);
            }
            for (Tag value = ReadValue(json); value != null; value = ReadValue(json)) {
                list.add(value);
                if (!json.startsWith(",")) {
                    break;
                }
                json.cut(1);
            }
            if (!json.startsWith("]")) {
                throw new NBTJsonUtil.JsonException("Expected ]", json);
            } else {
                json.cut(1);
                if (list.getElementType() == 3) {
                    int[] arr = new int[list.size()];
                    for (int i = 0; list.size() > 0; i++) {
                        arr[i] = ((IntTag) list.remove(0)).getAsInt();
                    }
                    return new IntArrayTag(arr);
                } else if (list.getElementType() == 1) {
                    byte[] arr = new byte[list.size()];
                    for (int i = 0; list.size() > 0; i++) {
                        arr[i] = ((ByteTag) list.remove(0)).getAsByte();
                    }
                    return new ByteArrayTag(arr);
                } else if (list.getElementType() != 4) {
                    return list;
                } else {
                    long[] arr = new long[list.size()];
                    for (int i = 0; list.size() > 0; i++) {
                        arr[i] = (long) ((LongTag) list.remove(0)).getAsByte();
                    }
                    return new LongArrayTag(arr);
                }
            }
        } else if (json.startsWith("\"")) {
            json.cut(1);
            String s = "";
            boolean ignore = false;
            while (!json.startsWith("\"") || ignore) {
                String cut = json.cutDirty(1);
                ignore = cut.equals("\\");
                s = s + cut;
            }
            json.cut(1);
            return StringTag.valueOf(s.replace("\\\\", "\\").replace("\\\"", "\""));
        } else {
            String s = "";
            while (!json.startsWith(",", "]", "}")) {
                s = s + json.cut(1);
            }
            s = s.trim().toLowerCase();
            if (s.isEmpty()) {
                return null;
            } else {
                try {
                    if (s.endsWith("d")) {
                        return DoubleTag.valueOf(Double.parseDouble(s.substring(0, s.length() - 1)));
                    } else if (s.endsWith("f")) {
                        return FloatTag.valueOf(Float.parseFloat(s.substring(0, s.length() - 1)));
                    } else if (s.endsWith("b")) {
                        return ByteTag.valueOf(Byte.parseByte(s.substring(0, s.length() - 1)));
                    } else if (s.endsWith("s")) {
                        return ShortTag.valueOf(Short.parseShort(s.substring(0, s.length() - 1)));
                    } else if (s.endsWith("l")) {
                        return LongTag.valueOf(Long.parseLong(s.substring(0, s.length() - 1)));
                    } else {
                        return (Tag) (s.contains(".") ? DoubleTag.valueOf(Double.parseDouble(s)) : IntTag.valueOf(Integer.parseInt(s)));
                    }
                } catch (NumberFormatException var5) {
                    throw new NBTJsonUtil.JsonException("Unable to convert: " + s + " to a number", json);
                }
            }
        }
    }

    private static NBTJsonUtil.JsonLine ReadTag(String name, Tag base, List<NBTJsonUtil.JsonLine> list) {
        if (!name.isEmpty()) {
            name = "\"" + name + "\": ";
        }
        if (base.getId() == 9) {
            list.add(new NBTJsonUtil.JsonLine(name + "["));
            ListTag tags = (ListTag) base;
            NBTJsonUtil.JsonLine line = null;
            for (Tag b : ((ListNBTMixin) tags).getList()) {
                line = ReadTag("", b, list);
            }
            if (line != null) {
                line.removeComma();
            }
            list.add(new NBTJsonUtil.JsonLine("]"));
        } else if (base.getId() == 10) {
            list.add(new NBTJsonUtil.JsonLine(name + "{"));
            CompoundTag compound = (CompoundTag) base;
            NBTJsonUtil.JsonLine line = null;
            for (Object key : compound.getAllKeys()) {
                line = ReadTag(key.toString(), compound.get(key.toString()), list);
            }
            if (line != null) {
                line.removeComma();
            }
            list.add(new NBTJsonUtil.JsonLine("}"));
        } else if (base.getId() == 11) {
            list.add(new NBTJsonUtil.JsonLine(name + base.toString().replaceFirst(",]", "]")));
        } else if (base.getId() == 8) {
            list.add(new NBTJsonUtil.JsonLine(name + quoteAndEscape(base.getAsString())));
        } else {
            list.add(new NBTJsonUtil.JsonLine(name + base));
        }
        NBTJsonUtil.JsonLine line = (NBTJsonUtil.JsonLine) list.get(list.size() - 1);
        line.line = line.line + ",";
        return line;
    }

    private static String ConvertList(List<NBTJsonUtil.JsonLine> list) {
        String json = "";
        int tab = 0;
        for (NBTJsonUtil.JsonLine tag : list) {
            if (tag.reduceTab()) {
                tab--;
            }
            for (int i = 0; i < tab; i++) {
                json = json + "    ";
            }
            json = json + tag + "\n";
            if (tag.increaseTab()) {
                tab++;
            }
        }
        return json;
    }

    public static CompoundTag LoadFile(File file) throws IOException, NBTJsonUtil.JsonException {
        return Convert(Files.toString(file, Charsets.UTF_8));
    }

    public static void SaveFile(File file, CompoundTag compound) throws IOException, NBTJsonUtil.JsonException {
        String json = Convert(compound);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
            writer.write(json);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void main(String[] args) {
        CompoundTag comp = new CompoundTag();
        CompoundTag comp2 = new CompoundTag();
        comp2.putByteArray("test", new byte[] { 0, 0, 1, 1, 0 });
        comp.put("comp", comp2);
        System.out.println(Convert(comp));
    }

    public static String quoteAndEscape(String p_193588_0_) {
        StringBuilder stringbuilder = new StringBuilder("\"");
        for (int i = 0; i < p_193588_0_.length(); i++) {
            char c0 = p_193588_0_.charAt(i);
            if (c0 == '\\' || c0 == '"') {
                stringbuilder.append('\\');
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.append('"').toString();
    }

    public static class JsonException extends Exception {

        public JsonException(String message, NBTJsonUtil.JsonFile json) {
            super(message + ": " + json.getCurrentPos());
        }
    }

    static class JsonFile {

        private String original;

        private String text;

        public JsonFile(String text) {
            this.text = text;
            this.original = text;
        }

        public int keyIndex() {
            boolean hasQuote = false;
            for (int i = 0; i < this.text.length(); i++) {
                char c = this.text.charAt(i);
                if (i == 0 && c == '"') {
                    hasQuote = true;
                } else if (hasQuote && c == '"') {
                    hasQuote = false;
                }
                if (!hasQuote && c == ':') {
                    return i;
                }
            }
            return -1;
        }

        public String cutDirty(int i) {
            String s = this.text.substring(0, i);
            this.text = this.text.substring(i);
            return s;
        }

        public String cut(int i) {
            String s = this.text.substring(0, i);
            this.text = this.text.substring(i).trim();
            return s;
        }

        public String substring(int beginIndex, int endIndex) {
            return this.text.substring(beginIndex, endIndex);
        }

        public int indexOf(String s) {
            return this.text.indexOf(s);
        }

        public String getCurrentPos() {
            int lengthOr = this.original.length();
            int lengthCur = this.text.length();
            int currentPos = lengthOr - lengthCur;
            String done = this.original.substring(0, currentPos);
            String[] lines = done.split("\r\n|\r|\n");
            int pos = 0;
            String line = "";
            if (lines.length > 0) {
                pos = lines[lines.length - 1].length();
                line = this.original.split("\r\n|\r|\n")[lines.length - 1].trim();
            }
            return "Line: " + lines.length + ", Pos: " + pos + ", Text: " + line;
        }

        public boolean startsWith(String... ss) {
            for (String s : ss) {
                if (this.text.startsWith(s)) {
                    return true;
                }
            }
            return false;
        }

        public boolean endsWith(String s) {
            return this.text.endsWith(s);
        }
    }

    static class JsonLine {

        private String line;

        public JsonLine(String line) {
            this.line = line;
        }

        public void removeComma() {
            if (this.line.endsWith(",")) {
                this.line = this.line.substring(0, this.line.length() - 1);
            }
        }

        public boolean reduceTab() {
            int length = this.line.length();
            return length == 1 && (this.line.endsWith("}") || this.line.endsWith("]")) || length == 2 && (this.line.endsWith("},") || this.line.endsWith("],"));
        }

        public boolean increaseTab() {
            return this.line.endsWith("{") || this.line.endsWith("[");
        }

        public String toString() {
            return this.line;
        }
    }
}