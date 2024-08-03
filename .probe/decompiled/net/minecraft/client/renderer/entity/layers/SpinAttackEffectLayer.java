package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class SpinAttackEffectLayer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident_riptide.png");

    public static final String BOX = "box";

    private final ModelPart box;

    public SpinAttackEffectLayer(RenderLayerParent<T, PlayerModel<T>> renderLayerParentTPlayerModelT0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTPlayerModelT0);
        ModelPart $$2 = entityModelSet1.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
        this.box = $$2.getChild("box");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (t3.isAutoSpinAttack()) {
            VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
            for (int $$11 = 0; $$11 < 3; $$11++) {
                poseStack0.pushPose();
                float $$12 = float7 * (float) (-(45 + $$11 * 5));
                poseStack0.mulPose(Axis.YP.rotationDegrees($$12));
                float $$13 = 0.75F * (float) $$11;
                poseStack0.scale($$13, $$13, $$13);
                poseStack0.translate(0.0F, -0.2F + 0.6F * (float) $$11, 0.0F);
                this.box.render(poseStack0, $$10, int2, OverlayTexture.NO_OVERLAY);
                poseStack0.popPose();
            }
        }
    }
}