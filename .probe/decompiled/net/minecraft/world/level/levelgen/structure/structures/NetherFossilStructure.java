package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class NetherFossilStructure extends Structure {

    public static final Codec<NetherFossilStructure> CODEC = RecordCodecBuilder.create(p_228585_ -> p_228585_.group(m_226567_(p_228585_), HeightProvider.CODEC.fieldOf("height").forGetter(p_228583_ -> p_228583_.height)).apply(p_228585_, NetherFossilStructure::new));

    public final HeightProvider height;

    public NetherFossilStructure(Structure.StructureSettings structureStructureSettings0, HeightProvider heightProvider1) {
        super(structureStructureSettings0);
        this.height = heightProvider1;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        WorldgenRandom $$1 = structureGenerationContext0.random();
        int $$2 = structureGenerationContext0.chunkPos().getMinBlockX() + $$1.m_188503_(16);
        int $$3 = structureGenerationContext0.chunkPos().getMinBlockZ() + $$1.m_188503_(16);
        int $$4 = structureGenerationContext0.chunkGenerator().getSeaLevel();
        WorldGenerationContext $$5 = new WorldGenerationContext(structureGenerationContext0.chunkGenerator(), structureGenerationContext0.heightAccessor());
        int $$6 = this.height.sample($$1, $$5);
        NoiseColumn $$7 = structureGenerationContext0.chunkGenerator().getBaseColumn($$2, $$3, structureGenerationContext0.heightAccessor(), structureGenerationContext0.randomState());
        BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos($$2, $$6, $$3);
        while ($$6 > $$4) {
            BlockState $$9 = $$7.getBlock($$6);
            BlockState $$10 = $$7.getBlock(--$$6);
            if ($$9.m_60795_() && ($$10.m_60713_(Blocks.SOUL_SAND) || $$10.m_60783_(EmptyBlockGetter.INSTANCE, $$8.setY($$6), Direction.UP))) {
                break;
            }
        }
        if ($$6 <= $$4) {
            return Optional.empty();
        } else {
            BlockPos $$11 = new BlockPos($$2, $$6, $$3);
            return Optional.of(new Structure.GenerationStub($$11, (Consumer<StructurePiecesBuilder>) (p_228581_ -> NetherFossilPieces.addPieces(structureGenerationContext0.structureTemplateManager(), p_228581_, $$1, $$11))));
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureType.NETHER_FOSSIL;
    }
}