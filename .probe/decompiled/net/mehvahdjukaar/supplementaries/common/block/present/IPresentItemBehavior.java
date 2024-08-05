package net.mehvahdjukaar.supplementaries.common.block.present;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.PositionImpl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IPresentItemBehavior {

    default ItemStack trigger(BlockSource pSource, ItemStack pStack) {
        ItemStack itemstack = (ItemStack) this.performSpecialAction(pSource, pStack).orElseGet(() -> this.spitItem(pSource, pStack));
        this.playAnimation(pSource);
        return itemstack;
    }

    Optional<ItemStack> performSpecialAction(BlockSource var1, ItemStack var2);

    private ItemStack spitItem(BlockSource pSource, ItemStack pStack) {
        ItemStack itemstack = pStack.split(1);
        spawnItem(pSource.getLevel(), itemstack, 7.0, pSource);
        return pStack;
    }

    static void spawnItem(Level pLevel, ItemStack pStack, double pSpeed, BlockSource source) {
        Position p = getDispensePosition(source);
        ItemEntity itementity = new ItemEntity(pLevel, p.x(), p.y(), p.z(), pStack);
        itementity.m_20334_(pLevel.random.nextGaussian() * 0.0075 * pSpeed, pLevel.random.nextGaussian() * 0.0075 * pSpeed + 0.3, pLevel.random.nextGaussian() * 0.0075 * pSpeed);
        pLevel.m_7967_(itementity);
    }

    default void playAnimation(BlockSource pSource) {
        Level level = pSource.getLevel();
        BlockPos pos = pSource.getPos();
        level.blockEvent(pos, pSource.getBlockState().m_60734_(), 0, 0);
    }

    static Position getDispensePosition(BlockSource source) {
        double d0 = source.x();
        double d1 = source.y() + 0.125 + 1.0E-4;
        double d2 = source.z();
        return new PositionImpl(d0, d1, d2);
    }
}