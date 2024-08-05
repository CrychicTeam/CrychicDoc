package com.simibubi.create.content.schematics.cannon;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;

public class SchematicannonInstance extends BlockEntityInstance<SchematicannonBlockEntity> implements DynamicInstance {

    private final ModelData connector;

    private final ModelData pipe;

    public SchematicannonInstance(MaterialManager materialManager, SchematicannonBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        Material<ModelData> mat = this.getTransformMaterial();
        this.connector = (ModelData) mat.getModel(AllPartialModels.SCHEMATICANNON_CONNECTOR, this.blockState).createInstance();
        this.pipe = (ModelData) mat.getModel(AllPartialModels.SCHEMATICANNON_PIPE, this.blockState).createInstance();
    }

    public void beginFrame() {
        float partialTicks = AnimationTickHolder.getPartialTicks();
        double[] cannonAngles = SchematicannonRenderer.getCannonAngles((SchematicannonBlockEntity) this.blockEntity, this.pos, partialTicks);
        double yaw = cannonAngles[0];
        double pitch = cannonAngles[1];
        double recoil = SchematicannonRenderer.getRecoil((SchematicannonBlockEntity) this.blockEntity, partialTicks);
        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(this.getInstancePosition());
        ms.pushPose();
        msr.centre();
        msr.rotate(Direction.UP, (float) ((yaw + 90.0) / 180.0 * Math.PI));
        msr.unCentre();
        this.connector.setTransform(ms);
        ms.popPose();
        msr.translate(0.5, 0.9375, 0.5);
        msr.rotate(Direction.UP, (float) ((yaw + 90.0) / 180.0 * Math.PI));
        msr.rotate(Direction.SOUTH, (float) (pitch / 180.0 * Math.PI));
        msr.translateBack(0.5, 0.9375, 0.5);
        msr.translate(0.0, -recoil / 100.0, 0.0);
        this.pipe.setTransform(ms);
    }

    public void remove() {
        this.connector.delete();
        this.pipe.delete();
    }

    public void updateLight() {
        this.relight(this.pos, new FlatLit[] { this.connector, this.pipe });
    }
}