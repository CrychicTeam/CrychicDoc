package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.block.state.CanvasSign;

public class HangingCanvasSignEditScreen extends AbstractSignEditScreen {

    private static final Vector3f TEXT_SCALE = new Vector3f(0.9F, 0.9F, 0.9F);

    protected DyeColor dye;

    private final ResourceLocation texture;

    public HangingCanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFrontText, boolean isTextFilteringEnabled) {
        super(signBlockEntity, isFrontText, isTextFilteringEnabled, Component.translatable("hanging_sign.edit"));
        if (signBlockEntity.m_58900_().m_60734_() instanceof CanvasSign canvasSign) {
            this.dye = canvasSign.getBackgroundColor();
        }
        String dyeName = this.dye != null ? "_" + this.dye.getName() : "";
        this.texture = new ResourceLocation("farmersdelight", "canvas" + dyeName + ".png").withPrefix("textures/gui/hanging_signs/");
    }

    @Override
    protected void offsetSign(GuiGraphics gui, BlockState state) {
        gui.pose().translate((float) this.f_96543_ / 2.0F, 125.0F, 50.0F);
    }

    @Override
    protected void renderSignBackground(GuiGraphics gui, BlockState blockState0) {
        gui.pose().translate(0.0F, -13.0F, 0.0F);
        gui.pose().scale(4.5F, 4.5F, 1.0F);
        gui.blit(this.texture, -8, -8, 0.0F, 0.0F, 16, 16, 16, 16);
    }

    @Override
    protected Vector3f getSignTextScale() {
        return TEXT_SCALE;
    }
}