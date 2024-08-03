package com.simibubi.create.content.equipment.symmetryWand.mirror;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.Lang;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlaneMirror extends SymmetryMirror {

    public PlaneMirror(Vec3 pos) {
        super(pos);
        this.orientation = PlaneMirror.Align.XY;
    }

    @Override
    protected void setOrientation() {
        if (this.orientationIndex < 0) {
            this.orientationIndex = this.orientationIndex + PlaneMirror.Align.values().length;
        }
        if (this.orientationIndex >= PlaneMirror.Align.values().length) {
            this.orientationIndex = this.orientationIndex - PlaneMirror.Align.values().length;
        }
        this.orientation = PlaneMirror.Align.values()[this.orientationIndex];
    }

    @Override
    public void setOrientation(int index) {
        this.orientation = PlaneMirror.Align.values()[index];
        this.orientationIndex = index;
    }

    @Override
    public Map<BlockPos, BlockState> process(BlockPos position, BlockState block) {
        Map<BlockPos, BlockState> result = new HashMap();
        switch((PlaneMirror.Align) this.orientation) {
            case XY:
                result.put(this.flipZ(position), this.flipZ(block));
                break;
            case YZ:
                result.put(this.flipX(position), this.flipX(block));
        }
        return result;
    }

    @Override
    public String typeName() {
        return "plane";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel getModel() {
        return AllPartialModels.SYMMETRY_PLANE;
    }

    @Override
    public void applyModelTransform(PoseStack ms) {
        super.applyModelTransform(ms);
        ((TransformStack) ((TransformStack) TransformStack.cast(ms).centre()).rotateY((PlaneMirror.Align) this.orientation == PlaneMirror.Align.XY ? 0.0 : 90.0)).unCentre();
    }

    @Override
    public List<Component> getAlignToolTips() {
        return ImmutableList.of(Lang.translateDirect("orientation.alongZ"), Lang.translateDirect("orientation.alongX"));
    }

    public static enum Align implements StringRepresentable {

        XY("xy"), YZ("yz");

        private final String name;

        private Align(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }
    }
}