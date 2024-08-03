package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIFindWaterTarget;
import com.github.alexthe666.iceandfire.entity.ai.AquaticAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.HippocampusAIWander;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import com.github.alexthe666.iceandfire.inventory.HippocampusContainerMenu;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityHippocampus extends TamableAnimal implements ISyncMount, IAnimatedEntity, ICustomMoveController, ContainerListener, Saddleable {

    public static final int INV_SLOT_SADDLE = 0;

    public static final int INV_SLOT_CHEST = 1;

    public static final int INV_SLOT_ARMOR = 2;

    public static final int INV_BASE_COUNT = 3;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SADDLE = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ARMOR = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityHippocampus.class, EntityDataSerializers.BYTE);

    private static final int FLAG_SITTING = 1;

    private static final int FLAG_TAME = 4;

    private static final Component CONTAINER_TITLE = Component.translatable("entity.iceandfire.hippocampus");

    public static Animation ANIMATION_SPEAK;

    public float onLandProgress;

    public ChainBuffer tail_buffer;

    public SimpleContainer inventory;

    public float sitProgress;

    private int animationTick;

    private Animation currentAnimation;

    private LazyOptional<?> itemHandler = null;

    public EntityHippocampus(EntityType<? extends EntityHippocampus> entityType, Level worldIn) {
        super(entityType, worldIn);
        ANIMATION_SPEAK = Animation.create(15);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.f_21342_ = new EntityHippocampus.HippoMoveControl(this);
        this.m_274367_(1.0F);
        if (worldIn.isClientSide) {
            this.tail_buffer = new ChainBuffer();
        }
        this.createInventory();
    }

    public static int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == Items.IRON_HORSE_ARMOR) {
            return 1;
        } else if (!stack.isEmpty() && stack.getItem() == Items.GOLDEN_HORSE_ARMOR) {
            return 2;
        } else {
            return !stack.isEmpty() && stack.getItem() == Items.DIAMOND_HORSE_ARMOR ? 3 : 0;
        }
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new AquaticAIFindWaterTarget(this, 10, true));
        this.f_21345_.addGoal(2, new AquaticAIGetInWater(this, 1.0));
        this.f_21345_.addGoal(3, new HippocampusAIWander(this, 1.0));
        this.f_21345_.addGoal(4, new BreedGoal(this, 1.0));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.f_21345_.addGoal(0, new TemptGoal(this, 1.0, Ingredient.of(IafItemTags.TEMPT_HIPPOCAMPUS), false));
    }

    @Override
    public int getExperienceReward() {
        return 2;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos) {
        return this.m_9236_().getBlockState(pos.below()).m_60713_(Blocks.WATER) ? 10.0F : (float) this.m_9236_().m_46803_(pos) - 0.5F;
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean isPushedByFluid(FluidType fluid) {
        return false;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(ARMOR, 0);
        this.f_19804_.define(SADDLE, Boolean.FALSE);
        this.f_19804_.define(CHESTED, Boolean.FALSE);
        this.f_19804_.define(CONTROL_STATE, (byte) 0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        if (entity instanceof Mob) {
            return (Mob) entity;
        } else {
            if (this.isSaddled()) {
                entity = this.m_146895_();
                if (entity instanceof Player) {
                    return (Player) entity;
                }
            }
            return null;
        }
    }

    @NotNull
    @Override
    public ItemStack equipItemIfPossible(@Nullable ItemStack itemStackIn) {
        if (itemStackIn == null) {
            return ItemStack.EMPTY;
        } else {
            EquipmentSlot equipmentSlot = m_147233_(itemStackIn);
            int j = equipmentSlot.getIndex() - 500 + 2;
            if (j >= 0 && j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, itemStackIn);
                return itemStackIn;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.inventory != null && !this.m_9236_().isClientSide) {
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    this.m_19983_(itemstack);
                }
            }
        }
        if (this.isChested()) {
            if (!this.m_9236_().isClientSide) {
                this.m_19998_(Blocks.CHEST);
            }
            this.setChested(false);
        }
    }

    protected void dropChestItems() {
        for (int i = 3; i < 18; i++) {
            if (!this.inventory.getItem(i).isEmpty()) {
                if (!this.m_9236_().isClientSide) {
                    this.m_5552_(this.inventory.getItem(i), 1.0F);
                }
                this.inventory.removeItemNoUpdate(i);
            }
        }
    }

    private void updateControlState(int i, boolean newState) {
        byte prevState = this.f_19804_.get(CONTROL_STATE);
        if (newState) {
            this.f_19804_.set(CONTROL_STATE, (byte) (prevState | 1 << i));
        } else {
            this.f_19804_.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return this.f_19804_.get(CONTROL_STATE);
    }

    @Override
    public void setControlState(byte state) {
        this.f_19804_.set(CONTROL_STATE, state);
    }

    @Override
    public boolean canRide(@NotNull Entity rider) {
        return true;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            this.f_20883_ = this.m_146908_();
            this.m_5618_(passenger.getYRot());
        }
        double ymod1 = (double) this.onLandProgress * -0.02;
        passenger.setPos(this.m_20185_(), this.m_20186_() + 0.6F + ymod1, this.m_20189_());
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide && this.f_19796_.nextInt(900) == 0 && this.f_20919_ == 0) {
            this.m_5634_(1.0F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.getControllingPassenger() != null && this.f_19797_ % 20 == 0) {
            this.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false));
        }
        if (this.m_9236_().isClientSide) {
            this.tail_buffer.calculateChainSwingBuffer(40.0F, 10, 1.0F, this);
        }
        boolean inWater = this.m_20069_();
        if (!inWater && this.onLandProgress < 20.0F) {
            this.onLandProgress++;
        } else if (inWater && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        boolean sitting = this.m_21827_();
        if (sitting && this.sitProgress < 20.0F) {
            this.sitProgress += 0.5F;
        } else if (!sitting && this.sitProgress > 0.0F) {
            this.sitProgress -= 0.5F;
        }
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
        super.m_274498_(player, travelVector);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.m_19915_(vec2.y, vec2.x);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        if (this.m_6109_()) {
            Vec3 vec3 = this.m_20184_();
            if (this.isGoingUp()) {
                if (!this.m_20069_() && this.m_20096_()) {
                    this.m_6135_();
                    ForgeHooks.onLivingJump(this);
                } else if (this.m_20069_()) {
                    this.m_20256_(vec3.add(0.0, 0.04F, 0.0));
                }
            }
            if (this.isGoingDown() && this.m_20069_()) {
                this.m_20256_(vec3.add(0.0, -0.025F, 0.0));
            }
        }
    }

    @NotNull
    @Override
    protected Vec3 getRiddenInput(Player player, @NotNull Vec3 travelVector) {
        float f = player.f_20900_ * 0.5F;
        float f1 = player.f_20902_;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        return new Vec3((double) f, 0.0, (double) f1);
    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.m_146909_() * 0.5F, entity.m_146908_());
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player player) {
        float speed = (float) this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.6F;
        if (this.m_20069_()) {
            speed *= (float) IafConfig.hippocampusSwimSpeedMod;
        } else {
            speed *= 0.2F;
        }
        return speed;
    }

    public boolean isGoingUp() {
        return (this.f_19804_.get(CONTROL_STATE) & 1) == 1;
    }

    public boolean isGoingDown() {
        return (this.f_19804_.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean isBlinking() {
        return this.f_19797_ % 50 > 43;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putInt("Armor", this.getArmor());
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag CompoundNBT = new CompoundTag();
                CompoundNBT.putByte("Slot", (byte) i);
                itemstack.save(CompoundNBT);
                nbttaglist.add(CompoundNBT);
            }
        }
        compound.put("Items", nbttaglist);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setArmor(compound.getInt("Armor"));
        if (this.inventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.createInventory();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.inventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        }
    }

    protected int getInventorySize() {
        return this.isChested() ? 18 : 3;
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());
            for (int j = 0; j < i; j++) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }
        this.inventory.addListener(this);
        this.updateContainerEquipment();
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
    }

    protected void updateContainerEquipment() {
        if (!this.m_9236_().isClientSide) {
            this.setSaddled(!this.inventory.getItem(0).isEmpty());
            this.setChested(!this.inventory.getItem(1).isEmpty());
            this.setArmor(getIntFromArmor(this.inventory.getItem(2)));
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return this.m_6084_() && capability == ForgeCapabilities.ITEM_HANDLER && this.itemHandler != null ? this.itemHandler.cast() : super.getCapability(capability, facing);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.itemHandler != null) {
            LazyOptional<?> oldHandler = this.itemHandler;
            this.itemHandler = null;
            oldHandler.invalidate();
        }
    }

    public boolean hasInventoryChanged(Container pInventory) {
        return this.inventory != pInventory;
    }

    @Override
    public boolean isSaddleable() {
        return this.m_6084_() && !this.m_6162_() && this.m_21824_();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource pSource) {
        this.inventory.setItem(0, new ItemStack(Items.SADDLE));
    }

    @Override
    public boolean isSaddled() {
        return this.f_19804_.get(SADDLE);
    }

    public void setSaddled(boolean saddle) {
        this.f_19804_.set(SADDLE, saddle);
    }

    public boolean isChested() {
        return this.f_19804_.get(CHESTED);
    }

    public void setChested(boolean chested) {
        this.f_19804_.set(CHESTED, chested);
        if (!chested) {
            this.dropChestItems();
        }
    }

    public int getArmor() {
        return this.f_19804_.get(ARMOR);
    }

    public void setArmor(int armorType) {
        this.f_19804_.set(ARMOR, armorType);
        double armorValue = switch(armorType) {
            case 1 ->
                10.0;
            case 2 ->
                20.0;
            case 3 ->
                30.0;
            default ->
                0.0;
        };
        this.m_21051_(Attributes.ARMOR).setBaseValue(armorValue);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.m_217043_().nextInt(6));
        return data;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { IAnimatedEntity.NO_ANIMATION, ANIMATION_SPEAK };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        if (ageable instanceof EntityHippocampus) {
            EntityHippocampus hippo = new EntityHippocampus(IafEntityRegistry.HIPPOCAMPUS.get(), this.m_9236_());
            hippo.setVariant(this.m_217043_().nextBoolean() ? this.getVariant() : ((EntityHippocampus) ageable).getVariant());
            return hippo;
        } else {
            return null;
        }
    }

    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.m_6109_() && this.m_20069_()) {
            this.m_19920_(0.1F, pTravelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
        } else {
            super.m_7023_(pTravelVector);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(IafItemTags.BREED_HIPPOCAMPUS);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_8032_();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_6677_(source);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.is(IafItemTags.BREED_HIPPOCAMPUS) && this.m_146764_() == 0 && !this.m_27593_()) {
            this.m_21839_(false);
            this.m_27595_(player);
            this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else if (!itemstack.is(IafItemTags.HEAL_HIPPOCAMPUS)) {
            if (this.m_21830_(player) && itemstack.getItem() == Items.STICK) {
                this.m_21839_(!this.m_21827_());
                return InteractionResult.SUCCESS;
            } else if (this.m_21830_(player) && itemstack.isEmpty() && player.m_6144_()) {
                this.openInventory(player);
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            } else if (this.m_21830_(player) && this.isSaddled() && !this.m_6162_() && !player.m_20159_()) {
                this.doPlayerRide(player);
                return InteractionResult.SUCCESS;
            } else {
                return super.m_6071_(player, hand);
            }
        } else {
            if (!this.m_9236_().isClientSide) {
                this.m_5634_(5.0F);
                this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                for (int i = 0; i < 3; i++) {
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), 0.0, 0.0, 0.0);
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            if (!this.m_21824_() && this.m_217043_().nextInt(3) == 0) {
                this.m_21828_(player);
                for (int i = 0; i < 6; i++) {
                    this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), 0.0, 0.0, 0.0);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    protected void doPlayerRide(Player pPlayer) {
        this.m_21839_(false);
        if (!this.m_9236_().isClientSide) {
            pPlayer.m_146922_(this.m_146908_());
            pPlayer.m_146926_(this.m_146909_());
            pPlayer.m_20329_(this);
        }
    }

    public void openInventory(Player player) {
        if (!this.m_9236_().isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, this.getMenuProvider());
        }
        IceAndFire.PROXY.setReferencedMob(this);
    }

    public MenuProvider getMenuProvider() {
        return new SimpleMenuProvider((containerId, playerInventory, player) -> new HippocampusContainerMenu(containerId, this.inventory, playerInventory, this), CONTAINER_TITLE);
    }

    @Override
    public void up(boolean up) {
        this.updateControlState(0, up);
    }

    @Override
    public void down(boolean down) {
        this.updateControlState(1, down);
    }

    @Override
    public void attack(boolean attack) {
    }

    @Override
    public void strike(boolean strike) {
    }

    @Override
    public void dismount(boolean dismount) {
        this.updateControlState(2, dismount);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOCAMPUS_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.HIPPOCAMPUS_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOCAMPUS_DIE;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Nullable
    public Player getRidingPlayer() {
        LivingEntity var2 = this.getControllingPassenger();
        return var2 instanceof Player ? (Player) var2 : null;
    }

    public int getInventoryColumns() {
        return 5;
    }

    @Override
    public void containerChanged(@NotNull Container pInvBasic) {
        boolean flag = this.isSaddled();
        this.updateContainerEquipment();
        if (this.f_19797_ > 20 && !flag && this.isSaddled()) {
            this.m_5496_(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
        }
    }

    class HippoMoveControl extends MoveControl {

        private final EntityHippocampus hippo = EntityHippocampus.this;

        public HippoMoveControl(EntityHippocampus entityHippocampus) {
            super(entityHippocampus);
        }

        private void updateSpeed() {
            if (this.hippo.m_20069_()) {
                this.hippo.m_20256_(this.hippo.m_20184_().add(0.0, 0.005, 0.0));
            } else if (this.hippo.m_20096_()) {
                this.hippo.m_7910_(Math.max(this.hippo.m_6113_() / 4.0F, 0.06F));
            }
        }

        @Override
        public void tick() {
            this.updateSpeed();
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.hippo.m_21573_().isDone()) {
                double d0 = this.f_24975_ - this.hippo.m_20185_();
                double d1 = this.f_24976_ - this.hippo.m_20186_();
                double d2 = this.f_24977_ - this.hippo.m_20189_();
                double distance = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                if (distance < 1.0E-5F) {
                    this.f_24974_.setSpeed(0.0F);
                } else {
                    d1 /= distance;
                    float minRotation = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    this.hippo.m_146922_(this.m_24991_(this.hippo.m_146908_(), minRotation, 90.0F));
                    this.hippo.f_20883_ = this.hippo.m_146908_();
                    float maxSpeed = (float) (this.f_24978_ * this.hippo.m_21133_(Attributes.MOVEMENT_SPEED));
                    maxSpeed *= 0.6F;
                    if (this.hippo.m_20069_()) {
                        maxSpeed *= (float) IafConfig.hippocampusSwimSpeedMod;
                    } else {
                        maxSpeed *= 0.2F;
                    }
                    this.hippo.m_7910_(Mth.lerp(0.125F, this.hippo.m_6113_(), maxSpeed));
                    this.hippo.m_20256_(this.hippo.m_20184_().add(0.0, (double) this.hippo.m_6113_() * d1 * 0.1, 0.0));
                }
            } else {
                this.hippo.m_7910_(0.0F);
            }
        }
    }
}