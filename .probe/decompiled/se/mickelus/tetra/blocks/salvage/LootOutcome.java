package se.mickelus.tetra.blocks.salvage;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class LootOutcome implements InteractionOutcome {

    @Override
    public boolean apply(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction hitFace) {
        return false;
    }
}