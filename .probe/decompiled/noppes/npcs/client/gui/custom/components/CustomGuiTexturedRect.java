package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import org.joml.Matrix4f;

public class CustomGuiTexturedRect extends AbstractWidget implements IGuiComponent {

    private CustomGuiTexturedRectWrapper component = null;

    GuiCustom parent;

    ResourceLocation texture;

    public int id;

    public int x;

    public int y;

    public int width;

    public int height;

    public int textureX;

    public int textureY;

    float scale = 1.0F;

    List<Component> hoverText;

    public boolean hasRepeatingTexture = false;

    public int texRepWidth;

    public int texRepHeight;

    public int texRepBorderSize = 0;

    public CustomGuiTexturedRect(GuiCustom parent, CustomGuiTexturedRectWrapper component) {
        super(component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), Component.empty());
        this.component = component;
        this.parent = parent;
        this.init();
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.texture = new ResourceLocation(this.component.getTexture());
        this.x = this.component.getPosX();
        this.y = this.component.getPosY();
        this.width = this.component.getWidth();
        this.height = this.component.getHeight();
        this.textureX = this.component.getTextureX();
        this.textureY = this.component.getTextureY();
        this.scale = this.component.getScale();
        this.hasRepeatingTexture = this.component.hasRepeatingTexture;
        this.texRepWidth = this.component.texRepWidth;
        this.texRepHeight = this.component.texRepHeight;
        this.texRepBorderSize = this.component.texRepBorderSize;
        if (this.component.hasHoverText()) {
            this.hoverText = this.component.getHoverTextList();
        }
    }

    public CustomGuiTexturedRect setRep(int texRepWidth, int texRepHeight, int texRepBorderSize) {
        this.texRepWidth = texRepWidth;
        this.texRepHeight = texRepHeight;
        this.texRepBorderSize = texRepBorderSize;
        this.hasRepeatingTexture = true;
        return this;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.component.getTexture().isEmpty() && this.component.getVisible()) {
            boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            graphics.pose().pushPose();
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.texture);
            Matrix4f m = graphics.pose().last().pose();
            if (!this.hasRepeatingTexture) {
                this.draw(m, (float) this.x, (float) this.y, (float) this.textureX, (float) this.textureY, (float) this.width, (float) this.height);
            } else {
                if (this.texRepBorderSize > 0) {
                    this.draw(m, (float) this.x, (float) this.y, (float) this.textureX, (float) this.textureY, (float) this.texRepBorderSize, (float) this.texRepBorderSize);
                    this.draw(m, (float) (this.x + this.width - this.texRepBorderSize), (float) this.y, (float) (this.textureX + this.texRepWidth - this.texRepBorderSize), (float) this.textureY, (float) this.texRepBorderSize, (float) this.texRepBorderSize);
                    this.draw(m, (float) this.x, (float) (this.y + this.height - this.texRepBorderSize), (float) this.textureX, (float) (this.textureY + this.texRepHeight - this.texRepBorderSize), (float) this.texRepBorderSize, (float) this.texRepBorderSize);
                    this.draw(m, (float) (this.x + this.width - this.texRepBorderSize), (float) (this.y + this.height - this.texRepBorderSize), (float) (this.textureX + this.texRepWidth - this.texRepBorderSize), (float) (this.textureY + this.texRepHeight - this.texRepBorderSize), (float) this.texRepBorderSize, (float) this.texRepBorderSize);
                }
                float w = (float) this.width - (float) this.texRepBorderSize * 2.0F;
                float h = (float) this.height - (float) this.texRepBorderSize * 2.0F;
                float tw = (float) this.texRepWidth - (float) this.texRepBorderSize * 2.0F;
                float th = (float) this.texRepHeight - (float) this.texRepBorderSize * 2.0F;
                float mx = w / tw;
                float my = h / th;
                for (int i = 0; (float) i < my; i++) {
                    float dh = th * Math.min(1.0F, my - (float) i);
                    this.draw(m, (float) this.x, (float) (this.y + this.texRepBorderSize) + th * (float) i, (float) this.textureX, (float) (this.textureY + this.texRepBorderSize), (float) this.texRepBorderSize, dh);
                    this.draw(m, (float) (this.x + this.width - this.texRepBorderSize), (float) (this.y + this.texRepBorderSize) + th * (float) i, (float) (this.textureX + this.texRepWidth - this.texRepBorderSize), (float) (this.textureY + this.texRepBorderSize), (float) this.texRepBorderSize, dh);
                    for (int j = 0; (float) j < mx; j++) {
                        float dw = tw * Math.min(1.0F, mx - (float) j);
                        this.draw(m, (float) (this.x + this.texRepBorderSize) + tw * (float) j, (float) this.y, (float) (this.textureX + this.texRepBorderSize), (float) this.textureY, dw, (float) this.texRepBorderSize);
                        this.draw(m, (float) (this.x + this.texRepBorderSize) + tw * (float) j, (float) (this.y + this.height - this.texRepBorderSize), (float) (this.textureX + this.texRepBorderSize), (float) (this.textureY + this.texRepHeight - this.texRepBorderSize), dw, (float) this.texRepBorderSize);
                        this.draw(m, (float) (this.x + this.texRepBorderSize) + tw * (float) j, (float) (this.y + this.texRepBorderSize) + th * (float) i, (float) (this.textureX + this.texRepBorderSize), (float) (this.textureY + this.texRepBorderSize), dw, dh);
                    }
                }
            }
            if (hovered && this.hoverText != null && this.hoverText.size() > 0) {
                this.parent.hoverText = this.hoverText;
            }
            graphics.pose().popPose();
        }
    }

    private void draw(Matrix4f m, float x, float y, float texX, float texY, float width, float height) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        int blitLevel = Math.max(0, this.id);
        bufferbuilder.m_252986_(m, x, y + height * this.scale, (float) blitLevel).uv(texX * 0.00390625F, (texY + height) * 0.00390625F).endVertex();
        bufferbuilder.m_252986_(m, x + width * this.scale, y + height * this.scale, (float) blitLevel).uv((texX + width) * 0.00390625F, (texY + height) * 0.00390625F).endVertex();
        bufferbuilder.m_252986_(m, x + width * this.scale, y, (float) blitLevel).uv((texX + width) * 0.00390625F, texY * 0.00390625F).endVertex();
        bufferbuilder.m_252986_(m, x, y, (float) blitLevel).uv(texX * 0.00390625F, texY * 0.00390625F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }
}