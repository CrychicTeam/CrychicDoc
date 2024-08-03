package com.rekindled.embers.entity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.projectile.EffectDamage;
import com.rekindled.embers.damage.DamageEmber;
import com.rekindled.embers.datagen.EmbersDamageTypes;
import com.rekindled.embers.datagen.EmbersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AncientGolemEntity extends Monster {

    public AncientGolemEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21364_ = 10;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 0.46, false));
        this.f_21345_.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.46));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ARMOR, 6.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_213877_() && this.m_21223_() > 0.0F && this.f_19797_ % 100 == 0 && this.m_5448_() != null && !this.m_9236_().isClientSide()) {
            this.m_5496_(EmbersSounds.FIREBALL.get(), 1.0F, 1.0F);
            EmberProjectileEntity proj = RegistryManager.EMBER_PROJECTILE.get().create(this.m_9236_());
            DamageSource damage = new DamageEmber(((Registry) this.m_9236_().registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(EmbersDamageTypes.EMBER_KEY), proj, this);
            EffectDamage effect = new EffectDamage(4.0F, e -> damage, 1, 1.0);
            Vec3 lookVec = this.m_20154_();
            proj.shoot(lookVec.x, lookVec.y, lookVec.z, 0.5F, 0.0F, 4.0);
            proj.m_146884_(this.m_146892_());
            proj.setEffect(effect);
            this.m_9236_().m_7967_(proj);
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (super.m_7327_(pEntity)) {
            this.m_216990_(EmbersSounds.ANCIENT_GOLEM_PUNCH.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return EmbersSounds.ANCIENT_GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EmbersSounds.ANCIENT_GOLEM_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.m_7355_(pos, state);
        this.m_216990_(EmbersSounds.ANCIENT_GOLEM_STEP.get());
    }
}