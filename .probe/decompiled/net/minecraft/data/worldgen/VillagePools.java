package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class VillagePools {

    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContextStructureTemplatePool0) {
        PlainVillagePools.bootstrap(bootstapContextStructureTemplatePool0);
        SnowyVillagePools.bootstrap(bootstapContextStructureTemplatePool0);
        SavannaVillagePools.bootstrap(bootstapContextStructureTemplatePool0);
        DesertVillagePools.bootstrap(bootstapContextStructureTemplatePool0);
        TaigaVillagePools.bootstrap(bootstapContextStructureTemplatePool0);
    }
}