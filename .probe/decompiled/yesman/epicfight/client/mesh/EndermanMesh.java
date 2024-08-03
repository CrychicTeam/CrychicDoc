package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class EndermanMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> headTop;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> headBottom;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> torso;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftArm;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightArm;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightLeg;

    public EndermanMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.headTop = this.getOrLogException(parts, "headTop");
        this.headBottom = this.getOrLogException(parts, "headBottom");
        this.torso = this.getOrLogException(parts, "torso");
        this.leftArm = this.getOrLogException(parts, "leftArm");
        this.rightArm = this.getOrLogException(parts, "rightArm");
        this.leftLeg = this.getOrLogException(parts, "leftLeg");
        this.rightLeg = this.getOrLogException(parts, "rightLeg");
    }
}