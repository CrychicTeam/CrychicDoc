package com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.assembler;

import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import com.yungnickyoung.minecraft.yungsapi.mixin.accessor.BoundingBoxAccessor;
import com.yungnickyoung.minecraft.yungsapi.mixin.accessor.StructureTemplatePoolAccessor;
import com.yungnickyoung.minecraft.yungsapi.util.BoxOctree;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element.IMaxCountJigsawPoolElement;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element.YungJigsawPoolElement;
import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.element.YungJigsawSinglePoolElement;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableObject;

public class JigsawStructureAssembler {

    private final JigsawStructureAssembler.Settings settings;

    private final List<PieceEntry> pieces = new ArrayList();

    public Deque<PieceEntry> unprocessedPieceEntries = Queues.newArrayDeque();

    private final Map<String, Integer> pieceCounts = new HashMap();

    private final Map<String, Integer> maxPieceCounts = new HashMap();

    public JigsawStructureAssembler(JigsawStructureAssembler.Settings settings) {
        this.settings = settings;
    }

    public void assembleStructure(PoolElementStructurePiece startPiece, BoxOctree structureBounds) {
        PieceEntry startPieceEntry = new PieceEntry(startPiece, new MutableObject(structureBounds), null, 0, null, null, null);
        this.pieces.add(startPieceEntry);
        this.unprocessedPieceEntries.addLast(startPieceEntry);
        while (!this.unprocessedPieceEntries.isEmpty()) {
            PieceEntry entry = (PieceEntry) this.unprocessedPieceEntries.removeFirst();
            this.addChildrenForPiece(entry);
        }
        this.applyModifications();
    }

    public void addAllPiecesToStructureBuilder(StructurePiecesBuilder structurePiecesBuilder) {
        this.pieces.forEach(pieceEntry -> structurePiecesBuilder.addPiece(pieceEntry.getPiece()));
    }

