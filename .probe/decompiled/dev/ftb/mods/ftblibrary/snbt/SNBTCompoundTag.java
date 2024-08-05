package dev.ftb.mods.ftblibrary.snbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public class SNBTCompoundTag extends CompoundTag {

    private HashMap<String, SNBTTagProperties> properties;

    boolean singleLine = false;

    public static SNBTCompoundTag of(@Nullable Tag tag) {
        if (tag instanceof SNBTCompoundTag) {
            return (SNBTCompoundTag) tag;
        } else if (!(tag instanceof CompoundTag)) {
            return new SNBTCompoundTag();
        } else {
            SNBTCompoundTag tag1 = new SNBTCompoundTag();
            for (String s : ((CompoundTag) tag).getAllKeys()) {
                tag1.m_128365_(s, ((CompoundTag) tag).get(s));
            }
            return tag1;
        }
    }

    public SNBTCompoundTag() {
        super(new LinkedHashMap());
    }

    SNBTTagProperties getOrCreateProperties(String key) {
        if (this.properties == null) {
            this.properties = new HashMap();
        }
        SNBTTagProperties p = (SNBTTagProperties) this.properties.get(key);
        if (p == null) {
            p = new SNBTTagProperties();
            this.properties.put(key, p);
        }
        return p;
    }

    SNBTTagProperties getProperties(String key) {
        if (this.properties != null) {
            SNBTTagProperties p = (SNBTTagProperties) this.properties.get(key);
            if (p != null) {
                return p;
            }
        }
        return SNBTTagProperties.DEFAULT;
    }

    public void comment(String key, String... comment) {
        if (comment.length > 0) {
            this.comment(key, String.join("\n", comment));
        }
    }

    public void comment(String key, String comment) {
        String s = comment == null ? "" : comment.trim();
        if (!s.isEmpty()) {
            this.getOrCreateProperties(key).comment = comment;
        }
    }

    public String getComment(String key) {
        return this.getProperties(key).comment;
    }

    public void singleLine() {
        this.singleLine = true;
    }

    public void singleLine(String key) {
        this.getOrCreateProperties(key).singleLine = true;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        this.getOrCreateProperties(key).valueType = value ? 2 : 1;
        super.putBoolean(key, value);
    }

    public boolean isBoolean(String key) {
        int t = this.getProperties(key).valueType;
        return t == 2 || t == 1;
    }

    public SNBTCompoundTag getCompound(String string) {
        return of(this.m_128423_(string));
    }

    public void putNumber(String key, Number number) {
        if (number instanceof Double) {
            this.m_128347_(key, number.doubleValue());
        } else if (number instanceof Float) {
            this.m_128350_(key, number.floatValue());
        } else if (number instanceof Long) {
            this.m_128356_(key, number.longValue());
        } else if (number instanceof Integer) {
            this.m_128405_(key, number.intValue());
        } else if (number instanceof Short) {
            this.m_128376_(key, number.shortValue());
        } else if (number instanceof Byte) {
            this.m_128344_(key, number.byteValue());
        } else if (number.toString().contains(".")) {
            this.m_128347_(key, number.doubleValue());
        } else {
            this.m_128405_(key, number.intValue());
        }
    }

    public void putNull(String key) {
        this.m_128365_(key, EndTag.INSTANCE);
    }

    @Nullable
    public ListTag getNullableList(String key, byte type) {
        Tag tag = this.m_128423_(key);
        return !(tag instanceof ListTag) || !((ListTag) tag).isEmpty() && type != 0 && ((ListTag) tag).getElementType() != type ? null : (ListTag) tag;
    }

    public <T extends Tag> List<T> getList(String key, Class<T> type) {
        if (this.m_128423_(key) instanceof CollectionTag<?> l) {
            if (l.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<T> list = new ArrayList(l.size());
                for (Tag t : l) {
                    if (type.isAssignableFrom(t.getClass())) {
                        list.add(t);
                    }
                }
                return list;
            }
        } else {
            return Collections.emptyList();
        }
    }
}