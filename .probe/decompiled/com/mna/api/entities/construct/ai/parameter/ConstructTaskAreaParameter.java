package com.mna.api.entities.construct.ai.parameter;

import com.mna.api.blocks.DirectionalPoint;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public class ConstructTaskAreaParameter extends ConstructAITaskParameter {

    private AABB area;

    private Pair<DirectionalPoint, DirectionalPoint> points;

    public ConstructTaskAreaParameter(String id) {
        super(id, ConstructParameterTypes.AREA);
    }

    public AABB getArea() {
        return this.area;
    }

    public Pair<DirectionalPoint, DirectionalPoint> getPoints() {
        return this.points;
    }

    public void setPoints(DirectionalPoint firstCorner, DirectionalPoint secondCorner) {
        this.points = new Pair(firstCorner, secondCorner);
        this.createInclusiveAABB();
    }

    private void createInclusiveAABB() {
        if (this.points != null && this.points.getFirst() != null && this.points.getSecond() != null) {
            this.area = new AABB(((DirectionalPoint) this.points.getFirst()).getPosition(), ((DirectionalPoint) this.points.getSecond()).getPosition()).expandTowards(1.0, 1.0, 1.0);
        }
    }

    @Override
    public void loadData(CompoundTag nbt) {
        super.loadData(nbt);
        DirectionalPoint first = null;
        DirectionalPoint second = null;
        if (nbt.contains("firstPoint")) {
            first = DirectionalPoint.of(nbt.getCompound("firstPoint"));
        }
        if (nbt.contains("secondPoint")) {
            second = DirectionalPoint.of(nbt.getCompound("secondPoint"));
        }
        this.points = new Pair(first, second);
        if (first != null && second != null) {
            this.createInclusiveAABB();
        }
    }

    @Override
    public CompoundTag saveData() {
        CompoundTag tag = super.saveData();
        if (this.points != null) {
            if (this.points.getFirst() != null) {
                tag.put("firstPoint", ((DirectionalPoint) this.points.getFirst()).save());
            }
            if (this.points.getSecond() != null) {
                tag.put("secondPoint", ((DirectionalPoint) this.points.getSecond()).save());
            }
        }
        return tag;
    }
}