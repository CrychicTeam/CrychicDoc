package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;

class GunpowderBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected GunpowderBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
        BlockPos blockpos = source.getPos().relative(direction);
        Direction direction1 = source.getLevel().m_46859_(blockpos.below()) ? direction : Direction.UP;
        InteractionResult result = ItemsUtil.place(new DirectionalPlaceContext(source.getLevel(), blockpos, direction, stack, direction1), (Block) ModRegistry.GUNPOWDER_BLOCK.get());
        return result.consumesAction() ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
    }
}