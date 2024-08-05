package com.simibubi.create.content.contraptions.actors.psi;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

public class PSIActorInstance extends ActorInstance {

    private final PIInstance instance;

    public PSIActorInstance(MaterialManager materialManager, VirtualRenderWorld world, MovementContext context) {
        super(materialManager, world, context);
        this.instance = new PIInstance(materialManager, context.state, context.localPos);
        this.instance.init(false);
        this.instance.middle.setBlockLight(this.localBlockLight());
        this.instance.top.setBlockLight(this.localBlockLight());
    }

    @Override
    public void beginFrame() {
        LerpedFloat lf = PortableStorageInterfaceMovement.getAnimation(this.context);
        this.instance.tick(lf.settled());
        this.instance.beginFrame(lf.getValue(AnimationTickHolder.getPartialTicks()));
    }
}