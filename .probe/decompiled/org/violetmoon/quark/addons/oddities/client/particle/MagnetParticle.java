package org.violetmoon.quark.addons.oddities.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.violetmoon.quark.addons.oddities.block.MagnetBlock;

public class MagnetParticle extends TextureSheetParticle {

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0);

    private float xWobble = 0.0F;

    private float xWobbleO = 0.0F;

    private float yWobble = 0.0F;

    private float yWobbleO = 0.0F;

    private float alphaO = 0.0F;

    public MagnetParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_107215_ = pXSpeed;
        this.f_107216_ = pYSpeed;
        this.f_107217_ = pZSpeed;
        this.f_107225_ = 33;
        this.f_172258_ = 1.0F;
        this.m_107250_(0.01F, 0.01F);
        this.f_107230_ = 0.0F;
        this.updateAlpha();
    }

    private void updateAlpha() {
        this.alphaO = this.f_107230_;
        int offset = 1;
        float t = (float) (this.f_107224_ + offset) / (float) (this.f_107225_ + 1 + offset);
        this.m_107271_(0.6F * (1.0F - Mth.square(2.0F * t - 1.0F)));
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float t = ((float) this.f_107224_ + partialTicks) / (float) (this.f_107225_ + 1);
        return this.f_107663_ * (0.6F + (1.0F - Mth.square(2.0F * t - 1.0F)) * 0.4F);
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float x = (float) (Mth.lerp((double) pPartialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float y = (float) (Mth.lerp((double) pPartialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float z = (float) (Mth.lerp((double) pPartialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternionf;
        if (this.f_107231_ == 0.0F) {
            quaternionf = pRenderInfo.rotation();
        } else {
            quaternionf = new Quaternionf(pRenderInfo.rotation());
            quaternionf.rotateZ(Mth.lerp(pPartialTicks, this.f_107204_, this.f_107231_));
        }
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float size = this.getQuadSize(pPartialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            float xWob = Mth.lerp(pPartialTicks, this.xWobbleO, this.xWobble);
            float yWob = Mth.lerp(pPartialTicks, this.yWobbleO, this.yWobble);
            vector3f.add(xWob, yWob, 0.0F);
            vector3f.rotate(quaternionf);
            vector3f.mul(size);
            vector3f.add(x, y, z);
        }
        float f6 = this.m_5970_();
        float f7 = this.m_5952_();
        float f4 = this.m_5951_();
        float f5 = this.m_5950_();
        float al = Mth.lerp(pPartialTicks, this.alphaO, this.f_107230_);
        int j = this.getLightColor(pPartialTicks);
        pBuffer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, al).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f7, f4).color(this.f_107227_, this.f_107228_, this.f_107229_, al).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f6, f4).color(this.f_107227_, this.f_107228_, this.f_107229_, al).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f6, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, al).uv2(j).endVertex();
    }

    @Override
    public int getLightColor(float pPartialTick) {
        int i = super.m_6355_(pPartialTick);
        int k = i >> 16 & 0xFF;
        return 240 | k << 16;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.updateAlpha();
        float wobbleAmount = 0.12F;
        this.xWobbleO = this.xWobble;
        this.yWobbleO = this.yWobble;
        this.xWobble = this.f_107223_.nextFloat() * wobbleAmount;
        this.yWobble = this.f_107223_.nextFloat() * wobbleAmount;
    }

    @Override
    public void move(double pX, double pY, double pZ) {
        if (this.f_107219_ && (pX != 0.0 || pY != 0.0 || pZ != 0.0) && pX * pX + pY * pY + pZ * pZ < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
            Vec3 moveDir = new Vec3(pX, pY, pZ);
            Vec3 vec3 = Entity.collideBoundingBox(null, moveDir, this.m_107277_(), this.f_107208_, List.of());
            if (moveDir.distanceToSqr(vec3) > 1.0E-9 && !(this.f_107208_.m_8055_(BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_)).m_60734_() instanceof MagnetBlock)) {
                this.m_107274_();
                return;
            }
        }
        if (pX != 0.0 || pY != 0.0 || pZ != 0.0) {
            this.m_107259_(this.m_107277_().move(pX, pY, pZ));
            this.m_107275_();
        }
        this.f_107218_ = pY != pY && pY < 0.0;
        if (pX != pX) {
            this.f_107215_ = 0.0;
        }
        if (pZ != pZ) {
            this.f_107217_ = 0.0;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return MagnetParticleRenderType.ADDITIVE_TRANSLUCENCY;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            MagnetParticle particle = new MagnetParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            particle.m_108335_(this.sprite);
            return particle;
        }
    }
}