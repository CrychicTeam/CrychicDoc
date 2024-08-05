package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockMyrmexConnectedResin;
import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.IPassabilityNavigator;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.ICustomSizeNavigator;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.NotNull;

public abstract class EntityMyrmexBase extends Animal implements IAnimatedEntity, Merchant, ICustomSizeNavigator, IPassabilityNavigator, IHasCustomizableAttributes {

    public static final Animation ANIMATION_PUPA_WIGGLE = Animation.create(20);

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityMyrmexBase.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> GROWTH_STAGE = SynchedEntityData.defineId(EntityMyrmexBase.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> VARIANT = SynchedEntityData.defineId(EntityMyrmexBase.class, EntityDataSerializers.BOOLEAN);

    private static final ResourceLocation TEXTURE_DESERT_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_larva.png");

    private static final ResourceLocation TEXTURE_DESERT_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_pupa.png");

    private static final ResourceLocation TEXTURE_JUNGLE_LARVA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_larva.png");

    private static final ResourceLocation TEXTURE_JUNGLE_PUPA = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_pupa.png");

    private final SimpleContainer villagerInventory = new SimpleContainer(8);

    public boolean isEnteringHive = false;

    public boolean isBeingGuarded = false;

    protected int growthTicks = 1;

    @Nullable
    protected MerchantOffers offers;

    private int waitTicks = 0;

    private int animationTick;

    private Animation currentAnimation;

    private MyrmexHive hive;

    private int timeUntilReset;

    private boolean leveledUp;

    @Nullable
    private Player customer;

    public EntityMyrmexBase(EntityType<? extends EntityMyrmexBase> t, Level worldIn) {
        super(t, worldIn);
        this.f_21344_ = this.createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    private static boolean isJungleBiome(Level world, BlockPos position) {
        return BiomeConfig.test(BiomeConfig.jungleMyrmexBiomes, world.m_204166_(position));
    }

    public static boolean haveSameHive(EntityMyrmexBase myrmex, Entity entity) {
        if (entity instanceof EntityMyrmexBase && myrmex.getHive() != null && ((EntityMyrmexBase) entity).getHive() != null && myrmex.isJungle() == ((EntityMyrmexBase) entity).isJungle()) {
            return myrmex.getHive().getCenter() == ((EntityMyrmexBase) entity).getHive().getCenter();
        } else {
            return entity instanceof EntityMyrmexEgg ? myrmex.isJungle() == ((EntityMyrmexEgg) entity).isJungle() : false;
        }
    }

    public static boolean isEdibleBlock(BlockState blockState) {
        return blockState.m_204336_(BlockTags.create(IafTagRegistry.MYRMEX_HARVESTABLES));
    }

    public static int getRandomCaste(Level world, RandomSource random, boolean royal) {
        float rand = random.nextFloat();
        if (royal) {
            if ((double) rand > 0.9) {
                return 2;
            } else if ((double) rand > 0.75) {
                return 3;
            } else {
                return (double) rand > 0.5 ? 1 : 0;
            }
        } else if ((double) rand > 0.8) {
            return 3;
        } else {
            return (double) rand > 0.6 ? 1 : 0;
        }
    }

    @NotNull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public boolean canMove() {
        return this.getGrowthStage() > 1;
    }

    @Override
    public boolean isBaby() {
        return this.getGrowthStage() < 2;
    }

    @Override
    protected void customServerAiStep() {
        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            this.timeUntilReset--;
            if (this.timeUntilReset <= 0) {
                if (this.leveledUp) {
                    this.levelUp();
                    this.leveledUp = false;
                }
                this.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
            }
        }
        if (this.getHive() != null && this.getTradingPlayer() != null) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 14);
            this.getHive().setWorld(this.m_9236_());
        }
        super.customServerAiStep();
    }

    @Override
    public int getExperienceReward() {
        return this.getCasteImportance() * 7 + this.m_9236_().random.nextInt(3);
    }

    @Override
    public boolean hurt(@NotNull DamageSource dmg, float i) {
        if (dmg == this.m_9236_().damageSources().inWall() && this.getGrowthStage() < 2) {
            return false;
        } else {
            if (this.getGrowthStage() < 2) {
                this.setAnimation(ANIMATION_PUPA_WIGGLE);
            }
            return super.hurt(dmg, i);
        }
    }

    @Override
    protected float getJumpPower() {
        return 0.52F;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos) {
        return this.m_9236_().getBlockState(pos.below()).m_60734_() instanceof BlockMyrmexResin ? 10.0F : super.m_21692_(pos);
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level worldIn) {
        return this.createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return this.createNavigator(worldIn, type, this.m_20205_(), this.m_20206_());
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, this.m_9236_(), type, width, height);
        this.f_21344_ = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.m_26575_().setCanOpenDoors(true);
        return newNavigator;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CLIMBING, (byte) 0);
        this.f_19804_.define(GROWTH_STAGE, 2);
        this.f_19804_.define(VARIANT, Boolean.FALSE);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.m_274367_(1.0F);
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        if (this.getGrowthStage() < 2 && this.m_20202_() != null && this.m_20202_() instanceof EntityMyrmexBase) {
            float yaw = this.m_20202_().getYRot();
            this.m_146922_(yaw);
            this.f_20885_ = yaw;
            this.f_20883_ = 0.0F;
            this.f_20884_ = 0.0F;
        }
        if (!this.m_9236_().isClientSide) {
            this.setBesideClimbableBlock(this.f_19862_ && (this.m_20096_() || !this.f_19863_));
        }
        if (this.getGrowthStage() < 2) {
            this.growthTicks++;
            if (this.growthTicks == IafConfig.myrmexLarvaTicks) {
                this.setGrowthStage(this.getGrowthStage() + 1);
                this.growthTicks = 0;
            }
        }
        if (!this.m_9236_().isClientSide && this.getGrowthStage() < 2 && this.m_217043_().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }
        if (this.m_5448_() != null && !(this.m_5448_() instanceof Player) && this.m_21573_().isDone()) {
            this.m_6710_(null);
        }
        if (this.m_5448_() != null && (haveSameHive(this, this.m_5448_()) || this.m_5448_() instanceof TamableAnimal && !this.canAttackTamable((TamableAnimal) this.m_5448_()) || this.m_5448_() instanceof Player && this.getHive() != null && !this.getHive().isPlayerReputationLowEnoughToFight(this.m_5448_().m_20148_()))) {
            this.m_6710_(null);
        }
        if (this.getWaitTicks() > 0) {
            this.setWaitTicks(this.getWaitTicks() - 1);
        }
        if (this.m_21223_() < this.m_21233_() && this.f_19797_ % 500 == 0 && this.isOnResin()) {
            this.m_5634_(1.0F);
            this.m_9236_().broadcastEntityEvent(this, (byte) 76);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GrowthStage", this.getGrowthStage());
        tag.putInt("GrowthTicks", this.growthTicks);
        tag.putBoolean("Variant", this.isJungle());
        if (this.getHive() != null) {
            tag.putUUID("HiveUUID", this.getHive().hiveUUID);
        }
        MerchantOffers merchantoffers = this.getOffers();
        if (!merchantoffers.isEmpty()) {
            tag.put("Offers", merchantoffers.createTag());
        }
        ListTag listnbt = new ListTag();
        for (int i = 0; i < this.villagerInventory.getContainerSize(); i++) {
            ItemStack itemstack = this.villagerInventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundTag()));
            }
        }
        tag.put("Inventory", listnbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setGrowthStage(tag.getInt("GrowthStage"));
        this.growthTicks = tag.getInt("GrowthTicks");
        this.setJungleVariant(tag.getBoolean("Variant"));
        if (tag.hasUUID("HiveUUID")) {
            this.setHive(MyrmexWorldData.get(this.m_9236_()).getHiveFromUUID(tag.getUUID("HiveUUID")));
        }
        if (tag.contains("Offers", 10)) {
            this.offers = new MerchantOffers(tag.getCompound("Offers"));
        }
        ListTag listnbt = tag.getList("Inventory", 10);
        for (int i = 0; i < listnbt.size(); i++) {
            ItemStack itemstack = ItemStack.of(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.villagerInventory.addItem(itemstack);
            }
        }
        this.setConfigurableAttributes();
    }

    public boolean canAttackTamable(TamableAnimal tameable) {
        return tameable.m_269323_() != null && this.getHive() != null ? this.getHive().isPlayerReputationLowEnoughToFight(tameable.getOwnerUUID()) : true;
    }

    public BlockPos getPos() {
        return this.m_20183_();
    }

    public int getGrowthStage() {
        return this.f_19804_.get(GROWTH_STAGE);
    }

    public void setGrowthStage(int stage) {
        this.f_19804_.set(GROWTH_STAGE, stage);
    }

    public int getWaitTicks() {
        return this.waitTicks;
    }

    public void setWaitTicks(int waitTicks) {
        this.waitTicks = waitTicks;
    }

    public boolean isJungle() {
        return this.f_19804_.get(VARIANT);
    }

    public void setJungleVariant(boolean isJungle) {
        this.f_19804_.set(VARIANT, isJungle);
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public boolean isBesideClimbableBlock() {
        return (this.f_19804_.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.f_19804_.get(CLIMBING);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.f_19804_.set(CLIMBING, b0);
    }

    @Override
    public boolean onClimbable() {
        return this.m_21573_() instanceof AdvancedPathNavigate && ((AdvancedPathNavigate) this.m_21573_()).entityOnAndBelowPath(this, new Vec3(1.1, 0.0, 1.1)) ? true : super.m_6147_();
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
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_PUPA_WIGGLE };
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingBase) {
        if (this.getHive() == null || livingBase == null || livingBase instanceof Player && this.getHive().isPlayerReputationLowEnoughToFight(livingBase.m_20148_())) {
            super.m_6703_(livingBase);
        }
        if (this.getHive() != null && livingBase != null) {
            this.getHive().addOrRenewAgressor(livingBase, this.getImportance());
        }
        if (this.getHive() != null && livingBase != null && livingBase instanceof Player) {
            int i = -5 * this.getCasteImportance();
            this.getHive().setWorld(this.m_9236_());
            this.getHive().modifyPlayerReputation(livingBase.m_20148_(), i);
            if (this.m_6084_()) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 13);
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        if (this.getHive() != null) {
            Entity entity = cause.getEntity();
            if (entity != null) {
                this.getHive().setWorld(this.m_9236_());
                this.getHive().modifyPlayerReputation(entity.getUUID(), -15);
            }
        }
        this.resetCustomer();
        super.m_6667_(cause);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (!this.shouldHaveNormalAI()) {
            return InteractionResult.PASS;
        } else {
            boolean flag2 = itemstack.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get() || itemstack.getItem() == IafItemRegistry.MYRMEX_DESERT_STAFF.get();
            if (flag2) {
                this.onStaffInteract(player, itemstack);
                player.m_6674_(hand);
                return InteractionResult.SUCCESS;
            } else {
                boolean flag = itemstack.getItem() == Items.NAME_TAG || itemstack.getItem() == Items.LEAD;
                if (flag) {
                    return super.mobInteract(player, hand);
                } else if (this.getGrowthStage() < 2 || !this.m_6084_() || this.isBaby() || player.m_6144_()) {
                    return super.mobInteract(player, hand);
                } else if (this.getOffers().isEmpty()) {
                    return super.mobInteract(player, hand);
                } else if (!this.m_9236_().isClientSide && (this.m_5448_() == null || !this.m_5448_().equals(player)) && hand == InteractionHand.MAIN_HAND && this.getHive() != null && !this.getHive().isPlayerReputationTooLowToTrade(player.m_20148_())) {
                    this.setTradingPlayer(player);
                    this.m_45301_(player, this.m_5446_(), 1);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            }
        }
    }

    public void onStaffInteract(Player player, ItemStack itemstack) {
        if (itemstack.getTag() != null) {
            UUID staffUUID = itemstack.getTag().hasUUID("HiveUUID") ? itemstack.getTag().getUUID("HiveUUID") : null;
            if (!this.m_9236_().isClientSide) {
                if (player.isCreative() || this.getHive() == null || this.getHive().canPlayerCommandHive(player.m_20148_())) {
                    if (this.getHive() == null) {
                        player.displayClientMessage(Component.translatable("myrmex.message.null_hive"), true);
                    } else if (staffUUID != null && staffUUID.equals(this.getHive().hiveUUID)) {
                        player.displayClientMessage(Component.translatable("myrmex.message.staff_already_set"), true);
                    } else {
                        this.getHive().setWorld(this.m_9236_());
                        EntityMyrmexQueen queen = this.getHive().getQueen();
                        BlockPos center = this.getHive().getCenterGround();
                        if (queen != null && queen.m_8077_()) {
                            player.displayClientMessage(Component.translatable("myrmex.message.staff_set_named", queen.m_7755_(), center.m_123341_(), center.m_123342_(), center.m_123343_()), true);
                        } else {
                            player.displayClientMessage(Component.translatable("myrmex.message.staff_set_unnamed", center.m_123341_(), center.m_123342_(), center.m_123343_()), true);
                        }
                        itemstack.getTag().putUUID("HiveUUID", this.getHive().hiveUUID);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHive(MyrmexWorldData.get(this.m_9236_()).getNearestHive(this.m_20183_(), 400));
        if (this.getHive() != null) {
            this.setJungleVariant(isJungleBiome(this.m_9236_(), this.getHive().getCenter()));
        } else {
            this.setJungleVariant(this.f_19796_.nextBoolean());
        }
        return spawnDataIn;
    }

    public abstract boolean shouldLeaveHive();

    public abstract boolean shouldEnterHive();

    @Override
    public float getScale() {
        return this.getGrowthStage() == 0 ? 0.5F : (this.getGrowthStage() == 1 ? 0.75F : 1.0F);
    }

    public abstract ResourceLocation getAdultTexture();

    public abstract float getModelScale();

    public ResourceLocation getTexture() {
        if (this.getGrowthStage() == 0) {
            return this.isJungle() ? TEXTURE_JUNGLE_LARVA : TEXTURE_DESERT_LARVA;
        } else if (this.getGrowthStage() == 1) {
            return this.isJungle() ? TEXTURE_JUNGLE_PUPA : TEXTURE_DESERT_PUPA;
        } else {
            return this.getAdultTexture();
        }
    }

    public MyrmexHive getHive() {
        return this.hive;
    }

    public void setHive(MyrmexHive newHive) {
        this.hive = newHive;
        if (this.hive != null) {
            this.hive.addMyrmex(this);
        }
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        if (!haveSameHive(this, entityIn)) {
            entityIn.push(this);
        }
    }

    public boolean canSeeSky() {
        return this.m_9236_().m_46861_(this.m_20183_());
    }

    public boolean isOnResin() {
        int d0 = this.m_146904_() - 1;
        BlockPos blockpos = new BlockPos(this.m_146903_(), d0, this.m_146907_());
        while (this.m_9236_().m_46859_(blockpos) && blockpos.m_123342_() > 1) {
            blockpos = blockpos.below();
        }
        BlockState BlockState = this.m_9236_().getBlockState(blockpos);
        return BlockState.m_60734_() instanceof BlockMyrmexResin || BlockState.m_60734_() instanceof BlockMyrmexConnectedResin;
    }

    public boolean isInNursery() {
        if (this.getHive() != null && this.getHive().getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && this.getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.m_217043_(), this.m_20183_()) != null) {
            return false;
        } else if (this.getHive() != null) {
            BlockPos nursery = this.getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.m_217043_(), this.m_20183_());
            return Math.sqrt(this.m_20275_((double) nursery.m_123341_(), (double) nursery.m_123342_(), (double) nursery.m_123343_())) < 45.0;
        } else {
            return false;
        }
    }

    public boolean isInHive() {
        if (this.getHive() != null) {
            for (BlockPos pos : this.getHive().getAllRooms()) {
                if (this.isCloseEnoughToTarget(MyrmexHive.getGroundedPos(this.m_9236_(), pos), 50.0)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void travel(@NotNull Vec3 motion) {
        if (!this.canMove()) {
            super.m_7023_(Vec3.ZERO);
        } else {
            super.m_7023_(motion);
        }
    }

    public int getImportance() {
        return this.getGrowthStage() < 2 ? 1 : this.getCasteImportance();
    }

    public abstract int getCasteImportance();

    public boolean needsGaurding() {
        return true;
    }

    public boolean shouldMoveThroughHive() {
        return true;
    }

    public boolean shouldWander() {
        return this.getHive() == null;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 76) {
            this.playVillagerEffect();
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.MYRMEX_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.MYRMEX_DIE;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.m_5496_(IafSoundRegistry.MYRMEX_WALK, 0.16F * this.getMyrmexPitch() * (this.m_217043_().nextFloat() * 0.6F + 0.4F), 1.0F);
    }

    protected void playBiteSound() {
        this.m_5496_(IafSoundRegistry.MYRMEX_BITE, this.getMyrmexPitch(), 1.0F);
    }

    protected void playStingSound() {
        this.m_5496_(IafSoundRegistry.MYRMEX_STING, this.getMyrmexPitch(), 0.6F);
    }

    protected void playVillagerEffect() {
        for (int i = 0; i < 7; i++) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + 0.5 + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d0, d1, d2);
        }
    }

    public float getMyrmexPitch() {
        return this.m_20205_();
    }

    public boolean shouldHaveNormalAI() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public AABB getAttackBounds() {
        float size = this.getScale() * 0.65F;
        return this.m_20191_().inflate((double) (1.0F + size), (double) (1.0F + size), (double) (1.0F + size));
    }

    @Nullable
    @Override
    public Player getTradingPlayer() {
        return this.customer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.customer = player;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    @NotNull
    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.populateTradeData();
        }
        return this.offers;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers offers) {
    }

    @Override
    public void overrideXp(int xpIn) {
    }

    @Override
    public void notifyTrade(MerchantOffer offer) {
        offer.increaseUses();
        this.f_21363_ = -this.m_8100_();
        this.onVillagerTrade(offer);
    }

    protected void onVillagerTrade(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int i = 3 + this.f_19796_.nextInt(4);
            this.m_9236_().m_7967_(new ExperienceOrb(this.m_9236_(), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_(), i));
        }
        if (this.getHive() != null && this.getTradingPlayer() != null) {
            this.getHive().setWorld(this.m_9236_());
            this.getHive().modifyPlayerReputation(this.getTradingPlayer().m_20148_(), 1);
        }
    }

    @Override
    public void notifyTradeUpdated(@NotNull ItemStack stack) {
        if (!this.m_9236_().isClientSide && this.f_21363_ > -this.m_8100_() + 20) {
            this.f_21363_ = -this.m_8100_();
            this.m_5496_(this.getVillagerYesNoSound(!stack.isEmpty()), this.m_6121_(), this.m_6100_());
        }
    }

    @NotNull
    @Override
    public SoundEvent getNotifyTradeSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    protected SoundEvent getVillagerYesNoSound(boolean getYesSound) {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    public void playCelebrateSound() {
    }

    protected void resetCustomer() {
        this.setTradingPlayer(null);
    }

    @Nullable
    public Entity changeDimension(@NotNull ServerLevel server, @NotNull ITeleporter teleporter) {
        this.resetCustomer();
        return super.changeDimension(server, teleporter);
    }

    public SimpleContainer getVillagerInventory() {
        return this.villagerInventory;
    }

    @NotNull
    @Override
    public ItemStack equipItemIfPossible(@NotNull ItemStack stack) {
        ItemStack superStack = super.m_255207_(stack);
        if (ItemStack.isSameItem(superStack, stack) && ItemStack.matches(superStack, stack)) {
            return stack;
        } else {
            EquipmentSlot inventorySlot = stack.getEquipmentSlot();
            int i = inventorySlot.getIndex() - 300;
            if (i >= 0 && i < this.villagerInventory.getContainerSize()) {
                this.villagerInventory.setItem(i, stack);
                return stack;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    protected void addTrades(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        Set<Integer> set = Sets.newHashSet();
        if (newTrades.length > maxNumbers) {
            while (set.size() < maxNumbers) {
                set.add(this.f_19796_.nextInt(newTrades.length));
            }
        } else {
            for (int i = 0; i < newTrades.length; i++) {
                set.add(i);
            }
        }
        for (Integer integer : set) {
            VillagerTrades.ItemListing villagertrades$itrade = newTrades[integer];
            MerchantOffer merchantoffer = villagertrades$itrade.getOffer(this, this.f_19796_);
            if (merchantoffer != null) {
                givenMerchantOffers.add(merchantoffer);
            }
        }
    }

    private void levelUp() {
        this.populateTradeData();
    }

    protected abstract VillagerTrades.ItemListing[] getLevel1Trades();

    protected abstract VillagerTrades.ItemListing[] getLevel2Trades();

    protected void populateTradeData() {
        VillagerTrades.ItemListing[] level1 = this.getLevel1Trades();
        VillagerTrades.ItemListing[] level2 = this.getLevel2Trades();
        if (level1 != null && level2 != null) {
            MerchantOffers merchantoffers = this.getOffers();
            this.addTrades(merchantoffers, level1, 5);
            int i = this.f_19796_.nextInt(level2.length);
            int j = this.f_19796_.nextInt(level2.length);
            int k = this.f_19796_.nextInt(level2.length);
            for (int rolls = 0; j == i && rolls < 100; rolls++) {
                j = this.f_19796_.nextInt(level2.length);
            }
            for (int var14 = 0; (k == i || k == j) && var14 < 100; var14++) {
                k = this.f_19796_.nextInt(level2.length);
            }
            VillagerTrades.ItemListing rareTrade1 = level2[i];
            VillagerTrades.ItemListing rareTrade2 = level2[j];
            VillagerTrades.ItemListing rareTrade3 = level2[k];
            MerchantOffer merchantoffer1 = rareTrade1.getOffer(this, this.f_19796_);
            if (merchantoffer1 != null) {
                merchantoffers.add(merchantoffer1);
            }
            MerchantOffer merchantoffer2 = rareTrade2.getOffer(this, this.f_19796_);
            if (merchantoffer2 != null) {
                merchantoffers.add(merchantoffer2);
            }
            MerchantOffer merchantoffer3 = rareTrade3.getOffer(this, this.f_19796_);
            if (merchantoffer3 != null) {
                merchantoffers.add(merchantoffer3);
            }
        }
    }

    public boolean isCloseEnoughToTarget(BlockPos target, double distanceSquared) {
        return target != null ? this.m_20275_((double) target.m_123341_() + 0.5, (double) target.m_123342_() + 0.5, (double) target.m_123343_() + 0.5) <= distanceSquared : false;
    }

    public boolean pathReachesTarget(PathResult path, BlockPos target, double distanceSquared) {
        return !path.failedToReachDestination() && (this.isCloseEnoughToTarget(target, distanceSquared) || this.m_21573_().getPath() == null || !this.m_21573_().getPath().isDone());
    }

    @Override
    public boolean isSmallerThanBlock() {
        return false;
    }

    @Override
    public float getXZNavSize() {
        return this.m_20205_() / 2.0F;
    }

    @Override
    public int getYNavSize() {
        return (int) this.m_20206_() / 2;
    }

    @Override
    public int maxSearchNodes() {
        return IafConfig.maxDragonPathingNodes;
    }

    @Override
    public boolean isBlockExplicitlyPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return false;
    }

    @Override
    public boolean isBlockExplicitlyNotPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return state.m_60734_() instanceof LeavesBlock;
    }
}