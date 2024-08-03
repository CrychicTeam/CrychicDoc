package com.simibubi.create.content.contraptions.actors.harvester;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class HarvesterActorInstance extends ActorInstance {

    static float originOffset = 0.0625F;

    static Vec3 rotOffset = new Vec3(0.5, (double) (-2.0F * originOffset + 0.5F), (double) (originOffset + 0.5F));

    protected ModelData harvester;

    private Direction facing;

    protected float horizontalAngle;

    private double rotation;

    private double previousRotation;

    public HarvesterActorInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        super(materialManager, simulationWorld, context);
        Material<ModelData> material = materialManager.defaultCutout().material(Materials.TRANSFORMED);
        BlockState state = context.state;
        this.facing = (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        this.harvester = (ModelData) material.getModel(this.getRollingPartial(), state).createInstance();
        this.horizontalAngle = this.facing.toYRot() + (float) (this.facing.getAxis() == Direction.Axis.X ? 180 : 0);
        this.harvester.setBlockLight(this.localBlockLight());
    }

    protected PartialModel getRollingPartial() {
        return AllPartialModels.HARVESTER_BLADE;
    }

    protected Vec3 getRotationOffset() {
        return rotOffset;
    }

    protected double getRadius() {
        return 6.5;
    }

    @Override
    public void tick() {
        super.tick();
        this.previousRotation = this.rotation;
        if (!this.context.contraption.stalled && !this.context.disabled && !VecHelper.isVecPointingTowards(this.context.relativeMotion, this.facing.getOpposite())) {
            double arcLength = this.context.motion.length();
            double radians = arcLength * 16.0 / this.getRadius();
            float deg = AngleHelper.deg(radians);
            deg = (float) ((int) (deg * 3000.0F) / 3000);
            this.rotation += (double) deg * 1.25;
            this.rotation %= 360.0;
        }
    }

    @Override
    public void beginFrame() {
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.harvester.loadIdentity().translate(this.context.localPos)).centre()).rotateY((double) this.horizontalAngle)).unCentre()).translate(this.getRotationOffset())).rotateX(this.getRotation())).translateBack(this.getRotationOffset());
    }

    protected double getRotation() {
        return (double) AngleHelper.angleLerp((double) AnimationTickHolder.getPartialTicks(), this.previousRotation, this.rotation);
    }
}