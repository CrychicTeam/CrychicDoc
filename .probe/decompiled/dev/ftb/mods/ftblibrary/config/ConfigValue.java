package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigValue<T> implements Comparable<ConfigValue<T>> {

    public static final Component NULL_TEXT = Component.literal("null");

    private ConfigGroup group;

    protected T value;

    private Consumer<T> setter;

    protected T defaultValue;

    public String id = "";

    private int order = 0;

    private String nameKey = "";

    private Icon icon = Icons.SETTINGS;

    private boolean canEdit = true;

    public ConfigValue<T> init(ConfigGroup group, String id, @Nullable T value, Consumer<T> setter, @Nullable T defaultValue) {
        this.group = group;
        this.id = id;
        this.value = value == null ? null : this.copy(value);
        this.setter = setter;
        this.defaultValue = defaultValue;
        this.order = group.getValues().size();
        return this;
    }

    public final boolean setCurrentValue(@Nullable T v) {
        if (!this.isEqual(this.value, v)) {
            this.value = v;
            return true;
        } else {
            return false;
        }
    }

    public ConfigGroup getGroup() {
        return this.group;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean isEqual(@Nullable T v1, @Nullable T v2) {
        return Objects.equals(v1, v2);
    }

    public T copy(T value) {
        return value;
    }

    public final Color4I getColor() {
        return this.getColor(this.value);
    }

    public Color4I getColor(@Nullable T v) {
        return Color4I.GRAY;
    }

    public void addInfo(TooltipList list) {
        list.add(info("Default", this.getStringForGUI(this.defaultValue)));
    }

    protected static Component info(String key) {
        return Component.literal(key + ":").withStyle(ChatFormatting.AQUA);
    }

    public static Component info(String key, Object value) {
        Component c = (Component) (value instanceof Component ? (Component) value : Component.literal(String.valueOf(value)));
        return Component.literal("").append(Component.literal(key + ": ").withStyle(ChatFormatting.AQUA)).append(c);
    }

    public abstract void onClicked(Widget var1, MouseButton var2, ConfigCallback var3);

    public final Component getStringForGUI() {
        return Component.literal(String.valueOf(this.value));
    }

    public Component getStringForGUI(@Nullable T v) {
        return Component.literal(String.valueOf(v));
    }

    public String getPath() {
        if (this.group == null) {
            return this.id;
        } else {
            String p = this.group.getPath();
            return p.isEmpty() ? this.id : p + "." + this.id;
        }
    }

    public String getNameKey() {
        return this.nameKey.isEmpty() ? this.getPath() : this.nameKey;
    }

    public ConfigValue<T> setNameKey(String key) {
        this.nameKey = key;
        return this;
    }

    public String getName() {
        return I18n.get(this.getNameKey());
    }

    public String getTooltip() {
        String k = this.getNameKey() + ".tooltip";
        return I18n.exists(k) ? I18n.get(k) : "";
    }

    public ConfigValue<T> setOrder(int o) {
        this.order = o;
        return this;
    }

    public ConfigValue<T> setCanEdit(boolean e) {
        this.canEdit = e;
        return this;
    }

    public boolean getCanEdit() {
        return this.canEdit;
    }

    public ConfigValue<T> setIcon(Icon i) {
        this.icon = i;
        return this;
    }

    public Icon getIcon() {
        return this.getIcon(this.getValue());
    }

    public Icon getIcon(@Nullable T v) {
        return this.icon;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int compareTo(ConfigValue<T> o) {
        int i = this.group.compareTo(o.group);
        return i == 0 ? Integer.compare(this.order, o.order) : i;
    }

    public void applyValue() {
        this.setter.accept(this.value);
    }
}