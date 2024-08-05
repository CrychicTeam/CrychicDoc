package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiMenuTopIconButton extends GuiMenuTopButton {

    private static final ResourceLocation resource = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    protected static ItemRenderer itemRenderer;

    private ItemStack item;

    public GuiMenuTopIconButton(IGuiInterface gui, int i, int x, int y, String s, ItemStack item) {
        super(gui, i, x, y, s);
        this.f_93618_ = 28;
        this.height = 28;
        this.item = item;
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    public GuiMenuTopIconButton(IGuiInterface gui, int i, GuiButtonNop parent, String s, ItemStack item) {
        super(gui, i, parent, s);
        this.f_93618_ = 28;
        this.height = 28;
        this.item = item;
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            if (this.item.isEmpty()) {
                this.item = new ItemStack(Blocks.DIRT);
            }
            this.hover = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.m_5711_() && mouseY < this.m_252907_() + this.height;
            Minecraft mc = Minecraft.getInstance();
            if (this.hover) {
                this.drawHoveringText(graphics, Arrays.asList(this.m_6035_()), mouseX, mouseY, Minecraft.getInstance().font);
            }
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, resource);
            graphics.pose().pushPose();
            graphics.blit(resource, this.m_252754_(), this.m_252907_() + (this.active ? 2 : 0), 0, this.active ? 32 : 0, 28, 28);
            graphics.pose().translate(0.0F, 0.0F, 100.0F);
            graphics.renderItem(this.item, this.m_252754_() + 6, this.m_252907_() + 10);
            graphics.renderItemDecorations(mc.font, this.item, this.m_252754_() + 6, this.m_252907_() + 10);
            graphics.pose().popPose();
        }
    }

    protected void drawHoveringText(GuiGraphics graphics, List<Component> list, int x, int y, Font font) {
        if (!list.isEmpty()) {
            RenderSystem.disableDepthTest();
            int k = 0;
            for (Component o : list) {
                int l = font.width(o);
                if (l > k) {
                    k = l;
                }
            }
            int j2 = x;
            int k2 = y;
            int i1 = 8;
            if (list.size() > 1) {
                i1 += 2 + (list.size() - 1) * 10;
            }
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 300.0F);
            int j1 = -267386864;
            graphics.fillGradient(x - 3, y - 4, x + k + 3, y - 3, j1, j1);
            graphics.fillGradient(x - 3, y + i1 + 3, x + k + 3, y + i1 + 4, j1, j1);
            graphics.fillGradient(x - 3, y - 3, x + k + 3, y + i1 + 3, j1, j1);
            graphics.fillGradient(x - 4, y - 3, x - 3, y + i1 + 3, j1, j1);
            graphics.fillGradient(x + k + 3, y - 3, x + k + 4, y + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & 0xFF000000;
            graphics.fillGradient(x - 3, y - 3 + 1, x - 3 + 1, y + i1 + 3 - 1, k1, l1);
            graphics.fillGradient(x + k + 2, y - 3 + 1, x + k + 3, y + i1 + 3 - 1, k1, l1);
            graphics.fillGradient(x - 3, y - 3, x + k + 3, y - 3 + 1, k1, k1);
            graphics.fillGradient(x - 3, y + i1 + 2, x + k + 3, y + i1 + 3, l1, l1);
            for (int i2 = 0; i2 < list.size(); i2++) {
                Component s1 = (Component) list.get(i2);
                graphics.drawString(font, s1, j2, k2, -1);
                if (i2 == 0) {
                    k2 += 2;
                }
                k2 += 10;
            }
            graphics.pose().popPose();
            RenderSystem.enableDepthTest();
        }
    }
}