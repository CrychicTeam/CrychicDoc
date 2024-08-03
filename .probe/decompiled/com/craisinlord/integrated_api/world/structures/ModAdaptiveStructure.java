package com.craisinlord.integrated_api.world.structures;

import com.craisinlord.integrated_api.modinit.IAStructures;
import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.craisinlord.integrated_api.world.structures.pieces.manager.PieceLimitedJigsawManager;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptationType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ModAdaptiveStructure extends JigsawStructure {

    public static final Codec<ModAdaptiveStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size), Codec.INT.optionalFieldOf("min_y_allowed").forGetter(structure -> structure.minYAllowed), Codec.INT.optionalFieldOf("max_y_allowed").forGetter(structure -> structure.maxYAllowed), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.BOOL.fieldOf("cannot_spawn_in_liquid").orElse(false).forGetter(structure -> structure.cannotSpawnInLiquid), Codec.intRange(1, 100).optionalFieldOf("terrain_height_radius_check").forGetter(structure -> structure.terrainHeightCheckRadius), Codec.intRange(1, 1000).optionalFieldOf("allowed_terrain_height_range").forGetter(structure -> structure.allowedTerrainHeightRange), Codec.intRange(1, 100).optionalFieldOf("valid_biome_radius_check").forGetter(structure -> structure.biomeRadius), Codec.intRange(1, 512).optionalFieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter), Codec.BOOL.fieldOf("rotation_fixed").orElse(false).forGetter(structure -> structure.rotationFixed), EnhancedTerrainAdaptationType.ADAPTATION_CODEC.optionalFieldOf("enhanced_terrain_adaptation", EnhancedTerrainAdaptation.NONE).forGetter(structure -> structure.enhancedTerrainAdaptation), Codec.STRING.fieldOf("change_pool_mods").forGetter(structure -> structure.changePoolMod), StructureTemplatePool.CODEC.fieldOf("new_pool").forGetter(structure -> structure.newPool)).apply(instance, ModAdaptiveStructure::new));

    public final String changePoolMod;

    public final Holder<StructureTemplatePool> newPool;

    public ModAdaptiveStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, Optional<Integer> minYAllowed, Optional<Integer> maxYAllowed, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, boolean cannotSpawnInLiquid, Optional<Integer> terrainHeightCheckRadius, Optional<Integer> allowedTerrainHeightRange, Optional<Integer> biomeRadius, Optional<Integer> maxDistanceFromCenter, boolean rotationFixed, EnhancedTerrainAdaptation enhancedTerrainAdaptation, String changePoolMod, Holder<StructureTemplatePool> newPool) {
        super(config, startPool, size, minYAllowed, maxYAllowed, Optional.empty(), startHeight, projectStartToHeightmap, cannotSpawnInLiquid, terrainHeightCheckRadius, allowedTerrainHeightRange, biomeRadius, maxDistanceFromCenter, Optional.empty(), rotationFixed, enhancedTerrainAdaptation);
        this.changePoolMod = changePoolMod;
        this.newPool = newPool;
    }

    private ArrayList<String> convertModList(String modlist) {
        int startChar = 0;
        ArrayList<String> convertedModList = new ArrayList();
        for (int i = 0; i < modlist.length(); i++) {
            if (modlist.charAt(i) == ',') {
                convertedModList.add(modlist.substring(startChar, i));
                startChar = i + 1;
            }
        }
        convertedModList.add(modlist.substring(startChar));
        return convertedModList;
    }

    public boolean allModsPresent(ArrayList<String> convertedModList) {
        for (String mod : convertedModList) {
            if (!isLoaded(mod)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        int offsetY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), offsetY, context.chunkPos().getMinBlockZ());
        if (!this.extraSpawningChecks(context, blockpos)) {
            return Optional.empty();
        } else {
            Holder<StructureTemplatePool> finalPool = this.startPool;
            if (!this.changePoolMod.isEmpty()) {
                ArrayList<String> changePoolMods = this.convertModList(this.changePoolMod);
                if (this.allModsPresent(changePoolMods)) {
                    finalPool = this.newPool;
                }
            }
            int topClipOff = Integer.MAX_VALUE;
            int bottomClipOff = Integer.MIN_VALUE;
            if (this.allowedYRangeFromStart.isPresent()) {
                topClipOff = blockpos.m_123342_() + (Integer) this.allowedYRangeFromStart.get();
                bottomClipOff = blockpos.m_123342_() - (Integer) this.allowedYRangeFromStart.get();
            }
            if (this.maxYAllowed.isPresent()) {
                topClipOff = Math.min(topClipOff, (Integer) this.maxYAllowed.get());
            }
            if (this.minYAllowed.isPresent()) {
                bottomClipOff = Math.max(bottomClipOff, (Integer) this.minYAllowed.get());
            }
            String rotationString;
            if (this.rotationFixed) {
                rotationString = "NONE";
            } else {
                rotationString = "RANDOM";
            }
            int finalTopClipOff = topClipOff;
            int finalBottomClipOff = bottomClipOff;
            return PieceLimitedJigsawManager.assembleJigsawStructure(context, finalPool, this.size, context.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(this), blockpos, false, this.projectStartToHeightmap, topClipOff, bottomClipOff, null, this.maxDistanceFromCenter, rotationString, this.buryingType, (structurePiecesBuilder, pieces) -> this.postLayoutAdjustments(structurePiecesBuilder, context, offsetY, blockpos, finalTopClipOff, finalBottomClipOff, pieces));
        }
    }

    public static boolean isLoaded(String name) {
        return PlatformHooks.isModLoaded(name);
    }

    @Override
    public StructureType<?> type() {
        return IAStructures.MOD_ADAPTIVE_STRUCTURE.get();
    }
}