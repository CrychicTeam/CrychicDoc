package net.minecraft.core.dispenser;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.DispenserBlock;
import org.slf4j.Logger;

public class ShulkerBoxDispenseBehavior extends OptionalDispenseItemBehavior {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    protected ItemStack execute(BlockSource blockSource0, ItemStack itemStack1) {
        this.m_123573_(false);
        Item $$2 = itemStack1.getItem();
        if ($$2 instanceof BlockItem) {
            Direction $$3 = (Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING);
            BlockPos $$4 = blockSource0.getPos().relative($$3);
            Direction $$5 = blockSource0.getLevel().m_46859_($$4.below()) ? $$3 : Direction.UP;
            try {
                this.m_123573_(((BlockItem) $$2).place(new DirectionalPlaceContext(blockSource0.getLevel(), $$4, $$3, itemStack1, $$5)).consumesAction());
            } catch (Exception var8) {
                LOGGER.error("Error trying to place shulker box at {}", $$4, var8);
            }
        }
        return itemStack1;
    }
}