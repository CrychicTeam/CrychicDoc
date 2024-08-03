package com.mna.api.entities.construct.ai.parameter;

import com.mna.api.blocks.DirectionalPoint;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class ConstructTaskPointParameter extends ConstructAITaskParameter {

    private DirectionalPoint point;

    public ConstructTaskPointParameter(String id) {
        super(id, ConstructParameterTypes.POINT);
    }

    @Nullable
    public BlockPos getPosition() {
        return this.point == null ? null : this.point.getPosition();
    }

    @Nullable
    public Direction getDirection() {
        return this.point == null ? null : this.point.getDirection();
    }

    @Nullable
    public String getOriginalBlockName() {
        return this.point == null ? null : this.point.getBlock();
    }

    @Nullable
    public DirectionalPoint getPoint() {
        return this.point;
    }

    public void setPoint(DirectionalPoint point) {
        this.point = new DirectionalPoint(point.getPosition(), point.getDirection(), point.getBlock());
    }

    @Override
    public void loadData(CompoundTag nbt) {
        super.loadData(nbt);
        if (nbt.contains("point")) {
            this.point = DirectionalPoint.of(nbt.getCompound("point"));
        }
    }

    @Override
    public CompoundTag saveData() {
        CompoundTag tag = super.saveData();
        if (this.point != null) {
            tag.put("point", this.point.save());
        }
        return tag;
    }
}