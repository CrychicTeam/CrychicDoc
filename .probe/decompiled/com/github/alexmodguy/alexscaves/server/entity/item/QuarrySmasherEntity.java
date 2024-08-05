package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.QuarryBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.QuarryBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class QuarrySmasherEntity extends Entity {

    public final QuarrySmasherHeadEntity headPart;

    public final QuarrySmasherHeadEntity[] allParts;

    public AABB lastMiningArea = null;

    private static final EntityDataAccessor<Boolean> INACTIVE = SynchedEntityData.defineId(QuarrySmasherEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> QUARRY_POS = SynchedEntityData.defineId(QuarrySmasherEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Optional<BlockPos>> TARGET_POS = SynchedEntityData.defineId(QuarrySmasherEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Boolean> SLAMMING = SynchedEntityData.defineId(QuarrySmasherEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> PULLING_ITEMS_FOR = SynchedEntityData.defineId(QuarrySmasherEntity.class, EntityDataSerializers.INT);

    private float inactiveProgress;

    private float prevInactiveProgress;

    private float headGroundProgress;

    private float prevHeadGroundProgress;

    private int damageSustained;

    private int quarryActivityTime = 0;

    private int blockBreakCooldown = 0;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    public int shakeTime = 0;

    public List<ItemEntity> pulledItems = new ArrayList();

    private boolean triggerAdvancement;

    public QuarrySmasherEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.headPart = new QuarrySmasherHeadEntity(this);
        this.allParts = new QuarrySmasherHeadEntity[] { this.headPart };
        this.headPart.m_20359_(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(QUARRY_POS, Optional.empty());
        this.f_19804_.define(TARGET_POS, Optional.empty());
        this.f_19804_.define(INACTIVE, true);
        this.f_19804_.define(SLAMMING, false);
        this.f_19804_.define(PULLING_ITEMS_FOR, 0);
    }

    public QuarrySmasherEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(ACEntityRegistry.QUARRY_SMASHER.get(), world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        this.tickMultipart();
        super.tick();
        this.prevInactiveProgress = this.inactiveProgress;
        this.prevHeadGroundProgress = this.headGroundProgress;
        if (this.isInactive() && this.inactiveProgress < 10.0F) {
            this.inactiveProgress++;
        }
        if (!this.isInactive() && this.inactiveProgress > 0.0F) {
            this.inactiveProgress--;
        }
        if (this.isSlamming() && this.headGroundProgress < 5.0F) {
            this.headGroundProgress++;
        }
        if (!this.isSlamming() && this.headGroundProgress > 0.0F) {
            this.headGroundProgress--;
        }
        if (this.damageSustained > 0 && this.f_19797_ % 500 == 0) {
            this.damageSustained--;
        }
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_(d5, d6, d7);
            } else {
                this.m_20090_();
            }
            if (this.m_6084_()) {
                AlexsCaves.PROXY.playWorldSound(this, (byte) 14);
            }
        } else if (this.triggerAdvancement && this.f_19797_ % 20 == 0) {
            boolean flag = false;
            double advancementRange = 20.0;
            for (Player player : this.m_9236_().m_45976_(Player.class, this.m_20191_().inflate(advancementRange))) {
                if ((double) player.m_20270_(this) < advancementRange) {
                    flag = true;
                    ACAdvancementTriggerRegistry.FINISHED_QUARRY.triggerForEntity(player);
                }
            }
            this.triggerAdvancement = !flag;
        }
        if (this.isInactive()) {
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.2, 0.0));
            }
        } else {
            BlockPos quarryPos = this.getQuarryPos();
            if (this.quarryActivityTime-- < 0) {
                this.quarryActivityTime = 20;
                if (quarryPos != null && this.m_9236_().getBlockState(quarryPos).m_60713_(ACBlockRegistry.QUARRY.get())) {
                    if (this.m_9236_().getBlockEntity(quarryPos) instanceof QuarryBlockEntity quarryBlockEntity) {
                        this.lastMiningArea = quarryBlockEntity.getMiningBox();
                    }
                } else {
                    this.setQuarryPos(null);
                    this.setInactive(true);
                }
            }
            BlockPos targetPos = this.getTargetPos();
            if (!this.m_9236_().isClientSide) {
                if (targetPos == null) {
                    this.setTargetPos(this.findTarget());
                } else {
                    Vec3 dist = Vec3.upFromBottomCenterOf(quarryPos == null ? targetPos : targetPos.atY(quarryPos.m_123342_()), 4.0).subtract(this.m_20182_());
                    if (dist.horizontalDistance() < 0.5) {
                        if (this.blockBreakCooldown <= 0) {
                            this.setSlamming(true);
                            if (this.headPart.m_20182_().distanceTo(Vec3.atBottomCenterOf(targetPos)) < 1.1) {
                                this.setSlamming(false);
                                this.m_9236_().playSound(null, targetPos, ACSoundRegistry.BOUNDROID_SLAM.get(), SoundSource.BLOCKS, 1.5F, 1.0F);
                                this.m_9236_().m_46961_(targetPos, true);
                                this.setTargetPos(null);
                                this.blockBreakCooldown = 35;
                                this.setPullingItemsFor(20);
                            }
                        }
                    } else {
                        this.m_20256_(this.m_20184_().add(dist.normalize().scale(0.1F)));
                    }
                }
            }
        }
        if (this.pullingItemsFor() > 0) {
            int i = this.pullingItemsFor() - 1;
            this.setPullingItemsFor(i);
            BlockPos quarry = this.getQuarryPos();
            boolean flag = false;
            for (ItemEntity itemEntity : this.m_9236_().m_45976_(ItemEntity.class, this.headPart.m_20191_().inflate(2.0, 4.0, 2.0))) {
                itemEntity.f_19812_ = true;
                if (!this.pulledItems.contains(itemEntity)) {
                    this.pulledItems.add(itemEntity);
                }
                flag = true;
            }
            for (ItemEntity itemEntity : this.pulledItems) {
                if (i == 0) {
                    if (quarry != null && this.m_9236_().getBlockState(quarry).m_60713_(ACBlockRegistry.QUARRY.get())) {
                        itemEntity.m_6034_((double) ((float) quarry.m_123341_() + 0.5F), (double) ((float) quarry.m_123342_() + 1.0F), (double) ((float) quarry.m_123343_() + 0.5F));
                        Direction facing = (Direction) this.m_9236_().getBlockState(quarry).m_61143_(QuarryBlock.FACING);
                        itemEntity.setDefaultPickUpDelay();
                        itemEntity.m_20256_(new Vec3((double) ((float) facing.getStepX() * 0.1F), 0.4F, (double) ((float) facing.getStepZ() * 0.1F)));
                    }
                } else {
                    itemEntity.m_146884_(this.headPart.m_20182_().subtract(0.0, 0.5, 0.0));
                    itemEntity.m_20256_(Vec3.ZERO);
                }
            }
            if (flag && quarry != null && this.m_9236_().getBlockEntity(quarry) instanceof QuarryBlockEntity quarryBlockEntity) {
                quarryBlockEntity.spinFor = 13;
                this.m_9236_().playSound((Player) null, quarry, ACSoundRegistry.QUARRY_CRUSH.get(), SoundSource.BLOCKS, 1.0F, 0.9F + this.m_9236_().random.nextFloat() * 0.2F);
            }
            if (i == 0) {
                this.pulledItems.clear();
            }
        }
        if (this.blockBreakCooldown > 0) {
            this.blockBreakCooldown--;
        }
        if (this.shakeTime > 0) {
            this.shakeTime--;
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        this.m_20256_(this.m_20184_().scale(0.7F));
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.m_6109_() && this.lSteps > 0) {
            this.lSteps = 0;
            this.m_19890_(this.lx, this.ly, this.lz, (float) this.lyr, (float) this.lxr);
        }
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

    public BlockPos findTarget() {
        BlockPos quarry = this.getQuarryPos();
        if (quarry != null && this.m_9236_().getBlockEntity(quarry) instanceof QuarryBlockEntity quarryBlockEntity && quarryBlockEntity.hasMiningArea()) {
            BlockPos pos = (BlockPos) quarryBlockEntity.findMinableBlock(this.m_9236_(), (double) (quarry.m_123342_() + 3)).orElse(null);
            if (pos != null) {
                return pos;
            }
            this.triggerAdvancement = true;
        }
        return null;
    }

    public void tickMultipart() {
        Vec3 headTarget = this.getHeadTargetPos();
        float fallSpeed = 0.05F;
        if (this.isInactive()) {
            fallSpeed = 0.5F;
        } else if (this.isSlamming()) {
            fallSpeed = this.getHeadGroundProgress(1.0F) * 0.3F;
        } else if (this.pullingItemsFor() > 5) {
            fallSpeed = 0.0F;
        }
        Vec3 moveHeadBy = headTarget.subtract(this.headPart.m_20182_()).scale((double) fallSpeed);
        if (this.f_19797_ > 1) {
            this.headPart.m_146867_();
            this.headPart.m_146884_(moveHeadBy.add(this.headPart.m_20182_()));
        } else {
            this.headPart.m_146884_(this.m_20182_());
            this.headPart.m_146867_();
        }
    }

    private Vec3 getHeadTargetPos() {
        if (this.isInactive()) {
            return this.m_20182_().add(0.75, 0.0, -0.75);
        } else {
            if (this.isSlamming()) {
                BlockPos target = this.getTargetPos();
                if (target != null) {
                    return Vec3.upFromBottomCenterOf(target, 1.0);
                }
            }
            return this.m_20182_().add(0.0, -1.0 + Math.sin((double) this.f_19797_ * 0.1) * 0.5, 0.0);
        }
    }

    public float getChainLength(float partialTick) {
        return this.headPart == null ? 0.0F : (float) this.headPart.m_20318_(partialTick).subtract(this.m_20318_(partialTick)).length();
    }

    public boolean isInactive() {
        return this.f_19804_.get(INACTIVE);
    }

    public void setInactive(boolean inactive) {
        this.f_19804_.set(INACTIVE, inactive);
    }

    public boolean isSlamming() {
        return this.f_19804_.get(SLAMMING);
    }

    public void setSlamming(boolean slamming) {
        this.f_19804_.set(SLAMMING, slamming);
    }

    public int pullingItemsFor() {
        return this.f_19804_.get(PULLING_ITEMS_FOR);
    }

    public void setPullingItemsFor(int pullingItemsFor) {
        this.f_19804_.set(PULLING_ITEMS_FOR, pullingItemsFor);
    }

    public float getInactiveProgress(float partialTick) {
        return (this.prevInactiveProgress + (this.inactiveProgress - this.prevInactiveProgress) * partialTick) * 0.1F;
    }

    public boolean isBeingActivated() {
        return this.inactiveProgress <= this.prevInactiveProgress;
    }

    public float getHeadGroundProgress(float partialTick) {
        return (this.prevHeadGroundProgress + (this.headGroundProgress - this.prevHeadGroundProgress) * partialTick) * 0.2F;
    }

    public void setQuarryPos(@Nullable BlockPos pos) {
        this.m_20088_().set(QUARRY_POS, Optional.ofNullable(pos));
    }

    @Nullable
    public BlockPos getQuarryPos() {
        return (BlockPos) this.m_20088_().get(QUARRY_POS).orElse((BlockPos) null);
    }

    public void setTargetPos(@Nullable BlockPos pos) {
        this.m_20088_().set(TARGET_POS, Optional.ofNullable(pos));
    }

    @Nullable
    public BlockPos getTargetPos() {
        return (BlockPos) this.m_20088_().get(TARGET_POS).orElse((BlockPos) null);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 48) {
            this.shakeTime = 10;
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public boolean isAttackable() {
        return !this.m_213877_();
    }

    @Override
    public boolean shouldBeSaved() {
        return !this.m_213877_();
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ACItemRegistry.QUARRY_SMASHER.get());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.remove(removalReason);
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageValue) {
        if (this.m_6673_(damageSource)) {
            return false;
        } else {
            this.damageSustained = (int) ((float) this.damageSustained + damageValue);
            this.m_9236_().broadcastEntityEvent(this, (byte) 48);
            if (this.damageSustained >= 10) {
                this.m_216990_(SoundEvents.ITEM_BREAK);
                if (!this.m_213877_()) {
                    for (int i = 0; i < 1 + this.f_19796_.nextInt(1); i++) {
                        this.m_19998_(ACItemRegistry.AZURE_NEODYMIUM_INGOT.get());
                    }
                    for (int i = 0; i < 1 + this.f_19796_.nextInt(1); i++) {
                        this.m_19998_(ACItemRegistry.SCARLET_NEODYMIUM_INGOT.get());
                    }
                }
                this.remove(Entity.RemovalReason.KILLED);
            }
            return true;
        }
    }
}