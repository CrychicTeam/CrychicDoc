package se.mickelus.tetra.blocks.salvage;

import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class TileEntityOutcome<T extends BlockEntity> implements InteractionOutcome {

    Class<T> tileEntityClass;

    Function<T, Boolean> outcome;

    public TileEntityOutcome(Class<T> tileEntityClass, Function<T, Boolean> outcome) {
        this.tileEntityClass = tileEntityClass;
        this.outcome = outcome;
    }

    @Override
    public boolean apply(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction hitFace) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (this.tileEntityClass.isInstance(tileEntity)) {
            boolean result = (Boolean) this.outcome.apply((BlockEntity) this.tileEntityClass.cast(tileEntity));
            world.sendBlockUpdated(pos, blockState, blockState, 3);
            return result;
        } else {
            return false;
        }
    }
}