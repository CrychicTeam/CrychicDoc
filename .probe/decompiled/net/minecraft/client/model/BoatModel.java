package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;

public class BoatModel extends ListModel<Boat> implements WaterPatchModel {

    private static final String LEFT_PADDLE = "left_paddle";

    private static final String RIGHT_PADDLE = "right_paddle";

    private static final String WATER_PATCH = "water_patch";

    private static final String BOTTOM = "bottom";

    private static final String BACK = "back";

    private static final String FRONT = "front";

    private static final String RIGHT = "right";

    private static final String LEFT = "left";

    private final ModelPart leftPaddle;

    private final ModelPart rightPaddle;

    private final ModelPart waterPatch;

    private final ImmutableList<ModelPart> parts;

    public BoatModel(ModelPart modelPart0) {
        this.leftPaddle = modelPart0.getChild("left_paddle");
        this.rightPaddle = modelPart0.getChild("right_paddle");
        this.waterPatch = modelPart0.getChild("water_patch");
        this.parts = this.createPartsBuilder(modelPart0).build();
    }

    protected Builder<ModelPart> createPartsBuilder(ModelPart modelPart0) {
        Builder<ModelPart> $$1 = new Builder();
        $$1.add(new ModelPart[] { modelPart0.getChild("bottom"), modelPart0.getChild("back"), modelPart0.getChild("front"), modelPart0.getChild("right"), modelPart0.getChild("left"), this.leftPaddle, this.rightPaddle });
        return $$1;
    }

    public static void createChildren(PartDefinition partDefinition0) {
        int $$1 = 32;
        int $$2 = 6;
        int $$3 = 20;
        int $$4 = 4;
        int $$5 = 28;
        partDefinition0.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 3.0F, 1.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        partDefinition0.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 19).addBox(-13.0F, -7.0F, -1.0F, 18.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(-15.0F, 4.0F, 4.0F, 0.0F, (float) (Math.PI * 3.0 / 2.0), 0.0F));
        partDefinition0.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 27).addBox(-8.0F, -7.0F, -1.0F, 16.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(15.0F, 4.0F, 0.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
        partDefinition0.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 35).addBox(-14.0F, -7.0F, -1.0F, 28.0F, 6.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 4.0F, -9.0F, 0.0F, (float) Math.PI, 0.0F));
        partDefinition0.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 43).addBox(-14.0F, -7.0F, -1.0F, 28.0F, 6.0F, 2.0F), PartPose.offset(0.0F, 4.0F, 9.0F));
        int $$6 = 20;
        int $$7 = 7;
        int $$8 = 6;
        float $$9 = -5.0F;
        partDefinition0.addOrReplaceChild("left_paddle", CubeListBuilder.create().texOffs(62, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -5.0F, 9.0F, 0.0F, 0.0F, (float) (Math.PI / 16)));
        partDefinition0.addOrReplaceChild("right_paddle", CubeListBuilder.create().texOffs(62, 20).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -5.0F, -9.0F, 0.0F, (float) Math.PI, (float) (Math.PI / 16)));
        partDefinition0.addOrReplaceChild("water_patch", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        createChildren($$1);
        return LayerDefinition.create($$0, 128, 64);
    }

    public void setupAnim(Boat boat0, float float1, float float2, float float3, float float4, float float5) {
        animatePaddle(boat0, 0, this.leftPaddle, float1);
        animatePaddle(boat0, 1, this.rightPaddle, float1);
    }

    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }

    @Override
    public ModelPart waterPatch() {
        return this.waterPatch;
    }

    private static void animatePaddle(Boat boat0, int int1, ModelPart modelPart2, float float3) {
        float $$4 = boat0.getRowingTime(int1, float3);
        modelPart2.xRot = Mth.clampedLerp((float) (-Math.PI / 3), (float) (-Math.PI / 12), (Mth.sin(-$$4) + 1.0F) / 2.0F);
        modelPart2.yRot = Mth.clampedLerp((float) (-Math.PI / 4), (float) (Math.PI / 4), (Mth.sin(-$$4 + 1.0F) + 1.0F) / 2.0F);
        if (int1 == 1) {
            modelPart2.yRot = (float) Math.PI - modelPart2.yRot;
        }
    }
}