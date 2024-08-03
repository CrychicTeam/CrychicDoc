package org.violetmoon.quark.content.mobs.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.module.ToretoiseModule;
import org.violetmoon.zeta.util.BlockUtils;
import org.violetmoon.zeta.util.MiscUtil;

public class Toretoise extends Animal {

    private static final TagKey<Block> BREAKS_TORETOISE_ORE = BlockTags.create(new ResourceLocation("quark", "breaks_toretoise_ore"));

    public static final int ORE_TYPES = 5;

    public static final int ANGERY_TIME = 20;

    private static final String TAG_TAMED = "tamed";

    private static final String TAG_ORE = "oreType";

    private static final String TAG_EAT_COOLDOWN = "eatCooldown";

    private static final String TAG_ANGERY_TICKS = "angeryTicks";

    public int rideTime;

    private boolean isTamed;

    private int eatCooldown;

    public int angeryTicks;

    private Ingredient goodFood;

    private LivingEntity lastAggressor;

    private static final EntityDataAccessor<Integer> ORE_TYPE = SynchedEntityData.defineId(Toretoise.class, EntityDataSerializers.INT);

    public Toretoise(EntityType<? extends Toretoise> type, Level world) {
        super(type, world);
        this.m_274367_(1.0F);
        this.m_21441_(BlockPathTypes.WATER, 1.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(ORE_TYPE, 0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(1, new TemptGoal(this, 1.25, this.getGoodFood(), false));
        this.f_21345_.addGoal(2, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(3, new RandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
    }

    private Ingredient getGoodFood() {
        if (this.goodFood == null) {
            this.computeGoodFood();
        }
        return this.goodFood;
    }

    private void computeGoodFood() {
        this.goodFood = Ingredient.of(ToretoiseModule.foods.stream().map(loc -> BuiltInRegistries.ITEM.get(new ResourceLocation(loc))).filter(i -> i != Items.AIR).map(ItemStack::new));
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, SpawnGroupData spawnData, CompoundTag additionalData) {
        this.popOre(true);
        return spawnData;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean canBreed() {
        return this.getOreType() == 0 && this.eatCooldown == 0;
    }

    @NotNull
    @Override
    public SoundEvent getEatingSound(@NotNull ItemStack itemStackIn) {
        return this.eatCooldown == 0 ? QuarkSounds.ENTITY_TORETOISE_EAT : QuarkSounds.ENTITY_TORETOISE_EAT_SATIATED;
    }

    @Override
    protected AABB makeBoundingBox() {
        AABB aabb = super.m_142242_();
        double rheight = this.getOreType() == 0 ? 0.0 : 0.4;
        return new AABB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY + rheight, aabb.maxZ);
    }

    @Override
    public void tick() {
        super.m_8119_();
        Entity riding = this.m_20202_();
        if (riding != null) {
            this.rideTime++;
        } else {
            this.rideTime = 0;
        }
        if (this.eatCooldown > 0) {
            this.eatCooldown--;
        }
        if (this.angeryTicks > 0 && this.m_6084_()) {
            this.angeryTicks--;
            if (this.m_20096_()) {
                int dangerRange = 3;
                double x = this.m_20185_() + (double) (this.m_20205_() / 2.0F);
                double y = this.m_20186_();
                double z = this.m_20189_() + (double) (this.m_20205_() / 2.0F);
                if (this.m_9236_() instanceof ServerLevel serverLevel) {
                    if (this.angeryTicks == 3) {
                        this.m_5496_(QuarkSounds.ENTITY_TORETOISE_ANGRY, 1.0F, 0.2F);
                    } else if (this.angeryTicks == 0) {
                        serverLevel.sendParticles(ParticleTypes.CLOUD, x, y, z, 200, (double) dangerRange, 0.5, (double) dangerRange, 0.0);
                        this.m_146850_(GameEvent.ENTITY_ROAR);
                    }
                }
                if (this.angeryTicks == 0) {
                    AABB hurtAabb = new AABB(x - (double) dangerRange, y - 1.0, z - (double) dangerRange, x + (double) dangerRange, y + 1.0, z + (double) dangerRange);
                    List<LivingEntity> hurtMeDaddy = this.m_9236_().m_6443_(LivingEntity.class, hurtAabb, e -> !(e instanceof Toretoise));
                    LivingEntity aggressor = (LivingEntity) (this.lastAggressor == null ? this : this.lastAggressor);
                    DamageSource damageSource = this.m_269291_().mobAttack(aggressor);
                    for (LivingEntity e : hurtMeDaddy) {
                        DamageSource useSource = damageSource;
                        if (e == aggressor) {
                            useSource = this.m_269291_().mobAttack(this);
                        }
                        e.hurt(useSource, (float) (4 + this.m_9236_().m_46791_().ordinal()));
                    }
                }
            }
        }
        if (this.m_9236_() instanceof ServerLevel serverLevelx) {
            int ore = this.getOreType();
            if (ore != 0) {
                AABB ourBoundingBox = this.m_20191_();
                BlockPos min = BlockPos.containing((double) Math.round(ourBoundingBox.minX), (double) Math.round(ourBoundingBox.minY), (double) Math.round(ourBoundingBox.minZ));
                BlockPos max = BlockPos.containing((double) Math.round(ourBoundingBox.maxX), (double) Math.round(ourBoundingBox.maxY), (double) Math.round(ourBoundingBox.maxZ));
                for (int ix = min.m_123341_(); ix <= max.m_123341_(); ix++) {
                    for (int iy = min.m_123342_(); iy <= max.m_123342_(); iy++) {
                        for (int iz = min.m_123343_(); iz <= max.m_123343_(); iz++) {
                            BlockPos test = new BlockPos(ix, iy, iz);
                            BlockState state = this.m_9236_().getBlockState(test);
                            if (state.m_60734_() == Blocks.MOVING_PISTON) {
                                BlockEntity tile = this.m_9236_().getBlockEntity(test);
                                if (tile instanceof PistonMovingBlockEntity) {
                                    PistonMovingBlockEntity piston = (PistonMovingBlockEntity) tile;
                                    if (piston.isExtending()) {
                                        BlockState pistonState = piston.getMovedState();
                                        if (pistonState.m_204336_(BREAKS_TORETOISE_ORE) && (!pistonState.m_61138_(RodBlock.f_52588_) || pistonState.m_61143_(RodBlock.f_52588_) == piston.getMovementDirection())) {
                                            this.dropOre(ore, new LootParams.Builder(serverLevelx).withParameter(LootContextParams.TOOL, new ItemStack(Items.IRON_PICKAXE)));
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity e = source.getDirectEntity();
        int ore = this.getOreType();
        if (e instanceof LivingEntity living) {
            ItemStack held = living.getMainHandItem();
            if (ore != 0 && held.getItem().canPerformAction(held, ToolActions.PICKAXE_DIG)) {
                if (this.m_9236_() instanceof ServerLevel serverLevel) {
                    if (held.isDamageableItem() && e instanceof Player) {
                        MiscUtil.damageStack((Player) e, InteractionHand.MAIN_HAND, held, 1);
                    }
                    LootParams.Builder lootBuilder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.TOOL, held);
                    if (living instanceof Player player) {
                        lootBuilder.withLuck(player.getLuck());
                    }
                    this.dropOre(ore, lootBuilder);
                    if (living instanceof ServerPlayer sp) {
                        ToretoiseModule.mineToretoiseTrigger.trigger(sp);
                        if (this.isTamed) {
                            ToretoiseModule.mineFedToretoiseTrigger.trigger(sp);
                        }
                    }
                }
                return false;
            }
            if (this.angeryTicks == 0) {
                this.angeryTicks = 20;
                this.lastAggressor = living;
            }
        }
        return super.hurt(source, amount);
    }

    public void dropOre(int ore, LootParams.Builder lootContext) {
        lootContext.withParameter(LootContextParams.ORIGIN, this.m_20182_());
        BlockState dropState = null;
        switch(ore) {
            case 1:
                dropState = Blocks.DEEPSLATE_COAL_ORE.defaultBlockState();
                break;
            case 2:
                dropState = Blocks.DEEPSLATE_IRON_ORE.defaultBlockState();
                break;
            case 3:
                dropState = Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState();
                break;
            case 4:
                dropState = Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState();
                break;
            case 5:
                dropState = Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState();
        }
        if (dropState != null) {
            this.m_5496_(QuarkSounds.ENTITY_TORETOISE_HARVEST, 1.0F, 0.6F);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            for (ItemStack drop : dropState.m_287290_(lootContext)) {
                this.m_5552_(drop, 1.2F);
            }
        }
        this.f_19804_.set(ORE_TYPE, 0);
        this.m_20011_(this.makeBoundingBox());
    }

    @Override
    public void setInLove(Player player) {
        this.setInLoveTime(0);
    }

    @Override
    public void setInLoveTime(int ticks) {
        if (!this.m_9236_().isClientSide) {
            this.m_5496_(this.eatCooldown == 0 ? QuarkSounds.ENTITY_TORETOISE_EAT : QuarkSounds.ENTITY_TORETOISE_EAT_SATIATED, 0.5F + 0.5F * (float) this.m_9236_().random.nextInt(2), (this.m_9236_().random.nextFloat() - this.m_9236_().random.nextFloat()) * 0.2F + 1.0F);
            this.m_5634_(8.0F);
            if (!this.isTamed) {
                this.isTamed = true;
                if (this.m_9236_() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HEART, this.m_20185_(), this.m_20186_(), this.m_20189_(), 20, 0.5, 0.5, 0.5, 0.0);
                }
            } else if (this.eatCooldown == 0) {
                this.popOre(false);
            }
        }
    }

    private void popOre(boolean natural) {
        if (natural || ToretoiseModule.regrowChance != 0) {
            if (this.getOreType() == 0 && (natural || this.m_9236_().random.nextInt(ToretoiseModule.regrowChance) == 0)) {
                int ore = this.f_19796_.nextInt(5) + 1;
                this.f_19804_.set(ORE_TYPE, ore);
                this.m_20011_(this.makeBoundingBox());
                if (!natural) {
                    this.eatCooldown = ToretoiseModule.cooldownTicks;
                    if (this.m_9236_() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CLOUD, this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), 100, 0.6, 0.6, 0.6, 0.0);
                        this.m_5496_(QuarkSounds.ENTITY_TORETOISE_REGROW, 10.0F, 0.7F);
                    }
                }
            }
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return this.getGoodFood().test(stack);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTamed;
    }

    public static boolean spawnPredicate(EntityType<? extends Toretoise> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        return world.m_46791_() != Difficulty.PEACEFUL && pos.m_123342_() <= ToretoiseModule.maxYLevel && MiscUtil.validSpawnLight(world, pos, rand) && MiscUtil.validSpawnLocation(type, world, reason, pos);
    }

    @Override
    public boolean checkSpawnRules(@NotNull LevelAccessor world, @NotNull MobSpawnType reason) {
        BlockPos pos = BlockPos.containing(this.m_20182_()).below();
        BlockState state = world.m_8055_(pos);
        return !BlockUtils.isStoneBased(state, world, pos) ? false : ToretoiseModule.dimensions.canSpawnHere(world);
    }

    @Override
    protected void jumpFromGround() {
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return false;
    }

    @Override
    public float getVoicePitch() {
        return (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 0.6F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return QuarkSounds.ENTITY_TORETOISE_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return QuarkSounds.ENTITY_TORETOISE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return QuarkSounds.ENTITY_TORETOISE_DIE;
    }

    public int getOreType() {
        return this.f_19804_.get(ORE_TYPE);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("tamed", this.isTamed);
        compound.putInt("oreType", this.getOreType());
        compound.putInt("eatCooldown", this.eatCooldown);
        compound.putInt("angeryTicks", this.angeryTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.isTamed = compound.getBoolean("tamed");
        this.f_19804_.set(ORE_TYPE, compound.getInt("oreType"));
        this.eatCooldown = compound.getInt("eatCooldown");
        this.angeryTicks = compound.getInt("angeryTicks");
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.MOVEMENT_SPEED, 0.08).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    public Toretoise getBreedOffspring(@NotNull ServerLevel sworld, @NotNull AgeableMob otherParent) {
        Toretoise e = new Toretoise(ToretoiseModule.toretoiseType, this.m_9236_());
        e.m_6074_();
        return e;
    }
}