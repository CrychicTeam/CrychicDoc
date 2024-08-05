package net.mehvahdjukaar.supplementaries.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FeatherParticle extends TextureSheetParticle {

    private final float rotSpeed;

    private boolean fallingAnim = false;

    private int animationOffset;

    private float rotOffset = 0.0F;

    private int groundTime = 0;

    private FeatherParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double speedX, double speedY, double speedZ) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.f_107663_ = (float) ((double) this.f_107663_ * (1.3125 + (double) this.f_107223_.nextFloat() * 0.15));
        this.f_107225_ = 360 + this.f_107223_.nextInt(60);
        this.rotSpeed = 2.0F * (0.045F + this.f_107223_.nextFloat() * 0.08F) + ((float) speedY - 0.03F);
        this.animationOffset = (int) (this.f_107223_.nextFloat() * (float) (Math.PI * 2) / this.rotSpeed);
        this.f_107215_ = speedX + ((double) this.f_107223_.nextFloat() * 2.0 - 1.0) * 0.008F;
        this.f_107216_ = speedY;
        this.f_107217_ = speedZ + ((double) this.f_107223_.nextFloat() * 2.0 - 1.0) * 0.008F;
        this.f_107226_ = 0.007F;
    }

    public void setRotOffset(int spriteIndex) {
        int[] offsets = new int[] { -45, 0, 16 };
        this.rotOffset = (float) offsets[spriteIndex] * (float) (Math.PI / 180.0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (++this.f_107224_ < this.f_107225_ && this.groundTime <= 20) {
            this.f_107216_ = this.f_107216_ - 0.04 * (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
            if (this.f_107218_ && this.f_107216_ > 0.0) {
                this.f_107218_ = false;
            }
            if (!this.f_107218_) {
                if (!this.fallingAnim) {
                    float rot = (float) ((double) ((float) (this.f_107224_ + this.animationOffset) * this.rotSpeed) % (Math.PI * 2));
                    if (this.f_107216_ <= 0.0 && rot > 0.0F && (double) rot < 0.01 + (double) (this.rotSpeed * 2.0F)) {
                        this.fallingAnim = true;
                        if (this.f_107204_ > 6.0F) {
                        }
                        this.animationOffset = this.f_107224_;
                    }
                    this.f_107204_ = this.f_107231_;
                    this.f_107231_ = rot;
                } else {
                    int t = this.f_107224_ - this.animationOffset;
                    double freq = (double) (1.0F - this.rotSpeed);
                    double k = 20.0;
                    float min = (float) (freq / 2.0);
                    float amp = (float) ((freq - (double) min) * Math.exp((double) (-t) / k)) + min;
                    float w = (float) ((double) this.rotSpeed / freq);
                    this.f_107204_ = this.f_107231_;
                    this.f_107231_ = Mth.sin((float) t * w) * amp;
                }
            } else {
                this.groundTime++;
                this.f_107204_ = this.f_107231_;
                this.f_107216_ = 0.0;
            }
        } else {
            this.m_107274_();
        }
    }

    @Override
    public void render(VertexConsumer builder, Camera info, float partialTicks) {
        Vec3 vector3d = info.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vector3d.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vector3d.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vector3d.z());
        Quaternionf quaternion;
        if (this.f_107231_ == 0.0F) {
            quaternion = info.rotation();
        } else {
            quaternion = new Quaternionf(info.rotation());
            float p = 180.0F / (float) Math.PI;
            float f3 = Mth.rotLerp(partialTicks, (this.rotOffset + this.f_107204_) * p, (this.rotOffset + this.f_107231_) * p);
            quaternion.mul(Axis.ZP.rotation(f3 / p));
        }
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f4 = this.m_5902_(partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = this.m_5970_();
        float f8 = this.m_5952_();
        float f5 = this.m_5951_();
        float f6 = this.m_5950_();
        int j = this.m_6355_(partialTicks);
        double offset = 0.125;
        builder.vertex((double) avector3f[0].x(), (double) avector3f[0].y() + offset, (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        builder.vertex((double) avector3f[1].x(), (double) avector3f[1].y() + offset, (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        builder.vertex((double) avector3f[2].x(), (double) avector3f[2].y() + offset, (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        builder.vertex((double) avector3f[3].x(), (double) avector3f[3].y() + offset, (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FeatherParticle particle = new FeatherParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            int i = particle.f_107223_.nextInt(3);
            particle.setRotOffset(i);
            particle.m_108337_(this.spriteSet.get(i, 2));
            return particle;
        }
    }
}