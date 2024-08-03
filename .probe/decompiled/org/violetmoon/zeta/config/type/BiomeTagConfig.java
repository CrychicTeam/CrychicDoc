package org.violetmoon.zeta.config.type;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.ConfigFlagManager;
import org.violetmoon.zeta.module.ZetaModule;

public class BiomeTagConfig implements IBiomeConfig, IConfigType {

    private final Object mutex = new Object();

    @Config(name = "Biome Tags")
    private List<String> biomeTagStrings;

    @Config
    private boolean isBlacklist;

    private List<TagKey<Biome>> tags;

    @SafeVarargs
    protected BiomeTagConfig(boolean isBlacklist, TagKey<Biome>... tagsIn) {
        this.isBlacklist = isBlacklist;
        this.biomeTagStrings = new LinkedList();
        for (TagKey<Biome> t : tagsIn) {
            this.biomeTagStrings.add(t.location().toString());
        }
    }

    private BiomeTagConfig(boolean isBlacklist, String... types) {
        this.isBlacklist = isBlacklist;
        this.biomeTagStrings = new LinkedList();
        this.biomeTagStrings.addAll(Arrays.asList(types));
    }

    protected static BiomeTagConfig fromStrings(boolean isBlacklist, String... types) {
        return new BiomeTagConfig(isBlacklist, types);
    }

    @Override
    public boolean canSpawn(Holder<Biome> biome) {
        if (biome == null) {
            return false;
        } else {
            synchronized (this.mutex) {
                if (this.tags == null) {
                    this.updateTypes();
                }
                for (TagKey<Biome> tag : this.tags) {
                    if (biome.is(tag)) {
                        return !this.isBlacklist;
                    }
                }
                return this.isBlacklist;
            }
        }
    }

    @Override
    public void onReload(ZetaModule module, ConfigFlagManager flagManager) {
        synchronized (this.mutex) {
            this.updateTypes();
        }
    }

    public void updateTypes() {
        this.tags = new LinkedList();
        for (String s : this.biomeTagStrings) {
            TagKey<Biome> tag = TagKey.create(Registries.BIOME, new ResourceLocation(s));
            if (tag != null) {
                this.tags.add(tag);
            }
        }
    }
}