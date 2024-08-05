package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.IntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class MineshaftStructure extends Structure {

    public static final Codec<MineshaftStructure> CODEC = RecordCodecBuilder.create(p_227971_ -> p_227971_.group(m_226567_(p_227971_), MineshaftStructure.Type.CODEC.fieldOf("mineshaft_type").forGetter(p_227969_ -> p_227969_.type)).apply(p_227971_, MineshaftStructure::new));

    private final MineshaftStructure.Type type;

    public MineshaftStructure(Structure.StructureSettings structureStructureSettings0, MineshaftStructure.Type mineshaftStructureType1) {
        super(structureStructureSettings0);
        this.type = mineshaftStructureType1;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        structureGenerationContext0.random().m_188500_();
        ChunkPos $$1 = structureGenerationContext0.chunkPos();
        BlockPos $$2 = new BlockPos($$1.getMiddleBlockX(), 50, $$1.getMinBlockZ());
        StructurePiecesBuilder $$3 = new StructurePiecesBuilder();
        int $$4 = this.generatePiecesAndAdjust($$3, structureGenerationContext0);
        return Optional.of(new Structure.GenerationStub($$2.offset(0, $$4, 0), Either.right($$3)));
    }

    private int generatePiecesAndAdjust(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        ChunkPos $$2 = structureGenerationContext1.chunkPos();
        WorldgenRandom $$3 = structureGenerationContext1.random();
        ChunkGenerator $$4 = structureGenerationContext1.chunkGenerator();
        MineshaftPieces.MineShaftRoom $$5 = new MineshaftPieces.MineShaftRoom(0, $$3, $$2.getBlockX(2), $$2.getBlockZ(2), this.type);
        structurePiecesBuilder0.addPiece($$5);
        $$5.addChildren($$5, structurePiecesBuilder0, $$3);
        int $$6 = $$4.getSeaLevel();
        if (this.type == MineshaftStructure.Type.MESA) {
            BlockPos $$7 = structurePiecesBuilder0.getBoundingBox().getCenter();
            int $$8 = $$4.getBaseHeight($$7.m_123341_(), $$7.m_123343_(), Heightmap.Types.WORLD_SURFACE_WG, structureGenerationContext1.heightAccessor(), structureGenerationContext1.randomState());
            int $$9 = $$8 <= $$6 ? $$6 : Mth.randomBetweenInclusive($$3, $$6, $$8);
            int $$10 = $$9 - $$7.m_123342_();
            structurePiecesBuilder0.offsetPiecesVertically($$10);
            return $$10;
        } else {
            return structurePiecesBuilder0.moveBelowSeaLevel($$6, $$4.getMinY(), $$3, 10);
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureType.MINESHAFT;
    }

    public static enum Type implements StringRepresentable {

        NORMAL("normal", Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.OAK_FENCE), MESA("mesa", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE);

        public static final Codec<MineshaftStructure.Type> CODEC = StringRepresentable.fromEnum(MineshaftStructure.Type::values);

        private static final IntFunction<MineshaftStructure.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private final String name;

        private final BlockState woodState;

        private final BlockState planksState;

        private final BlockState fenceState;

        private Type(String p_227985_, Block p_227986_, Block p_227987_, Block p_227988_) {
            this.name = p_227985_;
            this.woodState = p_227986_.defaultBlockState();
            this.planksState = p_227987_.defaultBlockState();
            this.fenceState = p_227988_.defaultBlockState();
        }

        public String getName() {
            return this.name;
        }

        public static MineshaftStructure.Type byId(int p_227991_) {
            return (MineshaftStructure.Type) BY_ID.apply(p_227991_);
        }

        public BlockState getWoodState() {
            return this.woodState;
        }

        public BlockState getPlanksState() {
            return this.planksState;
        }

        public BlockState getFenceState() {
            return this.fenceState;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}