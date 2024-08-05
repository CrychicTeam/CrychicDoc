package fr.frinn.custommachinery.impl.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;

public class TextComponentUtils {

    public static final NamedCodec<TextColor> COLOR_CODEC = NamedCodec.STRING.comapFlatMap(encoded -> {
        TextColor color = TextColor.parseColor(encoded);
        return color != null ? DataResult.success(color) : DataResult.error(() -> "Invalid color: " + encoded);
    }, TextColor::m_131274_, "Text color");

    public static final NamedMapCodec<Style> STYLE_CODEC = NamedCodec.record(styleInstance -> styleInstance.group(NamedCodec.BOOL.optionalFieldOf("bold", false).forGetter(Style::m_131154_), NamedCodec.BOOL.optionalFieldOf("italic", false).forGetter(Style::m_131161_), NamedCodec.BOOL.optionalFieldOf("underlined", false).forGetter(Style::m_131171_), NamedCodec.BOOL.optionalFieldOf("strikethrough", false).forGetter(Style::m_131168_), NamedCodec.BOOL.optionalFieldOf("obfuscated", false).forGetter(Style::m_131176_), COLOR_CODEC.optionalFieldOf("color").forGetter(style -> Optional.ofNullable(style.getColor())), DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("font", new ResourceLocation("default")).forGetter(Style::m_131192_)).apply(styleInstance, (bold, italic, underlined, strikethrough, obfuscated, color, font) -> Style.EMPTY.withBold(bold).withItalic(italic).withUnderlined(underlined).withStrikethrough(strikethrough).withObfuscated(obfuscated).withColor((TextColor) color.orElse(null)).withFont(font)), "Style");

    public static final NamedCodec<Component> TEXT_COMPONENT_CODEC = NamedCodec.record(iTextComponentInstance -> iTextComponentInstance.group(NamedCodec.STRING.fieldOf("text").forGetter(TextComponentUtils::getString), STYLE_CODEC.forGetter(Component::m_7383_), NamedCodec.lazy(TextComponentUtils::getCodec, "Text component").listOf().optionalFieldOf("childrens", Collections.emptyList()).forGetter(Component::m_7360_)).apply(iTextComponentInstance, (text, style, childrens) -> {
        MutableComponent component = Component.translatable(text);
        component.setStyle(style);
        childrens.forEach(component::m_7220_);
        return component;
    }), "Text component");

    public static final NamedCodec<Component> CODEC = NamedCodec.either(TEXT_COMPONENT_CODEC, NamedCodec.STRING).xmap(either -> (Component) either.map(Function.identity(), Component::m_237115_), Either::left, "Text component");

    public static String toJsonString(Component component) {
        DataResult<JsonElement> result = TEXT_COMPONENT_CODEC.encodeStart(JsonOps.INSTANCE, component);
        return (String) result.result().map(JsonElement::toString).orElse("");
    }

    public static Component fromJsonString(String jsonString) {
        JsonElement json = JsonParser.parseString(jsonString);
        return (Component) TEXT_COMPONENT_CODEC.decode(JsonOps.INSTANCE, json).result().map(Pair::getFirst).orElse(Component.empty());
    }

    private static NamedCodec<Component> getCodec() {
        return TEXT_COMPONENT_CODEC;
    }

    private static String getString(Component component) {
        ComponentContents contents = component.getContents();
        if (contents instanceof LiteralContents literal) {
            return literal.text();
        } else {
            return contents instanceof TranslatableContents translatable ? translatable.getKey() : component.getString();
        }
    }
}