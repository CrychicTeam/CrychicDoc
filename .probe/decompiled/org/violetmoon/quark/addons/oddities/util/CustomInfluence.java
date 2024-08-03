package org.violetmoon.quark.addons.oddities.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.quark.api.IEnchantmentInfluencer;

public record CustomInfluence(int strength, int color, Influence influence) implements IEnchantmentInfluencer {

    @Override
    public float[] getEnchantmentInfluenceColor(BlockGetter world, BlockPos pos, BlockState state) {
        float r = (float) FastColor.ARGB32.red(this.color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(this.color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(this.color) / 255.0F;
        return new float[] { r, g, b };
    }

    @Override
    public int getInfluenceStack(BlockGetter world, BlockPos pos, BlockState state) {
        return this.strength;
    }

    @Override
    public boolean influencesEnchantment(BlockGetter world, BlockPos pos, BlockState state, Enchantment enchantment) {
        return this.influence.boost().contains(enchantment);
    }

    @Override
    public boolean dampensEnchantment(BlockGetter world, BlockPos pos, BlockState state, Enchantment enchantment) {
        return this.influence.dampen().contains(enchantment);
    }
}