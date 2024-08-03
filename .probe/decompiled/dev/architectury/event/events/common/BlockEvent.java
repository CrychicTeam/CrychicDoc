package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.architectury.utils.value.IntValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockEvent {

    Event<BlockEvent.Break> BREAK = EventFactory.createEventResult();

    Event<BlockEvent.Place> PLACE = EventFactory.createEventResult();

    Event<BlockEvent.FallingLand> FALLING_LAND = EventFactory.createLoop();

    public interface Break {

        EventResult breakBlock(Level var1, BlockPos var2, BlockState var3, ServerPlayer var4, @Nullable IntValue var5);
    }

    public interface FallingLand {

        void onLand(Level var1, BlockPos var2, BlockState var3, BlockState var4, FallingBlockEntity var5);
    }

    public interface Place {

        EventResult placeBlock(Level var1, BlockPos var2, BlockState var3, @Nullable Entity var4);
    }
}