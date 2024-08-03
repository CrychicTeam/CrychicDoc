package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class SignEditScreen extends AbstractSignEditScreen {

    public static final float MAGIC_SCALE_NUMBER = 62.500004F;

    public static final float MAGIC_TEXT_SCALE = 0.9765628F;

    private static final Vector3f TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);

    @Nullable
    private SignRenderer.SignModel signModel;

    public SignEditScreen(SignBlockEntity signBlockEntity0, boolean boolean1, boolean boolean2) {
        super(signBlockEntity0, boolean1, boolean2);
    }

    @Override
    protected void init() {
        super.init();
        this.signModel = SignRenderer.createSignModel(this.f_96541_.getEntityModels(), this.f_244069_);
    }

    @Override
    protected void offsetSign(GuiGraphics guiGraphics0, BlockState blockState1) {
        super.offsetSign(guiGraphics0, blockState1);
        boolean $$2 = blockState1.m_60734_() instanceof StandingSignBlock;
        if (!$$2) {
            guiGraphics0.pose().translate(0.0F, 35.0F, 0.0F);
        }
    }

    @Override
    protected void renderSignBackground(GuiGraphics guiGraphics0, BlockState blockState1) {
        if (this.signModel != null) {
            boolean $$2 = blockState1.m_60734_() instanceof StandingSignBlock;
            guiGraphics0.pose().translate(0.0F, 31.0F, 0.0F);
            guiGraphics0.pose().scale(62.500004F, 62.500004F, -62.500004F);
            Material $$3 = Sheets.getSignMaterial(this.f_244069_);
            VertexConsumer $$4 = $$3.buffer(guiGraphics0.bufferSource(), this.signModel::m_103119_);
            this.signModel.stick.visible = $$2;
            this.signModel.root.render(guiGraphics0.pose(), $$4, 15728880, OverlayTexture.NO_OVERLAY);
        }
    }

    @Override
    protected Vector3f getSignTextScale() {
        return TEXT_SCALE;
    }
}