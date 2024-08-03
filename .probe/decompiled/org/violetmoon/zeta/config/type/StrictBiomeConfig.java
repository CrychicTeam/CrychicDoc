package org.violetmoon.zeta.config.type;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import org.violetmoon.zeta.config.Config;

public class StrictBiomeConfig implements IBiomeConfig, IConfigType {

    @Config(name = "Biomes")
    private List<String> biomeStrings;

    @Config
    private boolean isBlacklist;

    protected StrictBiomeConfig(boolean isBlacklist, String... biomes) {
        this.isBlacklist = isBlacklist;
        this.biomeStrings = new LinkedList();
        this.biomeStrings.addAll(Arrays.asList(biomes));
    }

    @Override
    public boolean canSpawn(Holder<Biome> res) {
        return (Boolean) res.unwrap().map(key -> this.biomeStrings.contains(key.location().toString()) != this.isBlacklist, unbound -> false);
    }
}