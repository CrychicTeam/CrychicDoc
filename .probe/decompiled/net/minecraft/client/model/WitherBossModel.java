package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.wither.WitherBoss;

public class WitherBossModel<T extends WitherBoss> extends HierarchicalModel<T> {

    private static final String RIBCAGE = "ribcage";

    private static final String CENTER_HEAD = "center_head";

    private static final String RIGHT_HEAD = "right_head";

    private static final String LEFT_HEAD = "left_head";

    private static final float RIBCAGE_X_ROT_OFFSET = 0.065F;

    private static final float TAIL_X_ROT_OFFSET = 0.265F;

    private final ModelPart root;

    private final ModelPart centerHead;

    private final ModelPart rightHead;

    private final ModelPart leftHead;

    private final ModelPart ribcage;

    private final ModelPart tail;

    public WitherBossModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.ribcage = modelPart0.getChild("ribcage");
        this.tail = modelPart0.getChild("tail");
        this.centerHead = modelPart0.getChild("center_head");
        this.rightHead = modelPart0.getChild("right_head");
        this.leftHead = modelPart0.getChild("left_head");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation0) {
        MeshDefinition $$1 = new MeshDefinition();
        PartDefinition $$2 = $$1.getRoot();
        $$2.addOrReplaceChild("shoulders", CubeListBuilder.create().texOffs(0, 16).addBox(-10.0F, 3.9F, -0.5F, 20.0F, 3.0F, 3.0F, cubeDeformation0), PartPose.ZERO);
        float $$3 = 0.20420352F;
        $$2.addOrReplaceChild("ribcage", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 10.0F, 3.0F, cubeDeformation0).texOffs(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11.0F, 2.0F, 2.0F, cubeDeformation0).texOffs(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11.0F, 2.0F, 2.0F, cubeDeformation0).texOffs(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11.0F, 2.0F, 2.0F, cubeDeformation0), PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F));
        $$2.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(12, 22).addBox(0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 3.0F, cubeDeformation0), PartPose.offsetAndRotation(-2.0F, 6.9F + Mth.cos(0.20420352F) * 10.0F, -0.5F + Mth.sin(0.20420352F) * 10.0F, 0.83252203F, 0.0F, 0.0F));
        $$2.addOrReplaceChild("center_head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation0), PartPose.ZERO);
        CubeListBuilder $$4 = CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 6.0F, 6.0F, 6.0F, cubeDeformation0);
        $$2.addOrReplaceChild("right_head", $$4, PartPose.offset(-8.0F, 4.0F, 0.0F));
        $$2.addOrReplaceChild("left_head", $$4, PartPose.offset(10.0F, 4.0F, 0.0F));
        return LayerDefinition.create($$1, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = Mth.cos(float3 * 0.1F);
        this.ribcage.xRot = (0.065F + 0.05F * $$6) * (float) Math.PI;
        this.tail.setPos(-2.0F, 6.9F + Mth.cos(this.ribcage.xRot) * 10.0F, -0.5F + Mth.sin(this.ribcage.xRot) * 10.0F);
        this.tail.xRot = (0.265F + 0.1F * $$6) * (float) Math.PI;
        this.centerHead.yRot = float4 * (float) (Math.PI / 180.0);
        this.centerHead.xRot = float5 * (float) (Math.PI / 180.0);
    }

    public void prepareMobModel(T t0, float float1, float float2, float float3) {
        setupHeadRotation(t0, this.rightHead, 0);
        setupHeadRotation(t0, this.leftHead, 1);
    }

    private static <T extends WitherBoss> void setupHeadRotation(T t0, ModelPart modelPart1, int int2) {
        modelPart1.yRot = (t0.getHeadYRot(int2) - t0.f_20883_) * (float) (Math.PI / 180.0);
        modelPart1.xRot = t0.getHeadXRot(int2) * (float) (Math.PI / 180.0);
    }
}