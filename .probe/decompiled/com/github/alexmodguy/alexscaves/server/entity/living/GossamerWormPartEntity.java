package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.util.ACMultipartEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GossamerWormPartEntity extends ACMultipartEntity<GossamerWormEntity> {

    private final Entity connectedTo;

    private EntityDimensions size;

    public float scale = 1.0F;

    public GossamerWormPartEntity(GossamerWormEntity parent, Entity connectedTo, float sizeXZ, float sizeY) {
        super(parent);
        this.connectedTo = connectedTo;
        this.size = EntityDimensions.fixed(sizeXZ, sizeY);
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

    public void setToTransformation(Vec3 offset, float xRot, float yRot) {
        Vec3 transformed = offset.xRot((float) ((double) (-xRot) * (Math.PI / 180.0))).yRot((float) ((double) (-yRot) * (Math.PI / 180.0)));
        Vec3 offseted = transformed.add(this.connectedTo.position().add(0.0, (double) (this.connectedTo.getBbHeight() * 0.5F), 0.0));
        this.m_6034_(offseted.x, offseted.y - (double) (this.m_20206_() * 0.5F), offseted.z);
    }
}