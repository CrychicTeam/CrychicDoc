package net.minecraft.client.model;

import java.util.Arrays;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class BlazeModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;

    private final ModelPart[] upperBodyParts;

    private final ModelPart head;

    public BlazeModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.head = modelPart0.getChild("head");
        this.upperBodyParts = new ModelPart[12];
        Arrays.setAll(this.upperBodyParts, p_170449_ -> modelPart0.getChild(getPartName(p_170449_)));
    }

    private static String getPartName(int int0) {
        return "part" + int0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        float $$2 = 0.0F;
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F);
        for (int $$4 = 0; $$4 < 4; $$4++) {
            float $$5 = Mth.cos($$2) * 9.0F;
            float $$6 = -2.0F + Mth.cos((float) ($$4 * 2) * 0.25F);
            float $$7 = Mth.sin($$2) * 9.0F;
            $$1.addOrReplaceChild(getPartName($$4), $$3, PartPose.offset($$5, $$6, $$7));
            $$2++;
        }
        $$2 = (float) (Math.PI / 4);
        for (int $$8 = 4; $$8 < 8; $$8++) {
            float $$9 = Mth.cos($$2) * 7.0F;
            float $$10 = 2.0F + Mth.cos((float) ($$8 * 2) * 0.25F);
            float $$11 = Mth.sin($$2) * 7.0F;
            $$1.addOrReplaceChild(getPartName($$8), $$3, PartPose.offset($$9, $$10, $$11));
            $$2++;
        }
        $$2 = 0.47123894F;
        for (int $$12 = 8; $$12 < 12; $$12++) {
            float $$13 = Mth.cos($$2) * 5.0F;
            float $$14 = 11.0F + Mth.cos((float) $$12 * 1.5F * 0.5F);
            float $$15 = Mth.sin($$2) * 5.0F;
            $$1.addOrReplaceChild(getPartName($$12), $$3, PartPose.offset($$13, $$14, $$15));
            $$2++;
        }
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = float3 * (float) Math.PI * -0.1F;
        for (int $$7 = 0; $$7 < 4; $$7++) {
            this.upperBodyParts[$$7].y = -2.0F + Mth.cos(((float) ($$7 * 2) + float3) * 0.25F);
            this.upperBodyParts[$$7].x = Mth.cos($$6) * 9.0F;
            this.upperBodyParts[$$7].z = Mth.sin($$6) * 9.0F;
            $$6++;
        }
        $$6 = (float) (Math.PI / 4) + float3 * (float) Math.PI * 0.03F;
        for (int $$8 = 4; $$8 < 8; $$8++) {
            this.upperBodyParts[$$8].y = 2.0F + Mth.cos(((float) ($$8 * 2) + float3) * 0.25F);
            this.upperBodyParts[$$8].x = Mth.cos($$6) * 7.0F;
            this.upperBodyParts[$$8].z = Mth.sin($$6) * 7.0F;
            $$6++;
        }
        $$6 = 0.47123894F + float3 * (float) Math.PI * -0.05F;
        for (int $$9 = 8; $$9 < 12; $$9++) {
            this.upperBodyParts[$$9].y = 11.0F + Mth.cos(((float) $$9 * 1.5F + float3) * 0.5F);
            this.upperBodyParts[$$9].x = Mth.cos($$6) * 5.0F;
            this.upperBodyParts[$$9].z = Mth.sin($$6) * 5.0F;
            $$6++;
        }
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
    }
}