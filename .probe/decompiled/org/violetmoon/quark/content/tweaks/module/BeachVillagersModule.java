package org.violetmoon.quark.content.tweaks.module;

import java.util.HashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class BeachVillagersModule extends ZetaModule {

    public static VillagerType beach = new VillagerType("beach");

    @LoadEvent
    public final void register(ZRegister event) {
        event.getRegistry().register(beach, "beach", Registries.VILLAGER_TYPE);
    }

    @LoadEvent
    public final void onCommonSetup(ZCommonSetup event) {
        if (this.enabled) {
            HashMap<ResourceKey<Biome>, VillagerType> map = new HashMap(VillagerType.BY_BIOME);
            map.put(Biomes.BEACH, beach);
            VillagerType.BY_BIOME = map;
        }
    }
}