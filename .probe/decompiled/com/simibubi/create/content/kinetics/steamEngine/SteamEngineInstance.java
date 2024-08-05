package com.simibubi.create.content.kinetics.steamEngine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class SteamEngineInstance extends BlockEntityInstance<SteamEngineBlockEntity> implements DynamicInstance {

    protected final ModelData piston;

    protected final ModelData linkage;

    protected final ModelData connector;

    public SteamEngineInstance(MaterialManager materialManager, SteamEngineBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.piston = (ModelData) materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.ENGINE_PISTON, this.blockState).createInstance();
        this.linkage = (ModelData) materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.ENGINE_LINKAGE, this.blockState).createInstance();
        this.connector = (ModelData) materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.ENGINE_CONNECTOR, this.blockState).createInstance();
    }

    public void beginFrame() {
        Float angle = ((SteamEngineBlockEntity) this.blockEntity).getTargetAngle();
        if (angle == null) {
            this.piston.setEmptyTransform();
            this.linkage.setEmptyTransform();
            this.connector.setEmptyTransform();
        } else {
            Direction facing = SteamEngineBlock.getFacing(this.blockState);
            Direction.Axis facingAxis = facing.getAxis();
            Direction.Axis axis = Direction.Axis.Y;
            PoweredShaftBlockEntity shaft = ((SteamEngineBlockEntity) this.blockEntity).getShaft();
            if (shaft != null) {
                axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
            }
            boolean roll90 = facingAxis.isHorizontal() && axis == Direction.Axis.Y || facingAxis.isVertical() && axis == Direction.Axis.Z;
            float sine = Mth.sin(angle);
            float sine2 = Mth.sin(angle - (float) (Math.PI / 2));
            float piston = (1.0F - sine) / 4.0F * 24.0F / 16.0F;
            this.transformed(this.piston, facing, roll90).translate(0.0, (double) piston, 0.0);
            ((ModelData) ((ModelData) ((ModelData) this.transformed(this.linkage, facing, roll90).centre()).translate(0.0, 1.0, 0.0).unCentre()).translate(0.0, (double) piston, 0.0).translate(0.0, 0.25, 0.5).rotateX((double) (sine2 * 23.0F))).translate(0.0, -0.25, -0.5);
            ((ModelData) ((ModelData) this.transformed(this.connector, facing, roll90).translate(0.0, 2.0, 0.0).centre()).rotateXRadians((double) (-angle + (float) (Math.PI / 2)))).unCentre();
        }
    }

    protected ModelData transformed(ModelData modelData, Direction facing, boolean roll90) {
        return (ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) modelData.loadIdentity().translate(this.getInstancePosition())).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) (AngleHelper.verticalAngle(facing) + 90.0F))).rotateY(roll90 ? -90.0 : 0.0)).unCentre();
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.piston, this.linkage, this.connector });
    }

    protected void remove() {
        this.piston.delete();
        this.linkage.delete();
        this.connector.delete();
    }
}