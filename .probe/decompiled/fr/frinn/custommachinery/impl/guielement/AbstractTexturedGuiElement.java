package fr.frinn.custommachinery.impl.guielement;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import fr.frinn.custommachinery.impl.util.TextureSizeHelper;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTexturedGuiElement extends AbstractGuiElement {

    private final ResourceLocation texture;

    public AbstractTexturedGuiElement(AbstractGuiElement.Properties properties) {
        super(properties);
        if (properties.texture() == null) {
            throw new IllegalArgumentException("Can't make a TexturedGuiElement without texture");
        } else {
            this.texture = properties.texture();
        }
    }

    public AbstractTexturedGuiElement(AbstractGuiElement.Properties properties, ResourceLocation defaultTexture) {
        super(properties);
        this.texture = defaultTexture;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public ResourceLocation getTextureHovered() {
        return this.getProperties().textureHovered();
    }

    @Override
    public int getWidth() {
        if (super.getWidth() >= 0) {
            return super.getWidth();
        } else {
            return Platform.getEnvironment() == Env.CLIENT ? TextureSizeHelper.getTextureWidth(this.getTexture()) : -1;
        }
    }

    @Override
    public int getHeight() {
        if (super.getHeight() >= 0) {
            return super.getHeight();
        } else {
            return Platform.getEnvironment() == Env.CLIENT ? TextureSizeHelper.getTextureHeight(this.getTexture()) : -1;
        }
    }
}