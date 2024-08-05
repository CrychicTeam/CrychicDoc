package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.message.MessageStartDancing;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityRainFrog extends Animal implements ITargetsDroppedItems, IDancingMob {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityRainFrog.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> STANCE_TIME = SynchedEntityData.defineId(EntityRainFrog.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ATTACK_TIME = SynchedEntityData.defineId(EntityRainFrog.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DANCE_TIME = SynchedEntityData.defineId(EntityRainFrog.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> BURROWED = SynchedEntityData.defineId(EntityRainFrog.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DISTURBED = SynchedEntityData.defineId(EntityRainFrog.class, EntityDataSerializers.BOOLEAN);

    public float burrowProgress;

    public float prevBurrowProgress;

    public float danceProgress;

    public float prevDanceProgress;

    public float attackProgress;

    public float prevAttackProgress;

    public float stanceProgress;

    public float prevStanceProgress;

    private int burrowCooldown = 0;

    private int weatherCooldown = 0;

    private boolean isJukeboxing;

    private BlockPos jukeboxPosition;

    protected EntityRainFrog(EntityType<? extends Animal> rainFrog, Level lvl) {
        super(rainFrog, lvl);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(AMTagRegistry.INSECT_ITEMS), false));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, EntityRattlesnake.class, 9.0F, 1.3, 1.0));
        this.f_21345_.addGoal(5, new EntityRainFrog.AIBurrow());
        this.f_21345_.addGoal(6, new AnimalAIWanderRanged(this, 20, 1.0, 10, 7));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false));
    }

    public static boolean canRainFrogSpawn(EntityType animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(BlockTags.SAND);
        return spawnBlock && worldIn.getLevelData() != null && (worldIn.getLevelData().isThundering() || worldIn.getLevelData().isRaining());
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.rainFrogSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public boolean isBurrowed() {
        return this.f_19804_.get(BURROWED);
    }

    public void setBurrowed(boolean burrowed) {
        this.f_19804_.set(BURROWED, burrowed);
    }

    public boolean isDisturbed() {
        return this.f_19804_.get(DISTURBED);
    }

    public void setDisturbed(boolean burrowed) {
        this.f_19804_.set(DISTURBED, burrowed);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getStanceTime() {
        return this.f_19804_.get(STANCE_TIME);
    }

    public void setStanceTime(int stanceTime) {
        this.f_19804_.set(STANCE_TIME, stanceTime);
    }

    public int getAttackTime() {
        return this.f_19804_.get(ATTACK_TIME);
    }

    public void setAttackTime(int attackTime) {
        this.f_19804_.set(ATTACK_TIME, attackTime);
    }

    public int getDanceTime() {
        return this.f_19804_.get(DANCE_TIME);
    }

    public void setDanceTime(int danceTime) {
        this.f_19804_.set(DANCE_TIME, danceTime);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        EntityRainFrog frog = AMEntityRegistry.RAIN_FROG.get().create(p_241840_1_);
        frog.setVariant(this.getVariant());
        frog.setDisturbed(true);
        return frog;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(STANCE_TIME, 0);
        this.f_19804_.define(ATTACK_TIME, 0);
        this.f_19804_.define(DANCE_TIME, 0);
        this.f_19804_.define(BURROWED, false);
        this.f_19804_.define(DISTURBED, false);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevBurrowProgress = this.burrowProgress;
        this.prevDanceProgress = this.danceProgress;
        this.prevAttackProgress = this.attackProgress;
        this.prevStanceProgress = this.stanceProgress;
        if (this.isBurrowed()) {
            if (this.burrowProgress < 5.0F) {
                this.burrowProgress += 0.5F;
            }
        } else if (this.burrowProgress > 0.0F) {
            this.burrowProgress -= 0.5F;
        }
        if (this.burrowCooldown > 0) {
            this.burrowCooldown--;
        }
        if (this.getStanceTime() > 0) {
            this.setStanceTime(this.getStanceTime() - 1);
            if (this.stanceProgress < 5.0F) {
                this.stanceProgress++;
            }
        } else if (this.stanceProgress > 0.0F) {
            this.stanceProgress--;
        }
        if (this.getAttackTime() > 0) {
            this.setAttackTime(this.getAttackTime() - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress += 2.5F;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress -= 0.5F;
        }
        boolean dancing = this.getDanceTime() > 0 || this.isJukeboxing;
        if (dancing) {
            if (this.danceProgress < 5.0F) {
                this.danceProgress++;
            }
        } else if (this.danceProgress > 0.0F) {
            this.danceProgress--;
        }
        if (this.getDanceTime() > 0) {
            this.setBurrowed(false);
            this.setDanceTime(this.getDanceTime() - 1);
            if (this.getDanceTime() == 1 && this.weatherCooldown <= 0 && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
                this.changeWeather();
            }
        }
        if (this.weatherCooldown > 0) {
            this.weatherCooldown--;
        }
        if (this.jukeboxPosition == null || !this.jukeboxPosition.m_203195_(this.m_20182_(), 15.0) || !this.m_9236_().getBlockState(this.jukeboxPosition).m_60713_(Blocks.JUKEBOX)) {
            this.isJukeboxing = false;
            this.setDanceTime(0);
            this.jukeboxPosition = null;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev && source.getDirectEntity() instanceof LivingEntity) {
            if (this.getStanceTime() <= 0) {
                this.setStanceTime(30 + this.f_19796_.nextInt(20));
            }
            this.setBurrowed(false);
        }
        return prev;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.isDisturbed();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public boolean isSleeping() {
        return this.isBurrowed();
    }

    public void calculateEntityAnimation(LivingEntity mob, boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 128.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.f_19796_.nextInt(3));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setDisturbed(compound.getBoolean("Disturbed"));
        this.setVariant(compound.getInt("Variant"));
        this.weatherCooldown = compound.getInt("WeatherCooldown");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Disturbed", this.isDisturbed());
        compound.putInt("Variant", this.getVariant());
        compound.putInt("WeatherCooldown", this.weatherCooldown);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (item instanceof ShovelItem && (this.isBurrowed() || !this.isDisturbed()) && !this.m_9236_().isClientSide) {
            this.f_21363_ = 1000;
            if (!player.isCreative()) {
                itemstack.hurt(1, this.m_217043_(), player instanceof ServerPlayer ? (ServerPlayer) player : null);
            }
            this.setStanceTime(20 + this.f_19796_.nextInt(30));
            this.setBurrowed(false);
            this.setDisturbed(true);
            this.burrowCooldown = this.burrowCooldown + 150 + this.f_19796_.nextInt(120);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SAND_BREAK, this.m_6121_(), this.m_6100_());
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.isBurrowed() && this.getDanceTime() <= 0) {
            if (this.m_21515_() && this.m_20069_()) {
                this.m_19920_(this.m_6113_(), travelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.9));
                if (this.m_5448_() == null) {
                    this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
                }
            } else {
                super.m_7023_(travelVector);
            }
        } else {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            travelVector = Vec3.ZERO;
            super.m_7023_(travelVector);
        }
    }

    @Override
    public void onFindTarget(ItemEntity e) {
        this.setBurrowed(false);
        this.burrowCooldown += 50;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.setAttackTime(10);
        this.m_5634_(2.0F);
    }

    private void changeWeather() {
        int time = 24000 + 1200 * this.f_19796_.nextInt(10);
        int type = 0;
        if (!this.m_9236_().isRaining()) {
            type = this.f_19796_.nextInt(1) + 1;
        }
        if (this.m_9236_() instanceof ServerLevel serverLevel) {
            if (type == 0) {
                serverLevel.setWeatherParameters(time, 0, false, false);
            } else {
                serverLevel.setWeatherParameters(0, time, true, type == 2);
            }
        }
        this.weatherCooldown = time + 24000;
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        AlexsMobs.sendMSGToServer(new MessageStartDancing(this.m_19879_(), isPartying, pos));
        if (isPartying) {
            this.setJukeboxPos(pos);
        } else {
            this.setJukeboxPos(null);
        }
    }

    @Override
    public void setDancing(boolean dancing) {
        this.setDanceTime(dancing && this.weatherCooldown == 0 ? 240 + this.f_19796_.nextInt(200) : 0);
    }

    @Override
    public void setJukeboxPos(BlockPos pos) {
        this.jukeboxPosition = pos;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getStanceTime() > 0 ? AMSoundRegistry.RAIN_FROG_HURT.get() : AMSoundRegistry.RAIN_FROG_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return this.getStanceTime() > 0 ? 10 : 80;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.RAIN_FROG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.RAIN_FROG_HURT.get();
    }

    private class AIBurrow extends Goal {

        private BlockPos sand = null;

        private int burrowedTime = 0;

        public AIBurrow() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!EntityRainFrog.this.isBurrowed() && EntityRainFrog.this.burrowCooldown == 0 && EntityRainFrog.this.f_19796_.nextInt(200) == 0) {
                this.burrowedTime = 0;
                this.sand = this.findSand();
                return this.sand != null;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.burrowedTime < 300;
        }

        public BlockPos findSand() {
            BlockPos blockpos = null;
            for (BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(EntityRainFrog.this.m_20185_() - 4.0), Mth.floor(EntityRainFrog.this.m_20186_() - 1.0), Mth.floor(EntityRainFrog.this.m_20189_() - 4.0), Mth.floor(EntityRainFrog.this.m_20185_() + 4.0), EntityRainFrog.this.m_146904_(), Mth.floor(EntityRainFrog.this.m_20189_() + 4.0))) {
                if (EntityRainFrog.this.m_9236_().getBlockState(blockpos1).m_204336_(BlockTags.SAND)) {
                    blockpos = blockpos1;
                    break;
                }
            }
            return blockpos;
        }

        @Override
        public void tick() {
            if (EntityRainFrog.this.isBurrowed()) {
                this.burrowedTime++;
                if (!EntityRainFrog.this.m_20075_().m_204336_(BlockTags.SAND)) {
                    EntityRainFrog.this.setBurrowed(false);
                }
            } else if (this.sand != null) {
                EntityRainFrog.this.m_21573_().moveTo((double) ((float) this.sand.m_123341_() + 0.5F), (double) ((float) this.sand.m_123342_() + 1.0F), (double) ((float) this.sand.m_123343_() + 0.5F), 1.0);
                if (EntityRainFrog.this.m_20075_().m_204336_(BlockTags.SAND)) {
                    EntityRainFrog.this.setBurrowed(true);
                    EntityRainFrog.this.m_21573_().stop();
                    this.sand = null;
                } else {
                    EntityRainFrog.this.setBurrowed(false);
                }
            }
        }

        @Override
        public void stop() {
            EntityRainFrog.this.setBurrowed(false);
            EntityRainFrog.this.burrowCooldown = 120 + EntityRainFrog.this.f_19796_.nextInt(1200);
            this.sand = null;
        }
    }
}