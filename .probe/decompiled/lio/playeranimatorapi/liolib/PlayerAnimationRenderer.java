package lio.playeranimatorapi.liolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lio.liosmultiloaderutils.utils.Platform;
import lio.playeranimatorapi.compatibility.PehkuiCompat;
import lio.playeranimatorapi.misc.PlayerModelInterface;
import net.liopyu.liolib.cache.object.BakedGeoModel;
import net.liopyu.liolib.cache.object.GeoBone;
import net.liopyu.liolib.renderer.GeoEntityRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class PlayerAnimationRenderer extends GeoEntityRenderer<AbstractClientPlayer> implements PlayerModelInterface {

    public PlayerModel playerModel;

    private static final Vec3 head_offset = new Vec3(0.0, 2.0, 0.0);

    private static final Vec3 right_arm_offset = new Vec3(5.0, -1.0, 0.0);

    private static final Vec3 left_arm_offset = new Vec3(-5.0, -1.0, 0.0);

    private static final Vec3 right_leg_offset = new Vec3(1.9, -12.0, 0.0);

    private static final Vec3 left_leg_offset = new Vec3(-1.9, -12.0, 0.0);

    public PlayerAnimationRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PlayerAnimationModel());
    }

    public ResourceLocation getTextureLocation(AbstractClientPlayer entity) {
        return this.getGeoModel().getTextureResource((AbstractClientPlayer) this.animatable);
    }

    public void preRender(PoseStack poseStack, AbstractClientPlayer player, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, (AbstractClientPlayer) this.animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pushPose();
        if (Platform.isModLoaded("pehkui")) {
            Vec2 scale = PehkuiCompat.getScale(player, partialTick);
            poseStack.scale(scale.x, scale.y, scale.x);
        }
        poseStack.popPose();
    }

    @Override
    public void setPlayerModel(PlayerModel model) {
        this.playerModel = model;
    }

    public void setupAnim(BakedGeoModel model) {
        this.matchPlayerModel(model, this.playerModel.f_102808_, "head");
        this.matchPlayerModel(model, this.playerModel.f_102810_, "torso");
        this.matchPlayerModel(model, this.playerModel.f_102811_, "right_arm");
        this.matchPlayerModel(model, this.playerModel.f_102812_, "left_arm");
        this.matchPlayerModel(model, this.playerModel.f_102813_, "right_leg");
        this.matchPlayerModel(model, this.playerModel.f_102814_, "left_leg");
    }

    public void matchPlayerModel(BakedGeoModel model, ModelPart part, String name) {
        if (model.getBone(name).isPresent()) {
            GeoBone bone = (GeoBone) model.getBone(name).get();
            Vec3 offset = this.getPositionOffsetForPart(name);
            bone.setPosX(-(part.x + (float) offset.x));
            bone.setPosY(-(part.y + (float) offset.y));
            bone.setPosZ(part.z + (float) offset.z);
            bone.setRotX(-part.xRot);
            bone.setRotY(-part.yRot);
            bone.setRotZ(part.zRot);
        }
    }

    public Vec3 getPositionOffsetForPart(String part) {
        switch(part) {
            case "head":
                return head_offset;
            case "right_arm":
                return right_arm_offset;
            case "left_arm":
                return left_arm_offset;
            case "right_leg":
                return right_leg_offset;
            case "left_leg":
                return left_leg_offset;
            default:
                return Vec3.ZERO;
        }
    }
}