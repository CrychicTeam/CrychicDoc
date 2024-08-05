package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.stream.IntStream;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.common.block.blocks.HourGlassBlock;
import net.mehvahdjukaar.supplementaries.common.block.hourglass.HourglassTimeData;
import net.mehvahdjukaar.supplementaries.common.block.hourglass.HourglassTimesManager;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HourGlassBlockTile extends ItemDisplayTile {

    private HourglassTimeData sandData = HourglassTimeData.EMPTY;

    private float progress = 0.0F;

    private float prevProgress = 0.0F;

    private int power = 0;

    private ResourceLocation cachedTexture = null;

    public HourGlassBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.HOURGLASS_TILE.get(), pos, state);
    }

    @Override
    public void updateTileOnInventoryChanged() {
        this.sandData = HourglassTimesManager.getData(this.getDisplayedItem().getItem());
        int p = this.getDirection() == Direction.DOWN ? 1 : 0;
        int l = this.sandData.light();
        if (l != (Integer) this.m_58900_().m_61143_(HourGlassBlock.LIGHT_LEVEL) && this.f_58857_ != null) {
            this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(HourGlassBlock.LIGHT_LEVEL, l), 20);
        }
        this.prevProgress = (float) p;
        this.progress = (float) p;
    }

    public HourglassTimeData getSandData() {
        return this.sandData;
    }

    public float getProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevProgress, this.progress);
    }

    public ResourceLocation getTexture() {
        if (this.cachedTexture == null) {
            this.cachedTexture = this.sandData.computeTexture(this.getDisplayedItem(), this.f_58857_);
        }
        return this.cachedTexture;
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, HourGlassBlockTile tile) {
        Direction dir = (Direction) pState.m_61143_(HourGlassBlock.FACING);
        if (!tile.sandData.isEmpty()) {
            tile.prevProgress = tile.progress;
            if (dir == Direction.UP && tile.progress != 1.0F) {
                tile.progress = Math.min(tile.progress + tile.sandData.getIncrement(), 1.0F);
            } else if (dir == Direction.DOWN && tile.progress != 0.0F) {
                tile.progress = Math.max(tile.progress - tile.sandData.getIncrement(), 0.0F);
            }
        }
        if (!pLevel.isClientSide) {
            int p;
            if (dir == Direction.DOWN) {
                p = 1 + (int) ((1.0F - tile.progress) * 14.0F);
            } else {
                p = 1 + (int) (tile.progress * 14.0F);
            }
            if (p != tile.power) {
                tile.power = p;
                pLevel.updateNeighbourForOutputSignal(pPos, pState.m_60734_());
            }
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.progress = compound.getFloat("Progress");
        this.prevProgress = compound.getFloat("PrevProgress");
        this.cachedTexture = null;
        this.sandData = HourglassTimesManager.getData(this.getDisplayedItem().getItem());
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("Progress", this.progress);
        tag.putFloat("PrevProgress", this.prevProgress);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.supplementaries.hourglass");
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.m_7983_() && !HourglassTimesManager.getData(stack.getItem()).isEmpty();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, this.m_6643_()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return direction == Direction.UP ? this.canPlaceItem(0, stack) : false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        Direction dir = (Direction) this.m_58900_().m_61143_(HourGlassBlock.FACING);
        return dir == Direction.UP && this.progress == 1.0F || dir == Direction.DOWN && this.progress == 0.0F;
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(HourGlassBlock.FACING);
    }

    public int getPower() {
        return this.power;
    }

    @Override
    public SoundEvent getAddItemSound() {
        return SoundEvents.SAND_PLACE;
    }
}