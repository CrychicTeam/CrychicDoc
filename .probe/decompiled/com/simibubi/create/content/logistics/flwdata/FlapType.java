package com.simibubi.create.content.logistics.flwdata;

import com.jozufozu.flywheel.api.struct.Batched;
import com.jozufozu.flywheel.api.struct.Instanced;
import com.jozufozu.flywheel.api.struct.StructWriter;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.core.layout.BufferLayout;
import com.jozufozu.flywheel.core.model.ModelTransformer.Params;
import com.simibubi.create.foundation.render.AllInstanceFormats;
import com.simibubi.create.foundation.render.AllProgramSpecs;
import net.minecraft.resources.ResourceLocation;

public class FlapType implements Instanced<FlapData>, Batched<FlapData> {

    public FlapData create() {
        return new FlapData();
    }

    public BufferLayout getLayout() {
        return AllInstanceFormats.FLAP;
    }

    public StructWriter<FlapData> getWriter(VecBuffer backing) {
        return new UnsafeFlapWriter(backing, this);
    }

    public ResourceLocation getProgramSpec() {
        return AllProgramSpecs.FLAPS;
    }

    public void transform(FlapData d, Params b) {
        ((Params) ((Params) ((Params) ((Params) ((Params) b.translate((double) d.x, (double) d.y, (double) d.z).centre()).rotateY((double) (-d.horizontalAngle))).unCentre()).translate((double) d.pivotX, (double) d.pivotY, (double) d.pivotZ).rotateX((double) getFlapAngle(d.flapness, d.intensity, d.flapScale))).translateBack((double) d.pivotX, (double) d.pivotY, (double) d.pivotZ)).translate((double) d.segmentOffsetX, (double) d.segmentOffsetY, (double) d.segmentOffsetZ).light(d.getPackedLight());
    }

    private static float getFlapAngle(float flapness, float intensity, float scale) {
        float absFlap = Math.abs(flapness);
        float angle = (float) (Math.sin((1.0 - (double) absFlap) * Math.PI * (double) intensity) * 30.0 * (double) flapness * (double) scale);
        return flapness > 0.0F ? angle * 0.5F : angle;
    }
}