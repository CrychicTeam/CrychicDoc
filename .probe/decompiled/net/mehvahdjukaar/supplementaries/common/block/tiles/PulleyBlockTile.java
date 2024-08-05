package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PulleyBlock;
import net.mehvahdjukaar.supplementaries.common.inventories.PulleyContainerMenu;
import net.mehvahdjukaar.supplementaries.common.misc.RopeHelper;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PulleyBlockTile extends ItemDisplayTile {

    public PulleyBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.PULLEY_BLOCK_TILE.get(), pos, state);
    }

    @Override
    public boolean needsToUpdateClientWhenChanged() {
        return false;
    }

    @Override
    public void updateTileOnInventoryChanged() {
        ModBlockProperties.Winding type = getContentType(this.getDisplayedItem().getItem());
        BlockState state = this.m_58900_();
        if (state.m_61143_(PulleyBlock.TYPE) != type) {
            this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) state.m_61124_(PulleyBlock.TYPE, type));
        }
    }

    public static ModBlockProperties.Winding getContentType(Item item) {
        ModBlockProperties.Winding type = ModBlockProperties.Winding.NONE;
        if (item instanceof BlockItem bi && bi.getBlock() instanceof ChainBlock || item.builtInRegistryHolder().is(ModTags.CHAINS)) {
            type = ModBlockProperties.Winding.CHAIN;
        } else if (item.builtInRegistryHolder().is(ModTags.ROPES)) {
            type = ModBlockProperties.Winding.ROPE;
        }
        return type;
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.pulley_block");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return new PulleyContainerMenu(id, player, this);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return getContentType(stack.getItem()) != ModBlockProperties.Winding.NONE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public boolean rotateDirectly(Rotation rot) {
        if (rot == Rotation.CLOCKWISE_90) {
            return this.pullRopeUp();
        } else {
            return rot == Rotation.COUNTERCLOCKWISE_90 ? this.releaseRopeDown() : false;
        }
    }

    public boolean pullRopeUp() {
        return this.pullRope(Direction.DOWN, Integer.MAX_VALUE, true);
    }

    public boolean pullRope(Direction moveDir, int maxDist, boolean addItem) {
        ItemStack stack = this.getDisplayedItem();
        boolean addNewItem = false;
        if (stack.isEmpty()) {
            Item i = this.f_58857_.getBlockState(this.f_58858_.below()).m_60734_().asItem();
            if (getContentType(i) == ModBlockProperties.Winding.NONE) {
                return false;
            }
            stack = new ItemStack(i);
            addNewItem = true;
        }
        if (stack.getCount() + 1 <= stack.getMaxStackSize() && stack.getItem() instanceof BlockItem) {
            Block ropeBlock = ((BlockItem) stack.getItem()).getBlock();
            boolean success = RopeHelper.removeRope(this.f_58858_.relative(moveDir), this.f_58857_, ropeBlock, moveDir, maxDist);
            if (success) {
                SoundType soundtype = ropeBlock.defaultBlockState().m_60827_();
                this.f_58857_.playSound(null, this.f_58858_, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (addNewItem) {
                    this.setDisplayedItem(stack);
                } else if (addItem) {
                    stack.grow(1);
                }
                this.m_6596_();
            }
            return success;
        } else {
            return false;
        }
    }

    public boolean releaseRopeDown() {
        return this.releaseRope(Direction.DOWN, Integer.MAX_VALUE, true);
    }

    public boolean releaseRope(Direction dir, int maxDist, boolean removeItem) {
        ItemStack stack = this.getDisplayedItem();
        if (stack.getCount() >= 1 && stack.getItem() instanceof BlockItem bi) {
            Block var9 = bi.getBlock();
            boolean success = RopeHelper.addRope(this.f_58858_.relative(dir), this.f_58857_, null, InteractionHand.MAIN_HAND, var9, dir, dir == Direction.DOWN, maxDist);
            if (success) {
                SoundType soundtype = var9.defaultBlockState().m_60827_();
                this.f_58857_.playSound(null, this.f_58858_, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (removeItem) {
                    stack.shrink(1);
                    this.m_6596_();
                }
            }
            return success;
        } else {
            return false;
        }
    }

    public boolean rotateIndirect(Player player, InteractionHand hand, Block ropeBlock, Direction moveDir, boolean retracting) {
        ItemStack stack = this.getDisplayedItem();
        if (stack.isEmpty()) {
            if (retracting) {
                return false;
            } else {
                this.setDisplayedItem(new ItemStack(ropeBlock));
                return true;
            }
        } else if (!stack.is(ropeBlock.asItem())) {
            return false;
        } else {
            BlockState state = this.m_58900_();
            Direction.Axis axis = (Direction.Axis) state.m_61143_(PulleyBlock.f_55923_);
            if (axis == moveDir.getAxis()) {
                return false;
            } else {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) state.m_61122_(PulleyBlock.FLIPPED));
                Direction[] order = moveDir.getAxis().isHorizontal() ? new Direction[] { Direction.DOWN } : new Direction[] { moveDir, moveDir.getClockWise(axis), moveDir.getCounterClockWise(axis) };
                List<Direction> remaining = new ArrayList();
                int maxSideDist = 7;
                for (Direction d : order) {
                    if (RopeHelper.isCorrectRope(ropeBlock, this.f_58857_.getBlockState(this.f_58858_.relative(d)), d)) {
                        if (this.moveConnected(retracting, maxSideDist, d)) {
                            return true;
                        }
                        return false;
                    }
                    remaining.add(d);
                }
                for (Direction d : remaining) {
                    if (this.moveConnected(retracting, maxSideDist, d)) {
                        return true;
                    }
                }
                if (retracting) {
                    stack.shrink(1);
                    this.m_6596_();
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private boolean moveConnected(boolean retracting, int maxSideDist, Direction d) {
        int dist = d == Direction.DOWN ? Integer.MAX_VALUE : maxSideDist;
        return retracting ? this.pullRope(d, dist, false) : this.releaseRope(d, dist, false);
    }
}