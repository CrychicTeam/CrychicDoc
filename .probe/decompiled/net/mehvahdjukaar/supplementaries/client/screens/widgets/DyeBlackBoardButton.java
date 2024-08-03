package net.mehvahdjukaar.supplementaries.client.screens.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mehvahdjukaar.supplementaries.client.screens.BlackBoardScreen;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlackboardBlock;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

public class DyeBlackBoardButton extends BlackboardButton {

    public static final int SIZE = 10;

    public DyeBlackBoardButton(BlackBoardScreen screen, int x, int y, byte color) {
        super(screen, x, y, color, 10);
    }

    @Override
    protected void onClick() {
        this.parent.setSelectedColor(this.color);
    }

    @Override
    protected void renderButton(GuiGraphics graphics) {
        int rgb = this.color == 0 ? DyeColor.BLACK.getMapColor().col : BlackboardBlock.colorFromByte(this.color);
        float mul = this.shouldDrawOverlay ? 1.2F : 1.0F;
        float b = Mth.clamp((float) FastColor.ARGB32.blue(rgb) / 255.0F * mul, 0.0F, 1.0F);
        float r = Mth.clamp((float) FastColor.ARGB32.red(rgb) / 255.0F * mul, 0.0F, 1.0F);
        float g = Mth.clamp((float) FastColor.ARGB32.green(rgb) / 255.0F * mul, 0.0F, 1.0F);
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        graphics.blit(ModTextures.BLACKBOARD_BLANK_TEXTURE, this.x, this.y, 0.0F, 0.0F, this.size, this.size, 8, 8);
        this.shouldDrawOverlay = this.parent.getSelectedColor() == this.color;
    }
}