package net.minecraft.world.level.levelgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class ScatteredFeaturePiece extends StructurePiece {

    protected final int width;

    protected final int height;

    protected final int depth;

    protected int heightPosition = -1;

    protected ScatteredFeaturePiece(StructurePieceType structurePieceType0, int int1, int int2, int int3, int int4, int int5, int int6, Direction direction7) {
        super(structurePieceType0, 0, StructurePiece.makeBoundingBox(int1, int2, int3, direction7, int4, int5, int6));
        this.width = int4;
        this.height = int5;
        this.depth = int6;
        this.m_73519_(direction7);
    }

    protected ScatteredFeaturePiece(StructurePieceType structurePieceType0, CompoundTag compoundTag1) {
        super(structurePieceType0, compoundTag1);
        this.width = compoundTag1.getInt("Width");
        this.height = compoundTag1.getInt("Height");
        this.depth = compoundTag1.getInt("Depth");
        this.heightPosition = compoundTag1.getInt("HPos");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        compoundTag1.putInt("Width", this.width);
        compoundTag1.putInt("Height", this.height);
        compoundTag1.putInt("Depth", this.depth);
        compoundTag1.putInt("HPos", this.heightPosition);
    }

    protected boolean updateAverageGroundHeight(LevelAccessor levelAccessor0, BoundingBox boundingBox1, int int2) {
        if (this.heightPosition >= 0) {
            return true;
        } else {
            int $$3 = 0;
            int $$4 = 0;
            BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
            for (int $$6 = this.f_73383_.minZ(); $$6 <= this.f_73383_.maxZ(); $$6++) {
                for (int $$7 = this.f_73383_.minX(); $$7 <= this.f_73383_.maxX(); $$7++) {
                    $$5.set($$7, 64, $$6);
                    if (boundingBox1.isInside($$5)) {
                        $$3 += levelAccessor0.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, $$5).m_123342_();
                        $$4++;
                    }
                }
            }
            if ($$4 == 0) {
                return false;
            } else {
                this.heightPosition = $$3 / $$4;
                this.f_73383_.move(0, this.heightPosition - this.f_73383_.minY() + int2, 0);
                return true;
            }
        }
    }

    protected boolean updateHeightPositionToLowestGroundHeight(LevelAccessor levelAccessor0, int int1) {
        if (this.heightPosition >= 0) {
            return true;
        } else {
            int $$2 = levelAccessor0.m_151558_();
            boolean $$3 = false;
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            for (int $$5 = this.f_73383_.minZ(); $$5 <= this.f_73383_.maxZ(); $$5++) {
                for (int $$6 = this.f_73383_.minX(); $$6 <= this.f_73383_.maxX(); $$6++) {
                    $$4.set($$6, 0, $$5);
                    $$2 = Math.min($$2, levelAccessor0.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, $$4).m_123342_());
                    $$3 = true;
                }
            }
            if (!$$3) {
                return false;
            } else {
                this.heightPosition = $$2;
                this.f_73383_.move(0, this.heightPosition - this.f_73383_.minY() + int1, 0);
                return true;
            }
        }
    }
}