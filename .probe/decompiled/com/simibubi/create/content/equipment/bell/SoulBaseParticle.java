package com.simibubi.create.content.equipment.bell;

import com.mojang.math.Axis;
import com.simibubi.create.AllParticleTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import org.joml.Quaternionf;

public class SoulBaseParticle extends CustomRotationParticle {

    private final SpriteSet animatedSprite;

    public SoulBaseParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(worldIn, x, y, z, spriteSet, 0.0F);
        this.animatedSprite = spriteSet;
        this.f_107663_ = 0.5F;
        this.m_107250_(this.f_107663_, this.f_107663_);
        this.loopLength = 16 + (int) (this.f_107223_.nextFloat() * 2.0F - 1.0F);
        this.f_107225_ = (int) (90.0F / (this.f_107223_.nextFloat() * 0.36F + 0.64F));
        this.selectSpriteLoopingWithAge(this.animatedSprite);
        this.f_107205_ = true;
    }

    @Override
    public void tick() {
        this.selectSpriteLoopingWithAge(this.animatedSprite);
        BlockPos pos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        if (this.f_107224_++ >= this.f_107225_ || !SoulPulseEffect.isDark(this.f_107208_, pos)) {
            this.m_107274_();
        }
    }

    @Override
    public Quaternionf getCustomRotation(Camera camera, float partialTicks) {
        return Axis.XP.rotationDegrees(90.0F);
    }

    public static class Data extends BasicParticleData<SoulBaseParticle> {

        @Override
        public BasicParticleData.IBasicParticleFactory<SoulBaseParticle> getBasicFactory() {
            return SoulBaseParticle::new;
        }

        @Override
        public ParticleType<?> getType() {
            return AllParticleTypes.SOUL_BASE.get();
        }
    }
}