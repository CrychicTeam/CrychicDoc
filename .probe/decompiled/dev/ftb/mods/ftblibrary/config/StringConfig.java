package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class StringConfig extends ConfigFromString<String> {

    public static final Color4I COLOR = Color4I.rgb(16755273);

    public final Pattern pattern;

    public StringConfig(@Nullable Pattern p) {
        this.pattern = p;
        this.defaultValue = "";
        this.value = "";
    }

    public StringConfig() {
        this(null);
    }

    public Color4I getColor(@Nullable String v) {
        return COLOR;
    }

    @Override
    public boolean parse(@Nullable Consumer<String> callback, String string) {
        return (this.pattern == null || this.pattern.matcher(string).matches()) && this.okValue(callback, string);
    }

    public Component getStringForGUI(@Nullable String v) {
        return (Component) (v == null ? NULL_TEXT : Component.literal("\"" + v + "\""));
    }

    @Override
    public void addInfo(TooltipList list) {
        if (this.value != null && !this.value.equals(this.defaultValue)) {
            list.add(Component.translatable("config.group.value").append(": ").withStyle(ChatFormatting.AQUA).append(Component.literal("\"" + this.value + "\"").withStyle(ChatFormatting.WHITE)));
        }
        super.addInfo(list);
        if (this.pattern != null) {
            list.add(info("Regex", this.pattern.pattern()));
        }
    }
}