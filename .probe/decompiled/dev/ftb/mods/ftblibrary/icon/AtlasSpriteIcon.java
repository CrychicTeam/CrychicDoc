package dev.ftb.mods.ftblibrary.icon;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class AtlasSpriteIcon extends Icon implements IResourceIcon {

    private final ResourceLocation id;

    private final Color4I color;

    AtlasSpriteIcon(ResourceLocation id) {
        this(id, Color4I.WHITE);
    }

    AtlasSpriteIcon(ResourceLocation id, Color4I color) {
        this.id = id;
        this.color = color;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(this.id);
        if (sprite != null) {
            Matrix4f m = graphics.pose().last().pose();
            int r = this.color.redi();
            int g = this.color.greeni();
            int b = this.color.bluei();
            int a = this.color.alphai();
            float minU = sprite.getU0();
            float minV = sprite.getV0();
            float maxU = sprite.getU1();
            float maxV = sprite.getV1();
            RenderSystem.setShader(GameRenderer::m_172814_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            buffer.m_252986_(m, (float) x, (float) y, 0.0F).color(r, g, b, a).uv(minU, minV).endVertex();
            buffer.m_252986_(m, (float) x, (float) (y + h), 0.0F).color(r, g, b, a).uv(minU, maxV).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) (y + h), 0.0F).color(r, g, b, a).uv(maxU, maxV).endVertex();
            buffer.m_252986_(m, (float) (x + w), (float) y, 0.0F).color(r, g, b, a).uv(maxU, minV).endVertex();
            BufferUploader.drawWithShader(buffer.end());
        }
    }

    public String toString() {
        return this.id.toString();
    }

    @Override
    public boolean hasPixelBuffer() {
        return true;
    }

    @Nullable
    @Override
    public PixelBuffer createPixelBuffer() {
        try {
            return PixelBuffer.from(((Resource) Minecraft.getInstance().getResourceManager().m_213713_(new ResourceLocation(this.id.getNamespace(), "textures/" + this.id.getPath() + ".png")).orElseThrow()).open());
        } catch (Exception var2) {
            return null;
        }
    }

    public AtlasSpriteIcon copy() {
        return new AtlasSpriteIcon(this.id);
    }

    public AtlasSpriteIcon withColor(Color4I color) {
        return new AtlasSpriteIcon(this.id, color);
    }

    public AtlasSpriteIcon withTint(Color4I c) {
        return this.withColor(this.color.withTint(c));
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return this.getId();
    }
}