    private void addChildrenForPiece(PieceEntry pieceEntry) {
        PoolElementStructurePiece piece = pieceEntry.getPiece();
        MutableObject<BoxOctree> parentOctree = new MutableObject();
        List<StructureTemplate.StructureBlockInfo> pieceJigsawBlocks = piece.getElement().getShuffledJigsawBlocks(this.settings.structureTemplateManager, piece.getPosition(), piece.getRotation(), this.settings.rand);
        boolean generatedAtLeastOneChildPiece = false;
        for (StructureTemplate.StructureBlockInfo jigsawBlockInfo : pieceJigsawBlocks) {
            ResourceKey<StructureTemplatePool> poolKey = readPoolName(jigsawBlockInfo);
            Optional<? extends Holder<StructureTemplatePool>> optionalPoolHolder = this.settings.poolRegistry.getHolder(poolKey);
            if (optionalPoolHolder.isEmpty()) {
                YungsApiCommon.LOGGER.warn("Empty or nonexistent pool: {}", poolKey.location());
            } else {
                Holder<StructureTemplatePool> targetPoolHolder = (Holder<StructureTemplatePool>) optionalPoolHolder.get();
                StructureTemplatePool targetPool = targetPoolHolder.value();
                if (targetPool.size() == 0 && !targetPoolHolder.is(Pools.EMPTY)) {
                    YungsApiCommon.LOGGER.warn("Empty or nonexistent pool: {}", poolKey.location());
                } else {
                    Holder<StructureTemplatePool> fallbackPoolHolder = targetPoolHolder.value().getFallback();
                    StructureTemplatePool fallbackPool = fallbackPoolHolder.value();
                    if (fallbackPool.size() == 0 && !fallbackPoolHolder.is(Pools.EMPTY)) {
                        YungsApiCommon.LOGGER.warn("Empty or nonexistent fallback pool: {}", fallbackPoolHolder.unwrapKey().map(key -> key.location().toString()).orElse("<unregistered>"));
                    } else {
                        PieceContext pieceContext = this.createPieceContextForJigsawBlock(jigsawBlockInfo, pieceEntry, parentOctree);
                        Optional<StructurePoolElement> newlyGeneratedPiece = Optional.empty();
                        if (pieceEntry.getDepth() != this.settings.maxDepth) {
                            pieceContext.candidatePoolElements = new ObjectArrayList(((StructureTemplatePoolAccessor) targetPool).getRawTemplates());
                            newlyGeneratedPiece = this.chooseCandidateFromPool(pieceContext);
                        }
                        if (newlyGeneratedPiece.isEmpty()) {
                            pieceContext.candidatePoolElements = new ObjectArrayList(((StructureTemplatePoolAccessor) fallbackPool).getRawTemplates());
                            newlyGeneratedPiece = this.chooseCandidateFromPool(pieceContext);
                        }
                        if (newlyGeneratedPiece.isPresent()) {
                            generatedAtLeastOneChildPiece = true;
                        }
                    }
                }
            }
        }
        if (pieceEntry.getDeadendPool().isPresent() && !generatedAtLeastOneChildPiece && pieceJigsawBlocks.size() > 1) {
            ResourceLocation deadendPoolId = (ResourceLocation) pieceEntry.getDeadendPool().get();
            Optional<StructureTemplatePool> deadendPool = this.settings.poolRegistry.getOptional(deadendPoolId);
            if (deadendPool.isEmpty()) {
                YungsApiCommon.LOGGER.error("Unable to find deadend pool {} for element {}", deadendPoolId, piece.getElement());
                return;
            }
            PieceEntry parentEntry = pieceEntry.getParentEntry();
            PieceContext newContext = pieceEntry.getSourcePieceContext().copy();
            newContext.candidatePoolElements = new ObjectArrayList(((StructureTemplatePoolAccessor) deadendPool.get()).getRawTemplates());
            AABB pieceAabb = pieceEntry.getPieceAabb();
            if (parentEntry != null && pieceAabb != null) {
                parentEntry.getPiece().getJunctions().remove(pieceEntry.getParentJunction());
                ((BoxOctree) pieceEntry.getBoxOctree().getValue()).removeBox(pieceAabb);
                this.pieces.remove(pieceEntry);
                if (pieceEntry.getPiece().getElement() instanceof YungJigsawPoolElement yungElement && yungElement.maxCount.isPresent() && yungElement.name.isPresent() && this.pieceCounts.containsKey(yungElement.name.get())) {
                    String pieceName = (String) yungElement.name.get();
                    this.pieceCounts.put(pieceName, (Integer) this.pieceCounts.get(pieceName) - 1);
                }
                if (pieceEntry.getPiece().getElement() instanceof IMaxCountJigsawPoolElement maxCountJigsawPoolElement && this.pieceCounts.containsKey(maxCountJigsawPoolElement.getName())) {
                    String pieceName = maxCountJigsawPoolElement.getName();
                    this.pieceCounts.put(pieceName, (Integer) this.pieceCounts.get(pieceName) - 1);
                }
                this.chooseCandidateFromPool(newContext);
            }
        }
    }

    private PieceContext createPieceContextForJigsawBlock(StructureTemplate.StructureBlockInfo jigsawBlockInfo, PieceEntry pieceEntry, MutableObject<BoxOctree> parentOctree) {
        BoundingBox pieceBoundingBox = pieceEntry.getPiece().m_73547_();
        MutableObject<BoxOctree> pieceOctree = pieceEntry.getBoxOctree();
        Direction direction = JigsawBlock.getFrontFacing(jigsawBlockInfo.state());
        BlockPos jigsawBlockTargetPos = jigsawBlockInfo.pos().relative(direction);
        boolean isTargetInsideCurrentPiece = pieceBoundingBox.isInside(jigsawBlockTargetPos);
        if (isTargetInsideCurrentPiece) {
            pieceOctree = parentOctree;
            if (parentOctree.getValue() == null) {
                parentOctree.setValue(new BoxOctree(AABB.of(pieceBoundingBox)));
            }
        }
        return new PieceContext(null, jigsawBlockInfo, jigsawBlockTargetPos, pieceBoundingBox.minY(), jigsawBlockInfo.pos(), pieceOctree, pieceEntry, pieceEntry.getDepth());
    }

