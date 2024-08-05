package fr.lucreeper74.createmetallurgy.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import fr.lucreeper74.createmetallurgy.content.glassed_foundry_lid.GlassedFoundryLidBlock;
import fr.lucreeper74.createmetallurgy.registries.CMBlocks;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedFoundryMixer extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 200.0F);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        int scale = 23;
        this.blockElement(CMPartialModels.SHAFTLESS_STONE_COGWHEEL).rotateBlock(0.0, (double) (getCurrentAngle() * 2.0F), 0.0).atLocal(0.0, 0.0, 0.0).scale((double) scale).render(graphics);
        this.blockElement(CMBlocks.FOUNDRY_MIXER_BLOCK.getDefaultState()).atLocal(0.0, 0.0, 0.0).scale((double) scale).render(graphics);
        float animation = (Mth.sin(AnimationTickHolder.getRenderTime() / 32.0F) + 1.0F) / 5.0F + 0.5F;
        this.blockElement(CMPartialModels.FOUNDRY_MIXER_POLE).atLocal(0.0, (double) animation, 0.0).scale((double) scale).render(graphics);
        this.blockElement(CMPartialModels.FOUNDRY_MIXER_HEAD).rotateBlock(0.0, (double) (getCurrentAngle() * 4.0F), 0.0).atLocal(0.0, (double) animation, 0.0).scale((double) scale).render(graphics);
        this.blockElement((BlockState) CMBlocks.GLASSED_FOUNDRY_LID_BLOCK.getDefaultState().m_61124_(GlassedFoundryLidBlock.UNDER_FOUNDRY_MIXER, true)).atLocal(0.0, 0.65, 0.0).scale((double) scale).render(graphics);
        this.blockElement(CMBlocks.FOUNDRY_BASIN_BLOCK.getDefaultState()).atLocal(0.0, 1.65, 0.0).scale((double) scale).render(graphics);
        matrixStack.popPose();
    }
}