package journeymap.client.model;

import java.awt.Color;
import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;
import journeymap.client.cartography.color.RGB;
import journeymap.client.render.RenderWrapper;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class GridSpec {

    public final GridSpec.Style style;

    public final float red;

    public final float green;

    public final float blue;

    public final float alpha;

    private int colorX = -1;

    private int colorY = -1;

    private transient Texture texture = null;

    public GridSpec(GridSpec.Style style, Color color, float alpha) {
        this.style = style;
        float[] rgb = RGB.floats(color.getRGB());
        this.red = rgb[0];
        this.green = rgb[1];
        this.blue = rgb[2];
        if (alpha < 0.0F) {
            alpha = 0.0F;
        }
        while (alpha > 1.0F) {
            alpha /= 100.0F;
        }
        this.alpha = alpha;
        assert alpha <= 1.0F;
    }

    public GridSpec(GridSpec.Style style, float red, float green, float blue, float alpha) {
        this.style = style;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        assert alpha <= 1.0F;
    }

    public GridSpec setColorCoords(int x, int y) {
        this.colorX = x;
        this.colorY = y;
        return this;
    }

    public void beginTexture(int textureFilter, int textureWrap, float mapAlpha) {
        RenderWrapper.enableBlend();
        RenderWrapper.defaultBlendFunc();
        RenderWrapper.setShader(GameRenderer::m_172817_);
        RenderWrapper.activeTexture(33984);
        RenderWrapper.bindTexture(this.getTexture().getTextureId());
        RenderWrapper.setShaderTexture(0, this.getTexture().getTextureId());
        RenderWrapper.setColor4f(this.red, this.green, this.blue, this.alpha * mapAlpha);
        RenderWrapper.texParameter(3553, 10241, textureFilter);
        RenderWrapper.texParameter(3553, 10240, textureFilter);
        RenderWrapper.texParameter(3553, 10242, textureWrap);
        RenderWrapper.texParameter(3553, 10243, textureWrap);
    }

    public Texture getTexture() {
        if (this.texture == null || !this.texture.hasImage()) {
            this.texture = TextureCache.getTexture(this.style.textureLocation);
        }
        return this.texture;
    }

    public GridSpec clone() {
        return new GridSpec(this.style, this.red, this.green, this.blue, this.alpha).setColorCoords(this.colorX, this.colorY);
    }

    public void finishTexture() {
        RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderWrapper.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Integer getColor() {
        return RGB.toInteger(this.red, this.green, this.blue);
    }

    public int getColorX() {
        return this.colorX;
    }

    public int getColorY() {
        return this.colorY;
    }

    public static enum Style implements KeyedEnum {

        Squares("jm.common.grid_style_squares", TextureCache.GridSquares), SquaresWithRegion("jm.common.grid_style_squares_region", TextureCache.GridRegionSquares), GridRegion("jm.common.grid_style_region", TextureCache.GridRegion), Dots("jm.common.grid_style_dots", TextureCache.GridDots), Checkers("jm.common.grid_style_checkers", TextureCache.GridCheckers);

        private final String key;

        private final ResourceLocation textureLocation;

        private Style(String key, ResourceLocation textureLocation) {
            this.key = key;
            this.textureLocation = textureLocation;
        }

        public String displayName() {
            return Constants.getString(this.key);
        }

        @Override
        public String getKey() {
            return this.key;
        }
    }
}