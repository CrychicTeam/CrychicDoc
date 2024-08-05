package net.liopyu.entityjs.util.implementation;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.events.BiomeSpawnsEventJS;
import net.liopyu.entityjs.util.EventHandlers;
import net.liopyu.entityjs.util.RegistryUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class EventBasedSpawnModifier implements BiomeModifier {

    private final BiomeSpawnsEventJS event;

    public EventBasedSpawnModifier() {
        if (EventHandlers.biomeSpawns.hasListeners()) {
            this.event = new BiomeSpawnsEventJS();
            EventHandlers.biomeSpawns.post(this.event);
        } else {
            this.event = null;
        }
    }

    @Override
    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        MobSpawnSettingsBuilder spawnsBuilder = builder.getMobSpawnSettings();
        if (phase == BiomeModifier.Phase.ADD) {
            for (EventBasedSpawnModifier.BiomeSpawn biomeSpawn : BaseLivingEntityBuilder.biomeSpawnList) {
                MobSpawnSettings.SpawnerData spawnerData = (MobSpawnSettings.SpawnerData) biomeSpawn.spawnerData().get();
                for (Either<ResourceLocation, TagKey<Biome>> either : biomeSpawn.biomes()) {
                    either.map(rl -> {
                        if (biome.is(rl)) {
                            spawnsBuilder.m_48376_(spawnerData.type.getCategory(), spawnerData);
                        }
                        return rl;
                    }, tag -> {
                        if (biome.is(tag)) {
                            spawnsBuilder.m_48376_(spawnerData.type.getCategory(), spawnerData);
                        }
                        return tag;
                    });
                }
            }
            if (this.event != null) {
                for (BiomeSpawnsEventJS.Addition addition : this.event.additions) {
                    for (Either<ResourceLocation, TagKey<Biome>> either : addition.biomes()) {
                        either.map(rl -> {
                            if (biome.is(rl)) {
                                spawnsBuilder.m_48376_(addition.entityType().getCategory(), addition.spawnData());
                            }
                            return rl;
                        }, tag -> {
                            if (biome.is(tag)) {
                                spawnsBuilder.m_48376_(addition.entityType().getCategory(), addition.spawnData());
                            }
                            return tag;
                        });
                    }
                }
            }
        }
        if (this.event != null && phase == BiomeModifier.Phase.REMOVE) {
            for (BiomeSpawnsEventJS.Removal removal : this.event.removals) {
                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                for (Either<ResourceLocation, TagKey<Biome>> either : removal.biomes()) {
                    either.map(rl -> {
                        if (biome.is(rl)) {
                            atomicBoolean.set(true);
                        }
                        return rl;
                    }, tag -> {
                        if (biome.is(tag)) {
                            atomicBoolean.set(true);
                        }
                        return tag;
                    });
                    if (atomicBoolean.get()) {
                        break;
                    }
                }
                if (atomicBoolean.get()) {
                    EntityType<?> type = removal.entityType();
                    spawnsBuilder.getSpawner(type.getCategory()).removeIf(spawnerData -> spawnerData.type == type);
                }
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return RegistryUtil.EVENT_SPAWN_MODIFIER.get();
    }

    public static record BiomeSpawn(List<Either<ResourceLocation, TagKey<Biome>>> biomes, Supplier<MobSpawnSettings.SpawnerData> spawnerData) {
    }
}