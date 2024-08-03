package net.minecraft.client.model.geom.builders;

import net.minecraft.client.model.geom.ModelPart;

public class LayerDefinition {

    private final MeshDefinition mesh;

    private final MaterialDefinition material;

    private LayerDefinition(MeshDefinition meshDefinition0, MaterialDefinition materialDefinition1) {
        this.mesh = meshDefinition0;
        this.material = materialDefinition1;
    }

    public ModelPart bakeRoot() {
        return this.mesh.getRoot().bake(this.material.xTexSize, this.material.yTexSize);
    }

    public static LayerDefinition create(MeshDefinition meshDefinition0, int int1, int int2) {
        return new LayerDefinition(meshDefinition0, new MaterialDefinition(int1, int2));
    }
}