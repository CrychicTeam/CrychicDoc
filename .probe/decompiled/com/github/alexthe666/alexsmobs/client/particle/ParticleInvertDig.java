package com.github.alexthe666.alexsmobs.client.particle;

import com.github.alexthe666.alexsmobs.item.ItemDimensionalCarver;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleInvertDig extends SimpleAnimatedParticle {

    private final Entity creator;

    protected ParticleInvertDig(ClientLevel world, double x, double y, double z, SpriteSet spriteWithAge, double creatorId) {
        super(world, x, y, z, spriteWithAge, 0.0F);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107663_ = 0.1F;
        this.f_107230_ = 1.0F;
        this.f_107225_ = 200;
        this.f_107219_ = false;
        this.creator = world.getEntity((int) creatorId);
    }

    @Override
    public int getLightColor(float p_189214_1_) {
        return 240;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        boolean live = false;
        this.f_107663_ = 0.1F + Math.min((float) this.f_107224_ / (float) this.f_107225_, 0.5F) * 0.5F;
        if (this.f_107224_++ < this.f_107225_ && this.creator != null) {
            if (this.creator instanceof Player player) {
                ItemStack item = player.m_21211_();
                if (item.getItem() instanceof ItemDimensionalCarver) {
                    this.f_107224_ = Mth.clamp(this.f_107225_ - player.m_21212_(), 0, this.f_107225_);
                    live = true;
                }
            }
        } else {
            this.m_107274_();
        }
        if (!live) {
            this.m_107274_();
        }
        this.m_108339_(this.f_107644_);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleInvertDig heartparticle = new ParticleInvertDig(worldIn, x, y, z, this.spriteSet, xSpeed);
            heartparticle.m_108339_(this.spriteSet);
            return heartparticle;
        }
    }
}