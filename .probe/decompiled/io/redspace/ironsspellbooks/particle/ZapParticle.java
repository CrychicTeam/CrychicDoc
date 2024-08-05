package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ZapParticle extends TextureSheetParticle {

    private static final Vector3f ROTATION_VECTOR = Util.make(new Vector3f(0.5F, 0.5F, 0.5F), Vector3f::normalize);

    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);

    private static final float DEGREES_90 = (float) (Math.PI / 2);

    Vec3 destination;

    public static ParticleRenderType PARTICLE_EMISSIVE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder p_107455_, TextureManager p_107456_) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            p_107455_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator p_107458_) {
            p_107458_.end();
        }

        public String toString() {
            return "PARTICLE_EMISSIVE";
        }
    };

    ZapParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd, ZapParticleOption options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.m_107250_(1.0F, 1.0F);
        this.f_107663_ = 1.0F;
        this.destination = options.getDestination();
        this.f_107225_ = Utils.random.nextIntBetweenInclusive(3, 8);
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
    }

    @Override
    public void tick() {
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        }
    }

    public Vector3f randomVector3f(RandomSource random, float scale) {
        return new Vector3f((2.0F * random.nextFloat() - 1.0F) * scale, (2.0F * random.nextFloat() - 1.0F) * scale, (2.0F * random.nextFloat() - 1.0F) * scale);
    }

    private void setRGBA(float r, float g, float b, float a) {
        this.f_107227_ = r * a;
        this.f_107228_ = g * a;
        this.f_107229_ = b * a;
        this.f_107230_ = 1.0F;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        Vector3f start = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f end = new Vector3f((float) (this.destination.x - this.f_107212_), (float) (this.destination.y - this.f_107213_), (float) (this.destination.z - this.f_107214_));
        RandomSource randomSource = RandomSource.create((long) (this.f_107224_ + this.f_107225_) * 3456798L);
        int segments = randomSource.nextIntBetweenInclusive(1, 3);
        end.mul(1.0F / (float) segments);
        for (int i = 0; i < segments; i++) {
            Vector3f wiggle = this.randomVector3f(randomSource, 0.2F);
            end.add(wiggle);
            this.drawLightningBeam(consumer, partialTick, f, f1, f2, start, end, 0.6F, randomSource);
            start = new Vector3f(end.x, end.y, end.z);
            end.sub(wiggle);
            end.add(end);
        }
    }

    private void drawLightningBeam(VertexConsumer consumer, float partialTick, float f, float f1, float f2, Vector3f start, Vector3f end, float chanceToBranch, RandomSource randomSource) {
        Vector3f d = new Vector3f(end.x() - start.x(), end.y() - start.y(), end.z() - start.z());
        d.normalize();
        Vec2 heading = new Vec2((float) Math.asin((double) (-d.y())), (float) (-Mth.atan2((double) d.x(), (double) d.z())));
        this.setRGBA(1.0F, 1.0F, 1.0F, 1.0F);
        this.tube(consumer, partialTick, f, f1, f2, heading, start, end, 0.06F);
        this.setRGBA(0.25F, 0.7F, 1.0F, 0.3F);
        this.tube(consumer, partialTick, f, f1, f2, heading, start, end, 0.11F);
        this.setRGBA(0.25F, 0.7F, 1.0F, 0.15F);
        this.tube(consumer, partialTick, f, f1, f2, heading, start, end, 0.25F);
        if (randomSource.nextFloat() < chanceToBranch) {
            Vector3f branch = this.randomVector3f(randomSource, 0.5F);
            this.drawLightningBeam(consumer, partialTick, f, f1, f2, start, branch, chanceToBranch * 0.5F, randomSource);
        }
    }

    private void tube(VertexConsumer consumer, float partialTick, float f, float f1, float f2, Vec2 heading, Vector3f start, Vector3f end, float width) {
        float h = width * 0.5F;
        Vector3f[] left = new Vector3f[] { new Vector3f(-h * Mth.cos(heading.y) + start.x(), -h + start.y(), start.z() - h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + start.x(), h + start.y(), start.z() - h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + end.x(), h + end.y(), end.z() - h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + end.x(), -h + end.y(), end.z() - h * Mth.sin(heading.y)) };
        Vector3f[] right = new Vector3f[] { new Vector3f(h * Mth.cos(heading.y) + end.x(), -h + end.y(), end.z() + h * Mth.sin(heading.y)), new Vector3f(h * Mth.cos(heading.y) + end.x(), h + end.y(), end.z() + h * Mth.sin(heading.y)), new Vector3f(h * Mth.cos(heading.y) + start.x(), h + start.y(), start.z() + h * Mth.sin(heading.y)), new Vector3f(h * Mth.cos(heading.y) + start.x(), -h + start.y(), start.z() + h * Mth.sin(heading.y)) };
        Vector3f[] top = new Vector3f[] { new Vector3f(h * Mth.cos(heading.y) + start.x(), -h + start.y(), start.z() + h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + start.x(), -h + start.y(), start.z() - h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + end.x(), -h + end.y(), end.z() - h * Mth.sin(heading.y)), new Vector3f(h * Mth.cos(heading.y) + end.x(), -h + end.y(), end.z() + h * Mth.sin(heading.y)) };
        Vector3f[] bottom = new Vector3f[] { new Vector3f(h * Mth.cos(heading.y) + end.x(), h + end.y(), end.z() + h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + end.x(), h + end.y(), end.z() - h * Mth.sin(heading.y)), new Vector3f(-h * Mth.cos(heading.y) + start.x(), h + start.y(), start.z() - h * Mth.sin(heading.y)), new Vector3f(h * Mth.cos(heading.y) + start.x(), h + start.y(), start.z() + h * Mth.sin(heading.y)) };
        this.quad(consumer, partialTick, f, f1, f2, left);
        this.quad(consumer, partialTick, f, f1, f2, right);
        this.quad(consumer, partialTick, f, f1, f2, top);
        this.quad(consumer, partialTick, f, f1, f2, bottom);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float float0, float float1, int int2) {
        pConsumer.vertex((double) pVec3f.x(), (double) pVec3f.y(), (double) pVec3f.z()).uv(float0, float1).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(int2).endVertex();
    }

    private void quad(VertexConsumer pConsumer, float partialTick, float f, float f1, float f2, Vector3f[] avector3f) {
        float f3 = this.m_5902_(partialTick);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        int j = this.getLightColor(partialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.m_5952_(), this.m_5950_(), j);
        this.makeCornerVertex(pConsumer, avector3f[1], this.m_5952_(), this.m_5951_(), j);
        this.makeCornerVertex(pConsumer, avector3f[2], this.m_5970_(), this.m_5951_(), j);
        this.makeCornerVertex(pConsumer, avector3f[3], this.m_5970_(), this.m_5950_(), j);
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return PARTICLE_EMISSIVE;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 15728880;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ZapParticleOption> {

        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull ZapParticleOption options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ZapParticle particle = new ZapParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
            particle.m_108335_(this.sprite);
            particle.m_107271_(1.0F);
            return particle;
        }
    }
}