package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySunbird;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class ModelSunbird extends AdvancedEntityModel<EntitySunbird> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox hair;

    private final AdvancedModelBox left_wing;

    private final AdvancedModelBox left_wing1;

    private final AdvancedModelBox left_wing2;

    private final AdvancedModelBox right_wing;

    private final AdvancedModelBox right_wing1;

    private final AdvancedModelBox right_wing2;

    private final AdvancedModelBox tail1;

    private final AdvancedModelBox tail2;

    private final AdvancedModelBox left_leg;

    private final AdvancedModelBox left_foot;

    private final AdvancedModelBox right_leg;

    private final AdvancedModelBox right_foot;

    public ModelSunbird() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(106, 38).addBox(-7.0F, -5.0F, -11.0F, 14.0F, 12.0F, 23.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setRotationPoint(0.0F, -1.0F, -12.0F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(0, 38).addBox(-3.0F, -3.0F, -12.0F, 6.0F, 6.0F, 13.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, 1.0F, -13.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -5.0F, -7.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.head.setTextureOffset(12, 17).addBox(-2.0F, -2.0F, -12.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-1.0F, 1.0F, -12.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        this.hair = new AdvancedModelBox(this, "hair");
        this.hair.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.head.addChild(this.hair);
        this.hair.setTextureOffset(0, 17).addBox(0.0F, -8.0F, -9.0F, 0.0F, 8.0F, 11.0F, 0.0F, false);
        this.left_wing = new AdvancedModelBox(this, "left_wing");
        this.left_wing.setRotationPoint(8.0F, -3.0F, -8.0F);
        this.body.addChild(this.left_wing);
        this.left_wing.setTextureOffset(0, 119).addBox(-1.0F, -3.0F, -5.2F, 15.0F, 5.0F, 6.0F, 0.0F, false);
        this.left_wing1 = new AdvancedModelBox(this, "left_wing1");
        this.left_wing1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.left_wing.addChild(this.left_wing1);
        this.left_wing1.setTextureOffset(103, 80).addBox(-1.0F, 0.0F, -8.0F, 33.0F, 0.0F, 37.0F, 0.0F, false);
        this.left_wing2 = new AdvancedModelBox(this, "left_wing2");
        this.left_wing2.setRotationPoint(32.0F, 0.0F, 0.0F);
        this.left_wing1.addChild(this.left_wing2);
        this.left_wing2.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -8.0F, 50.0F, 0.0F, 37.0F, 0.0F, false);
        this.right_wing = new AdvancedModelBox(this, "right_wing");
        this.right_wing.setRotationPoint(-8.0F, -3.0F, -8.0F);
        this.body.addChild(this.right_wing);
        this.right_wing.setTextureOffset(0, 119).addBox(-14.0F, -3.0F, -5.2F, 15.0F, 5.0F, 6.0F, 0.0F, true);
        this.right_wing1 = new AdvancedModelBox(this, "right_wing1");
        this.right_wing1.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.right_wing.addChild(this.right_wing1);
        this.right_wing1.setTextureOffset(103, 80).addBox(-32.0F, 0.0F, -8.0F, 33.0F, 0.0F, 37.0F, 0.0F, true);
        this.right_wing2 = new AdvancedModelBox(this, "right_wing2");
        this.right_wing2.setRotationPoint(-32.0F, 0.0F, 0.0F);
        this.right_wing1.addChild(this.right_wing2);
        this.right_wing2.setTextureOffset(0, 0).addBox(-50.0F, 0.0F, -8.0F, 50.0F, 0.0F, 37.0F, 0.0F, true);
        this.tail1 = new AdvancedModelBox(this, "tail1");
        this.tail1.setRotationPoint(0.0F, -5.0F, 12.0F);
        this.body.addChild(this.tail1);
        this.tail1.setTextureOffset(0, 80).addBox(-23.0F, 0.0F, 0.0F, 32.0F, 0.0F, 38.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this, "tail2");
        this.tail2.setRotationPoint(-6.0F, 0.0F, 38.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(0, 38).addBox(-16.0F, 0.0F, 0.0F, 32.0F, 0.0F, 41.0F, 0.0F, false);
        this.left_leg = new AdvancedModelBox(this, "left_leg");
        this.left_leg.setRotationPoint(3.0F, 8.0F, 8.0F);
        this.body.addChild(this.left_leg);
        this.left_leg.setTextureOffset(0, 58).addBox(-2.0F, -1.0F, -5.0F, 5.0F, 4.0F, 8.0F, 0.0F, false);
        this.left_foot = new AdvancedModelBox(this, "left_foot");
        this.left_foot.setRotationPoint(0.5F, 3.0F, -2.0F);
        this.left_leg.addChild(this.left_foot);
        this.setRotationAngle(this.left_foot, 0.0436F, 0.0F, 0.0F);
        this.left_foot.setTextureOffset(22, 66).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        this.right_leg = new AdvancedModelBox(this, "right_leg");
        this.right_leg.setRotationPoint(-3.0F, 8.0F, 8.0F);
        this.body.addChild(this.right_leg);
        this.right_leg.setTextureOffset(0, 58).addBox(-3.0F, -1.0F, -5.0F, 5.0F, 4.0F, 8.0F, 0.0F, true);
        this.right_foot = new AdvancedModelBox(this, "right_foot");
        this.right_foot.setRotationPoint(-0.5F, 3.0F, -2.0F);
        this.right_leg.addChild(this.right_foot);
        this.setRotationAngle(this.right_foot, 0.0436F, 0.0F, 0.0F);
        this.right_foot.setTextureOffset(22, 66).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 3.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.hair, this.body, this.tail1, this.tail2, this.left_wing, this.left_wing1, this.left_wing2, this.right_wing, this.right_wing1, this.right_wing2, this.left_leg, new AdvancedModelBox[] { this.right_leg, this.right_foot, this.left_foot, this.neck, this.head });
    }

    public void setupAnim(EntitySunbird entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flySpeed = 0.2F;
        float flyDegree = 0.6F;
        this.flap(this.right_wing, flySpeed, flyDegree, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.left_wing, flySpeed, flyDegree, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.right_wing2, flySpeed, flyDegree, false, -1.2F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.left_wing2, flySpeed, flyDegree, true, -1.2F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail1, flySpeed, flyDegree * 0.1F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail1, flySpeed, flyDegree * 0.2F, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.left_leg, flySpeed, flyDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.right_leg, flySpeed, flyDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.body, flySpeed, flyDegree * 6.0F, false, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.neck, this.head });
        float partialTick = Minecraft.getInstance().getFrameTime();
        float birdPitch = entityIn.prevBirdPitch + (entityIn.birdPitch - entityIn.prevBirdPitch) * partialTick;
        this.body.rotateAngleX = birdPitch * (float) (Math.PI / 180.0);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}