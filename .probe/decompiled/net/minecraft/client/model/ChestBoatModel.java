package net.minecraft.client.model;

import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ChestBoatModel extends BoatModel {

    private static final String CHEST_BOTTOM = "chest_bottom";

    private static final String CHEST_LID = "chest_lid";

    private static final String CHEST_LOCK = "chest_lock";

    public ChestBoatModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    @Override
    protected Builder<ModelPart> createPartsBuilder(ModelPart modelPart0) {
        Builder<ModelPart> $$1 = super.createPartsBuilder(modelPart0);
        $$1.add(modelPart0.getChild("chest_bottom"));
        $$1.add(modelPart0.getChild("chest_lid"));
        $$1.add(modelPart0.getChild("chest_lock"));
        return $$1;
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        BoatModel.createChildren($$1);
        $$1.addOrReplaceChild("chest_bottom", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 12.0F, 8.0F, 12.0F), PartPose.offsetAndRotation(-2.0F, -5.0F, -6.0F, 0.0F, (float) (-Math.PI / 2), 0.0F));
        $$1.addOrReplaceChild("chest_lid", CubeListBuilder.create().texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 12.0F, 4.0F, 12.0F), PartPose.offsetAndRotation(-2.0F, -9.0F, -6.0F, 0.0F, (float) (-Math.PI / 2), 0.0F));
        $$1.addOrReplaceChild("chest_lock", CubeListBuilder.create().texOffs(0, 59).addBox(0.0F, 0.0F, 0.0F, 2.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, -6.0F, -1.0F, 0.0F, (float) (-Math.PI / 2), 0.0F));
        return LayerDefinition.create($$0, 128, 128);
    }
}