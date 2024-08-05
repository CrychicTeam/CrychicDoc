package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

public class TeslaBulbLightningParticle extends Particle {

    private LightningRender lightningRender = new LightningRender();

    private static final Vector4f LIGHTNING_COLOR = new Vector4f(0.71F, 0.76F, 0.95F, 0.3F);

    public TeslaBulbLightningParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.m_107250_(6.0F, 6.0F);
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        Vec3 lightningTo = new Vec3(xd, yd, zd);
        this.f_107225_ = (int) Math.ceil(lightningTo.length());
        int sections = 4 * this.f_107225_;
        LightningBoltData.BoltRenderInfo boltData = new LightningBoltData.BoltRenderInfo(0.2F, 0.1F, 0.2F, 0.6F, LIGHTNING_COLOR, 0.5F);
        LightningBoltData bolt = new LightningBoltData(boltData, Vec3.ZERO, new Vec3(xd, yd, zd), sections).size(0.3F + this.f_107223_.nextFloat() * 0.2F).fade(LightningBoltData.FadeFunction.fade(0.5F)).lifespan(this.f_107225_ + 1).spawn(LightningBoltData.SpawnFunction.NO_DELAY);
        this.lightningRender.update(this, bolt, 1.0F);
    }

    public boolean shouldCull() {
        return false;
    }

    private boolean canSeeBlock(Vec3 from, Vec3 to) {
        BlockHitResult result = this.f_107208_.m_45547_(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        return Vec3.atCenterOf(result.getBlockPos()).distanceTo(to) < 3.0;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        Vec3 cameraPos = camera.getPosition();
        float x = (float) Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_);
        float y = (float) Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_);
        float z = (float) Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_);
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        posestack.translate(x, y, z);
        this.lightningRender.render(partialTick, posestack, multibuffersource$buffersource);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TeslaBulbLightningParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}