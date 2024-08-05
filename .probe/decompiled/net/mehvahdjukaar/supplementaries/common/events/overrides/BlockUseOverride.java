package net.mehvahdjukaar.supplementaries.common.events.overrides;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

interface BlockUseOverride {

    default boolean altersWorld() {
        return false;
    }

    boolean isEnabled();

    boolean appliesToBlock(Block var1);

    InteractionResult tryPerformingAction(BlockState var1, BlockPos var2, Level var3, Player var4, InteractionHand var5, ItemStack var6, BlockHitResult var7);
}