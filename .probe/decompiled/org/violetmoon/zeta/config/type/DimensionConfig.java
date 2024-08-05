package org.violetmoon.zeta.config.type;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.violetmoon.zeta.config.Config;

public class DimensionConfig implements IConfigType {

    @Config
    private boolean isBlacklist;

    @Config
    private List<String> dimensions;

    public DimensionConfig(boolean blacklist, String... dims) {
        this.isBlacklist = blacklist;
        this.dimensions = new LinkedList();
        Collections.addAll(this.dimensions, dims);
    }

    public static DimensionConfig overworld(boolean blacklist) {
        return new DimensionConfig(blacklist, "minecraft:overworld");
    }

    public static DimensionConfig nether(boolean blacklist) {
        return new DimensionConfig(blacklist, "minecraft:the_nether");
    }

    public static DimensionConfig end(boolean blacklist) {
        return new DimensionConfig(blacklist, "minecraft:the_end");
    }

    public static DimensionConfig all() {
        return new DimensionConfig(true);
    }

    public boolean canSpawnHere(LevelAccessor world) {
        return world instanceof Level level ? this.canSpawnHere(level.dimension().location()) : false;
    }

    public boolean canSpawnHere(ResourceLocation resloc) {
        return this.dimensions.contains(resloc.toString()) != this.isBlacklist;
    }
}