package com.simibubi.create.content.equipment.toolbox;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.FlatLit;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ToolBoxInstance extends BlockEntityInstance<ToolboxBlockEntity> implements DynamicInstance {

    private final Direction facing = ((Direction) this.blockState.m_61143_(ToolboxBlock.f_54117_)).getOpposite();

    private ModelData lid;

    private ModelData[] drawers;

    public ToolBoxInstance(MaterialManager materialManager, ToolboxBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    public void init() {
        BlockState blockState = ((ToolboxBlockEntity) this.blockEntity).m_58900_();
        Instancer<ModelData> drawerModel = this.materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.TOOLBOX_DRAWER, blockState);
        this.drawers = new ModelData[] { (ModelData) drawerModel.createInstance(), (ModelData) drawerModel.createInstance() };
        this.lid = (ModelData) this.materialManager.defaultCutout().material(Materials.TRANSFORMED).getModel((PartialModel) AllPartialModels.TOOLBOX_LIDS.get(((ToolboxBlockEntity) this.blockEntity).getColor()), blockState).createInstance();
    }

    public void remove() {
        this.lid.delete();
        for (ModelData drawer : this.drawers) {
            drawer.delete();
        }
    }

    public void beginFrame() {
        float partialTicks = AnimationTickHolder.getPartialTicks();
        float lidAngle = ((ToolboxBlockEntity) this.blockEntity).lid.getValue(partialTicks);
        float drawerOffset = ((ToolboxBlockEntity) this.blockEntity).drawers.getValue(partialTicks);
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.lid.loadIdentity().translate(this.instancePos)).centre()).rotateY((double) (-this.facing.toYRot()))).unCentre()).translate(0.0, 0.375, 0.75).rotateX((double) (135.0F * lidAngle))).translateBack(0.0, 0.375, 0.75);
        for (int offset : Iterate.zeroAndOne) {
            ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.drawers[offset].loadIdentity().translate(this.instancePos)).centre()).rotateY((double) (-this.facing.toYRot()))).unCentre()).translate(0.0, (double) ((float) (offset * 1) / 8.0F), (double) (-drawerOffset * 0.175F * (float) (2 - offset)));
        }
    }

    public void updateLight() {
        this.relight(this.pos, this.drawers);
        this.relight(this.pos, new FlatLit[] { this.lid });
    }
}