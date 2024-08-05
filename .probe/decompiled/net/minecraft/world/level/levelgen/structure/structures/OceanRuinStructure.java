package net.minecraft.world.level.levelgen.structure.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class OceanRuinStructure extends Structure {

    public static final Codec<OceanRuinStructure> CODEC = RecordCodecBuilder.create(p_229075_ -> p_229075_.group(m_226567_(p_229075_), OceanRuinStructure.Type.CODEC.fieldOf("biome_temp").forGetter(p_229079_ -> p_229079_.biomeTemp), Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter(p_229077_ -> p_229077_.largeProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter(p_229073_ -> p_229073_.clusterProbability)).apply(p_229075_, OceanRuinStructure::new));

    public final OceanRuinStructure.Type biomeTemp;

    public final float largeProbability;

    public final float clusterProbability;

    public OceanRuinStructure(Structure.StructureSettings structureStructureSettings0, OceanRuinStructure.Type oceanRuinStructureType1, float float2, float float3) {
        super(structureStructureSettings0);
        this.biomeTemp = oceanRuinStructureType1;
        this.largeProbability = float2;
        this.clusterProbability = float3;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext structureGenerationContext0) {
        return m_226585_(structureGenerationContext0, Heightmap.Types.OCEAN_FLOOR_WG, p_229068_ -> this.generatePieces(p_229068_, structureGenerationContext0));
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder0, Structure.GenerationContext structureGenerationContext1) {
        BlockPos $$2 = new BlockPos(structureGenerationContext1.chunkPos().getMinBlockX(), 90, structureGenerationContext1.chunkPos().getMinBlockZ());
        Rotation $$3 = Rotation.getRandom(structureGenerationContext1.random());
        OceanRuinPieces.addPieces(structureGenerationContext1.structureTemplateManager(), $$2, $$3, structurePiecesBuilder0, structureGenerationContext1.random(), this);
    }

    @Override
    public StructureType<?> type() {
        return StructureType.OCEAN_RUIN;
    }

    public static enum Type implements StringRepresentable {

        WARM("warm"), COLD("cold");

        public static final Codec<OceanRuinStructure.Type> CODEC = StringRepresentable.fromEnum(OceanRuinStructure.Type::values);

        private final String name;

        private Type(String p_229090_) {
            this.name = p_229090_;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}