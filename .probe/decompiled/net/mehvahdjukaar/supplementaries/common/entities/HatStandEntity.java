package net.mehvahdjukaar.supplementaries.common.entities;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import net.mehvahdjukaar.moonlight.api.client.anim.PendulumAnimation;
import net.mehvahdjukaar.moonlight.api.client.anim.SwingAnimation;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class HatStandEntity extends LivingEntity {

    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = SynchedEntityData.defineId(HatStandEntity.class, EntityDataSerializers.BYTE);

    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(HatStandEntity.class, EntityDataSerializers.ROTATIONS);

    public static final EntityDataAccessor<OptionalInt> FACING_TARGET = SynchedEntityData.defineId(HatStandEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    private final NonNullList<ItemStack> helmet = NonNullList.withSize(1, ItemStack.EMPTY);

    private boolean invisible;

    public long lastHit;

    private boolean slotsDisabled = false;

    private Rotations headPose;

    public final SwingAnimation swingAnimation;

    public final AnimationState skibidiAnimation;

    private final int tickOffset;

    private int skibidiAnimDur = 0;

    @Nullable
    private Float originalYRot = null;

    @Nullable
    private Entity target = null;

    public Vec3 jumpScareAngles = Vec3.ZERO;

    public HatStandEntity(EntityType<? extends HatStandEntity> entityType, Level level) {
        super(entityType, level);
        this.headPose = DEFAULT_HEAD_POSE;
        this.m_274367_(0.0F);
        if (PlatHelper.getPhysicalSide().isClient()) {
            this.swingAnimation = new PendulumAnimation(ClientConfigs.Blocks.HAT_STAND_CONFIG, this::getRotationAxis);
            this.skibidiAnimation = new AnimationState();
        } else {
            this.swingAnimation = null;
            this.skibidiAnimation = null;
        }
        this.tickOffset = level.random.nextInt(100);
        this.originalYRot = null;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getPlatform().isForge() ? PlatHelper.getEntitySpawnPacket(this) : super.m_5654_();
    }

    private Vector3f getRotationAxis() {
        return this.m_20252_(0.0F).toVector3f();
    }

    @Override
    public void refreshDimensions() {
        double d = this.m_20185_();
        double e = this.m_20186_();
        double f = this.m_20189_();
        super.m_6210_();
        this.m_6034_(d, e, f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_CLIENT_FLAGS, (byte) 0);
        this.f_19804_.define(FACING_TARGET, OptionalInt.empty());
        this.f_19804_.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.helmet;
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return Objects.requireNonNull(slot) == EquipmentSlot.HEAD ? this.helmet.get(0) : ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.m_181122_(stack);
        if (slot == EquipmentSlot.HEAD) {
            this.m_238392_(slot, this.helmet.set(0, stack), stack);
        }
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        EquipmentSlot equipmentSlot = Mob.m_147233_(stack);
        return this.getItemBySlot(equipmentSlot).isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        ItemStack stack = this.helmet.get(0);
        if (!stack.isEmpty()) {
            compound.put("Helmet", stack.save(new CompoundTag()));
        }
        compound.putBoolean("Invisible", this.m_20145_());
        compound.putBoolean("NoBasePlate", this.isNoBasePlate());
        compound.putBoolean("DisabledSlots", this.slotsDisabled);
        ListTag compoundTag = new ListTag();
        if (!DEFAULT_HEAD_POSE.equals(this.headPose)) {
            compoundTag = this.headPose.save();
        }
        compound.put("HeadPose", compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Helmet")) {
            this.helmet.set(0, ItemStack.of(compound.getCompound("Helmet")));
        }
        this.setInvisible(compound.getBoolean("Invisible"));
        this.setNoBasePlate(compound.getBoolean("NoBasePlate"));
        this.slotsDisabled = compound.getBoolean("DisabledSlots");
        ListTag listTag = compound.getList("HeadPose", 5);
        this.setHeadPose(listTag.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(listTag));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Level level = this.m_9236_();
        if (level.isClientSide) {
            this.swingAnimation.tick(!level.getFluidState(this.m_20097_()).isEmpty());
            for (Entity e : level.m_45933_(this, this.m_20191_())) {
                if (this.swingAnimation.hitByEntity(e)) {
                    break;
                }
            }
            float currentRot = this.m_146908_();
            if (this.target != null && this.skibidiAnimation.isStarted()) {
                Vec3 distanceVec = this.target.position().subtract(this.m_20182_()).normalize();
                float targetYRot = distanceVec.toVector3f().angleSigned(new Vector3f(1.0F, 0.0F, 0.0F), new Vector3f(0.0F, 1.0F, 0.0F)) * (180.0F / (float) Math.PI) - 90.0F;
                float difference = Mth.degreesDifference(currentRot, targetYRot);
                this.m_146922_(Mth.lerp(0.5F, currentRot, currentRot + difference));
            } else if (this.originalYRot != null && currentRot != this.originalYRot) {
                this.m_146922_(this.originalYRot);
                this.originalYRot = null;
            }
        } else if (this.skibidiAnimDur != 0) {
            this.skibidiAnimDur--;
            if (this.skibidiAnimDur == 0) {
                this.m_20088_().set(FACING_TARGET, OptionalInt.empty());
                this.m_20124_(Pose.STANDING);
            }
        } else if (this.m_20089_() == Pose.STANDING && (this.f_19797_ + this.tickOffset) % 100 == 0 && this.f_19796_.nextFloat() < 0.2F) {
            this.setSkibidiIfInCauldron(null);
        }
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
        super.pushEntities();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (!itemStack.is(Items.NAME_TAG)) {
            boolean isClientSide = player.m_9236_().isClientSide;
            if (player.isSecondaryUseActive()) {
                if (isClientSide) {
                    this.swingAnimation.addImpulse(0.001F);
                    this.swingAnimation.addPositiveImpulse(1.2F);
                }
                return InteractionResult.sidedSuccess(isClientSide);
            } else if (player.isSpectator()) {
                return InteractionResult.SUCCESS;
            } else if (isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!this.slotsDisabled) {
                    if (itemStack.isEmpty()) {
                        EquipmentSlot targetSlot = EquipmentSlot.HEAD;
                        if (this.m_21033_(targetSlot) && this.swapItem(player, targetSlot, itemStack, hand)) {
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        EquipmentSlot equipmentSlot = Mob.m_147233_(itemStack);
                        if ((Boolean) CommonConfigs.Building.HAT_STAND_UNRESTRICTED.get()) {
                            equipmentSlot = EquipmentSlot.HEAD;
                        }
                        if (equipmentSlot != EquipmentSlot.HEAD) {
                            return InteractionResult.FAIL;
                        }
                        if (this.swapItem(player, equipmentSlot, itemStack, hand)) {
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean swapItem(Player player, EquipmentSlot slot, ItemStack stack, InteractionHand hand) {
        ItemStack itemStack = this.getItemBySlot(slot);
        if (player.getAbilities().instabuild && itemStack.isEmpty() && !stack.isEmpty()) {
            this.setItemSlot(slot, stack.copyWithCount(1));
            return true;
        } else if (stack.isEmpty() || stack.getCount() <= 1) {
            this.setItemSlot(slot, stack);
            player.m_21008_(hand, itemStack);
            return true;
        } else if (!itemStack.isEmpty()) {
            return false;
        } else {
            this.setItemSlot(slot, stack.split(1));
            return true;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_9236_().isClientSide && source.getDirectEntity() instanceof Projectile) {
            this.swingAnimation.hitByEntity(source.getDirectEntity());
        }
        if (!this.m_9236_().isClientSide && !this.m_213877_()) {
            if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.dismantle(source);
                return false;
            }
            if (!this.m_6673_(source) && !this.invisible) {
                if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                    this.dismantle(source);
                    return false;
                }
                if (source.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
                    if (this.m_6060_()) {
                        this.causeDamage(source, 0.15F);
                    } else {
                        this.m_20254_(5);
                    }
                    return false;
                }
                if (source.is(DamageTypeTags.BURNS_ARMOR_STANDS) && this.m_21223_() > 0.5F) {
                    this.causeDamage(source, 4.0F);
                    return false;
                }
                boolean isDirectArrow = source.getDirectEntity() instanceof AbstractArrow;
                boolean isPierceArrow = isDirectArrow && ((AbstractArrow) source.getDirectEntity()).getPierceLevel() > 0;
                boolean bl3 = "player".equals(source.getMsgId());
                if (!bl3 && !isDirectArrow) {
                    return false;
                }
                if (source.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
                    return false;
                }
                if (source.isCreativePlayer()) {
                    this.dismantle(null);
                    return isPierceArrow;
                }
                long l = this.m_9236_().getGameTime();
                if (l - this.lastHit > 5L && !isDirectArrow) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 32);
                    this.m_146852_(GameEvent.ENTITY_DAMAGE, source.getEntity());
                    this.lastHit = l;
                } else {
                    this.dismantle(source);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 32) {
            if (this.m_9236_().isClientSide) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ARMOR_STAND_HIT, this.m_5720_(), 0.3F, 1.0F, false);
                this.lastHit = this.m_9236_().getGameTime();
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d = this.m_20191_().getSize() * 4.0;
        if (Double.isNaN(d) || d == 0.0) {
            d = 4.0;
        }
        d *= 64.0;
        return distance < d * d;
    }

    private void showBreakingParticles() {
        if (this.m_9236_() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), this.m_20185_(), this.m_20227_(0.6666666666666666), this.m_20189_(), 10, (double) (this.m_20205_() / 4.0F), (double) (this.m_20206_() / 4.0F), (double) (this.m_20205_() / 4.0F), 0.05);
        }
    }

    private void causeDamage(DamageSource damageSource, float amount) {
        float f = this.m_21223_();
        f -= amount;
        if (f <= 0.5F) {
            this.dismantle(damageSource);
        } else {
            this.m_21153_(f);
            this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
        }
    }

    @Override
    protected float tickHeadTurn(float yRot, float animStep) {
        this.f_20884_ = this.f_19859_;
        this.f_20883_ = this.m_146908_();
        return 0.0F;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * (this.m_6162_() ? 0.5F : 0.7F);
    }

    @Override
    public void setYBodyRot(float pOffset) {
        float r = this.m_146908_();
        this.f_19859_ = r;
        this.f_20884_ = this.f_20883_ = r;
    }

    @Override
    public void setYHeadRot(float pRotation) {
        float r = this.m_146908_();
        this.f_19859_ = r;
        this.f_20886_ = this.f_20885_ = r;
    }

    @Override
    public void tick() {
        super.tick();
        Rotations rotations = this.f_19804_.get(DATA_HEAD_POSE);
        if (!this.headPose.equals(rotations)) {
            this.setHeadPose(rotations);
        }
    }

    @Override
    protected void updateInvisibilityStatus() {
        this.setInvisible(this.invisible);
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        super.m_6842_(invisible);
    }

    public void setNoBasePlate(boolean noBasePlate) {
        this.f_19804_.set(DATA_CLIENT_FLAGS, this.setBit(this.f_19804_.get(DATA_CLIENT_FLAGS), 8, noBasePlate));
    }

    public boolean isNoBasePlate() {
        return (this.f_19804_.get(DATA_CLIENT_FLAGS) & 8) != 0;
    }

    private byte setBit(byte oldBit, int offset, boolean value) {
        if (value) {
            oldBit = (byte) (oldBit | offset);
        } else {
            oldBit = (byte) (oldBit & ~offset);
        }
        return oldBit;
    }

    public void dismantle(@Nullable DamageSource source) {
        if (source != null) {
            this.dropAllDeathLoot(source);
        }
        this.showBreakingParticles();
        this.playBrokenSound();
        this.m_142687_(Entity.RemovalReason.KILLED);
        this.m_146850_(GameEvent.ENTITY_DIE);
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource) {
        super.dropAllDeathLoot(damageSource);
        this.m_5552_(this.getPickResult(), 1.0F);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        ItemStack itemStack = this.helmet.get(0);
        if (!itemStack.isEmpty()) {
            this.m_5552_(itemStack, 1.0F);
            this.helmet.set(0, ItemStack.EMPTY);
        }
    }

    private void playBrokenSound() {
        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.ARMOR_STAND_BREAK, this.m_5720_(), 1.0F, 1.0F);
    }

    @Override
    public void kill() {
        this.dismantle(this.m_9236_().damageSources().generic());
    }

    @Override
    public boolean ignoreExplosion() {
        return this.m_20145_();
    }

    public void setHeadPose(Rotations headPose) {
        this.headPose = headPose;
        this.f_19804_.set(DATA_HEAD_POSE, headPose);
    }

    public Rotations getHeadPose() {
        return this.headPose;
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player p && !this.m_9236_().mayInteract(p, this.m_20183_())) {
            return true;
        }
        return false;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_CLIENT_FLAGS.equals(key)) {
            this.refreshDimensions();
            this.f_19850_ = true;
        }
        if (f_19806_.equals(key) && this.m_9236_().isClientSide) {
            Pose pose = this.m_20089_();
            if (pose != Pose.SNIFFING && pose != Pose.SPIN_ATTACK) {
                this.skibidiAnimation.stop();
            } else {
                this.skibidiAnimation.start(this.f_19797_);
            }
        }
        if (FACING_TARGET.equals(key) && this.m_9236_().isClientSide) {
            OptionalInt o = this.m_20088_().get(FACING_TARGET);
            if (o.isPresent()) {
                this.target = this.m_9236_().getEntity(o.getAsInt());
                this.originalYRot = this.m_146908_();
            } else {
                this.target = null;
            }
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public ItemStack getPickResult() {
        ItemStack itemStack = new ItemStack((ItemLike) ModRegistry.HAT_STAND.get());
        if (this.m_8077_()) {
            itemStack.setHoverName(this.m_7770_());
        }
        return itemStack;
    }

    @Override
    public boolean canBeSeenByAnyone() {
        return !this.m_20145_();
    }

    private void setSkibidiIfInCauldron(@Nullable LivingEntity target) {
        BlockState state = this.m_146900_();
        Block block = state.m_60734_();
        if (block instanceof AbstractCauldronBlock || block instanceof ComposterBlock) {
            this.setSkibidi(true, true, target);
        } else if (block instanceof HopperBlock) {
            this.setSkibidi(true, false, target);
        }
    }

    public void setSkibidi(boolean skibidi, boolean tall, @Nullable LivingEntity playerTarget) {
        OptionalInt opt;
        if (playerTarget != null) {
            opt = OptionalInt.of(playerTarget.m_19879_());
        } else {
            opt = OptionalInt.empty();
        }
        this.f_19804_.set(FACING_TARGET, opt);
        if (skibidi) {
            this.m_20124_(tall ? Pose.SPIN_ATTACK : Pose.SNIFFING);
            this.skibidiAnimDur = 160;
        } else {
            this.m_20124_(Pose.STANDING);
        }
    }

    public static void makeSkibidiInArea(LivingEntity player) {
        Level level = player.m_9236_();
        List<HatStandEntity> toilets = level.m_45976_(HatStandEntity.class, new AABB(player.m_20097_()).inflate(10.0));
        toilets.forEach(h -> h.setSkibidiIfInCauldron(player));
    }
}