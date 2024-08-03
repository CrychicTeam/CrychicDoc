package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.definitions.CamelAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.camel.Camel;

public class CamelModel<T extends Camel> extends HierarchicalModel<T> {

    private static final float MAX_WALK_ANIMATION_SPEED = 2.0F;

    private static final float WALK_ANIMATION_SCALE_FACTOR = 2.5F;

    private static final float BABY_SCALE = 0.45F;

    private static final float BABY_Y_OFFSET = 29.35F;

    private static final String SADDLE = "saddle";

    private static final String BRIDLE = "bridle";

    private static final String REINS = "reins";

    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart[] saddleParts;

    private final ModelPart[] ridingParts;

    public CamelModel(ModelPart modelPart0) {
        this.root = modelPart0;
        ModelPart $$1 = modelPart0.getChild("body");
        this.head = $$1.getChild("head");
        this.saddleParts = new ModelPart[] { $$1.getChild("saddle"), this.head.getChild("bridle") };
        this.ridingParts = new ModelPart[] { this.head.getChild("reins") };
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        CubeDeformation $$2 = new CubeDeformation(0.1F);
        PartDefinition $$3 = $$1.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 25).addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F), PartPose.offset(0.0F, 4.0F, 9.5F));
        $$3.addOrReplaceChild("hump", CubeListBuilder.create().texOffs(74, 0).addBox(-4.5F, -5.0F, -5.5F, 9.0F, 5.0F, 11.0F), PartPose.offset(0.0F, -12.0F, -10.0F));
        $$3.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(122, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 0.0F), PartPose.offset(0.0F, -9.0F, 3.5F));
        PartDefinition $$4 = $$3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(60, 24).addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F).texOffs(21, 0).addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F).texOffs(50, 0).addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F), PartPose.offset(0.0F, -3.0F, -19.5F));
        $$4.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(45, 0).addBox(-0.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.offset(3.0F, -21.0F, -9.5F));
        $$4.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(67, 0).addBox(-2.5F, 0.5F, -1.0F, 3.0F, 1.0F, 2.0F), PartPose.offset(-3.0F, -21.0F, -9.5F));
        $$1.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(58, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(4.9F, 1.0F, 9.5F));
        $$1.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(94, 16).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(-4.9F, 1.0F, 9.5F));
        $$1.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(4.9F, 1.0F, -10.5F));
        $$1.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 26).addBox(-2.5F, 2.0F, -2.5F, 5.0F, 21.0F, 5.0F), PartPose.offset(-4.9F, 1.0F, -10.5F));
        $$3.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(74, 64).addBox(-4.5F, -17.0F, -15.5F, 9.0F, 5.0F, 11.0F, $$2).texOffs(92, 114).addBox(-3.5F, -20.0F, -15.5F, 7.0F, 3.0F, 11.0F, $$2).texOffs(0, 89).addBox(-7.5F, -12.0F, -23.5F, 15.0F, 12.0F, 27.0F, $$2), PartPose.offset(0.0F, 0.0F, 0.0F));
        $$4.addOrReplaceChild("reins", CubeListBuilder.create().texOffs(98, 42).addBox(3.51F, -18.0F, -17.0F, 0.0F, 7.0F, 15.0F).texOffs(84, 57).addBox(-3.5F, -18.0F, -2.0F, 7.0F, 7.0F, 0.0F).texOffs(98, 42).addBox(-3.51F, -18.0F, -17.0F, 0.0F, 7.0F, 15.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        $$4.addOrReplaceChild("bridle", CubeListBuilder.create().texOffs(60, 87).addBox(-3.5F, -7.0F, -15.0F, 7.0F, 8.0F, 19.0F, $$2).texOffs(21, 64).addBox(-3.5F, -21.0F, -15.0F, 7.0F, 14.0F, 7.0F, $$2).texOffs(50, 64).addBox(-2.5F, -21.0F, -21.0F, 5.0F, 5.0F, 6.0F, $$2).texOffs(74, 70).addBox(2.5F, -19.0F, -18.0F, 1.0F, 2.0F, 2.0F).texOffs(74, 70).mirror().addBox(-3.5F, -19.0F, -18.0F, 1.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create($$0, 128, 128);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        this.root().getAllParts().forEach(ModelPart::m_233569_);
        this.applyHeadRotation(t0, float4, float5, float3);
        this.toggleInvisibleParts(t0);
        this.m_267799_(CamelAnimation.CAMEL_WALK, float1, float2, 2.0F, 2.5F);
        this.m_233385_(t0.sitAnimationState, CamelAnimation.CAMEL_SIT, float3, 1.0F);
        this.m_233385_(t0.sitPoseAnimationState, CamelAnimation.CAMEL_SIT_POSE, float3, 1.0F);
        this.m_233385_(t0.sitUpAnimationState, CamelAnimation.CAMEL_STANDUP, float3, 1.0F);
        this.m_233385_(t0.idleAnimationState, CamelAnimation.CAMEL_IDLE, float3, 1.0F);
        this.m_233385_(t0.dashAnimationState, CamelAnimation.CAMEL_DASH, float3, 1.0F);
    }

    private void applyHeadRotation(T t0, float float1, float float2, float float3) {
        float1 = Mth.clamp(float1, -30.0F, 30.0F);
        float2 = Mth.clamp(float2, -25.0F, 45.0F);
        if (t0.getJumpCooldown() > 0) {
            float $$4 = float3 - (float) t0.f_19797_;
            float $$5 = 45.0F * ((float) t0.getJumpCooldown() - $$4) / 55.0F;
            float2 = Mth.clamp(float2 + $$5, -25.0F, 70.0F);
        }
        this.head.yRot = float1 * (float) (Math.PI / 180.0);
        this.head.xRot = float2 * (float) (Math.PI / 180.0);
    }

    private void toggleInvisibleParts(T t0) {
        boolean $$1 = t0.m_6254_();
        boolean $$2 = t0.m_20160_();
        for (ModelPart $$3 : this.saddleParts) {
            $$3.visible = $$1;
        }
        for (ModelPart $$4 : this.ridingParts) {
            $$4.visible = $$2 && $$1;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack0, VertexConsumer vertexConsumer1, int int2, int int3, float float4, float float5, float float6, float float7) {
        if (this.f_102610_) {
            poseStack0.pushPose();
            poseStack0.scale(0.45F, 0.45F, 0.45F);
            poseStack0.translate(0.0F, 1.834375F, 0.0F);
            this.root().render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
            poseStack0.popPose();
        } else {
            this.root().render(poseStack0, vertexConsumer1, int2, int3, float4, float5, float6, float7);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}