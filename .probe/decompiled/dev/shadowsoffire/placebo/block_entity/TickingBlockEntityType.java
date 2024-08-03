package dev.shadowsoffire.placebo.block_entity;

import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class TickingBlockEntityType<T extends BlockEntity & TickingBlockEntity> extends BlockEntityType<T> {

    protected final boolean clientTick;

    protected final boolean serverTick;

    public TickingBlockEntityType(BlockEntityType.BlockEntitySupplier<? extends T> pFactory, Set<Block> pValidBlocks, boolean clientTick, boolean serverTick) {
        super(pFactory, pValidBlocks, null);
        this.clientTick = clientTick;
        this.serverTick = serverTick;
    }

    @Nullable
    public BlockEntityTicker<T> getTicker(boolean client) {
        if (client && this.clientTick) {
            return (level, pos, state, entity) -> ((TickingBlockEntity) entity).clientTick(level, pos, state);
        } else {
            return !client && this.serverTick ? (level, pos, state, entity) -> ((TickingBlockEntity) entity).serverTick(level, pos, state) : null;
        }
    }
}