package com.simibubi.create.foundation.utility;

import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class LangBuilder {

    String namespace;

    MutableComponent component;

    public LangBuilder(String namespace) {
        this.namespace = namespace;
    }

    public LangBuilder space() {
        return this.text(" ");
    }

    public LangBuilder newLine() {
        return this.text("\n");
    }

    public LangBuilder translate(String langKey, Object... args) {
        return this.add(Components.translatable(this.namespace + "." + langKey, Lang.resolveBuilders(args)));
    }

    public LangBuilder text(String literalText) {
        return this.add(Components.literal(literalText));
    }

    public LangBuilder text(ChatFormatting format, String literalText) {
        return this.add(Components.literal(literalText).withStyle(format));
    }

    public LangBuilder text(int color, String literalText) {
        return this.add(Components.literal(literalText).withStyle(s -> s.withColor(color)));
    }

    public LangBuilder add(LangBuilder otherBuilder) {
        return this.add(otherBuilder.component());
    }

    public LangBuilder add(MutableComponent customComponent) {
        this.component = this.component == null ? customComponent : this.component.append(customComponent);
        return this;
    }

    public LangBuilder style(ChatFormatting format) {
        this.assertComponent();
        this.component = this.component.withStyle(format);
        return this;
    }

    public LangBuilder color(int color) {
        this.assertComponent();
        this.component = this.component.withStyle(s -> s.withColor(color));
        return this;
    }

    public MutableComponent component() {
        this.assertComponent();
        return this.component;
    }

    public String string() {
        return this.component().getString();
    }

    public String json() {
        return Component.Serializer.toJson(this.component());
    }

    public void sendStatus(Player player) {
        player.displayClientMessage(this.component(), true);
    }

    public void sendChat(Player player) {
        player.displayClientMessage(this.component(), false);
    }

    public void addTo(List<? super MutableComponent> tooltip) {
        tooltip.add(this.component());
    }

    public void forGoggles(List<? super MutableComponent> tooltip) {
        this.forGoggles(tooltip, 0);
    }

    public void forGoggles(List<? super MutableComponent> tooltip, int indents) {
        tooltip.add(Lang.builder().text(Strings.repeat(' ', 4 + indents)).add(this).component());
    }

    private void assertComponent() {
        if (this.component == null) {
            throw new IllegalStateException("No components were added to builder");
        }
    }
}