package com.simibubi.create.infrastructure.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class LayeredOreFeature extends Feature<LayeredOreConfiguration> {

    public LayeredOreFeature() {
        super(LayeredOreConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<LayeredOreConfiguration> pContext) {
        RandomSource random = pContext.random();
        BlockPos blockpos = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        LayeredOreConfiguration config = pContext.config();
        List<LayerPattern> patternPool = config.layerPatterns;
        if (patternPool.isEmpty()) {
            return false;
        } else {
            LayerPattern layerPattern = (LayerPattern) patternPool.get(random.nextInt(patternPool.size()));
            int placedAmount = 0;
            int size = config.size;
            int radius = Mth.ceil((float) config.size / 2.0F);
            int x0 = blockpos.m_123341_() - radius;
            int y0 = blockpos.m_123342_() - radius;
            int z0 = blockpos.m_123343_() - radius;
            int width = size + 1;
            int length = size + 1;
            int height = size + 1;
            if (blockpos.m_123342_() >= worldgenlevel.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, blockpos.m_123341_(), blockpos.m_123343_())) {
                return false;
            } else {
                List<LayerPattern.Layer> resolvedLayers = new ArrayList();
                List<Float> layerDiameterOffsets = new ArrayList();
                BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
                int layerCoordinate = random.nextInt(4);
                int slantyCoordinate = random.nextInt(3);
                float slope = random.nextFloat() * 0.75F;
                try (BulkSectionAccess bulksectionaccess = new BulkSectionAccess(worldgenlevel)) {
                    for (int x = 0; x < width; x++) {
                        float dx = (float) x * 2.0F / (float) width - 1.0F;
                        if (!(dx * dx > 1.0F)) {
                            for (int y = 0; y < height; y++) {
                                float dy = (float) y * 2.0F / (float) height - 1.0F;
                                if (!(dx * dx + dy * dy > 1.0F) && !worldgenlevel.m_151562_(y0 + y)) {
                                    for (int z = 0; z < length; z++) {
                                        float dz = (float) z * 2.0F / (float) height - 1.0F;
                                        int layerIndex = layerCoordinate == 0 ? z : (layerCoordinate == 1 ? x : y);
                                        if (slantyCoordinate != layerCoordinate) {
                                            layerIndex = (int) ((float) layerIndex + (float) Mth.floor(slantyCoordinate == 0 ? (float) z : (slantyCoordinate == 1 ? (float) x : (float) y)) * slope);
                                        }
                                        while (layerIndex >= resolvedLayers.size()) {
                                            LayerPattern.Layer next = layerPattern.rollNext(resolvedLayers.isEmpty() ? null : (LayerPattern.Layer) resolvedLayers.get(resolvedLayers.size() - 1), random);
                                            float offset = random.nextFloat() * 0.5F + 0.5F;
                                            for (int i = 0; i < next.minSize + random.nextInt(1 + next.maxSize - next.minSize); i++) {
                                                resolvedLayers.add(next);
                                                layerDiameterOffsets.add(offset);
                                            }
                                        }
                                        if (!(dx * dx + dy * dy + dz * dz > 1.0F * (Float) layerDiameterOffsets.get(layerIndex))) {
                                            LayerPattern.Layer layer = (LayerPattern.Layer) resolvedLayers.get(layerIndex);
                                            List<OreConfiguration.TargetBlockState> state = layer.rollBlock(random);
                                            int currentX = x0 + x;
                                            int currentY = y0 + y;
                                            int currentZ = z0 + z;
                                            mutablePos.set(currentX, currentY, currentZ);
                                            if (worldgenlevel.ensureCanWrite(mutablePos)) {
                                                LevelChunkSection levelchunksection = bulksectionaccess.getSection(mutablePos);
                                                if (levelchunksection != null) {
                                                    int i3 = SectionPos.sectionRelative(currentX);
                                                    int j3 = SectionPos.sectionRelative(currentY);
                                                    int k3 = SectionPos.sectionRelative(currentZ);
                                                    BlockState blockstate = levelchunksection.getBlockState(i3, j3, k3);
                                                    for (OreConfiguration.TargetBlockState oreconfiguration$targetblockstate : state) {
                                                        if (this.canPlaceOre(blockstate, bulksectionaccess::m_156110_, random, config, oreconfiguration$targetblockstate, mutablePos) && !oreconfiguration$targetblockstate.state.m_60795_()) {
                                                            levelchunksection.setBlockState(i3, j3, k3, oreconfiguration$targetblockstate.state, false);
                                                            placedAmount++;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return placedAmount > 0;
            }
        }
    }

    public boolean canPlaceOre(BlockState pState, Function<BlockPos, BlockState> pAdjacentStateAccessor, RandomSource pRandom, LayeredOreConfiguration pConfig, OreConfiguration.TargetBlockState pTargetState, BlockPos.MutableBlockPos pMatablePos) {
        if (!pTargetState.target.test(pState, pRandom)) {
            return false;
        } else {
            return this.shouldSkipAirCheck(pRandom, pConfig.discardChanceOnAirExposure) ? true : !m_159750_(pAdjacentStateAccessor, pMatablePos);
        }
    }

    protected boolean shouldSkipAirCheck(RandomSource pRandom, float pChance) {
        return pChance <= 0.0F ? true : (pChance >= 1.0F ? false : pRandom.nextFloat() >= pChance);
    }
}