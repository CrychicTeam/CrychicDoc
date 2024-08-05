package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BambooSpikesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

class BambooSpikesBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected BambooSpikesBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        BlockPos blockpos = source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
        BlockState state = world.m_8055_(blockpos);
        if (state.m_60734_() instanceof BambooSpikesBlock) {
            return BambooSpikesBlock.tryAddingPotion(state, world, blockpos, PotionUtils.getPotion(stack)) ? InteractionResultHolder.success(new ItemStack(Items.GLASS_BOTTLE)) : InteractionResultHolder.fail(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
}