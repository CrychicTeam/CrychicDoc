package com.rekindled.embers.worldgen;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rekindled.embers.RegistryManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

public class CaveStructure extends Structure {

    public static final Codec<CaveStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec(instance -> instance.group(m_226567_(instance), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName), Codec.intRange(0, 7).fieldOf("size").forGetter(structure -> structure.maxDepth), HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight), HeightProvider.CODEC.optionalFieldOf("max_height", ConstantHeight.of(VerticalAnchor.top())).forGetter(structure -> structure.maxHeight), Codec.BOOL.fieldOf("use_expansion_hack").forGetter(structure -> structure.useExpansionHack), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)).apply(instance, CaveStructure::new)), CaveStructure::verifyRange).codec();

    private final Holder<StructureTemplatePool> startPool;

    private final Optional<ResourceLocation> startJigsawName;

    private final int maxDepth;

    private final HeightProvider startHeight;

    private final HeightProvider maxHeight;

    private final boolean useExpansionHack;

    private final Optional<Heightmap.Types> projectStartToHeightmap;

    private final int maxDistanceFromCenter;

    static final Logger LOGGER = LogUtils.getLogger();

    public static DataResult<CaveStructure> verifyRange(CaveStructure structure) {
        return structure.maxDistanceFromCenter + switch(structure.m_226620_()) {
            case NONE ->
                0;
            case BURY, BEARD_THIN, BEARD_BOX ->
                12;
        } > 128 ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128") : DataResult.success(structure);
    }

    protected CaveStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, HeightProvider maxHeight, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.maxHeight = maxHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    public CaveStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, HeightProvider maxHeight, boolean useExpansionHack, Heightmap.Types projectStartToHeightmap) {
        this(settings, startPool, Optional.empty(), maxDepth, startHeight, maxHeight, useExpansionHack, Optional.of(projectStartToHeightmap), 80);
    }

    public CaveStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, HeightProvider maxHeight, boolean useExpansionHack) {
        this(settings, startPool, Optional.empty(), maxDepth, startHeight, maxHeight, useExpansionHack, Optional.empty(), 80);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkpos = context.chunkPos();
        int i = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        int max = this.maxHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        return addPieces(context, this.startPool, this.startJigsawName, this.maxDepth, blockpos, max, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> type() {
        return RegistryManager.CAVE_STRUCTURE.get();
    }

    public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext pContext, Holder<StructureTemplatePool> pStartPool, Optional<ResourceLocation> pStartJigsawName, int pMaxDepth, BlockPos pPos, int maxHeight, boolean pUseExpansionHack, Optional<Heightmap.Types> pProjectStartToHeightmap, int pMaxDistanceFromCenter) {
        RegistryAccess registryaccess = pContext.registryAccess();
        ChunkGenerator chunkgenerator = pContext.chunkGenerator();
        StructureTemplateManager structuretemplatemanager = pContext.structureTemplateManager();
        LevelHeightAccessor levelheightaccessor = pContext.heightAccessor();
        WorldgenRandom worldgenrandom = pContext.random();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = pStartPool.value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            BlockPos blockpos;
            if (pStartJigsawName.isPresent()) {
                ResourceLocation resourcelocation = (ResourceLocation) pStartJigsawName.get();
                Optional<BlockPos> optional = JigsawPlacement.getRandomNamedJigsaw(structurepoolelement, resourcelocation, pPos, rotation, structuretemplatemanager, worldgenrandom);
                if (optional.isEmpty()) {
                    LOGGER.error("No starting jigsaw {} found in start pool {}", resourcelocation, pStartPool.unwrapKey().map(p_248484_ -> p_248484_.location().toString()).orElse("<unregistered>"));
                    return Optional.empty();
                }
                blockpos = (BlockPos) optional.get();
            } else {
                blockpos = pPos;
            }
            Vec3i vec3i = blockpos.subtract(pPos);
            BlockPos blockpos1 = pPos.subtract(vec3i);
            PoolElementStructurePiece poolelementstructurepiece = new CaveStructurePiece(structuretemplatemanager, structurepoolelement, blockpos1, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation), pContext, maxHeight);
            BoundingBox boundingbox = poolelementstructurepiece.m_73547_();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;
            if (pProjectStartToHeightmap.isPresent()) {
                k = pPos.m_123342_() + chunkgenerator.getFirstFreeHeight(i, j, (Heightmap.Types) pProjectStartToHeightmap.get(), levelheightaccessor, pContext.randomState());
            } else {
                k = blockpos1.m_123342_();
            }
            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            int i1 = k + vec3i.getY();
            return Optional.of(new Structure.GenerationStub(new BlockPos(i, i1, j), (Consumer<StructurePiecesBuilder>) (p_227237_ -> {
                List<PoolElementStructurePiece> list = Lists.newArrayList();
                list.add(poolelementstructurepiece);
                if (pMaxDepth > 0) {
                    AABB aabb = new AABB((double) (i - pMaxDistanceFromCenter), (double) (i1 - pMaxDistanceFromCenter), (double) (j - pMaxDistanceFromCenter), (double) (i + pMaxDistanceFromCenter + 1), (double) (i1 + pMaxDistanceFromCenter + 1), (double) (j + pMaxDistanceFromCenter + 1));
                    VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
                    JigsawPlacement.addPieces(pContext.randomState(), pMaxDepth, pUseExpansionHack, chunkgenerator, structuretemplatemanager, levelheightaccessor, worldgenrandom, registry, poolelementstructurepiece, list, voxelshape);
                    list.forEach(p_227237_::m_142679_);
                }
            })));
        }
    }
}