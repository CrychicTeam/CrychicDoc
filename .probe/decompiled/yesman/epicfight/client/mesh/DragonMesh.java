package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class DragonMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> head;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> neck;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> torso;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftLegFront;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightLegFront;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftLegBack;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightLegBack;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftWing;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightWing;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> tail;

    public DragonMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.head = this.getOrLogException(parts, "head");
        this.neck = this.getOrLogException(parts, "neck");
        this.torso = this.getOrLogException(parts, "torso");
        this.leftLegFront = this.getOrLogException(parts, "leftLegFront");
        this.rightLegFront = this.getOrLogException(parts, "rightLegFront");
        this.leftLegBack = this.getOrLogException(parts, "leftLegBack");
        this.rightLegBack = this.getOrLogException(parts, "rightLegBack");
        this.leftWing = this.getOrLogException(parts, "leftWing");
        this.rightWing = this.getOrLogException(parts, "rightWing");
        this.tail = this.getOrLogException(parts, "tail");
    }
}