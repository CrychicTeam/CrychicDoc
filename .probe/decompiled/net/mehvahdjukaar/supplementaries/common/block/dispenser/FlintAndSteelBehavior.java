package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

class FlintAndSteelBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected FlintAndSteelBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        BlockPos blockpos = source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
        BlockState state = world.m_8055_(blockpos);
        if (state.m_60734_() instanceof ILightable block) {
            if (block.lightUp(null, state, blockpos, world, ILightable.FireSourceType.FLINT_AND_STEEL)) {
                if (stack.hurt(1, world.f_46441_, null)) {
                    stack.setCount(0);
                }
                return InteractionResultHolder.success(stack);
            } else {
                return InteractionResultHolder.fail(stack);
            }
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
}