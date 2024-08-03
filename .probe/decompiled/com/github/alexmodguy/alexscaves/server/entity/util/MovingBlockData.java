package com.github.alexmodguy.alexscaves.server.entity.util;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MovingBlockData {

    private BlockState state;

    private VoxelShape shape;

    private BlockPos offset;

    @Nullable
    public CompoundTag blockData;

    public MovingBlockData(BlockState state, VoxelShape shape, BlockPos offset, @Nullable CompoundTag blockData) {
        this.state = state;
        this.shape = shape;
        this.offset = offset;
        this.blockData = blockData;
    }

    public MovingBlockData(Level level, CompoundTag tag) {
        this(NbtUtils.readBlockState(level.m_246945_(Registries.BLOCK), tag.getCompound("BlockState")), getShapeFromTag(tag.getCompound("VoxelShape")), new BlockPos(tag.getInt("OffsetX"), tag.getInt("OffsetY"), tag.getInt("OffsetZ")), tag.contains("BlockData") ? tag.getCompound("BlockData") : null);
    }

    public BlockState getState() {
        return this.state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public VoxelShape getShape() {
        return this.shape;
    }

    public void setShape(VoxelShape shape) {
        this.shape = shape;
    }

    public BlockPos getOffset() {
        return this.offset;
    }

    public void setOffset(BlockPos offset) {
        this.offset = offset;
    }

    @Nullable
    public CompoundTag getBlockData() {
        return this.blockData;
    }

    public void setBlockData(@Nullable CompoundTag blockData) {
        this.blockData = blockData;
    }

    public CompoundTag toTag() {
        CompoundTag data = new CompoundTag();
        data.put("BlockState", NbtUtils.writeBlockState(this.state));
        data.put("VoxelShape", this.getShapeTag());
        data.putInt("OffsetX", this.offset.m_123341_());
        data.putInt("OffsetY", this.offset.m_123342_());
        data.putInt("OffsetZ", this.offset.m_123343_());
        if (this.blockData != null) {
            data.put("BlockData", this.blockData);
        }
        return data;
    }

    private CompoundTag getShapeTag() {
        CompoundTag data = new CompoundTag();
        ListTag listTag = new ListTag();
        for (AABB shapeAABB : this.shape.toAabbs()) {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("BoxMinX", shapeAABB.minX);
            tag.putDouble("BoxMinY", shapeAABB.minY);
            tag.putDouble("BoxMinZ", shapeAABB.minZ);
            tag.putDouble("BoxMaxX", shapeAABB.maxX);
            tag.putDouble("BoxMaxY", shapeAABB.maxY);
            tag.putDouble("BoxMaxZ", shapeAABB.maxZ);
            listTag.add(tag);
        }
        data.put("AABBs", listTag);
        return data;
    }

    private static VoxelShape getShapeFromTag(CompoundTag data) {
        VoxelShape shape = Shapes.empty();
        if (data.contains("AABBs")) {
            ListTag listtag = data.getList("AABBs", 10);
            for (int i = 0; i < listtag.size(); i++) {
                CompoundTag innerTag = listtag.getCompound(i);
                AABB aabb = new AABB(innerTag.getDouble("BoxMinX"), innerTag.getDouble("BoxMinY"), innerTag.getDouble("BoxMinZ"), innerTag.getDouble("BoxMaxX"), innerTag.getDouble("BoxMaxY"), innerTag.getDouble("BoxMaxZ"));
                shape = Shapes.join(shape, Shapes.create(aabb), BooleanOp.OR);
            }
        }
        return shape;
    }
}