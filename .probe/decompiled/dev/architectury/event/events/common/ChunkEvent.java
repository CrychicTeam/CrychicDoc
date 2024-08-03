package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

public interface ChunkEvent {

    Event<ChunkEvent.SaveData> SAVE_DATA = EventFactory.createLoop();

    Event<ChunkEvent.LoadData> LOAD_DATA = EventFactory.createLoop();

    public interface LoadData {

        void load(ChunkAccess var1, @Nullable ServerLevel var2, CompoundTag var3);
    }

    public interface SaveData {

        void save(ChunkAccess var1, ServerLevel var2, CompoundTag var3);
    }
}