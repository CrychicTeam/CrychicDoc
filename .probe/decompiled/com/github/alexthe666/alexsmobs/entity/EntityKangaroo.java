package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRideParent;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.KangarooAIMelee;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwner;
import com.github.alexthe666.alexsmobs.message.MessageKangarooEat;
import com.github.alexthe666.alexsmobs.message.MessageKangarooInventorySync;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.network.NetworkHooks;

public class EntityKangaroo extends TamableAnimal implements ContainerListener, IAnimatedEntity, IFollower {

    public static final Animation ANIMATION_EAT_GRASS = Animation.create(30);

    public static final Animation ANIMATION_KICK = Animation.create(15);

    public static final Animation ANIMATION_PUNCH_R = Animation.create(13);

    public static final Animation ANIMATION_PUNCH_L = Animation.create(13);

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VISUAL_FLAG = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> POUCH_TICK = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> HELMET_INDEX = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SWORD_INDEX = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> CHEST_INDEX = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FORCED_SIT = SynchedEntityData.defineId(EntityKangaroo.class, EntityDataSerializers.BOOLEAN);

    public float prevPouchProgress;

    public float pouchProgress;

    public float sitProgress;

    public float prevSitProgress;

    public float standProgress;

    public float prevStandProgress;

    public float totalMovingProgress;

    public float prevTotalMovingProgress;

    public int maxStandTime = 75;

    public SimpleContainer kangarooInventory;

    private int animationTick;

    private Animation currentAnimation;

    private int jumpTicks;

    private int jumpDuration;

    private boolean wasOnGround;

    private int currentMoveTypeDuration;

    private int standingTime = 0;

    private int sittingTime = 0;

    private int maxSitTime = 75;

    private int eatCooldown = 0;

    private int carrotFeedings = 0;

    private int clientArmorCooldown = 0;

    protected EntityKangaroo(EntityType type, Level world) {
        super(type, world);
        this.initKangarooInventory();
        this.f_21343_ = new EntityKangaroo.JumpHelperController(this);
        this.f_21342_ = new EntityKangaroo.MoveHelperController(this);
    }

