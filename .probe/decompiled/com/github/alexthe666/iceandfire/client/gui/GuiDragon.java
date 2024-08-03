package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class GuiDragon extends AbstractContainerScreen<ContainerDragon> {

    private static final ResourceLocation texture = new ResourceLocation("iceandfire:textures/gui/dragon.png");

    public GuiDragon(ContainerDragon dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
        this.f_97727_ = 214;
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics matrixStack, int mouseX, int mouseY) {
    }

    @Override
    public void render(@NotNull GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.m_280072_(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int k = (this.f_96543_ - this.f_97726_) / 2;
        int l = (this.f_96544_ - this.f_97727_) / 2;
        matrixStack.blit(texture, k, l, 0, 0, this.f_97726_, this.f_97727_);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase dragon) {
            float dragonScale = 1.0F / Math.max(1.0E-4F, dragon.getScale());
            Quaternionf quaternionf = new Quaternionf().rotateY((float) Mth.lerp((double) ((float) mouseX / (float) this.f_96543_), 0.0, Math.PI)).rotateZ((float) Mth.lerp((double) ((float) mouseY / (float) this.f_96543_), Math.PI, 3.3415926535897933));
            InventoryScreen.renderEntityInInventory(matrixStack, k + 88, l + (int) (0.5F * dragon.flyProgress) + 55, (int) (dragonScale * 23.0F), quaternionf, null, dragon);
        }
        if (entity instanceof EntityDragonBase dragon) {
            Font font = this.getMinecraft().font;
            String s3 = dragon.m_7770_() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + " " + dragon.m_7770_().getString();
            font.drawInBatch(s3, (float) (k + this.f_97726_ / 2 - font.width(s3) / 2), (float) (l + 75), 16777215, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor((double) Math.min(dragon.m_21223_(), dragon.m_21233_())) + " / " + dragon.m_21233_();
            font.drawInBatch(s2, (float) (k + this.f_97726_ / 2 - font.width(s2) / 2), (float) (l + 84), 16777215, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal(dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female");
            font.drawInBatch(s5, (float) (k + this.f_97726_ / 2 - font.width(s5) / 2), (float) (l + 93), 16777215, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            font.drawInBatch(s6, (float) (k + this.f_97726_ / 2 - font.width(s6) / 2), (float) (l + 102), 16777215, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s4 = StatCollector.translateToLocal("dragon.stage") + " " + dragon.getDragonStage() + " " + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal("dragon.days.back");
            font.drawInBatch(s4, (float) (k + this.f_97726_ / 2 - font.width(s4) / 2), (float) (l + 111), 16777215, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s7 = dragon.m_269323_() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.m_269323_().m_7755_().getString() : StatCollector.translateToLocal("dragon.untamed");
            font.drawInBatch(s7, (float) (k + this.f_97726_ / 2 - font.width(s7) / 2), (float) (l + 120), 16777215, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}