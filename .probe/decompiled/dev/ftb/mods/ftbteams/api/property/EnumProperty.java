package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnumProperty extends TeamProperty<String> {

    private final List<String> values;

    private final Map<String, Component> names;

    public EnumProperty(ResourceLocation id, Supplier<String> def, List<String> values, Map<String, Component> names) {
        super(id, def);
        this.values = values;
        this.names = names;
    }

    public <T> EnumProperty(ResourceLocation id, NameMap<T> nameMap) {
        this(id, () -> nameMap.getName(nameMap.defaultValue), nameMap.keys, buildMap(nameMap));
    }

    private static <T> Map<String, Component> buildMap(NameMap<T> nameMap) {
        Map<String, Component> res = new HashMap();
        nameMap.forEach(val -> res.put(nameMap.getName((T) val), nameMap.getDisplayName((T) val)));
        return res;
    }

    static EnumProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        String def = buf.readUtf(32767);
        List<String> values = buf.readList(b -> b.readUtf(32767));
        Map<String, Component> names = buf.readMap(b -> b.readUtf(32767), FriendlyByteBuf::m_130238_);
        return new EnumProperty(id, () -> def, values, names);
    }

    @Override
    public TeamPropertyType<String> getType() {
        return TeamPropertyType.ENUM;
    }

    @Override
    public Optional<String> fromString(String string) {
        return Optional.of(string);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.getDefaultValue(), 32767);
        buf.writeCollection(this.values, FriendlyByteBuf::m_130070_);
        buf.writeMap(this.names, FriendlyByteBuf::m_130070_, FriendlyByteBuf::m_130083_);
    }

    @Override
    public void config(ConfigGroup config, TeamPropertyValue<String> value) {
        config.addEnum(this.id.getPath(), value.value, value.consumer, NameMap.of(this.getDefaultValue(), this.values).name(s -> (Component) this.names.getOrDefault(s, Component.literal(s))).create());
    }

    public Tag toNBT(String value) {
        return StringTag.valueOf(value);
    }

    @Override
    public Optional<String> fromNBT(Tag tag) {
        return tag instanceof StringTag ? Optional.of(tag.getAsString()) : Optional.empty();
    }
}