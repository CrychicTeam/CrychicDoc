package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;

class FishBucketJarBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected FishBucketJarBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        BlockPos blockpos = source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
        if (world.m_7702_(blockpos) instanceof JarBlockTile tile) {
            if (tile.fluidHolder.isEmpty() && tile.m_7983_() && tile.mobContainer.interactWithBucket(stack, world, blockpos, null, null)) {
                tile.m_6596_();
                return InteractionResultHolder.success(new ItemStack(Items.BUCKET));
            } else {
                return InteractionResultHolder.fail(stack);
            }
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
}