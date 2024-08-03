package net.minecraft.data.worldgen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class Pools {

    public static final ResourceKey<StructureTemplatePool> EMPTY = createKey("empty");

    public static ResourceKey<StructureTemplatePool> createKey(String string0) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(string0));
    }

    public static void register(BootstapContext<StructureTemplatePool> bootstapContextStructureTemplatePool0, String string1, StructureTemplatePool structureTemplatePool2) {
        bootstapContextStructureTemplatePool0.register(createKey(string1), structureTemplatePool2);
    }

    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContextStructureTemplatePool0) {
        HolderGetter<StructureTemplatePool> $$1 = bootstapContextStructureTemplatePool0.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> $$2 = $$1.getOrThrow(EMPTY);
        bootstapContextStructureTemplatePool0.register(EMPTY, new StructureTemplatePool($$2, ImmutableList.of(), StructureTemplatePool.Projection.RIGID));
        BastionPieces.bootstrap(bootstapContextStructureTemplatePool0);
        PillagerOutpostPools.bootstrap(bootstapContextStructureTemplatePool0);
        VillagePools.bootstrap(bootstapContextStructureTemplatePool0);
        AncientCityStructurePieces.bootstrap(bootstapContextStructureTemplatePool0);
        TrailRuinsStructurePools.bootstrap(bootstapContextStructureTemplatePool0);
    }
}