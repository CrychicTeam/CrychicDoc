package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class HangingSignEditScreen extends AbstractSignEditScreen {

    public static final float MAGIC_BACKGROUND_SCALE = 4.5F;

    private static final Vector3f TEXT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);

    private static final int TEXTURE_WIDTH = 16;

    private static final int TEXTURE_HEIGHT = 16;

    private final ResourceLocation texture = new ResourceLocation("textures/gui/hanging_signs/" + this.f_244069_.name() + ".png");

    public HangingSignEditScreen(SignBlockEntity signBlockEntity0, boolean boolean1, boolean boolean2) {
        super(signBlockEntity0, boolean1, boolean2, Component.translatable("hanging_sign.edit"));
    }

    @Override
    protected void offsetSign(GuiGraphics guiGraphics0, BlockState blockState1) {
        guiGraphics0.pose().translate((float) this.f_96543_ / 2.0F, 125.0F, 50.0F);
    }

    @Override
    protected void renderSignBackground(GuiGraphics guiGraphics0, BlockState blockState1) {
        guiGraphics0.pose().translate(0.0F, -13.0F, 0.0F);
        guiGraphics0.pose().scale(4.5F, 4.5F, 1.0F);
        guiGraphics0.blit(this.texture, -8, -8, 0.0F, 0.0F, 16, 16, 16, 16);
    }

    @Override
    protected Vector3f getSignTextScale() {
        return TEXT_SCALE;
    }
}