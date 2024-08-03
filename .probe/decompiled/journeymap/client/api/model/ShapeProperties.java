package journeymap.client.api.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.mojang.blaze3d.platform.NativeImage;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import journeymap.client.api.display.Displayable;
import net.minecraft.resources.ResourceLocation;

public class ShapeProperties {

    private int strokeColor = 0;

    private int fillColor = 0;

    private float strokeOpacity = 1.0F;

    private float fillOpacity = 0.5F;

    private float strokeWidth = 2.0F;

    private NativeImage image;

    private ResourceLocation imageLocation;

    private double texturePositionX = 0.0;

    private double texturePositionY = 0.0;

    private double textureScaleX = 1.0;

    private double textureScaleY = 1.0;

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public ShapeProperties setStrokeColor(int strokeColor) {
        this.strokeColor = Displayable.clampRGB(strokeColor);
        return this;
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public ShapeProperties setFillColor(int fillColor) {
        this.fillColor = Displayable.clampRGB(fillColor);
        return this;
    }

    public float getStrokeOpacity() {
        return this.strokeOpacity;
    }

    public ShapeProperties setStrokeOpacity(float strokeOpacity) {
        this.strokeOpacity = Displayable.clampOpacity(strokeOpacity);
        return this;
    }

    public float getFillOpacity() {
        return this.fillOpacity;
    }

    public ShapeProperties setFillOpacity(float fillOpacity) {
        this.fillOpacity = Displayable.clampOpacity(fillOpacity);
        return this;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public ShapeProperties setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    @Nullable
    public NativeImage getImage() {
        return this.image;
    }

    public ShapeProperties setImage(NativeImage image) {
        this.image = image;
        return this;
    }

    @Nullable
    public ResourceLocation getImageLocation() {
        return this.imageLocation;
    }

    public ShapeProperties setImageLocation(ResourceLocation imageLocation) {
        this.imageLocation = imageLocation;
        return this;
    }

    public double getTexturePositionX() {
        return this.texturePositionX;
    }

    public ShapeProperties setTexturePositionX(double texturePositionX) {
        this.texturePositionX = texturePositionX;
        return this;
    }

    public double getTexturePositionY() {
        return this.texturePositionY;
    }

    public ShapeProperties setTexturePositionY(double texturePositionY) {
        this.texturePositionY = texturePositionY;
        return this;
    }

    public double getTextureScaleX() {
        return this.textureScaleX;
    }

    public ShapeProperties setTextureScaleX(double textureScaleX) {
        this.textureScaleX = textureScaleX;
        return this;
    }

    public double getTextureScaleY() {
        return this.textureScaleY;
    }

    public ShapeProperties setTextureScaleY(double textureScaleY) {
        this.textureScaleY = textureScaleY;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof ShapeProperties that) ? false : Objects.equal(this.strokeColor, that.strokeColor) && Objects.equal(this.fillColor, that.fillColor) && Objects.equal(this.strokeOpacity, that.strokeOpacity) && Objects.equal(this.fillOpacity, that.fillOpacity) && Objects.equal(this.strokeWidth, that.strokeWidth) && Objects.equal(this.imageLocation, that.imageLocation) && Objects.equal(this.texturePositionX, that.texturePositionX) && Objects.equal(this.texturePositionY, that.texturePositionY) && Objects.equal(this.textureScaleX, that.textureScaleX) && Objects.equal(this.textureScaleY, that.textureScaleY);
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.strokeColor, this.fillColor, this.strokeOpacity, this.fillOpacity, this.strokeWidth, this.imageLocation, this.texturePositionX, this.texturePositionY, this.textureScaleX, this.textureScaleY });
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("fillColor", this.fillColor).add("fillOpacity", this.fillOpacity).add("strokeColor", this.strokeColor).add("strokeOpacity", this.strokeOpacity).add("strokeWidth", this.strokeWidth).add("imageLocation", this.imageLocation).add("texturePositionX", this.texturePositionX).add("texturePositionY", this.texturePositionY).add("textureScaleX", this.textureScaleX).add("textureScaleY", this.textureScaleY).toString();
    }
}