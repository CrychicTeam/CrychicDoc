package dev.xkmc.l2hostility.content.config;

import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

@SerialClass
public class WorldDifficultyConfig extends BaseConfig {

    @ConfigCollect(CollectType.MAP_OVERWRITE)
    @SerialField
    public final HashMap<ResourceLocation, WorldDifficultyConfig.DifficultyConfig> levelMap = new HashMap();

    @ConfigCollect(CollectType.MAP_OVERWRITE)
    @SerialField
    public final HashMap<ResourceLocation, WorldDifficultyConfig.DifficultyConfig> biomeMap = new HashMap();

    public static WorldDifficultyConfig.DifficultyConfig defaultLevel() {
        int base = LHConfig.COMMON.defaultLevelBase.get();
        double var = LHConfig.COMMON.defaultLevelVar.get();
        double scale = LHConfig.COMMON.defaultLevelScale.get();
        return new WorldDifficultyConfig.DifficultyConfig(0, base, var, scale, 1.0, 1.0);
    }

    public WorldDifficultyConfig putDim(ResourceKey<Level> key, int min, int base, double var, double scale) {
        this.levelMap.put(key.location(), new WorldDifficultyConfig.DifficultyConfig(min, base, var, scale, 1.0, 1.0));
        return this;
    }

    @SafeVarargs
    public final WorldDifficultyConfig putBiome(int min, int base, double var, double scale, ResourceKey<Biome>... keys) {
        for (ResourceKey<Biome> key : keys) {
            this.biomeMap.put(key.location(), new WorldDifficultyConfig.DifficultyConfig(min, base, var, scale, 1.0, 1.0));
        }
        return this;
    }

    public static record DifficultyConfig(int min, int base, double variation, double scale, double apply_chance, double trait_chance) {
    }
}