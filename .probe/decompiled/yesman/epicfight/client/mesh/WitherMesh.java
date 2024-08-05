package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class WitherMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> centerHead;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftHead;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightHead;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> ribcage;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> tail;

    public WitherMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.centerHead = this.getOrLogException(parts, "centerHead");
        this.leftHead = this.getOrLogException(parts, "leftHead");
        this.rightHead = this.getOrLogException(parts, "rightHead");
        this.ribcage = this.getOrLogException(parts, "ribcage");
        this.tail = this.getOrLogException(parts, "tail");
    }
}