package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class HoglinMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> head;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> body;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftFrontLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightFrontLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftBackLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightBackLeg;

    public HoglinMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.head = this.getOrLogException(parts, "head");
        this.body = this.getOrLogException(parts, "body");
        this.leftFrontLeg = this.getOrLogException(parts, "leftFrontLeg");
        this.rightFrontLeg = this.getOrLogException(parts, "rightFrontLeg");
        this.leftBackLeg = this.getOrLogException(parts, "leftBackLeg");
        this.rightBackLeg = this.getOrLogException(parts, "rightBackLeg");
    }
}