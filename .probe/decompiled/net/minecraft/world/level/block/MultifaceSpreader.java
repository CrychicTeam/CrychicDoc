package net.minecraft.world.level.block;

import com.google.common.annotations.VisibleForTesting;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class MultifaceSpreader {

    public static final MultifaceSpreader.SpreadType[] DEFAULT_SPREAD_ORDER = new MultifaceSpreader.SpreadType[] { MultifaceSpreader.SpreadType.SAME_POSITION, MultifaceSpreader.SpreadType.SAME_PLANE, MultifaceSpreader.SpreadType.WRAP_AROUND };

    private final MultifaceSpreader.SpreadConfig config;

    public MultifaceSpreader(MultifaceBlock multifaceBlock0) {
        this(new MultifaceSpreader.DefaultSpreaderConfig(multifaceBlock0));
    }

    public MultifaceSpreader(MultifaceSpreader.SpreadConfig multifaceSpreaderSpreadConfig0) {
        this.config = multifaceSpreaderSpreadConfig0;
    }

    public boolean canSpreadInAnyDirection(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return Direction.stream().anyMatch(p_221611_ -> this.getSpreadFromFaceTowardDirection(blockState0, blockGetter1, blockPos2, direction3, p_221611_, this.config::m_213973_).isPresent());
    }

    public Optional<MultifaceSpreader.SpreadPos> spreadFromRandomFaceTowardRandomDirection(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, RandomSource randomSource3) {
        return (Optional<MultifaceSpreader.SpreadPos>) Direction.allShuffled(randomSource3).stream().filter(p_221680_ -> this.config.canSpreadFrom(blockState0, p_221680_)).map(p_221629_ -> this.spreadFromFaceTowardRandomDirection(blockState0, levelAccessor1, blockPos2, p_221629_, randomSource3, false)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    public long spreadAll(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, boolean boolean3) {
        return (Long) Direction.stream().filter(p_221670_ -> this.config.canSpreadFrom(blockState0, p_221670_)).map(p_221667_ -> this.spreadFromFaceTowardAllDirections(blockState0, levelAccessor1, blockPos2, p_221667_, boolean3)).reduce(0L, Long::sum);
    }

    public Optional<MultifaceSpreader.SpreadPos> spreadFromFaceTowardRandomDirection(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, Direction direction3, RandomSource randomSource4, boolean boolean5) {
        return (Optional<MultifaceSpreader.SpreadPos>) Direction.allShuffled(randomSource4).stream().map(p_221677_ -> this.spreadFromFaceTowardDirection(blockState0, levelAccessor1, blockPos2, direction3, p_221677_, boolean5)).filter(Optional::isPresent).findFirst().orElse(Optional.empty());
    }

    private long spreadFromFaceTowardAllDirections(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, Direction direction3, boolean boolean4) {
        return Direction.stream().map(p_221656_ -> this.spreadFromFaceTowardDirection(blockState0, levelAccessor1, blockPos2, direction3, p_221656_, boolean4)).filter(Optional::isPresent).count();
    }

    @VisibleForTesting
    public Optional<MultifaceSpreader.SpreadPos> spreadFromFaceTowardDirection(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, Direction direction3, Direction direction4, boolean boolean5) {
        return this.getSpreadFromFaceTowardDirection(blockState0, levelAccessor1, blockPos2, direction3, direction4, this.config::m_213973_).flatMap(p_221600_ -> this.spreadToFace(levelAccessor1, p_221600_, boolean5));
    }

    public Optional<MultifaceSpreader.SpreadPos> getSpreadFromFaceTowardDirection(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3, Direction direction4, MultifaceSpreader.SpreadPredicate multifaceSpreaderSpreadPredicate5) {
        if (direction4.getAxis() == direction3.getAxis()) {
            return Optional.empty();
        } else if (this.config.isOtherBlockValidAsSource(blockState0) || this.config.hasFace(blockState0, direction3) && !this.config.hasFace(blockState0, direction4)) {
            for (MultifaceSpreader.SpreadType $$6 : this.config.getSpreadTypes()) {
                MultifaceSpreader.SpreadPos $$7 = $$6.getSpreadPos(blockPos2, direction4, direction3);
                if (multifaceSpreaderSpreadPredicate5.test(blockGetter1, blockPos2, $$7)) {
                    return Optional.of($$7);
                }
            }
            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    public Optional<MultifaceSpreader.SpreadPos> spreadToFace(LevelAccessor levelAccessor0, MultifaceSpreader.SpreadPos multifaceSpreaderSpreadPos1, boolean boolean2) {
        BlockState $$3 = levelAccessor0.m_8055_(multifaceSpreaderSpreadPos1.pos());
        return this.config.placeBlock(levelAccessor0, multifaceSpreaderSpreadPos1, $$3, boolean2) ? Optional.of(multifaceSpreaderSpreadPos1) : Optional.empty();
    }

    public static class DefaultSpreaderConfig implements MultifaceSpreader.SpreadConfig {

        protected MultifaceBlock block;

        public DefaultSpreaderConfig(MultifaceBlock multifaceBlock0) {
            this.block = multifaceBlock0;
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
            return this.block.getStateForPlacement(blockState0, blockGetter1, blockPos2, direction3);
        }

        protected boolean stateCanBeReplaced(BlockGetter blockGetter0, BlockPos blockPos1, BlockPos blockPos2, Direction direction3, BlockState blockState4) {
            return blockState4.m_60795_() || blockState4.m_60713_(this.block) || blockState4.m_60713_(Blocks.WATER) && blockState4.m_60819_().isSource();
        }

        @Override
        public boolean canSpreadInto(BlockGetter blockGetter0, BlockPos blockPos1, MultifaceSpreader.SpreadPos multifaceSpreaderSpreadPos2) {
            BlockState $$3 = blockGetter0.getBlockState(multifaceSpreaderSpreadPos2.pos());
            return this.stateCanBeReplaced(blockGetter0, blockPos1, multifaceSpreaderSpreadPos2.pos(), multifaceSpreaderSpreadPos2.face(), $$3) && this.block.isValidStateForPlacement(blockGetter0, $$3, multifaceSpreaderSpreadPos2.pos(), multifaceSpreaderSpreadPos2.face());
        }
    }

    public interface SpreadConfig {

        @Nullable
        BlockState getStateForPlacement(BlockState var1, BlockGetter var2, BlockPos var3, Direction var4);

        boolean canSpreadInto(BlockGetter var1, BlockPos var2, MultifaceSpreader.SpreadPos var3);

        default MultifaceSpreader.SpreadType[] getSpreadTypes() {
            return MultifaceSpreader.DEFAULT_SPREAD_ORDER;
        }

        default boolean hasFace(BlockState blockState0, Direction direction1) {
            return MultifaceBlock.hasFace(blockState0, direction1);
        }

        default boolean isOtherBlockValidAsSource(BlockState blockState0) {
            return false;
        }

        default boolean canSpreadFrom(BlockState blockState0, Direction direction1) {
            return this.isOtherBlockValidAsSource(blockState0) || this.hasFace(blockState0, direction1);
        }

        default boolean placeBlock(LevelAccessor levelAccessor0, MultifaceSpreader.SpreadPos multifaceSpreaderSpreadPos1, BlockState blockState2, boolean boolean3) {
            BlockState $$4 = this.getStateForPlacement(blockState2, levelAccessor0, multifaceSpreaderSpreadPos1.pos(), multifaceSpreaderSpreadPos1.face());
            if ($$4 != null) {
                if (boolean3) {
                    levelAccessor0.m_46865_(multifaceSpreaderSpreadPos1.pos()).markPosForPostprocessing(multifaceSpreaderSpreadPos1.pos());
                }
                return levelAccessor0.m_7731_(multifaceSpreaderSpreadPos1.pos(), $$4, 2);
            } else {
                return false;
            }
        }
    }

    public static record SpreadPos(BlockPos f_221717_, Direction f_221718_) {

        private final BlockPos pos;

        private final Direction face;

        public SpreadPos(BlockPos f_221717_, Direction f_221718_) {
            this.pos = f_221717_;
            this.face = f_221718_;
        }
    }

    @FunctionalInterface
    public interface SpreadPredicate {

        boolean test(BlockGetter var1, BlockPos var2, MultifaceSpreader.SpreadPos var3);
    }

    public static enum SpreadType {

        SAME_POSITION {

            @Override
            public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos p_221751_, Direction p_221752_, Direction p_221753_) {
                return new MultifaceSpreader.SpreadPos(p_221751_, p_221752_);
            }
        }
        , SAME_PLANE {

            @Override
            public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos p_221758_, Direction p_221759_, Direction p_221760_) {
                return new MultifaceSpreader.SpreadPos(p_221758_.relative(p_221759_), p_221760_);
            }
        }
        , WRAP_AROUND {

            @Override
            public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos p_221765_, Direction p_221766_, Direction p_221767_) {
                return new MultifaceSpreader.SpreadPos(p_221765_.relative(p_221766_).relative(p_221767_), p_221766_.getOpposite());
            }
        }
        ;

        public abstract MultifaceSpreader.SpreadPos getSpreadPos(BlockPos var1, Direction var2, Direction var3);
    }
}