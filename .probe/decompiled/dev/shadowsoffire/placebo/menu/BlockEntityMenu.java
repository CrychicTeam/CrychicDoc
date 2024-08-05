package dev.shadowsoffire.placebo.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BlockEntityMenu<T extends BlockEntity> extends PlaceboContainerMenu {

    protected final BlockPos pos;

    protected final T tile;

    protected BlockEntityMenu(MenuType<?> type, int id, Inventory pInv, BlockPos pos) {
        super(type, id, pInv);
        this.pos = pos;
        this.tile = (T) this.level.getBlockEntity(pos);
        if (this.tile instanceof SimpleDataSlots.IDataAutoRegister) {
            ((SimpleDataSlots.IDataAutoRegister) this.tile).registerSlots(x$0 -> this.m_38895_(x$0));
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.tile != null && this.tile.getType().isValid(this.level.getBlockState(this.pos));
    }
}