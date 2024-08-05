package org.violetmoon.zeta.world;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.config.type.EntitySpawnConfig;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.item.ZetaSpawnEggItem;
import org.violetmoon.zeta.module.ZetaModule;

public class EntitySpawnHandler {

    public List<EntitySpawnHandler.TrackedSpawnConfig> trackedSpawnConfigs = new LinkedList();

    private final Zeta zeta;

    public EntitySpawnHandler(Zeta zeta) {
        this.zeta = zeta;
    }

    public <T extends Mob> void registerSpawn(EntityType<T> entityType, MobCategory classification, SpawnPlacements.Type placementType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<T> placementPredicate, EntitySpawnConfig config) {
        SpawnPlacements.register(entityType, placementType, heightMapType, placementPredicate);
        this.track(entityType, classification, config, false);
    }

    public <T extends Mob> void track(EntityType<T> entityType, MobCategory classification, EntitySpawnConfig config, boolean secondary) {
        this.trackedSpawnConfigs.add(new EntitySpawnHandler.TrackedSpawnConfig(entityType, classification, config, secondary));
    }

    public void addEgg(ZetaModule module, EntityType<? extends Mob> entityType, int color1, int color2, EntitySpawnConfig config) {
        this.addEgg(entityType, color1, color2, module, config::isEnabled);
    }

    public void addEgg(EntityType<? extends Mob> entityType, int color1, int color2, ZetaModule module, BooleanSupplier enabledSupplier) {
        new ZetaSpawnEggItem(() -> entityType, color1, color2, this.zeta.registry.getRegistryName(entityType, BuiltInRegistries.ENTITY_TYPE) + "_spawn_egg", module, new Item.Properties()).setCondition(enabledSupplier);
    }

    @LoadEvent
    public void refresh(ZConfigChanged event) {
        for (EntitySpawnHandler.TrackedSpawnConfig c : this.trackedSpawnConfigs) {
            c.refresh();
        }
    }

    public static class TrackedSpawnConfig {

        public final EntityType<?> entityType;

        public final MobCategory classification;

        public final EntitySpawnConfig config;

        public final boolean secondary;

        MobSpawnSettings.SpawnerData entry;

        TrackedSpawnConfig(EntityType<?> entityType, MobCategory classification, EntitySpawnConfig config, boolean secondary) {
            this.entityType = entityType;
            this.classification = classification;
            this.config = config;
            this.secondary = secondary;
            this.refresh();
        }

        private void refresh() {
            this.entry = new MobSpawnSettings.SpawnerData(this.entityType, this.config.spawnWeight, Math.min(this.config.minGroupSize, this.config.maxGroupSize), Math.max(this.config.minGroupSize, this.config.maxGroupSize));
        }

        public MobSpawnSettings.SpawnerData getEntry() {
            return this.entry;
        }
    }
}