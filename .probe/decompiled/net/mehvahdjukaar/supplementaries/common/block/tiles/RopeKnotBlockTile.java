package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeKnotBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RopeKnotBlockTile extends MimicBlockTile {

    private VoxelShape collisionShape = null;

    private VoxelShape shape = null;

    private static final VoxelShape DOWN_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);

    public RopeKnotBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.ROPE_KNOT_TILE.get(), pos, state);
        this.setHeldBlock(Blocks.AIR.defaultBlockState());
    }

    public VoxelShape getCollisionShape() {
        if (this.collisionShape == null) {
            this.recalculateShapes(this.m_58900_());
        }
        return (VoxelShape) Objects.requireNonNullElseGet(this.collisionShape, Shapes::m_83144_);
    }

    public VoxelShape getShape() {
        if (this.shape == null) {
            this.recalculateShapes(this.m_58900_());
        }
        return (VoxelShape) Objects.requireNonNullElseGet(this.shape, Shapes::m_83144_);
    }

    public void recalculateShapes(BlockState state) {
        try {
            if (state == null || !state.m_60713_((Block) ModRegistry.ROPE_KNOT.get()) || this.f_58857_ == null) {
                return;
            }
            BlockState mimic = this.getHeldBlock();
            if (mimic.m_60795_()) {
                mimic = Blocks.STONE.defaultBlockState();
            }
            boolean up = (Boolean) state.m_61143_(AbstractRopeKnotBlock.UP);
            boolean down = (Boolean) state.m_61143_(AbstractRopeKnotBlock.DOWN);
            VoxelShape r;
            if (down && !up) {
                r = DOWN_SHAPE;
            } else {
                BlockState rope = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((RopeBlock) ModRegistry.ROPE.get()).m_49966_().m_61124_(AbstractRopeBlock.KNOT, false)).m_61124_(AbstractRopeKnotBlock.UP, up)).m_61124_(AbstractRopeKnotBlock.DOWN, down)).m_61124_(AbstractRopeKnotBlock.NORTH, (Boolean) state.m_61143_(AbstractRopeKnotBlock.NORTH))).m_61124_(AbstractRopeKnotBlock.SOUTH, (Boolean) state.m_61143_(AbstractRopeKnotBlock.SOUTH))).m_61124_(AbstractRopeKnotBlock.EAST, (Boolean) state.m_61143_(AbstractRopeKnotBlock.EAST))).m_61124_(AbstractRopeKnotBlock.WEST, (Boolean) state.m_61143_(AbstractRopeKnotBlock.WEST));
                r = rope.m_60808_(this.f_58857_, this.f_58858_);
            }
            VoxelShape c = mimic.m_60812_(this.f_58857_, this.f_58858_);
            VoxelShape s = mimic.m_60808_(this.f_58857_, this.f_58858_);
            c = Shapes.or(c, r);
            s = Shapes.or(s, r);
            this.collisionShape = c.optimize();
            this.shape = s.optimize();
        } catch (Exception var8) {
            Supplementaries.LOGGER.warn("failed to calculate roped fence hitbox: " + var8);
        }
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            this.requestModelReload();
            this.collisionShape = null;
            this.shape = null;
            super.m_6596_();
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.collisionShape = null;
        this.shape = null;
    }
}