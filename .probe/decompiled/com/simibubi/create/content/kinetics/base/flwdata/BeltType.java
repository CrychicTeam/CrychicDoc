package com.simibubi.create.content.kinetics.base.flwdata;

import com.jozufozu.flywheel.api.struct.Batched;
import com.jozufozu.flywheel.api.struct.Instanced;
import com.jozufozu.flywheel.api.struct.StructWriter;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.core.layout.BufferLayout;
import com.jozufozu.flywheel.core.model.ModelTransformer.Params;
import com.jozufozu.flywheel.util.RenderMath;
import com.simibubi.create.content.kinetics.KineticDebugger;
import com.simibubi.create.foundation.render.AllInstanceFormats;
import com.simibubi.create.foundation.render.AllProgramSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class BeltType implements Instanced<BeltData>, Batched<BeltData> {

    public BeltData create() {
        return new BeltData();
    }

    public BufferLayout getLayout() {
        return AllInstanceFormats.BELT;
    }

    public StructWriter<BeltData> getWriter(VecBuffer backing) {
        return new BeltWriterUnsafe(backing, this);
    }

    public ResourceLocation getProgramSpec() {
        return AllProgramSpecs.BELT;
    }

    public void transform(BeltData d, Params b) {
        float spriteHeight = d.maxV - d.minV;
        double scroll = (double) (d.rotationalSpeed * AnimationTickHolder.getRenderTime()) / 504.0 + (double) d.rotationOffset;
        scroll -= Math.floor(scroll);
        scroll = scroll * (double) spriteHeight * (double) RenderMath.f(d.scrollMult);
        float finalScroll = (float) scroll;
        b.shiftUV((builder, u, v) -> {
            float targetU = u - d.sourceU + d.minU;
            float targetV = v - d.sourceV + d.minV + finalScroll;
            builder.uv(targetU, targetV);
        });
        ((Params) b.translate((double) d.x + 0.5, (double) d.y + 0.5, (double) d.z + 0.5).multiply(new Quaternionf(d.qX, d.qY, d.qZ, d.qW)).unCentre()).light(d.getPackedLight());
        if (KineticDebugger.isActive()) {
            b.color(d.r, d.g, d.b, d.a);
        }
    }
}