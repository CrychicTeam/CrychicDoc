package com.simibubi.create.content.contraptions.actors.flwdata;

import com.jozufozu.flywheel.api.struct.Batched;
import com.jozufozu.flywheel.api.struct.Instanced;
import com.jozufozu.flywheel.api.struct.StructWriter;
import com.jozufozu.flywheel.backend.gl.buffer.VecBuffer;
import com.jozufozu.flywheel.core.layout.BufferLayout;
import com.jozufozu.flywheel.core.model.ModelTransformer.Params;
import com.simibubi.create.foundation.render.AllInstanceFormats;
import com.simibubi.create.foundation.render.AllProgramSpecs;
import net.minecraft.resources.ResourceLocation;

public class ActorType implements Instanced<ActorData>, Batched<ActorData> {

    public ActorData create() {
        return new ActorData();
    }

    public BufferLayout getLayout() {
        return AllInstanceFormats.ACTOR;
    }

    public StructWriter<ActorData> getWriter(VecBuffer backing) {
        return new UnsafeActorWriter(backing, this);
    }

    public ResourceLocation getProgramSpec() {
        return AllProgramSpecs.ACTOR;
    }

    public void transform(ActorData d, Params b) {
    }
}