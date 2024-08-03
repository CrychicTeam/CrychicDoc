package net.minecraft.world.inventory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ContainerLevelAccess {

    ContainerLevelAccess NULL = new ContainerLevelAccess() {

        @Override
        public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> p_39304_) {
            return Optional.empty();
        }
    };

    static ContainerLevelAccess create(final Level level0, final BlockPos blockPos1) {
        return new ContainerLevelAccess() {

            @Override
            public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> p_39311_) {
                return Optional.of(p_39311_.apply(level0, blockPos1));
            }
        };
    }

    <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> var1);

    default <T> T evaluate(BiFunction<Level, BlockPos, T> biFunctionLevelBlockPosT0, T t1) {
        return (T) this.evaluate(biFunctionLevelBlockPosT0).orElse(t1);
    }

    default void execute(BiConsumer<Level, BlockPos> biConsumerLevelBlockPos0) {
        this.evaluate((p_39296_, p_39297_) -> {
            biConsumerLevelBlockPos0.accept(p_39296_, p_39297_);
            return Optional.empty();
        });
    }
}