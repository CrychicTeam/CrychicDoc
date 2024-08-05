package fr.frinn.custommachinery.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import fr.frinn.custommachinery.impl.util.TextureSizeHelper;
import java.util.Collections;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BackgroundGuiElement extends AbstractGuiElement {

    public static final ResourceLocation BASE_BACKGROUND = new ResourceLocation("custommachinery", "textures/gui/base_background.png");

    public static final NamedCodec<BackgroundGuiElement> CODEC = NamedCodec.record(sizeGuiElement -> sizeGuiElement.group(DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("texture", BASE_BACKGROUND).forGetter(BackgroundGuiElement::getTexture), NamedCodec.intRange(1, 3840).optionalFieldOf("width", -1).forGetter(BackgroundGuiElement::getWidth), NamedCodec.intRange(1, 2160).optionalFieldOf("height", -1).forGetter(BackgroundGuiElement::getHeight)).apply(sizeGuiElement, BackgroundGuiElement::new), "Size gui element");

    public BackgroundGuiElement(ResourceLocation texture, int width, int height) {
        super(new AbstractGuiElement.Properties(0, 0, width, height, 0, texture, null, Collections.emptyList(), ""));
    }

    @Nullable
    public ResourceLocation getTexture() {
        return this.getProperties().texture();
    }

    @Override
    public int getWidth() {
        if (super.getWidth() > 0) {
            return super.getWidth();
        } else {
            int width = TextureSizeHelper.getTextureWidth(this.getTexture());
            return width == 0 ? 256 : width;
        }
    }

    @Override
    public int getHeight() {
        if (super.getHeight() > 0) {
            return super.getHeight();
        } else {
            int height = TextureSizeHelper.getTextureHeight(this.getTexture());
            return height == 0 ? 256 : height;
        }
    }

    @Override
    public GuiElementType<BackgroundGuiElement> getType() {
        return (GuiElementType<BackgroundGuiElement>) Registration.BACKGROUND_GUI_ELEMENT.get();
    }
}