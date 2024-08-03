package net.minecraft.world.level.levelgen.structure.pieces;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

public class StructurePiecesBuilder implements StructurePieceAccessor {

    private final List<StructurePiece> pieces = Lists.newArrayList();

    @Override
    public void addPiece(StructurePiece structurePiece0) {
        this.pieces.add(structurePiece0);
    }

    @Nullable
    @Override
    public StructurePiece findCollisionPiece(BoundingBox boundingBox0) {
        return StructurePiece.findCollisionPiece(this.pieces, boundingBox0);
    }

    @Deprecated
    public void offsetPiecesVertically(int int0) {
        for (StructurePiece $$1 : this.pieces) {
            $$1.move(0, int0, 0);
        }
    }

    @Deprecated
    public int moveBelowSeaLevel(int int0, int int1, RandomSource randomSource2, int int3) {
        int $$4 = int0 - int3;
        BoundingBox $$5 = this.getBoundingBox();
        int $$6 = $$5.getYSpan() + int1 + 1;
        if ($$6 < $$4) {
            $$6 += randomSource2.nextInt($$4 - $$6);
        }
        int $$7 = $$6 - $$5.maxY();
        this.offsetPiecesVertically($$7);
        return $$7;
    }

    /**
     * @deprecated
     */
    public void moveInsideHeights(RandomSource randomSource0, int int1, int int2) {
        BoundingBox $$3 = this.getBoundingBox();
        int $$4 = int2 - int1 + 1 - $$3.getYSpan();
        int $$5;
        if ($$4 > 1) {
            $$5 = int1 + randomSource0.nextInt($$4);
        } else {
            $$5 = int1;
        }
        int $$7 = $$5 - $$3.minY();
        this.offsetPiecesVertically($$7);
    }

    public PiecesContainer build() {
        return new PiecesContainer(this.pieces);
    }

    public void clear() {
        this.pieces.clear();
    }

    public boolean isEmpty() {
        return this.pieces.isEmpty();
    }

    public BoundingBox getBoundingBox() {
        return StructurePiece.createBoundingBox(this.pieces.stream());
    }
}