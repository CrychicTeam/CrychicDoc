package org.violetmoon.quark.content.automation.block.be;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.block.ChuteBlock;
import org.violetmoon.quark.content.automation.module.ChuteModule;
import org.violetmoon.quark.content.building.module.GrateModule;
import org.violetmoon.quark.content.building.module.HollowLogsModule;
import org.violetmoon.zeta.block.be.ZetaBlockEntity;

public class ChuteBlockEntity extends ZetaBlockEntity {

    private final IItemHandler handler = new IItemHandler() {

        @Override
        public int getSlots() {
            return 1;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (!ChuteBlockEntity.this.canDropItem()) {
                return stack;
            } else {
                if (!simulate && ChuteBlockEntity.this.f_58857_ != null && !stack.isEmpty()) {
                    ItemEntity entity = new ItemEntity(ChuteBlockEntity.this.f_58857_, (double) ChuteBlockEntity.this.f_58858_.m_123341_() + 0.5, (double) ChuteBlockEntity.this.f_58858_.m_123342_() - 0.5, (double) ChuteBlockEntity.this.f_58858_.m_123343_() + 0.5, stack.copy());
                    entity.m_20334_(0.0, 0.0, 0.0);
                    ChuteBlockEntity.this.f_58857_.m_7967_(entity);
                }
                return ItemStack.EMPTY;
            }
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }
    };

    public ChuteBlockEntity(BlockPos pos, BlockState state) {
        super(ChuteModule.blockEntityType, pos, state);
    }

    private boolean canDropItem() {
        if (this.f_58857_ != null && (Boolean) this.f_58857_.getBlockState(this.f_58858_).m_61143_(ChuteBlock.ENABLED)) {
            BlockPos below = this.f_58858_.below();
            BlockState state = this.f_58857_.getBlockState(below);
            return state.m_60795_() || state.m_60812_(this.f_58857_, below).isEmpty() || state.m_60734_() == GrateModule.grate || state.m_204336_(HollowLogsModule.hollowLogsTag) && state.m_61143_(RotatedPillarBlock.AXIS) == Direction.Axis.Y;
        } else {
            return false;
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return side != Direction.DOWN && cap == ForgeCapabilities.ITEM_HANDLER ? LazyOptional.<IItemHandler>of(() -> this.handler).cast() : super.getCapability(cap, side);
    }
}