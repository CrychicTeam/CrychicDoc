package dev.latvian.mods.kubejs.core;

import com.google.common.collect.Iterators;
import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.bindings.TextWrapper;
import dev.latvian.mods.kubejs.util.WrappedJS;
import dev.latvian.mods.rhino.mod.util.JsonSerializable;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ComponentKJS extends Component, JsonSerializable, WrappedJS {

    default Iterable<Component> kjs$asIterable() {
        return new Iterable<Component>() {

            @NotNull
            public Iterator<Component> iterator() {
                if (!ComponentKJS.this.kjs$hasSiblings()) {
                    return Iterators.forArray(new Component[] { ComponentKJS.this.kjs$self() });
                } else {
                    List<Component> list = new LinkedList();
                    list.add(ComponentKJS.this.kjs$self());
                    for (Component child : ComponentKJS.this.m_7360_()) {
                        if (child instanceof ComponentKJS wrapped) {
                            wrapped.forEach(list::add);
                        } else {
                            list.add(child);
                        }
                    }
                    return list.iterator();
                }
            }
        };
    }

    default MutableComponent kjs$self() {
        return (MutableComponent) this;
    }

    @RemapForJS("toJson")
    @Override
    default JsonElement toJsonJS() {
        return Component.Serializer.toJsonTree(this.kjs$self());
    }

    default boolean kjs$hasStyle() {
        return this.m_7383_() != null && !this.m_7383_().isEmpty();
    }

    default boolean kjs$hasSiblings() {
        return !this.m_7360_().isEmpty();
    }

    default void forEach(Consumer<? super Component> action) {
        this.kjs$asIterable().forEach(action);
    }

    default MutableComponent kjs$black() {
        return this.kjs$self().withStyle(ChatFormatting.BLACK);
    }

    default MutableComponent kjs$darkBlue() {
        return this.kjs$self().withStyle(ChatFormatting.DARK_BLUE);
    }

    default MutableComponent kjs$darkGreen() {
        return this.kjs$self().withStyle(ChatFormatting.DARK_GREEN);
    }

    default MutableComponent kjs$darkAqua() {
        return this.kjs$self().withStyle(ChatFormatting.DARK_AQUA);
    }

    default MutableComponent kjs$darkRed() {
        return this.kjs$self().withStyle(ChatFormatting.DARK_RED);
    }

    default MutableComponent kjs$darkPurple() {
        return this.kjs$self().withStyle(ChatFormatting.DARK_PURPLE);
    }

    default MutableComponent kjs$gold() {
        return this.kjs$self().withStyle(ChatFormatting.GOLD);
    }

    default MutableComponent kjs$gray() {
        return this.kjs$self().withStyle(ChatFormatting.GRAY);
    }

    default MutableComponent kjs$darkGray() {
        return this.kjs$self().withStyle(ChatFormatting.DARK_GRAY);
    }

    default MutableComponent kjs$blue() {
        return this.kjs$self().withStyle(ChatFormatting.BLUE);
    }

    default MutableComponent kjs$green() {
        return this.kjs$self().withStyle(ChatFormatting.GREEN);
    }

    default MutableComponent kjs$aqua() {
        return this.kjs$self().withStyle(ChatFormatting.AQUA);
    }

    default MutableComponent kjs$red() {
        return this.kjs$self().withStyle(ChatFormatting.RED);
    }

    default MutableComponent kjs$lightPurple() {
        return this.kjs$self().withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    default MutableComponent kjs$yellow() {
        return this.kjs$self().withStyle(ChatFormatting.YELLOW);
    }

    default MutableComponent kjs$white() {
        return this.kjs$self().withStyle(ChatFormatting.WHITE);
    }

    default MutableComponent kjs$color(@Nullable Color c) {
        TextColor col = c == null ? null : c.createTextColorJS();
        return this.kjs$self().setStyle(this.m_7383_().withColor(col));
    }

    default MutableComponent kjs$noColor() {
        return this.kjs$color(null);
    }

    default MutableComponent kjs$bold(@Nullable Boolean value) {
        return this.kjs$self().setStyle(this.m_7383_().withBold(value));
    }

    default MutableComponent kjs$bold() {
        return this.kjs$bold(true);
    }

    default MutableComponent kjs$italic(@Nullable Boolean value) {
        return this.kjs$self().setStyle(this.m_7383_().withItalic(value));
    }

    default MutableComponent kjs$italic() {
        return this.kjs$italic(true);
    }

    default MutableComponent kjs$underlined(@Nullable Boolean value) {
        return this.kjs$self().setStyle(this.m_7383_().withUnderlined(value));
    }

    default MutableComponent kjs$underlined() {
        return this.kjs$underlined(true);
    }

    default MutableComponent kjs$strikethrough(@Nullable Boolean value) {
        return this.kjs$self().setStyle(this.m_7383_().withStrikethrough(value));
    }

    default MutableComponent kjs$strikethrough() {
        return this.kjs$strikethrough(true);
    }

    default MutableComponent kjs$obfuscated(@Nullable Boolean value) {
        return this.kjs$self().setStyle(this.m_7383_().withObfuscated(value));
    }

    default MutableComponent kjs$obfuscated() {
        return this.kjs$obfuscated(true);
    }

    default MutableComponent kjs$insertion(@Nullable String s) {
        return this.kjs$self().setStyle(this.m_7383_().withInsertion(s));
    }

    default MutableComponent kjs$font(@Nullable ResourceLocation s) {
        return this.kjs$self().setStyle(this.m_7383_().withFont(s));
    }

    default MutableComponent kjs$click(@Nullable ClickEvent s) {
        return this.kjs$self().setStyle(this.m_7383_().withClickEvent(s));
    }

    default MutableComponent kjs$clickRunCommand(String command) {
        return this.kjs$click(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }

    default MutableComponent kjs$clickSuggestCommand(String command) {
        return this.kjs$click(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    }

    default MutableComponent kjs$clickCopy(String text) {
        return this.kjs$click(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text));
    }

    default MutableComponent kjs$clickChangePage(String page) {
        return this.kjs$click(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));
    }

    default MutableComponent kjs$clickOpenUrl(String url) {
        return this.kjs$click(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }

    default MutableComponent kjs$clickOpenFile(String path) {
        return this.kjs$click(new ClickEvent(ClickEvent.Action.OPEN_FILE, path));
    }

    default MutableComponent kjs$hover(@Nullable Component s) {
        return this.kjs$self().setStyle(this.m_7383_().withHoverEvent(s == null ? null : new HoverEvent(HoverEvent.Action.SHOW_TEXT, s)));
    }

    default boolean kjs$isEmpty() {
        return TextWrapper.isEmpty(this.kjs$self());
    }

    @Deprecated(forRemoval = true)
    default MutableComponent kjs$rawComponent() {
        KubeJS.LOGGER.warn("Using rawComponent() is deprecated, since components no longer need to be wrapped to Text! You can safely remove this method.");
        return this.kjs$self();
    }

    @Deprecated(forRemoval = true)
    default MutableComponent kjs$rawCopy() {
        KubeJS.LOGGER.warn("Using rawCopy() is deprecated, since components no longer need to be wrapped to Text! Use copy() instead.");
        return this.m_6881_();
    }

    @Deprecated(forRemoval = true)
    default Component kjs$component() {
        KubeJS.LOGGER.warn("Using component() is deprecated, since components no longer need to be wrapped to Text! You can safely remove this method.");
        return this.kjs$self();
    }
}