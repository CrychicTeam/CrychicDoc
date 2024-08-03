package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.enchantment.AMEnchantmentRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityStraddleboard extends Entity implements PlayerRideableJumping {

    private static final EntityDataAccessor<ItemStack> ITEMSTACK = SynchedEntityData.defineId(EntityStraddleboard.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Integer> TIME_SINCE_HIT = SynchedEntityData.defineId(EntityStraddleboard.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityStraddleboard.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DEFAULT_COLOR = SynchedEntityData.defineId(EntityStraddleboard.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> BOARD_ROT = SynchedEntityData.defineId(EntityStraddleboard.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> REMOVE_SOON = SynchedEntityData.defineId(EntityStraddleboard.class, EntityDataSerializers.BOOLEAN);

    public float prevBoardRot = 0.0F;

    private boolean rocking;

    private float rockingIntensity;

    private float rockingAngle;

    private float prevRockingAngle;

    private int extinguishTimer = 0;

    private int jumpFor = 0;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private int rideForTicks = 0;

    private float boardForwards = 0.0F;

    private int removeIn;

    private Player returnToPlayer = null;

    public EntityStraddleboard(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
        this.f_19850_ = true;
    }

    public EntityStraddleboard(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.STRADDLEBOARD.get(), world);
    }

    public EntityStraddleboard(Level worldIn, double x, double y, double z) {
        this(AMEntityRegistry.STRADDLEBOARD.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20256_(Vec3.ZERO);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
    }

    public static boolean canVehicleCollide(Entity p_242378_0_, Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !p_242378_0_.isPassengerOfSameVehicle(entity);
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return sizeIn.height;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TIME_SINCE_HIT, 0);
        this.f_19804_.define(ITEMSTACK, new ItemStack(AMItemRegistry.STRADDLEBOARD.get()));
        this.f_19804_.define(DEFAULT_COLOR, true);
        this.f_19804_.define(COLOR, 0);
        this.f_19804_.define(BOARD_ROT, 0.0F);
        this.f_19804_.define(REMOVE_SOON, false);
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return canVehicleCollide(this, entity);
    }

    @Override
    protected Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle result) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, result));
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.5;
    }

    public float getBoardRot() {
        return this.f_19804_.get(BOARD_ROT);
    }

    public void setBoardRot(float f) {
        this.f_19804_.set(BOARD_ROT, f);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else if (!this.m_9236_().isClientSide && !this.m_213877_()) {
            this.f_19804_.set(REMOVE_SOON, true);
            return true;
        } else {
            return true;
        }
    }

    private ItemStack getItemBoard() {
        return this.getItemStack();
    }

    @Override
    public void push(Entity entityIn) {
        if (entityIn instanceof EntityStraddleboard) {
            if (entityIn.getBoundingBox().minY < this.m_20191_().maxY) {
                super.push(entityIn);
            }
        } else if (entityIn.getBoundingBox().minY <= this.m_20191_().minY) {
            super.push(entityIn);
        }
    }

    public boolean isRemoveLogic() {
        return this.f_19804_.get(REMOVE_SOON) || this.m_213877_();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isRemoveLogic();
    }

    @Override
    public boolean isPushable() {
        return !this.isRemoveLogic();
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoveLogic();
    }

    @Override
    public boolean shouldBeSaved() {
        return !this.isRemoveLogic();
    }

    @Override
    public boolean isAttackable() {
        return !this.isRemoveLogic();
    }

    public boolean isDefaultColor() {
        return this.f_19804_.get(DEFAULT_COLOR);
    }

    public void setDefaultColor(boolean bar) {
        this.f_19804_.set(DEFAULT_COLOR, bar);
    }

    public int getColor() {
        return this.isDefaultColor() ? 11387863 : this.f_19804_.get(COLOR);
    }

    public void setColor(int index) {
        this.f_19804_.set(COLOR, index);
    }

    @Override
    public void tick() {
        super.tick();
        float boardRot = this.getBoardRot();
        if (this.jumpFor > 0) {
            this.jumpFor--;
        }
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.extinguishTimer > 0) {
            this.extinguishTimer--;
        }
        if (this.f_19804_.get(REMOVE_SOON)) {
            this.removeIn--;
            this.setBoardRot((float) Math.sin((double) ((float) this.removeIn * 0.3F) * Math.PI) * 50.0F);
            if (this.removeIn <= 0 && !this.m_9236_().isClientSide) {
                this.removeIn = 0;
                boolean drop;
                if (this.getEnchant(AMEnchantmentRegistry.STRADDLE_BOARDRETURN.get()) <= 0) {
                    drop = true;
                } else {
                    drop = this.returnToPlayer != null && !this.returnToPlayer.addItem(this.getItemBoard());
                }
                if (drop) {
                    this.m_19983_(this.getItemStack().copy());
                }
                this.m_146870_();
            }
        }
        Entity controller = this.getControllingPlayer();
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_(d5, d6, d7);
                this.m_19915_(this.m_146908_(), this.m_146909_());
            } else {
                this.m_20090_();
                this.m_19915_(this.m_146908_(), this.m_146909_());
            }
        } else {
            this.m_20101_();
            float slowdown = !this.m_20072_() && !this.m_20096_() ? 0.98F : 0.05F;
            this.tickMovement();
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().multiply((double) slowdown, (double) slowdown, (double) slowdown));
            float f2 = (float) (-((double) ((float) this.m_20184_().y * 0.5F) * 180.0F / (float) Math.PI));
            this.m_146926_(Mth.approachDegrees(this.m_146909_(), f2, 5.0F));
            if (controller instanceof Player player) {
                this.returnToPlayer = player;
                this.rideForTicks++;
                if (this.f_19797_ % 50 == 0 && this.getEnchant(AMEnchantmentRegistry.STRADDLE_LAVAWAX.get()) > 0) {
                    player.m_7292_(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false));
                }
                if (player.m_20094_() > 0 && this.extinguishTimer == 0) {
                    player.m_20095_();
                }
                this.m_146922_(Mth.approachDegrees(this.m_146908_(), player.m_146908_(), 6.0F));
                Vec3 deltaMovement = this.m_20184_();
                if (deltaMovement.y > -0.5) {
                    this.f_19789_ = 1.0F;
                }
                float slow = player.f_20902_ < 0.0F ? 0.0F : player.f_20902_ * 0.115F;
                float threshold = 3.0F;
                boolean flag = false;
                float boardRot1 = boardRot;
                if (this.f_19859_ - this.m_146908_() > threshold) {
                    boardRot1 = boardRot + 10.0F;
                    flag = true;
                }
                if (this.f_19859_ - this.m_146908_() < -threshold) {
                    boardRot1 -= 10.0F;
                    flag = true;
                }
                if (!flag) {
                    if (boardRot1 > 0.0F) {
                        boardRot1 = Math.max(boardRot1 - 5.0F, 0.0F);
                    }
                    if (boardRot1 < 0.0F) {
                        boardRot1 = Math.min(boardRot1 + 5.0F, 0.0F);
                    }
                }
                this.setBoardRot(Mth.approachDegrees(boardRot, Mth.clamp(boardRot1, -25.0F, 25.0F), 5.0F));
                this.boardForwards = slow;
                if (player.m_6144_() || !this.m_6084_() || this.f_19804_.get(REMOVE_SOON)) {
                    this.m_20153_();
                }
                if (player.m_5830_()) {
                    this.m_20153_();
                    this.hurt(this.m_269291_().generic(), 100.0F);
                }
            } else {
                this.rideForTicks = 0;
            }
        }
        this.prevBoardRot = boardRot;
    }

    private void tickMovement() {
        this.f_19812_ = true;
        float moveForwards = Math.min(this.boardForwards, 1.0F);
        float yRot = this.m_146908_();
        Vec3 prev = this.m_20184_();
        float gravity = this.isOnLava() ? 0.0F : (this.m_20077_() ? 0.1F : -1.0F);
        float f1 = -Mth.sin(yRot * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(yRot * (float) (Math.PI / 180.0));
        Vec3 moveVec = new Vec3((double) f1, 0.0, (double) f2).scale((double) moveForwards);
        Vec3 vec31 = prev.scale(0.975F).add(moveVec);
        float jumpGravity = gravity;
        if (this.jumpFor > 0) {
            float jumpRunsOutIn = this.jumpFor < 5 ? (float) this.jumpFor / 5.0F : 1.0F;
            jumpGravity = gravity + jumpRunsOutIn + jumpRunsOutIn * 1.0F;
        }
        this.m_20334_(vec31.x, (double) jumpGravity, vec31.z);
    }

    private boolean isOnLava() {
        BlockPos ourPos = BlockPos.containing(this.m_20185_(), this.m_20186_() + 0.4F, this.m_20189_());
        BlockPos underPos = this.m_20097_();
        return this.m_9236_().getFluidState(underPos).is(FluidTags.LAVA) && !this.m_9236_().getFluidState(ourPos).is(FluidTags.LAVA);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double) yr;
        this.lxr = (double) xr;
        this.lSteps = steps;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public double getEyeY() {
        return this.m_20186_() + 0.3F;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getControllingPlayer();
    }

    @Nullable
    @Override
    public boolean isControlledByLocalInstance() {
        return false;
    }

    @Nullable
    public Player getControllingPlayer() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player) {
                return (Player) passenger;
            }
        }
        return null;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.isControlledByLocalInstance() && this.lSteps > 0) {
            this.lSteps = 0;
            this.m_19890_(this.lx, this.ly, this.lz, (float) this.lyr, (float) this.lxr);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.onSyncedDataUpdated(entityDataAccessor);
        if (REMOVE_SOON.equals(entityDataAccessor)) {
            this.removeIn = 5;
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (!this.m_9236_().isClientSide) {
            return player.m_20329_(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    public int getTimeSinceHit() {
        return this.f_19804_.get(TIME_SINCE_HIT);
    }

    public void setTimeSinceHit(int timeSinceHit) {
        this.f_19804_.set(TIME_SINCE_HIT, timeSinceHit);
    }

    @OnlyIn(Dist.CLIENT)
    public float getRockingAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevRockingAngle, this.rockingAngle);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setDefaultColor(compound.getBoolean("IsDefColor"));
        if (compound.contains("BoardStack")) {
            this.setItemStack(ItemStack.of(compound.getCompound("BoardStack")));
        }
        this.setColor(compound.getInt("Color"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("IsDefColor", this.isDefaultColor());
        compound.putInt("Color", this.getColor());
        if (!this.getItemStack().isEmpty()) {
            CompoundTag stackTag = new CompoundTag();
            this.getItemStack().save(stackTag);
            compound.put("BoardStack", stackTag);
        }
    }

    @Override
    public void onPlayerJump(int i) {
    }

    @Override
    public boolean canJump() {
        return this.isOnLava();
    }

    @Override
    public void handleStartJump(int i) {
        this.f_19812_ = true;
        if (this.canJump()) {
            float f = 0.075F + (float) this.getEnchant(AMEnchantmentRegistry.STRADDLE_JUMP.get()) * 0.05F;
            this.jumpFor = 5 + (int) ((float) i * f);
        }
    }

    private int getEnchant(Enchantment enchantment) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, this.getItemBoard());
    }

    public boolean shouldSerpentFriend() {
        return this.getEnchant(AMEnchantmentRegistry.STRADDLE_SERPENTFRIEND.get()) > 0;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
        return new Vec3(this.m_20185_(), this.m_20186_() + 2.0, this.m_20189_());
    }

    @Override
    public void handleStopJump() {
    }

    public ItemStack getItemStack() {
        return this.f_19804_.get(ITEMSTACK);
    }

    public void setItemStack(ItemStack item) {
        this.f_19804_.set(ITEMSTACK, item);
    }
}