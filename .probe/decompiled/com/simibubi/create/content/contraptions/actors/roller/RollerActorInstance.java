package com.simibubi.create.content.contraptions.actors.roller;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterActorInstance;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.world.phys.Vec3;

public class RollerActorInstance extends HarvesterActorInstance {

    ModelData frame;

    public RollerActorInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        super(materialManager, simulationWorld, context);
        Material<ModelData> material = materialManager.defaultCutout().material(Materials.TRANSFORMED);
        this.frame = (ModelData) material.getModel(AllPartialModels.ROLLER_FRAME, context.state).createInstance();
        this.frame.setBlockLight(this.localBlockLight());
    }

    @Override
    public void beginFrame() {
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.harvester.loadIdentity().translate(this.context.localPos)).centre()).rotateY((double) this.horizontalAngle)).unCentre()).translate(0.0, -0.25, 1.0625).rotateX(this.getRotation())).translate(0.0, -0.5, 0.5).rotateY(90.0);
        ((ModelData) ((ModelData) ((ModelData) this.frame.loadIdentity().translate(this.context.localPos)).centre()).rotateY((double) (this.horizontalAngle + 180.0F))).unCentre();
    }

    @Override
    protected PartialModel getRollingPartial() {
        return AllPartialModels.ROLLER_WHEEL;
    }

    @Override
    protected Vec3 getRotationOffset() {
        return Vec3.ZERO;
    }

    @Override
    protected double getRadius() {
        return 16.5;
    }
}