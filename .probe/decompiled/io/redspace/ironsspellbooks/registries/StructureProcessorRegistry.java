package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.worldgen.RemoveWaterProcessor;
import io.redspace.ironsspellbooks.worldgen.WeatherCopperProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureProcessorRegistry {

    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, "irons_spellbooks");

    public static final RegistryObject<StructureProcessorType<RemoveWaterProcessor>> REMOVE_WATER = PROCESSORS.register("remove_water", () -> () -> RemoveWaterProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<WeatherCopperProcessor>> WEATHER_COPPER = PROCESSORS.register("weather_copper", () -> () -> WeatherCopperProcessor.CODEC);

    public static void register(IEventBus eventBus) {
        PROCESSORS.register(eventBus);
    }
}