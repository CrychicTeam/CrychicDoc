package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface FaucetItemSource {

    ItemStack tryExtractItem(Level var1, BlockPos var2, BlockState var3, Direction var4, @Nullable BlockEntity var5);
}