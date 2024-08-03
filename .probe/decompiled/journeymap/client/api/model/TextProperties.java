package journeymap.client.api.model;

import com.google.common.base.MoreObjects;
import java.util.EnumSet;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.Displayable;
import journeymap.client.api.util.UIState;

public class TextProperties {

    protected EnumSet<Context.UI> activeUIs = EnumSet.of(Context.UI.Any);

    protected EnumSet<Context.MapType> activeMapTypes = EnumSet.of(Context.MapType.Any);

    protected float scale = 1.0F;

    protected int color = 16777215;

    protected int backgroundColor = 0;

    protected float opacity = 1.0F;

    protected float backgroundOpacity = 0.5F;

    protected boolean fontShadow = true;

    protected int minZoom = 0;

    protected int maxZoom = 8;

    protected int offsetX = 0;

    protected int offsetY = 0;

    public float getScale() {
        return this.scale;
    }

    public TextProperties setScale(float scale) {
        this.scale = Math.max(1.0F, Math.min(scale, 8.0F));
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public TextProperties setColor(int color) {
        this.color = Displayable.clampRGB(color);
        return this;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public TextProperties setBackgroundColor(int backgroundColor) {
        this.backgroundColor = Displayable.clampRGB(backgroundColor);
        return this;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public TextProperties setOpacity(float opacity) {
        this.opacity = Displayable.clampOpacity(opacity);
        return this;
    }

    public float getBackgroundOpacity() {
        return this.backgroundOpacity;
    }

    public TextProperties setBackgroundOpacity(float backgroundOpacity) {
        this.backgroundOpacity = Displayable.clampOpacity(backgroundOpacity);
        return this;
    }

    public boolean hasFontShadow() {
        return this.fontShadow;
    }

    public TextProperties setFontShadow(boolean fontShadow) {
        this.fontShadow = fontShadow;
        return this;
    }

    public EnumSet<Context.UI> getActiveUIs() {
        return this.activeUIs;
    }

    public TextProperties setActiveUIs(EnumSet<Context.UI> activeUIs) {
        if (activeUIs.contains(Context.UI.Any)) {
            activeUIs = EnumSet.of(Context.UI.Any);
        }
        this.activeUIs = activeUIs;
        return this;
    }

    public EnumSet<Context.MapType> getActiveMapTypes() {
        return this.activeMapTypes;
    }

    public TextProperties setActiveMapTypes(EnumSet<Context.MapType> activeMapTypes) {
        if (activeMapTypes.contains(Context.MapType.Any)) {
            activeMapTypes = EnumSet.of(Context.MapType.Any);
        }
        this.activeMapTypes = activeMapTypes;
        return this;
    }

    public boolean isActiveIn(UIState uiState) {
        return uiState.active && (this.activeUIs.contains(Context.UI.Any) || this.activeUIs.contains(uiState.ui)) && (this.activeMapTypes.contains(Context.MapType.Any) || this.activeMapTypes.contains(uiState.mapType)) && this.minZoom <= uiState.zoom && this.maxZoom >= uiState.zoom;
    }

    public int getMinZoom() {
        return this.minZoom;
    }

    public TextProperties setMinZoom(int minZoom) {
        this.minZoom = Math.max(0, minZoom);
        return this;
    }

    public int getMaxZoom() {
        return this.maxZoom;
    }

    public TextProperties setMaxZoom(int maxZoom) {
        this.maxZoom = Math.min(8, maxZoom);
        return this;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public TextProperties setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public TextProperties setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("activeMapTypes", this.activeMapTypes).add("activeUIs", this.activeUIs).add("backgroundColor", this.backgroundColor).add("backgroundOpacity", this.backgroundOpacity).add("color", this.color).add("opacity", this.opacity).add("fontShadow", this.fontShadow).add("maxZoom", this.maxZoom).add("minZoom", this.minZoom).add("offsetX", this.offsetX).add("offsetY", this.offsetY).add("scale", this.scale).toString();
    }
}