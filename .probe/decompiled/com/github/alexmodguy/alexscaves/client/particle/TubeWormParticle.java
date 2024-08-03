package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.TubeWormModel;
import com.github.alexmodguy.alexscaves.server.block.TubeWormBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TubeWormParticle extends Particle {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/particle/tube_worm.png");

    private static final TubeWormModel MODEL = new TubeWormModel();

    private BlockPos blockPos;

    private int checkScareCooldown;

    private float tuckAmount;

    private float prevTuckAmount;

    private float yRot;

    private float animationOffset;

    private boolean scared;

    protected TubeWormParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.f_107226_ = 0.0F;
        this.f_107225_ = 90 + this.f_107223_.nextInt(50);
        this.m_107250_(0.6F, 1.5F);
        this.blockPos = BlockPos.containing(x, y, z);
        this.animationOffset = ACMath.sampleNoise3D((int) x, (int) y, (int) z, 6.0F);
        this.checkScareCooldown = 5 + this.f_107223_.nextInt(10);
        this.prevTuckAmount = this.tuckAmount = 1.0F;
        this.yRot = Direction.from2DDataValue(2 + this.f_107223_.nextInt(3)).toYRot();
    }

    public boolean shouldCull() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevTuckAmount = this.tuckAmount;
        if (this.checkScareCooldown-- <= 0) {
            this.checkScareCooldown = 10 + this.f_107223_.nextInt(10);
            Vec3 vec3 = Vec3.atCenterOf(this.blockPos);
            AABB scareBox = new AABB(vec3.add(-5.0, -1.5, -5.0), vec3.add(5.0, 3.0, 5.0));
            this.scared = !this.f_107208_.m_45976_(LivingEntity.class, scareBox).isEmpty();
        }
        BlockState state = this.f_107208_.m_8055_(this.blockPos);
        float targetTuckAmount;
        if (!this.scared && this.f_107224_ < this.f_107225_ - 10 && state.m_60819_().is(FluidTags.WATER) && this.f_107208_.m_6425_(this.blockPos.above()).is(FluidTags.WATER)) {
            targetTuckAmount = 0.0F;
        } else {
            targetTuckAmount = 1.0F;
        }
        if (this.tuckAmount < targetTuckAmount) {
            this.tuckAmount = Math.min(targetTuckAmount, this.tuckAmount + 0.1F);
        }
        if (this.tuckAmount > targetTuckAmount) {
            this.tuckAmount = Math.max(targetTuckAmount, this.tuckAmount - 0.1F);
        }
        if (!TubeWormBlock.canSupportWormAt(this.f_107208_, state, this.blockPos)) {
            this.remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        ((ClientProxy) AlexsCaves.PROXY).removeParticleAt(this.blockPos);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float scale = 1.0F;
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        float lerpedTuck = this.prevTuckAmount + (this.tuckAmount - this.prevTuckAmount) * partialTick;
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(f, f1 + 1.0F, f2);
        posestack.scale(-scale, -scale, scale);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        MODEL.animateParticle((float) this.f_107224_, lerpedTuck, this.animationOffset, this.yRot, partialTick);
        VertexConsumer baseConsumer = multibuffersource$buffersource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        MODEL.m_7695_(posestack, baseConsumer, this.m_6355_(partialTick), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TubeWormParticle(worldIn, x, y, z);
        }
    }
}