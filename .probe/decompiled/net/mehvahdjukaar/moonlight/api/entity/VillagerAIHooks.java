package net.mehvahdjukaar.moonlight.api.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.events.IVillagerBrainEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class VillagerAIHooks {

    public static void addBrainModification(Consumer<IVillagerBrainEvent> eventConsumer) {
        MoonlightEventsHelper.addListener(eventConsumer, IVillagerBrainEvent.class);
    }

    public static void registerMemory(MemoryModuleType<?> memoryModuleType) {
        try {
            Builder<MemoryModuleType<?>> builder = ImmutableList.builder();
            builder.addAll(Villager.MEMORY_TYPES);
            builder.add(memoryModuleType);
            Villager.MEMORY_TYPES = builder.build();
        } catch (Exception var2) {
            Moonlight.LOGGER.warn("failed to register memory module type for villagers: " + var2);
        }
    }
}