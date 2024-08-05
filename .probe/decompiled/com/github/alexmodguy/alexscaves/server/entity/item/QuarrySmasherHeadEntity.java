package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.util.ACMultipartEntity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;

public class QuarrySmasherHeadEntity extends ACMultipartEntity<QuarrySmasherEntity> {

    private EntityDimensions size = EntityDimensions.fixed(0.9F, 0.6F);

    public QuarrySmasherHeadEntity(QuarrySmasherEntity parent) {
        super(parent);
        this.m_6210_();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(1.0, 1.0, 1.0);
    }
}