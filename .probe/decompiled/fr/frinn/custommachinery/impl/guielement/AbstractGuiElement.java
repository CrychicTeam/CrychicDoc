package fr.frinn.custommachinery.impl.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import fr.frinn.custommachinery.impl.util.TextComponentUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGuiElement implements IGuiElement {

    private final AbstractGuiElement.Properties properties;

    public AbstractGuiElement(AbstractGuiElement.Properties properties) {
        this.properties = properties;
    }

    @Override
    public AbstractGuiElement.Properties getProperties() {
        return this.properties;
    }

    @Override
    public int getX() {
        return this.properties.x();
    }

    @Override
    public int getY() {
        return this.properties.y();
    }

    @Override
    public int getWidth() {
        return this.properties.width();
    }

    @Override
    public int getHeight() {
        return this.properties.height();
    }

    @Override
    public int getPriority() {
        return this.properties.priority();
    }

    @Override
    public List<Component> getTooltips() {
        return this.properties.tooltips();
    }

    @Override
    public String getId() {
        return this.properties.id();
    }

    public static NamedMapCodec<AbstractGuiElement.Properties> makePropertiesCodec() {
        return makePropertiesCodec(null, null, Collections.emptyList());
    }

    public static NamedMapCodec<AbstractGuiElement.Properties> makePropertiesCodec(@Nullable ResourceLocation defaultTexture) {
        return makePropertiesCodec(defaultTexture, null, Collections.emptyList());
    }

    public static NamedMapCodec<AbstractGuiElement.Properties> makePropertiesCodec(@Nullable ResourceLocation defaultTexture, @Nullable ResourceLocation defaultTextureHovered) {
        return makePropertiesCodec(defaultTexture, defaultTextureHovered, Collections.emptyList());
    }

    public static NamedMapCodec<AbstractGuiElement.Properties> makePropertiesCodec(@Nullable ResourceLocation defaultTexture, @Nullable ResourceLocation defaultTextureHovered, @NotNull List<Component> defaultTooltips) {
        return NamedCodec.record(propertiesInstance -> propertiesInstance.group(NamedCodec.intRange(0, Integer.MAX_VALUE).fieldOf("x").forGetter(AbstractGuiElement.Properties::x), NamedCodec.intRange(0, Integer.MAX_VALUE).fieldOf("y").forGetter(AbstractGuiElement.Properties::y), NamedCodec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("width", -1).forGetter(AbstractGuiElement.Properties::width), NamedCodec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("height", -1).forGetter(AbstractGuiElement.Properties::height), NamedCodec.INT.optionalFieldOf("priority", 0).forGetter(AbstractGuiElement.Properties::priority), DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("texture").forGetter(properties -> defaultTexture != null && defaultTexture.equals(properties.texture) ? Optional.empty() : Optional.ofNullable(properties.texture())), DefaultCodecs.RESOURCE_LOCATION.optionalFieldOf("texture_hovered").forGetter(properties -> defaultTextureHovered != null && defaultTextureHovered.equals(properties.textureHovered) ? Optional.empty() : Optional.ofNullable(properties.textureHovered())), TextComponentUtils.CODEC.listOf().optionalFieldOf("tooltips", defaultTooltips).forGetter(AbstractGuiElement.Properties::tooltips), NamedCodec.STRING.optionalFieldOf("id", "").forGetter(AbstractGuiElement.Properties::id)).apply(propertiesInstance, (x, y, width, height, priority, texture, textureHovered, tooltips, id) -> new AbstractGuiElement.Properties(x, y, width, height, priority, (ResourceLocation) texture.orElse(defaultTexture), (ResourceLocation) textureHovered.orElse(defaultTextureHovered), tooltips, id)), "Gui element properties");
    }

    public static record Properties(int x, int y, int width, int height, int priority, @Nullable ResourceLocation texture, @Nullable ResourceLocation textureHovered, List<Component> tooltips, String id) {
    }
}