package com.yungnickyoung.minecraft.yungsapi.world.structure.context;

import com.yungnickyoung.minecraft.yungsapi.world.structure.jigsaw.PieceEntry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class StructureContext {

    private final int pieceMinY;

    private final int pieceMaxY;

    private final int depth;

    private final BlockPos pos;

    private final Rotation rotation;

    private final StructureTemplateManager structureTemplateManager;

    private final List<PieceEntry> pieces;

    private final PieceEntry pieceEntry;

    private final RandomSource random;

    private StructureContext(StructureContext.Builder builder) {
        this.pieceMinY = builder.pieceMinY;
        this.pieceMaxY = builder.pieceMaxY;
        this.depth = builder.depth;
        this.pos = builder.pos;
        this.structureTemplateManager = builder.structureTemplateManager;
        this.pieces = builder.pieces;
        this.pieceEntry = builder.pieceEntry;
        this.rotation = builder.rotation;
        this.random = builder.random;
    }

    public int pieceMinY() {
        return this.pieceMinY;
    }

    public int pieceMaxY() {
        return this.pieceMaxY;
    }

    public int depth() {
        return this.depth;
    }

    public BlockPos pos() {
        return this.pos;
    }

    public Rotation rotation() {
        return this.rotation;
    }

    public StructureTemplateManager structureTemplateManager() {
        return this.structureTemplateManager;
    }

    public List<PieceEntry> pieces() {
        return this.pieces;
    }

    public PieceEntry pieceEntry() {
        return this.pieceEntry;
    }

    public RandomSource random() {
        return this.random;
    }

    public static class Builder {

        private int pieceMinY = 0;

        private int pieceMaxY = 0;

        private int depth = 0;

        private BlockPos pos = null;

        private Rotation rotation = null;

        private StructureTemplateManager structureTemplateManager = null;

        private List<PieceEntry> pieces = null;

        private PieceEntry pieceEntry = null;

        private RandomSource random = null;

        public StructureContext.Builder pieceMinY(int pieceMinY) {
            this.pieceMinY = pieceMinY;
            return this;
        }

        public StructureContext.Builder pieceMaxY(int pieceMaxY) {
            this.pieceMaxY = pieceMaxY;
            return this;
        }

        public StructureContext.Builder depth(int depth) {
            this.depth = depth;
            return this;
        }

        public StructureContext.Builder pos(BlockPos pos) {
            this.pos = pos;
            return this;
        }

        public StructureContext.Builder rotation(Rotation rotation) {
            this.rotation = rotation;
            return this;
        }

        public StructureContext.Builder structureTemplateManager(StructureTemplateManager structureTemplateManager) {
            this.structureTemplateManager = structureTemplateManager;
            return this;
        }

        public StructureContext.Builder pieces(List<PieceEntry> pieces) {
            this.pieces = pieces;
            return this;
        }

        public StructureContext.Builder pieceEntry(PieceEntry pieceEntry) {
            this.pieceEntry = pieceEntry;
            return this;
        }

        public StructureContext.Builder random(RandomSource random) {
            this.random = random;
            return this;
        }

        public StructureContext build() {
            return new StructureContext(this);
        }
    }
}