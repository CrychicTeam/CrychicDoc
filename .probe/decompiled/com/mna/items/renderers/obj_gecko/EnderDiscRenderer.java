package com.mna.items.renderers.obj_gecko;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.items.artifice.ItemEnderDisk;
import com.mna.items.renderers.models.ModelEnderDisc;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class EnderDiscRenderer extends GeoItemRenderer<ItemEnderDisk> {

    public static final ResourceLocation disk_model = RLoc.create("item/special/ender_disc");

    protected MultiBufferSource rtb;

    protected Minecraft mc = Minecraft.getInstance();

    public EnderDiscRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(new ModelEnderDisc());
    }

    public RenderType getRenderType(ItemEnderDisk animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.solid();
    }

    public void renderRecursively(PoseStack poseStack, ItemEnderDisk animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.mc != null) {
            poseStack.pushPose();
            RenderUtils.translateMatrixToBone(poseStack, bone);
            RenderUtils.translateToPivotPoint(poseStack, bone);
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
            RenderUtils.scaleMatrixForBone(poseStack, bone);
            if (!bone.isHidden()) {
                poseStack.pushPose();
                String var15 = bone.getName();
                byte childBone = -1;
                Iterator var17;
                switch(var15.hashCode()) {
                    case 2521314:
                        if (var15.equals("ROOT")) {
                            childBone = 0;
                        }
                    default:
                        switch(childBone) {
                            case 0:
                                ModelUtils.renderEntityModel(buffer, ManaAndArtifice.instance.proxy.getClientWorld(), disk_model, poseStack, packedLight, OverlayTexture.NO_OVERLAY);
                            default:
                                poseStack.popPose();
                                var17 = bone.getChildBones().iterator();
                        }
                }
                while (var17.hasNext()) {
                    GeoBone childBone = (GeoBone) var17.next();
                    this.renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }
            poseStack.popPose();
        }
    }
}