    public static <T extends Mob> boolean canKangarooSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.KANGAROO_SPAWNS);
        return spawnBlock && worldIn.m_45524_(pos, 0) > 8;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 22.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected void tickLeash() {
        super.m_6119_();
        Entity lvt_1_1_ = this.m_21524_();
        if (lvt_1_1_ != null && lvt_1_1_.level() == this.m_9236_()) {
            this.m_21446_(lvt_1_1_.blockPosition(), 5);
            float lvt_2_1_ = this.m_20270_(lvt_1_1_);
            if (this.isSitting()) {
                if (lvt_2_1_ > 10.0F) {
                    this.m_21455_(true, true);
                }
                return;
            }
            this.m_7880_(lvt_2_1_);
            if (lvt_2_1_ > 10.0F) {
                this.m_21455_(true, true);
                this.f_21345_.disableControlFlag(Goal.Flag.MOVE);
            } else if (lvt_2_1_ > 6.0F) {
                double lvt_3_1_ = (lvt_1_1_.getX() - this.m_20185_()) / (double) lvt_2_1_;
                double lvt_5_1_ = (lvt_1_1_.getY() - this.m_20186_()) / (double) lvt_2_1_;
                double lvt_7_1_ = (lvt_1_1_.getZ() - this.m_20189_()) / (double) lvt_2_1_;
                this.m_20256_(this.m_20184_().add(Math.copySign(lvt_3_1_ * lvt_3_1_ * 0.4, lvt_3_1_), Math.copySign(lvt_5_1_ * lvt_5_1_ * 0.4, lvt_5_1_), Math.copySign(lvt_7_1_ * lvt_7_1_ * 0.4, lvt_7_1_)));
            } else {
                this.f_21345_.enableControlFlag(Goal.Flag.MOVE);
                float lvt_3_2_ = 2.0F;
                try {
                    Vec3 lvt_4_1_ = new Vec3(lvt_1_1_.getX() - this.m_20185_(), lvt_1_1_.getY() - this.m_20186_(), lvt_1_1_.getZ() - this.m_20189_()).normalize().scale((double) Math.max(lvt_2_1_ - 2.0F, 0.0F));
                    this.getNavigation().moveTo(this.m_20185_() + lvt_4_1_.x, this.m_20186_() + lvt_4_1_.y, this.m_20189_() + lvt_4_1_.z, this.m_5823_());
                } catch (Exception var9) {
                }
            }
        }
    }

    public boolean forcedSit() {
        return this.f_19804_.get(FORCED_SIT);
    }

    public boolean isRoger() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.equalsIgnoreCase("roger");
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.kangarooSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.KANGAROO_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.KANGAROO_IDLE.get();
    }

    private void initKangarooInventory() {
        SimpleContainer animalchest = this.kangarooInventory;
        this.kangarooInventory = new SimpleContainer(9) {

            @Override
            public void stopOpen(Player player) {
                EntityKangaroo.this.f_19804_.set(EntityKangaroo.POUCH_TICK, 10);
                EntityKangaroo.this.resetKangarooSlots();
            }

            @Override
            public boolean stillValid(Player player) {
                return EntityKangaroo.this.m_6084_() && !EntityKangaroo.this.f_19817_;
            }
        };
        this.kangarooInventory.addListener(this);
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.kangarooInventory.getContainerSize());
            for (int j = 0; j < i; j++) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.kangarooInventory.setItem(j, itemstack.copy());
                }
            }
            this.resetKangarooSlots();
        }
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        for (int i = 0; i < this.kangarooInventory.getContainerSize(); i++) {
            this.m_19983_(this.kangarooInventory.getItem(i));
        }
        this.kangarooInventory.clearContent();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && item == Items.CARROT) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.HORSE_EAT, this.m_6121_(), this.m_6100_());
            this.carrotFeedings++;
            if ((this.carrotFeedings <= 10 || this.m_217043_().nextInt(2) != 0) && this.carrotFeedings <= 15) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            } else {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && this.m_21223_() < this.m_21233_() && item.isEdible() && item.getFoodProperties() != null && !item.getFoodProperties().isMeat()) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.HORSE_EAT, this.m_6121_(), this.m_6100_());
            this.m_5634_((float) item.getFoodProperties().getNutrition());
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult == InteractionResult.SUCCESS || type == InteractionResult.SUCCESS || !this.m_21824_() || !this.m_21830_(player) || this.isFood(itemstack)) {
                return type;
            } else if (player.m_6144_()) {
                if (!this.m_6162_()) {
                    this.openGUI(player);
                    this.m_20153_();
                    this.f_19804_.set(POUCH_TICK, -1);
                }
                return InteractionResult.SUCCESS;
            } else {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.f_19804_.set(FORCED_SIT, true);
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.f_19804_.set(FORCED_SIT, false);
                    this.maxSitTime = 0;
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("KangarooSitting", this.isSitting());
        compound.putBoolean("KangarooSittingForced", this.forcedSit());
        compound.putBoolean("Standing", this.isStanding());
        compound.putInt("Command", this.getCommand());
        compound.putInt("HelmetInvIndex", this.f_19804_.get(HELMET_INDEX));
        compound.putInt("SwordInvIndex", this.f_19804_.get(SWORD_INDEX));
        compound.putInt("ChestInvIndex", this.f_19804_.get(CHEST_INDEX));
        if (this.kangarooInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.kangarooInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.kangarooInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("KangarooSitting"));
        this.f_19804_.set(FORCED_SIT, compound.getBoolean("KangarooSittingForced"));
        this.setStanding(compound.getBoolean("Standing"));
        this.setCommand(compound.getInt("Command"));
        this.f_19804_.set(HELMET_INDEX, compound.getInt("HelmetInvIndex"));
        this.f_19804_.set(SWORD_INDEX, compound.getInt("SwordInvIndex"));
        this.f_19804_.set(CHEST_INDEX, compound.getInt("ChestInvIndex"));
        if (this.kangarooInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initKangarooInventory();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.kangarooInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        } else {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initKangarooInventory();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initKangarooInventory();
                this.kangarooInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        }
        this.resetKangarooSlots();
    }

    public void openGUI(Player playerEntity) {
        if (!this.m_9236_().isClientSide && !this.m_20363_(playerEntity)) {
            NetworkHooks.openScreen((ServerPlayer) playerEntity, new MenuProvider() {

                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
                    return new DispenserMenu(p_createMenu_1_, p_createMenu_2_, EntityKangaroo.this.kangarooInventory);
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable("entity.alexsmobs.kangaroo.pouch");
                }
            });
        }
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public int getVisualFlag() {
        return this.f_19804_.get(VISUAL_FLAG);
    }

    public void setVisualFlag(int visualFlag) {
        this.f_19804_.set(VISUAL_FLAG, visualFlag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(STANDING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(FORCED_SIT, false);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(VISUAL_FLAG, 0);
        this.f_19804_.define(POUCH_TICK, 0);
        this.f_19804_.define(CHEST_INDEX, -1);
        this.f_19804_.define(HELMET_INDEX, -1);
        this.f_19804_.define(SWORD_INDEX, -1);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigatorWide(this, worldIn, 2.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new KangarooAIMelee(this, 1.2, false));
        this.f_21345_.addGoal(2, new FloatGoal(this));
        this.f_21345_.addGoal(2, new TameableAIFollowOwner(this, 1.2, 5.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new AnimalAIRideParent(this, 1.25));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(Items.CARROT), false));
        this.f_21345_.addGoal(5, new AnimalAIWanderRanged(this, 110, 1.2, 10, 7));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new AnimalAIHurtByTargetNotBaby(this));
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return super.m_7310_(passenger) && this.f_19804_.get(POUCH_TICK) == 0;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_() * 0.35F;
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.updateClientInventory();
    }

    @Override
    public void tick() {
        super.m_8119_();
        boolean moving = this.m_20184_().lengthSqr() > 0.03;
        int pouchTick = this.f_19804_.get(POUCH_TICK);
        this.prevTotalMovingProgress = this.totalMovingProgress;
        this.prevPouchProgress = this.pouchProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevStandProgress = this.standProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.eatCooldown > 0) {
            this.eatCooldown--;
        }
        if (this.isStanding()) {
            if (this.standProgress < 5.0F) {
                this.standProgress++;
            }
        } else if (this.standProgress > 0.0F) {
            this.standProgress--;
        }
        if (moving) {
            if (this.totalMovingProgress < 5.0F) {
                this.totalMovingProgress++;
            }
        } else if (this.totalMovingProgress > 0.0F) {
            this.totalMovingProgress--;
        }
        if (pouchTick != 0 && this.pouchProgress < 5.0F) {
            this.pouchProgress++;
        }
        if (pouchTick == 0 && this.pouchProgress > 0.0F) {
            this.pouchProgress--;
        }
        if (pouchTick > 0) {
            this.f_19804_.set(POUCH_TICK, pouchTick - 1);
        }
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
        }
        if (this.isSitting() && !this.forcedSit() && ++this.sittingTime > this.maxSitTime) {
            this.setOrderedToSit(false);
            this.sittingTime = 0;
            this.maxSitTime = 75 + this.f_19796_.nextInt(50);
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && this.getCommand() != 1 && !this.isStanding() && !this.isSitting() && this.f_19796_.nextInt(1500) == 0) {
            this.maxSitTime = 500 + this.f_19796_.nextInt(350);
            this.setOrderedToSit(true);
        }
        if (!this.forcedSit() && this.isSitting() && (this.m_5448_() != null || this.isStanding())) {
            this.setOrderedToSit(false);
        }
        if (this.getAnimation() == NO_ANIMATION && !this.isStanding() && !this.isSitting() && this.f_19796_.nextInt(1500) == 0) {
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
            this.setStanding(true);
        }
        if (this.forcedSit() && !this.m_20160_() && this.m_21824_()) {
            this.setOrderedToSit(true);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.f_19797_ == 1) {
                this.updateClientInventory();
            }
            if (!moving && this.getAnimation() == NO_ANIMATION && !this.isSitting() && !this.isStanding() && (this.m_217043_().nextInt(180) == 0 || this.m_21223_() < this.m_21233_() && this.m_217043_().nextInt(40) == 0) && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                this.setAnimation(ANIMATION_EAT_GRASS);
            }
            if (this.getAnimation() == ANIMATION_EAT_GRASS && this.getAnimationTick() == 20 && this.m_21223_() < this.m_21233_() && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                this.m_5634_(6.0F);
                this.m_9236_().m_46796_(2001, this.m_20183_().below(), Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                this.m_9236_().setBlock(this.m_20183_().below(), Blocks.DIRT.defaultBlockState(), 2);
            }
            if (this.m_21223_() < this.m_21233_() && this.m_21824_() && this.eatCooldown == 0) {
                this.eatCooldown = 20 + this.f_19796_.nextInt(40);
                if (!this.kangarooInventory.isEmpty()) {
                    ItemStack foodStack = ItemStack.EMPTY;
                    for (int i = 0; i < this.kangarooInventory.getContainerSize(); i++) {
                        ItemStack stack = this.kangarooInventory.getItem(i);
                        if (stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && !stack.getItem().getFoodProperties().isMeat()) {
                            foodStack = stack;
                        }
                    }
                    if (!foodStack.isEmpty() && foodStack.getItem().getFoodProperties() != null) {
                        AlexsMobs.sendMSGToAll(new MessageKangarooEat(this.m_19879_(), foodStack));
                        this.m_5634_((float) (foodStack.getItem().getFoodProperties().getNutrition() * 2));
                        foodStack.shrink(1);
                        this.m_146850_(GameEvent.EAT);
                        this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                    }
                }
            }
        }
        if (this.jumpTicks < this.jumpDuration) {
            this.jumpTicks++;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.m_6862_(false);
        }
        LivingEntity attackTarget = this.m_5448_();
        if (attackTarget != null && this.m_142582_(attackTarget)) {
            if (this.m_20270_(attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 1.0F) {
                if (this.getAnimation() == ANIMATION_KICK && this.getAnimationTick() == 8) {
                    attackTarget.knockback(1.3F, (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                    this.doHurtTarget(this.m_5448_());
                }
                if (this.getAnimation() == ANIMATION_PUNCH_L && this.getAnimationTick() == 6) {
                    float rot = this.m_146908_() + 90.0F;
                    attackTarget.knockback(0.85F, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
                    this.doHurtTarget(this.m_5448_());
                }
                if (this.getAnimation() == ANIMATION_PUNCH_R && this.getAnimationTick() == 6) {
                    float rot = this.m_146908_() - 90.0F;
                    attackTarget.knockback(0.85F, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
                    this.doHurtTarget(this.m_5448_());
                }
            }
            this.m_21391_(attackTarget, 360.0F, 360.0F);
        }
        if (this.m_6162_() && attackTarget != null) {
            this.m_6710_(null);
        }
        if (this.m_20160_()) {
            this.f_19804_.set(POUCH_TICK, 10);
            this.setStanding(true);
            this.maxStandTime = 25;
        }
        if (this.m_20159_()) {
            if (this.m_6162_() && this.m_20202_() instanceof EntityKangaroo) {
                EntityKangaroo mount = (EntityKangaroo) this.m_20202_();
                this.m_146922_(mount.f_20883_);
                this.f_20885_ = mount.f_20883_;
                this.f_20883_ = mount.f_20883_;
            }
            if (this.m_20202_() instanceof EntityKangaroo && !this.m_6162_()) {
                this.m_6038_();
            }
        }
        if (this.clientArmorCooldown > 0) {
            this.clientArmorCooldown--;
        }
        if (this.f_19797_ > 5 && !this.m_9236_().isClientSide && this.clientArmorCooldown == 0 && this.m_21824_()) {
            this.updateClientInventory();
            this.clientArmorCooldown = 20;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean prev = super.m_7327_(entityIn);
        if (prev && !this.m_21205_().isEmpty()) {
            this.damageItem(this.m_21205_());
        }
        return prev;
    }

    @Override
    public boolean hurt(DamageSource src, float amount) {
        boolean prev = super.m_6469_(src, amount);
        if (prev) {
            if (!this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                this.damageItem(this.getItemBySlot(EquipmentSlot.HEAD));
            }
            if (!this.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                this.damageItem(this.getItemBySlot(EquipmentSlot.CHEST));
            }
        }
        return prev;
    }

    private void damageItem(ItemStack stack) {
        if (stack != null) {
            stack.hurt(1, this.m_217043_(), null);
            if (stack.getDamageValue() <= 0) {
                stack.shrink(1);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.m_6673_(source) || source.is(DamageTypes.IN_WALL);
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
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
    public MoveControl getMoveControl() {
        return this.f_21342_;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting() || this.getAnimation() == ANIMATION_EAT_GRASS) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    private void checkLandingDelay() {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }

    @Override
    public PathNavigation getNavigation() {
        return this.f_21344_;
    }

    @Nullable
    @Override
    public Entity getControlledVehicle() {
        return this.m_20202_() instanceof EntityKangaroo ? null : super.m_275832_();
    }

    private void enableJumpControl() {
        if (this.f_21343_ instanceof EntityKangaroo.JumpHelperController) {
            ((EntityKangaroo.JumpHelperController) this.f_21343_).setCanJump(true);
        }
    }

    private void disableJumpControl() {
        if (this.f_21343_ instanceof EntityKangaroo.JumpHelperController) {
            ((EntityKangaroo.JumpHelperController) this.f_21343_).setCanJump(false);
        }
    }

    private void updateMoveTypeDuration() {
        if (this.f_21342_.getSpeedModifier() < 2.0) {
            this.currentMoveTypeDuration = 2;
        } else {
            this.currentMoveTypeDuration = 1;
        }
    }

    private void calculateRotationYaw(double x, double z) {
        this.m_146922_((float) (Mth.atan2(z - this.m_20189_(), x - this.m_20185_()) * 180.0F / (float) Math.PI) - 90.0F);
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    @Override
    public void customServerAiStep() {
        super.m_8024_();
        if (this.currentMoveTypeDuration > 0) {
            this.currentMoveTypeDuration--;
        }
        if (this.m_20096_()) {
            if (!this.wasOnGround) {
                this.m_6862_(false);
                this.checkLandingDelay();
            }
            if (this.currentMoveTypeDuration == 0) {
                LivingEntity livingentity = this.m_5448_();
                if (livingentity != null && this.m_20280_(livingentity) < 16.0) {
                    this.calculateRotationYaw(livingentity.m_20185_(), livingentity.m_20189_());
                    this.f_21342_.setWantedPosition(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_(), this.f_21342_.getSpeedModifier());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }
            if (this.f_21343_ instanceof EntityKangaroo.JumpHelperController rabbitController) {
                if (!rabbitController.getIsJumping()) {
                    if (this.f_21342_.hasWanted() && this.currentMoveTypeDuration == 0) {
                        Path path = this.f_21344_.getPath();
                        new Vec3(this.f_21342_.getWantedX(), this.f_21342_.getWantedY(), this.f_21342_.getWantedZ());
                        if (path != null && !path.isDone()) {
                            Vec3 vector3d = path.getNextEntityPos(this);
                        }
                        this.startJumping();
                    }
                } else if (!rabbitController.canJump()) {
                    this.enableJumpControl();
                }
            }
        }
        this.wasOnGround = this.m_20096_();
    }

    public float getJumpCompletion(float partialTicks) {
        return this.jumpDuration == 0 ? 0.0F : ((float) this.jumpTicks + partialTicks) / (float) this.jumpDuration;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
        if (animation == ANIMATION_KICK) {
            this.setStanding(true);
            this.maxStandTime = 30;
        } else if (animation == ANIMATION_PUNCH_R) {
            this.setStanding(true);
            this.maxStandTime = 15;
        } else if (animation == ANIMATION_PUNCH_L) {
            this.setStanding(true);
            this.maxStandTime = 15;
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_EAT_GRASS, ANIMATION_KICK, ANIMATION_PUNCH_L, ANIMATION_PUNCH_R };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.KANGAROO.get().create(serverWorld);
    }

    public void setMovementSpeed(double newSpeed) {
        this.getNavigation().setSpeedModifier(newSpeed);
        this.f_21342_.setWantedPosition(this.f_21342_.getWantedX(), this.f_21342_.getWantedY(), this.f_21342_.getWantedZ(), newSpeed);
    }

    @Override
    protected float getJumpPower() {
        return 0.5F;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.DEAD_BUSH || item == Items.GRASS;
    }

    public void resetKangarooSlots() {
        if (!this.m_9236_().isClientSide) {
            int swordIndex = -1;
            double swordDamage = 0.0;
            int helmetIndex = -1;
            double helmetArmor = 0.0;
            int chestplateIndex = -1;
            double chestplateArmor = 0.0;
            for (int i = 0; i < this.kangarooInventory.getContainerSize(); i++) {
                ItemStack stack = this.kangarooInventory.getItem(i);
                if (!stack.isEmpty()) {
                    double dmg = this.getDamageForItem(stack);
                    if (dmg > 0.0 && dmg > swordDamage) {
                        swordDamage = dmg;
                        swordIndex = i;
                    }
                    if (stack.getItem().canEquip(stack, EquipmentSlot.HEAD, this) && !this.m_6162_() && helmetIndex == -1) {
                        helmetIndex = i;
                    }
                    if (stack.getItem() instanceof ArmorItem && !this.m_6162_()) {
                        ArmorItem armorItem = (ArmorItem) stack.getItem();
                        if (armorItem.getEquipmentSlot() == EquipmentSlot.HEAD) {
                            double prot = this.getProtectionForItem(stack, EquipmentSlot.HEAD);
                            if (prot > 0.0 && prot > helmetArmor) {
                                helmetArmor = prot;
                                helmetIndex = i;
                            }
                        }
                        if (armorItem.getEquipmentSlot() == EquipmentSlot.CHEST) {
                            double prot = this.getProtectionForItem(stack, EquipmentSlot.CHEST);
                            if (prot > 0.0 && prot > chestplateArmor) {
                                chestplateArmor = prot;
                                chestplateIndex = i;
                            }
                        }
                    }
                }
            }
            this.f_19804_.set(SWORD_INDEX, swordIndex);
            this.f_19804_.set(CHEST_INDEX, chestplateIndex);
            this.f_19804_.set(HELMET_INDEX, helmetIndex);
            this.updateClientInventory();
        }
    }

    private void updateClientInventory() {
        if (!this.m_9236_().isClientSide) {
            for (int i = 0; i < 9; i++) {
                AlexsMobs.sendMSGToAll(new MessageKangarooInventorySync(this.m_19879_(), i, this.kangarooInventory.getItem(i)));
            }
        }
    }

    @Nullable
    private Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
        Map<EquipmentSlot, ItemStack> map = null;
        for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
            ItemStack itemstack;
            switch(equipmentslottype.getType()) {
                case HAND:
                    itemstack = this.getItemInHand(equipmentslottype);
                    break;
                case ARMOR:
                    itemstack = this.getArmorInSlot(equipmentslottype);
                    break;
                default:
                    continue;
            }
            ItemStack itemstack1 = this.getItemBySlot(equipmentslottype);
            if (!ItemStack.matches(itemstack1, itemstack)) {
                MinecraftForge.EVENT_BUS.post(new LivingEquipmentChangeEvent(this, equipmentslottype, itemstack, itemstack1));
                if (map == null) {
                    map = Maps.newEnumMap(EquipmentSlot.class);
                }
                map.put(equipmentslottype, itemstack1);
                if (!itemstack.isEmpty()) {
                    this.m_21204_().removeAttributeModifiers(itemstack.getAttributeModifiers(equipmentslottype));
                }
                if (!itemstack1.isEmpty()) {
                    this.m_21204_().addTransientAttributeModifiers(itemstack1.getAttributeModifiers(equipmentslottype));
                }
            }
        }
        return map;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return switch(slotIn.getType()) {
            case HAND ->
                this.getItemInHand(slotIn);
            case ARMOR ->
                this.getArmorInSlot(slotIn);
            default ->
                ItemStack.EMPTY;
        };
    }

    private ItemStack getArmorInSlot(EquipmentSlot slot) {
        int helmIndex = this.f_19804_.get(HELMET_INDEX);
        int chestIndex = this.f_19804_.get(CHEST_INDEX);
        return slot == EquipmentSlot.HEAD && helmIndex >= 0 ? this.kangarooInventory.getItem(helmIndex) : (slot == EquipmentSlot.CHEST && chestIndex >= 0 ? this.kangarooInventory.getItem(chestIndex) : ItemStack.EMPTY);
    }

    private ItemStack getItemInHand(EquipmentSlot slot) {
        int index = this.f_19804_.get(SWORD_INDEX);
        return slot == EquipmentSlot.MAINHAND && index >= 0 ? this.kangarooInventory.getItem(index) : ItemStack.EMPTY;
    }

    public double getDamageForItem(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> map = itemStack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        if (map.isEmpty()) {
            return 0.0;
        } else {
            double d = 0.0;
            for (AttributeModifier mod : map.get(Attributes.ATTACK_DAMAGE)) {
                d += mod.getAmount();
            }
            return d;
        }
    }

    public double getProtectionForItem(ItemStack itemStack, EquipmentSlot type) {
        Multimap<Attribute, AttributeModifier> map = itemStack.getAttributeModifiers(type);
        if (map.isEmpty()) {
            return 0.0;
        } else {
            double d = 0.0;
            for (AttributeModifier mod : map.get(Attributes.ARMOR)) {
                d += mod.getAmount();
            }
            return d;
        }
    }

    @Override
    protected void jumpFromGround() {
        super.m_6135_();
        double d0 = this.f_21342_.getSpeedModifier();
        if (d0 > 0.0) {
            double d1 = this.m_20184_().horizontalDistanceSqr();
            if (d1 < 0.01) {
            }
        }
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 1);
        }
    }

    public boolean hasJumper() {
        return this.f_21343_ instanceof EntityKangaroo.JumpHelperController;
    }

    public void startJumping() {
        if (!this.isSitting() || this.m_20069_()) {
            this.m_6862_(true);
            this.jumpDuration = 16;
            this.jumpTicks = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 1) {
            this.m_20076_();
            this.jumpDuration = 16;
            this.jumpTicks = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public void containerChanged(Container iInventory) {
        this.resetKangarooSlots();
    }

    public static class JumpHelperController extends JumpControl {

        private final EntityKangaroo kangaroo;

        private boolean canJump;

        public JumpHelperController(EntityKangaroo kangaroo) {
            super(kangaroo);
            this.kangaroo = kangaroo;
        }

        public boolean getIsJumping() {
            return this.f_24897_;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean canJumpIn) {
            this.canJump = canJumpIn;
        }

        @Override
        public void tick() {
            if (this.f_24897_) {
                this.kangaroo.startJumping();
                this.f_24897_ = false;
            }
        }
    }

    static class MoveHelperController extends MoveControl {

        private final EntityKangaroo kangaroo;

        private double nextJumpSpeed;

        public MoveHelperController(EntityKangaroo kangaroo) {
            super(kangaroo);
            this.kangaroo = kangaroo;
        }

        @Override
        public void tick() {
            if (this.kangaroo.hasJumper() && this.kangaroo.m_20096_() && !this.kangaroo.f_20899_ && !((EntityKangaroo.JumpHelperController) this.kangaroo.f_21343_).getIsJumping()) {
                this.kangaroo.setMovementSpeed(0.0);
            } else if (this.m_24995_()) {
                this.kangaroo.setMovementSpeed(this.nextJumpSpeed);
            }
            super.tick();
        }

        @Override
        public void setWantedPosition(double x, double y, double z, double speedIn) {
            if (this.kangaroo.m_20069_()) {
                speedIn = 1.5;
            }
            super.setWantedPosition(x, y, z, speedIn);
            if (speedIn > 0.0) {
                this.nextJumpSpeed = speedIn;
            }
        }
    }
}