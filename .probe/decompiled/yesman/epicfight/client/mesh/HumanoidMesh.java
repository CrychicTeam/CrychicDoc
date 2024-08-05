package yesman.epicfight.client.mesh;

import java.util.Map;
import net.minecraft.world.entity.EquipmentSlot;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.ModelPart;
import yesman.epicfight.api.client.model.VertexIndicator;

public class HumanoidMesh extends AnimatedMesh {

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> head;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> torso;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> lefrArm;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightArm;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightLeg;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> hat;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> jacket;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftSleeve;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightSleeve;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> leftPants;

    public final ModelPart<VertexIndicator.AnimatedVertexIndicator> rightPants;

    public HumanoidMesh(Map<String, float[]> arrayMap, AnimatedMesh parent, Mesh.RenderProperties properties, Map<String, ModelPart<VertexIndicator.AnimatedVertexIndicator>> parts) {
        super(arrayMap, parent, properties, parts);
        this.head = this.getOrLogException(parts, "head");
        this.torso = this.getOrLogException(parts, "torso");
        this.lefrArm = this.getOrLogException(parts, "leftArm");
        this.rightArm = this.getOrLogException(parts, "rightArm");
        this.leftLeg = this.getOrLogException(parts, "leftLeg");
        this.rightLeg = this.getOrLogException(parts, "rightLeg");
        this.hat = this.getOrLogException(parts, "hat");
        this.jacket = this.getOrLogException(parts, "jacket");
        this.leftSleeve = this.getOrLogException(parts, "leftSleeve");
        this.rightSleeve = this.getOrLogException(parts, "rightSleeve");
        this.leftPants = this.getOrLogException(parts, "leftPants");
        this.rightPants = this.getOrLogException(parts, "rightPants");
    }

    public AnimatedMesh getHumanoidArmorModel(EquipmentSlot slot) {
        switch(slot) {
            case HEAD:
                return Meshes.HELMET;
            case CHEST:
                return Meshes.CHESTPLATE;
            case LEGS:
                return Meshes.LEGGINS;
            case FEET:
                return Meshes.BOOTS;
            default:
                return null;
        }
    }
}