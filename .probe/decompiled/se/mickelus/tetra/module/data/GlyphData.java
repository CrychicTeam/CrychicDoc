package se.mickelus.tetra.module.data;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GlyphData {

    public int tint = -1;

    public int textureX = 0;

    public int textureY = 0;

    public ResourceLocation textureLocation = GuiTextures.glyphs;

    public GlyphData() {
    }

    public GlyphData(int textureX, int textureY) {
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public GlyphData(ResourceLocation textureLocation, int textureX, int textureY) {
        this.textureLocation = textureLocation;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public GlyphData(String texture, int textureX, int textureY) {
        this(new ResourceLocation("tetra", texture), textureX, textureY);
    }

    public GlyphData(ResourceLocation textureLocation, int textureX, int textureY, int tint) {
        this(textureLocation, textureX, textureY);
        this.tint = tint;
    }

    public boolean equals(Object obj) {
        return obj instanceof GlyphData && this.textureX == ((GlyphData) obj).textureX && this.textureY == ((GlyphData) obj).textureY && this.tint == ((GlyphData) obj).tint && this.textureLocation.equals(((GlyphData) obj).textureLocation);
    }
}