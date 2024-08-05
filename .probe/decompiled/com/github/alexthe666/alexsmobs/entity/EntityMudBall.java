package com.github.alexthe666.alexsmobs.entity;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class EntityMudBall extends EntityMobProjectile {

    public EntityMudBall(EntityType type, Level level) {
        super(type, level);
    }

    public EntityMudBall(Level worldIn, EntityMudskipper mudskipper) {
        super(AMEntityRegistry.MUD_BALL.get(), worldIn, mudskipper);
        Vec3 vec3 = mudskipper.m_20182_().add(this.calcOffsetVec(new Vec3(0.0, 0.0, (double) (0.2F * mudskipper.m_6134_())), 0.0F, mudskipper.m_146908_()));
        this.m_6034_(vec3.x, vec3.y, vec3.z);
    }

    public EntityMudBall(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.MUD_BALL.get(), world);
    }

    @Override
    public void doBehavior() {
        this.m_20256_(this.m_20184_().scale(0.9F));
        if (!this.m_20068_()) {
            this.m_20256_(this.m_20184_().add(0.0, -0.06F, 0.0));
        }
    }

    @Override
    protected boolean removeInWater() {
        return false;
    }

    @Override
    protected float getDamage() {
        return (float) (1 + this.f_19796_.nextInt(3));
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (result.getEntity() instanceof LivingEntity hurt) {
            hurt.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60));
        }
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == 3) {
            ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MUD.defaultBlockState());
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(particle, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
        } else {
            super.m_7822_(event);
        }
    }

    @Override
    protected void onImpact(HitResult result) {
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
        }
        super.onImpact(result);
    }
}