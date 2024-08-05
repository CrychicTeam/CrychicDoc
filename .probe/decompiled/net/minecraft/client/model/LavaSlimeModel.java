package net.minecraft.client.model;

import java.util.Arrays;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;

public class LavaSlimeModel<T extends Slime> extends HierarchicalModel<T> {

    private static final int SEGMENT_COUNT = 8;

    private final ModelPart root;

    private final ModelPart[] bodyCubes = new ModelPart[8];

    public LavaSlimeModel(ModelPart modelPart0) {
        this.root = modelPart0;
        Arrays.setAll(this.bodyCubes, p_170709_ -> modelPart0.getChild(getSegmentName(p_170709_)));
    }

    private static String getSegmentName(int int0) {
        return "cube" + int0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        for (int $$2 = 0; $$2 < 8; $$2++) {
            int $$3 = 0;
            int $$4 = $$2;
            if ($$2 == 2) {
                $$3 = 24;
                $$4 = 10;
            } else if ($$2 == 3) {
                $$3 = 24;
                $$4 = 19;
            }
            $$1.addOrReplaceChild(getSegmentName($$2), CubeListBuilder.create().texOffs($$3, $$4).addBox(-4.0F, (float) (16 + $$2), -4.0F, 8.0F, 1.0F, 8.0F), PartPose.ZERO);
        }
        $$1.addOrReplaceChild("inside_cube", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 18.0F, -2.0F, 4.0F, 4.0F, 4.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 32);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        float $$4 = Mth.lerp(float3, t0.oSquish, t0.squish);
        if ($$4 < 0.0F) {
            $$4 = 0.0F;
        }
        for (int $$5 = 0; $$5 < this.bodyCubes.length; $$5++) {
            this.bodyCubes[$$5].y = (float) (-(4 - $$5)) * $$4 * 1.7F;
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}