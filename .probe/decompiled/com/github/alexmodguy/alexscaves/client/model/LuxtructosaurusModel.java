package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.util.RandomSource;

public class LuxtructosaurusModel extends SauropodBaseModel<LuxtructosaurusEntity> {

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

    public LuxtructosaurusModel() {
        this.cube_r1.setRotationPoint(-24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.0F, -0.7854F);
        this.cube_r1.setTextureOffset(0, 123).addBox(-6.0F, -23.0F, 14.5F, 11.0F, 38.0F, 11.0F, 0.0F, true);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, 0.0F, 0.7854F);
        this.cube_r2.setTextureOffset(0, 123).addBox(-5.0F, -23.0F, 14.5F, 11.0F, 38.0F, 11.0F, 0.0F, false);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(-24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, 0.3927F, 0.0F, -0.7854F);
        this.cube_r3.setTextureOffset(146, 285).addBox(-7.0F, -40.0F, -6.5F, 13.0F, 51.0F, 13.0F, 0.0F, true);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(24.0F, -33.0F, -39.0F);
        this.chest.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, 0.3927F, 0.0F, 0.7854F);
        this.cube_r4.setTextureOffset(146, 285).addBox(-6.0F, -40.0F, -6.5F, 13.0F, 51.0F, 13.0F, 0.0F, false);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(8.9991F, 25.5F, -2.7496F);
        this.right_Hand.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, 0.0F, -0.3927F, 0.0F);
        this.cube_r5.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, true);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(-15.0F, 25.5F, -2.75F);
        this.right_Hand.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, 0.0F, 0.3927F, 0.0F);
        this.cube_r6.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, true);
        this.cube_r7 = new AdvancedModelBox(this);
        this.cube_r7.setRotationPoint(-8.9991F, 25.5F, -2.7496F);
        this.left_Hand.addChild(this.cube_r7);
        this.setRotateAngle(this.cube_r7, 0.0F, 0.3927F, 0.0F);
        this.cube_r7.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, false);
        this.cube_r8 = new AdvancedModelBox(this);
        this.cube_r8.setRotationPoint(15.0F, 25.5F, -2.75F);
        this.left_Hand.addChild(this.cube_r8);
        this.setRotateAngle(this.cube_r8, 0.0F, -0.3927F, 0.0F);
        this.cube_r8.setTextureOffset(20, 236).addBox(0.0F, -27.5F, -10.0F, 0.0F, 47.0F, 10.0F, 0.0F, false);
        this.cube_r9 = new AdvancedModelBox(this);
        this.cube_r9.setRotationPoint(-7.0F, -2.0F, -69.0F);
        this.neck2.addChild(this.cube_r9);
        this.setRotateAngle(this.cube_r9, 0.0F, 0.0F, -0.7854F);
        this.cube_r9.setTextureOffset(230, 149).addBox(-4.0F, -26.0F, 2.0F, 8.0F, 30.0F, 8.0F, 0.0F, true);
        this.cube_r9.setTextureOffset(227, 259).addBox(-4.0F, -20.0F, 19.0F, 8.0F, 24.0F, 8.0F, 0.0F, true);
        this.cube_r9.setTextureOffset(139, 62).addBox(-4.0F, -13.0F, 36.0F, 8.0F, 17.0F, 8.0F, 0.0F, true);
        this.cube_r10 = new AdvancedModelBox(this);
        this.cube_r10.setRotationPoint(7.0F, -2.0F, -69.0F);
        this.neck2.addChild(this.cube_r10);
        this.setRotateAngle(this.cube_r10, 0.0F, 0.0F, 0.7854F);
        this.cube_r10.setTextureOffset(230, 149).addBox(-4.0F, -26.0F, 2.0F, 8.0F, 30.0F, 8.0F, 0.0F, false);
        this.cube_r10.setTextureOffset(227, 259).addBox(-4.0F, -20.0F, 19.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
        this.cube_r10.setTextureOffset(139, 62).addBox(-4.0F, -13.0F, 36.0F, 8.0F, 17.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(SauropodBaseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.walk(this.jaw, 0.05F, 0.1F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return this.getListOfAllParts();
    }

    public ImmutableList<AdvancedModelBox> getListOfAllParts() {
        return ImmutableList.of(this.root, this.body, this.chest, this.hips, this.tail, this.tail2, this.tail3, this.left_Leg, this.left_Foot, this.right_Leg, this.right_Foot, this.left_Arm, new AdvancedModelBox[] { this.left_Hand, this.right_Arm, this.right_Hand, this.neck, this.neck2, this.head, this.jaw, this.dewlap, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r8, this.cube_r9, this.cube_r10 });
    }

    public AdvancedModelBox getRandomModelPart(RandomSource randomSource) {
        List<AdvancedModelBox> list = this.getListOfAllParts();
        return (AdvancedModelBox) list.get(randomSource.nextInt(list.size()));
    }
}