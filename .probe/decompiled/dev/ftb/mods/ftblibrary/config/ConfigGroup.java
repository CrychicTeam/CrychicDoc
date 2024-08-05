package dev.ftb.mods.ftblibrary.config;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigGroup implements Comparable<ConfigGroup> {

    private final String id;

    private final ConfigGroup parent;

    private final Map<String, ConfigValue<?>> values;

    private final Map<String, ConfigGroup> subgroups;

    private final ConfigCallback savedCallback;

    private final int displayOrder;

    private String nameKey;

    private ConfigGroup(String id, ConfigGroup parent, ConfigCallback savedCallback, int displayOrder) {
        this.id = id;
        this.parent = parent;
        this.values = new LinkedHashMap();
        this.subgroups = new LinkedHashMap();
        this.savedCallback = savedCallback;
        this.nameKey = "";
        this.displayOrder = displayOrder;
    }

    public ConfigGroup(String id) {
        this(id, null, null, 0);
    }

    public ConfigGroup(String id, ConfigCallback savedCallback) {
        this(id, null, savedCallback, 0);
    }

    public String getId() {
        return this.id;
    }

    @Nullable
    public ConfigGroup getParent() {
        return this.parent;
    }

    public String getNameKey() {
        return this.nameKey.isEmpty() ? this.getPath() : this.nameKey;
    }

    public ConfigGroup setNameKey(String key) {
        this.nameKey = key;
        return this;
    }

    public Component getName() {
        return Component.translatable(this.getNameKey());
    }

    public Component getTooltip() {
        String t = this.getNameKey() + ".tooltip";
        return I18n.exists(t) ? Component.translatable(t) : Component.empty();
    }

    public ConfigGroup getOrCreateSubgroup(String id, int displayOrder) {
        int index = id.indexOf(46);
        return index == -1 ? (ConfigGroup) this.subgroups.computeIfAbsent(id, k -> new ConfigGroup(id, this, null, displayOrder)) : this.getOrCreateSubgroup(id.substring(0, index), displayOrder).getOrCreateSubgroup(id.substring(index + 1), displayOrder);
    }

    public ConfigGroup getOrCreateSubgroup(String id) {
        return this.getOrCreateSubgroup(id, 0);
    }

    public <T, CV extends ConfigValue<T>> CV add(String id, CV type, @Nullable T value, Consumer<T> setter, @Nullable T defaultValue) {
        this.values.put(id, type.init(this, id, value, setter, defaultValue));
        return type;
    }

    public BooleanConfig addBool(String id, boolean value, Consumer<Boolean> setter, boolean def) {
        return this.add(id, new BooleanConfig(), value, setter, def);
    }

    public IntConfig addInt(String id, int value, Consumer<Integer> setter, int def, int min, int max) {
        return this.add(id, new IntConfig(min, max), value, setter, def);
    }

    public LongConfig addLong(String id, long value, Consumer<Long> setter, long def, long min, long max) {
        return this.add(id, new LongConfig(min, max), value, setter, def);
    }

    public DoubleConfig addDouble(String id, double value, Consumer<Double> setter, double def, double min, double max) {
        return this.add(id, new DoubleConfig(min, max), value, setter, def);
    }

    public StringConfig addString(String id, String value, Consumer<String> setter, String def, @Nullable Pattern pattern) {
        return this.add(id, new StringConfig(pattern), value, setter, def);
    }

    public StringConfig addString(String id, String value, Consumer<String> setter, String def) {
        return this.addString(id, value, setter, def, null);
    }

    public <E> EnumConfig<E> addEnum(String id, E value, Consumer<E> setter, NameMap<E> nameMap, E def) {
        return this.add(id, new EnumConfig<>(nameMap), value, setter, def);
    }

    public <E> EnumConfig<E> addEnum(String id, E value, Consumer<E> setter, NameMap<E> nameMap) {
        return this.addEnum(id, value, setter, nameMap, nameMap.defaultValue);
    }

    public <E, CV extends ConfigValue<E>> ListConfig<E, CV> addList(String id, List<E> c, CV type, E def) {
        type.setDefaultValue(def);
        return this.add(id, new ListConfig<>(type), c, t -> {
            c.clear();
            c.addAll(t);
        }, Collections.emptyList());
    }

    public EnumConfig<Tristate> addTristate(String id, Tristate value, Consumer<Tristate> setter, Tristate def) {
        return this.addEnum(id, value, setter, Tristate.NAME_MAP, def);
    }

    public EnumConfig<Tristate> addTristate(String id, Tristate value, Consumer<Tristate> setter) {
        return this.addTristate(id, value, setter, Tristate.DEFAULT);
    }

    public ItemStackConfig addItemStack(String id, ItemStack value, Consumer<ItemStack> setter, ItemStack def, boolean singleItem, boolean allowEmpty) {
        return this.add(id, new ItemStackConfig(singleItem, allowEmpty), value, setter, def);
    }

    public ItemStackConfig addItemStack(String id, ItemStack value, Consumer<ItemStack> setter, ItemStack def, int fixedSize) {
        return this.add(id, new ItemStackConfig((long) fixedSize), value, setter, def);
    }

    public FluidConfig addFluidStack(String id, FluidStack value, Consumer<FluidStack> setter, FluidStack def, boolean allowEmpty) {
        return this.add(id, new FluidConfig(allowEmpty), value, setter, def);
    }

    public FluidConfig addFluidStack(String id, FluidStack value, Consumer<FluidStack> setter, FluidStack def, long fixedSize) {
        return this.add(id, new FluidConfig(fixedSize), value, setter, def);
    }

    public ImageResourceConfig addImage(String id, ResourceLocation value, Consumer<ResourceLocation> setter, ResourceLocation def) {
        return this.add(id, new ImageResourceConfig(), value, setter, def);
    }

    public ColorConfig addColor(String id, Color4I value, Consumer<Color4I> setter, Color4I def) {
        return this.add(id, new ColorConfig(), value, setter, def);
    }

    public final Collection<ConfigValue<?>> getValues() {
        return this.values.values();
    }

    public final Collection<ConfigGroup> getSubgroups() {
        return this.subgroups.values();
    }

    public String getPath() {
        return this.parent == null ? this.id : this.parent.getPath() + "." + this.id;
    }

    public void save(boolean accepted) {
        if (accepted) {
            this.values.values().forEach(ConfigValue::applyValue);
        }
        for (ConfigGroup group : this.subgroups.values()) {
            group.save(accepted);
        }
        if (this.savedCallback != null) {
            this.savedCallback.save(accepted);
        }
    }

    public int compareTo(@NotNull ConfigGroup o) {
        int i = Integer.compare(this.displayOrder, o.displayOrder);
        return i == 0 ? this.getPath().compareToIgnoreCase(o.getPath()) : i;
    }
}