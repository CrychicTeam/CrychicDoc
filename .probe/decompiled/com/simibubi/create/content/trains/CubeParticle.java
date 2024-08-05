package com.simibubi.create.content.trains;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.AllSpecialTextures;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CubeParticle extends Particle {

    public static final Vec3[] CUBE = new Vec3[] { new Vec3(1.0, 1.0, -1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, -1.0, -1.0), new Vec3(1.0, 1.0, -1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, -1.0, -1.0), new Vec3(-1.0, 1.0, -1.0), new Vec3(-1.0, 1.0, 1.0), new Vec3(-1.0, -1.0, 1.0), new Vec3(1.0, -1.0, 1.0), new Vec3(1.0, 1.0, 1.0), new Vec3(1.0, 1.0, -1.0), new Vec3(1.0, -1.0, -1.0) };

    private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            AllSpecialTextures.BLANK.bind();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }
    };

    protected float scale;

    protected boolean hot;

    private boolean billowing = false;

    public CubeParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.f_107215_ = motionX;
        this.f_107216_ = motionY;
        this.f_107217_ = motionZ;
        this.setScale(0.2F);
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.m_107250_(scale * 0.5F, scale * 0.5F);
    }

    public void averageAge(int age) {
        this.f_107225_ = (int) ((double) age + (this.f_107223_.nextDouble() * 2.0 - 1.0) * 8.0);
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    @Override
    public void tick() {
        if (this.hot && this.f_107224_ > 0) {
            if (this.f_107210_ == this.f_107213_) {
                this.billowing = true;
                this.f_107205_ = false;
                if (this.f_107215_ == 0.0 && this.f_107217_ == 0.0) {
                    Vec3 diff = Vec3.atLowerCornerOf(BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_)).add(0.5, 0.5, 0.5).subtract(this.f_107212_, this.f_107213_, this.f_107214_);
                    this.f_107215_ = -diff.x * 0.1;
                    this.f_107217_ = -diff.z * 0.1;
                }
                this.f_107215_ *= 1.1;
                this.f_107216_ *= 0.9;
                this.f_107217_ *= 1.1;
            } else if (this.billowing) {
                this.f_107216_ *= 1.2;
            }
        }
        super.tick();
    }

    @Override
    public void render(VertexConsumer builder, Camera renderInfo, float p_225606_3_) {
        Vec3 projectedView = renderInfo.getPosition();
        float lerpedX = (float) (Mth.lerp((double) p_225606_3_, this.f_107209_, this.f_107212_) - projectedView.x());
        float lerpedY = (float) (Mth.lerp((double) p_225606_3_, this.f_107210_, this.f_107213_) - projectedView.y());
        float lerpedZ = (float) (Mth.lerp((double) p_225606_3_, this.f_107211_, this.f_107214_) - projectedView.z());
        int light = 15728880;
        double ageMultiplier = 1.0 - Math.pow((double) Mth.clamp((float) this.f_107224_ + p_225606_3_, 0.0F, (float) this.f_107225_), 3.0) / Math.pow((double) this.f_107225_, 3.0);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                Vec3 vec = CUBE[i * 4 + j].scale(-1.0);
                vec = vec.scale((double) this.scale * ageMultiplier).add((double) lerpedX, (double) lerpedY, (double) lerpedZ);
                builder.vertex(vec.x, vec.y, vec.z).uv((float) (j / 2), (float) (j % 2)).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return RENDER_TYPE;
    }

    public static class Factory implements ParticleProvider<CubeParticleData> {

        public Particle createParticle(CubeParticleData data, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            CubeParticle particle = new CubeParticle(world, x, y, z, motionX, motionY, motionZ);
            particle.m_107253_(data.r, data.g, data.b);
            particle.setScale(data.scale);
            particle.averageAge(data.avgAge);
            particle.setHot(data.hot);
            return particle;
        }
    }
}