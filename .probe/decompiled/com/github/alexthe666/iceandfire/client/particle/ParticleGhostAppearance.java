package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.client.model.ModelGhost;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.client.render.entity.RenderGhost;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ParticleGhostAppearance extends Particle {

    private final ModelGhost model = new ModelGhost(0.0F);

    private final int ghost;

    private boolean fromLeft = false;

    public ParticleGhostAppearance(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int ghost) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.f_107226_ = 0.0F;
        this.f_107225_ = 15;
        this.ghost = ghost;
        this.fromLeft = worldIn.f_46441_.nextBoolean();
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        float f = ((float) this.f_107224_ + partialTicks) / (float) this.f_107225_;
        float f1 = 0.05F + 0.5F * Mth.sin(f * (float) Math.PI);
        Entity entity = this.f_107208_.getEntity(this.ghost);
        if (entity instanceof EntityGhost && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            EntityGhost ghostEntity = (EntityGhost) entity;
            PoseStack matrixstack = new PoseStack();
            matrixstack.mulPose(renderInfo.rotation());
            if (this.fromLeft) {
                matrixstack.mulPose(Axis.YN.rotationDegrees(150.0F * f - 60.0F));
                matrixstack.mulPose(Axis.ZN.rotationDegrees(150.0F * f - 60.0F));
            } else {
                matrixstack.mulPose(Axis.YP.rotationDegrees(150.0F * f - 60.0F));
                matrixstack.mulPose(Axis.ZP.rotationDegrees(150.0F * f - 60.0F));
            }
            matrixstack.scale(-1.0F, -1.0F, 1.0F);
            matrixstack.translate(0.0, 0.3F, 1.25);
            MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer ivertexbuilder = irendertypebuffer$impl.getBuffer(IafRenderType.getGhost(RenderGhost.getGhostOverlayForType(ghostEntity.getColor())));
            this.model.setupAnim(ghostEntity, 0.0F, 0.0F, (float) entity.tickCount + partialTicks, 0.0F, 0.0F);
            this.model.m_7695_(matrixstack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, f1);
            irendertypebuffer$impl.endBatch();
        }
    }
}