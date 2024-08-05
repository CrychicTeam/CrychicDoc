package dev.latvian.mods.kubejs.level.gen;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.level.gen.filter.biome.BiomeFilter;
import dev.latvian.mods.kubejs.level.gen.properties.AddLakeProperties;
import dev.latvian.mods.kubejs.level.gen.properties.AddOreProperties;
import dev.latvian.mods.kubejs.level.gen.properties.AddSpawnProperties;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ClassWrapper;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class AddWorldgenEventJS extends StartupEventJS {

    private static final Pattern SPAWN_PATTERN = Pattern.compile("(\\w+:\\w+)\\*\\((\\d+)-(\\d+)\\):(\\d+)");

    public final ClassWrapper<VerticalAnchor> anchors = new ClassWrapper<>(VerticalAnchor.class);

    private void addFeature(ResourceLocation id, BiomeFilter filter, GenerationStep.Decoration step, ConfiguredFeature<?, ?> feature, List<PlacementModifier> modifiers) {
        if (id == null) {
            new ResourceLocation("kubejs:features/" + UtilsJS.getUniqueId(feature, ConfiguredFeature.DIRECT_CODEC));
        }
        ConsoleJS.STARTUP.error("WorldgenEvents.add() is currently not supported in 1.20 yet! This will be fixed soon, but for now we recommend you comment out worldgen code. Sorry for inconvenience!");
    }

    private void addFeature(ResourceLocation id, BiomeFilter filter, GenerationStep.Decoration step, PlacedFeature feature) {
        if (id == null) {
            new ResourceLocation("kubejs:features/" + UtilsJS.getUniqueId(feature, PlacedFeature.DIRECT_CODEC));
        }
    }

    private void addEntitySpawn(BiomeFilter filter, MobCategory category, MobSpawnSettings.SpawnerData spawnerData) {
        BiomeModifications.postProcessProperties(filter, (ctx, props) -> props.getSpawnProperties().addSpawn(category, spawnerData));
    }

    public void addOre(Consumer<AddOreProperties> p) {
        ConsoleJS.STARTUP.error("WorldgenEvents.add() is currently not supported in 1.20 yet! This will be fixed soon, but for now we recommend you comment out worldgen code. Sorry for inconvenience!");
    }

    public void addLake(Consumer<AddLakeProperties> p) {
        ConsoleJS.STARTUP.error("WorldgenEvents.add() is currently not supported in 1.20 yet! This will be fixed soon, but for now we recommend you comment out worldgen code. Sorry for inconvenience!");
    }

    public void addSpawn(Consumer<AddSpawnProperties> p) {
        AddSpawnProperties properties = new AddSpawnProperties();
        p.accept(properties);
        if (properties._entity != null && properties._category != null) {
            this.addEntitySpawn(properties.biomes, properties._category, new MobSpawnSettings.SpawnerData(properties._entity, properties.weight, properties.minCount, properties.maxCount));
        }
    }

    public void addSpawn(BiomeFilter filter, MobCategory category, String spawn) {
        Matcher matcher = SPAWN_PATTERN.matcher(spawn);
        if (matcher.matches()) {
            try {
                EntityType entity = (EntityType) Objects.requireNonNull(RegistryInfo.ENTITY_TYPE.getValue(new ResourceLocation(matcher.group(1))));
                int weight = Integer.parseInt(matcher.group(4));
                int min = Integer.parseInt(matcher.group(2));
                int max = Integer.parseInt(matcher.group(3));
                this.addEntitySpawn(filter, category, new MobSpawnSettings.SpawnerData(entity, weight, min, max));
            } catch (Exception var9) {
                ConsoleJS.STARTUP.info("Failed to add spawn: " + var9);
            }
        } else {
            ConsoleJS.STARTUP.info("Invalid spawn syntax! Must be mod:entity_type*(minCount-maxCount):weight");
        }
    }

    public void addSpawn(MobCategory category, String spawn) {
        this.addSpawn(BiomeFilter.ALWAYS_TRUE, category, spawn);
    }
}