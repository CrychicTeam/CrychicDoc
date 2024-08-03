package com.simibubi.create.content.contraptions.actors.psi;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class PIInstance {

    private final MaterialManager materialManager;

    private final BlockState blockState;

    private final BlockPos instancePos;

    private final float angleX;

    private final float angleY;

    private boolean lit;

    ModelData middle;

    ModelData top;

    public PIInstance(MaterialManager materialManager, BlockState blockState, BlockPos instancePos) {
        this.materialManager = materialManager;
        this.blockState = blockState;
        this.instancePos = instancePos;
        Direction facing = (Direction) blockState.m_61143_(PortableStorageInterfaceBlock.f_52588_);
        this.angleX = facing == Direction.UP ? 0.0F : (facing == Direction.DOWN ? 180.0F : 90.0F);
        this.angleY = AngleHelper.horizontalAngle(facing);
    }

    public void init(boolean lit) {
        this.lit = lit;
        this.middle = (ModelData) this.materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(PortableStorageInterfaceRenderer.getMiddleForState(this.blockState, lit), this.blockState).createInstance();
        this.top = (ModelData) this.materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(PortableStorageInterfaceRenderer.getTopForState(this.blockState), this.blockState).createInstance();
    }

    public void beginFrame(float progress) {
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.middle.loadIdentity().translate(this.instancePos)).centre()).rotateY((double) this.angleY)).rotateX((double) this.angleX)).unCentre();
        ((ModelData) ((ModelData) ((ModelData) ((ModelData) this.top.loadIdentity().translate(this.instancePos)).centre()).rotateY((double) this.angleY)).rotateX((double) this.angleX)).unCentre();
        this.middle.translate(0.0, (double) (progress * 0.5F + 0.375F), 0.0);
        this.top.translate(0.0, (double) progress, 0.0);
    }

    public void tick(boolean lit) {
        if (this.lit != lit) {
            this.lit = lit;
            this.materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(PortableStorageInterfaceRenderer.getMiddleForState(this.blockState, lit), this.blockState).stealInstance(this.middle);
        }
    }

    public void remove() {
        this.middle.delete();
        this.top.delete();
    }
}