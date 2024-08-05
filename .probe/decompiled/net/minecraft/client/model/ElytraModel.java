package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ElytraModel<T extends LivingEntity> extends AgeableListModel<T> {

    private final ModelPart rightWing;

    private final ModelPart leftWing;

    public ElytraModel(ModelPart modelPart0) {
        this.leftWing = modelPart0.getChild("left_wing");
        this.rightWing = modelPart0.getChild("right_wing");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        CubeDeformation $$2 = new CubeDeformation(1.0F);
        $$1.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(22, 0).addBox(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, $$2), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (-Math.PI / 12)));
        $$1.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(22, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, $$2), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, (float) (Math.PI / 12), 0.0F, (float) (Math.PI / 12)));
        return LayerDefinition.create($$0, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.leftWing, this.rightWing);
    }

    public void setupAnim(T t0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = (float) (Math.PI / 12);
        float $$7 = (float) (-Math.PI / 12);
        float $$8 = 0.0F;
        float $$9 = 0.0F;
        if (t0.isFallFlying()) {
            float $$10 = 1.0F;
            Vec3 $$11 = t0.m_20184_();
            if ($$11.y < 0.0) {
                Vec3 $$12 = $$11.normalize();
                $$10 = 1.0F - (float) Math.pow(-$$12.y, 1.5);
            }
            $$6 = $$10 * (float) (Math.PI / 9) + (1.0F - $$10) * $$6;
            $$7 = $$10 * (float) (-Math.PI / 2) + (1.0F - $$10) * $$7;
        } else if (t0.m_6047_()) {
            $$6 = (float) (Math.PI * 2.0 / 9.0);
            $$7 = (float) (-Math.PI / 4);
            $$8 = 3.0F;
            $$9 = 0.08726646F;
        }
        this.leftWing.y = $$8;
        if (t0 instanceof AbstractClientPlayer $$13) {
            $$13.elytraRotX = $$13.elytraRotX + ($$6 - $$13.elytraRotX) * 0.1F;
            $$13.elytraRotY = $$13.elytraRotY + ($$9 - $$13.elytraRotY) * 0.1F;
            $$13.elytraRotZ = $$13.elytraRotZ + ($$7 - $$13.elytraRotZ) * 0.1F;
            this.leftWing.xRot = $$13.elytraRotX;
            this.leftWing.yRot = $$13.elytraRotY;
            this.leftWing.zRot = $$13.elytraRotZ;
        } else {
            this.leftWing.xRot = $$6;
            this.leftWing.zRot = $$7;
            this.leftWing.yRot = $$9;
        }
        this.rightWing.yRot = -this.leftWing.yRot;
        this.rightWing.y = this.leftWing.y;
        this.rightWing.xRot = this.leftWing.xRot;
        this.rightWing.zRot = -this.leftWing.zRot;
    }
}