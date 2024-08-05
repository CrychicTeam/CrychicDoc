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

public class CrossPlaneMirror extends SymmetryMirror {

    public CrossPlaneMirror(Vec3 pos) {
        super(pos);
        this.orientation = CrossPlaneMirror.Align.Y;
    }

    @Override
    protected void setOrientation() {
        if (this.orientationIndex < 0) {
            this.orientationIndex = this.orientationIndex + CrossPlaneMirror.Align.values().length;
        }
        if (this.orientationIndex >= CrossPlaneMirror.Align.values().length) {
            this.orientationIndex = this.orientationIndex - CrossPlaneMirror.Align.values().length;
        }
        this.orientation = CrossPlaneMirror.Align.values()[this.orientationIndex];
    }

    @Override
    public void setOrientation(int index) {
        this.orientation = CrossPlaneMirror.Align.values()[index];
        this.orientationIndex = index;
    }

    @Override
    public Map<BlockPos, BlockState> process(BlockPos position, BlockState block) {
        Map<BlockPos, BlockState> result = new HashMap();
        switch((CrossPlaneMirror.Align) this.orientation) {
            case D:
                result.put(this.flipD1(position), this.flipD1(block));
                result.put(this.flipD2(position), this.flipD2(block));
                result.put(this.flipD1(this.flipD2(position)), this.flipD1(this.flipD2(block)));
                break;
            case Y:
                result.put(this.flipX(position), this.flipX(block));
                result.put(this.flipZ(position), this.flipZ(block));
                result.put(this.flipX(this.flipZ(position)), this.flipX(this.flipZ(block)));
        }
        return result;
    }

    @Override
    public String typeName() {
        return "cross_plane";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel getModel() {
        return AllPartialModels.SYMMETRY_CROSSPLANE;
    }

    @Override
    public void applyModelTransform(PoseStack ms) {
        super.applyModelTransform(ms);
        ((TransformStack) ((TransformStack) TransformStack.cast(ms).centre()).rotateY((CrossPlaneMirror.Align) this.orientation == CrossPlaneMirror.Align.Y ? 0.0 : 45.0)).unCentre();
    }

    @Override
    public List<Component> getAlignToolTips() {
        return ImmutableList.of(Lang.translateDirect("orientation.orthogonal"), Lang.translateDirect("orientation.diagonal"));
    }

    public static enum Align implements StringRepresentable {

        Y("y"), D("d");

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