    private Optional<StructurePoolElement> chooseCandidateFromPool(PieceContext context) {
        ObjectArrayList<Pair<StructurePoolElement, Integer>> candidatePoolElements = context.candidatePoolElements;
        PoolElementStructurePiece piece = context.pieceEntry.getPiece();
        boolean isPieceRigid = piece.getElement().getProjection() == StructureTemplatePool.Projection.RIGID;
        int jigsawBlockRelativeY = context.jigsawBlockPos.m_123342_() - context.pieceMinY;
        int surfaceHeight = -1;
        Util.shuffle(candidatePoolElements, this.settings.rand);
        int totalWeightSum = candidatePoolElements.stream().mapToInt(Pair::getSecond).reduce(0, Integer::sum);
        label201: while (candidatePoolElements.size() > 0 && totalWeightSum > 0) {
            Pair<StructurePoolElement, Integer> chosenPoolElementPair = null;
            ObjectListIterator chosenPoolElement = candidatePoolElements.iterator();
            while (chosenPoolElement.hasNext()) {
                Pair<StructurePoolElement, Integer> candidatePiecePair = (Pair<StructurePoolElement, Integer>) chosenPoolElement.next();
                StructurePoolElement candidatePiece = (StructurePoolElement) candidatePiecePair.getFirst();
                if (candidatePiece instanceof YungJigsawPoolElement yungElement && yungElement.isPriorityPiece()) {
                    chosenPoolElementPair = candidatePiecePair;
                    break;
                }
            }
            if (chosenPoolElementPair == null) {
                int chosenWeight = this.settings.rand.nextInt(totalWeightSum) + 1;
                ObjectListIterator var42 = candidatePoolElements.iterator();
                while (var42.hasNext()) {
                    Pair<StructurePoolElement, Integer> candidate = (Pair<StructurePoolElement, Integer>) var42.next();
                    chosenWeight -= candidate.getSecond();
                    if (chosenWeight <= 0) {
                        chosenPoolElementPair = candidate;
                        break;
                    }
                }
            }
            StructurePoolElement chosenPoolElementx = (StructurePoolElement) chosenPoolElementPair.getFirst();
            int chosenPieceWeight = (Integer) chosenPoolElementPair.getSecond();
            if (chosenPoolElementx == EmptyPoolElement.INSTANCE) {
                return Optional.empty();
            }
            if (chosenPoolElementx instanceof YungJigsawPoolElement yungElement && yungElement.maxCount.isPresent()) {
                int pieceMaxCount = (Integer) yungElement.maxCount.get();
                if (yungElement.name.isEmpty()) {
                    YungsApiCommon.LOGGER.error("Found YUNG Jigsaw piece with max_count={} missing \"name\" property.", pieceMaxCount);
                    YungsApiCommon.LOGGER.error("Max count pieces must be named in order to work properly!");
                    YungsApiCommon.LOGGER.error("Ignoring max_count for this piece...");
                } else {
                    String pieceName = (String) yungElement.name.get();
                    if (this.maxPieceCounts.containsKey(pieceName) && (Integer) this.maxPieceCounts.get(pieceName) != pieceMaxCount) {
                        YungsApiCommon.LOGGER.error("YUNG Jigsaw Piece with name {} and max_count {} does not match stored max_count of {}!", pieceName, pieceMaxCount, this.maxPieceCounts.get(pieceName));
                        YungsApiCommon.LOGGER.error("This can happen when multiple pieces across pools use the same name, but have different max_count values.");
                        YungsApiCommon.LOGGER.error("Please change these max_count values to match. Using max_count={} for now...", pieceMaxCount);
                    }
                    this.maxPieceCounts.put(pieceName, pieceMaxCount);
                    if ((Integer) this.pieceCounts.getOrDefault(pieceName, 0) >= pieceMaxCount) {
                        totalWeightSum -= chosenPieceWeight;
                        candidatePoolElements.remove(chosenPoolElementPair);
                        continue;
                    }
                }
            }
            if (chosenPoolElementx instanceof IMaxCountJigsawPoolElement) {
                String pieceNamex = ((IMaxCountJigsawPoolElement) chosenPoolElementx).getName();
                int maxCount = ((IMaxCountJigsawPoolElement) chosenPoolElementx).getMaxCount();
                if (this.maxPieceCounts.containsKey(pieceNamex) && (Integer) this.maxPieceCounts.get(pieceNamex) != maxCount) {
                    YungsApiCommon.LOGGER.error("Max Count Jigsaw Piece with name {} and max_count {} does not match stored max_count of {}!", pieceNamex, maxCount, this.maxPieceCounts.get(pieceNamex));
                    YungsApiCommon.LOGGER.error("This can happen when multiple pieces across pools use the same name, but have different max_count values.");
                    YungsApiCommon.LOGGER.error("Please change these max_count values to match. Using max_count={} for now...", maxCount);
                }
                this.maxPieceCounts.put(pieceNamex, maxCount);
                if ((Integer) this.pieceCounts.getOrDefault(pieceNamex, 0) >= maxCount) {
                    totalWeightSum -= chosenPoolElementPair.getSecond();
                    candidatePoolElements.remove(chosenPoolElementPair);
                    continue;
                }
            }
            if (chosenPoolElementx instanceof YungJigsawPoolElement yungElementx && !yungElementx.isAtValidDepth(context.depth)) {
                totalWeightSum -= chosenPieceWeight;
                candidatePoolElements.remove(chosenPoolElementPair);
                continue;
            }
            Iterator var48 = Rotation.getShuffled(this.settings.rand).iterator();
            int candidateJigsawBlockRelativeY;
            int candidateJigsawYOffsetNeeded;
            int candidateJigsawBlockY;
            PoolElementStructurePiece newPiece;
            JigsawJunction newJunctionOnParent;
            PieceEntry newPieceEntry;
            AABB aabb;
            int groundLevelDelta;
            label177: while (true) {
                if (!var48.hasNext()) {
                    totalWeightSum -= chosenPieceWeight;
                    candidatePoolElements.remove(chosenPoolElementPair);
                    continue label201;
                }
                Rotation rotation = (Rotation) var48.next();
                List<StructureTemplate.StructureBlockInfo> candidateJigsawBlocks = chosenPoolElementx.getShuffledJigsawBlocks(this.settings.structureTemplateManager, BlockPos.ZERO, rotation, this.settings.rand);
                BoundingBox tempCandidateBoundingBox = chosenPoolElementx.getBoundingBox(this.settings.structureTemplateManager, BlockPos.ZERO, rotation);
                int candidateHeightAdjustments = 0;
                if (this.settings.useExpansionHack && tempCandidateBoundingBox.getYSpan() <= 16) {
                    candidateHeightAdjustments = candidateJigsawBlocks.stream().mapToInt(pieceCandidateJigsawBlock -> {
                        if (!tempCandidateBoundingBox.isInside(pieceCandidateJigsawBlock.pos().relative(JigsawBlock.getFrontFacing(pieceCandidateJigsawBlock.state())))) {
                            return 0;
                        } else {
                            ResourceKey<StructureTemplatePool> candidateTargetPoolKey = readPoolName(pieceCandidateJigsawBlock);
                            Optional<? extends Holder<StructureTemplatePool>> candidateTargetPool = this.settings.poolRegistry.getHolder(candidateTargetPoolKey);
                            Optional<Holder<StructureTemplatePool>> candidateFallbackPool = candidateTargetPool.map(poolHolder -> ((StructureTemplatePool) poolHolder.value()).getFallback());
                            int candidateMaxSize = (Integer) candidateTargetPool.map(poolHolder -> ((StructureTemplatePool) poolHolder.value()).getMaxSize(this.settings.structureTemplateManager)).orElse(0);
                            int candidateFallbackMaxSize = (Integer) candidateFallbackPool.map(poolHolder -> ((StructureTemplatePool) poolHolder.value()).getMaxSize(this.settings.structureTemplateManager)).orElse(0);
                            return Math.max(candidateMaxSize, candidateFallbackMaxSize);
                        }
                    }).max().orElse(0);
                }
                for (StructureTemplate.StructureBlockInfo candidateJigsawBlock : candidateJigsawBlocks) {
                    if (JigsawBlock.canAttach(context.jigsawBlock, candidateJigsawBlock)) {
                        BlockPos candidateJigsawBlockPos = candidateJigsawBlock.pos();
                        BlockPos candidateJigsawBlockRelativePos = context.jigsawBlockTargetPos.subtract(candidateJigsawBlockPos);
                        BoundingBox rotatedCandidateBoundingBox = chosenPoolElementx.getBoundingBox(this.settings.structureTemplateManager, candidateJigsawBlockRelativePos, rotation);
                        StructureTemplatePool.Projection candidateProjection = chosenPoolElementx.getProjection();
                        boolean isCandidateRigid = candidateProjection == StructureTemplatePool.Projection.RIGID;
                        candidateJigsawBlockRelativeY = candidateJigsawBlockPos.m_123342_();
                        candidateJigsawYOffsetNeeded = jigsawBlockRelativeY - candidateJigsawBlockRelativeY + JigsawBlock.getFrontFacing(context.jigsawBlock.state()).getStepY();
                        int adjustedCandidatePieceMinY;
                        if (isPieceRigid && isCandidateRigid) {
                            adjustedCandidatePieceMinY = context.pieceMinY + candidateJigsawYOffsetNeeded;
                        } else {
                            if (surfaceHeight == -1) {
                                surfaceHeight = this.settings.chunkGenerator.getFirstFreeHeight(context.jigsawBlockPos.m_123341_(), context.jigsawBlockPos.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, this.settings.levelHeightAccessor, this.settings.randomState);
                            }
                            adjustedCandidatePieceMinY = surfaceHeight - candidateJigsawBlockRelativeY;
                        }
                        int candidatePieceYOffsetNeeded = adjustedCandidatePieceMinY - rotatedCandidateBoundingBox.minY();
                        BoundingBox adjustedCandidateBoundingBox = rotatedCandidateBoundingBox.moved(0, candidatePieceYOffsetNeeded, 0);
                        BlockPos adjustedCandidateJigsawBlockRelativePos = candidateJigsawBlockRelativePos.offset(0, candidatePieceYOffsetNeeded, 0);
                        if (candidateHeightAdjustments > 0) {
                            int k2 = Math.max(candidateHeightAdjustments + 1, adjustedCandidateBoundingBox.maxY() - adjustedCandidateBoundingBox.minY());
                            ((BoundingBoxAccessor) adjustedCandidateBoundingBox).setMaxY(adjustedCandidateBoundingBox.minY() + k2);
                        }
                        if ((!this.settings.maxY.isPresent() || adjustedCandidateBoundingBox.maxY() <= (Integer) this.settings.maxY.get()) && (!this.settings.minY.isPresent() || adjustedCandidateBoundingBox.minY() >= (Integer) this.settings.minY.get())) {
                            aabb = AABB.of(adjustedCandidateBoundingBox);
                            AABB aabbDeflated = aabb.deflate(0.25);
                            boolean pieceIgnoresBounds = false;
                            if (chosenPoolElementx instanceof YungJigsawPoolElement yungElementx) {
                                pieceIgnoresBounds = yungElementx.ignoresBounds();
                            }
                            if (!pieceIgnoresBounds) {
                                boolean pieceIntersectsExistingPieces = ((BoxOctree) context.boxOctree.getValue()).intersectsAnyBox(aabbDeflated);
                                boolean pieceIsContainedWithinStructureBoundaries = ((BoxOctree) context.boxOctree.getValue()).boundaryContains(aabbDeflated);
                                if (pieceIntersectsExistingPieces || !pieceIsContainedWithinStructureBoundaries) {
                                    continue;
                                }
                            }
                            int newPieceGroundLevelDelta = piece.getGroundLevelDelta();
                            if (isCandidateRigid) {
                                groundLevelDelta = newPieceGroundLevelDelta - candidateJigsawYOffsetNeeded;
                            } else {
                                groundLevelDelta = chosenPoolElementx.getGroundLevelDelta();
                            }
                            if (isPieceRigid) {
                                candidateJigsawBlockY = context.pieceMinY + jigsawBlockRelativeY;
                            } else if (isCandidateRigid) {
                                candidateJigsawBlockY = adjustedCandidatePieceMinY + candidateJigsawBlockRelativeY;
                            } else {
                                if (surfaceHeight == -1) {
                                    surfaceHeight = this.settings.chunkGenerator.getFirstFreeHeight(context.jigsawBlockPos.m_123341_(), context.jigsawBlockPos.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, this.settings.levelHeightAccessor, this.settings.randomState);
                                }
                                candidateJigsawBlockY = surfaceHeight + candidateJigsawYOffsetNeeded / 2;
                            }
                            newPiece = new PoolElementStructurePiece(this.settings.structureTemplateManager, chosenPoolElementx, adjustedCandidateJigsawBlockRelativePos, groundLevelDelta, rotation, adjustedCandidateBoundingBox);
                            newJunctionOnParent = new JigsawJunction(context.jigsawBlockTargetPos.m_123341_(), candidateJigsawBlockY - jigsawBlockRelativeY + newPieceGroundLevelDelta, context.jigsawBlockTargetPos.m_123343_(), candidateJigsawYOffsetNeeded, candidateProjection);
                            newPieceEntry = new PieceEntry(newPiece, context.boxOctree, aabb, context.depth + 1, context.pieceEntry, context.copy(), newJunctionOnParent);
                            if (!(chosenPoolElementx instanceof YungJigsawPoolElement yungElementx)) {
                                break label177;
                            }
                            StructureContext ctx = new StructureContext.Builder().structureTemplateManager(this.settings.structureTemplateManager).pieces(this.pieces).pieceEntry(newPieceEntry).pos(adjustedCandidateJigsawBlockRelativePos).rotation(rotation).pieceMinY(adjustedCandidateBoundingBox.minY()).pieceMaxY(adjustedCandidateBoundingBox.maxY()).depth(context.depth + 1).random(this.settings.rand).build();
                            if (yungElementx.passesConditions(ctx)) {
                                break label177;
                            }
                        }
                    }
                }
            }
            piece.addJunction(newJunctionOnParent);
            newPiece.addJunction(new JigsawJunction(context.jigsawBlockPos.m_123341_(), candidateJigsawBlockY - candidateJigsawBlockRelativeY + groundLevelDelta, context.jigsawBlockPos.m_123343_(), -candidateJigsawYOffsetNeeded, piece.getElement().getProjection()));
            ((BoxOctree) context.boxOctree.getValue()).addBox(aabb);
            this.pieces.add(newPieceEntry);
            context.pieceEntry.addChildEntry(newPieceEntry);
            if (context.depth + 1 <= this.settings.maxDepth) {
                this.unprocessedPieceEntries.addLast(newPieceEntry);
            }
            if (chosenPoolElementx instanceof YungJigsawPoolElement yungElementx && yungElementx.maxCount.isPresent()) {
                if (yungElementx.name.isEmpty()) {
                    return Optional.of(chosenPoolElementx);
                }
                String pieceNamexx = (String) yungElementx.name.get();
                this.pieceCounts.put(pieceNamexx, (Integer) this.pieceCounts.getOrDefault(pieceNamexx, 0) + 1);
            }
            if (chosenPoolElementx instanceof IMaxCountJigsawPoolElement) {
                String pieceNamexx = ((IMaxCountJigsawPoolElement) chosenPoolElementx).getName();
                this.pieceCounts.put(pieceNamexx, (Integer) this.pieceCounts.getOrDefault(pieceNamexx, 0) + 1);
            }
            return Optional.of(chosenPoolElementx);
        }
        return Optional.empty();
    }

