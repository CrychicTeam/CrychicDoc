package com.simibubi.create.content.schematics.client.tools;

public class PlaceTool extends SchematicToolBase {

    @Override
    public boolean handleRightClick() {
        this.schematicHandler.printInstantly();
        return true;
    }

    @Override
    public boolean handleMouseWheel(double delta) {
        return false;
    }
}