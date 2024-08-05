package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class AtlatitanModel extends SauropodBaseModel<AtlatitanEntity> {

    private final AdvancedModelBox cube_r1 = new AdvancedModelBox(this);

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox cube_r4;

    private final AdvancedModelBox cube_r5;

    private final AdvancedModelBox cube_r6;

    private final AdvancedModelBox cube_r7;

    private final AdvancedModelBox cube_r8;

    private final AdvancedModelBox cube_r9;

    private final AdvancedModelBox cube_r10;

    private final AdvancedModelBox cube_r11;

    private final AdvancedModelBox cube_r12;

    public AtlatitanModel() {
        this.cube_r1.setRotationPoint(-24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.0F, -0.7854F);
        this.cube_r1.setTextureOffset(0, 123).addBox(-6.0F, -23.0F, 14.5F, 11.0F, 38.0F, 11.0F, 0.0F, true);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(-24.0F, -33.0F, -2.0F);
        this.chest.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, -0.7854F, 0.0F, -0.7854F);
        this.cube_r2.setTextureOffset(0, 123).addBox(-6.0F, -15.0F, 0.5F, 11.0F, 38.0F, 11.0F, 0.0F, true);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(24.0F, -33.0F, -2.0F);
        this.chest.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, -0.7854F, 0.0F, 0.7854F);
        this.cube_r3.setTextureOffset(0, 123).addBox(-5.0F, -15.0F, 0.5F, 11.0F, 38.0F, 11.0F, 0.0F, false);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, 0.0F, 0.0F, 0.7854F);
        this.cube_r4.setTextureOffset(0, 123).addBox(-5.0F, -23.0F, 14.5F, 11.0F, 38.0F, 11.0F, 0.0F, false);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(-24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, 0.3927F, 0.0F, -0.7854F);
        this.cube_r5.setTextureOffset(146, 285).addBox(-7.0F, -14.0F, -6.5F, 13.0F, 51.0F, 13.0F, 0.0F, true);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, 0.3927F, 0.0F, 0.7854F);
        this.cube_r6.setTextureOffset(146, 285).addBox(-6.0F, -14.0F, -6.5F, 13.0F, 51.0F, 13.0F, 0.0F, false);
        this.cube_r7 = new AdvancedModelBox(this);
        this.cube_r7.setRotationPoint(8.9991F, 25.5F, -2.7496F);
        this.right_Hand.addChild(this.cube_r7);
        this.setRotateAngle(this.cube_r7, 0.0F, -0.3927F, 0.0F);
        this.cube_r7.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, true);
        this.cube_r8 = new AdvancedModelBox(this);
        this.cube_r8.setRotationPoint(-15.0F, 25.5F, -2.75F);
        this.right_Hand.addChild(this.cube_r8);
        this.setRotateAngle(this.cube_r8, 0.0F, 0.3927F, 0.0F);
        this.cube_r8.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, true);
        this.cube_r9 = new AdvancedModelBox(this);
        this.cube_r9.setRotationPoint(-8.9991F, 25.5F, -2.7496F);
        this.left_Hand.addChild(this.cube_r9);
        this.setRotateAngle(this.cube_r9, 0.0F, 0.3927F, 0.0F);
        this.cube_r9.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, false);
        this.cube_r10 = new AdvancedModelBox(this);
        this.cube_r10.setRotationPoint(15.0F, 25.5F, -2.75F);
        this.left_Hand.addChild(this.cube_r10);
        this.setRotateAngle(this.cube_r10, 0.0F, -0.3927F, 0.0F);
        this.cube_r10.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, false);
        this.cube_r11 = new AdvancedModelBox(this);
        this.cube_r11.setRotationPoint(-7.0F, -2.0F, -69.0F);
        this.neck2.addChild(this.cube_r11);
        this.setRotateAngle(this.cube_r11, 0.0F, 0.0F, -0.7854F);
        this.cube_r11.setTextureOffset(227, 259).addBox(-4.0F, -20.0F, 19.0F, 8.0F, 24.0F, 8.0F, 0.0F, true);
        this.cube_r11.setTextureOffset(227, 259).addBox(-4.0F, -20.0F, 36.0F, 8.0F, 24.0F, 8.0F, 0.0F, true);
        this.cube_r11.setTextureOffset(139, 62).addBox(-4.0F, -13.0F, 1.0F, 8.0F, 17.0F, 8.0F, 0.0F, true);
        this.cube_r11.setTextureOffset(139, 62).addBox(-4.0F, -8.0F, 51.0F, 8.0F, 17.0F, 8.0F, 0.0F, true);
        this.cube_r12 = new AdvancedModelBox(this);
        this.cube_r12.setRotationPoint(7.0F, -2.0F, -69.0F);
        this.neck2.addChild(this.cube_r12);
        this.setRotateAngle(this.cube_r12, 0.0F, 0.0F, 0.7854F);
        this.cube_r12.setTextureOffset(227, 259).addBox(-4.0F, -20.0F, 36.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
        this.cube_r12.setTextureOffset(227, 259).addBox(-4.0F, -20.0F, 19.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
        this.cube_r12.setTextureOffset(139, 62).addBox(-4.0F, -13.0F, 2.0F, 8.0F, 17.0F, 8.0F, 0.0F, false);
        this.cube_r12.setTextureOffset(139, 62).addBox(-4.0F, -10.0F, 51.0F, 8.0F, 17.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 2.0F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            this.head.setRotationPoint(0.8F, 3.0F, -75.0F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.15F, 0.15F, 0.15F);
            matrixStackIn.translate(0.0, 8.55F, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setRotationPoint(0.8F, 8.0F, -75.0F);
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.chest, this.hips, this.tail, this.tail2, this.tail3, this.left_Leg, this.left_Foot, this.right_Leg, this.right_Foot, this.left_Arm, new AdvancedModelBox[] { this.left_Hand, this.right_Arm, this.right_Hand, this.neck, this.neck2, this.head, this.jaw, this.dewlap, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r8, this.cube_r9, this.cube_r10, this.cube_r11, this.cube_r12 });
    }

    public Vec3 getRiderPosition(Vec3 offsetIn) {
        PoseStack translationStack = new PoseStack();
        translationStack.pushPose();
        this.root.translateAndRotate(translationStack);
        this.body.translateAndRotate(translationStack);
        this.chest.translateAndRotate(translationStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(translationStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) armOffsetVec.y(), (double) armOffsetVec.z());
        translationStack.popPose();
        return vec3;
    }
}