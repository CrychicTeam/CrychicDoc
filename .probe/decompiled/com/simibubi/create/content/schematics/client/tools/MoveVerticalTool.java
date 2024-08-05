package com.simibubi.create.content.schematics.client.tools;

import net.minecraft.util.Mth;

public class MoveVerticalTool extends PlacementToolBase {

    @Override
    public boolean handleMouseWheel(double delta) {
        if (this.schematicHandler.isDeployed()) {
            this.schematicHandler.getTransformation().move(0, Mth.sign(delta), 0);
            this.schematicHandler.markDirty();
        }
        return true;
    }
}