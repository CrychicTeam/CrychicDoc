package net.minecraftforge.common.world;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;

public class MobSpawnSettingsBuilder extends MobSpawnSettings.Builder {

    private final Set<MobCategory> typesView = Collections.unmodifiableSet(this.f_48362_.keySet());

    private final Set<EntityType<?>> costView = Collections.unmodifiableSet(this.f_48363_.keySet());

    public MobSpawnSettingsBuilder(MobSpawnSettings orig) {
        orig.getSpawnerTypes().forEach(k -> {
            ((List) this.f_48362_.get(k)).clear();
            ((List) this.f_48362_.get(k)).addAll(orig.getMobs(k).unwrap());
        });
        orig.getEntityTypes().forEach(k -> this.f_48363_.put(k, orig.getMobSpawnCost(k)));
        this.f_48364_ = orig.getCreatureProbability();
    }

    public Set<MobCategory> getSpawnerTypes() {
        return this.typesView;
    }

    public List<MobSpawnSettings.SpawnerData> getSpawner(MobCategory type) {
        return (List<MobSpawnSettings.SpawnerData>) this.f_48362_.get(type);
    }

    public Set<EntityType<?>> getEntityTypes() {
        return this.costView;
    }

    @Nullable
    public MobSpawnSettings.MobSpawnCost getCost(EntityType<?> type) {
        return (MobSpawnSettings.MobSpawnCost) this.f_48363_.get(type);
    }

    public float getProbability() {
        return this.f_48364_;
    }

    public MobSpawnSettingsBuilder disablePlayerSpawn() {
        return this;
    }
}