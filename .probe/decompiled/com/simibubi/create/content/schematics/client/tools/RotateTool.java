package com.simibubi.create.content.schematics.client.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.outliner.LineOutline;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RotateTool extends PlacementToolBase {

    private LineOutline line = new LineOutline();

    @Override
    public boolean handleMouseWheel(double delta) {
        this.schematicHandler.getTransformation().rotate90(delta > 0.0);
        this.schematicHandler.markDirty();
        return true;
    }

    @Override
    public void renderOnSchematic(PoseStack ms, SuperRenderTypeBuffer buffer) {
        AABB bounds = this.schematicHandler.getBounds();
        double height = bounds.getYsize() + Math.max(20.0, bounds.getYsize());
        Vec3 center = bounds.getCenter().add(this.schematicHandler.getTransformation().getRotationOffset(false));
        Vec3 start = center.subtract(0.0, height / 2.0, 0.0);
        Vec3 end = center.add(0.0, height / 2.0, 0.0);
        this.line.getParams().disableCull().disableLineNormals().colored(14540253).lineWidth(0.0625F);
        this.line.set(start, end).render(ms, buffer, Vec3.ZERO, AnimationTickHolder.getPartialTicks());
        super.renderOnSchematic(ms, buffer);
    }
}