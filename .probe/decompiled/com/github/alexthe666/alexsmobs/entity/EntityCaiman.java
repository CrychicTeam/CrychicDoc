package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockReptileEgg;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.CaimanAIBellow;
import com.github.alexthe666.alexsmobs.entity.ai.CaimanAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwnerWater;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public class EntityCaiman extends TamableAnimal implements ISemiAquatic, IFollower {

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityCaiman.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityCaiman.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BELLOWING = SynchedEntityData.defineId(EntityCaiman.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> HELD_MOB_ID = SynchedEntityData.defineId(EntityCaiman.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityCaiman.class, EntityDataSerializers.BOOLEAN);

    public float prevSitProgress;

    public float sitProgress;

    public float prevHoldProgress;

    public float holdProgress;

    public float prevSwimProgress;

    public float swimProgress;

    public float prevVibrateProgress;

    public float vibrateProgress;

    private int swimTimer = -1000;

    public int bellowCooldown = 100 + this.f_19796_.nextInt(1000);

    private boolean isLandNavigator;

    public boolean tameAttackFlag = false;

    public EntityCaiman(EntityType type, Level level) {
        super(type, level);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(BELLOWING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(HAS_EGG, false);
        this.f_19804_.define(HELD_MOB_ID, -1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new EntityCaiman.MateGoal(this, 1.0));
        this.f_21345_.addGoal(1, new EntityCaiman.LayEggGoal(this, 1.0));
        this.f_21345_.addGoal(2, new CaimanAIMelee(this));
        this.f_21345_.addGoal(3, new BreathAirGoal(this));
        this.f_21345_.addGoal(4, new TameableAIFollowOwnerWater(this, 1.1, 4.0F, 2.0F, false));
        this.f_21345_.addGoal(5, new MeleeAttackGoal(this, 1.2F, false));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.1, Ingredient.of(AMItemRegistry.COOKED_CATFISH.get(), AMItemRegistry.RAW_CATFISH.get()), false));
        this.f_21345_.addGoal(7, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(7, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(8, new CaimanAIBellow(this));
        this.f_21345_.addGoal(9, new SemiAquaticAIRandomSwimming(this, 1.0, 30));
        this.f_21345_.addGoal(10, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(11, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this).m_26044_(new Class[0]));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this) {

            @Override
            public void start() {
                super.start();
                EntityCaiman.this.tameAttackFlag = true;
            }

            @Override
            public void stop() {
                super.start();
                EntityCaiman.this.tameAttackFlag = false;
            }
        });
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this) {

            @Override
            public void start() {
                super.start();
                EntityCaiman.this.tameAttackFlag = true;
            }

            @Override
            public void stop() {
                super.start();
                EntityCaiman.this.tameAttackFlag = false;
            }
        });
        this.f_21346_.addGoal(5, new EntityAINearestTarget3D(this, LivingEntity.class, 180, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.CAIMAN_TARGETS)) {

            @Override
            public boolean canUse() {
                return !EntityCaiman.this.m_6162_() && !EntityCaiman.this.m_21824_() && super.m_8036_();
            }
        });
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.caimanSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static <T extends Mob> boolean canCaimanSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.below());
        return blockstate.m_60713_(Blocks.MUD) || blockstate.m_60713_(Blocks.MUDDY_MANGROVE_ROOTS) || blockstate.m_204336_(AMTagRegistry.CROCODILE_SPAWNS);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMItemRegistry.RAW_CATFISH.get()) || stack.is(AMItemRegistry.COOKED_CATFISH.get());
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AquaticMoveController(this, 1.1F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_6162_() ? AMSoundRegistry.CROCODILE_BABY.get() : AMSoundRegistry.CAIMAN_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.CAIMAN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.CAIMAN_HURT.get();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevHoldProgress = this.holdProgress;
        this.prevSwimProgress = this.swimProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevVibrateProgress = this.vibrateProgress;
        boolean ground = !this.m_20072_();
        boolean bellowing = this.isBellowing();
        boolean grabbing = this.getHeldMobId() != -1;
        boolean sitting = this.isSitting() && ground;
        if (!ground && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (ground && this.swimProgress > 0.0F) {
            this.swimProgress--;
        }
        if (!ground && this.swimProgress < 5.0F) {
            this.swimProgress++;
        }
        if (bellowing && this.vibrateProgress < 5.0F) {
            this.vibrateProgress++;
        }
        if (!bellowing && this.vibrateProgress > 0.0F) {
            this.vibrateProgress--;
        }
        if (sitting && this.sitProgress < 5.0F) {
            this.sitProgress++;
        }
        if (!sitting && this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (grabbing && this.holdProgress < 5.0F) {
            this.holdProgress += 2.5F;
        }
        if (!grabbing && this.holdProgress > 0.0F) {
            this.holdProgress -= 2.5F;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20069_()) {
                this.swimTimer++;
            } else {
                if (this.isBellowing()) {
                    this.setBellowing(false);
                }
                this.swimTimer--;
            }
            if (this.m_5448_() instanceof WaterAnimal && !this.m_21824_()) {
                WaterAnimal fish = (WaterAnimal) this.m_5448_();
                CompoundTag fishNbt = new CompoundTag();
                fish.m_7380_(fishNbt);
                fishNbt.putString("DeathLootTable", BuiltInLootTables.EMPTY.toString());
                fish.m_7378_(fishNbt);
            }
        } else if (this.m_20072_() && this.isBellowing()) {
            int particles = 4 + this.m_217043_().nextInt(3);
            for (int i = 0; i <= particles; i++) {
                Vec3 particleVec = new Vec3(0.0, 0.0, 1.0).yRot((float) i / (float) particles * (float) Math.PI * 2.0F).add(this.m_20182_());
                double particleY = this.m_20191_().minY + this.getFluidTypeHeight(ForgeMod.WATER_TYPE.get());
                this.m_9236_().addParticle(ParticleTypes.SPLASH, particleVec.x, particleY, particleVec.z, 0.0, 0.3F, 0.0);
            }
        }
        if (this.bellowCooldown > 0) {
            this.bellowCooldown--;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (this.m_21824_() && itemstack.is(ItemTags.FISHES) && this.m_21223_() < this.m_21233_()) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
            this.m_5634_(5.0F);
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_(player) && !this.isFood(itemstack)) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            } else {
                return type;
            }
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ARMOR, 8.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public void setHeldMobId(int i) {
        this.f_19804_.set(HELD_MOB_ID, i);
    }

    public int getHeldMobId() {
        return this.f_19804_.get(HELD_MOB_ID);
    }

    public boolean hasEgg() {
        return this.f_19804_.get(HAS_EGG);
    }

    private void setHasEgg(boolean hasEgg) {
        this.f_19804_.set(HAS_EGG, hasEgg);
    }

    public Entity getHeldMob() {
        int id = this.getHeldMobId();
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isBellowing() {
        return this.f_19804_.get(BELLOWING);
    }

    public void setBellowing(boolean bellowing) {
        this.f_19804_.set(BELLOWING, bellowing);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isSitting()) {
            super.m_7023_(Vec3.ZERO);
        } else if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    public void calculateEntityAnimation(LivingEntity living, boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.shouldLeaveWater() && this.swimTimer <= -1000 || this.bellowCooldown == 0;
    }

    @Override
    public boolean shouldLeaveWater() {
        LivingEntity target = this.m_5448_();
        return target != null && !target.m_20069_() ? true : this.swimTimer > 600 && !this.isBellowing();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isSitting();
    }

    @Override
    public int getWaterSearchRange() {
        return 12;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return null;
    }

    public Vec3 getShakePreyPos() {
        Vec3 jaw = new Vec3(0.0, -0.1, 1.0);
        Vec3 head = jaw.xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_6080_() * (float) (Math.PI / 180.0));
        return this.m_146892_().add(head);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getHeldMobId() == -1) {
            super.m_5997_(x, y, z);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HasEgg", this.hasEgg());
        compound.putBoolean("Bellowing", this.isBellowing());
        compound.putInt("CaimanCommand", this.getCommand());
        compound.putBoolean("CaimanSitting", this.m_21827_());
        compound.putInt("BellowCooldown", this.bellowCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setHasEgg(compound.getBoolean("HasEgg"));
        this.setBellowing(compound.getBoolean("Bellowing"));
        this.bellowCooldown = compound.getInt("BellowCooldown");
        this.setCommand(compound.getInt("CaimanCommand"));
        this.setOrderedToSit(compound.getBoolean("CaimanSitting"));
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    static class LayEggGoal extends MoveToBlockGoal {

        private final EntityCaiman caiman;

        private int digTime;

        LayEggGoal(EntityCaiman caiman, double speedIn) {
            super(caiman, speedIn, 16);
            this.caiman = caiman;
        }

        @Override
        public void stop() {
            this.digTime = 0;
        }

        @Override
        public boolean canUse() {
            return this.caiman.hasEgg() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.caiman.hasEgg();
        }

        @Override
        public double acceptedDistance() {
            return (double) this.caiman.m_20205_() + 0.5;
        }

        @Override
        public void tick() {
            super.tick();
            BlockPos blockpos = this.caiman.m_20183_();
            this.caiman.swimTimer = 1000;
            if (!this.caiman.m_20069_() && this.m_25625_()) {
                Level world = this.caiman.m_9236_();
                this.caiman.m_146850_(GameEvent.BLOCK_PLACE);
                world.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                world.setBlock(this.f_25602_.above(), (BlockState) AMBlockRegistry.CAIMAN_EGG.get().defaultBlockState().m_61124_(BlockReptileEgg.EGGS, this.caiman.f_19796_.nextInt(1) + 3), 3);
                this.caiman.setHasEgg(false);
                this.caiman.m_27601_(600);
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            return worldIn.isEmptyBlock(pos.above()) && BlockReptileEgg.isProperHabitat(worldIn, pos);
        }
    }

    static class MateGoal extends BreedGoal {

        private final EntityCaiman caiman;

        MateGoal(EntityCaiman caiman, double speedIn) {
            super(caiman, speedIn);
            this.caiman = caiman;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.caiman.hasEgg();
        }

        @Override
        protected void breed() {
            ServerPlayer serverplayerentity = this.f_25113_.getLoveCause();
            if (serverplayerentity == null && this.f_25115_.getLoveCause() != null) {
                serverplayerentity = this.f_25115_.getLoveCause();
            }
            if (serverplayerentity != null) {
                serverplayerentity.m_36220_(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.f_25113_, this.f_25115_, this.f_25113_);
            }
            this.caiman.setHasEgg(true);
            this.f_25113_.resetLove();
            this.f_25115_.resetLove();
            this.f_25113_.m_146762_(6000);
            this.f_25115_.m_146762_(6000);
            RandomSource random = this.f_25113_.m_217043_();
            if (this.f_25114_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.f_25114_.m_7967_(new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), random.nextInt(7) + 1));
            }
        }
    }
}