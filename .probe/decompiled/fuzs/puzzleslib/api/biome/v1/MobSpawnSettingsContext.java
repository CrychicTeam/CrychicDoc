package fuzs.puzzleslib.api.biome.v1;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;

public interface MobSpawnSettingsContext {

    void addSpawn(MobCategory var1, MobSpawnSettings.SpawnerData var2);

    boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> var1);

    default boolean removeSpawnsOfEntityType(EntityType<?> entityType) {
        return this.removeSpawns((spawnGroup, spawnEntry) -> spawnEntry.type == entityType);
    }

    default void clearSpawns(MobCategory group) {
        this.removeSpawns((spawnGroup, spawnEntry) -> spawnGroup == group);
    }

    default void clearSpawns() {
        this.removeSpawns((spawnGroup, spawnEntry) -> true);
    }

    void setSpawnCost(EntityType<?> var1, double var2, double var4);

    boolean clearSpawnCost(EntityType<?> var1);

    Set<MobCategory> getMobCategoriesWithSpawns();

    List<MobSpawnSettings.SpawnerData> getSpawnerData(MobCategory var1);

    Set<EntityType<?>> getEntityTypesWithSpawnCost();

    @Nullable
    MobSpawnSettings.MobSpawnCost getSpawnCost(EntityType<?> var1);

    float getCreatureGenerationProbability();

    void setCreatureGenerationProbability(float var1);
}