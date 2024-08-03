package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.SheetGlyphInfo;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.Dumpable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class FontTexture extends AbstractTexture implements Dumpable {

    private static final int SIZE = 256;

    private final GlyphRenderTypes renderTypes;

    private final boolean colored;

    private final FontTexture.Node root;

    public FontTexture(GlyphRenderTypes glyphRenderTypes0, boolean boolean1) {
        this.colored = boolean1;
        this.root = new FontTexture.Node(0, 0, 256, 256);
        TextureUtil.prepareImage(boolean1 ? NativeImage.InternalGlFormat.RGBA : NativeImage.InternalGlFormat.RED, this.m_117963_(), 256, 256);
        this.renderTypes = glyphRenderTypes0;
    }

    @Override
    public void load(ResourceManager resourceManager0) {
    }

    @Override
    public void close() {
        this.m_117964_();
    }

    @Nullable
    public BakedGlyph add(SheetGlyphInfo sheetGlyphInfo0) {
        if (sheetGlyphInfo0.isColored() != this.colored) {
            return null;
        } else {
            FontTexture.Node $$1 = this.root.insert(sheetGlyphInfo0);
            if ($$1 != null) {
                this.m_117966_();
                sheetGlyphInfo0.upload($$1.x, $$1.y);
                float $$2 = 256.0F;
                float $$3 = 256.0F;
                float $$4 = 0.01F;
                return new BakedGlyph(this.renderTypes, ((float) $$1.x + 0.01F) / 256.0F, ((float) $$1.x - 0.01F + (float) sheetGlyphInfo0.getPixelWidth()) / 256.0F, ((float) $$1.y + 0.01F) / 256.0F, ((float) $$1.y - 0.01F + (float) sheetGlyphInfo0.getPixelHeight()) / 256.0F, sheetGlyphInfo0.getLeft(), sheetGlyphInfo0.getRight(), sheetGlyphInfo0.getUp(), sheetGlyphInfo0.getDown());
            } else {
                return null;
            }
        }
    }

    @Override
    public void dumpContents(ResourceLocation resourceLocation0, Path path1) {
        String $$2 = resourceLocation0.toDebugFileName();
        TextureUtil.writeAsPNG(path1, $$2, this.m_117963_(), 0, 256, 256, p_285145_ -> (p_285145_ & 0xFF000000) == 0 ? -16777216 : p_285145_);
    }

    static class Node {

        final int x;

        final int y;

        private final int width;

        private final int height;

        @Nullable
        private FontTexture.Node left;

        @Nullable
        private FontTexture.Node right;

        private boolean occupied;

        Node(int int0, int int1, int int2, int int3) {
            this.x = int0;
            this.y = int1;
            this.width = int2;
            this.height = int3;
        }

        @Nullable
        FontTexture.Node insert(SheetGlyphInfo sheetGlyphInfo0) {
            if (this.left != null && this.right != null) {
                FontTexture.Node $$1 = this.left.insert(sheetGlyphInfo0);
                if ($$1 == null) {
                    $$1 = this.right.insert(sheetGlyphInfo0);
                }
                return $$1;
            } else if (this.occupied) {
                return null;
            } else {
                int $$2 = sheetGlyphInfo0.getPixelWidth();
                int $$3 = sheetGlyphInfo0.getPixelHeight();
                if ($$2 > this.width || $$3 > this.height) {
                    return null;
                } else if ($$2 == this.width && $$3 == this.height) {
                    this.occupied = true;
                    return this;
                } else {
                    int $$4 = this.width - $$2;
                    int $$5 = this.height - $$3;
                    if ($$4 > $$5) {
                        this.left = new FontTexture.Node(this.x, this.y, $$2, this.height);
                        this.right = new FontTexture.Node(this.x + $$2 + 1, this.y, this.width - $$2 - 1, this.height);
                    } else {
                        this.left = new FontTexture.Node(this.x, this.y, this.width, $$3);
                        this.right = new FontTexture.Node(this.x, this.y + $$3 + 1, this.width, this.height - $$3 - 1);
                    }
                    return this.left.insert(sheetGlyphInfo0);
                }
            }
        }
    }
}