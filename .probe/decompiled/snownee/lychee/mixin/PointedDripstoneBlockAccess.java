package snownee.lychee.mixin;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ PointedDripstoneBlock.class })
public interface PointedDripstoneBlockAccess {

    @Invoker
    static boolean callIsStalactiteStartPos(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        throw new IllegalStateException();
    }

    @Invoker
    static BlockPos callFindTip(BlockState blockState, LevelAccessor level, BlockPos blockPos, int i, boolean b) {
        throw new IllegalStateException();
    }

    @Invoker
    static boolean callCanDripThrough(BlockGetter level, BlockPos blockPos, BlockState blockState) {
        throw new IllegalStateException();
    }

    @Invoker
    static Optional<BlockPos> callFindBlockVertical(LevelAccessor level, BlockPos blockPos2, Direction.AxisDirection axisDirection, BiPredicate<BlockPos, BlockState> biPredicate, Predicate<BlockState> predicate, int i) {
        throw new IllegalStateException();
    }

    @Invoker
    static Optional<BlockPos> callFindRootBlock(Level level, BlockPos blockPos2, BlockState blockState, int i) {
        throw new IllegalStateException();
    }
}