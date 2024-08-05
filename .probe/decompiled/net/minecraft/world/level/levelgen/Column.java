package net.minecraft.world.level.levelgen;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;

public abstract class Column {

    public static Column.Range around(int int0, int int1) {
        return new Column.Range(int0 - 1, int1 + 1);
    }

    public static Column.Range inside(int int0, int int1) {
        return new Column.Range(int0, int1);
    }

    public static Column below(int int0) {
        return new Column.Ray(int0, false);
    }

    public static Column fromHighest(int int0) {
        return new Column.Ray(int0 + 1, false);
    }

    public static Column above(int int0) {
        return new Column.Ray(int0, true);
    }

    public static Column fromLowest(int int0) {
        return new Column.Ray(int0 - 1, true);
    }

    public static Column line() {
        return Column.Line.INSTANCE;
    }

    public static Column create(OptionalInt optionalInt0, OptionalInt optionalInt1) {
        if (optionalInt0.isPresent() && optionalInt1.isPresent()) {
            return inside(optionalInt0.getAsInt(), optionalInt1.getAsInt());
        } else if (optionalInt0.isPresent()) {
            return above(optionalInt0.getAsInt());
        } else {
            return optionalInt1.isPresent() ? below(optionalInt1.getAsInt()) : line();
        }
    }

    public abstract OptionalInt getCeiling();

    public abstract OptionalInt getFloor();

    public abstract OptionalInt getHeight();

    public Column withFloor(OptionalInt optionalInt0) {
        return create(optionalInt0, this.getCeiling());
    }

    public Column withCeiling(OptionalInt optionalInt0) {
        return create(this.getFloor(), optionalInt0);
    }

    public static Optional<Column> scan(LevelSimulatedReader levelSimulatedReader0, BlockPos blockPos1, int int2, Predicate<BlockState> predicateBlockState3, Predicate<BlockState> predicateBlockState4) {
        BlockPos.MutableBlockPos $$5 = blockPos1.mutable();
        if (!levelSimulatedReader0.isStateAtPosition(blockPos1, predicateBlockState3)) {
            return Optional.empty();
        } else {
            int $$6 = blockPos1.m_123342_();
            OptionalInt $$7 = scanDirection(levelSimulatedReader0, int2, predicateBlockState3, predicateBlockState4, $$5, $$6, Direction.UP);
            OptionalInt $$8 = scanDirection(levelSimulatedReader0, int2, predicateBlockState3, predicateBlockState4, $$5, $$6, Direction.DOWN);
            return Optional.of(create($$8, $$7));
        }
    }

    private static OptionalInt scanDirection(LevelSimulatedReader levelSimulatedReader0, int int1, Predicate<BlockState> predicateBlockState2, Predicate<BlockState> predicateBlockState3, BlockPos.MutableBlockPos blockPosMutableBlockPos4, int int5, Direction direction6) {
        blockPosMutableBlockPos4.setY(int5);
        for (int $$7 = 1; $$7 < int1 && levelSimulatedReader0.isStateAtPosition(blockPosMutableBlockPos4, predicateBlockState2); $$7++) {
            blockPosMutableBlockPos4.move(direction6);
        }
        return levelSimulatedReader0.isStateAtPosition(blockPosMutableBlockPos4, predicateBlockState3) ? OptionalInt.of(blockPosMutableBlockPos4.m_123342_()) : OptionalInt.empty();
    }

    public static final class Line extends Column {

        static final Column.Line INSTANCE = new Column.Line();

        private Line() {
        }

        @Override
        public OptionalInt getCeiling() {
            return OptionalInt.empty();
        }

        @Override
        public OptionalInt getFloor() {
            return OptionalInt.empty();
        }

        @Override
        public OptionalInt getHeight() {
            return OptionalInt.empty();
        }

        public String toString() {
            return "C(-)";
        }
    }

    public static final class Range extends Column {

        private final int floor;

        private final int ceiling;

        protected Range(int int0, int int1) {
            this.floor = int0;
            this.ceiling = int1;
            if (this.height() < 0) {
                throw new IllegalArgumentException("Column of negative height: " + this);
            }
        }

        @Override
        public OptionalInt getCeiling() {
            return OptionalInt.of(this.ceiling);
        }

        @Override
        public OptionalInt getFloor() {
            return OptionalInt.of(this.floor);
        }

        @Override
        public OptionalInt getHeight() {
            return OptionalInt.of(this.height());
        }

        public int ceiling() {
            return this.ceiling;
        }

        public int floor() {
            return this.floor;
        }

        public int height() {
            return this.ceiling - this.floor - 1;
        }

        public String toString() {
            return "C(" + this.ceiling + "-" + this.floor + ")";
        }
    }

    public static final class Ray extends Column {

        private final int edge;

        private final boolean pointingUp;

        public Ray(int int0, boolean boolean1) {
            this.edge = int0;
            this.pointingUp = boolean1;
        }

        @Override
        public OptionalInt getCeiling() {
            return this.pointingUp ? OptionalInt.empty() : OptionalInt.of(this.edge);
        }

        @Override
        public OptionalInt getFloor() {
            return this.pointingUp ? OptionalInt.of(this.edge) : OptionalInt.empty();
        }

        @Override
        public OptionalInt getHeight() {
            return OptionalInt.empty();
        }

        public String toString() {
            return this.pointingUp ? "C(" + this.edge + "-)" : "C(-" + this.edge + ")";
        }
    }
}