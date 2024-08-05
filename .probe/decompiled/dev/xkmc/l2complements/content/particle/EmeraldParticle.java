package dev.xkmc.l2complements.content.particle;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EmeraldParticle extends TextureSheetParticle {

    public static final int LIFE = 20;

    protected EmeraldParticle(ClientLevel world, double x0, double y0, double z0, double x1, double y1, double z1) {
        super(world, x0, y0, z0, 0.0, 0.0, 0.0);
        this.f_107215_ = (x1 - x0) / 20.0;
        this.f_107216_ = (y1 - y0) / 20.0;
        this.f_107217_ = (z1 - z0) / 20.0;
        this.m_107250_(0.03F, 0.03F);
        this.f_107226_ = 0.0F;
        this.f_107225_ = 20;
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
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleEngine.SpriteParticleRegistration<SimpleParticleType> {

        @Override
        public ParticleProvider<SimpleParticleType> create(SpriteSet sprite) {
            return (type, world, x0, y0, z0, x1, y1, z1) -> {
                EmeraldParticle part = new EmeraldParticle(world, x0, y0, z0, x1, y1, z1);
                part.m_108335_(sprite);
                return part;
            };
        }
    }
}