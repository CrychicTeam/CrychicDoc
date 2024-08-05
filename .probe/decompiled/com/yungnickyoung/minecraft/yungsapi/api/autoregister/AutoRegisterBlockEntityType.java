package com.yungnickyoung.minecraft.yungsapi.api.autoregister;

import com.mojang.datafixers.types.Type;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterEntry;
import com.yungnickyoung.minecraft.yungsapi.services.Services;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus.Internal;

public class AutoRegisterBlockEntityType<T extends BlockEntity> extends AutoRegisterEntry<BlockEntityType<T>> {

    public static <U extends BlockEntity> AutoRegisterBlockEntityType<U> of(Supplier<BlockEntityType<U>> blockSupplier) {
        return new AutoRegisterBlockEntityType<>(blockSupplier);
    }

    private AutoRegisterBlockEntityType(Supplier<BlockEntityType<T>> blockSupplier) {
        super(blockSupplier);
    }

    public static class Builder<T extends BlockEntity> {

        private final AutoRegisterBlockEntityType.Builder.BlockEntitySupplier<? extends T> factory;

        private final Block[] blocks;

        private Builder(AutoRegisterBlockEntityType.Builder.BlockEntitySupplier<? extends T> factory, Block[] blocks) {
            this.factory = factory;
            this.blocks = blocks;
        }

        public static <T extends BlockEntity> AutoRegisterBlockEntityType.Builder<T> of(AutoRegisterBlockEntityType.Builder.BlockEntitySupplier<? extends T> factory, Block... blocks) {
            return new AutoRegisterBlockEntityType.Builder<>(factory, blocks);
        }

        public BlockEntityType<T> build() {
            return this.build(null);
        }

        public BlockEntityType<T> build(Type<?> type) {
            return Services.BLOCK_ENTITY_TYPE_HELPER.build(this, type);
        }

        @Internal
        public AutoRegisterBlockEntityType.Builder.BlockEntitySupplier<? extends T> getFactory() {
            return this.factory;
        }

        @Internal
        public Block[] getBlocks() {
            return this.blocks;
        }

        @FunctionalInterface
        public interface BlockEntitySupplier<T extends BlockEntity> {

            T create(BlockPos var1, BlockState var2);
        }
    }
}