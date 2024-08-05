package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

public class CanvasSignEditScreen extends SignEditScreen {

    @Nullable
    protected SignRenderer.SignModel signModel;

    @Nullable
    protected DyeColor dye;

    protected final boolean isFrontText;

    public CanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
        super(signBlockEntity, isFront, isTextFilteringEnabled);
        if (signBlockEntity.m_58900_().m_60734_() instanceof CanvasSign canvasSign) {
            this.dye = canvasSign.getBackgroundColor();
        }
        this.isFrontText = isFront;
    }

    @Override
    protected void init() {
        super.init();
        this.signModel = SignRenderer.createSignModel(this.f_96541_.getEntityModels(), this.f_244069_);
    }

    @Override
    protected void renderSignBackground(GuiGraphics guiGraphics, BlockState state) {
        if (this.signModel != null) {
            boolean flag = state.m_60734_() instanceof StandingSignBlock;
            guiGraphics.pose().translate(0.0F, 31.0F, 0.0F);
            if (!this.isFrontText) {
                guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(180.0F));
            }
            guiGraphics.pose().scale(62.500004F, 62.500004F, -62.500004F);
            Material material = ModAtlases.getCanvasSignMaterial(this.dye);
            VertexConsumer vertexconsumer = material.buffer(guiGraphics.bufferSource(), this.signModel::m_103119_);
            this.signModel.stick.visible = flag;
            this.signModel.root.render(guiGraphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }
}