package net.minecraft.world.level.levelgen.structure.pools;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class ListPoolElement extends StructurePoolElement {

    public static final Codec<ListPoolElement> CODEC = RecordCodecBuilder.create(p_210367_ -> p_210367_.group(StructurePoolElement.CODEC.listOf().fieldOf("elements").forGetter(p_210369_ -> p_210369_.elements), m_210538_()).apply(p_210367_, ListPoolElement::new));

    private final List<StructurePoolElement> elements;

    public ListPoolElement(List<StructurePoolElement> listStructurePoolElement0, StructureTemplatePool.Projection structureTemplatePoolProjection1) {
        super(structureTemplatePoolProjection1);
        if (listStructurePoolElement0.isEmpty()) {
            throw new IllegalArgumentException("Elements are empty");
        } else {
            this.elements = listStructurePoolElement0;
            this.setProjectionOnEachElement(structureTemplatePoolProjection1);
        }
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager0, Rotation rotation1) {
        int $$2 = 0;
        int $$3 = 0;
        int $$4 = 0;
        for (StructurePoolElement $$5 : this.elements) {
            Vec3i $$6 = $$5.getSize(structureTemplateManager0, rotation1);
            $$2 = Math.max($$2, $$6.getX());
            $$3 = Math.max($$3, $$6.getY());
            $$4 = Math.max($$4, $$6.getZ());
        }
        return new Vec3i($$2, $$3, $$4);
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, RandomSource randomSource3) {
        return ((StructurePoolElement) this.elements.get(0)).getShuffledJigsawBlocks(structureTemplateManager0, blockPos1, rotation2, randomSource3);
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2) {
        Stream<BoundingBox> $$3 = this.elements.stream().filter(p_210371_ -> p_210371_ != EmptyPoolElement.INSTANCE).map(p_227298_ -> p_227298_.getBoundingBox(structureTemplateManager0, blockPos1, rotation2));
        return (BoundingBox) BoundingBox.encapsulatingBoxes($$3::iterator).orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox for ListPoolElement"));
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager0, WorldGenLevel worldGenLevel1, StructureManager structureManager2, ChunkGenerator chunkGenerator3, BlockPos blockPos4, BlockPos blockPos5, Rotation rotation6, BoundingBox boundingBox7, RandomSource randomSource8, boolean boolean9) {
        for (StructurePoolElement $$10 : this.elements) {
            if (!$$10.place(structureTemplateManager0, worldGenLevel1, structureManager2, chunkGenerator3, blockPos4, blockPos5, rotation6, boundingBox7, randomSource8, boolean9)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.LIST;
    }

    @Override
    public StructurePoolElement setProjection(StructureTemplatePool.Projection structureTemplatePoolProjection0) {
        super.setProjection(structureTemplatePoolProjection0);
        this.setProjectionOnEachElement(structureTemplatePoolProjection0);
        return this;
    }

    public String toString() {
        return "List[" + (String) this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
    }

    private void setProjectionOnEachElement(StructureTemplatePool.Projection structureTemplatePoolProjection0) {
        this.elements.forEach(p_210376_ -> p_210376_.setProjection(structureTemplatePoolProjection0));
    }
}