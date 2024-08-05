package net.mehvahdjukaar.supplementaries.client.renderers.color;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class DefaultWaterColor implements ItemColor, BlockColor {

    @Override
    public int getColor(ItemStack stack, int color) {
        return color != 0 ? -1 : 4159204;
    }

    @Override
    public int getColor(BlockState state, BlockAndTintGetter reader, BlockPos pos, int tint) {
        if (tint != 1) {
            return -1;
        } else {
            return reader != null && pos != null ? BiomeColors.getAverageWaterColor(reader, pos) : -1;
        }
    }
}