package com.simibubi.create.content.kinetics.base.flwdata;

import com.jozufozu.flywheel.core.materials.BasicData;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import org.joml.Vector3f;

public class KineticData extends BasicData {

    float x;

    float y;

    float z;

    float rotationalSpeed;

    float rotationOffset;

    public KineticData setPosition(BlockPos pos) {
        return this.setPosition((float) pos.m_123341_(), (float) pos.m_123342_(), (float) pos.m_123343_());
    }

    public KineticData setPosition(Vector3f pos) {
        return this.setPosition(pos.x(), pos.y(), pos.z());
    }

    public KineticData setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.markDirty();
        return this;
    }

    public KineticData nudge(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.markDirty();
        return this;
    }

    public KineticData setColor(KineticBlockEntity blockEntity) {
        if (blockEntity.hasNetwork()) {
            this.setColor(Color.generateFromLong(blockEntity.network));
        } else {
            this.setColor(255, 255, 255);
        }
        return this;
    }

    public KineticData setColor(Color c) {
        this.setColor(c.getRed(), c.getGreen(), c.getBlue());
        return this;
    }

    public KineticData setRotationalSpeed(float rotationalSpeed) {
        this.rotationalSpeed = rotationalSpeed;
        return this;
    }

    public KineticData setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
        return this;
    }
}