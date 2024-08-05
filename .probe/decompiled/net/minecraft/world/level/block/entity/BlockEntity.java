package net.minecraft.world.level.block.entity;

import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public abstract class BlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final BlockEntityType<?> type;

    @Nullable
    protected Level level;

    protected final BlockPos worldPosition;

    protected boolean remove;

    private BlockState blockState;

    public BlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        this.type = blockEntityType0;
        this.worldPosition = blockPos1.immutable();
        this.blockState = blockState2;
    }

    public static BlockPos getPosFromTag(CompoundTag compoundTag0) {
        return new BlockPos(compoundTag0.getInt("x"), compoundTag0.getInt("y"), compoundTag0.getInt("z"));
    }

    @Nullable
    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level0) {
        this.level = level0;
    }

    public boolean hasLevel() {
        return this.level != null;
    }

    public void load(CompoundTag compoundTag0) {
    }

    protected void saveAdditional(CompoundTag compoundTag0) {
    }

    public final CompoundTag saveWithFullMetadata() {
        CompoundTag $$0 = this.saveWithoutMetadata();
        this.saveMetadata($$0);
        return $$0;
    }

    public final CompoundTag saveWithId() {
        CompoundTag $$0 = this.saveWithoutMetadata();
        this.saveId($$0);
        return $$0;
    }

    public final CompoundTag saveWithoutMetadata() {
        CompoundTag $$0 = new CompoundTag();
        this.saveAdditional($$0);
        return $$0;
    }

    private void saveId(CompoundTag compoundTag0) {
        ResourceLocation $$1 = BlockEntityType.getKey(this.getType());
        if ($$1 == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            compoundTag0.putString("id", $$1.toString());
        }
    }

    public static void addEntityType(CompoundTag compoundTag0, BlockEntityType<?> blockEntityType1) {
        compoundTag0.putString("id", BlockEntityType.getKey(blockEntityType1).toString());
    }

    public void saveToItem(ItemStack itemStack0) {
        BlockItem.setBlockEntityData(itemStack0, this.getType(), this.saveWithoutMetadata());
    }

    private void saveMetadata(CompoundTag compoundTag0) {
        this.saveId(compoundTag0);
        compoundTag0.putInt("x", this.worldPosition.m_123341_());
        compoundTag0.putInt("y", this.worldPosition.m_123342_());
        compoundTag0.putInt("z", this.worldPosition.m_123343_());
    }

    @Nullable
    public static BlockEntity loadStatic(BlockPos blockPos0, BlockState blockState1, CompoundTag compoundTag2) {
        String $$3 = compoundTag2.getString("id");
        ResourceLocation $$4 = ResourceLocation.tryParse($$3);
        if ($$4 == null) {
            LOGGER.error("Block entity has invalid type: {}", $$3);
            return null;
        } else {
            return (BlockEntity) BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional($$4).map(p_155240_ -> {
                try {
                    return p_155240_.create(blockPos0, blockState1);
                } catch (Throwable var5) {
                    LOGGER.error("Failed to create block entity {}", $$3, var5);
                    return null;
                }
            }).map(p_155249_ -> {
                try {
                    p_155249_.load(compoundTag2);
                    return p_155249_;
                } catch (Throwable var4x) {
                    LOGGER.error("Failed to load data for block entity {}", $$3, var4x);
                    return null;
                }
            }).orElseGet(() -> {
                LOGGER.warn("Skipping BlockEntity with id {}", $$3);
                return null;
            });
        }
    }

    public void setChanged() {
        if (this.level != null) {
            setChanged(this.level, this.worldPosition, this.blockState);
        }
    }

    protected static void setChanged(Level level0, BlockPos blockPos1, BlockState blockState2) {
        level0.blockEntityChanged(blockPos1);
        if (!blockState2.m_60795_()) {
            level0.updateNeighbourForOutputSignal(blockPos1, blockState2.m_60734_());
        }
    }

    public BlockPos getBlockPos() {
        return this.worldPosition;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return null;
    }

    public CompoundTag getUpdateTag() {
        return new CompoundTag();
    }

    public boolean isRemoved() {
        return this.remove;
    }

    public void setRemoved() {
        this.remove = true;
    }

    public void clearRemoved() {
        this.remove = false;
    }

    public boolean triggerEvent(int int0, int int1) {
        return false;
    }

    public void fillCrashReportCategory(CrashReportCategory crashReportCategory0) {
        crashReportCategory0.setDetail("Name", (CrashReportDetail<String>) (() -> BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(this.getType()) + " // " + this.getClass().getCanonicalName()));
        if (this.level != null) {
            CrashReportCategory.populateBlockDetails(crashReportCategory0, this.level, this.worldPosition, this.getBlockState());
            CrashReportCategory.populateBlockDetails(crashReportCategory0, this.level, this.worldPosition, this.level.getBlockState(this.worldPosition));
        }
    }

    public boolean onlyOpCanSetNbt() {
        return false;
    }

    public BlockEntityType<?> getType() {
        return this.type;
    }

    @Deprecated
    public void setBlockState(BlockState blockState0) {
        this.blockState = blockState0;
    }
}