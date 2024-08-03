package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.entity.TremorzillaRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TremorzillaLightningParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private final int tremorzillaId;

    private final Vec3 inMouthOffset;

    private final Vec3 inMouthOrigin;

    private final Vec3 beamPosOffset;

    protected TremorzillaLightningParticle(ClientLevel level, double x, double y, double z, int tremorzillaId, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_107226_ = 0.0F;
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107663_ = this.f_107663_ * (2.0F + this.f_107223_.nextFloat() * 2.0F);
        this.f_107225_ = 2;
        this.m_108339_(sprites);
        this.f_107219_ = true;
        this.tremorzillaId = tremorzillaId;
        this.inMouthOffset = new Vec3(this.f_107223_.nextBoolean() ? 0.8F : -0.8F, (double) (this.f_107223_.nextFloat() * 0.8F), (double) (this.f_107223_.nextFloat() * 0.8F - 0.2F));
        this.beamPosOffset = new Vec3((double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F)).scale(6.0);
        Vec3 vec3 = this.getInMouthPos(1.0F);
        this.f_107212_ = vec3.x;
        this.f_107213_ = vec3.y;
        this.f_107214_ = vec3.z;
        this.inMouthOrigin = vec3;
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107231_ = (float) Math.toRadians((double) ((float) this.f_107223_.nextInt(3) * 90.0F));
        this.f_107204_ = this.f_107231_;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        if (this.tremorzillaId != -1 && this.f_107208_.getEntity(this.tremorzillaId) instanceof TremorzillaEntity entity) {
            Vec3 beamPos = entity.getClientBeamEndPosition(1.0F);
            Vec3 inMouthPos = this.getInMouthPos(1.0F);
            if (entity.getBeamProgress(1.0F) > 0.0F && beamPos != null) {
                Vec3 dist = beamPos.add(this.beamPosOffset).subtract(inMouthPos);
                int distInTicks = (int) Math.ceil(beamPos.length() * 0.005F);
                this.f_107225_ = Math.max(this.f_107225_, distInTicks);
                float f = Mth.clamp((float) this.f_107224_ / (float) this.f_107225_, 0.0F, 1.0F);
                Vec3 setPosVec = inMouthPos.add(dist.scale((double) f));
                this.m_107264_(setPosVec.x, setPosVec.y, setPosVec.z);
            } else {
                this.m_107274_();
            }
        }
    }

    public Vec3 getInMouthPos(float partialTick) {
        if (this.tremorzillaId != -1 && this.f_107208_.getEntity(this.tremorzillaId) instanceof TremorzillaEntity entity) {
            Vec3 mouthPos = TremorzillaRenderer.getMouthPositionFor(this.tremorzillaId);
            if (mouthPos != null) {
                Vec3 translate = mouthPos.add(this.inMouthOffset).yRot((float) (Math.PI - (double) (entity.f_20883_ * (float) (Math.PI / 180.0))));
                return new Vec3(entity.m_20185_() + translate.x, entity.m_20186_() + translate.y, entity.m_20189_() + translate.z);
            }
        }
        return Vec3.ZERO;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TremorzillaLightningParticle(worldIn, x, y, z, (int) xSpeed, this.spriteSet);
        }
    }
}