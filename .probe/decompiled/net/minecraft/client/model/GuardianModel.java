package net.minecraft.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.phys.Vec3;

public class GuardianModel extends HierarchicalModel<Guardian> {

    private static final float[] SPIKE_X_ROT = new float[] { 1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F };

    private static final float[] SPIKE_Y_ROT = new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F };

    private static final float[] SPIKE_Z_ROT = new float[] { 0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F };

    private static final float[] SPIKE_X = new float[] { 0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F };

    private static final float[] SPIKE_Y = new float[] { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F };

    private static final float[] SPIKE_Z = new float[] { 8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F };

    private static final String EYE = "eye";

    private static final String TAIL_0 = "tail0";

    private static final String TAIL_1 = "tail1";

    private static final String TAIL_2 = "tail2";

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart eye;

    private final ModelPart[] spikeParts;

    private final ModelPart[] tailParts;

    public GuardianModel(ModelPart modelPart0) {
        this.root = modelPart0;
        this.spikeParts = new ModelPart[12];
        this.head = modelPart0.getChild("head");
        for (int $$1 = 0; $$1 < this.spikeParts.length; $$1++) {
            this.spikeParts[$$1] = this.head.getChild(createSpikeName($$1));
        }
        this.eye = this.head.getChild("eye");
        this.tailParts = new ModelPart[3];
        this.tailParts[0] = this.head.getChild("tail0");
        this.tailParts[1] = this.tailParts[0].getChild("tail1");
        this.tailParts[2] = this.tailParts[1].getChild("tail2");
    }

    private static String createSpikeName(int int0) {
        return "spike" + int0;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12.0F, 12.0F, 16.0F).texOffs(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F).texOffs(0, 28).addBox(6.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F, true).texOffs(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 2.0F, 12.0F).texOffs(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12.0F, 2.0F, 12.0F), PartPose.ZERO);
        CubeListBuilder $$3 = CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F);
        for (int $$4 = 0; $$4 < 12; $$4++) {
            float $$5 = getSpikeX($$4, 0.0F, 0.0F);
            float $$6 = getSpikeY($$4, 0.0F, 0.0F);
            float $$7 = getSpikeZ($$4, 0.0F, 0.0F);
            float $$8 = (float) Math.PI * SPIKE_X_ROT[$$4];
            float $$9 = (float) Math.PI * SPIKE_Y_ROT[$$4];
            float $$10 = (float) Math.PI * SPIKE_Z_ROT[$$4];
            $$2.addOrReplaceChild(createSpikeName($$4), $$3, PartPose.offsetAndRotation($$5, $$6, $$7, $$8, $$9, $$10));
        }
        $$2.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 15.0F, 0.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 0.0F, -8.25F));
        PartDefinition $$11 = $$2.addOrReplaceChild("tail0", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, 14.0F, 7.0F, 4.0F, 4.0F, 8.0F), PartPose.ZERO);
        PartDefinition $$12 = $$11.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, 14.0F, 0.0F, 3.0F, 3.0F, 7.0F), PartPose.offset(-1.5F, 0.5F, 14.0F));
        $$12.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(41, 32).addBox(0.0F, 14.0F, 0.0F, 2.0F, 2.0F, 6.0F).texOffs(25, 19).addBox(1.0F, 10.5F, 3.0F, 1.0F, 9.0F, 9.0F), PartPose.offset(0.5F, 0.5F, 6.0F));
        return LayerDefinition.create($$0, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(Guardian guardian0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = float3 - (float) guardian0.f_19797_;
        this.head.yRot = float4 * (float) (Math.PI / 180.0);
        this.head.xRot = float5 * (float) (Math.PI / 180.0);
        float $$7 = (1.0F - guardian0.getSpikesAnimation($$6)) * 0.55F;
        this.setupSpikes(float3, $$7);
        Entity $$8 = Minecraft.getInstance().getCameraEntity();
        if (guardian0.hasActiveAttackTarget()) {
            $$8 = guardian0.getActiveAttackTarget();
        }
        if ($$8 != null) {
            Vec3 $$9 = $$8.getEyePosition(0.0F);
            Vec3 $$10 = guardian0.m_20299_(0.0F);
            double $$11 = $$9.y - $$10.y;
            if ($$11 > 0.0) {
                this.eye.y = 0.0F;
            } else {
                this.eye.y = 1.0F;
            }
            Vec3 $$12 = guardian0.m_20252_(0.0F);
            $$12 = new Vec3($$12.x, 0.0, $$12.z);
            Vec3 $$13 = new Vec3($$10.x - $$9.x, 0.0, $$10.z - $$9.z).normalize().yRot((float) (Math.PI / 2));
            double $$14 = $$12.dot($$13);
            this.eye.x = Mth.sqrt((float) Math.abs($$14)) * 2.0F * (float) Math.signum($$14);
        }
        this.eye.visible = true;
        float $$15 = guardian0.getTailAnimation($$6);
        this.tailParts[0].yRot = Mth.sin($$15) * (float) Math.PI * 0.05F;
        this.tailParts[1].yRot = Mth.sin($$15) * (float) Math.PI * 0.1F;
        this.tailParts[2].yRot = Mth.sin($$15) * (float) Math.PI * 0.15F;
    }

    private void setupSpikes(float float0, float float1) {
        for (int $$2 = 0; $$2 < 12; $$2++) {
            this.spikeParts[$$2].x = getSpikeX($$2, float0, float1);
            this.spikeParts[$$2].y = getSpikeY($$2, float0, float1);
            this.spikeParts[$$2].z = getSpikeZ($$2, float0, float1);
        }
    }

    private static float getSpikeOffset(int int0, float float1, float float2) {
        return 1.0F + Mth.cos(float1 * 1.5F + (float) int0) * 0.01F - float2;
    }

    private static float getSpikeX(int int0, float float1, float float2) {
        return SPIKE_X[int0] * getSpikeOffset(int0, float1, float2);
    }

    private static float getSpikeY(int int0, float float1, float float2) {
        return 16.0F + SPIKE_Y[int0] * getSpikeOffset(int0, float1, float2);
    }

    private static float getSpikeZ(int int0, float float1, float float2) {
        return SPIKE_Z[int0] * getSpikeOffset(int0, float1, float2);
    }
}