package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class GhastModel<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;

    private final ModelPart[] tentacles = new ModelPart[9];

    public GhastModel(ModelPart modelPart0) {
        this.root = modelPart0;
        for (int $$1 = 0; $$1 < this.tentacles.length; $$1++) {
            this.tentacles[$$1] = modelPart0.getChild(createTentacleName($$1));
        }
    }

    private static String createTentacleName(int int0) {
        return "tentacle" + int0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.offset(0.0F, 17.6F, 0.0F));
        RandomSource $$2 = RandomSource.create(1660L);
        for (int $$3 = 0; $$3 < 9; $$3++) {
            float $$4 = (((float) ($$3 % 3) - (float) ($$3 / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float $$5 = ((float) ($$3 / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int $$6 = $$2.nextInt(7) + 8;
            $$1.addOrReplaceChild(createTentacleName($$3), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, (float) $$6, 2.0F), PartPose.offset($$4, 24.6F, $$5));
        }
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        for (int $$6 = 0; $$6 < this.tentacles.length; $$6++) {
            this.tentacles[$$6].xRot = 0.2F * Mth.sin(float3 * 0.3F + (float) $$6) + 0.4F;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}