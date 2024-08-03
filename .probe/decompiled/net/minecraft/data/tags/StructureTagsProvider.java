package net.minecraft.data.tags;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureTagsProvider extends TagsProvider<Structure> {

    public StructureTagsProvider(PackOutput packOutput0, CompletableFuture<HolderLookup.Provider> completableFutureHolderLookupProvider1) {
        super(packOutput0, Registries.STRUCTURE, completableFutureHolderLookupProvider1);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206424_(StructureTags.VILLAGE).add(BuiltinStructures.VILLAGE_PLAINS).add(BuiltinStructures.VILLAGE_DESERT).add(BuiltinStructures.VILLAGE_SAVANNA).add(BuiltinStructures.VILLAGE_SNOWY).add(BuiltinStructures.VILLAGE_TAIGA);
        this.m_206424_(StructureTags.MINESHAFT).add(BuiltinStructures.MINESHAFT).add(BuiltinStructures.MINESHAFT_MESA);
        this.m_206424_(StructureTags.OCEAN_RUIN).add(BuiltinStructures.OCEAN_RUIN_COLD).add(BuiltinStructures.OCEAN_RUIN_WARM);
        this.m_206424_(StructureTags.SHIPWRECK).add(BuiltinStructures.SHIPWRECK).add(BuiltinStructures.SHIPWRECK_BEACHED);
        this.m_206424_(StructureTags.RUINED_PORTAL).add(BuiltinStructures.RUINED_PORTAL_DESERT).add(BuiltinStructures.RUINED_PORTAL_JUNGLE).add(BuiltinStructures.RUINED_PORTAL_MOUNTAIN).add(BuiltinStructures.RUINED_PORTAL_NETHER).add(BuiltinStructures.RUINED_PORTAL_OCEAN).add(BuiltinStructures.RUINED_PORTAL_STANDARD).add(BuiltinStructures.RUINED_PORTAL_SWAMP);
        this.m_206424_(StructureTags.CATS_SPAWN_IN).add(BuiltinStructures.SWAMP_HUT);
        this.m_206424_(StructureTags.CATS_SPAWN_AS_BLACK).add(BuiltinStructures.SWAMP_HUT);
        this.m_206424_(StructureTags.EYE_OF_ENDER_LOCATED).add(BuiltinStructures.STRONGHOLD);
        this.m_206424_(StructureTags.DOLPHIN_LOCATED).addTag(StructureTags.OCEAN_RUIN).addTag(StructureTags.SHIPWRECK);
        this.m_206424_(StructureTags.ON_WOODLAND_EXPLORER_MAPS).add(BuiltinStructures.WOODLAND_MANSION);
        this.m_206424_(StructureTags.ON_OCEAN_EXPLORER_MAPS).add(BuiltinStructures.OCEAN_MONUMENT);
        this.m_206424_(StructureTags.ON_TREASURE_MAPS).add(BuiltinStructures.BURIED_TREASURE);
    }
}