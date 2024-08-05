package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.entity.LuxtructosaurusRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LuxtructosaurusSpitParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private int onGroundTime;

    private final int luxtructosaurusId;

    private Vec3 inMouthOffset;

    protected LuxtructosaurusSpitParticle(ClientLevel level, double x, double y, double z, int luxtructosaurusId, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_107226_ = 0.0F;
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107663_ = this.f_107663_ * (0.8F + this.f_107223_.nextFloat() * 0.5F);
        this.f_107225_ = 100 + this.f_107223_.nextInt(40);
        this.f_107225_ = Math.max(this.f_107225_, 1);
        this.m_108339_(sprites);
        this.f_107219_ = true;
        this.luxtructosaurusId = luxtructosaurusId;
        this.inMouthOffset = new Vec3(this.f_107223_.nextBoolean() ? 0.7F : -0.7F, -0.0, (double) (this.f_107223_.nextFloat() * 1.0F - 0.25F));
        this.setInMouthPos(1.0F);
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
    }

    @Override
    public float getQuadSize(float f) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + f) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (this.isInMouth()) {
            this.f_107226_ = 0.0F;
            this.f_107215_ = 0.0;
            this.f_107216_ = 0.0;
            this.f_107217_ = 0.0;
            this.setInMouthPos(1.0F);
        } else {
            this.f_107226_ = 1.0F;
            if (this.f_107218_) {
                this.onGroundTime++;
            }
        }
        int sprite = this.f_107218_ ? 1 : 0;
        this.m_108337_(this.sprites.get(sprite, 1));
        if (this.onGroundTime > 5) {
            this.m_107274_();
        }
    }

    public boolean isInMouth() {
        return (float) this.f_107224_ < (float) this.f_107225_ * 0.25F;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    public void setInMouthPos(float partialTick) {
        if (this.luxtructosaurusId != -1 && this.f_107208_.getEntity(this.luxtructosaurusId) instanceof LuxtructosaurusEntity entity) {
            Vec3 mouthPos = LuxtructosaurusRenderer.getMouthPositionFor(this.luxtructosaurusId);
            if (mouthPos != null) {
                Vec3 translate = mouthPos.add(this.inMouthOffset).yRot((float) (Math.PI - (double) (entity.f_20883_ * (float) (Math.PI / 180.0))));
                this.m_107264_(entity.m_20185_() + translate.x, entity.m_20186_() + translate.y, entity.m_20189_() + translate.z);
            }
        }
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
            return new LuxtructosaurusSpitParticle(worldIn, x, y, z, (int) xSpeed, this.spriteSet);
        }
    }
}