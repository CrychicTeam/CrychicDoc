package net.mehvahdjukaar.amendments.integration;

import java.util.Optional;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.mehvahdjukaar.suppsquared.common.LightableLanternBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SuppSquaredCompat {

    public static boolean isLightableLantern(Block b) {
        return b instanceof LightableLanternBlock;
    }

    @Nullable
    public static InteractionResult lightUpLantern(Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, WallLanternBlockTile te, BlockState lantern) {
        if (lantern.m_60734_() instanceof LightableLanternBlock) {
            Optional<BlockState> opt = LightableLanternBlock.toggleLight(lantern, pLevel, pPos, pPlayer, pHand);
            if (opt.isPresent()) {
                te.setHeldBlock((BlockState) opt.get());
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        }
        return null;
    }
}