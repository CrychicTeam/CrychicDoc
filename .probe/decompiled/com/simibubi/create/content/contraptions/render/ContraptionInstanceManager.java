package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.TaskEngine;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstanceManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;

public class ContraptionInstanceManager extends BlockEntityInstanceManager {

    protected ArrayList<ActorInstance> actors = new ArrayList();

    private final VirtualRenderWorld renderWorld;

    private Contraption contraption;

    ContraptionInstanceManager(MaterialManager materialManager, VirtualRenderWorld renderWorld, Contraption contraption) {
        super(materialManager);
        this.renderWorld = renderWorld;
        this.contraption = contraption;
    }

    public void tick() {
        this.actors.forEach(ActorInstance::tick);
    }

    protected boolean canCreateInstance(BlockEntity blockEntity) {
        return !this.contraption.isHiddenInPortal(blockEntity.getBlockPos());
    }

    public void beginFrame(TaskEngine taskEngine, Camera info) {
        super.beginFrame(taskEngine, info);
        this.actors.forEach(ActorInstance::beginFrame);
    }

    protected void updateInstance(DynamicInstance dyn, float lookX, float lookY, float lookZ, int cX, int cY, int cZ) {
        dyn.beginFrame();
    }

    @Nullable
    public ActorInstance createActor(Pair<StructureTemplate.StructureBlockInfo, MovementContext> actor) {
        StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) actor.getLeft();
        MovementContext context = (MovementContext) actor.getRight();
        if (this.contraption.isHiddenInPortal(context.localPos)) {
            return null;
        } else {
            MovementBehaviour movementBehaviour = AllMovementBehaviours.getBehaviour(blockInfo.state());
            if (movementBehaviour != null && movementBehaviour.hasSpecialInstancedRendering()) {
                ActorInstance instance = movementBehaviour.createInstance(this.materialManager, this.renderWorld, context);
                this.actors.add(instance);
                return instance;
            } else {
                return null;
            }
        }
    }

    public void detachLightListeners() {
    }
}