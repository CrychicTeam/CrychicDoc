package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

class KeyBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected KeyBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel level = source.getLevel();
        Direction dir = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
        BlockPos pos = source.getPos().relative(dir);
        if (level.m_7702_(pos) instanceof KeyLockableTile kt) {
            Player fp = FakePlayerManager.getDefault(level);
            fp.m_21008_(InteractionHand.MAIN_HAND, stack);
            BlockHitResult blockHitResult = new BlockHitResult(Vec3.atCenterOf(pos), dir, pos, false);
            kt.m_58900_().m_60664_(level, fp, InteractionHand.MAIN_HAND, blockHitResult);
            return InteractionResultHolder.consume(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }
}