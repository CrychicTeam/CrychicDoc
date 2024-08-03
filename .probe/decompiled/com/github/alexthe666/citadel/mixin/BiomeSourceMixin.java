package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ BiomeSource.class })
public class BiomeSourceMixin implements ExpandedBiomeSource {

    @Shadow
    public Supplier<Set<Holder<Biome>>> possibleBiomes;

    private boolean expanded;

    private Map<ResourceKey<Biome>, Holder<Biome>> map = new HashMap();

    @Override
    public void setResourceKeyMap(Map<ResourceKey<Biome>, Holder<Biome>> map) {
        this.map = map;
    }

    @Override
    public Map<ResourceKey<Biome>, Holder<Biome>> getResourceKeyMap() {
        return this.map;
    }

    @Override
    public void expandBiomesWith(Set<Holder<Biome>> newGenBiomes) {
        if (!this.expanded) {
            Builder<Holder<Biome>> builder = ImmutableSet.builder();
            builder.addAll((Iterable) this.possibleBiomes.get());
            builder.addAll(newGenBiomes);
            this.possibleBiomes = Suppliers.memoize(builder::build);
            this.expanded = true;
        }
    }
}