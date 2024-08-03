package com.craisinlord.integrated_api.world.structures.pieces.manager;

import com.craisinlord.integrated_api.IntegratedAPI;
import com.craisinlord.integrated_api.misc.structurepiececounter.StructurePieceCountsManager;
import com.craisinlord.integrated_api.mixins.structures.SinglePoolElementAccessor;
import com.craisinlord.integrated_api.mixins.structures.StructurePoolAccessor;
import com.craisinlord.integrated_api.utils.BoxOctree;
import com.craisinlord.integrated_api.utils.GeneralUtils;
import com.craisinlord.integrated_api.world.structures.JigsawStructure;
import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableObject;

public class PieceLimitedJigsawManager {

    public static Optional<Structure.GenerationStub> assembleJigsawStructure(Structure.GenerationContext context, Holder<StructureTemplatePool> startPoolHolder, int size, ResourceLocation structureID, BlockPos startPos, boolean doBoundaryAdjustments, Optional<Heightmap.Types> heightmapType, int maxY, int minY, Set<ResourceLocation> poolsThatIgnoreBounds, Optional<Integer> maxDistanceFromCenter, String rotationString, Optional<JigsawStructure.BURYING_TYPE> buryingType, BiConsumer<StructurePiecesBuilder, List<PoolElementStructurePiece>> structureBoundsAdjuster) {
        Registry<StructureTemplatePool> jigsawPoolRegistry = context.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        Rotation rotation = Rotation.NONE;
        if (rotationString.equals("RANDOM")) {
            rotation = Rotation.getRandom(random);
        } else if (rotationString.equals("CLOCKWISE_180")) {
            rotation = Rotation.CLOCKWISE_180;
        } else if (rotationString.equals("CLOCKWISE_90")) {
            rotation = Rotation.CLOCKWISE_90;
        } else if (rotationString.equals("COUNTERCLOCKWISE_90")) {
            rotation = Rotation.COUNTERCLOCKWISE_90;
        }
        StructureTemplatePool startPool = startPoolHolder.value();
        if (startPool.size() == 0) {
            IntegratedAPI.LOGGER.warn("Integrated API: Empty or nonexistent start pool in structure: {}  Crash is imminent", structureID);
            throw new RuntimeException("Integrated API: Empty or nonexistent start pool in structure: " + structureID + " Crash is imminent");
        } else {
            StructurePoolElement startPieceBlueprint = startPool.getRandomTemplate(random);
            if (startPieceBlueprint == EmptyPoolElement.INSTANCE) {
                return Optional.empty();
            } else {
                PoolElementStructurePiece startPiece = new PoolElementStructurePiece(context.structureTemplateManager(), startPieceBlueprint, startPos, startPieceBlueprint.getGroundLevelDelta(), rotation, startPieceBlueprint.getBoundingBox(context.structureTemplateManager(), startPos, rotation));
                BoundingBox pieceBoundingBox = startPiece.m_73547_();
                int pieceCenterX = (pieceBoundingBox.maxX() + pieceBoundingBox.minX()) / 2;
                int pieceCenterZ = (pieceBoundingBox.maxZ() + pieceBoundingBox.minZ()) / 2;
                int pieceCenterY = (Integer) heightmapType.map(types -> startPos.m_123342_() + context.chunkGenerator().getFirstFreeHeight(pieceCenterX, pieceCenterZ, types, context.heightAccessor(), context.randomState())).orElseGet(startPos::m_123342_);
                if (!heightmapType.isPresent() || pieceCenterY <= maxY && pieceCenterY >= minY) {
                    int yAdjustment = pieceBoundingBox.minY() + startPiece.getGroundLevelDelta();
                    startPiece.move(0, pieceCenterY - yAdjustment, 0);
                    return !context.validBiome().test(context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pieceCenterX), QuartPos.fromBlock(pieceCenterY), QuartPos.fromBlock(pieceCenterZ), context.randomState().sampler())) ? Optional.empty() : Optional.of(new Structure.GenerationStub(new BlockPos(pieceCenterX, pieceCenterY, pieceCenterZ), (Consumer<StructurePiecesBuilder>) (structurePiecesBuilder -> {
                        List<PoolElementStructurePiece> components = new ArrayList();
                        components.add(startPiece);
                        Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds> requiredPieces = StructurePieceCountsManager.STRUCTURE_PIECE_COUNTS_MANAGER.getRequirePieces(structureID);
                        boolean runOnce = requiredPieces == null || requiredPieces.isEmpty();
                        Map<ResourceLocation, Integer> currentPieceCounter = new HashMap();
                        for (int attempts = 0; runOnce || doesNotHaveAllRequiredPieces(components, requiredPieces, currentPieceCounter); attempts++) {
                            if (attempts == 40) {
                                IntegratedAPI.LOGGER.error("\n-------------------------------------------------------------------\nIntegrated API: Failed to create valid structure with all required pieces starting from this pool file: {}. Required pieces failed to generate the required amount are: {}\n  This can happen if a structure has a required piece but the structure size is set too low.\n  However, this is most likely caused by a structure unable to spawn properly due to hitting the world's min y or max y build thresholds or a broken RS datapack.\n  Try teleporting to: {} and see if the structure generated fine with the required structure piece or if it is indeed missing it.\n  Please report the issue to CraisinLord with latest.log file if the structure is not cut off by world min/max y build thresholds.\n\n", jigsawPoolRegistry.getKey(startPool), Arrays.toString(currentPieceCounter.entrySet().stream().filter(entryx -> (Integer) entryx.getValue() > 0).toArray()), new BlockPos(pieceCenterX, pieceCenterY, pieceCenterZ));
                                break;
                            }
                            PoolElementStructurePiece startPieceToUse = startPiece;
                            if (attempts > 0) {
                                StructurePoolElement startPieceBlueprintNew = startPool.getRandomTemplate(random);
                                startPieceToUse = new PoolElementStructurePiece(context.structureTemplateManager(), startPieceBlueprintNew, startPiece.getPosition(), startPieceBlueprintNew.getGroundLevelDelta(), startPiece.getRotation(), startPieceBlueprintNew.getBoundingBox(context.structureTemplateManager(), startPiece.getPosition(), startPiece.getRotation()));
                            }
                            components.clear();
                            components.add(startPieceToUse);
                            if (size > 0) {
                                int boxRange = (Integer) maxDistanceFromCenter.orElse(80);
                                AABB axisAlignedBB = new AABB((double) (pieceCenterX - boxRange), (double) (pieceCenterY - 120), (double) (pieceCenterZ - boxRange), (double) (pieceCenterX + boxRange + 1), (double) (pieceCenterY + 180 + 1), (double) (pieceCenterZ + boxRange + 1));
                                BoxOctree boxOctree = new BoxOctree(axisAlignedBB);
                                boxOctree.addBox(AABB.of(pieceBoundingBox));
                                PieceLimitedJigsawManager.Entry startPieceEntry = new PieceLimitedJigsawManager.Entry(startPieceToUse, new MutableObject(boxOctree), pieceCenterY + 80, 0);
                                PieceLimitedJigsawManager.Assembler assembler = new PieceLimitedJigsawManager.Assembler(structureID, jigsawPoolRegistry, size, context, components, random, requiredPieces, buryingType.isEmpty() ? maxY : Integer.MAX_VALUE, buryingType.isEmpty() ? minY : Integer.MIN_VALUE, poolsThatIgnoreBounds);
                                assembler.availablePieces.addLast(startPieceEntry);
                                while (!assembler.availablePieces.isEmpty()) {
                                    PieceLimitedJigsawManager.Entry entry = (PieceLimitedJigsawManager.Entry) assembler.availablePieces.removeFirst();
                                    assembler.generatePiece(entry.piece, entry.boxOctreeMutableObject, entry.topYLimit, entry.depth, doBoundaryAdjustments, context.heightAccessor());
                                }
                            }
                            if (runOnce) {
                                break;
                            }
                        }
                        components.forEach(structurePiecesBuilder::m_142679_);
                        structureBoundsAdjuster.accept(structurePiecesBuilder, components);
                        if (structurePiecesBuilder.getBoundingBox().maxY() > context.heightAccessor().getMaxBuildHeight()) {
                            structurePiecesBuilder.clear();
                        }
                    })));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private static boolean doesNotHaveAllRequiredPieces(List<? extends StructurePiece> components, Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds> requiredPieces, Map<ResourceLocation, Integer> counter) {
        counter.clear();
        requiredPieces.forEach((key, value) -> counter.put(key, value.getRequiredAmount()));
        for (Object piece : components) {
            if (piece instanceof PoolElementStructurePiece) {
                StructurePoolElement poolElement = ((PoolElementStructurePiece) piece).getElement();
                if (poolElement instanceof SinglePoolElement) {
                    ResourceLocation pieceID = (ResourceLocation) ((SinglePoolElementAccessor) poolElement).integratedapi_getTemplate().left().orElse(null);
                    if (counter.containsKey(pieceID)) {
                        counter.put(pieceID, (Integer) counter.get(pieceID) - 1);
                    }
                }
            }
        }
        return counter.values().stream().anyMatch(count -> count > 0);
    }

    public static final class Assembler {

        private final Registry<StructureTemplatePool> poolRegistry;

        private final int maxDepth;

        private final Structure.GenerationContext context;

        private final List<? super PoolElementStructurePiece> structurePieces;

        private final RandomSource random;

        public final Deque<PieceLimitedJigsawManager.Entry> availablePieces = Queues.newArrayDeque();

        private final Map<ResourceLocation, Integer> currentPieceCounts;

        private final Map<ResourceLocation, Integer> maximumPieceCounts;

        private final Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds> requiredPieces;

        private final int maxY;

        private final int minY;

        private final Set<ResourceLocation> poolsThatIgnoreBounds;

        public Assembler(ResourceLocation structureID, Registry<StructureTemplatePool> poolRegistry, int maxDepth, Structure.GenerationContext context, List<? super PoolElementStructurePiece> structurePieces, RandomSource random, Map<ResourceLocation, StructurePieceCountsManager.RequiredPieceNeeds> requiredPieces, int maxY, int minY, Set<ResourceLocation> poolsThatIgnoreBounds) {
            this.poolRegistry = poolRegistry;
            this.maxDepth = maxDepth;
            this.context = context;
            this.structurePieces = structurePieces;
            this.random = random;
            this.maxY = maxY;
            this.minY = minY;
            this.requiredPieces = requiredPieces == null ? new HashMap() : new HashMap(requiredPieces);
            this.maximumPieceCounts = new HashMap(StructurePieceCountsManager.STRUCTURE_PIECE_COUNTS_MANAGER.getMaximumCountForPieces(structureID));
            this.poolsThatIgnoreBounds = poolsThatIgnoreBounds;
            this.currentPieceCounts = new HashMap();
            this.requiredPieces.forEach((key, value) -> this.currentPieceCounts.putIfAbsent(key, 0));
            this.maximumPieceCounts.forEach((key, value) -> this.currentPieceCounts.putIfAbsent(key, 0));
        }

        public void generatePiece(PoolElementStructurePiece piece, MutableObject<BoxOctree> boxOctree, int minY, int depth, boolean doBoundaryAdjustments, LevelHeightAccessor heightLimitView) {
            StructurePoolElement pieceBlueprint = piece.getElement();
            BlockPos piecePos = piece.getPosition();
            Rotation pieceRotation = piece.getRotation();
            BoundingBox pieceBoundingBox = piece.m_73547_();
            int pieceMinY = pieceBoundingBox.minY();
            MutableObject<BoxOctree> parentOctree = new MutableObject();
            for (StructureTemplate.StructureBlockInfo jigsawBlock : pieceBlueprint.getShuffledJigsawBlocks(this.context.structureTemplateManager(), piecePos, pieceRotation, this.random)) {
                Direction direction = JigsawBlock.getFrontFacing(jigsawBlock.state());
                BlockPos jigsawBlockPos = jigsawBlock.pos();
                BlockPos jigsawBlockTargetPos = jigsawBlockPos.relative(direction);
                ResourceLocation jigsawBlockPool = new ResourceLocation(jigsawBlock.nbt().getString("pool"));
                Optional<StructureTemplatePool> poolOptional = this.poolRegistry.getOptional(jigsawBlockPool);
                if (poolOptional.isPresent() && (((StructureTemplatePool) poolOptional.get()).size() != 0 || Objects.equals(jigsawBlockPool, Pools.EMPTY.location()))) {
                    Holder<StructureTemplatePool> jigsawBlockFallback = ((StructureTemplatePool) poolOptional.get()).getFallback();
                    boolean isTargetInsideCurrentPiece = pieceBoundingBox.isInside(jigsawBlockTargetPos);
                    int targetPieceBoundsTop;
                    MutableObject<BoxOctree> octreeToUse;
                    if (isTargetInsideCurrentPiece) {
                        octreeToUse = parentOctree;
                        targetPieceBoundsTop = pieceMinY;
                        if (parentOctree.getValue() == null) {
                            parentOctree.setValue(new BoxOctree(AABB.of(pieceBoundingBox)));
                        }
                    } else {
                        octreeToUse = boxOctree;
                        targetPieceBoundsTop = minY;
                    }
                    if (depth != this.maxDepth) {
                        StructurePoolElement generatedPiece = this.processList(new ArrayList(((StructurePoolAccessor) poolOptional.get()).integratedapi_getRawTemplates()), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, octreeToUse, piece, depth, targetPieceBoundsTop, heightLimitView, false);
                        if (generatedPiece != null) {
                            continue;
                        }
                    }
                    boolean ignoreBounds = false;
                    if (this.poolsThatIgnoreBounds != null) {
                        ResourceLocation fallBackPoolRL = this.poolRegistry.getKey(jigsawBlockFallback.value());
                        ignoreBounds = this.poolsThatIgnoreBounds.contains(fallBackPoolRL);
                    }
                    this.processList(new ArrayList(((StructurePoolAccessor) jigsawBlockFallback.value()).integratedapi_getRawTemplates()), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, octreeToUse, piece, depth, targetPieceBoundsTop, heightLimitView, ignoreBounds);
                } else {
                    IntegratedAPI.LOGGER.warn("Integrated API: Empty or nonexistent pool: {} which is being called from {}", jigsawBlockPool, pieceBlueprint instanceof SinglePoolElement ? ((SinglePoolElementAccessor) pieceBlueprint).integratedapi_getTemplate().left().get() : "not a SinglePoolElement class");
                }
            }
        }

        private StructurePoolElement processList(List<Pair<StructurePoolElement, Integer>> candidatePieces, boolean doBoundaryAdjustments, StructureTemplate.StructureBlockInfo jigsawBlock, BlockPos jigsawBlockTargetPos, int pieceMinY, BlockPos jigsawBlockPos, MutableObject<BoxOctree> boxOctreeMutableObject, PoolElementStructurePiece piece, int depth, int targetPieceBoundsTop, LevelHeightAccessor heightLimitView, boolean ignoreBounds) {
            StructureTemplatePool.Projection piecePlacementBehavior = piece.getElement().getProjection();
            boolean isPieceRigid = piecePlacementBehavior == StructureTemplatePool.Projection.RIGID;
            int jigsawBlockRelativeY = jigsawBlockPos.m_123342_() - pieceMinY;
            int surfaceHeight = -1;
            int totalCount = candidatePieces.stream().mapToInt(Pair::getSecond).reduce(0, Integer::sum);
            while (candidatePieces.size() > 0) {
                Pair<StructurePoolElement, Integer> chosenPiecePair = null;
                Optional<ResourceLocation> pieceNeededToSpawn = this.requiredPieces.keySet().stream().filter(key -> {
                    int currentCount = (Integer) this.currentPieceCounts.get(key);
                    StructurePieceCountsManager.RequiredPieceNeeds requiredPieceNeeds = (StructurePieceCountsManager.RequiredPieceNeeds) this.requiredPieces.get(key);
                    int requireCount = requiredPieceNeeds == null ? 0 : requiredPieceNeeds.getRequiredAmount();
                    return currentCount < requireCount;
                }).findFirst();
                if (pieceNeededToSpawn.isPresent()) {
                    for (int i = 0; i < candidatePieces.size(); i++) {
                        Pair<StructurePoolElement, Integer> candidatePiecePair = (Pair<StructurePoolElement, Integer>) candidatePieces.get(i);
                        StructurePoolElement candidatePiece = (StructurePoolElement) candidatePiecePair.getFirst();
                        if (candidatePiece instanceof SinglePoolElement && ((ResourceLocation) ((SinglePoolElementAccessor) candidatePiece).integratedapi_getTemplate().left().get()).equals(pieceNeededToSpawn.get())) {
                            if (depth >= Math.min(this.maxDepth - 1, ((StructurePieceCountsManager.RequiredPieceNeeds) this.requiredPieces.get(pieceNeededToSpawn.get())).getMinDistanceFromCenter())) {
                                chosenPiecePair = candidatePiecePair;
                            } else {
                                totalCount -= candidatePiecePair.getSecond();
                                candidatePieces.remove(candidatePiecePair);
                            }
                            break;
                        }
                    }
                }
                if (chosenPiecePair == null) {
                    int chosenWeight = this.random.nextInt(totalCount) + 1;
                    for (Pair<StructurePoolElement, Integer> candidate : candidatePieces) {
                        chosenWeight -= candidate.getSecond();
                        if (chosenWeight <= 0) {
                            chosenPiecePair = candidate;
                            break;
                        }
                    }
                }
                StructurePoolElement candidatePiece = (StructurePoolElement) chosenPiecePair.getFirst();
                if (candidatePiece == EmptyPoolElement.INSTANCE) {
                    return null;
                }
                ResourceLocation pieceName = null;
                if (candidatePiece instanceof SinglePoolElement) {
                    pieceName = (ResourceLocation) ((SinglePoolElementAccessor) candidatePiece).integratedapi_getTemplate().left().get();
                    if (this.currentPieceCounts.containsKey(pieceName) && this.maximumPieceCounts.containsKey(pieceName) && (Integer) this.currentPieceCounts.get(pieceName) >= (Integer) this.maximumPieceCounts.get(pieceName)) {
                        totalCount -= chosenPiecePair.getSecond();
                        candidatePieces.remove(chosenPiecePair);
                        continue;
                    }
                }
                for (Rotation rotation : Rotation.getShuffled(this.random)) {
                    List<StructureTemplate.StructureBlockInfo> candidateJigsawBlocks = candidatePiece.getShuffledJigsawBlocks(this.context.structureTemplateManager(), BlockPos.ZERO, rotation, this.random);
                    BoundingBox tempCandidateBoundingBox = candidatePiece.getBoundingBox(this.context.structureTemplateManager(), BlockPos.ZERO, rotation);
                    int candidateHeightAdjustments;
                    if (doBoundaryAdjustments && tempCandidateBoundingBox.getYSpan() <= 16) {
                        candidateHeightAdjustments = candidateJigsawBlocks.stream().mapToInt(pieceCandidateJigsawBlock -> {
                            if (!tempCandidateBoundingBox.isInside(pieceCandidateJigsawBlock.pos().relative(JigsawBlock.getFrontFacing(pieceCandidateJigsawBlock.state())))) {
                                return 0;
                            } else {
                                ResourceLocation candidateTargetPool = new ResourceLocation(pieceCandidateJigsawBlock.nbt().getString("pool"));
                                Optional<StructureTemplatePool> candidateTargetPoolOptional = this.poolRegistry.getOptional(candidateTargetPool);
                                if (candidateTargetPoolOptional.isEmpty()) {
                                    IntegratedAPI.LOGGER.warn("Integrated API: Non-existent child pool attempted to be spawned: {} which is being called from {}. Let Integrated API dev (CraisinLord) know about this log entry.", candidateTargetPool, candidatePiece instanceof SinglePoolElement ? ((SinglePoolElementAccessor) candidatePiece).integratedapi_getTemplate().left().get() : "not a SinglePoolElement class");
                                }
                                int tallestCandidateTargetFallbackPieceHeight = (Integer) candidateTargetPoolOptional.map(c -> c.getFallback().value().getMaxSize(this.context.structureTemplateManager())).orElse(0);
                                int tallestCandidateTargetPoolPieceHeight = (Integer) candidateTargetPoolOptional.map(c -> c.getMaxSize(this.context.structureTemplateManager())).orElse(0);
                                return Math.max(tallestCandidateTargetPoolPieceHeight, tallestCandidateTargetFallbackPieceHeight);
                            }
                        }).max().orElse(0);
                    } else {
                        candidateHeightAdjustments = 0;
                    }
                    for (StructureTemplate.StructureBlockInfo candidateJigsawBlock : candidateJigsawBlocks) {
                        if (GeneralUtils.canJigsawsAttach(jigsawBlock, candidateJigsawBlock)) {
                            BlockPos candidateJigsawBlockPos = candidateJigsawBlock.pos();
                            BlockPos candidateJigsawBlockRelativePos = new BlockPos(jigsawBlockTargetPos.m_123341_() - candidateJigsawBlockPos.m_123341_(), jigsawBlockTargetPos.m_123342_() - candidateJigsawBlockPos.m_123342_(), jigsawBlockTargetPos.m_123343_() - candidateJigsawBlockPos.m_123343_());
                            BoundingBox candidateBoundingBox = candidatePiece.getBoundingBox(this.context.structureTemplateManager(), candidateJigsawBlockRelativePos, rotation);
                            StructureTemplatePool.Projection candidatePlacementBehavior = candidatePiece.getProjection();
                            boolean isCandidateRigid = candidatePlacementBehavior == StructureTemplatePool.Projection.RIGID;
                            int candidateJigsawBlockRelativeY = candidateJigsawBlockPos.m_123342_();
                            int candidateJigsawYOffsetNeeded = jigsawBlockRelativeY - candidateJigsawBlockRelativeY + JigsawBlock.getFrontFacing(jigsawBlock.state()).getStepY();
                            int adjustedCandidatePieceMinY;
                            if (isPieceRigid && isCandidateRigid) {
                                adjustedCandidatePieceMinY = pieceMinY + candidateJigsawYOffsetNeeded;
                            } else {
                                if (surfaceHeight == -1) {
                                    surfaceHeight = this.context.chunkGenerator().getFirstFreeHeight(jigsawBlockPos.m_123341_(), jigsawBlockPos.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, this.context.randomState());
                                }
                                adjustedCandidatePieceMinY = surfaceHeight - candidateJigsawBlockRelativeY;
                            }
                            int candidatePieceYOffsetNeeded = adjustedCandidatePieceMinY - candidateBoundingBox.minY();
                            BoundingBox adjustedCandidateBoundingBox = candidateBoundingBox.moved(0, candidatePieceYOffsetNeeded, 0);
                            BlockPos adjustedCandidateJigsawBlockRelativePos = candidateJigsawBlockRelativePos.offset(0, candidatePieceYOffsetNeeded, 0);
                            if (candidateHeightAdjustments > 0) {
                                int k2 = Math.max(candidateHeightAdjustments + 1, adjustedCandidateBoundingBox.maxY() - adjustedCandidateBoundingBox.minY());
                                adjustedCandidateBoundingBox.encapsulate(new BlockPos(adjustedCandidateBoundingBox.minX(), adjustedCandidateBoundingBox.minY() + k2, adjustedCandidateBoundingBox.minZ()));
                            }
                            if (adjustedCandidateBoundingBox.maxY() <= this.maxY && adjustedCandidateBoundingBox.minY() >= this.minY) {
                                AABB axisAlignedBB = AABB.of(adjustedCandidateBoundingBox);
                                AABB axisAlignedBBDeflated = axisAlignedBB.deflate(0.25);
                                boolean validBounds = false;
                                if (ignoreBounds || ((BoxOctree) boxOctreeMutableObject.getValue()).boundaryContains(axisAlignedBBDeflated) && !((BoxOctree) boxOctreeMutableObject.getValue()).intersectsAnyBox(axisAlignedBBDeflated)) {
                                    ((BoxOctree) boxOctreeMutableObject.getValue()).addBox(axisAlignedBB);
                                    validBounds = true;
                                }
                                if (validBounds) {
                                    int newPieceGroundLevelDelta = piece.getGroundLevelDelta();
                                    int groundLevelDelta;
                                    if (isCandidateRigid) {
                                        groundLevelDelta = newPieceGroundLevelDelta - candidateJigsawYOffsetNeeded;
                                    } else {
                                        groundLevelDelta = candidatePiece.getGroundLevelDelta();
                                    }
                                    PoolElementStructurePiece newPiece = new PoolElementStructurePiece(this.context.structureTemplateManager(), candidatePiece, adjustedCandidateJigsawBlockRelativePos, groundLevelDelta, rotation, adjustedCandidateBoundingBox);
                                    int candidateJigsawBlockY;
                                    if (isPieceRigid) {
                                        candidateJigsawBlockY = pieceMinY + jigsawBlockRelativeY;
                                    } else if (isCandidateRigid) {
                                        candidateJigsawBlockY = adjustedCandidatePieceMinY + candidateJigsawBlockRelativeY;
                                    } else {
                                        if (surfaceHeight == -1) {
                                            surfaceHeight = this.context.chunkGenerator().getFirstFreeHeight(jigsawBlockPos.m_123341_(), jigsawBlockPos.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, this.context.randomState());
                                        }
                                        candidateJigsawBlockY = surfaceHeight + candidateJigsawYOffsetNeeded / 2;
                                    }
                                    piece.addJunction(new JigsawJunction(jigsawBlockTargetPos.m_123341_(), candidateJigsawBlockY - jigsawBlockRelativeY + newPieceGroundLevelDelta, jigsawBlockTargetPos.m_123343_(), candidateJigsawYOffsetNeeded, candidatePlacementBehavior));
                                    newPiece.addJunction(new JigsawJunction(jigsawBlockPos.m_123341_(), candidateJigsawBlockY - candidateJigsawBlockRelativeY + groundLevelDelta, jigsawBlockPos.m_123343_(), -candidateJigsawYOffsetNeeded, piecePlacementBehavior));
                                    this.structurePieces.add(newPiece);
                                    if (depth + 1 <= this.maxDepth) {
                                        this.availablePieces.addLast(new PieceLimitedJigsawManager.Entry(newPiece, boxOctreeMutableObject, targetPieceBoundsTop, depth + 1));
                                    }
                                    if (pieceName != null && this.currentPieceCounts.containsKey(pieceName)) {
                                        this.currentPieceCounts.put(pieceName, (Integer) this.currentPieceCounts.get(pieceName) + 1);
                                    }
                                    return candidatePiece;
                                }
                            }
                        }
                    }
                }
                totalCount -= chosenPiecePair.getSecond();
                candidatePieces.remove(chosenPiecePair);
            }
            return null;
        }
    }

    public static record Entry(PoolElementStructurePiece piece, MutableObject<BoxOctree> boxOctreeMutableObject, int topYLimit, int depth) {
    }
}