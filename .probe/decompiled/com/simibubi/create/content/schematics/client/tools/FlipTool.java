package com.simibubi.create.content.schematics.client.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.foundation.outliner.AABBOutline;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FlipTool extends PlacementToolBase {

    private AABBOutline outline = new AABBOutline(new AABB(BlockPos.ZERO));

    @Override
    public void init() {
        super.init();
        this.renderSelectedFace = false;
    }

    @Override
    public boolean handleRightClick() {
        this.mirror();
        return true;
    }

    @Override
    public boolean handleMouseWheel(double delta) {
        this.mirror();
        return true;
    }

    @Override
    public void updateSelection() {
        super.updateSelection();
    }

    private void mirror() {
        if (this.schematicSelected && this.selectedFace.getAxis().isHorizontal()) {
            this.schematicHandler.getTransformation().flip(this.selectedFace.getAxis());
            this.schematicHandler.markDirty();
        }
    }

    @Override
    public void renderOnSchematic(PoseStack ms, SuperRenderTypeBuffer buffer) {
        if (this.schematicSelected && this.selectedFace.getAxis().isHorizontal()) {
            Direction facing = this.selectedFace.getClockWise();
            AABB bounds = this.schematicHandler.getBounds();
            Vec3 directionVec = Vec3.atLowerCornerOf(Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis()).getNormal());
            Vec3 boundsSize = new Vec3(bounds.getXsize(), bounds.getYsize(), bounds.getZsize());
            Vec3 vec = boundsSize.multiply(directionVec);
            bounds = bounds.contract(vec.x, vec.y, vec.z).inflate(1.0 - directionVec.x, 1.0 - directionVec.y, 1.0 - directionVec.z);
            bounds = bounds.move(directionVec.scale(0.5).multiply(boundsSize));
            this.outline.setBounds(bounds);
            AllSpecialTextures tex = AllSpecialTextures.CHECKERED;
            this.outline.getParams().lineWidth(0.0625F).disableLineNormals().colored(14540253).withFaceTextures(tex, tex);
            this.outline.render(ms, buffer, Vec3.ZERO, AnimationTickHolder.getPartialTicks());
            super.renderOnSchematic(ms, buffer);
        } else {
            super.renderOnSchematic(ms, buffer);
        }
    }
}