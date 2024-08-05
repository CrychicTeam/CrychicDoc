package net.minecraft.world.level.levelgen.structure.pools;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

public class JigsawPlacement {

    static final Logger LOGGER = LogUtils.getLogger();

    public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext structureGenerationContext0, Holder<StructureTemplatePool> holderStructureTemplatePool1, Optional<ResourceLocation> optionalResourceLocation2, int int3, BlockPos blockPos4, boolean boolean5, Optional<Heightmap.Types> optionalHeightmapTypes6, int int7) {
        RegistryAccess $$8 = structureGenerationContext0.registryAccess();
        ChunkGenerator $$9 = structureGenerationContext0.chunkGenerator();
        StructureTemplateManager $$10 = structureGenerationContext0.structureTemplateManager();
        LevelHeightAccessor $$11 = structureGenerationContext0.heightAccessor();
        WorldgenRandom $$12 = structureGenerationContext0.random();
        Registry<StructureTemplatePool> $$13 = $$8.registryOrThrow(Registries.TEMPLATE_POOL);
        Rotation $$14 = Rotation.getRandom($$12);
        StructureTemplatePool $$15 = holderStructureTemplatePool1.value();
        StructurePoolElement $$16 = $$15.getRandomTemplate($$12);
        if ($$16 == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            BlockPos $$19;
            if (optionalResourceLocation2.isPresent()) {
                ResourceLocation $$17 = (ResourceLocation) optionalResourceLocation2.get();
                Optional<BlockPos> $$18 = getRandomNamedJigsaw($$16, $$17, blockPos4, $$14, $$10, $$12);
                if ($$18.isEmpty()) {
                    LOGGER.error("No starting jigsaw {} found in start pool {}", $$17, holderStructureTemplatePool1.unwrapKey().map(p_248484_ -> p_248484_.location().toString()).orElse("<unregistered>"));
                    return Optional.empty();
                }
                $$19 = (BlockPos) $$18.get();
            } else {
                $$19 = blockPos4;
            }
            Vec3i $$21 = $$19.subtract(blockPos4);
            BlockPos $$22 = blockPos4.subtract($$21);
            PoolElementStructurePiece $$23 = new PoolElementStructurePiece($$10, $$16, $$22, $$16.getGroundLevelDelta(), $$14, $$16.getBoundingBox($$10, $$22, $$14));
            BoundingBox $$24 = $$23.m_73547_();
            int $$25 = ($$24.maxX() + $$24.minX()) / 2;
            int $$26 = ($$24.maxZ() + $$24.minZ()) / 2;
            int $$27;
            if (optionalHeightmapTypes6.isPresent()) {
                $$27 = blockPos4.m_123342_() + $$9.getFirstFreeHeight($$25, $$26, (Heightmap.Types) optionalHeightmapTypes6.get(), $$11, structureGenerationContext0.randomState());
            } else {
                $$27 = $$22.m_123342_();
            }
            int $$29 = $$24.minY() + $$23.getGroundLevelDelta();
            $$23.move(0, $$27 - $$29, 0);
            int $$30 = $$27 + $$21.getY();
            return Optional.of(new Structure.GenerationStub(new BlockPos($$25, $$30, $$26), (Consumer<StructurePiecesBuilder>) (p_227237_ -> {
                List<PoolElementStructurePiece> $$15x = Lists.newArrayList();
                $$15x.add($$23);
                if (int3 > 0) {
                    AABB $$16x = new AABB((double) ($$25 - int7), (double) ($$30 - int7), (double) ($$26 - int7), (double) ($$25 + int7 + 1), (double) ($$30 + int7 + 1), (double) ($$26 + int7 + 1));
                    VoxelShape $$17 = Shapes.join(Shapes.create($$16x), Shapes.create(AABB.of($$24)), BooleanOp.ONLY_FIRST);
                    addPieces(structureGenerationContext0.randomState(), int3, boolean5, $$9, $$10, $$11, $$12, $$13, $$23, $$15x, $$17);
                    $$15x.forEach(p_227237_::m_142679_);
                }
            })));
        }
    }

    private static Optional<BlockPos> getRandomNamedJigsaw(StructurePoolElement structurePoolElement0, ResourceLocation resourceLocation1, BlockPos blockPos2, Rotation rotation3, StructureTemplateManager structureTemplateManager4, WorldgenRandom worldgenRandom5) {
        List<StructureTemplate.StructureBlockInfo> $$6 = structurePoolElement0.getShuffledJigsawBlocks(structureTemplateManager4, blockPos2, rotation3, worldgenRandom5);
        Optional<BlockPos> $$7 = Optional.empty();
        for (StructureTemplate.StructureBlockInfo $$8 : $$6) {
            ResourceLocation $$9 = ResourceLocation.tryParse($$8.nbt().getString("name"));
            if (resourceLocation1.equals($$9)) {
                $$7 = Optional.of($$8.pos());
                break;
            }
        }
        return $$7;
    }

    private static void addPieces(RandomState randomState0, int int1, boolean boolean2, ChunkGenerator chunkGenerator3, StructureTemplateManager structureTemplateManager4, LevelHeightAccessor levelHeightAccessor5, RandomSource randomSource6, Registry<StructureTemplatePool> registryStructureTemplatePool7, PoolElementStructurePiece poolElementStructurePiece8, List<PoolElementStructurePiece> listPoolElementStructurePiece9, VoxelShape voxelShape10) {
        JigsawPlacement.Placer $$11 = new JigsawPlacement.Placer(registryStructureTemplatePool7, int1, chunkGenerator3, structureTemplateManager4, listPoolElementStructurePiece9, randomSource6);
        $$11.placing.addLast(new JigsawPlacement.PieceState(poolElementStructurePiece8, new MutableObject(voxelShape10), 0));
        while (!$$11.placing.isEmpty()) {
            JigsawPlacement.PieceState $$12 = (JigsawPlacement.PieceState) $$11.placing.removeFirst();
            $$11.tryPlacingChildren($$12.piece, $$12.free, $$12.depth, boolean2, levelHeightAccessor5, randomState0);
        }
    }

    public static boolean generateJigsaw(ServerLevel serverLevel0, Holder<StructureTemplatePool> holderStructureTemplatePool1, ResourceLocation resourceLocation2, int int3, BlockPos blockPos4, boolean boolean5) {
        ChunkGenerator $$6 = serverLevel0.getChunkSource().getGenerator();
        StructureTemplateManager $$7 = serverLevel0.getStructureManager();
        StructureManager $$8 = serverLevel0.structureManager();
        RandomSource $$9 = serverLevel0.m_213780_();
        Structure.GenerationContext $$10 = new Structure.GenerationContext(serverLevel0.m_9598_(), $$6, $$6.getBiomeSource(), serverLevel0.getChunkSource().randomState(), $$7, serverLevel0.getSeed(), new ChunkPos(blockPos4), serverLevel0, p_227255_ -> true);
        Optional<Structure.GenerationStub> $$11 = addPieces($$10, holderStructureTemplatePool1, Optional.of(resourceLocation2), int3, blockPos4, false, Optional.empty(), 128);
        if ($$11.isPresent()) {
            StructurePiecesBuilder $$12 = ((Structure.GenerationStub) $$11.get()).getPiecesBuilder();
            for (StructurePiece $$13 : $$12.build().pieces()) {
                if ($$13 instanceof PoolElementStructurePiece $$14) {
                    $$14.place(serverLevel0, $$8, $$6, $$9, BoundingBox.infinite(), blockPos4, boolean5);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    static final class PieceState {

        final PoolElementStructurePiece piece;

        final MutableObject<VoxelShape> free;

        final int depth;

        PieceState(PoolElementStructurePiece poolElementStructurePiece0, MutableObject<VoxelShape> mutableObjectVoxelShape1, int int2) {
            this.piece = poolElementStructurePiece0;
            this.free = mutableObjectVoxelShape1;
            this.depth = int2;
        }
    }

    static final class Placer {

        private final Registry<StructureTemplatePool> pools;

        private final int maxDepth;

        private final ChunkGenerator chunkGenerator;

        private final StructureTemplateManager structureTemplateManager;

        private final List<? super PoolElementStructurePiece> pieces;

        private final RandomSource random;

        final Deque<JigsawPlacement.PieceState> placing = Queues.newArrayDeque();

        Placer(Registry<StructureTemplatePool> registryStructureTemplatePool0, int int1, ChunkGenerator chunkGenerator2, StructureTemplateManager structureTemplateManager3, List<? super PoolElementStructurePiece> listSuperPoolElementStructurePiece4, RandomSource randomSource5) {
            this.pools = registryStructureTemplatePool0;
            this.maxDepth = int1;
            this.chunkGenerator = chunkGenerator2;
            this.structureTemplateManager = structureTemplateManager3;
            this.pieces = listSuperPoolElementStructurePiece4;
            this.random = randomSource5;
        }

        void tryPlacingChildren(PoolElementStructurePiece poolElementStructurePiece0, MutableObject<VoxelShape> mutableObjectVoxelShape1, int int2, boolean boolean3, LevelHeightAccessor levelHeightAccessor4, RandomState randomState5) {
            StructurePoolElement $$6 = poolElementStructurePiece0.getElement();
            BlockPos $$7 = poolElementStructurePiece0.getPosition();
            Rotation $$8 = poolElementStructurePiece0.getRotation();
            StructureTemplatePool.Projection $$9 = $$6.getProjection();
            boolean $$10 = $$9 == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> $$11 = new MutableObject();
            BoundingBox $$12 = poolElementStructurePiece0.m_73547_();
            int $$13 = $$12.minY();
            label129: for (StructureTemplate.StructureBlockInfo $$14 : $$6.getShuffledJigsawBlocks(this.structureTemplateManager, $$7, $$8, this.random)) {
                Direction $$15 = JigsawBlock.getFrontFacing($$14.state());
                BlockPos $$16 = $$14.pos();
                BlockPos $$17 = $$16.relative($$15);
                int $$18 = $$16.m_123342_() - $$13;
                int $$19 = -1;
                ResourceKey<StructureTemplatePool> $$20 = readPoolName($$14);
                Optional<? extends Holder<StructureTemplatePool>> $$21 = this.pools.getHolder($$20);
                if ($$21.isEmpty()) {
                    JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", $$20.location());
                } else {
                    Holder<StructureTemplatePool> $$22 = (Holder<StructureTemplatePool>) $$21.get();
                    if ($$22.value().size() == 0 && !$$22.is(Pools.EMPTY)) {
                        JigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", $$20.location());
                    } else {
                        Holder<StructureTemplatePool> $$23 = $$22.value().getFallback();
                        if ($$23.value().size() == 0 && !$$23.is(Pools.EMPTY)) {
                            JigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", $$23.unwrapKey().map(p_255599_ -> p_255599_.location().toString()).orElse("<unregistered>"));
                        } else {
                            boolean $$24 = $$12.isInside($$17);
                            MutableObject<VoxelShape> $$25;
                            if ($$24) {
                                $$25 = $$11;
                                if ($$11.getValue() == null) {
                                    $$11.setValue(Shapes.create(AABB.of($$12)));
                                }
                            } else {
                                $$25 = mutableObjectVoxelShape1;
                            }
                            List<StructurePoolElement> $$27 = Lists.newArrayList();
                            if (int2 != this.maxDepth) {
                                $$27.addAll($$22.value().getShuffledTemplates(this.random));
                            }
                            $$27.addAll($$23.value().getShuffledTemplates(this.random));
                            for (StructurePoolElement $$28 : $$27) {
                                if ($$28 == EmptyPoolElement.INSTANCE) {
                                    break;
                                }
                                for (Rotation $$29 : Rotation.getShuffled(this.random)) {
                                    List<StructureTemplate.StructureBlockInfo> $$30 = $$28.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, $$29, this.random);
                                    BoundingBox $$31 = $$28.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, $$29);
                                    int $$33;
                                    if (boolean3 && $$31.getYSpan() <= 16) {
                                        $$33 = $$30.stream().mapToInt(p_255598_ -> {
                                            if (!$$31.isInside(p_255598_.pos().relative(JigsawBlock.getFrontFacing(p_255598_.state())))) {
                                                return 0;
                                            } else {
                                                ResourceKey<StructureTemplatePool> $$2 = readPoolName(p_255598_);
                                                Optional<? extends Holder<StructureTemplatePool>> $$3 = this.pools.getHolder($$2);
                                                Optional<Holder<StructureTemplatePool>> $$4 = $$3.map(p_255600_ -> ((StructureTemplatePool) p_255600_.value()).getFallback());
                                                int $$5 = (Integer) $$3.map(p_255596_ -> ((StructureTemplatePool) p_255596_.value()).getMaxSize(this.structureTemplateManager)).orElse(0);
                                                int $$6x = (Integer) $$4.map(p_255601_ -> ((StructureTemplatePool) p_255601_.value()).getMaxSize(this.structureTemplateManager)).orElse(0);
                                                return Math.max($$5, $$6x);
                                            }
                                        }).max().orElse(0);
                                    } else {
                                        $$33 = 0;
                                    }
                                    for (StructureTemplate.StructureBlockInfo $$34 : $$30) {
                                        if (JigsawBlock.canAttach($$14, $$34)) {
                                            BlockPos $$35 = $$34.pos();
                                            BlockPos $$36 = $$17.subtract($$35);
                                            BoundingBox $$37 = $$28.getBoundingBox(this.structureTemplateManager, $$36, $$29);
                                            int $$38 = $$37.minY();
                                            StructureTemplatePool.Projection $$39 = $$28.getProjection();
                                            boolean $$40 = $$39 == StructureTemplatePool.Projection.RIGID;
                                            int $$41 = $$35.m_123342_();
                                            int $$42 = $$18 - $$41 + JigsawBlock.getFrontFacing($$14.state()).getStepY();
                                            int $$43;
                                            if ($$10 && $$40) {
                                                $$43 = $$13 + $$42;
                                            } else {
                                                if ($$19 == -1) {
                                                    $$19 = this.chunkGenerator.getFirstFreeHeight($$16.m_123341_(), $$16.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor4, randomState5);
                                                }
                                                $$43 = $$19 - $$41;
                                            }
                                            int $$45 = $$43 - $$38;
                                            BoundingBox $$46 = $$37.moved(0, $$45, 0);
                                            BlockPos $$47 = $$36.offset(0, $$45, 0);
                                            if ($$33 > 0) {
                                                int $$48 = Math.max($$33 + 1, $$46.maxY() - $$46.minY());
                                                $$46.encapsulate(new BlockPos($$46.minX(), $$46.minY() + $$48, $$46.minZ()));
                                            }
                                            if (!Shapes.joinIsNotEmpty((VoxelShape) $$25.getValue(), Shapes.create(AABB.of($$46).deflate(0.25)), BooleanOp.ONLY_SECOND)) {
                                                $$25.setValue(Shapes.joinUnoptimized((VoxelShape) $$25.getValue(), Shapes.create(AABB.of($$46)), BooleanOp.ONLY_FIRST));
                                                int $$49 = poolElementStructurePiece0.getGroundLevelDelta();
                                                int $$50;
                                                if ($$40) {
                                                    $$50 = $$49 - $$42;
                                                } else {
                                                    $$50 = $$28.getGroundLevelDelta();
                                                }
                                                PoolElementStructurePiece $$52 = new PoolElementStructurePiece(this.structureTemplateManager, $$28, $$47, $$50, $$29, $$46);
                                                int $$53;
                                                if ($$10) {
                                                    $$53 = $$13 + $$18;
                                                } else if ($$40) {
                                                    $$53 = $$43 + $$41;
                                                } else {
                                                    if ($$19 == -1) {
                                                        $$19 = this.chunkGenerator.getFirstFreeHeight($$16.m_123341_(), $$16.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor4, randomState5);
                                                    }
                                                    $$53 = $$19 + $$42 / 2;
                                                }
                                                poolElementStructurePiece0.addJunction(new JigsawJunction($$17.m_123341_(), $$53 - $$18 + $$49, $$17.m_123343_(), $$42, $$39));
                                                $$52.addJunction(new JigsawJunction($$16.m_123341_(), $$53 - $$41 + $$50, $$16.m_123343_(), -$$42, $$9));
                                                this.pieces.add($$52);
                                                if (int2 + 1 <= this.maxDepth) {
                                                    this.placing.addLast(new JigsawPlacement.PieceState($$52, $$25, int2 + 1));
                                                }
                                                continue label129;
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

        private static ResourceKey<StructureTemplatePool> readPoolName(StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo0) {
            return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(structureTemplateStructureBlockInfo0.nbt().getString("pool")));
        }
    }
}