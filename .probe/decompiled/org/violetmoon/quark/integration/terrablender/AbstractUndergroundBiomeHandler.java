package org.violetmoon.quark.integration.terrablender;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.violetmoon.zeta.module.ZetaModule;

public abstract class AbstractUndergroundBiomeHandler {

    protected List<AbstractUndergroundBiomeHandler.UndergroundBiomeDesc> undergroundBiomeDescs = new ArrayList(2);

    public void registerUndergroundBiome(ZetaModule module, ResourceLocation id, Climate.ParameterPoint climate) {
        this.registerUndergroundBiome(new AbstractUndergroundBiomeHandler.UndergroundBiomeDesc(module, id, climate));
    }

    public void registerUndergroundBiome(AbstractUndergroundBiomeHandler.UndergroundBiomeDesc desc) {
        this.undergroundBiomeDescs.add(desc);
    }

    protected void addUndergroundBiomesTo(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        for (AbstractUndergroundBiomeHandler.UndergroundBiomeDesc desc : this.undergroundBiomeDescs) {
            if (desc.module().enabled) {
                consumer.accept(Pair.of(desc.climateParameterPoint(), desc.resourceKey()));
            }
        }
    }

    public abstract void modifyVanillaOverworldPreset(OverworldBiomeBuilder var1, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> var2);

    public static record UndergroundBiomeDesc(ZetaModule module, ResourceLocation id, Climate.ParameterPoint climateParameterPoint) {

        public ResourceKey<Biome> resourceKey() {
            return ResourceKey.create(Registries.BIOME, this.id);
        }
    }
}