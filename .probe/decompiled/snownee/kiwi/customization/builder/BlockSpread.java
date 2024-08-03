package snownee.kiwi.customization.builder;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.placement.StatePropertiesPredicate;

public record BlockSpread(BlockSpread.Type type, Optional<StatePropertiesPredicate> statePropertiesPredicate, FacingLimitation facingLimitation, int maxDistance) {

    public List<BlockPos> collect(UseOnContext context, Predicate<Block> blockPredicate) {
        return this.collect(context.getLevel(), context.getClickedPos(), (Player) Objects.requireNonNull(context.getPlayer()), blockPredicate);
    }

    public List<BlockPos> collect(BlockGetter level, BlockPos origin, Player player, Predicate<Block> blockPredicate) {
        BlockState originalBlock = level.getBlockState(origin);
        Direction direction = player.m_6350_();
        Direction originalDirection = Direction.NORTH;
        try {
            String s = KBlockUtils.getValueString(originalBlock, "facing");
            originalDirection = Direction.valueOf(s.toUpperCase(Locale.ENGLISH));
        } catch (Exception var10) {
        }
        return switch(this.type) {
            case PLANE_XZ ->
                {
                    List<BlockPos> list = List.of();
                    if (this.facingLimitation.test(originalDirection, direction)) {
                        list = this.collectPlaneXZ(level, origin, originalBlock, direction);
                        if (list.size() > 1) {
                            yield list;
                        }
                    }
                    if (this.facingLimitation.test(originalDirection, direction.getClockWise())) {
                        List<BlockPos> list2 = this.collectPlaneXZ(level, origin, originalBlock, direction.getClockWise());
                        if (list2.size() > list.size()) {
                            yield list2;
                        }
                    }
                    yield list;
                }
            case PLANE_XYZ ->
                this.collectPlaneXYZ(level, origin, originalBlock, player);
        };
    }

    private List<BlockPos> collectPlaneXYZ(BlockGetter level, BlockPos origin, BlockState originalBlock, Player player) {
        throw new NotImplementedException();
    }

    private List<BlockPos> collectPlaneXZ(BlockGetter level, BlockPos origin, BlockState originalBlock, Direction direction) {
        List<BlockPos> list = Lists.newArrayList(new BlockPos[] { origin });
        BlockSpread.PosIterator iterator = new BlockSpread.PlacePosIterator(origin, this.maxDistance, direction);
        while (iterator.hasNext()) {
            BlockPos next = iterator.next();
            BlockState blockState = level.getBlockState(next);
            if (blockState.m_60713_(originalBlock.m_60734_()) && (!this.statePropertiesPredicate.isPresent() || ((StatePropertiesPredicate) this.statePropertiesPredicate.get()).smartTest(originalBlock, blockState))) {
                list.add(next);
                iterator.add(next, null);
            }
        }
        return list;
    }

    static class PlacePosIterator extends BlockSpread.PosIterator {

        final Direction direction;

        PlacePosIterator(BlockPos origin, int maxDistance, Direction direction) {
            super(origin, maxDistance);
            this.direction = direction;
        }

        @Override
        public Stream<BlockPos> listPossibleNext(BlockPos cur, @Nullable BlockPos from) {
            Builder<BlockPos> builder = Stream.builder();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        BlockPos next = cur.relative(this.direction, i).relative(Direction.UP, j);
                        if (!next.equals(from)) {
                            builder.accept(next);
                        }
                    }
                }
            }
            return builder.build();
        }
    }

    abstract static class PosIterator implements Iterator<BlockPos> {

        final LongSet visited = new LongAVLTreeSet();

        final Queue<BlockPos> queue = Lists.newLinkedList();

        final BlockPos origin;

        final int maxDistance;

        PosIterator(BlockPos origin, int maxDistance) {
            this.origin = origin;
            this.maxDistance = maxDistance;
        }

        public boolean hasNext() {
            if (this.visited.isEmpty()) {
                this.add(this.origin, null);
            }
            return !this.queue.isEmpty();
        }

        public BlockPos next() {
            return (BlockPos) this.queue.poll();
        }

        public void add(BlockPos cur, @Nullable BlockPos from) {
            if (this.origin.m_123333_(cur) <= this.maxDistance) {
                this.visited.add(cur.asLong());
                this.listPossibleNext(cur, from).filter(pos -> {
                    long l = pos.asLong();
                    if (this.visited.contains(l)) {
                        return false;
                    } else {
                        this.visited.add(l);
                        return true;
                    }
                }).forEach(this.queue::add);
            }
        }

        public abstract Stream<BlockPos> listPossibleNext(BlockPos var1, @Nullable BlockPos var2);
    }

    public static enum Type {

        PLANE_XZ, PLANE_XYZ
    }
}