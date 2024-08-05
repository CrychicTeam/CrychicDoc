package org.violetmoon.quark.integration.terrablender;

import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.QuarkGeneralConfig;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;

public class TerrablenderUndergroundBiomeHandler extends AbstractUndergroundBiomeHandler {

    public TerrablenderUndergroundBiomeHandler() {
        Quark.LOG.info("Initializing TerraBlender underground biome compat");
        Quark.ZETA.loadBus.subscribe(this);
    }

    @LoadEvent
    public void commonSetup(ZCommonSetup event) {
        event.enqueueWork(() -> {
            if (!this.undergroundBiomeDescs.isEmpty() && QuarkGeneralConfig.terrablenderAddRegion) {
                Regions.register(new Region(Quark.asResource("biome_provider"), RegionType.OVERWORLD, QuarkGeneralConfig.terrablenderRegionWeight) {

                    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
                        this.addModifiedVanillaOverworldBiomes(consumer, noModifications -> {
                        });
                        if (!QuarkGeneralConfig.terrablenderModifyVanillaAnyway) {
                            TerrablenderUndergroundBiomeHandler.this.addUndergroundBiomesTo(consumer);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void modifyVanillaOverworldPreset(OverworldBiomeBuilder builder, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
        if (QuarkGeneralConfig.terrablenderModifyVanillaAnyway) {
            this.addUndergroundBiomesTo(consumer);
        }
    }
}