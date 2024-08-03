package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class StructurePlaceSettings {

    private Mirror mirror = Mirror.NONE;

    private Rotation rotation = Rotation.NONE;

    private BlockPos rotationPivot = BlockPos.ZERO;

    private boolean ignoreEntities;

    @Nullable
    private BoundingBox boundingBox;

    private boolean keepLiquids = true;

    @Nullable
    private RandomSource random;

    private int palette;

    private final List<StructureProcessor> processors = Lists.newArrayList();

    private boolean knownShape;

    private boolean finalizeEntities;

    public StructurePlaceSettings copy() {
        StructurePlaceSettings $$0 = new StructurePlaceSettings();
        $$0.mirror = this.mirror;
        $$0.rotation = this.rotation;
        $$0.rotationPivot = this.rotationPivot;
        $$0.ignoreEntities = this.ignoreEntities;
        $$0.boundingBox = this.boundingBox;
        $$0.keepLiquids = this.keepLiquids;
        $$0.random = this.random;
        $$0.palette = this.palette;
        $$0.processors.addAll(this.processors);
        $$0.knownShape = this.knownShape;
        $$0.finalizeEntities = this.finalizeEntities;
        return $$0;
    }

    public StructurePlaceSettings setMirror(Mirror mirror0) {
        this.mirror = mirror0;
        return this;
    }

    public StructurePlaceSettings setRotation(Rotation rotation0) {
        this.rotation = rotation0;
        return this;
    }

    public StructurePlaceSettings setRotationPivot(BlockPos blockPos0) {
        this.rotationPivot = blockPos0;
        return this;
    }

    public StructurePlaceSettings setIgnoreEntities(boolean boolean0) {
        this.ignoreEntities = boolean0;
        return this;
    }

    public StructurePlaceSettings setBoundingBox(BoundingBox boundingBox0) {
        this.boundingBox = boundingBox0;
        return this;
    }

    public StructurePlaceSettings setRandom(@Nullable RandomSource randomSource0) {
        this.random = randomSource0;
        return this;
    }

    public StructurePlaceSettings setKeepLiquids(boolean boolean0) {
        this.keepLiquids = boolean0;
        return this;
    }

    public StructurePlaceSettings setKnownShape(boolean boolean0) {
        this.knownShape = boolean0;
        return this;
    }

    public StructurePlaceSettings clearProcessors() {
        this.processors.clear();
        return this;
    }

    public StructurePlaceSettings addProcessor(StructureProcessor structureProcessor0) {
        this.processors.add(structureProcessor0);
        return this;
    }

    public StructurePlaceSettings popProcessor(StructureProcessor structureProcessor0) {
        this.processors.remove(structureProcessor0);
        return this;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public BlockPos getRotationPivot() {
        return this.rotationPivot;
    }

    public RandomSource getRandom(@Nullable BlockPos blockPos0) {
        if (this.random != null) {
            return this.random;
        } else {
            return blockPos0 == null ? RandomSource.create(Util.getMillis()) : RandomSource.create(Mth.getSeed(blockPos0));
        }
    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    @Nullable
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public boolean getKnownShape() {
        return this.knownShape;
    }

    public List<StructureProcessor> getProcessors() {
        return this.processors;
    }

    public boolean shouldKeepLiquids() {
        return this.keepLiquids;
    }

    public StructureTemplate.Palette getRandomPalette(List<StructureTemplate.Palette> listStructureTemplatePalette0, @Nullable BlockPos blockPos1) {
        int $$2 = listStructureTemplatePalette0.size();
        if ($$2 == 0) {
            throw new IllegalStateException("No palettes");
        } else {
            return (StructureTemplate.Palette) listStructureTemplatePalette0.get(this.getRandom(blockPos1).nextInt($$2));
        }
    }

    public StructurePlaceSettings setFinalizeEntities(boolean boolean0) {
        this.finalizeEntities = boolean0;
        return this;
    }

    public boolean shouldFinalizeEntities() {
        return this.finalizeEntities;
    }
}