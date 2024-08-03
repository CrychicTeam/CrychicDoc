package net.minecraft.world.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Illusioner extends SpellcasterIllager implements RangedAttackMob {

    private static final int NUM_ILLUSIONS = 4;

    private static final int ILLUSION_TRANSITION_TICKS = 3;

    private static final int ILLUSION_SPREAD = 3;

    private int clientSideIllusionTicks;

    private final Vec3[][] clientSideIllusionOffsets;

    public Illusioner(EntityType<? extends Illusioner> entityTypeExtendsIllusioner0, Level level1) {
        super(entityTypeExtendsIllusioner0, level1);
        this.f_21364_ = 5;
        this.clientSideIllusionOffsets = new Vec3[2][4];
        for (int $$2 = 0; $$2 < 4; $$2++) {
            this.clientSideIllusionOffsets[0][$$2] = Vec3.ZERO;
            this.clientSideIllusionOffsets[1][$$2] = Vec3.ZERO;
        }
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SpellcasterIllager.SpellcasterCastingSpellGoal());
        this.f_21345_.addGoal(4, new Illusioner.IllusionerMirrorSpellGoal());
        this.f_21345_.addGoal(5, new Illusioner.IllusionerBlindnessSpellGoal());
        this.f_21345_.addGoal(6, new RangedBowAttackGoal<>(this, 0.5, 20, 15.0F));
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true).m_26146_(300));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false).m_26146_(300));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, false).m_26146_(300));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 18.0).add(Attributes.MAX_HEALTH, 32.0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        this.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(3.0, 0.0, 3.0);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_9236_().isClientSide && this.m_20145_()) {
            this.clientSideIllusionTicks--;
            if (this.clientSideIllusionTicks < 0) {
                this.clientSideIllusionTicks = 0;
            }
            if (this.f_20916_ == 1 || this.f_19797_ % 1200 == 0) {
                this.clientSideIllusionTicks = 3;
                float $$0 = -6.0F;
                int $$1 = 13;
                for (int $$2 = 0; $$2 < 4; $$2++) {
                    this.clientSideIllusionOffsets[0][$$2] = this.clientSideIllusionOffsets[1][$$2];
                    this.clientSideIllusionOffsets[1][$$2] = new Vec3((double) (-6.0F + (float) this.f_19796_.nextInt(13)) * 0.5, (double) Math.max(0, this.f_19796_.nextInt(6) - 4), (double) (-6.0F + (float) this.f_19796_.nextInt(13)) * 0.5);
                }
                for (int $$3 = 0; $$3 < 16; $$3++) {
                    this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20208_(0.5), this.m_20187_(), this.m_20246_(0.5), 0.0, 0.0, 0.0);
                }
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ILLUSIONER_MIRROR_MOVE, this.m_5720_(), 1.0F, 1.0F, false);
            } else if (this.f_20916_ == this.f_20917_ - 1) {
                this.clientSideIllusionTicks = 3;
                for (int $$4 = 0; $$4 < 4; $$4++) {
                    this.clientSideIllusionOffsets[0][$$4] = this.clientSideIllusionOffsets[1][$$4];
                    this.clientSideIllusionOffsets[1][$$4] = new Vec3(0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    public Vec3[] getIllusionOffsets(float float0) {
        if (this.clientSideIllusionTicks <= 0) {
            return this.clientSideIllusionOffsets[1];
        } else {
            double $$1 = (double) (((float) this.clientSideIllusionTicks - float0) / 3.0F);
            $$1 = Math.pow($$1, 0.25);
            Vec3[] $$2 = new Vec3[4];
            for (int $$3 = 0; $$3 < 4; $$3++) {
                $$2[$$3] = this.clientSideIllusionOffsets[1][$$3].scale(1.0 - $$1).add(this.clientSideIllusionOffsets[0][$$3].scale($$1));
            }
            return $$2;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity0) {
        if (super.m_7307_(entity0)) {
            return true;
        } else {
            return entity0 instanceof LivingEntity && ((LivingEntity) entity0).getMobType() == MobType.ILLAGER ? this.m_5647_() == null && entity0.getTeam() == null : false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.ILLUSIONER_HURT;
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    @Override
    public void applyRaidBuffs(int int0, boolean boolean1) {
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        ItemStack $$2 = this.m_6298_(this.m_21120_(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = ProjectileUtil.getMobArrow(this, $$2, float1);
        double $$4 = livingEntity0.m_20185_() - this.m_20185_();
        double $$5 = livingEntity0.m_20227_(0.3333333333333333) - $$3.m_20186_();
        double $$6 = livingEntity0.m_20189_() - this.m_20189_();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6);
        $$3.shoot($$4, $$5 + $$7 * 0.2F, $$6, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
        this.m_5496_(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        this.m_9236_().m_7967_($$3);
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.m_33736_()) {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        } else {
            return this.m_5912_() ? AbstractIllager.IllagerArmPose.BOW_AND_ARROW : AbstractIllager.IllagerArmPose.CROSSED;
        }
    }

    class IllusionerBlindnessSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        private int lastTargetId;

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (Illusioner.this.m_5448_() == null) {
                return false;
            } else {
                return Illusioner.this.m_5448_().m_19879_() == this.lastTargetId ? false : Illusioner.this.m_9236_().getCurrentDifficultyAt(Illusioner.this.m_20183_()).isHarderThan((float) Difficulty.NORMAL.ordinal());
            }
        }

        @Override
        public void start() {
            super.start();
            LivingEntity $$0 = Illusioner.this.m_5448_();
            if ($$0 != null) {
                this.lastTargetId = $$0.m_19879_();
            }
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 180;
        }

        @Override
        protected void performSpellCasting() {
            Illusioner.this.m_5448_().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400), Illusioner.this);
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.BLINDNESS;
        }
    }

    class IllusionerMirrorSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        @Override
        public boolean canUse() {
            return !super.canUse() ? false : !Illusioner.this.m_21023_(MobEffects.INVISIBILITY);
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected void performSpellCasting() {
            Illusioner.this.m_7292_(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
        }

        @Nullable
        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.DISAPPEAR;
        }
    }
}