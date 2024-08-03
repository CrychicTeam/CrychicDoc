package net.minecraft.data.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class BastionBridgePools {

    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContextStructureTemplatePool0) {
        HolderGetter<StructureProcessorList> $$1 = bootstapContextStructureTemplatePool0.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> $$2 = $$1.getOrThrow(ProcessorLists.ENTRANCE_REPLACEMENT);
        Holder<StructureProcessorList> $$3 = $$1.getOrThrow(ProcessorLists.BASTION_GENERIC_DEGRADATION);
        Holder<StructureProcessorList> $$4 = $$1.getOrThrow(ProcessorLists.BRIDGE);
        Holder<StructureProcessorList> $$5 = $$1.getOrThrow(ProcessorLists.RAMPART_DEGRADATION);
        HolderGetter<StructureTemplatePool> $$6 = bootstapContextStructureTemplatePool0.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> $$7 = $$6.getOrThrow(Pools.EMPTY);
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/starting_pieces", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance", $$2), 1), Pair.of(StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_face", $$3), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/bridge_pieces", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/bridge_pieces/bridge", $$4), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/legs", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/legs/leg_0", $$3), 1), Pair.of(StructurePoolElement.single("bastion/bridge/legs/leg_1", $$3), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/walls", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/walls/wall_base_0", $$5), 1), Pair.of(StructurePoolElement.single("bastion/bridge/walls/wall_base_1", $$5), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/ramparts", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/ramparts/rampart_0", $$5), 1), Pair.of(StructurePoolElement.single("bastion/bridge/ramparts/rampart_1", $$5), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/rampart_plates", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/rampart_plates/plate_0", $$5), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstapContextStructureTemplatePool0, "bastion/bridge/connectors", new StructureTemplatePool($$7, ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/bridge/connectors/back_bridge_top", $$3), 1), Pair.of(StructurePoolElement.single("bastion/bridge/connectors/back_bridge_bottom", $$3), 1)), StructureTemplatePool.Projection.RIGID));
    }
}