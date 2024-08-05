package com.simibubi.create.content.contraptions.actors.flwdata;

import com.jozufozu.flywheel.api.InstanceData;
import net.minecraft.core.BlockPos;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ActorData extends InstanceData {

    float x;

    float y;

    float z;

    byte blockLight;

    byte skyLight;

    float rotationOffset;

    byte rotationAxisX;

    byte rotationAxisY;

    byte rotationAxisZ;

    float qX;

    float qY;

    float qZ;

    float qW;

    byte rotationCenterX = 64;

    byte rotationCenterY = 64;

    byte rotationCenterZ = 64;

    float speed;

    public ActorData setPosition(BlockPos pos) {
        this.x = (float) pos.m_123341_();
        this.y = (float) pos.m_123342_();
        this.z = (float) pos.m_123343_();
        this.markDirty();
        return this;
    }

    public ActorData setBlockLight(int blockLight) {
        this.blockLight = (byte) ((blockLight & 15) << 4);
        this.markDirty();
        return this;
    }

    public ActorData setSkyLight(int skyLight) {
        this.skyLight = (byte) ((skyLight & 15) << 4);
        this.markDirty();
        return this;
    }

    public ActorData setRotationOffset(float rotationOffset) {
        this.rotationOffset = rotationOffset;
        this.markDirty();
        return this;
    }

    public ActorData setSpeed(float speed) {
        this.speed = speed;
        this.markDirty();
        return this;
    }

    public ActorData setRotationAxis(Vector3f axis) {
        this.setRotationAxis(axis.x(), axis.y(), axis.z());
        return this;
    }

    public ActorData setRotationAxis(float rotationAxisX, float rotationAxisY, float rotationAxisZ) {
        this.rotationAxisX = (byte) ((int) (rotationAxisX * 127.0F));
        this.rotationAxisY = (byte) ((int) (rotationAxisY * 127.0F));
        this.rotationAxisZ = (byte) ((int) (rotationAxisZ * 127.0F));
        this.markDirty();
        return this;
    }

    public ActorData setRotationCenter(Vector3f axis) {
        this.setRotationCenter(axis.x(), axis.y(), axis.z());
        return this;
    }

    public ActorData setRotationCenter(float rotationCenterX, float rotationCenterY, float rotationCenterZ) {
        this.rotationCenterX = (byte) ((int) (rotationCenterX * 127.0F));
        this.rotationCenterY = (byte) ((int) (rotationCenterY * 127.0F));
        this.rotationCenterZ = (byte) ((int) (rotationCenterZ * 127.0F));
        this.markDirty();
        return this;
    }

    public ActorData setLocalRotation(Quaternionf q) {
        this.qX = q.x();
        this.qY = q.y();
        this.qZ = q.z();
        this.qW = q.w();
        this.markDirty();
        return this;
    }
}