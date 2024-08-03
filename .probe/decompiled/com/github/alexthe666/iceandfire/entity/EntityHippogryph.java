package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAIMate;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAITarget;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.HippogryphAIWander;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.entity.util.IDragonFlute;
import com.github.alexthe666.iceandfire.entity.util.IDropArmor;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.inventory.ContainerHippogryph;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageHippogryphArmor;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class EntityHippogryph extends TamableAnimal implements ISyncMount, IAnimatedEntity, IDragonFlute, IVillagerFear, IAnimalFear, IDropArmor, IFlyingMount, ICustomMoveController, IHasCustomizableAttributes {

    private static final int FLIGHT_CHANCE_PER_TICK = 1200;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SADDLE = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ARMOR = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HOVERING = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityHippogryph.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_EAT;

    public static Animation ANIMATION_SPEAK;

    public static Animation ANIMATION_SCRATCH;

    public static Animation ANIMATION_BITE;

    public SimpleContainer hippogryphInventory;

    public float sitProgress;

    public float hoverProgress;

    public float flyProgress;

    public int spacebarTicks;

    public int airBorneCounter;

    public BlockPos homePos;

    public boolean hasHomePosition = false;

    public int feedings = 0;

    private boolean isLandNavigator;

    private boolean isSitting;

    private boolean isHovering;

    private boolean isFlying;

    private int animationTick;

    private Animation currentAnimation;

    private int flyTicks;

    private int hoverTicks;

    private boolean hasChestVarChanged = false;

    private boolean isOverAir;

    public EntityHippogryph(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.switchNavigator(true);
        ANIMATION_EAT = Animation.create(25);
        ANIMATION_SPEAK = Animation.create(15);
        ANIMATION_SCRATCH = Animation.create(25);
        ANIMATION_BITE = Animation.create(20);
        this.initHippogryphInv();
        this.m_274367_(1.0F);
    }

    public static int getIntFromArmor(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.IRON_HIPPOGRYPH_ARMOR.get()) {
            return 1;
        } else if (!stack.isEmpty() && stack.getItem() == IafItemRegistry.GOLD_HIPPOGRYPH_ARMOR.get()) {
            return 2;
        } else {
            return !stack.isEmpty() && stack.getItem() == IafItemRegistry.DIAMOND_HIPPOGRYPH_ARMOR.get() ? 3 : 0;
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.FLYING_SPEED, IafConfig.hippogryphFlightSpeedMod).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.FLYING_SPEED).setBaseValue(IafConfig.hippogryphFlightSpeedMod);
    }

    protected boolean isOverAir() {
        return this.isOverAir;
    }

    private boolean isOverAirLogic() {
        return this.m_9236_().m_46859_(BlockPos.containing((double) this.m_146903_(), this.m_20191_().minY - 1.0, (double) this.m_146907_()));
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.2, true));
        this.f_21345_.addGoal(4, new HippogryphAIMate(this, 1.0));
        this.f_21345_.addGoal(5, new TemptGoal(this, 1.0, Ingredient.of(IafItemTags.TEMPT_HIPPOGRYPH), false));
        this.f_21345_.addGoal(6, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.f_21345_.addGoal(7, new HippogryphAIWander(this, 1.0));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new HippogryphAITargetItems(this, false));
        this.f_21346_.addGoal(5, new HippogryphAITarget(this, LivingEntity.class, false, new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && !(entity instanceof AbstractHorse) && DragonUtils.isAlive((LivingEntity) entity);
            }
        }));
        this.f_21346_.addGoal(5, new HippogryphAITarget(this, Player.class, 350, false, new Predicate<Player>() {

            public boolean apply(@Nullable Player entity) {
                return !entity.isCreative();
            }
        }));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(ARMOR, 0);
        this.f_19804_.define(SADDLE, Boolean.FALSE);
        this.f_19804_.define(CHESTED, Boolean.FALSE);
        this.f_19804_.define(HOVERING, Boolean.FALSE);
        this.f_19804_.define(FLYING, Boolean.FALSE);
        this.f_19804_.define(CONTROL_STATE, (byte) 0);
        this.f_19804_.define(COMMAND, 0);
    }

    @Override
    public double getYSpeedMod() {
        return 4.0;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            this.f_20883_ = this.m_146908_();
            this.m_5616_(passenger.getYHeadRot());
            this.m_5618_(passenger.getYRot());
        }
        passenger.setPos(this.m_20185_(), this.m_20186_() + 1.05F, this.m_20189_());
    }

    private void initHippogryphInv() {
        SimpleContainer animalchest = this.hippogryphInventory;
        this.hippogryphInventory = new SimpleContainer(18);
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.hippogryphInventory.getContainerSize());
            for (int j = 0; j < i; j++) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.hippogryphInventory.setItem(j, itemstack.copy());
                }
            }
            if (this.m_9236_().isClientSide) {
                ItemStack saddle = animalchest.getItem(0);
                ItemStack chest = animalchest.getItem(1);
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.m_19879_(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.m_19879_(), 1, chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty() ? 1 : 0));
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.m_19879_(), 2, getIntFromArmor(animalchest.getItem(2))));
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player && this.m_5448_() != passenger) {
                Player player = (Player) passenger;
                if (this.m_21824_() && this.m_21805_() != null && this.m_21805_().equals(player.m_20148_())) {
                    return player;
                }
            }
        }
        return null;
    }

    public boolean isBlinking() {
        return this.f_19797_ % 50 > 43;
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        String s = ChatFormatting.stripFormatting(player.getName().getString());
        boolean isDev = s.equals("Alexthe666") || s.equals("Raptorfarian") || s.equals("tweakbsd");
        if (this.m_21824_() && this.m_21830_(player)) {
            if (itemstack.getItem() == Items.RED_DYE && this.getEnumVariant() != EnumHippogryphTypes.ALEX && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.ALEX);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.m_5496_(SoundEvents.ZOMBIE_INFECT, 1.0F, 1.0F);
                for (int i = 0; i < 20; i++) {
                    this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), 0.0, 0.0, 0.0);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem() == Items.LIGHT_GRAY_DYE && this.getEnumVariant() != EnumHippogryphTypes.RAPTOR && isDev) {
                this.setEnumVariant(EnumHippogryphTypes.RAPTOR);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.m_5496_(SoundEvents.ZOMBIE_INFECT, 1.0F, 1.0F);
                for (int i = 0; i < 20; i++) {
                    this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), 0.0, 0.0, 0.0);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.is(IafItemTags.BREED_HIPPOGRYPH) && this.m_146764_() == 0 && !this.m_27593_()) {
                this.m_27595_(player);
                this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem() == Items.STICK) {
                if (player.m_6144_()) {
                    if (this.hasHomePosition) {
                        this.hasHomePosition = false;
                        player.displayClientMessage(Component.translatable("hippogryph.command.remove_home"), true);
                        return InteractionResult.SUCCESS;
                    }
                    this.homePos = this.m_20183_();
                    this.hasHomePosition = true;
                    player.displayClientMessage(Component.translatable("hippogryph.command.new_home", this.homePos.m_123341_(), this.homePos.m_123342_(), this.homePos.m_123343_()), true);
                    return InteractionResult.SUCCESS;
                }
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() > 1) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("hippogryph.command." + (this.getCommand() == 1 ? "sit" : "stand")), true);
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem() == Items.GLISTERING_MELON_SLICE && this.getEnumVariant() != EnumHippogryphTypes.DODO) {
                this.setEnumVariant(EnumHippogryphTypes.DODO);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.m_5496_(SoundEvents.ZOMBIE_INFECT, 1.0F, 1.0F);
                for (int i = 0; i < 20; i++) {
                    this.m_9236_().addParticle(ParticleTypes.ENCHANT, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), 0.0, 0.0, 0.0);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.getItem().isEdible() && itemstack.getItem().getFoodProperties() != null && itemstack.getItem().getFoodProperties().isMeat() && this.m_21223_() < this.m_21233_()) {
                this.m_5634_(5.0F);
                this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                for (int i = 0; i < 3; i++) {
                    this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemstack), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), 0.0, 0.0, 0.0);
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                if (player.m_6144_()) {
                    this.openGUI(player);
                    return InteractionResult.SUCCESS;
                }
                if (this.isSaddled() && !this.m_6162_() && !player.m_20159_()) {
                    player.m_7998_(this, true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.m_6071_(player, hand);
    }

    public void openGUI(Player playerEntity) {
        if (!this.m_9236_().isClientSide && (!this.m_20160_() || this.m_20363_(playerEntity))) {
            NetworkHooks.openScreen((ServerPlayer) playerEntity, new MenuProvider() {

                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, @NotNull Inventory p_createMenu_2_, @NotNull Player p_createMenu_3_) {
                    return new ContainerHippogryph(p_createMenu_1_, EntityHippogryph.this.hippogryphInventory, p_createMenu_2_, EntityHippogryph.this);
                }

                @NotNull
                @Override
                public Component getDisplayName() {
                    return Component.translatable("entity.iceandfire.hippogryph");
                }
            });
        }
        IceAndFire.PROXY.setReferencedMob(this);
    }

    @Override
    public boolean isGoingUp() {
        return (this.f_19804_.get(CONTROL_STATE) & 1) == 1;
    }

    @Override
    public boolean isGoingDown() {
        return (this.f_19804_.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (this.f_19804_.get(CONTROL_STATE) >> 2 & 1) == 1;
    }

    public boolean dismountIAF() {
        return (this.f_19804_.get(CONTROL_STATE) >> 3 & 1) == 1;
    }

    @Override
    public void up(boolean up) {
        this.setStateField(0, up);
    }

    @Override
    public void down(boolean down) {
        this.setStateField(1, down);
    }

    @Override
    public void attack(boolean attack) {
        this.setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {
    }

    @Override
    public void dismount(boolean dismount) {
        this.setStateField(3, dismount);
    }

    private void setStateField(int i, boolean newState) {
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

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putBoolean("Hovering", this.isHovering());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("Armor", this.getArmor());
        compound.putInt("Feedings", this.feedings);
        if (this.hippogryphInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.hippogryphInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
        compound.putBoolean("HasHomePosition", this.hasHomePosition);
        if (this.homePos != null && this.hasHomePosition) {
            compound.putInt("HomeAreaX", this.homePos.m_123341_());
            compound.putInt("HomeAreaY", this.homePos.m_123342_());
            compound.putInt("HomeAreaZ", this.homePos.m_123343_());
        }
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setChested(compound.getBoolean("Chested"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setArmor(compound.getInt("Armor"));
        this.feedings = compound.getInt("Feedings");
        if (this.hippogryphInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.hippogryphInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        } else {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initHippogryphInv();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initHippogryphInv();
                this.hippogryphInventory.setItem(j, ItemStack.of(CompoundNBT));
                ItemStack saddle = this.hippogryphInventory.getItem(0);
                ItemStack chest = this.hippogryphInventory.getItem(1);
                if (this.m_9236_().isClientSide) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.m_19879_(), 0, saddle != null && saddle.getItem() == Items.SADDLE && !saddle.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.m_19879_(), 1, chest != null && chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty() ? 1 : 0));
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageHippogryphArmor(this.m_19879_(), 2, getIntFromArmor(this.hippogryphInventory.getItem(2))));
                }
            }
        }
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (this.hasHomePosition && compound.getInt("HomeAreaX") != 0 && compound.getInt("HomeAreaY") != 0 && compound.getInt("HomeAreaZ") != 0) {
            this.homePos = new BlockPos(compound.getInt("HomeAreaX"), compound.getInt("HomeAreaY"), compound.getInt("HomeAreaZ"));
        }
        this.setCommand(compound.getInt("Command"));
        if (this.isOrderedToSit()) {
            this.sitProgress = 20.0F;
        }
        this.setConfigurableAttributes();
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public EnumHippogryphTypes getEnumVariant() {
        return EnumHippogryphTypes.values()[this.getVariant()];
    }

    public void setEnumVariant(EnumHippogryphTypes variant) {
        this.setVariant(variant.ordinal());
    }

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
        this.hasChestVarChanged = true;
    }

    @Override
    public boolean isOrderedToSit() {
        if (this.m_9236_().isClientSide) {
            boolean isSitting = (this.f_19804_.<Byte>get(f_21798_) & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        } else {
            return this.isSitting;
        }
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        if (!this.m_9236_().isClientSide) {
            this.isSitting = sitting;
        }
        byte b0 = this.f_19804_.<Byte>get(f_21798_);
        if (sitting) {
            this.f_19804_.set(f_21798_, (byte) (b0 | 1));
        } else {
            this.f_19804_.set(f_21798_, (byte) (b0 & -2));
        }
    }

    @Override
    public boolean isHovering() {
        return this.m_9236_().isClientSide ? (this.isHovering = this.f_19804_.get(HOVERING)) : this.isHovering;
    }

    public void setHovering(boolean hovering) {
        this.f_19804_.set(HOVERING, hovering);
        if (!this.m_9236_().isClientSide) {
            this.isHovering = hovering;
        }
    }

    @Nullable
    @Override
    public Player getRidingPlayer() {
        return this.getControllingPassenger() instanceof Player ? (Player) this.getControllingPassenger() : null;
    }

    @Override
    public double getFlightSpeedModifier() {
        return IafConfig.hippogryphFlightSpeedMod * 0.9F;
    }

    @Override
    public boolean isFlying() {
        return this.m_9236_().isClientSide ? (this.isFlying = this.f_19804_.get(FLYING)) : this.isFlying;
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
        if (!this.m_9236_().isClientSide) {
            this.isFlying = flying;
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

    public boolean canMove() {
        return !this.isOrderedToSit() && this.getControllingPassenger() == null && this.sitProgress == 0.0F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEnumVariant(EnumHippogryphTypes.getBiomeType(worldIn.m_204166_(this.m_20183_())));
        return data;
    }

    @Override
    public boolean hurt(@NotNull DamageSource dmg, float i) {
        return this.m_20160_() && dmg.getEntity() != null && this.getControllingPassenger() != null && dmg.getEntity() == this.getControllingPassenger() ? false : super.m_6469_(dmg, i);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
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

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return !this.isFlying() && !this.isHovering() ? (float) this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.75F : (float) this.m_21133_(Attributes.FLYING_SPEED);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.HIPPOGRYPH_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.HIPPOGRYPH_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.HIPPOGRYPH_DIE;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { IAnimatedEntity.NO_ANIMATION, ANIMATION_EAT, ANIMATION_BITE, ANIMATION_SPEAK, ANIMATION_SCRATCH };
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.m_6109_()) {
            if (this.m_20069_()) {
                this.m_19920_(0.02F, pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.8F));
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.5));
            } else if (!this.isFlying() && !this.isHovering()) {
                super.m_7023_(pTravelVector);
            } else {
                this.m_19920_(0.1F, pTravelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.9));
            }
        } else {
            super.m_7023_(pTravelVector);
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
            float vertical = this.isGoingUp() ? 0.2F : (this.isGoingDown() ? -0.2F : 0.0F);
            if (!this.isFlying() && !this.isHovering()) {
                vertical = (float) travelVector.y;
            }
            this.m_20256_(vec3.add(0.0, (double) vertical, 0.0));
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
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_SCRATCH && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isOrderedToSit() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setOrderedToSit(false);
            }
            if (!this.isOrderedToSit() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setOrderedToSit(true);
            }
            if (this.isOrderedToSit()) {
                this.m_21573_().stop();
            }
            if (this.f_19796_.nextInt(900) == 0 && this.f_20919_ == 0) {
                this.m_5634_(1.0F);
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.m_5448_() != null && this.getAnimationTick() == 6) {
            double dist = this.m_20280_(this.m_5448_());
            if (dist < 8.0) {
                this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        LivingEntity attackTarget = this.m_5448_();
        if (this.getAnimation() == ANIMATION_SCRATCH && attackTarget != null && this.getAnimationTick() == 6) {
            double dist = this.m_20280_(attackTarget);
            if (dist < 8.0) {
                attackTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                attackTarget.f_19812_ = true;
                float f = Mth.sqrt(0.5F);
                attackTarget.m_20256_(attackTarget.m_20184_().add(-0.5 / (double) f, 1.0, -0.5 / (double) f));
                attackTarget.m_20256_(attackTarget.m_20184_().multiply(0.5, 1.0, 0.5));
                if (attackTarget.m_20096_()) {
                    attackTarget.m_20256_(attackTarget.m_20184_().add(0.0, 0.3, 0.0));
                }
            }
        }
        if (!this.m_9236_().isClientSide && !this.isOverAir() && this.m_21573_().isDone() && attackTarget != null && attackTarget.m_20186_() - 3.0 > this.m_20186_() && this.m_217043_().nextInt(15) == 0 && this.canMove() && !this.isHovering() && !this.isFlying()) {
            this.setHovering(true);
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        if (this.isOverAir()) {
            this.airBorneCounter++;
        } else {
            this.airBorneCounter = 0;
        }
        if (this.hasChestVarChanged && this.hippogryphInventory != null && !this.isChested()) {
            for (int i = 3; i < 18; i++) {
                if (!this.hippogryphInventory.getItem(i).isEmpty()) {
                    if (!this.m_9236_().isClientSide) {
                        this.m_5552_(this.hippogryphInventory.getItem(i), 1.0F);
                    }
                    this.hippogryphInventory.removeItemNoUpdate(i);
                }
            }
            this.hasChestVarChanged = false;
        }
        if (this.isFlying() && this.f_19797_ % 40 == 0 || this.isFlying() && this.isOrderedToSit()) {
            this.setFlying(true);
        }
        if (!this.canMove() && attackTarget != null) {
            this.m_6710_(null);
        }
        if (!this.canMove()) {
            this.m_21573_().stop();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        boolean sitting = this.isOrderedToSit() && !this.isHovering() && !this.isFlying();
        if (sitting && this.sitProgress < 20.0F) {
            this.sitProgress += 0.5F;
        } else if (!sitting && this.sitProgress > 0.0F) {
            this.sitProgress -= 0.5F;
        }
        boolean hovering = this.isHovering();
        if (hovering && this.hoverProgress < 20.0F) {
            this.hoverProgress += 0.5F;
        } else if (!hovering && this.hoverProgress > 0.0F) {
            this.hoverProgress -= 0.5F;
        }
        boolean flying = this.isFlying() || this.isHovering() && this.airBorneCounter > 10;
        if (flying && this.flyProgress < 20.0F) {
            this.flyProgress += 0.5F;
        } else if (!flying && this.flyProgress > 0.0F) {
            this.flyProgress -= 0.5F;
        }
        if (flying && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!flying && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if ((flying || hovering) && !this.doesWantToLand() && this.getControllingPassenger() == null) {
            double up = this.m_20069_() ? 0.16 : 0.08;
            this.m_20256_(this.m_20184_().add(0.0, up, 0.0));
        }
        if ((flying || hovering) && this.f_19797_ % 20 == 0 && this.isOverAir()) {
            this.m_5496_(SoundEvents.ENDER_DRAGON_FLAP, this.m_6121_() * (float) (IafConfig.dragonFlapNoiseDistance / 2), 0.6F + this.f_19796_.nextFloat() * 0.6F * this.m_6100_());
        }
        if (this.m_20096_() && this.doesWantToLand() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.isHovering()) {
            if (this.isOrderedToSit()) {
                this.setHovering(false);
            }
            this.hoverTicks++;
            if (this.doesWantToLand()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.05, 0.0));
            } else {
                if (this.getControllingPassenger() == null) {
                    this.m_20256_(this.m_20184_().add(0.0, 0.08, 0.0));
                }
                if (this.hoverTicks > 40) {
                    if (!this.m_6162_()) {
                        this.setFlying(true);
                    }
                    this.setHovering(false);
                    this.hoverTicks = 0;
                    this.flyTicks = 0;
                }
            }
        }
        if (this.isOrderedToSit()) {
            this.m_21573_().stop();
        }
        if (this.m_20096_() && this.flyTicks != 0) {
            this.flyTicks = 0;
        }
        if (this.isFlying() && this.doesWantToLand() && this.getControllingPassenger() == null) {
            this.setHovering(false);
            if (this.m_20096_()) {
                this.flyTicks = 0;
            }
            this.setFlying(false);
        }
        if (this.isFlying()) {
            this.flyTicks++;
        }
        if ((this.isHovering() || this.isFlying()) && this.isOrderedToSit()) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.m_20160_() && this.isGoingDown() && this.m_20096_()) {
            this.setHovering(false);
            this.setFlying(false);
        }
        if (!this.m_9236_().isClientSide && this.m_217043_().nextInt(1200) == 0 && !this.isOrderedToSit() && !this.isFlying() && this.m_20197_().isEmpty() && !this.m_6162_() && !this.isHovering() && !this.isOrderedToSit() && this.canMove() && !this.isOverAir() || this.m_20186_() < -1.0) {
            this.setHovering(true);
            this.hoverTicks = 0;
            this.flyTicks = 0;
        }
        if (this.m_5448_() != null && !this.m_20197_().isEmpty() && this.m_269323_() != null && this.m_20197_().contains(this.m_269323_())) {
            this.m_6710_(null);
        }
    }

    public boolean doesWantToLand() {
        return (this.flyTicks > 200 || this.flyTicks > 40 && this.flyProgress == 0.0F) && !this.m_20160_();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.isOverAir = this.isOverAirLogic();
        if (this.isGoingUp()) {
            if (this.airBorneCounter == 0) {
                this.m_20256_(this.m_20184_().add(0.0, 0.02F, 0.0));
            }
            if (!this.isFlying() && !this.isHovering()) {
                this.spacebarTicks += 2;
            }
        } else if (this.dismountIAF() && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            LivingEntity target = DragonUtils.riderLookingAtEntity(this, (Player) this.getControllingPassenger(), 3.0);
            if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_SCRATCH) {
                this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
            }
            if (target != null && this.getAnimationTick() >= 10 && this.getAnimationTick() < 13) {
                target.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getControllingPassenger() != null && this.getControllingPassenger().m_6144_()) {
            this.getControllingPassenger().stopRiding();
        }
        double motion = this.m_20184_().x * this.m_20184_().x + this.m_20184_().z * this.m_20184_().z;
        if (this.isFlying() && !this.isHovering() && this.getControllingPassenger() != null && this.isOverAir() && motion < 0.01F) {
            this.setHovering(true);
            this.setFlying(false);
        }
        if (this.isHovering() && !this.isFlying() && this.getControllingPassenger() != null && this.isOverAir() && motion > 0.01F) {
            this.setFlying(true);
            this.setHovering(false);
        }
        if (this.spacebarTicks > 0) {
            this.spacebarTicks--;
        }
        if (this.spacebarTicks > 10 && this.m_269323_() != null && this.m_20197_().contains(this.m_269323_()) && !this.isFlying() && !this.isHovering()) {
            this.setHovering(true);
        }
        if (this.m_5448_() != null && this.m_20202_() == null && !this.m_5448_().isAlive() || this.m_5448_() != null && this.m_5448_() instanceof EntityDragonBase && !this.m_5448_().isAlive()) {
            this.m_6710_(null);
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        if (target != null) {
            BlockHitResult rayTrace = this.m_9236_().m_45547_(new ClipContext(this.m_20299_(1.0F), target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            BlockPos pos = rayTrace.getBlockPos();
            return !this.m_9236_().m_46859_(pos);
        } else {
            return false;
        }
    }

    public float getDistanceSquared(Vec3 Vector3d) {
        float f = (float) (this.m_20185_() - Vector3d.x);
        float f1 = (float) (this.m_20186_() - Vector3d.y);
        float f2 = (float) (this.m_20189_() - Vector3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        if (this.hippogryphInventory != null && !this.m_9236_().isClientSide) {
            for (int i = 0; i < this.hippogryphInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.m_5552_(itemstack, 0.0F);
                }
            }
        }
    }

    public void refreshInventory() {
        if (!this.m_9236_().isClientSide) {
            ItemStack saddle = this.hippogryphInventory.getItem(0);
            ItemStack chest = this.hippogryphInventory.getItem(1);
            this.setSaddled(saddle.getItem() == Items.SADDLE && !saddle.isEmpty());
            this.setChested(chest.getItem() == Blocks.CHEST.asItem() && !chest.isEmpty());
            this.setArmor(getIntFromArmor(this.hippogryphInventory.getItem(2)));
        }
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.CLIMBING);
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlyingMoveControl(this, 10, true);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.FLYING);
            this.isLandNavigator = false;
        }
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level worldIn) {
        return this.createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return this.createNavigator(worldIn, type, 2.0F, 2.0F);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, this.m_9236_(), type, width, height);
        this.f_21344_ = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.m_26575_().setCanOpenDoors(true);
        return newNavigator;
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
    public void onHearFlute(Player player) {
        if (this.m_21824_() && this.m_21830_(player) && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return DragonUtils.canTameDragonAttack(this, entity);
    }

    @Override
    public void dropArmor() {
        if (this.hippogryphInventory != null && !this.m_9236_().isClientSide) {
            for (int i = 0; i < this.hippogryphInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.hippogryphInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.m_5552_(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }
}