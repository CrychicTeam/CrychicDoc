package io.redspace.ironsspellbooks.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.util.Utils;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FogParticle extends TextureSheetParticle {

    private static final Vector3f ROTATION_VECTOR = Util.make(new Vector3f(0.5F, 0.5F, 0.5F), Vector3f::normalize);

    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);

    private static final float DEGREES_90 = (float) (Math.PI / 2);

    FogParticle(ClientLevel pLevel, double pX, double pY, double pZ, double xd, double yd, double zd, FogParticleOptions options) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        float mag = 0.3F;
        this.f_107215_ = xd + (Math.random() * 2.0 - 1.0) * (double) mag;
        this.f_107216_ = yd + (Math.random() * 2.0 - 1.0) * (double) mag;
        this.f_107217_ = zd + (Math.random() * 2.0 - 1.0) * (double) mag;
        double d0 = (Math.random() + Math.random() + 1.0) * (double) mag * 0.3F;
        double d1 = Math.sqrt(this.f_107215_ * this.f_107215_ + this.f_107216_ * this.f_107216_ + this.f_107217_ * this.f_107217_);
        this.f_107215_ = this.f_107215_ / d1 * d0 * (double) mag;
        this.f_107216_ = this.f_107216_ / d1 * d0 * (double) mag + (double) (mag * 0.25F);
        this.f_107217_ = this.f_107217_ / d1 * d0 * (double) mag;
        this.f_107663_ = 1.5F * options.m_175813_();
        this.f_107225_ = Utils.random.nextIntBetweenInclusive(60, 120);
        this.f_107226_ = 0.1F;
        float f = this.f_107223_.nextFloat() * 0.14F + 0.85F;
        this.f_107227_ = options.m_252837_().x() * f;
        this.f_107228_ = options.m_252837_().y() * f;
        this.f_107229_ = options.m_252837_().z() * f;
        this.f_172258_ = 1.0F;
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return this.f_107663_ * (1.0F + Mth.clamp(((float) this.f_107224_ + pScaleFactor) / (float) this.f_107225_ * 0.75F, 0.0F, 1.0F)) * Mth.clamp((float) this.f_107224_ / 5.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.f_107216_ = this.f_107216_ - 0.04 * (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ *= 0.85F;
            this.f_107215_ *= 0.94F;
            this.f_107217_ *= 0.94F;
        }
    }

    private float noise(float offset) {
        return 10.0F * Mth.sin(offset * 0.01F);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialticks) {
        this.f_107230_ = 1.0F - Mth.clamp(((float) this.f_107224_ + partialticks - 20.0F) / (float) this.f_107225_, 0.2F, 0.7F);
        this.renderRotatedParticle(buffer, camera, partialticks, p_234005_ -> {
            p_234005_.mul(Axis.YP.rotation(0.0F));
            p_234005_.mul(Axis.XP.rotation((float) (-Math.PI / 2)));
        });
        this.renderRotatedParticle(buffer, camera, partialticks, p_234000_ -> {
            p_234000_.mul(Axis.YP.rotation((float) -Math.PI));
            p_234000_.mul(Axis.XP.rotation((float) (Math.PI / 2)));
        });
    }

    private void renderRotatedParticle(VertexConsumer pConsumer, Camera camera, float partialTick, Consumer<Quaternionf> pQuaternion) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternion = new Quaternionf().setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        pQuaternion.accept(quaternion);
        quaternion.transform(TRANSFORM_VECTOR);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f3 = this.getQuadSize(partialTick);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f3);
            vector3f.add(f, f1, f2);
        }
        int j = this.m_6355_(partialTick);
        this.makeCornerVertex(pConsumer, avector3f[0], this.m_5952_(), this.m_5950_(), j);
        this.makeCornerVertex(pConsumer, avector3f[1], this.m_5952_(), this.m_5951_(), j);
        this.makeCornerVertex(pConsumer, avector3f[2], this.m_5970_(), this.m_5951_(), j);
        this.makeCornerVertex(pConsumer, avector3f[3], this.m_5970_(), this.m_5950_(), j);
    }

    private void makeCornerVertex(VertexConsumer pConsumer, Vector3f pVec3f, float float0, float float1, int int2) {
        Vec3 wiggle = new Vec3((double) this.noise((float) ((double) this.f_107224_ + this.f_107212_)), (double) this.noise((float) ((double) this.f_107224_ - this.f_107212_)), (double) this.noise((float) ((double) this.f_107224_ + this.f_107214_))).scale(0.02F);
        pConsumer.vertex((double) pVec3f.x() + wiggle.x, (double) (pVec3f.y() + 0.08F + this.f_107230_ * 0.125F), (double) pVec3f.z() + wiggle.z).uv(float0, float1).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(int2).endVertex();
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<FogParticleOptions> {

        private final SpriteSet sprite;

        public Provider(SpriteSet pSprite) {
            this.sprite = pSprite;
        }

        public Particle createParticle(@NotNull FogParticleOptions options, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            FogParticle shriekparticle = new FogParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, options);
            shriekparticle.m_108335_(this.sprite);
            shriekparticle.m_107271_(1.0F);
            return shriekparticle;
        }
    }
}