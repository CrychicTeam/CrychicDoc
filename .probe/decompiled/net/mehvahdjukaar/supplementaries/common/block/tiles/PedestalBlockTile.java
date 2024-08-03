package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PedestalBlock;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PedestalBlockTile extends ItemDisplayTile {

    private PedestalBlockTile.DisplayType type = PedestalBlockTile.DisplayType.ITEM;

    public PedestalBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType) ModRegistry.PEDESTAL_TILE.get(), pos, state);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_, this.f_58858_.offset(1, 2, 1));
    }

    public PedestalBlockTile.DisplayType getDisplayType() {
        return this.type;
    }

    @Override
    public void updateTileOnInventoryChanged() {
        BlockState state = this.m_58900_();
        boolean hasItem = !this.m_7983_();
        BlockState newState = (BlockState) ((BlockState) state.m_61124_(PedestalBlock.ITEM_STATUS, PedestalBlock.getStatus(this.f_58857_, this.f_58858_, hasItem))).m_61124_(PedestalBlock.UP, PedestalBlock.canConnectTo(this.f_58857_.getBlockState(this.f_58858_.above()), this.f_58858_, this.f_58857_, Direction.UP, hasItem));
        if (state != newState) {
            this.f_58857_.setBlock(this.f_58858_, newState, 3);
            if (!((ModBlockProperties.DisplayStatus) state.m_61143_(PedestalBlock.ITEM_STATUS)).hasTile()) {
                this.f_58857_.removeBlockEntity(this.f_58858_);
            }
        }
        ItemStack stack = this.getDisplayedItem();
        Item it = stack.getItem();
        if (MiscUtils.isSword(it) || stack.is(ModTags.PEDESTAL_DOWNRIGHT)) {
            this.type = PedestalBlockTile.DisplayType.SWORD;
        } else if (it instanceof TridentItem || stack.is(ModTags.PEDESTAL_UPRIGHT)) {
            this.type = PedestalBlockTile.DisplayType.TRIDENT;
        } else if (it instanceof EndCrystalItem) {
            this.type = PedestalBlockTile.DisplayType.CRYSTAL;
        } else if (it == ModRegistry.GLOBE_ITEM.get()) {
            this.type = PedestalBlockTile.DisplayType.GLOBE;
        } else if (it == ModRegistry.GLOBE_SEPIA_ITEM.get()) {
            this.type = PedestalBlockTile.DisplayType.SEPIA_GLOBE;
        } else if (it instanceof BlockItem) {
            this.type = PedestalBlockTile.DisplayType.BLOCK;
        } else {
            this.type = PedestalBlockTile.DisplayType.ITEM;
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.type = PedestalBlockTile.DisplayType.values()[compound.getInt("Type")];
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Type", this.type.ordinal());
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.pedestal");
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public static enum DisplayType {

        ITEM,
        BLOCK,
        SWORD,
        TRIDENT,
        CRYSTAL,
        GLOBE,
        SEPIA_GLOBE;

        public boolean isGlobe() {
            return this == GLOBE || this == SEPIA_GLOBE;
        }
    }
}