package net.mehvahdjukaar.amendments.forge;

import java.util.Optional;
import net.mehvahdjukaar.amendments.Amendments;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.MissingMappingsEvent;

@EventBusSubscriber(modid = "amendments", bus = Bus.FORGE)
public class RemapHandler {

    @SubscribeEvent
    public static void onRemapBlocks(MissingMappingsEvent event) {
        remapAll(event, BuiltInRegistries.BLOCK);
        remapAll(event, BuiltInRegistries.ITEM);
        remapAll(event, BuiltInRegistries.BLOCK_ENTITY_TYPE);
        remapAll(event, BuiltInRegistries.ENTITY_TYPE);
    }

    private static <T> void remapAll(MissingMappingsEvent event, Registry<T> registry) {
        for (String mod : Amendments.OLD_MODS) {
            for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(registry.key(), mod)) {
                ResourceLocation newLoc = Amendments.res(mapping.getKey().getPath());
                Optional<T> newBlock = registry.getOptional(newLoc);
                newBlock.ifPresent(mapping::remap);
            }
        }
    }
}