    private void applyModifications() {
        for (PieceEntry pieceEntry : this.pieces) {
            StructurePoolElement piece = pieceEntry.getPiece().getElement();
            if (piece instanceof YungJigsawSinglePoolElement) {
                YungJigsawSinglePoolElement yungElement = (YungJigsawSinglePoolElement) piece;
                if (yungElement.hasModifiers()) {
                    PoolElementStructurePiece piecex = pieceEntry.getPiece();
                    StructureContext structureContext = new StructureContext.Builder().pos(piecex.getPosition()).rotation(piecex.getRotation()).depth(pieceEntry.getDepth()).structureTemplateManager(this.settings.structureTemplateManager).pieceEntry(pieceEntry).pieces(this.pieces).pieceMaxY(piecex.m_73547_().maxY()).pieceMinY(piecex.m_73547_().minY()).random(this.settings.rand).build();
                    yungElement.modifiers.forEach(modifier -> modifier.apply(structureContext));
                }
            }
        }
        List<PieceEntry> delayedEntries = this.pieces.stream().filter(PieceEntry::isDelayGeneration).toList();
        this.pieces.removeAll(delayedEntries);
        this.pieces.addAll(delayedEntries);
    }

    private static ResourceKey<StructureTemplatePool> readPoolName(StructureTemplate.StructureBlockInfo jigsawBlockInfo) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(jigsawBlockInfo.nbt().getString("pool")));
    }

    public static class Settings {

        private Registry<StructureTemplatePool> poolRegistry;

        private int maxDepth;

        private ChunkGenerator chunkGenerator;

        private StructureTemplateManager structureTemplateManager;

        private LevelHeightAccessor levelHeightAccessor;

        private RandomSource rand;

        private boolean useExpansionHack;

        public RandomState randomState;

        private Optional<Integer> maxY;

        private Optional<Integer> minY;

        public JigsawStructureAssembler.Settings poolRegistry(Registry<StructureTemplatePool> poolRegistry) {
            this.poolRegistry = poolRegistry;
            return this;
        }

        public JigsawStructureAssembler.Settings maxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public JigsawStructureAssembler.Settings chunkGenerator(ChunkGenerator chunkGenerator) {
            this.chunkGenerator = chunkGenerator;
            return this;
        }

        public JigsawStructureAssembler.Settings structureTemplateManager(StructureTemplateManager structureTemplateManager) {
            this.structureTemplateManager = structureTemplateManager;
            return this;
        }

        public JigsawStructureAssembler.Settings randomState(RandomState randomState) {
            this.randomState = randomState;
            return this;
        }

        public JigsawStructureAssembler.Settings rand(RandomSource rand) {
            this.rand = rand;
            return this;
        }

        public JigsawStructureAssembler.Settings useExpansionHack(boolean useExpansionHack) {
            this.useExpansionHack = useExpansionHack;
            return this;
        }

        public JigsawStructureAssembler.Settings levelHeightAccessor(LevelHeightAccessor levelHeightAccessor) {
            this.levelHeightAccessor = levelHeightAccessor;
            return this;
        }

        public JigsawStructureAssembler.Settings maxY(Optional<Integer> maxY) {
            this.maxY = maxY;
            return this;
        }

        public JigsawStructureAssembler.Settings minY(Optional<Integer> minY) {
            this.minY = minY;
            return this;
        }
    }
}