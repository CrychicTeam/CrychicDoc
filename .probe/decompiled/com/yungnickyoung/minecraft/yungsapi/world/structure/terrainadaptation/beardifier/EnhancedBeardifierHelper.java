package com.yungnickyoung.minecraft.yungsapi.world.structure.terrainadaptation.beardifier;

import com.yungnickyoung.minecraft.yungsapi.mixin.accessor.BeardifierAccessor;
import com.yungnickyoung.minecraft.yungsapi.world.structure.YungJigsawStructure;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element.YungJigsawPoolElement;
import com.yungnickyoung.minecraft.yungsapi.world.structure.terrainadaptation.EnhancedTerrainAdaptation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class EnhancedBeardifierHelper {

    public static Beardifier forStructuresInChunk(StructureManager structureManager, ChunkPos chunkPos, Beardifier original) {
        ObjectList<EnhancedBeardifierRigid> enhancedBeardifierRigidList = new ObjectArrayList(10);
        ObjectList<EnhancedJigsawJunction> enhancedJunctionList = new ObjectArrayList(10);
        int chunkMinBlockX = chunkPos.getMinBlockX();
        int chunkMinBlockZ = chunkPos.getMinBlockZ();
        for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, structure -> structure instanceof YungJigsawStructure)) {
            EnhancedTerrainAdaptation structureTerrainAdaptation = ((YungJigsawStructure) structureStart.getStructure()).enhancedTerrainAdaptation;
            int kernelRadius = structureTerrainAdaptation.getKernelRadius();
            for (StructurePiece structurePiece : structureStart.getPieces()) {
                if (structurePiece instanceof PoolElementStructurePiece) {
                    PoolElementStructurePiece poolPiece = (PoolElementStructurePiece) structurePiece;
                    StructurePoolElement poolElementPiece = poolPiece.getElement();
                    if (poolElementPiece instanceof YungJigsawPoolElement) {
                        YungJigsawPoolElement yungElement = (YungJigsawPoolElement) poolElementPiece;
                        if (yungElement.getEnhancedTerrainAdaptation().isPresent()) {
                            kernelRadius = Math.max(kernelRadius, ((EnhancedTerrainAdaptation) yungElement.getEnhancedTerrainAdaptation().get()).getKernelRadius());
                        }
                    }
                }
            }
            int maxKernelRadius = kernelRadius;
            if (maxKernelRadius > 0) {
                for (StructurePiece nearbyPiece : structureStart.getPieces().stream().filter(structurePiecex -> structurePiecex.isCloseToChunk(chunkPos, maxKernelRadius)).toList()) {
                    if (nearbyPiece instanceof PoolElementStructurePiece) {
                        PoolElementStructurePiece poolElementPiece = (PoolElementStructurePiece) nearbyPiece;
                        StructureTemplatePool.Projection projection = poolElementPiece.getElement().getProjection();
                        EnhancedTerrainAdaptation pieceTerrainAdaptation = structureTerrainAdaptation;
                        if (poolElementPiece.getElement() instanceof YungJigsawPoolElement yungElement && yungElement.getEnhancedTerrainAdaptation().isPresent()) {
                            pieceTerrainAdaptation = (EnhancedTerrainAdaptation) yungElement.getEnhancedTerrainAdaptation().get();
                        }
                        if (pieceTerrainAdaptation != EnhancedTerrainAdaptation.NONE) {
                            int pieceKernelRadius = pieceTerrainAdaptation.getKernelRadius();
                            if (projection == StructureTemplatePool.Projection.RIGID) {
                                enhancedBeardifierRigidList.add(new EnhancedBeardifierRigid(poolElementPiece.m_73547_(), pieceTerrainAdaptation, poolElementPiece.getGroundLevelDelta()));
                            }
                            for (JigsawJunction jigsawJunction : poolElementPiece.getJunctions()) {
                                int sourceX = jigsawJunction.getSourceX();
                                int sourceZ = jigsawJunction.getSourceZ();
                                if (sourceX > chunkMinBlockX - pieceKernelRadius && sourceZ > chunkMinBlockZ - pieceKernelRadius && sourceX < chunkMinBlockX + 15 + pieceKernelRadius && sourceZ < chunkMinBlockZ + 15 + pieceKernelRadius) {
                                    enhancedJunctionList.add(new EnhancedJigsawJunction(jigsawJunction, pieceTerrainAdaptation));
                                }
                            }
                        }
                    } else if (structureTerrainAdaptation != EnhancedTerrainAdaptation.NONE) {
                        enhancedBeardifierRigidList.add(new EnhancedBeardifierRigid(nearbyPiece.getBoundingBox(), structureTerrainAdaptation, 0));
                    }
                }
            }
        }
        Beardifier newBeardifier = new Beardifier(((BeardifierAccessor) original).getPieceIterator(), ((BeardifierAccessor) original).getJunctionIterator());
        ((EnhancedBeardifierData) newBeardifier).setEnhancedRigidIterator(enhancedBeardifierRigidList.iterator());
        ((EnhancedBeardifierData) newBeardifier).setEnhancedJunctionIterator(enhancedJunctionList.iterator());
        return newBeardifier;
    }

    public static double computeDensity(DensityFunction.FunctionContext ctx, double density, EnhancedBeardifierData data) {
        int x = ctx.blockX();
        int y = ctx.blockY();
        int z = ctx.blockZ();
        while (data.getEnhancedRigidIterator() != null && data.getEnhancedRigidIterator().hasNext()) {
            EnhancedBeardifierRigid rigid = (EnhancedBeardifierRigid) data.getEnhancedRigidIterator().next();
            BoundingBox pieceBoundingBox = rigid.pieceBoundingBox();
            int adjustedPieceMinY = pieceBoundingBox.minY();
            EnhancedTerrainAdaptation pieceTerrainAdaptation = rigid.pieceTerrainAdaptation();
            int xDistanceToBoundingBox = Math.max(0, Math.max(pieceBoundingBox.minX() - x, x - pieceBoundingBox.maxX()));
            int yDistanceToBoundingBox = Math.max(0, Math.max(adjustedPieceMinY - y, y - pieceBoundingBox.maxY()));
            int zDistanceToBoundingBox = Math.max(0, Math.max(pieceBoundingBox.minZ() - z, z - pieceBoundingBox.maxZ()));
            int yDistanceToAdjustedPieceBottom = y - adjustedPieceMinY;
            double densityFactor = 0.0;
            if (pieceTerrainAdaptation != EnhancedTerrainAdaptation.NONE) {
                densityFactor = pieceTerrainAdaptation.computeDensityFactor(xDistanceToBoundingBox, yDistanceToBoundingBox, zDistanceToBoundingBox, yDistanceToAdjustedPieceBottom) * 0.8;
            }
            density += densityFactor;
        }
        data.getEnhancedRigidIterator().back(Integer.MAX_VALUE);
        while (data.getEnhancedJunctionIterator() != null && data.getEnhancedJunctionIterator().hasNext()) {
            EnhancedJigsawJunction enhancedJigsawJunction = (EnhancedJigsawJunction) data.getEnhancedJunctionIterator().next();
            JigsawJunction jigsawJunction = enhancedJigsawJunction.jigsawJunction();
            EnhancedTerrainAdaptation pieceTerrainAdaptation = enhancedJigsawJunction.pieceTerrainAdaptation();
            int xDistanceToJunction = x - jigsawJunction.getSourceX();
            int yDistanceToJunction = y - jigsawJunction.getSourceGroundY();
            int zDistanceToJunction = z - jigsawJunction.getSourceZ();
            density += pieceTerrainAdaptation.computeDensityFactor(xDistanceToJunction, yDistanceToJunction, zDistanceToJunction, yDistanceToJunction) * 0.4;
        }
        data.getEnhancedJunctionIterator().back(Integer.MAX_VALUE);
        return density;
    }
}