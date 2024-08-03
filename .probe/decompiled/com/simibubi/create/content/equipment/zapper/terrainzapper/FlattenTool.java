package com.simibubi.create.content.equipment.zapper.terrainzapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public class FlattenTool {

    static float[][] kernel = new float[][] { { 0.003765F, 0.015019F, 0.023792F, 0.015019F, 0.003765F }, { 0.015019F, 0.059912F, 0.094907F, 0.059912F, 0.015019F }, { 0.023792F, 0.094907F, 0.150342F, 0.094907F, 0.023792F }, { 0.015019F, 0.059912F, 0.094907F, 0.059912F, 0.015019F }, { 0.003765F, 0.015019F, 0.023792F, 0.015019F, 0.003765F } };

    private static int[][] applyKernel(int[][] values) {
        int[][] result = new int[values.length][values[0].length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                int value = values[i][j];
                float newValue = 0.0F;
                for (int iOffset = -2; iOffset <= 2; iOffset++) {
                    for (int jOffset = -2; jOffset <= 2; jOffset++) {
                        int iTarget = i + iOffset;
                        int jTarget = j + jOffset;
                        int ref = 0;
                        if (iTarget >= 0 && iTarget < values.length && jTarget >= 0 && jTarget < values[0].length) {
                            ref = values[iTarget][jTarget];
                        } else {
                            ref = value;
                        }
                        if (ref == Integer.MIN_VALUE) {
                            ref = value;
                        }
                        newValue += kernel[iOffset + 2][jOffset + 2] * (float) ref;
                    }
                }
                result[i][j] = Mth.floor(newValue + 0.5F);
            }
        }
        return result;
    }

    public static void apply(Level world, List<BlockPos> targetPositions, Direction facing) {
        List<BlockPos> surfaces = new ArrayList();
        Map<Pair<Integer, Integer>, Integer> heightMap = new HashMap();
        int offset = facing.getAxisDirection().getStep();
        int minEntry = Integer.MAX_VALUE;
        int minCoord1 = Integer.MAX_VALUE;
        int minCoord2 = Integer.MAX_VALUE;
        int maxEntry = Integer.MIN_VALUE;
        int maxCoord1 = Integer.MIN_VALUE;
        int maxCoord2 = Integer.MIN_VALUE;
        for (BlockPos p : targetPositions) {
            Pair<Integer, Integer> coords = getCoords(p, facing);
            BlockState belowSurface = world.getBlockState(p);
            minCoord1 = Math.min(minCoord1, (Integer) coords.getKey());
            minCoord2 = Math.min(minCoord2, (Integer) coords.getValue());
            maxCoord1 = Math.max(maxCoord1, (Integer) coords.getKey());
            maxCoord2 = Math.max(maxCoord2, (Integer) coords.getValue());
            if (TerrainTools.isReplaceable(belowSurface)) {
                if (!heightMap.containsKey(coords)) {
                    heightMap.put(coords, Integer.MIN_VALUE);
                }
            } else {
                p = p.relative(facing);
                BlockState surface = world.getBlockState(p);
                if (!TerrainTools.isReplaceable(surface)) {
                    if (!heightMap.containsKey(coords) || ((Integer) heightMap.get(coords)).equals(Integer.MIN_VALUE)) {
                        heightMap.put(coords, Integer.MAX_VALUE);
                    }
                } else {
                    surfaces.add(p);
                    int coordinate = facing.getAxis().choose(p.m_123341_(), p.m_123342_(), p.m_123343_());
                    if (!heightMap.containsKey(coords) || ((Integer) heightMap.get(coords)).equals(Integer.MAX_VALUE) || ((Integer) heightMap.get(coords)).equals(Integer.MIN_VALUE) || (Integer) heightMap.get(coords) * offset < coordinate * offset) {
                        heightMap.put(coords, coordinate);
                        maxEntry = Math.max(maxEntry, coordinate);
                        minEntry = Math.min(minEntry, coordinate);
                    }
                }
            }
        }
        if (!surfaces.isEmpty()) {
            int[][] heightMapArray = new int[maxCoord1 - minCoord1 + 1][maxCoord2 - minCoord2 + 1];
            for (int i = 0; i < heightMapArray.length; i++) {
                for (int j = 0; j < heightMapArray[i].length; j++) {
                    Pair<Integer, Integer> pair = Pair.of(minCoord1 + i, minCoord2 + j);
                    if (!heightMap.containsKey(pair)) {
                        heightMapArray[i][j] = Integer.MIN_VALUE;
                    } else {
                        Integer height = (Integer) heightMap.get(pair);
                        if (height.equals(Integer.MAX_VALUE)) {
                            heightMapArray[i][j] = offset == 1 ? maxEntry + 2 : minEntry - 2;
                        } else if (height.equals(Integer.MIN_VALUE)) {
                            heightMapArray[i][j] = offset == 1 ? minEntry - 2 : maxEntry + 2;
                        } else {
                            heightMapArray[i][j] = height;
                        }
                    }
                }
            }
            heightMapArray = applyKernel(heightMapArray);
            for (BlockPos px : surfaces) {
                Pair<Integer, Integer> coords = getCoords(px, facing);
                int surfaceCoord = facing.getAxis().choose(px.m_123341_(), px.m_123342_(), px.m_123343_()) * offset;
                int targetCoord = heightMapArray[coords.getKey() - minCoord1][coords.getValue() - minCoord2] * offset;
                if (surfaceCoord != targetCoord) {
                    BlockState blockState = world.getBlockState(px);
                    int timeOut = 1000;
                    while (surfaceCoord > targetCoord) {
                        BlockPos below = px.relative(facing.getOpposite());
                        world.setBlockAndUpdate(below, blockState);
                        world.setBlockAndUpdate(px, blockState.m_60819_().createLegacyBlock());
                        px = px.relative(facing.getOpposite());
                        surfaceCoord--;
                        if (timeOut-- <= 0) {
                            break;
                        }
                    }
                    while (surfaceCoord < targetCoord) {
                        BlockPos above = px.relative(facing);
                        if (!(blockState.m_60734_() instanceof LiquidBlock)) {
                            world.setBlockAndUpdate(above, blockState);
                        }
                        world.setBlockAndUpdate(px, world.getBlockState(px.relative(facing.getOpposite())));
                        px = px.relative(facing);
                        surfaceCoord++;
                        if (timeOut-- <= 0) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private static Pair<Integer, Integer> getCoords(BlockPos pos, Direction facing) {
        switch(facing.getAxis()) {
            case X:
                return Pair.of(pos.m_123343_(), pos.m_123342_());
            case Y:
                return Pair.of(pos.m_123341_(), pos.m_123343_());
            case Z:
                return Pair.of(pos.m_123341_(), pos.m_123342_());
            default:
                return null;
        }
    }
}