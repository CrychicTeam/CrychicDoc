package net.minecraft.world.entity.monster;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Evoker extends SpellcasterIllager {

    @Nullable
    private Sheep wololoTarget;

    public Evoker(EntityType<? extends Evoker> entityTypeExtendsEvoker0, Level level1) {
        super(entityTypeExtendsEvoker0, level1);
        this.f_21364_ = 10;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new Evoker.EvokerCastingSpellGoal());
        this.f_21345_.addGoal(2, new AvoidEntityGoal(this, Player.class, 8.0F, 0.6, 1.0));
        this.f_21345_.addGoal(4, new Evoker.EvokerSummonSpellGoal());
        this.f_21345_.addGoal(5, new Evoker.EvokerAttackSpellGoal());
        this.f_21345_.addGoal(6, new Evoker.EvokerWololoSpellGoal());
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true).m_26146_(300));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false).m_26146_(300));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 12.0).add(Attributes.MAX_HEALTH, 24.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    public boolean isAlliedTo(Entity entity0) {
        if (entity0 == null) {
            return false;
        } else if (entity0 == this) {
            return true;
        } else if (super.m_7307_(entity0)) {
            return true;
        } else if (entity0 instanceof Vex) {
            return this.isAlliedTo(((Vex) entity0).getOwner());
        } else {
            return entity0 instanceof LivingEntity && ((LivingEntity) entity0).getMobType() == MobType.ILLAGER ? this.m_5647_() == null && entity0.getTeam() == null : false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.EVOKER_HURT;
    }

    void setWololoTarget(@Nullable Sheep sheep0) {
        this.wololoTarget = sheep0;
    }

    @Nullable
    Sheep getWololoTarget() {
        return this.wololoTarget;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public void applyRaidBuffs(int int0, boolean boolean1) {
    }

    class EvokerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity $$0 = Evoker.this.m_5448_();
            double $$1 = Math.min($$0.m_20186_(), Evoker.this.m_20186_());
            double $$2 = Math.max($$0.m_20186_(), Evoker.this.m_20186_()) + 1.0;
            float $$3 = (float) Mth.atan2($$0.m_20189_() - Evoker.this.m_20189_(), $$0.m_20185_() - Evoker.this.m_20185_());
            if (Evoker.this.m_20280_($$0) < 9.0) {
                for (int $$4 = 0; $$4 < 5; $$4++) {
                    float $$5 = $$3 + (float) $$4 * (float) Math.PI * 0.4F;
                    this.createSpellEntity(Evoker.this.m_20185_() + (double) Mth.cos($$5) * 1.5, Evoker.this.m_20189_() + (double) Mth.sin($$5) * 1.5, $$1, $$2, $$5, 0);
                }
                for (int $$6 = 0; $$6 < 8; $$6++) {
                    float $$7 = $$3 + (float) $$6 * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
                    this.createSpellEntity(Evoker.this.m_20185_() + (double) Mth.cos($$7) * 2.5, Evoker.this.m_20189_() + (double) Mth.sin($$7) * 2.5, $$1, $$2, $$7, 3);
                }
            } else {
                for (int $$8 = 0; $$8 < 16; $$8++) {
                    double $$9 = 1.25 * (double) ($$8 + 1);
                    int $$10 = 1 * $$8;
                    this.createSpellEntity(Evoker.this.m_20185_() + (double) Mth.cos($$3) * $$9, Evoker.this.m_20189_() + (double) Mth.sin($$3) * $$9, $$1, $$2, $$3, $$10);
                }
            }
        }

        private void createSpellEntity(double double0, double double1, double double2, double double3, float float4, int int5) {
            BlockPos $$6 = BlockPos.containing(double0, double3, double1);
            boolean $$7 = false;
            double $$8 = 0.0;
            do {
                BlockPos $$9 = $$6.below();
                BlockState $$10 = Evoker.this.m_9236_().getBlockState($$9);
                if ($$10.m_60783_(Evoker.this.m_9236_(), $$9, Direction.UP)) {
                    if (!Evoker.this.m_9236_().m_46859_($$6)) {
                        BlockState $$11 = Evoker.this.m_9236_().getBlockState($$6);
                        VoxelShape $$12 = $$11.m_60812_(Evoker.this.m_9236_(), $$6);
                        if (!$$12.isEmpty()) {
                            $$8 = $$12.max(Direction.Axis.Y);
                        }
                    }
                    $$7 = true;
                    break;
                }
                $$6 = $$6.below();
            } while ($$6.m_123342_() >= Mth.floor(double2) - 1);
            if ($$7) {
                Evoker.this.m_9236_().m_7967_(new EvokerFangs(Evoker.this.m_9236_(), double0, (double) $$6.m_123342_() + $$8, double1, float4, int5, Evoker.this));
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.FANGS;
        }
    }

    class EvokerCastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {

        @Override
        public void tick() {
            if (Evoker.this.m_5448_() != null) {
                Evoker.this.m_21563_().setLookAt(Evoker.this.m_5448_(), (float) Evoker.this.m_8085_(), (float) Evoker.this.m_8132_());
            } else if (Evoker.this.getWololoTarget() != null) {
                Evoker.this.m_21563_().setLookAt(Evoker.this.getWololoTarget(), (float) Evoker.this.m_8085_(), (float) Evoker.this.m_8132_());
            }
        }
    }

    class EvokerSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        private final TargetingConditions vexCountTargeting = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight().ignoreInvisibilityTesting();

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                int $$0 = Evoker.this.m_9236_().m_45971_(Vex.class, this.vexCountTargeting, Evoker.this, Evoker.this.m_20191_().inflate(16.0)).size();
                return Evoker.this.f_19796_.nextInt(8) + 1 > $$0;
            }
        }

        @Override
        protected int getCastingTime() {
            return 100;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected void performSpellCasting() {
            ServerLevel $$0 = (ServerLevel) Evoker.this.m_9236_();
            for (int $$1 = 0; $$1 < 3; $$1++) {
                BlockPos $$2 = Evoker.this.m_20183_().offset(-2 + Evoker.this.f_19796_.nextInt(5), 1, -2 + Evoker.this.f_19796_.nextInt(5));
                Vex $$3 = EntityType.VEX.create(Evoker.this.m_9236_());
                if ($$3 != null) {
                    $$3.m_20035_($$2, 0.0F, 0.0F);
                    $$3.finalizeSpawn($$0, Evoker.this.m_9236_().getCurrentDifficultyAt($$2), MobSpawnType.MOB_SUMMONED, null, null);
                    $$3.setOwner(Evoker.this);
                    $$3.setBoundOrigin($$2);
                    $$3.setLimitedLife(20 * (30 + Evoker.this.f_19796_.nextInt(90)));
                    $$0.m_47205_($$3);
                }
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
        }
    }

    public class EvokerWololoSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        private final TargetingConditions wololoTargeting = TargetingConditions.forNonCombat().range(16.0).selector(p_32710_ -> ((Sheep) p_32710_).getColor() == DyeColor.BLUE);

        @Override
        public boolean canUse() {
            if (Evoker.this.m_5448_() != null) {
                return false;
            } else if (Evoker.this.m_33736_()) {
                return false;
            } else if (Evoker.this.f_19797_ < this.f_33775_) {
                return false;
            } else if (!Evoker.this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return false;
            } else {
                List<Sheep> $$0 = Evoker.this.m_9236_().m_45971_(Sheep.class, this.wololoTargeting, Evoker.this, Evoker.this.m_20191_().inflate(16.0, 4.0, 16.0));
                if ($$0.isEmpty()) {
                    return false;
                } else {
                    Evoker.this.setWololoTarget((Sheep) $$0.get(Evoker.this.f_19796_.nextInt($$0.size())));
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return Evoker.this.getWololoTarget() != null && this.f_33774_ > 0;
        }

        @Override
        public void stop() {
            super.m_8041_();
            Evoker.this.setWololoTarget(null);
        }

        @Override
        protected void performSpellCasting() {
            Sheep $$0 = Evoker.this.getWololoTarget();
            if ($$0 != null && $$0.m_6084_()) {
                $$0.setColor(DyeColor.RED);
            }
        }

        @Override
        protected int getCastWarmupTime() {
            return 40;
        }

        @Override
        protected int getCastingTime() {
            return 60;
        }

        @Override
        protected int getCastingInterval() {
            return 140;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_WOLOLO;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.WOLOLO;
        }
    }
}