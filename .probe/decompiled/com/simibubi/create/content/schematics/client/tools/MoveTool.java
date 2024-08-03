package com.simibubi.create.content.schematics.client.tools;

import com.simibubi.create.content.schematics.client.SchematicTransformation;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class MoveTool extends PlacementToolBase {

    @Override
    public void init() {
        super.init();
        this.renderSelectedFace = true;
    }

    @Override
    public void updateSelection() {
        super.updateSelection();
    }

    @Override
    public boolean handleMouseWheel(double delta) {
        if (this.schematicSelected && this.selectedFace.getAxis().isHorizontal()) {
            SchematicTransformation transformation = this.schematicHandler.getTransformation();
            Vec3 vec = Vec3.atLowerCornerOf(this.selectedFace.getNormal()).scale(-Math.signum(delta));
            vec = vec.multiply((double) transformation.getMirrorModifier(Direction.Axis.X), 1.0, (double) transformation.getMirrorModifier(Direction.Axis.Z));
            vec = VecHelper.rotate(vec, (double) transformation.getRotationTarget(), Direction.Axis.Y);
            transformation.move((int) vec.x, 0, (int) vec.z);
            this.schematicHandler.markDirty();
            return true;
        } else {
            return true;
        }
    }
}