package com.simibubi.create.content.logistics.flwdata;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.core.materials.FlatLit;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import org.joml.Vector3f;

public class FlapData extends InstanceData implements FlatLit<FlapData> {

    float x;

    float y;

    float z;

    byte blockLight;

    byte skyLight;

    float segmentOffsetX;

    float segmentOffsetY;

    float segmentOffsetZ;

    float pivotX;

    float pivotY;

    float pivotZ;

    float horizontalAngle;

    float intensity;

    float flapScale;

    float flapness;

    public FlapData setPosition(BlockPos pos) {
        return this.setPosition((float) pos.m_123341_(), (float) pos.m_123342_(), (float) pos.m_123343_());
    }

    public FlapData setPosition(Vector3f pos) {
        return this.setPosition(pos.x(), pos.y(), pos.z());
    }

    public FlapData setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.markDirty();
        return this;
    }

    public FlapData setBlockLight(int blockLight) {
        this.blockLight = (byte) (blockLight & 15);
        this.markDirty();
        return this;
    }

    public FlapData setSkyLight(int skyLight) {
        this.skyLight = (byte) (skyLight & 15);
        this.markDirty();
        return this;
    }

    public int getPackedLight() {
        return LightTexture.pack(this.blockLight, this.skyLight);
    }

    public FlapData setSegmentOffset(float x, float y, float z) {
        this.segmentOffsetX = x;
        this.segmentOffsetY = y;
        this.segmentOffsetZ = z;
        this.markDirty();
        return this;
    }

    public FlapData setIntensity(float intensity) {
        this.intensity = intensity;
        this.markDirty();
        return this;
    }

    public FlapData setHorizontalAngle(float horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
        this.markDirty();
        return this;
    }

    public FlapData setFlapScale(float flapScale) {
        this.flapScale = flapScale;
        this.markDirty();
        return this;
    }

    public FlapData setFlapness(float flapness) {
        this.flapness = flapness;
        this.markDirty();
        return this;
    }

    public FlapData setPivotVoxelSpace(float x, float y, float z) {
        this.pivotX = x / 16.0F;
        this.pivotY = y / 16.0F;
        this.pivotZ = z / 16.0F;
        this.markDirty();
        return this;
    }
}