package dev.xkmc.l2complements.content.enchantment.digging;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public record DiggerContext(Player player, Direction dire, ItemStack stack, int level, BlockPos pos, BlockState state) {
}