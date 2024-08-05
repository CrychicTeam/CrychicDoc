package fr.frinn.custommachinery.common.guielement;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.client.ClientHandler;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import fr.frinn.custommachinery.impl.util.TextComponentUtils;
import java.util.Locale;
import net.minecraft.network.chat.Component;

public class TextGuiElement extends AbstractGuiElement {

    public static final NamedCodec<TextGuiElement> CODEC = NamedCodec.record(textGuiElementCodec -> textGuiElementCodec.group(makePropertiesCodec().forGetter(AbstractGuiElement::getProperties), TextComponentUtils.CODEC.fieldOf("text").forGetter(TextGuiElement::getText), NamedCodec.enumCodec(TextGuiElement.Alignment.class).optionalFieldOf("alignment", TextGuiElement.Alignment.LEFT).forGetter(TextGuiElement::getAlignment), NamedCodec.BOOL.optionalFieldOf("jei", false).forGetter(IGuiElement::showInJei)).apply(textGuiElementCodec, TextGuiElement::new), "Text gui element");

    private final Component text;

    private final TextGuiElement.Alignment alignment;

    private final boolean jei;

    public TextGuiElement(AbstractGuiElement.Properties properties, Component text, TextGuiElement.Alignment alignment, boolean jei) {
        super(properties);
        this.text = text;
        this.alignment = alignment;
        this.jei = jei;
    }

    @Override
    public GuiElementType<TextGuiElement> getType() {
        return (GuiElementType<TextGuiElement>) Registration.TEXT_GUI_ELEMENT.get();
    }

    @Override
    public int getWidth() {
        if (this.getProperties().width() != -1) {
            return this.getProperties().width();
        } else {
            return Platform.getEnvironment() == Env.CLIENT ? ClientHandler.textWidth(this.text) : -1;
        }
    }

    @Override
    public int getHeight() {
        if (this.getProperties().height() != -1) {
            return this.getProperties().height();
        } else {
            return Platform.getEnvironment() == Env.CLIENT ? ClientHandler.getLineHeight() : -1;
        }
    }

    public Component getText() {
        return this.text;
    }

    public TextGuiElement.Alignment getAlignment() {
        return this.alignment;
    }

    @Override
    public boolean showInJei() {
        return this.jei;
    }

    public static enum Alignment {

        LEFT, CENTER, RIGHT;

        public static TextGuiElement.Alignment value(String value) {
            return valueOf(value.toUpperCase(Locale.ENGLISH));
        }

        public String toString() {
            return super.toString().toLowerCase(Locale.ENGLISH);
        }
    }
}