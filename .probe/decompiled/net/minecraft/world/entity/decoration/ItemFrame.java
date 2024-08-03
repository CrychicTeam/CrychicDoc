package net.minecraft.world.entity.decoration;

import com.mojang.logging.LogUtils;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

public class ItemFrame extends HangingEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.INT);

    public static final int NUM_ROTATIONS = 8;

    private float dropChance = 1.0F;

    private boolean fixed;

    public ItemFrame(EntityType<? extends ItemFrame> entityTypeExtendsItemFrame0, Level level1) {
        super(entityTypeExtendsItemFrame0, level1);
    }

    public ItemFrame(Level level0, BlockPos blockPos1, Direction direction2) {
        this(EntityType.ITEM_FRAME, level0, blockPos1, direction2);
    }

    public ItemFrame(EntityType<? extends ItemFrame> entityTypeExtendsItemFrame0, Level level1, BlockPos blockPos2, Direction direction3) {
        super(entityTypeExtendsItemFrame0, level1, blockPos2);
        this.setDirection(direction3);
    }

    @Override
    protected float getEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.0F;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_ITEM, ItemStack.EMPTY);
        this.m_20088_().define(DATA_ROTATION, 0);
    }

    @Override
    protected void setDirection(Direction direction0) {
        Validate.notNull(direction0);
        this.f_31699_ = direction0;
        if (direction0.getAxis().isHorizontal()) {
            this.m_146926_(0.0F);
            this.m_146922_((float) (this.f_31699_.get2DDataValue() * 90));
        } else {
            this.m_146926_((float) (-90 * direction0.getAxisDirection().getStep()));
            this.m_146922_(0.0F);
        }
        this.f_19860_ = this.m_146909_();
        this.f_19859_ = this.m_146908_();
        this.recalculateBoundingBox();
    }

    @Override
    protected void recalculateBoundingBox() {
        if (this.f_31699_ != null) {
            double $$0 = 0.46875;
            double $$1 = (double) this.f_31698_.m_123341_() + 0.5 - (double) this.f_31699_.getStepX() * 0.46875;
            double $$2 = (double) this.f_31698_.m_123342_() + 0.5 - (double) this.f_31699_.getStepY() * 0.46875;
            double $$3 = (double) this.f_31698_.m_123343_() + 0.5 - (double) this.f_31699_.getStepZ() * 0.46875;
            this.m_20343_($$1, $$2, $$3);
            double $$4 = (double) this.getWidth();
            double $$5 = (double) this.getHeight();
            double $$6 = (double) this.getWidth();
            Direction.Axis $$7 = this.f_31699_.getAxis();
            switch($$7) {
                case X:
                    $$4 = 1.0;
                    break;
                case Y:
                    $$5 = 1.0;
                    break;
                case Z:
                    $$6 = 1.0;
            }
            $$4 /= 32.0;
            $$5 /= 32.0;
            $$6 /= 32.0;
            this.m_20011_(new AABB($$1 - $$4, $$2 - $$5, $$3 - $$6, $$1 + $$4, $$2 + $$5, $$3 + $$6));
        }
    }

    @Override
    public boolean survives() {
        if (this.fixed) {
            return true;
        } else if (!this.m_9236_().m_45786_(this)) {
            return false;
        } else {
            BlockState $$0 = this.m_9236_().getBlockState(this.f_31698_.relative(this.f_31699_.getOpposite()));
            return $$0.m_280296_() || this.f_31699_.getAxis().isHorizontal() && DiodeBlock.isDiode($$0) ? this.m_9236_().getEntities(this, this.m_20191_(), f_31697_).isEmpty() : false;
        }
    }

    @Override
    public void move(MoverType moverType0, Vec3 vec1) {
        if (!this.fixed) {
            super.move(moverType0, vec1);
        }
    }

    @Override
    public void push(double double0, double double1, double double2) {
        if (!this.fixed) {
            super.push(double0, double1, double2);
        }
    }

    @Override
    public float getPickRadius() {
        return 0.0F;
    }

    @Override
    public void kill() {
        this.removeFramedMap(this.getItem());
        super.m_6074_();
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.fixed) {
            return !damageSource0.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource0.isCreativePlayer() ? false : super.hurt(damageSource0, float1);
        } else if (this.m_6673_(damageSource0)) {
            return false;
        } else if (!damageSource0.is(DamageTypeTags.IS_EXPLOSION) && !this.getItem().isEmpty()) {
            if (!this.m_9236_().isClientSide) {
                this.dropItem(damageSource0.getEntity(), false);
                this.m_146852_(GameEvent.BLOCK_CHANGE, damageSource0.getEntity());
                this.m_5496_(this.getRemoveItemSound(), 1.0F, 1.0F);
            }
            return true;
        } else {
            return super.hurt(damageSource0, float1);
        }
    }

    public SoundEvent getRemoveItemSound() {
        return SoundEvents.ITEM_FRAME_REMOVE_ITEM;
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double double0) {
        double $$1 = 16.0;
        $$1 *= 64.0 * m_20150_();
        return double0 < $$1 * $$1;
    }

    @Override
    public void dropItem(@Nullable Entity entity0) {
        this.m_5496_(this.getBreakSound(), 1.0F, 1.0F);
        this.dropItem(entity0, true);
        this.m_146852_(GameEvent.BLOCK_CHANGE, entity0);
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.ITEM_FRAME_BREAK;
    }

    @Override
    public void playPlacementSound() {
        this.m_5496_(this.getPlaceSound(), 1.0F, 1.0F);
    }

    public SoundEvent getPlaceSound() {
        return SoundEvents.ITEM_FRAME_PLACE;
    }

    private void dropItem(@Nullable Entity entity0, boolean boolean1) {
        if (!this.fixed) {
            ItemStack $$2 = this.getItem();
            this.setItem(ItemStack.EMPTY);
            if (!this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                if (entity0 == null) {
                    this.removeFramedMap($$2);
                }
            } else {
                if (entity0 instanceof Player $$3 && $$3.getAbilities().instabuild) {
                    this.removeFramedMap($$2);
                    return;
                }
                if (boolean1) {
                    this.m_19983_(this.getFrameItemStack());
                }
                if (!$$2.isEmpty()) {
                    $$2 = $$2.copy();
                    this.removeFramedMap($$2);
                    if (this.f_19796_.nextFloat() < this.dropChance) {
                        this.m_19983_($$2);
                    }
                }
            }
        }
    }

    private void removeFramedMap(ItemStack itemStack0) {
        this.getFramedMapId().ifPresent(p_289456_ -> {
            MapItemSavedData $$1 = MapItem.getSavedData(p_289456_, this.m_9236_());
            if ($$1 != null) {
                $$1.removedFromFrame(this.f_31698_, this.m_19879_());
                $$1.m_77760_(true);
            }
        });
        itemStack0.setEntityRepresentation(null);
    }

    public ItemStack getItem() {
        return this.m_20088_().get(DATA_ITEM);
    }

    public OptionalInt getFramedMapId() {
        ItemStack $$0 = this.getItem();
        if ($$0.is(Items.FILLED_MAP)) {
            Integer $$1 = MapItem.getMapId($$0);
            if ($$1 != null) {
                return OptionalInt.of($$1);
            }
        }
        return OptionalInt.empty();
    }

    public boolean hasFramedMap() {
        return this.getFramedMapId().isPresent();
    }

    public void setItem(ItemStack itemStack0) {
        this.setItem(itemStack0, true);
    }

    public void setItem(ItemStack itemStack0, boolean boolean1) {
        if (!itemStack0.isEmpty()) {
            itemStack0 = itemStack0.copyWithCount(1);
        }
        this.onItemChanged(itemStack0);
        this.m_20088_().set(DATA_ITEM, itemStack0);
        if (!itemStack0.isEmpty()) {
            this.m_5496_(this.getAddItemSound(), 1.0F, 1.0F);
        }
        if (boolean1 && this.f_31698_ != null) {
            this.m_9236_().updateNeighbourForOutputSignal(this.f_31698_, Blocks.AIR);
        }
    }

    public SoundEvent getAddItemSound() {
        return SoundEvents.ITEM_FRAME_ADD_ITEM;
    }

    @Override
    public SlotAccess getSlot(int int0) {
        return int0 == 0 ? new SlotAccess() {

            @Override
            public ItemStack get() {
                return ItemFrame.this.getItem();
            }

            @Override
            public boolean set(ItemStack p_149635_) {
                ItemFrame.this.setItem(p_149635_);
                return true;
            }
        } : super.m_141942_(int0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (entityDataAccessor0.equals(DATA_ITEM)) {
            this.onItemChanged(this.getItem());
        }
    }

    private void onItemChanged(ItemStack itemStack0) {
        if (!itemStack0.isEmpty() && itemStack0.getFrame() != this) {
            itemStack0.setEntityRepresentation(this);
        }
        this.recalculateBoundingBox();
    }

    public int getRotation() {
        return this.m_20088_().get(DATA_ROTATION);
    }

    public void setRotation(int int0) {
        this.setRotation(int0, true);
    }

    private void setRotation(int int0, boolean boolean1) {
        this.m_20088_().set(DATA_ROTATION, int0 % 8);
        if (boolean1 && this.f_31698_ != null) {
            this.m_9236_().updateNeighbourForOutputSignal(this.f_31698_, Blocks.AIR);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (!this.getItem().isEmpty()) {
            compoundTag0.put("Item", this.getItem().save(new CompoundTag()));
            compoundTag0.putByte("ItemRotation", (byte) this.getRotation());
            compoundTag0.putFloat("ItemDropChance", this.dropChance);
        }
        compoundTag0.putByte("Facing", (byte) this.f_31699_.get3DDataValue());
        compoundTag0.putBoolean("Invisible", this.m_20145_());
        compoundTag0.putBoolean("Fixed", this.fixed);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        CompoundTag $$1 = compoundTag0.getCompound("Item");
        if ($$1 != null && !$$1.isEmpty()) {
            ItemStack $$2 = ItemStack.of($$1);
            if ($$2.isEmpty()) {
                LOGGER.warn("Unable to load item from: {}", $$1);
            }
            ItemStack $$3 = this.getItem();
            if (!$$3.isEmpty() && !ItemStack.matches($$2, $$3)) {
                this.removeFramedMap($$3);
            }
            this.setItem($$2, false);
            this.setRotation(compoundTag0.getByte("ItemRotation"), false);
            if (compoundTag0.contains("ItemDropChance", 99)) {
                this.dropChance = compoundTag0.getFloat("ItemDropChance");
            }
        }
        this.setDirection(Direction.from3DDataValue(compoundTag0.getByte("Facing")));
        this.m_6842_(compoundTag0.getBoolean("Invisible"));
        this.fixed = compoundTag0.getBoolean("Fixed");
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        boolean $$3 = !this.getItem().isEmpty();
        boolean $$4 = !$$2.isEmpty();
        if (this.fixed) {
            return InteractionResult.PASS;
        } else if (!this.m_9236_().isClientSide) {
            if (!$$3) {
                if ($$4 && !this.m_213877_()) {
                    if ($$2.is(Items.FILLED_MAP)) {
                        MapItemSavedData $$5 = MapItem.getSavedData($$2, this.m_9236_());
                        if ($$5 != null && $$5.isTrackedCountOverLimit(256)) {
                            return InteractionResult.FAIL;
                        }
                    }
                    this.setItem($$2);
                    this.m_146852_(GameEvent.BLOCK_CHANGE, player0);
                    if (!player0.getAbilities().instabuild) {
                        $$2.shrink(1);
                    }
                }
            } else {
                this.m_5496_(this.getRotateItemSound(), 1.0F, 1.0F);
                this.setRotation(this.getRotation() + 1);
                this.m_146852_(GameEvent.BLOCK_CHANGE, player0);
            }
            return InteractionResult.CONSUME;
        } else {
            return !$$3 && !$$4 ? InteractionResult.PASS : InteractionResult.SUCCESS;
        }
    }

    public SoundEvent getRotateItemSound() {
        return SoundEvents.ITEM_FRAME_ROTATE_ITEM;
    }

    public int getAnalogOutput() {
        return this.getItem().isEmpty() ? 0 : this.getRotation() % 8 + 1;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.f_31699_.get3DDataValue(), this.m_31748_());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket0) {
        super.m_141965_(clientboundAddEntityPacket0);
        this.setDirection(Direction.from3DDataValue(clientboundAddEntityPacket0.getData()));
    }

    @Override
    public ItemStack getPickResult() {
        ItemStack $$0 = this.getItem();
        return $$0.isEmpty() ? this.getFrameItemStack() : $$0.copy();
    }

    protected ItemStack getFrameItemStack() {
        return new ItemStack(Items.ITEM_FRAME);
    }

    @Override
    public float getVisualRotationYInDegrees() {
        Direction $$0 = this.m_6350_();
        int $$1 = $$0.getAxis().isVertical() ? 90 * $$0.getAxisDirection().getStep() : 0;
        return (float) Mth.wrapDegrees(180 + $$0.get2DDataValue() * 90 + this.getRotation() * 45 + $$1);
    }
}