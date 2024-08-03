package com.simibubi.create.content.kinetics.base.flwdata;

import com.jozufozu.flywheel.api.struct.Batched;
import com.jozufozu.flywheel.api.struct.Instanced;
import com.jozufozu.flywheel.api.struct.StructWriter;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.core.layout.BufferLayout;
import com.jozufozu.flywheel.core.model.ModelTransformer.Params;
import com.jozufozu.flywheel.util.RenderMath;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.KineticDebugger;
import com.simibubi.create.foundation.render.AllInstanceFormats;
import com.simibubi.create.foundation.render.AllProgramSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class RotatingType implements Instanced<RotatingData>, Batched<RotatingData> {

    public RotatingData create() {
        return new RotatingData();
    }

    public BufferLayout getLayout() {
        return AllInstanceFormats.ROTATING;
    }

    public StructWriter<RotatingData> getWriter(VecBuffer backing) {
        return new RotatingWriterUnsafe(backing, this);
    }

    public ResourceLocation getProgramSpec() {
        return AllProgramSpecs.ROTATING;
    }

    public void transform(RotatingData d, Params b) {
        float angle = (AnimationTickHolder.getRenderTime() * d.rotationalSpeed * 3.0F / 10.0F + d.rotationOffset) % 360.0F;
        Axis axis = Axis.of(new Vector3f(RenderMath.f(d.rotationAxisX), RenderMath.f(d.rotationAxisY), RenderMath.f(d.rotationAxisZ)));
        b.light(d.getPackedLight()).translate((double) d.x + 0.5, (double) d.y + 0.5, (double) d.z + 0.5).multiply(axis.rotationDegrees(angle)).unCentre();
        if (KineticDebugger.isActive()) {
            b.color(d.r, d.g, d.b, d.a);
        }
    }
}