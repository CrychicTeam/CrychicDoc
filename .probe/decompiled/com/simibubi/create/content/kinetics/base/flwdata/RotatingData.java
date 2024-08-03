package com.simibubi.create.content.kinetics.base.flwdata;

import net.minecraft.core.Direction;
import org.joml.Vector3f;

public class RotatingData extends KineticData {

    byte rotationAxisX;

    byte rotationAxisY;

    byte rotationAxisZ;

    public RotatingData setRotationAxis(Direction.Axis axis) {
        Direction orientation = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return this.setRotationAxis(orientation.step());
    }

    public RotatingData setRotationAxis(Vector3f axis) {
        return this.setRotationAxis(axis.x(), axis.y(), axis.z());
    }

    public RotatingData setRotationAxis(float rotationAxisX, float rotationAxisY, float rotationAxisZ) {
        this.rotationAxisX = (byte) ((int) (rotationAxisX * 127.0F));
        this.rotationAxisY = (byte) ((int) (rotationAxisY * 127.0F));
        this.rotationAxisZ = (byte) ((int) (rotationAxisZ * 127.0F));
        this.markDirty();
        return this;
    }
}