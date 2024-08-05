package net.minecraft.client.model;

import java.util.Arrays;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class SquidModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart[] tentacles = new ModelPart[8];

    private final ModelPart root;

    public SquidModel(ModelPart modelPart0) {
        this.root = modelPart0;
        Arrays.setAll(this.tentacles, p_170995_ -> modelPart0.getChild(createTentacleName(p_170995_)));
    }

    private static String createTentacleName(int int0) {
        return "tentacle" + int0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        CubeDeformation $$2 = new CubeDeformation(0.02F);
        int $$3 = -16;
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 16.0F, 12.0F, $$2), PartPose.offset(0.0F, 8.0F, 0.0F));
        int $$4 = 8;
        CubeListBuilder $$5 = CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F);
        for (int $$6 = 0; $$6 < 8; $$6++) {
            double $$7 = (double) $$6 * Math.PI * 2.0 / 8.0;
            float $$8 = (float) Math.cos($$7) * 5.0F;
            float $$9 = 15.0F;
            float $$10 = (float) Math.sin($$7) * 5.0F;
            $$7 = (double) $$6 * Math.PI * -2.0 / 8.0 + (Math.PI / 2);
            float $$11 = (float) $$7;
            $$1.addOrReplaceChild(createTentacleName($$6), $$5, PartPose.offsetAndRotation($$8, 15.0F, $$10, 0.0F, $$11, 0.0F));
        }
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        for (ModelPart $$6 : this.tentacles) {
            $$6.xRot = float3;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}