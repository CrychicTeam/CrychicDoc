package com.simibubi.create.content.kinetics.mechanicalArm;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ArmInteractionPoint {

    protected final ArmInteractionPointType type;

    protected Level level;

    protected final BlockPos pos;

    protected ArmInteractionPoint.Mode mode = ArmInteractionPoint.Mode.DEPOSIT;

    protected BlockState cachedState;

    protected LazyOptional<IItemHandler> cachedHandler = LazyOptional.empty();

    protected ArmAngleTarget cachedAngles;

    public ArmInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
        this.type = type;
        this.level = level;
        this.pos = pos;
        this.cachedState = state;
    }

    public ArmInteractionPointType getType() {
        return this.type;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ArmInteractionPoint.Mode getMode() {
        return this.mode;
    }

    public void cycleMode() {
        this.mode = this.mode == ArmInteractionPoint.Mode.DEPOSIT ? ArmInteractionPoint.Mode.TAKE : ArmInteractionPoint.Mode.DEPOSIT;
    }

    protected Vec3 getInteractionPositionVector() {
        return VecHelper.getCenterOf(this.pos);
    }

    protected Direction getInteractionDirection() {
        return Direction.DOWN;
    }

    public ArmAngleTarget getTargetAngles(BlockPos armPos, boolean ceiling) {
        if (this.cachedAngles == null) {
            this.cachedAngles = new ArmAngleTarget(armPos, this.getInteractionPositionVector(), this.getInteractionDirection(), ceiling);
        }
        return this.cachedAngles;
    }

    public void updateCachedState() {
        this.cachedState = this.level.getBlockState(this.pos);
    }

    public boolean isValid() {
        this.updateCachedState();
        return this.type.canCreatePoint(this.level, this.pos, this.cachedState);
    }

    public void keepAlive() {
    }

    @Nullable
    protected IItemHandler getHandler() {
        if (!this.cachedHandler.isPresent()) {
            BlockEntity be = this.level.getBlockEntity(this.pos);
            if (be == null) {
                return null;
            }
            this.cachedHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
        }
        return this.cachedHandler.orElse(null);
    }

    public ItemStack insert(ItemStack stack, boolean simulate) {
        IItemHandler handler = this.getHandler();
        return handler == null ? stack : ItemHandlerHelper.insertItem(handler, stack, simulate);
    }

    public ItemStack extract(int slot, int amount, boolean simulate) {
        IItemHandler handler = this.getHandler();
        return handler == null ? ItemStack.EMPTY : handler.extractItem(slot, amount, simulate);
    }

    public ItemStack extract(int slot, boolean simulate) {
        return this.extract(slot, 64, simulate);
    }

    public int getSlotCount() {
        IItemHandler handler = this.getHandler();
        return handler == null ? 0 : handler.getSlots();
    }

    protected void serialize(CompoundTag nbt, BlockPos anchor) {
        NBTHelper.writeEnum(nbt, "Mode", this.mode);
    }

    protected void deserialize(CompoundTag nbt, BlockPos anchor) {
        this.mode = NBTHelper.readEnum(nbt, "Mode", ArmInteractionPoint.Mode.class);
    }

    public final CompoundTag serialize(BlockPos anchor) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Type", this.type.getId().toString());
        nbt.put("Pos", NbtUtils.writeBlockPos(this.pos.subtract(anchor)));
        this.serialize(nbt, anchor);
        return nbt;
    }

    @Nullable
    public static ArmInteractionPoint deserialize(CompoundTag nbt, Level level, BlockPos anchor) {
        ResourceLocation id = ResourceLocation.tryParse(nbt.getString("Type"));
        if (id == null) {
            return null;
        } else {
            ArmInteractionPointType type = ArmInteractionPointType.get(id);
            if (type == null) {
                return null;
            } else {
                BlockPos pos = NbtUtils.readBlockPos(nbt.getCompound("Pos")).offset(anchor);
                BlockState state = level.getBlockState(pos);
                if (!type.canCreatePoint(level, pos, state)) {
                    return null;
                } else {
                    ArmInteractionPoint point = type.createPoint(level, pos, state);
                    if (point == null) {
                        return null;
                    } else {
                        point.deserialize(nbt, anchor);
                        return point;
                    }
                }
            }
        }
    }

    public static void transformPos(CompoundTag nbt, StructureTransform transform) {
        BlockPos pos = NbtUtils.readBlockPos(nbt.getCompound("Pos"));
        pos = transform.applyWithoutOffset(pos);
        nbt.put("Pos", NbtUtils.writeBlockPos(pos));
    }

    public static boolean isInteractable(Level level, BlockPos pos, BlockState state) {
        return ArmInteractionPointType.getPrimaryType(level, pos, state) != null;
    }

    @Nullable
    public static ArmInteractionPoint create(Level level, BlockPos pos, BlockState state) {
        ArmInteractionPointType type = ArmInteractionPointType.getPrimaryType(level, pos, state);
        return type == null ? null : type.createPoint(level, pos, state);
    }

    public static enum Mode {

        DEPOSIT("mechanical_arm.deposit_to", 14532966), TAKE("mechanical_arm.extract_from", 8375776);

        private final String translationKey;

        private final int color;

        private Mode(String translationKey, int color) {
            this.translationKey = translationKey;
            this.color = color;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }

        public int getColor() {
            return this.color;
        }
    }
}