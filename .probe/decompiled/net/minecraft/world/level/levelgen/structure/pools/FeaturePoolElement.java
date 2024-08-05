package net.minecraft.world.level.levelgen.structure.pools;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class FeaturePoolElement extends StructurePoolElement {

    public static final Codec<FeaturePoolElement> CODEC = RecordCodecBuilder.create(p_210213_ -> p_210213_.group(PlacedFeature.CODEC.fieldOf("feature").forGetter(p_210215_ -> p_210215_.feature), m_210538_()).apply(p_210213_, FeaturePoolElement::new));

    private final Holder<PlacedFeature> feature;

    private final CompoundTag defaultJigsawNBT;

    protected FeaturePoolElement(Holder<PlacedFeature> holderPlacedFeature0, StructureTemplatePool.Projection structureTemplatePoolProjection1) {
        super(structureTemplatePoolProjection1);
        this.feature = holderPlacedFeature0;
        this.defaultJigsawNBT = this.fillDefaultJigsawNBT();
    }

    private CompoundTag fillDefaultJigsawNBT() {
        CompoundTag $$0 = new CompoundTag();
        $$0.putString("name", "minecraft:bottom");
        $$0.putString("final_state", "minecraft:air");
        $$0.putString("pool", "minecraft:empty");
        $$0.putString("target", "minecraft:empty");
        $$0.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
        return $$0;
    }

    @Override
    public Vec3i getSize(StructureTemplateManager structureTemplateManager0, Rotation rotation1) {
        return Vec3i.ZERO;
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, RandomSource randomSource3) {
        List<StructureTemplate.StructureBlockInfo> $$4 = Lists.newArrayList();
        $$4.add(new StructureTemplate.StructureBlockInfo(blockPos1, (BlockState) Blocks.JIGSAW.defaultBlockState().m_61124_(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT));
        return $$4;
    }

    @Override
    public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2) {
        Vec3i $$3 = this.getSize(structureTemplateManager0, rotation2);
        return new BoundingBox(blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_(), blockPos1.m_123341_() + $$3.getX(), blockPos1.m_123342_() + $$3.getY(), blockPos1.m_123343_() + $$3.getZ());
    }

    @Override
    public boolean place(StructureTemplateManager structureTemplateManager0, WorldGenLevel worldGenLevel1, StructureManager structureManager2, ChunkGenerator chunkGenerator3, BlockPos blockPos4, BlockPos blockPos5, Rotation rotation6, BoundingBox boundingBox7, RandomSource randomSource8, boolean boolean9) {
        return this.feature.value().place(worldGenLevel1, chunkGenerator3, randomSource8, blockPos4);
    }

    @Override
    public StructurePoolElementType<?> getType() {
        return StructurePoolElementType.FEATURE;
    }

    public String toString() {
        return "Feature[" + this.feature + "]";
    }
}