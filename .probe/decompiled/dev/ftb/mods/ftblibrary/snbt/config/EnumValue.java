package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnumValue<T> extends BaseValue<T> {

    private final NameMap<T> nameMap;

    EnumValue(SNBTConfig c, String key, NameMap<T> nameMap) {
        this(c, key, nameMap, nameMap.defaultValue);
    }

    EnumValue(SNBTConfig c, String key, NameMap<T> nameMap, T def) {
        super(c, key, def);
        this.nameMap = nameMap;
    }

    @Override
    public void set(T v) {
        if (this.nameMap.values.contains(v)) {
            super.set(v);
        } else {
            super.set(this.defaultValue);
        }
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        List<String> s = new ArrayList(this.comment);
        s.add("Default: \"" + this.nameMap.getName(this.defaultValue) + "\"");
        s.add("Valid values: " + (String) this.nameMap.keys.stream().map(StringTag::m_129303_).collect(Collectors.joining(", ")));
        tag.comment(this.key, String.join("\n", s));
        tag.m_128359_(this.key, this.nameMap.getName(this.get()));
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        this.set(this.nameMap.get(tag.m_128461_(this.key)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
        group.addEnum(this.key, this.get(), this::set, this.nameMap).setCanEdit(this.enabled.getAsBoolean());
    }
}