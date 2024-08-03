package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class NBTConfig extends ConfigFromString<CompoundTag> {

    public static final Component EMPTY_NBT = Component.literal("{}");

    public static final Component NON_EMPTY_NBT = Component.literal("{...}");

    public CompoundTag copy(CompoundTag v) {
        return v.copy();
    }

    public String getStringFromValue(@Nullable CompoundTag v) {
        return v == null ? "null" : v.toString();
    }

    public Component getStringForGUI(@Nullable CompoundTag v) {
        return v == null ? NULL_TEXT : (v.isEmpty() ? EMPTY_NBT : NON_EMPTY_NBT);
    }

    @Override
    public boolean parse(@Nullable Consumer<CompoundTag> callback, String string) {
        if (string.equals("null")) {
            return this.okValue(callback, null);
        } else {
            try {
                return this.okValue(callback, TagParser.parseTag(string));
            } catch (Exception var4) {
                return false;
            }
        }
    }

    @Override
    public void addInfo(TooltipList list) {
        list.add(info("Value", this.value == null ? "null" : this.value));
        list.add(info("Default", this.defaultValue == null ? "null" : this.defaultValue));
    }
}