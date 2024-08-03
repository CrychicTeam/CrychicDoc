package yesman.epicfight.client.mesh;

import java.util.Map;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class CreeperMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> head;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> torso;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> legRF;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> legLF;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> legRB;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> legLB;

    public CreeperMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.head = this.getOrLogException(parts, "head");
        this.torso = this.getOrLogException(parts, "torso");
        this.legRF = this.getOrLogException(parts, "legRF");
        this.legLF = this.getOrLogException(parts, "legLF");
        this.legRB = this.getOrLogException(parts, "legRB");
        this.legLB = this.getOrLogException(parts, "legLB");
    }
}