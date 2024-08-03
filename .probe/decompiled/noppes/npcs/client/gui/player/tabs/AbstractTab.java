package noppes.npcs.client.gui.player.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractTab extends AbstractButton {

    public int id;

    ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    ItemStack renderStack;

    public AbstractTab(int id, int posX, int posY, ItemStack renderStack) {
        super(posX, posY, 28, 32, Component.translatable(""));
        this.renderStack = renderStack;
        this.id = id;
    }

    public AbstractTab init(Screen s) {
        int guiLeft = (s.width - 176) / 2;
        int guiTop = (s.height - 166) / 2;
        this.m_252865_(guiLeft + this.id * 28);
        this.m_253211_(guiTop - 28);
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft mc = Minecraft.getInstance();
            int yTexPos = this.f_93623_ ? 3 : 32;
            int ySize = this.f_93623_ ? 25 : 32;
            int xOffset = this.id == 2 ? 0 : 1;
            int yPos = this.m_252907_() + (this.f_93623_ ? 3 : 0);
            ItemRenderer itemRender = mc.getItemRenderer();
            RenderSystem.setShaderTexture(0, this.texture);
            graphics.blit(this.texture, this.m_252754_(), yPos, xOffset * 28, yTexPos, 28, ySize);
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 30.0F);
            graphics.renderItem(this.renderStack, this.m_252754_() + 6, this.m_252907_() + 8);
            graphics.renderItemDecorations(mc.font, this.renderStack, this.m_252754_() + 6, this.m_252907_() + 8, null);
            graphics.pose().popPose();
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onTabClicked();
    }

    @Override
    public void onPress() {
    }

    public abstract void onTabClicked();

    public abstract boolean shouldAddToList();
}