package com.mna.entities.faction;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.faction.base.BaseFlyingFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class LivingWard extends BaseFlyingFactionMob<LivingWard> {

    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(LivingWard.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DETONATING = SynchedEntityData.defineId(LivingWard.class, EntityDataSerializers.BOOLEAN);

    public LivingWard(EntityType<LivingWard> mobType, Level world) {
        super(mobType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide()) {
            this.spawnParticles();
        }
    }

    private void spawnParticles() {
        int numParticles = 2;
        for (int i = 0; i < numParticles; i++) {
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), this.m_20185_() - 0.3 + Math.random() * 0.6, this.m_20186_() + Math.random() * 1.2, this.m_20189_() - 0.3 + Math.random() * 0.6, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(4, new LivingWard.DetonateGoal(this));
        this.f_21345_.addGoal(7, new LivingWard.BoltAttackGoal(this));
    }

    public void setAttacking(boolean attacking) {
        this.f_19804_.set(ATTACKING, attacking);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ATTACKING, false);
        this.f_19804_.define(DETONATING, false);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (cause.is(DamageTypes.FELL_OUT_OF_WORLD) && this.m_9236_().isClientSide() && this.f_19804_.get(DETONATING) && this.m_9236_().isClientSide() && this.f_19804_.get(DETONATING)) {
            for (int i = 0; i < 150; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), -0.5 + Math.random(), -0.5 + Math.random(), -0.5 + Math.random());
            }
        }
    }

    @Override
    public IFaction getFaction() {
        return Factions.COUNCIL;
    }

    @Override
    public CompoundTag getPacketData() {
        return new CompoundTag();
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFlyingFactionMob<?>> state) {
        return state.setAndContinue(RawAnimation.begin().thenLoop("animation.arcane_eye.idle"));
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    public static boolean canSpawnPredicate(EntityType<LivingWard> type, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.m_46791_() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(worldIn, pos, randomIn) && m_217057_(type, worldIn, reason, pos, randomIn);
    }

    static class BoltAttackGoal extends Goal {

        private final LivingWard parentEntity;

        public int attackTimer;

        public BoltAttackGoal(LivingWard ward) {
            this.parentEntity = ward;
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.m_5448_() != null;
        }

        @Override
        public void start() {
            this.attackTimer = 0;
        }

        @Override
        public void stop() {
            this.parentEntity.setAttacking(false);
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.parentEntity.m_5448_();
            if (livingentity != null) {
                if (livingentity.m_20280_(this.parentEntity) < 144.0 && this.parentEntity.m_21574_().hasLineOfSight(livingentity)) {
                    this.attackTimer++;
                    if (this.attackTimer == 40) {
                        this.parentEntity.setAttacking(true);
                    }
                    if (this.attackTimer == 42) {
                        if (!this.parentEntity.m_20067_()) {
                            this.parentEntity.m_5496_(SFX.Spell.Cast.ARCANE, 1.0F, (float) (0.9F + Math.random() * 0.2F));
                        }
                        boolean isMiss = Math.random() > (double) (0.3F + (float) this.parentEntity.getTier() * 0.5F);
                        Vec3 offset = new Vec3(0.0, 0.0, 0.0);
                        if (isMiss) {
                            offset = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() * 0.5);
                        }
                        int color = -10545027;
                        ServerMessageDispatcher.sendParticleSpawn(this.parentEntity.m_20185_(), this.parentEntity.m_20186_(), this.parentEntity.m_20189_(), this.parentEntity.m_5448_().m_20185_() + offset.x(), this.parentEntity.m_5448_().m_20186_() + (double) this.parentEntity.m_5448_().m_20192_() + offset.y(), this.parentEntity.m_5448_().m_20189_() + offset.z(), color, 64.0F, this.parentEntity.m_9236_().dimension(), ParticleInit.LIGHTNING_BOLT.get());
                        if (!isMiss) {
                            if (!this.parentEntity.m_5448_().m_20096_() && this.parentEntity.m_5448_().getEffect(EffectInit.DAMPEN_MAGIC.get()) == null) {
                                this.parentEntity.m_5448_().addEffect(new MobEffectInstance(EffectInit.DAMPEN_MAGIC.get(), 100));
                            }
                            this.parentEntity.m_5448_().hurt(this.parentEntity.m_269291_().mobAttack(this.parentEntity), 4.0F);
                        }
                    } else if (this.attackTimer >= 62) {
                        this.parentEntity.setAttacking(false);
                        this.attackTimer = 0;
                    }
                } else if (this.attackTimer > 0) {
                    this.attackTimer--;
                }
            }
        }
    }

    static class DetonateGoal extends Goal {

        private final LivingWard parentEntity;

        private int updateCount = 0;

        public DetonateGoal(LivingWard ward) {
            this.parentEntity = ward;
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.m_5448_() != null && this.parentEntity.m_21223_() < this.parentEntity.m_21223_() * 0.35F;
        }

        @Override
        public boolean canContinueToUse() {
            return this.parentEntity.m_5448_() != null;
        }

        @Override
        public void start() {
            this.parentEntity.f_19804_.set(LivingWard.DETONATING, true);
        }

        @Override
        public void tick() {
            if (this.parentEntity.m_5448_() != null) {
                if (++this.updateCount > 10) {
                    this.updateCount = 0;
                    this.parentEntity.m_20219_(this.parentEntity.m_146892_());
                }
                if (this.parentEntity.m_20270_(this.parentEntity.m_5448_()) < 5.0F) {
                    this.parentEntity.m_9236_().getEntities(this.parentEntity, this.parentEntity.m_20191_().inflate(7.5), e -> {
                        if (e instanceof Player && e.isAlive()) {
                            IPlayerProgression progression = (IPlayerProgression) ((Player) e).getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                            return progression != null && progression.getAlliedFaction() != null && progression.getAlliedFaction() != Factions.COUNCIL;
                        } else {
                            return false;
                        }
                    }).stream().map(e -> (Player) e).forEach(p -> p.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().consume(p, m.getCastingResource().getMaxAmount() * 0.1F)));
                    this.parentEntity.m_6469_(this.parentEntity.m_269291_().fellOutOfWorld(), 50000.0F);
                }
            }
        }
    }
}