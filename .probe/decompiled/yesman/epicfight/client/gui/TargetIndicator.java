package yesman.epicfight.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class TargetIndicator extends EntityIndicator {

    @Override
    public boolean shouldDraw(LivingEntity entityIn, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch) {
        if (!EpicFightMod.CLIENT_CONFIGS.showTargetIndicator.getValue()) {
            return false;
        } else if (playerpatch != null && entityIn != playerpatch.getTarget()) {
            return false;
        } else if (!entityIn.m_20145_() && entityIn.isAlive() && entityIn != playerpatch.getOriginal()) {
            if (entityIn.m_20280_(Minecraft.getInstance().getCameraEntity()) >= 400.0) {
                return false;
            } else {
                return entityIn instanceof Player playerIn ? !playerIn.isSpectator() : true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void drawIndicator(LivingEntity entityIn, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, PoseStack matStackIn, MultiBufferSource bufferIn, float partialTicks) {
        Matrix4f mvMatrix = super.getMVMatrix(matStackIn, entityIn, 0.0F, entityIn.m_20206_() + 0.45F, 0.0F, true, partialTicks);
        if (entitypatch == null) {
            this.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(EpicFightRenderTypes.entityIndicator(BATTLE_ICON)), -0.1F, -0.1F, 0.1F, 0.1F, 97.0F, 2.0F, 128.0F, 33.0F);
        } else if (entityIn.f_19797_ % 2 == 0 && !entitypatch.flashTargetIndicator(playerpatch)) {
            this.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(EpicFightRenderTypes.entityIndicator(BATTLE_ICON)), -0.1F, -0.1F, 0.1F, 0.1F, 132.0F, 0.0F, 167.0F, 36.0F);
        } else {
            this.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(EpicFightRenderTypes.entityIndicator(BATTLE_ICON)), -0.1F, -0.1F, 0.1F, 0.1F, 97.0F, 2.0F, 128.0F, 33.0F);
        }
    }
}