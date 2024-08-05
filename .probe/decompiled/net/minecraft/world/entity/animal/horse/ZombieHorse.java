package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ZombieHorse extends AbstractHorse {

    public ZombieHorse(EntityType<? extends ZombieHorse> entityTypeExtendsZombieHorse0, Level level1) {
        super(entityTypeExtendsZombieHorse0, level1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return m_30627_().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource0) {
        this.m_21051_(Attributes.JUMP_STRENGTH).setBaseValue(m_272017_(randomSource0::m_188500_));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ZOMBIE_HORSE_HURT;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.ZOMBIE_HORSE.create(serverLevel0);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        return !this.m_30614_() ? InteractionResult.PASS : super.mobInteract(player0, interactionHand1);
    }

    @Override
    protected void addBehaviourGoals() {
    }
}