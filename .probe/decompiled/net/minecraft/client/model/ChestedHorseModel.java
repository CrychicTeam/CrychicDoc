package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class ChestedHorseModel<T extends AbstractChestedHorse> extends HorseModel<T> {

    private final ModelPart leftChest = this.f_102751_.getChild("left_chest");

    private final ModelPart rightChest = this.f_102751_.getChild("right_chest");

    public ChestedHorseModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = HorseModel.createBodyMesh(CubeDeformation.NONE);
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.getChild("body");
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(26, 21).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 3.0F);
        $$2.addOrReplaceChild("left_chest", $$3, PartPose.offsetAndRotation(6.0F, -8.0F, 0.0F, 0.0F, (float) (-Math.PI / 2), 0.0F));
        $$2.addOrReplaceChild("right_chest", $$3, PartPose.offsetAndRotation(-6.0F, -8.0F, 0.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
        PartDefinition $$4 = $$1.getChild("head_parts").getChild("head");
        CubeListBuilder $$5 = CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 1.0F);
        $$4.addOrReplaceChild("left_ear", $$5, PartPose.offsetAndRotation(1.25F, -10.0F, 4.0F, (float) (Math.PI / 12), 0.0F, (float) (Math.PI / 12)));
        $$4.addOrReplaceChild("right_ear", $$5, PartPose.offsetAndRotation(-1.25F, -10.0F, 4.0F, (float) (Math.PI / 12), 0.0F, (float) (-Math.PI / 12)));
        return LayerDefinition.create($$0, 64, 64);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        super.setupAnim(t0, float1, float2, float3, float4, float5);
        if (t0.hasChest()) {
            this.leftChest.visible = true;
            this.rightChest.visible = true;
        } else {
            this.leftChest.visible = false;
            this.rightChest.visible = false;
        }
    }
}