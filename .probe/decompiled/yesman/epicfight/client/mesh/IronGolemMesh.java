package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class IronGolemMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> head;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> chest;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> core;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftArm;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightArm;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightLeg;

    public IronGolemMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.head = this.getOrLogException(parts, "head");
        this.chest = this.getOrLogException(parts, "chest");
        this.core = this.getOrLogException(parts, "core");
        this.leftArm = this.getOrLogException(parts, "leftArm");
        this.rightArm = this.getOrLogException(parts, "rightArm");
        this.leftLeg = this.getOrLogException(parts, "leftLeg");
        this.rightLeg = this.getOrLogException(parts, "rightLeg");
    }
}