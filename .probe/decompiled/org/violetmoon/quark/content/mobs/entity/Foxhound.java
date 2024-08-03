package org.violetmoon.quark.content.mobs.entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BegGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.module.TinyPotatoModule;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.ai.FindPlaceToSleepGoal;
import org.violetmoon.quark.content.mobs.ai.SleepGoal;
import org.violetmoon.quark.content.mobs.module.FoxhoundModule;
import org.violetmoon.quark.content.tweaks.ai.WantLoveGoal;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class Foxhound extends Wolf implements Enemy {

    public static final ResourceLocation FOXHOUND_LOOT_TABLE = new ResourceLocation("quark", "entities/foxhound");

    private static final EntityDataAccessor<Boolean> TEMPTATION = SynchedEntityData.defineId(Foxhound.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_BLUE = SynchedEntityData.defineId(Foxhound.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TATERING = SynchedEntityData.defineId(Foxhound.class, EntityDataSerializers.BOOLEAN);

    private int timeUntilPotatoEmerges = 0;

    protected SleepGoal sleepGoal;

    public Foxhound(EntityType<? extends Foxhound> type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.LAVA, 1.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 1.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_30397_(DyeColor.ORANGE);
        this.f_19804_.define(TEMPTATION, false);
        this.f_19804_.define(IS_BLUE, false);
        this.f_19804_.define(TATERING, false);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public boolean isPersistenceRequired() {
        return super.m_21532_();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return this.m_21824_();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.m_21824_();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        Holder<Biome> biome = worldIn.m_204166_(BlockPos.containing(this.m_20182_()));
        if (biome.is(Biomes.SOUL_SAND_VALLEY.location())) {
            this.setBlue(true);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void tick() {
        super.tick();
        Pose pose = this.m_20089_();
        if (this.m_5803_()) {
            if (pose != Pose.SLEEPING) {
                this.m_20124_(Pose.SLEEPING);
            }
        } else if (pose == Pose.SLEEPING) {
            this.m_20124_(Pose.STANDING);
        }
        Level level = this.m_9236_();
        if (!level.isClientSide && level.m_46791_() == Difficulty.PEACEFUL && !this.m_21824_()) {
            this.m_146870_();
        } else {
            if (!level.isClientSide && this.timeUntilPotatoEmerges > 0) {
                if (--this.timeUntilPotatoEmerges == 0) {
                    this.setTatering(false);
                    ItemStack stack = new ItemStack(TinyPotatoModule.tiny_potato);
                    ItemNBTHelper.setBoolean(stack, "angery", true);
                    this.m_19983_(stack);
                    this.m_5496_(QuarkSounds.BLOCK_POTATO_HURT, 1.0F, 1.0F);
                } else if (!this.isTatering()) {
                    this.setTatering(true);
                }
            }
            if (this.m_5803_()) {
                Optional<BlockPos> sleepPos = this.m_21257_();
                if (sleepPos.isPresent()) {
                    BlockPos pos = (BlockPos) sleepPos.get();
                    if (this.m_20275_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5) > 1.0) {
                        this.m_5796_();
                    }
                }
                AABB aabb = this.m_20191_();
                if (aabb.getYsize() < 0.21) {
                    this.m_20011_(new AABB(aabb.minX - 0.2, aabb.minY, aabb.minZ - 0.2, aabb.maxX + 0.2, aabb.maxY + 0.5, aabb.maxZ + 0.2));
                }
            }
            if (WantLoveGoal.needsPets(this)) {
                Entity owner = this.m_269323_();
                if (owner != null && owner.distanceToSqr(this) < 1.0 && !owner.isInWater() && !owner.fireImmune() && (!(owner instanceof Player player) || !player.getAbilities().invulnerable)) {
                    owner.setSecondsOnFire(5);
                }
            }
            Vec3 pos = this.m_20182_();
            if (level.isClientSide && !this.m_6162_() ^ this.f_19796_.nextBoolean()) {
                SimpleParticleType particle = ParticleTypes.FLAME;
                if (this.m_5803_()) {
                    particle = ParticleTypes.SMOKE;
                } else if (this.isBlue()) {
                    particle = ParticleTypes.SOUL_FIRE_FLAME;
                }
                level.addParticle(particle, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), 0.0, 0.0, 0.0);
                if (this.isTatering() && this.f_19796_.nextDouble() < 0.1) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), 0.0, 0.0, 0.0);
                    level.playLocalSound(pos.x, pos.y, pos.z, QuarkSounds.ENTITY_FOXHOUND_CRACKLE, this.m_5720_(), 1.0F, 1.0F, false);
                }
            }
            if (this.m_21824_() && FoxhoundModule.foxhoundsSpeedUpFurnaces) {
                BlockPos below = this.m_20183_().below();
                if (level.getBlockEntity(below) instanceof AbstractFurnaceBlockEntity furnace) {
                    int cookTime = furnace.cookingProgress;
                    if (cookTime > 0 && cookTime % 3 == 0) {
                        List<Foxhound> foxhounds = level.m_6443_(Foxhound.class, new AABB(this.m_20183_()), fox -> fox != null && fox.m_21824_());
                        if (!foxhounds.isEmpty() && foxhounds.get(0) == this) {
                            furnace.cookingProgress = furnace.cookingProgress == 3 ? 5 : Math.min(furnace.cookingTotalTime - 1, cookTime + 1);
                            if (this.m_269323_() instanceof ServerPlayer sp) {
                                FoxhoundModule.foxhoundFurnaceTrigger.trigger(sp);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInWaterOrRain() {
        return false;
    }

    @NotNull
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return FOXHOUND_LOOT_TABLE;
    }

    @Override
    protected void registerGoals() {
        this.sleepGoal = new SleepGoal(this);
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, this.sleepGoal);
        this.f_21345_.addGoal(3, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.f_21345_.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.f_21345_.addGoal(7, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(8, new FindPlaceToSleepGoal(this, 0.8, FindPlaceToSleepGoal.Target.LIT_FURNACE));
        this.f_21345_.addGoal(9, new FindPlaceToSleepGoal(this, 0.8, FindPlaceToSleepGoal.Target.FURNACE));
        this.f_21345_.addGoal(10, new FindPlaceToSleepGoal(this, 0.8, FindPlaceToSleepGoal.Target.GLOWING));
        this.f_21345_.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(12, new BegGoal(this, 8.0F));
        this.f_21345_.addGoal(13, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(14, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(4, new NonTameRandomTargetGoal(this, Animal.class, false, target -> target instanceof Sheep || target instanceof Rabbit));
        this.f_21346_.addGoal(4, new NonTameRandomTargetGoal(this, Player.class, false, target -> !this.m_21824_()));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return !this.m_21824_() && this.m_9236_().m_46791_() != Difficulty.PEACEFUL ? 0 : super.getRemainingPersistentAngerTime();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (entityIn.getType().fireImmune()) {
            return entityIn instanceof Player ? false : super.doHurtTarget(entityIn);
        } else {
            boolean flag = entityIn.hurt(this.m_9236_().damageSources().onFire(), (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE)));
            if (flag) {
                entityIn.setSecondsOnFire(5);
                this.m_19970_(this, entityIn);
            }
            return flag;
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        this.setWoke();
        return super.hurt(source, amount);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.getItem() == Items.BONE && !this.m_21824_()) {
            return InteractionResult.PASS;
        } else {
            Level level = this.m_9236_();
            if (this.m_21824_()) {
                if (this.timeUntilPotatoEmerges <= 0 && itemstack.is(TinyPotatoModule.tiny_potato.asItem())) {
                    this.timeUntilPotatoEmerges = 600;
                    this.m_5496_(QuarkSounds.ENTITY_FOXHOUND_EAT, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (!itemstack.isEmpty() && itemstack.getItem() == Items.COAL && (level.m_46791_() == Difficulty.PEACEFUL || player.getAbilities().invulnerable || player.m_21124_(MobEffects.FIRE_RESISTANCE) != null) && !level.isClientSide) {
                if (this.f_19796_.nextDouble() < FoxhoundModule.tameChance) {
                    this.m_21828_(player);
                    this.f_21344_.stop();
                    this.m_6710_(null);
                    this.m_21839_(true);
                    this.m_21153_(20.0F);
                    level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    level.broadcastEntityEvent(this, (byte) 6);
                }
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            InteractionResult res = super.mobInteract(player, hand);
            if (res.consumesAction() && !level.isClientSide) {
                this.setWoke();
            }
            return res;
        }
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return super.canMate(otherAnimal) && otherAnimal instanceof Foxhound;
    }

    @Override
    public Wolf getBreedOffspring(@NotNull ServerLevel sworld, @NotNull AgeableMob otherParent) {
        Foxhound kid = new Foxhound(FoxhoundModule.foxhoundType, this.m_9236_());
        UUID uuid = this.m_21805_();
        if (uuid != null) {
            kid.m_21816_(uuid);
            kid.m_7105_(true);
        }
        if (this.isBlue()) {
            kid.setBlue(true);
        }
        return kid;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("OhLawdHeComin", this.timeUntilPotatoEmerges);
        compound.putBoolean("IsSlep", this.m_5803_());
        compound.putBoolean("IsBlue", this.isBlue());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.timeUntilPotatoEmerges = compound.getInt("OhLawdHeComin");
        this.m_21837_(compound.getBoolean("IsSlep"));
        this.setBlue(compound.getBoolean("IsBlue"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.m_5803_()) {
            return null;
        } else if (this.m_21660_()) {
            return QuarkSounds.ENTITY_FOXHOUND_GROWL;
        } else if (this.f_19796_.nextInt(3) != 0) {
            return QuarkSounds.ENTITY_FOXHOUND_IDLE;
        } else {
            return this.m_21824_() && this.m_21223_() < 10.0F ? QuarkSounds.ENTITY_FOXHOUND_WHINE : QuarkSounds.ENTITY_FOXHOUND_PANT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return QuarkSounds.ENTITY_FOXHOUND_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return QuarkSounds.ENTITY_FOXHOUND_DIE;
    }

    public boolean isBlue() {
        return this.f_19804_.get(IS_BLUE);
    }

    public void setBlue(boolean blue) {
        this.f_19804_.set(IS_BLUE, blue);
    }

    public boolean isTatering() {
        return this.f_19804_.get(TATERING);
    }

    public void setTatering(boolean tatering) {
        this.f_19804_.set(TATERING, tatering);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_8055_(pos.below()).m_204336_(FoxhoundModule.foxhoundSpawnableTag) ? 10.0F : (float) worldIn.m_45524_(pos, 0) - 0.5F;
    }

    public static boolean spawnPredicate(EntityType<? extends Foxhound> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        return world.m_46791_() != Difficulty.PEACEFUL && world.m_8055_(pos.below()).m_204336_(FoxhoundModule.foxhoundSpawnableTag);
    }

    public SleepGoal getSleepGoal() {
        return this.sleepGoal;
    }

    private void setWoke() {
        SleepGoal sleep = this.getSleepGoal();
        if (sleep != null) {
            this.m_21837_(false);
            sleep.setSleeping(false);
        }
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}