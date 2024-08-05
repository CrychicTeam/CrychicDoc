package fr.frinn.custommachinery.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractTexturedGuiElement;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigGuiElement extends AbstractTexturedGuiElement {

    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation("custommachinery", "textures/gui/base_config.png");

    public static final ResourceLocation BASE_TEXTURE_HOVERED = new ResourceLocation("custommachinery", "textures/gui/base_config_hovered.png");

    public static final List<Component> BASE_TOOLTIPS = Collections.singletonList(Component.translatable("custommachinery.gui.element.config.name"));

    public static final NamedCodec<ConfigGuiElement> CODEC = NamedCodec.record(configGuiElement -> configGuiElement.group(makePropertiesCodec(BASE_TEXTURE, BASE_TEXTURE_HOVERED, BASE_TOOLTIPS).forGetter(AbstractGuiElement::getProperties)).apply(configGuiElement, ConfigGuiElement::new), "Config gui element");

    public ConfigGuiElement(AbstractGuiElement.Properties properties) {
        super(properties);
    }

    @Override
    public GuiElementType<ConfigGuiElement> getType() {
        return (GuiElementType<ConfigGuiElement>) Registration.CONFIG_GUI_ELEMENT.get();
    }
}