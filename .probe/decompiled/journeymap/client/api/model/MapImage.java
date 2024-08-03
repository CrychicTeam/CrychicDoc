package journeymap.client.api.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.Since;
import com.mojang.blaze3d.platform.NativeImage;
import javax.annotation.Nullable;
import journeymap.client.api.display.Displayable;
import net.minecraft.resources.ResourceLocation;

public final class MapImage {

    @Since(1.1)
    private transient NativeImage image;

    @Since(1.1)
    private ResourceLocation imageLocation;

    @Since(1.1)
    private Integer color = 16777215;

    @Since(1.1)
    private Float opacity = 1.0F;

    @Since(1.1)
    private Integer textureX = 0;

    @Since(1.1)
    private Integer textureY = 0;

    @Since(1.1)
    private Integer textureWidth;

    @Since(1.1)
    private Integer textureHeight;

    @Since(1.1)
    private Integer rotation = 0;

    @Since(1.1)
    private Double displayWidth;

    @Since(1.1)
    private Double displayHeight;

    @Since(1.1)
    private Double anchorX;

    @Since(1.1)
    private Double anchorY;

    public MapImage(NativeImage image) {
        this(image, 0, 0, image.getWidth(), image.getHeight(), 16777215, 1.0F);
    }

    public MapImage(NativeImage image, int textureX, int textureY, int textureWidth, int textureHeight, int color, float opacity) {
        this.image = image;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = Math.max(1, textureWidth);
        this.textureHeight = Math.max(1, textureHeight);
        this.setDisplayWidth((double) this.textureWidth.intValue());
        this.setDisplayHeight((double) this.textureHeight.intValue());
        this.setColor(color);
        this.setOpacity(opacity);
    }

    public MapImage(ResourceLocation imageLocation, int textureWidth, int textureHeight) {
        this(imageLocation, 0, 0, textureWidth, textureHeight, 16777215, 1.0F);
    }

    public MapImage(ResourceLocation imageLocation, int textureX, int textureY, int textureWidth, int textureHeight, int color, float opacity) {
        this.imageLocation = imageLocation;
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureWidth = Math.max(1, textureWidth);
        this.textureHeight = Math.max(1, textureHeight);
        this.setDisplayWidth((double) this.textureWidth.intValue());
        this.setDisplayHeight((double) this.textureHeight.intValue());
        this.setColor(color);
        this.setOpacity(opacity);
    }

    public int getColor() {
        return this.color;
    }

    public MapImage setColor(int color) {
        this.color = Displayable.clampRGB(color);
        return this;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public MapImage setOpacity(float opacity) {
        this.opacity = Displayable.clampOpacity(opacity);
        return this;
    }

    public int getTextureX() {
        return this.textureX;
    }

    public int getTextureY() {
        return this.textureY;
    }

    public double getAnchorX() {
        return this.anchorX;
    }

    public MapImage setAnchorX(double anchorX) {
        this.anchorX = anchorX;
        return this;
    }

    public double getAnchorY() {
        return this.anchorY;
    }

    public MapImage setAnchorY(double anchorY) {
        this.anchorY = anchorY;
        return this;
    }

    public MapImage centerAnchors() {
        this.setAnchorX(this.displayWidth / 2.0);
        this.setAnchorY(this.displayHeight / 2.0);
        return this;
    }

    public int getTextureWidth() {
        return this.textureWidth;
    }

    public int getTextureHeight() {
        return this.textureHeight;
    }

    @Nullable
    public ResourceLocation getImageLocation() {
        return this.imageLocation;
    }

    @Nullable
    public NativeImage getImage() {
        return this.image;
    }

    public int getRotation() {
        return this.rotation;
    }

    public MapImage setRotation(int rotation) {
        this.rotation = rotation % 360;
        return this;
    }

    public double getDisplayWidth() {
        return this.displayWidth;
    }

    public MapImage setDisplayWidth(double displayWidth) {
        this.displayWidth = displayWidth;
        return this;
    }

    public double getDisplayHeight() {
        return this.displayHeight;
    }

    public MapImage setDisplayHeight(double displayHeight) {
        this.displayHeight = displayHeight;
        return this;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MapImage mapImage = (MapImage) o;
            return Objects.equal(this.color, mapImage.color) && Objects.equal(this.opacity, mapImage.opacity) && Objects.equal(this.anchorX, mapImage.anchorX) && Objects.equal(this.anchorY, mapImage.anchorY) && Objects.equal(this.textureX, mapImage.textureX) && Objects.equal(this.textureY, mapImage.textureY) && Objects.equal(this.textureWidth, mapImage.textureWidth) && Objects.equal(this.textureHeight, mapImage.textureHeight) && Objects.equal(this.imageLocation, mapImage.imageLocation);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.imageLocation, this.color, this.opacity, this.anchorX, this.anchorY, this.textureX, this.textureY, this.textureWidth, this.textureHeight });
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("imageLocation", this.imageLocation).add("anchorX", this.anchorX).add("anchorY", this.anchorY).add("color", this.color).add("textureHeight", this.textureHeight).add("opacity", this.opacity).add("textureX", this.textureX).add("textureY", this.textureY).add("textureWidth", this.textureWidth).toString();
    }
}