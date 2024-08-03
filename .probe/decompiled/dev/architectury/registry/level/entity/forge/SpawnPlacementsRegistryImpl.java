package dev.architectury.registry.level.entity.forge;

import dev.architectury.platform.forge.EventBuses;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

public class SpawnPlacementsRegistryImpl {

    private static List<SpawnPlacementsRegistryImpl.Entry<?>> entries = new ArrayList();

    public static <T extends Mob> void register(Supplier<? extends EntityType<T>> type, SpawnPlacements.Type spawnPlacement, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        if (entries != null) {
            entries.add(new SpawnPlacementsRegistryImpl.Entry<>(type, spawnPlacement, heightmapType, spawnPredicate));
        } else {
            throw new IllegalStateException("SpawnPlacementsRegistry.register must not be called after the registry has been collected!");
        }
    }

    static {
        EventBuses.onRegistered("architectury", bus -> bus.addListener(event -> {
            for (SpawnPlacementsRegistryImpl.Entry<?> entry : entries) {
                event.register((EntityType<?>) entry.type().get(), entry.spawnPlacement(), entry.heightmapType(), entry.spawnPredicate(), SpawnPlacementRegisterEvent.Operation.OR);
            }
            entries = null;
        }));
    }

    private static record Entry<T extends Mob>(Supplier<? extends EntityType<T>> type, SpawnPlacements.Type spawnPlacement, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
    }
}