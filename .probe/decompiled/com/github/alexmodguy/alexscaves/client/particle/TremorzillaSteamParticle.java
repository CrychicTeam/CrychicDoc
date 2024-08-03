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
import net.minecraft.world.phys.Vec3;

public class TremorzillaSteamParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private final int tremorzillaId;

    private final Vec3 inMouthOffset;

    protected TremorzillaSteamParticle(ClientLevel level, double x, double y, double z, int tremorzillaId, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107663_ = this.f_107663_ * (1.0F + this.f_107223_.nextFloat() * 3.0F);
        this.f_107225_ = 40 + this.f_107223_.nextInt(10);
        this.m_108339_(sprites);
        this.f_107219_ = true;
        this.tremorzillaId = tremorzillaId;
        this.inMouthOffset = new Vec3(this.f_107223_.nextBoolean() ? 0.9F : -0.9F, (double) (0.7F + this.f_107223_.nextFloat() * 0.3F), (double) (this.f_107223_.nextFloat() * 2.0F - 1.2F));
        Vec3 vec3 = this.getInMouthPos(1.0F);
        this.m_107264_(vec3.x, vec3.y, vec3.z);
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107226_ = -0.05F - this.f_107223_.nextFloat() * 0.05F;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        float f = (float) this.f_107224_ / (float) this.f_107225_;
        this.m_107271_(1.0F - f);
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TremorzillaSteamParticle particle = new TremorzillaSteamParticle(worldIn, x, y, z, (int) xSpeed, this.spriteSet);
            float color = 0.2F * worldIn.f_46441_.nextFloat() + 0.6F;
            particle.m_107253_(color, color, color);
            return particle;
        }
    }
}