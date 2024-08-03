package net.minecraft.world.entity.animal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Ocelot extends Animal {

    public static final double CROUCH_SPEED_MOD = 0.6;

    public static final double WALK_SPEED_MOD = 0.8;

    public static final double SPRINT_SPEED_MOD = 1.33;

    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);

    private static final EntityDataAccessor<Boolean> DATA_TRUSTING = SynchedEntityData.defineId(Ocelot.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private Ocelot.OcelotAvoidEntityGoal<Player> ocelotAvoidPlayersGoal;

    @Nullable
    private Ocelot.OcelotTemptGoal temptGoal;

    public Ocelot(EntityType<? extends Ocelot> entityTypeExtendsOcelot0, Level level1) {
        super(entityTypeExtendsOcelot0, level1);
        this.reassessTrustingGoals();
    }

    boolean isTrusting() {
        return this.f_19804_.get(DATA_TRUSTING);
    }

    private void setTrusting(boolean boolean0) {
        this.f_19804_.set(DATA_TRUSTING, boolean0);
        this.reassessTrustingGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("Trusting", this.isTrusting());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setTrusting(compoundTag0.getBoolean("Trusting"));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_TRUSTING, false);
    }

    @Override
    protected void registerGoals() {
        this.temptGoal = new Ocelot.OcelotTemptGoal(this, 0.6, TEMPT_INGREDIENT, true);
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(3, this.temptGoal);
        this.f_21345_.addGoal(7, new LeapAtTargetGoal(this, 0.3F));
        this.f_21345_.addGoal(8, new OcelotAttackGoal(this));
        this.f_21345_.addGoal(9, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 0.8, 1.0000001E-5F));
        this.f_21345_.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Chicken.class, false));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Turtle.class, 10, false, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public void customServerAiStep() {
        if (this.m_21566_().hasWanted()) {
            double $$0 = this.m_21566_().getSpeedModifier();
            if ($$0 == 0.6) {
                this.m_20124_(Pose.CROUCHING);
                this.m_6858_(false);
            } else if ($$0 == 1.33) {
                this.m_20124_(Pose.STANDING);
                this.m_6858_(true);
            } else {
                this.m_20124_(Pose.STANDING);
                this.m_6858_(false);
            }
        } else {
            this.m_20124_(Pose.STANDING);
            this.m_6858_(false);
        }
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.isTrusting() && this.f_19797_ > 2400;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.OCELOT_AMBIENT;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.OCELOT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.OCELOT_DEATH;
    }

    private float getAttackDamage() {
        return (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        return entity0.hurt(this.m_269291_().mobAttack(this), this.getAttackDamage());
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ((this.temptGoal == null || this.temptGoal.m_25955_()) && !this.isTrusting() && this.isFood($$2) && player0.m_20280_(this) < 9.0) {
            this.m_142075_(player0, interactionHand1, $$2);
            if (!this.m_9236_().isClientSide) {
                if (this.f_19796_.nextInt(3) == 0) {
                    this.setTrusting(true);
                    this.spawnTrustingParticles(true);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 41);
                } else {
                    this.spawnTrustingParticles(false);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 40);
                }
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.mobInteract(player0, interactionHand1);
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 41) {
            this.spawnTrustingParticles(true);
        } else if (byte0 == 40) {
            this.spawnTrustingParticles(false);
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    private void spawnTrustingParticles(boolean boolean0) {
        ParticleOptions $$1 = ParticleTypes.HEART;
        if (!boolean0) {
            $$1 = ParticleTypes.SMOKE;
        }
        for (int $$2 = 0; $$2 < 7; $$2++) {
            double $$3 = this.f_19796_.nextGaussian() * 0.02;
            double $$4 = this.f_19796_.nextGaussian() * 0.02;
            double $$5 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle($$1, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), $$3, $$4, $$5);
        }
    }

    protected void reassessTrustingGoals() {
        if (this.ocelotAvoidPlayersGoal == null) {
            this.ocelotAvoidPlayersGoal = new Ocelot.OcelotAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8, 1.33);
        }
        this.f_21345_.removeGoal(this.ocelotAvoidPlayersGoal);
        if (!this.isTrusting()) {
            this.f_21345_.addGoal(4, this.ocelotAvoidPlayersGoal);
        }
    }

    @Nullable
    public Ocelot getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.OCELOT.create(serverLevel0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return TEMPT_INGREDIENT.test(itemStack0);
    }

    public static boolean checkOcelotSpawnRules(EntityType<Ocelot> entityTypeOcelot0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return randomSource4.nextInt(3) != 0;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        if (levelReader0.m_45784_(this) && !levelReader0.containsAnyLiquid(this.m_20191_())) {
            BlockPos $$1 = this.m_20183_();
            if ($$1.m_123342_() < levelReader0.getSeaLevel()) {
                return false;
            }
            BlockState $$2 = levelReader0.m_8055_($$1.below());
            if ($$2.m_60713_(Blocks.GRASS_BLOCK) || $$2.m_204336_(BlockTags.LEAVES)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AgeableMob.AgeableMobGroupData(1.0F);
        }
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double) (0.5F * this.m_20192_()), (double) (this.m_20205_() * 0.4F));
    }

    @Override
    public boolean isSteppingCarefully() {
        return this.m_6047_() || super.m_20161_();
    }

    static class OcelotAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

        private final Ocelot ocelot;

        public OcelotAvoidEntityGoal(Ocelot ocelot0, Class<T> classT1, float float2, double double3, double double4) {
            super(ocelot0, classT1, float2, double3, double4, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.ocelot = ocelot0;
        }

        @Override
        public boolean canUse() {
            return !this.ocelot.isTrusting() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.ocelot.isTrusting() && super.canContinueToUse();
        }
    }

    static class OcelotTemptGoal extends TemptGoal {

        private final Ocelot ocelot;

        public OcelotTemptGoal(Ocelot ocelot0, double double1, Ingredient ingredient2, boolean boolean3) {
            super(ocelot0, double1, ingredient2, boolean3);
            this.ocelot = ocelot0;
        }

        @Override
        protected boolean canScare() {
            return super.canScare() && !this.ocelot.isTrusting();
        }
    }
}