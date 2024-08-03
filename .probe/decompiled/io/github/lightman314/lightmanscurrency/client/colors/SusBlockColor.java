package io.github.lightman314.lightmanscurrency.client.colors;

import io.github.lightman314.lightmanscurrency.common.blockentity.CoinJarBlockEntity;
import javax.annotation.Nonnull;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SusBlockColor implements BlockColor {

    @Override
    public int getColor(@Nonnull BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int layer) {
        return layer == 0 && level.m_7702_(pos) instanceof CoinJarBlockEntity jarBlock ? jarBlock.getColor() : 16777215;
    }

    public static class Item implements ItemColor {

        @Override
        public int getColor(@Nonnull ItemStack stack, int layer) {
            return layer == 0 && stack.getItem() instanceof DyeableLeatherItem dyeable ? dyeable.getColor(stack) : 16777215;
        }
    }
}