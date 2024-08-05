package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Ravager;

public class RavagerModel extends HierarchicalModel<Ravager> {

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart mouth;

    private final ModelPart rightHindLeg;

    private final ModelPart leftHindLeg;

    private final ModelPart rightFrontLeg;

    private final ModelPart leftFrontLeg;

    private final ModelPart neck;

    public RavagerModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.neck = modelPart0.getChild("neck");
        this.head = this.neck.getChild("head");
        this.mouth = this.head.getChild("mouth");
        this.rightHindLeg = modelPart0.getChild("right_hind_leg");
        this.leftHindLeg = modelPart0.getChild("left_hind_leg");
        this.rightFrontLeg = modelPart0.getChild("right_front_leg");
        this.leftFrontLeg = modelPart0.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        int $$2 = 16;
        PartDefinition $$3 = $$1.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(68, 73).addBox(-5.0F, -1.0F, -18.0F, 10.0F, 10.0F, 18.0F), PartPose.offset(0.0F, -7.0F, 5.5F));
        PartDefinition $$4 = $$3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -20.0F, -14.0F, 16.0F, 20.0F, 16.0F).texOffs(0, 0).addBox(-2.0F, -6.0F, -18.0F, 4.0F, 8.0F, 4.0F), PartPose.offset(0.0F, 16.0F, -17.0F));
        $$4.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(74, 55).addBox(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(-10.0F, -14.0F, -8.0F, 1.0995574F, 0.0F, 0.0F));
        $$4.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(74, 55).mirror().addBox(0.0F, -14.0F, -2.0F, 2.0F, 14.0F, 4.0F), PartPose.offsetAndRotation(8.0F, -14.0F, -8.0F, 1.0995574F, 0.0F, 0.0F));
        $$4.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 36).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 3.0F, 16.0F), PartPose.offset(0.0F, -2.0F, 2.0F));
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 55).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 16.0F, 20.0F).texOffs(0, 91).addBox(-6.0F, 6.0F, -7.0F, 12.0F, 13.0F, 18.0F), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
        $$1.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(96, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), PartPose.offset(-8.0F, -13.0F, 18.0F));
        $$1.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(96, 0).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), PartPose.offset(8.0F, -13.0F, 18.0F));
        $$1.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(64, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), PartPose.offset(-8.0F, -13.0F, -5.0F));
        $$1.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 37.0F, 8.0F), PartPose.offset(8.0F, -13.0F, -5.0F));
        return LayerDefinition.create($$0, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(Ravager ravager0, float float1, float float2, float float3, float float4, float float5) {
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        float $$6 = 0.4F * float2;
        this.rightHindLeg.xRot = Mth.cos(float1 * 0.6662F) * $$6;
        this.leftHindLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * $$6;
        this.rightFrontLeg.xRot = Mth.cos(float1 * 0.6662F + (float) Math.PI) * $$6;
        this.leftFrontLeg.xRot = Mth.cos(float1 * 0.6662F) * $$6;
    }

    public void prepareMobModel(Ravager ravager0, float float1, float float2, float float3) {
        super.m_6839_(ravager0, float1, float2, float3);
        int $$4 = ravager0.getStunnedTick();
        int $$5 = ravager0.getRoarTick();
        int $$6 = 20;
        int $$7 = ravager0.getAttackTick();
        int $$8 = 10;
        if ($$7 > 0) {
            float $$9 = Mth.triangleWave((float) $$7 - float3, 10.0F);
            float $$10 = (1.0F + $$9) * 0.5F;
            float $$11 = $$10 * $$10 * $$10 * 12.0F;
            float $$12 = $$11 * Mth.sin(this.neck.xRot);
            this.neck.z = -6.5F + $$11;
            this.neck.y = -7.0F - $$12;
            float $$13 = Mth.sin(((float) $$7 - float3) / 10.0F * (float) Math.PI * 0.25F);
            this.mouth.xRot = (float) (Math.PI / 2) * $$13;
            if ($$7 > 5) {
                this.mouth.xRot = Mth.sin(((float) (-4 + $$7) - float3) / 4.0F) * (float) Math.PI * 0.4F;
            } else {
                this.mouth.xRot = (float) (Math.PI / 20) * Mth.sin((float) Math.PI * ((float) $$7 - float3) / 10.0F);
            }
        } else {
            float $$14 = -1.0F;
            float $$15 = -1.0F * Mth.sin(this.neck.xRot);
            this.neck.x = 0.0F;
            this.neck.y = -7.0F - $$15;
            this.neck.z = 5.5F;
            boolean $$16 = $$4 > 0;
            this.neck.xRot = $$16 ? 0.21991149F : 0.0F;
            this.mouth.xRot = (float) Math.PI * ($$16 ? 0.05F : 0.01F);
            if ($$16) {
                double $$17 = (double) $$4 / 40.0;
                this.neck.x = (float) Math.sin($$17 * 10.0) * 3.0F;
            } else if ($$5 > 0) {
                float $$18 = Mth.sin(((float) (20 - $$5) - float3) / 20.0F * (float) Math.PI * 0.25F);
                this.mouth.xRot = (float) (Math.PI / 2) * $$18;
            }
        }
    }
}