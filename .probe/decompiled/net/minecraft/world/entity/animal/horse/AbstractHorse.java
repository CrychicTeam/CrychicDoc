package net.minecraft.world.entity.animal.horse;

import com.google.common.collect.UnmodifiableIterator;
import java.util.UUID;
import java.util.function.DoubleSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractHorse extends Animal implements ContainerListener, HasCustomInventoryScreen, OwnableEntity, PlayerRideableJumping, Saddleable {

    public static final int EQUIPMENT_SLOT_OFFSET = 400;

    public static final int CHEST_SLOT_OFFSET = 499;

    public static final int INVENTORY_SLOT_OFFSET = 500;

    public static final double BREEDING_CROSS_FACTOR = 0.15;

    private static final float MIN_MOVEMENT_SPEED = (float) generateSpeed(() -> 0.0);

    private static final float MAX_MOVEMENT_SPEED = (float) generateSpeed(() -> 1.0);

    private static final float MIN_JUMP_STRENGTH = (float) generateJumpStrength(() -> 0.0);

    private static final float MAX_JUMP_STRENGTH = (float) generateJumpStrength(() -> 1.0);

    private static final float MIN_HEALTH = generateMaxHealth(p_272505_ -> 0);

    private static final float MAX_HEALTH = generateMaxHealth(p_272504_ -> p_272504_ - 1);

    private static final float BACKWARDS_MOVE_SPEED_FACTOR = 0.25F;

    private static final float SIDEWAYS_MOVE_SPEED_FACTOR = 0.5F;

    private static final Predicate<LivingEntity> PARENT_HORSE_SELECTOR = p_30636_ -> p_30636_ instanceof AbstractHorse && ((AbstractHorse) p_30636_).isBred();

    private static final TargetingConditions MOMMY_TARGETING = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight().selector(PARENT_HORSE_SELECTOR);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.WHEAT, Items.SUGAR, Blocks.HAY_BLOCK.asItem(), Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);

    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.BYTE);

    private static final int FLAG_TAME = 2;

    private static final int FLAG_SADDLE = 4;

    private static final int FLAG_BRED = 8;

    private static final int FLAG_EATING = 16;

    private static final int FLAG_STANDING = 32;

    private static final int FLAG_OPEN_MOUTH = 64;

    public static final int INV_SLOT_SADDLE = 0;

    public static final int INV_SLOT_ARMOR = 1;

    public static final int INV_BASE_COUNT = 2;

    private int eatingCounter;

    private int mouthCounter;

    private int standCounter;

    public int tailCounter;

    public int sprintCounter;

    protected boolean isJumping;

    protected SimpleContainer inventory;

    protected int temper;

    protected float playerJumpPendingScale;

    protected boolean allowStandSliding;

    private float eatAnim;

    private float eatAnimO;

    private float standAnim;

    private float standAnimO;

    private float mouthAnim;

    private float mouthAnimO;

    protected boolean canGallop = true;

    protected int gallopSoundCounter;

    @Nullable
    private UUID owner;

    protected AbstractHorse(EntityType<? extends AbstractHorse> entityTypeExtendsAbstractHorse0, Level level1) {
        super(entityTypeExtendsAbstractHorse0, level1);
        this.m_274367_(1.0F);
        this.createInventory();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.2));
        this.f_21345_.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0, AbstractHorse.class));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.0));
        this.f_21345_.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        if (this.canPerformRearing()) {
            this.f_21345_.addGoal(9, new RandomStandGoal(this));
        }
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE), false));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_ID_FLAGS, (byte) 0);
    }

    protected boolean getFlag(int int0) {
        return (this.f_19804_.get(DATA_ID_FLAGS) & int0) != 0;
    }

    protected void setFlag(int int0, boolean boolean1) {
        byte $$2 = this.f_19804_.get(DATA_ID_FLAGS);
        if (boolean1) {
            this.f_19804_.set(DATA_ID_FLAGS, (byte) ($$2 | int0));
        } else {
            this.f_19804_.set(DATA_ID_FLAGS, (byte) ($$2 & ~int0));
        }
    }

    public boolean isTamed() {
        return this.getFlag(2);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.owner;
    }

    public void setOwnerUUID(@Nullable UUID uUID0) {
        this.owner = uUID0;
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setTamed(boolean boolean0) {
        this.setFlag(2, boolean0);
    }

    public void setIsJumping(boolean boolean0) {
        this.isJumping = boolean0;
    }

    @Override
    protected void onLeashDistance(float float0) {
        if (float0 > 6.0F && this.isEating()) {
            this.setEating(false);
        }
    }

    public boolean isEating() {
        return this.getFlag(16);
    }

    public boolean isStanding() {
        return this.getFlag(32);
    }

    public boolean isBred() {
        return this.getFlag(8);
    }

    public void setBred(boolean boolean0) {
        this.setFlag(8, boolean0);
    }

    @Override
    public boolean isSaddleable() {
        return this.m_6084_() && !this.m_6162_() && this.isTamed();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource0) {
        this.inventory.setItem(0, new ItemStack(Items.SADDLE));
    }

    public void equipArmor(Player player0, ItemStack itemStack1) {
        if (this.isArmor(itemStack1)) {
            this.inventory.setItem(1, itemStack1.copyWithCount(1));
            if (!player0.getAbilities().instabuild) {
                itemStack1.shrink(1);
            }
        }
    }

    @Override
    public boolean isSaddled() {
        return this.getFlag(4);
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int int0) {
        this.temper = int0;
    }

    public int modifyTemper(int int0) {
        int $$1 = Mth.clamp(this.getTemper() + int0, 0, this.getMaxTemper());
        this.setTemper($$1);
        return $$1;
    }

    @Override
    public boolean isPushable() {
        return !this.m_20160_();
    }

    private void eating() {
        this.openMouth();
        if (!this.m_20067_()) {
            SoundEvent $$0 = this.getEatingSound();
            if ($$0 != null) {
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), $$0, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
            }
        }
    }

    @Override
    public boolean causeFallDamage(float float0, float float1, DamageSource damageSource2) {
        if (float0 > 1.0F) {
            this.m_5496_(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }
        int $$3 = this.calculateFallDamage(float0, float1);
        if ($$3 <= 0) {
            return false;
        } else {
            this.hurt(damageSource2, (float) $$3);
            if (this.m_20160_()) {
                for (Entity $$4 : this.m_146897_()) {
                    $$4.hurt(damageSource2, (float) $$3);
                }
            }
            this.m_21229_();
            return true;
        }
    }

    @Override
    protected int calculateFallDamage(float float0, float float1) {
        return Mth.ceil((float0 * 0.5F - 3.0F) * float1);
    }

    protected int getInventorySize() {
        return 2;
    }

    protected void createInventory() {
        SimpleContainer $$0 = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if ($$0 != null) {
            $$0.removeListener(this);
            int $$1 = Math.min($$0.getContainerSize(), this.inventory.getContainerSize());
            for (int $$2 = 0; $$2 < $$1; $$2++) {
                ItemStack $$3 = $$0.getItem($$2);
                if (!$$3.isEmpty()) {
                    this.inventory.setItem($$2, $$3.copy());
                }
            }
        }
        this.inventory.addListener(this);
        this.updateContainerEquipment();
    }

    protected void updateContainerEquipment() {
        if (!this.m_9236_().isClientSide) {
            this.setFlag(4, !this.inventory.getItem(0).isEmpty());
        }
    }

    @Override
    public void containerChanged(Container container0) {
        boolean $$1 = this.isSaddled();
        this.updateContainerEquipment();
        if (this.f_19797_ > 20 && !$$1 && this.isSaddled()) {
            this.m_5496_(this.m_246265_(), 0.5F, 1.0F);
        }
    }

    public double getCustomJump() {
        return this.m_21133_(Attributes.JUMP_STRENGTH);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        boolean $$2 = super.hurt(damageSource0, float1);
        if ($$2 && this.f_19796_.nextInt(3) == 0) {
            this.standIfPossible();
        }
        return $$2;
    }

    protected boolean canPerformRearing() {
        return true;
    }

    @Nullable
    protected SoundEvent getEatingSound() {
        return null;
    }

    @Nullable
    protected SoundEvent getAngrySound() {
        return null;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        if (!blockState1.m_278721_()) {
            BlockState $$2 = this.m_9236_().getBlockState(blockPos0.above());
            SoundType $$3 = blockState1.m_60827_();
            if ($$2.m_60713_(Blocks.SNOW)) {
                $$3 = $$2.m_60827_();
            }
            if (this.m_20160_() && this.canGallop) {
                this.gallopSoundCounter++;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playGallopSound($$3);
                } else if (this.gallopSoundCounter <= 5) {
                    this.m_5496_(SoundEvents.HORSE_STEP_WOOD, $$3.getVolume() * 0.15F, $$3.getPitch());
                }
            } else if (this.isWoodSoundType($$3)) {
                this.m_5496_(SoundEvents.HORSE_STEP_WOOD, $$3.getVolume() * 0.15F, $$3.getPitch());
            } else {
                this.m_5496_(SoundEvents.HORSE_STEP, $$3.getVolume() * 0.15F, $$3.getPitch());
            }
        }
    }

    private boolean isWoodSoundType(SoundType soundType0) {
        return soundType0 == SoundType.WOOD || soundType0 == SoundType.NETHER_WOOD || soundType0 == SoundType.STEM || soundType0 == SoundType.CHERRY_WOOD || soundType0 == SoundType.BAMBOO_WOOD;
    }

    protected void playGallopSound(SoundType soundType0) {
        this.m_5496_(SoundEvents.HORSE_GALLOP, soundType0.getVolume() * 0.15F, soundType0.getPitch());
    }

    public static AttributeSupplier.Builder createBaseHorseAttributes() {
        return Mob.createMobAttributes().add(Attributes.JUMP_STRENGTH).add(Attributes.MAX_HEALTH, 53.0).add(Attributes.MOVEMENT_SPEED, 0.225F);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    public int getMaxTemper() {
        return 100;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Override
    public void openCustomInventoryScreen(Player player0) {
        if (!this.m_9236_().isClientSide && (!this.m_20160_() || this.m_20363_(player0)) && this.isTamed()) {
            player0.openHorseInventory(this, this.inventory);
        }
    }

    public InteractionResult fedFood(Player player0, ItemStack itemStack1) {
        boolean $$2 = this.handleEating(player0, itemStack1);
        if (!player0.getAbilities().instabuild) {
            itemStack1.shrink(1);
        }
        if (this.m_9236_().isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            return $$2 ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    protected boolean handleEating(Player player0, ItemStack itemStack1) {
        boolean $$2 = false;
        float $$3 = 0.0F;
        int $$4 = 0;
        int $$5 = 0;
        if (itemStack1.is(Items.WHEAT)) {
            $$3 = 2.0F;
            $$4 = 20;
            $$5 = 3;
        } else if (itemStack1.is(Items.SUGAR)) {
            $$3 = 1.0F;
            $$4 = 30;
            $$5 = 3;
        } else if (itemStack1.is(Blocks.HAY_BLOCK.asItem())) {
            $$3 = 20.0F;
            $$4 = 180;
        } else if (itemStack1.is(Items.APPLE)) {
            $$3 = 3.0F;
            $$4 = 60;
            $$5 = 3;
        } else if (itemStack1.is(Items.GOLDEN_CARROT)) {
            $$3 = 4.0F;
            $$4 = 60;
            $$5 = 5;
            if (!this.m_9236_().isClientSide && this.isTamed() && this.m_146764_() == 0 && !this.m_27593_()) {
                $$2 = true;
                this.m_27595_(player0);
            }
        } else if (itemStack1.is(Items.GOLDEN_APPLE) || itemStack1.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            $$3 = 10.0F;
            $$4 = 240;
            $$5 = 10;
            if (!this.m_9236_().isClientSide && this.isTamed() && this.m_146764_() == 0 && !this.m_27593_()) {
                $$2 = true;
                this.m_27595_(player0);
            }
        }
        if (this.m_21223_() < this.m_21233_() && $$3 > 0.0F) {
            this.m_5634_($$3);
            $$2 = true;
        }
        if (this.m_6162_() && $$4 > 0) {
            this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), 0.0, 0.0, 0.0);
            if (!this.m_9236_().isClientSide) {
                this.m_146758_($$4);
            }
            $$2 = true;
        }
        if ($$5 > 0 && ($$2 || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
            $$2 = true;
            if (!this.m_9236_().isClientSide) {
                this.modifyTemper($$5);
            }
        }
        if ($$2) {
            this.eating();
            this.m_146850_(GameEvent.EAT);
        }
        return $$2;
    }

    protected void doPlayerRide(Player player0) {
        this.setEating(false);
        this.setStanding(false);
        if (!this.m_9236_().isClientSide) {
            player0.m_146922_(this.m_146908_());
            player0.m_146926_(this.m_146909_());
            player0.m_20329_(this);
        }
    }

    @Override
    public boolean isImmobile() {
        return super.m_6107_() && this.m_20160_() && this.isSaddled() || this.isEating() || this.isStanding();
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return FOOD_ITEMS.test(itemStack0);
    }

    private void moveTail() {
        this.tailCounter = 1;
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.inventory != null) {
            for (int $$0 = 0; $$0 < this.inventory.getContainerSize(); $$0++) {
                ItemStack $$1 = this.inventory.getItem($$0);
                if (!$$1.isEmpty() && !EnchantmentHelper.hasVanishingCurse($$1)) {
                    this.m_19983_($$1);
                }
            }
        }
    }

    @Override
    public void aiStep() {
        if (this.f_19796_.nextInt(200) == 0) {
            this.moveTail();
        }
        super.aiStep();
        if (!this.m_9236_().isClientSide && this.m_6084_()) {
            if (this.f_19796_.nextInt(900) == 0 && this.f_20919_ == 0) {
                this.m_5634_(1.0F);
            }
            if (this.canEatGrass()) {
                if (!this.isEating() && !this.m_20160_() && this.f_19796_.nextInt(300) == 0 && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                    this.setEating(true);
                }
                if (this.isEating() && ++this.eatingCounter > 50) {
                    this.eatingCounter = 0;
                    this.setEating(false);
                }
            }
            this.followMommy();
        }
    }

    protected void followMommy() {
        if (this.isBred() && this.m_6162_() && !this.isEating()) {
            LivingEntity $$0 = this.m_9236_().m_45963_(AbstractHorse.class, MOMMY_TARGETING, this, this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_20191_().inflate(16.0));
            if ($$0 != null && this.m_20280_($$0) > 4.0) {
                this.f_21344_.createPath($$0, 0);
            }
        }
    }

    public boolean canEatGrass() {
        return true;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
            this.mouthCounter = 0;
            this.setFlag(64, false);
        }
        if (this.m_21515_() && this.standCounter > 0 && ++this.standCounter > 20) {
            this.standCounter = 0;
            this.setStanding(false);
        }
        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }
        if (this.sprintCounter > 0) {
            this.sprintCounter++;
            if (this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }
        }
        this.eatAnimO = this.eatAnim;
        if (this.isEating()) {
            this.eatAnim = this.eatAnim + (1.0F - this.eatAnim) * 0.4F + 0.05F;
            if (this.eatAnim > 1.0F) {
                this.eatAnim = 1.0F;
            }
        } else {
            this.eatAnim = this.eatAnim + ((0.0F - this.eatAnim) * 0.4F - 0.05F);
            if (this.eatAnim < 0.0F) {
                this.eatAnim = 0.0F;
            }
        }
        this.standAnimO = this.standAnim;
        if (this.isStanding()) {
            this.eatAnim = 0.0F;
            this.eatAnimO = this.eatAnim;
            this.standAnim = this.standAnim + (1.0F - this.standAnim) * 0.4F + 0.05F;
            if (this.standAnim > 1.0F) {
                this.standAnim = 1.0F;
            }
        } else {
            this.allowStandSliding = false;
            this.standAnim = this.standAnim + ((0.8F * this.standAnim * this.standAnim * this.standAnim - this.standAnim) * 0.6F - 0.05F);
            if (this.standAnim < 0.0F) {
                this.standAnim = 0.0F;
            }
        }
        this.mouthAnimO = this.mouthAnim;
        if (this.getFlag(64)) {
            this.mouthAnim = this.mouthAnim + (1.0F - this.mouthAnim) * 0.7F + 0.05F;
            if (this.mouthAnim > 1.0F) {
                this.mouthAnim = 1.0F;
            }
        } else {
            this.mouthAnim = this.mouthAnim + ((0.0F - this.mouthAnim) * 0.7F - 0.05F);
            if (this.mouthAnim < 0.0F) {
                this.mouthAnim = 0.0F;
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        if (this.m_20160_() || this.m_6162_()) {
            return super.mobInteract(player0, interactionHand1);
        } else if (this.isTamed() && player0.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player0);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            ItemStack $$2 = player0.m_21120_(interactionHand1);
            if (!$$2.isEmpty()) {
                InteractionResult $$3 = $$2.interactLivingEntity(player0, this, interactionHand1);
                if ($$3.consumesAction()) {
                    return $$3;
                }
                if (this.canWearArmor() && this.isArmor($$2) && !this.isWearingArmor()) {
                    this.equipArmor(player0, $$2);
                    return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
                }
            }
            this.doPlayerRide(player0);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        }
    }

    private void openMouth() {
        if (!this.m_9236_().isClientSide) {
            this.mouthCounter = 1;
            this.setFlag(64, true);
        }
    }

    public void setEating(boolean boolean0) {
        this.setFlag(16, boolean0);
    }

    public void setStanding(boolean boolean0) {
        if (boolean0) {
            this.setEating(false);
        }
        this.setFlag(32, boolean0);
    }

    @Nullable
    public SoundEvent getAmbientStandSound() {
        return this.m_7515_();
    }

    public void standIfPossible() {
        if (this.canPerformRearing() && this.m_21515_()) {
            this.standCounter = 1;
            this.setStanding(true);
        }
    }

    public void makeMad() {
        if (!this.isStanding()) {
            this.standIfPossible();
            SoundEvent $$0 = this.getAngrySound();
            if ($$0 != null) {
                this.m_5496_($$0, this.getSoundVolume(), this.m_6100_());
            }
        }
    }

    public boolean tameWithName(Player player0) {
        this.setOwnerUUID(player0.m_20148_());
        this.setTamed(true);
        if (player0 instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer) player0, this);
        }
        this.m_9236_().broadcastEntityEvent(this, (byte) 7);
        return true;
    }

    @Override
    protected void tickRidden(Player player0, Vec3 vec1) {
        super.m_274498_(player0, vec1);
        Vec2 $$2 = this.getRiddenRotation(player0);
        this.m_19915_($$2.y, $$2.x);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        if (this.m_6109_()) {
            if (vec1.z <= 0.0) {
                this.gallopSoundCounter = 0;
            }
            if (this.m_20096_()) {
                this.setIsJumping(false);
                if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
                    this.executeRidersJump(this.playerJumpPendingScale, vec1);
                }
                this.playerJumpPendingScale = 0.0F;
            }
        }
    }

    protected Vec2 getRiddenRotation(LivingEntity livingEntity0) {
        return new Vec2(livingEntity0.m_146909_() * 0.5F, livingEntity0.m_146908_());
    }

    @Override
    protected Vec3 getRiddenInput(Player player0, Vec3 vec1) {
        if (this.m_20096_() && this.playerJumpPendingScale == 0.0F && this.isStanding() && !this.allowStandSliding) {
            return Vec3.ZERO;
        } else {
            float $$2 = player0.f_20900_ * 0.5F;
            float $$3 = player0.f_20902_;
            if ($$3 <= 0.0F) {
                $$3 *= 0.25F;
            }
            return new Vec3((double) $$2, 0.0, (double) $$3);
        }
    }

    @Override
    protected float getRiddenSpeed(Player player0) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    protected void executeRidersJump(float float0, Vec3 vec1) {
        double $$2 = this.getCustomJump() * (double) float0 * (double) this.m_20098_();
        double $$3 = $$2 + (double) this.m_285755_();
        Vec3 $$4 = this.m_20184_();
        this.m_20334_($$4.x, $$3, $$4.z);
        this.setIsJumping(true);
        this.f_19812_ = true;
        if (vec1.z > 0.0) {
            float $$5 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
            float $$6 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
            this.m_20256_(this.m_20184_().add((double) (-0.4F * $$5 * float0), 0.0, (double) (0.4F * $$6 * float0)));
        }
    }

    protected void playJumpSound() {
        this.m_5496_(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("EatingHaystack", this.isEating());
        compoundTag0.putBoolean("Bred", this.isBred());
        compoundTag0.putInt("Temper", this.getTemper());
        compoundTag0.putBoolean("Tame", this.isTamed());
        if (this.getOwnerUUID() != null) {
            compoundTag0.putUUID("Owner", this.getOwnerUUID());
        }
        if (!this.inventory.getItem(0).isEmpty()) {
            compoundTag0.put("SaddleItem", this.inventory.getItem(0).save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setEating(compoundTag0.getBoolean("EatingHaystack"));
        this.setBred(compoundTag0.getBoolean("Bred"));
        this.setTemper(compoundTag0.getInt("Temper"));
        this.setTamed(compoundTag0.getBoolean("Tame"));
        UUID $$1;
        if (compoundTag0.hasUUID("Owner")) {
            $$1 = compoundTag0.getUUID("Owner");
        } else {
            String $$2 = compoundTag0.getString("Owner");
            $$1 = OldUsersConverter.convertMobOwnerIfNecessary(this.m_20194_(), $$2);
        }
        if ($$1 != null) {
            this.setOwnerUUID($$1);
        }
        if (compoundTag0.contains("SaddleItem", 10)) {
            ItemStack $$4 = ItemStack.of(compoundTag0.getCompound("SaddleItem"));
            if ($$4.is(Items.SADDLE)) {
                this.inventory.setItem(0, $$4);
            }
        }
        this.updateContainerEquipment();
    }

    @Override
    public boolean canMate(Animal animal0) {
        return false;
    }

    protected boolean canParent() {
        return !this.m_20160_() && !this.m_20159_() && this.isTamed() && !this.m_6162_() && this.m_21223_() >= this.m_21233_() && this.m_27593_();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return null;
    }

    protected void setOffspringAttributes(AgeableMob ageableMob0, AbstractHorse abstractHorse1) {
        this.setOffspringAttribute(ageableMob0, abstractHorse1, Attributes.MAX_HEALTH, (double) MIN_HEALTH, (double) MAX_HEALTH);
        this.setOffspringAttribute(ageableMob0, abstractHorse1, Attributes.JUMP_STRENGTH, (double) MIN_JUMP_STRENGTH, (double) MAX_JUMP_STRENGTH);
        this.setOffspringAttribute(ageableMob0, abstractHorse1, Attributes.MOVEMENT_SPEED, (double) MIN_MOVEMENT_SPEED, (double) MAX_MOVEMENT_SPEED);
    }

    private void setOffspringAttribute(AgeableMob ageableMob0, AbstractHorse abstractHorse1, Attribute attribute2, double double3, double double4) {
        double $$5 = createOffspringAttribute(this.m_21172_(attribute2), ageableMob0.m_21172_(attribute2), double3, double4, this.f_19796_);
        abstractHorse1.m_21051_(attribute2).setBaseValue($$5);
    }

    static double createOffspringAttribute(double double0, double double1, double double2, double double3, RandomSource randomSource4) {
        if (double3 <= double2) {
            throw new IllegalArgumentException("Incorrect range for an attribute");
        } else {
            double0 = Mth.clamp(double0, double2, double3);
            double1 = Mth.clamp(double1, double2, double3);
            double $$5 = 0.15 * (double3 - double2);
            double $$6 = Math.abs(double0 - double1) + $$5 * 2.0;
            double $$7 = (double0 + double1) / 2.0;
            double $$8 = (randomSource4.nextDouble() + randomSource4.nextDouble() + randomSource4.nextDouble()) / 3.0 - 0.5;
            double $$9 = $$7 + $$6 * $$8;
            if ($$9 > double3) {
                double $$10 = $$9 - double3;
                return double3 - $$10;
            } else if ($$9 < double2) {
                double $$11 = double2 - $$9;
                return double2 + $$11;
            } else {
                return $$9;
            }
        }
    }

    public float getEatAnim(float float0) {
        return Mth.lerp(float0, this.eatAnimO, this.eatAnim);
    }

    public float getStandAnim(float float0) {
        return Mth.lerp(float0, this.standAnimO, this.standAnim);
    }

    public float getMouthAnim(float float0) {
        return Mth.lerp(float0, this.mouthAnimO, this.mouthAnim);
    }

    @Override
    public void onPlayerJump(int int0) {
        if (this.isSaddled()) {
            if (int0 < 0) {
                int0 = 0;
            } else {
                this.allowStandSliding = true;
                this.standIfPossible();
            }
            if (int0 >= 90) {
                this.playerJumpPendingScale = 1.0F;
            } else {
                this.playerJumpPendingScale = 0.4F + 0.4F * (float) int0 / 90.0F;
            }
        }
    }

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int int0) {
        this.allowStandSliding = true;
        this.standIfPossible();
        this.playJumpSound();
    }

    @Override
    public void handleStopJump() {
    }

    protected void spawnTamingParticles(boolean boolean0) {
        ParticleOptions $$1 = boolean0 ? ParticleTypes.HEART : ParticleTypes.SMOKE;
        for (int $$2 = 0; $$2 < 7; $$2++) {
            double $$3 = this.f_19796_.nextGaussian() * 0.02;
            double $$4 = this.f_19796_.nextGaussian() * 0.02;
            double $$5 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle($$1, this.m_20208_(1.0), this.m_20187_() + 0.5, this.m_20262_(1.0), $$3, $$4, $$5);
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 7) {
            this.spawnTamingParticles(true);
        } else if (byte0 == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    @Override
    protected void positionRider(Entity entity0, Entity.MoveFunction entityMoveFunction1) {
        super.m_19956_(entity0, entityMoveFunction1);
        if (this.standAnimO > 0.0F) {
            float $$2 = Mth.sin(this.f_20883_ * (float) (Math.PI / 180.0));
            float $$3 = Mth.cos(this.f_20883_ * (float) (Math.PI / 180.0));
            float $$4 = 0.7F * this.standAnimO;
            float $$5 = 0.15F * this.standAnimO;
            entityMoveFunction1.accept(entity0, this.m_20185_() + (double) ($$4 * $$2), this.m_20186_() + this.m_6048_() + entity0.getMyRidingOffset() + (double) $$5, this.m_20189_() - (double) ($$4 * $$3));
            if (entity0 instanceof LivingEntity) {
                ((LivingEntity) entity0).yBodyRot = this.f_20883_;
            }
        }
    }

    protected static float generateMaxHealth(IntUnaryOperator intUnaryOperator0) {
        return 15.0F + (float) intUnaryOperator0.applyAsInt(8) + (float) intUnaryOperator0.applyAsInt(9);
    }

    protected static double generateJumpStrength(DoubleSupplier doubleSupplier0) {
        return 0.4F + doubleSupplier0.getAsDouble() * 0.2 + doubleSupplier0.getAsDouble() * 0.2 + doubleSupplier0.getAsDouble() * 0.2;
    }

    protected static double generateSpeed(DoubleSupplier doubleSupplier0) {
        return (0.45F + doubleSupplier0.getAsDouble() * 0.3 + doubleSupplier0.getAsDouble() * 0.3 + doubleSupplier0.getAsDouble() * 0.3) * 0.25;
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.95F;
    }

    public boolean canWearArmor() {
        return false;
    }

    public boolean isWearingArmor() {
        return !this.m_6844_(EquipmentSlot.CHEST).isEmpty();
    }

    public boolean isArmor(ItemStack itemStack0) {
        return false;
    }

    private SlotAccess createEquipmentSlotAccess(final int int0, final Predicate<ItemStack> predicateItemStack1) {
        return new SlotAccess() {

            @Override
            public ItemStack get() {
                return AbstractHorse.this.inventory.getItem(int0);
            }

            @Override
            public boolean set(ItemStack p_149528_) {
                if (!predicateItemStack1.test(p_149528_)) {
                    return false;
                } else {
                    AbstractHorse.this.inventory.setItem(int0, p_149528_);
                    AbstractHorse.this.updateContainerEquipment();
                    return true;
                }
            }
        };
    }

    @Override
    public SlotAccess getSlot(int int0) {
        int $$1 = int0 - 400;
        if ($$1 >= 0 && $$1 < 2 && $$1 < this.inventory.getContainerSize()) {
            if ($$1 == 0) {
                return this.createEquipmentSlotAccess($$1, p_149518_ -> p_149518_.isEmpty() || p_149518_.is(Items.SADDLE));
            }
            if ($$1 == 1) {
                if (!this.canWearArmor()) {
                    return SlotAccess.NULL;
                }
                return this.createEquipmentSlotAccess($$1, p_149516_ -> p_149516_.isEmpty() || this.isArmor(p_149516_));
            }
        }
        int $$2 = int0 - 500 + 2;
        return $$2 >= 2 && $$2 < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, $$2) : super.m_141942_(int0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity var3 = this.m_146895_();
        if (var3 instanceof Mob) {
            return (Mob) var3;
        } else {
            if (this.isSaddled()) {
                var3 = this.m_146895_();
                if (var3 instanceof Player) {
                    return (Player) var3;
                }
            }
            return null;
        }
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 vec0, LivingEntity livingEntity1) {
        double $$2 = this.m_20185_() + vec0.x;
        double $$3 = this.m_20191_().minY;
        double $$4 = this.m_20189_() + vec0.z;
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        UnmodifiableIterator var10 = livingEntity1.getDismountPoses().iterator();
        while (var10.hasNext()) {
            Pose $$6 = (Pose) var10.next();
            $$5.set($$2, $$3, $$4);
            double $$7 = this.m_20191_().maxY + 0.75;
            do {
                double $$8 = this.m_9236_().m_45573_($$5);
                if ((double) $$5.m_123342_() + $$8 > $$7) {
                    break;
                }
                if (DismountHelper.isBlockFloorValid($$8)) {
                    AABB $$9 = livingEntity1.getLocalBoundsForPose($$6);
                    Vec3 $$10 = new Vec3($$2, (double) $$5.m_123342_() + $$8, $$4);
                    if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity1, $$9.move($$10))) {
                        livingEntity1.m_20124_($$6);
                        return $$10;
                    }
                }
                $$5.move(Direction.UP);
            } while (!((double) $$5.m_123342_() < $$7));
        }
        return null;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        Vec3 $$1 = m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), this.m_146908_() + (livingEntity0.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 $$2 = this.getDismountLocationInDirection($$1, livingEntity0);
        if ($$2 != null) {
            return $$2;
        } else {
            Vec3 $$3 = m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), this.m_146908_() + (livingEntity0.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 $$4 = this.getDismountLocationInDirection($$3, livingEntity0);
            return $$4 != null ? $$4 : this.m_20182_();
        }
    }

    protected void randomizeAttributes(RandomSource randomSource0) {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AgeableMob.AgeableMobGroupData(0.2F);
        }
        this.randomizeAttributes(serverLevelAccessor0.m_213780_());
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    public boolean hasInventoryChanged(Container container0) {
        return this.inventory != container0;
    }

    public int getAmbientStandInterval() {
        return this.getAmbientSoundInterval();
    }
}