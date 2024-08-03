package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class VexMesh extends HumanoidMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftWing;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightWing;

    public VexMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.leftWing = this.getOrLogException(parts, "leftWing");
        this.rightWing = this.getOrLogException(parts, "rightWing");
    }
}