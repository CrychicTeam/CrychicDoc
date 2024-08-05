package com.craisinlord.integrated_api.misc.maptrades;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.jetbrains.annotations.Nullable;

public class StructureSpecificMaps {

    public static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {

        private final int emeraldCost;

        private final ResourceKey<Structure> destination;

        private final TagKey<Structure> destinationTag;

        private final String displayName;

        private final MapDecoration.Type destinationType;

        private final int maxUses;

        private final int villagerXp;

        private final int spawnRegionSearchRadius;

        public TreasureMapForEmeralds(int emeraldCost, String csf, String displayName, MapDecoration.Type mapIcon, int maxUse, int xp, int spawnRegionSearchRadius) {
            this.emeraldCost = emeraldCost;
            if (csf.startsWith("#")) {
                this.destination = null;
                this.destinationTag = TagKey.create(Registries.STRUCTURE, new ResourceLocation(csf.replaceFirst("#", "")));
            } else {
                this.destination = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(csf));
                this.destinationTag = null;
            }
            this.displayName = displayName;
            this.destinationType = mapIcon;
            this.maxUses = maxUse;
            this.villagerXp = xp;
            this.spawnRegionSearchRadius = spawnRegionSearchRadius;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            return entity.level() instanceof ServerLevel serverlevel ? this.getOffer(serverlevel, entity) : null;
        }

        private HolderSet<Structure> getHolderSet(ServerLevel level) {
            Registry<Structure> registry = level.m_9598_().registryOrThrow(Registries.STRUCTURE);
            return HolderSet.direct(registry.getHolderOrThrow(this.destination));
        }

        private boolean mapValid(ServerLevel level) {
            HolderSet<Structure> holderSet;
            if (this.destinationTag == null) {
                holderSet = this.getHolderSet(level);
            } else {
                Optional<HolderSet.Named<Structure>> optionalHolders = level.m_9598_().registryOrThrow(Registries.STRUCTURE).getTag(this.destinationTag);
                if (optionalHolders.isEmpty()) {
                    return false;
                }
                holderSet = (HolderSet<Structure>) optionalHolders.get();
            }
            boolean isValidSpawning = true;
            for (Holder<Structure> structureHolder : holderSet) {
                if (level.getChunkSource().getGeneratorState().getPlacementsForStructure(structureHolder).isEmpty()) {
                    isValidSpawning = false;
                    break;
                }
                if (level.getChunkSource().getGenerator().getBiomeSource().possibleBiomes().stream().noneMatch(e -> structureHolder.value().biomes().contains(e))) {
                    isValidSpawning = false;
                    break;
                }
            }
            return isValidSpawning;
        }

        private MerchantOffer getOffer(ServerLevel level, Entity entity) {
            if (!this.mapValid(level)) {
                return null;
            } else if (this.destinationTag == null) {
                HolderSet<Structure> holderSet = this.getHolderSet(level);
                return MerchantMapUpdating.updateMapAsync(entity, this.emeraldCost, this.displayName, this.destinationType, this.maxUses, this.villagerXp, holderSet, this.spawnRegionSearchRadius);
            } else {
                return MerchantMapUpdating.updateMapAsync(entity, this.emeraldCost, this.displayName, this.destinationType, this.maxUses, this.villagerXp, this.destinationTag, this.spawnRegionSearchRadius);
            }
        }
    }
}