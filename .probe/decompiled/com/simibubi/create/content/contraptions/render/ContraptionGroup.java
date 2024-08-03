package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.backend.instancing.instancing.InstancedMaterialGroup;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine.GroupFactory;
import net.minecraft.client.renderer.RenderType;

public class ContraptionGroup<P extends ContraptionProgram> extends InstancedMaterialGroup<P> {

    private final FlwContraption contraption;

    public ContraptionGroup(FlwContraption contraption, InstancingEngine<P> owner, RenderType type) {
        super(owner, type);
        this.contraption = contraption;
    }

    protected void setup(P program) {
        this.contraption.setup(program);
    }

    public static <P extends ContraptionProgram> GroupFactory<P> forContraption(FlwContraption c) {
        return (materialManager, type) -> new ContraptionGroup(c, materialManager, type);
    }
}