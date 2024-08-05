package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXSoul extends MAParticleBase {

    public FXSoul(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        Vector3 color = new Vector3(0.29803923F, 0.24705882F, 0.7490196F);
        this.m_107253_(color.x, color.y, color.z);
        this.f_107663_ = (float) ((double) this.f_107663_ * (0.15F + Math.random() * 0.15F));
        this.f_107230_ = 0.0F;
        this.maxAlpha = 1.0F;
        this.f_107225_ = (int) (20.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = true;
        this.m_108339_(sprite);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXSoulFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXSoulFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXSoul particle = new FXSoul(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            particle.f_107226_ = 0.003F;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}