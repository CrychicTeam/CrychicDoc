package yesman.epicfight.client.renderer.patched.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;

@OnlyIn(Dist.CLIENT)
public class PatchedCapeLayer extends PatchedLayer<AbstractClientPlayer, AbstractClientPlayerPatch<AbstractClientPlayer>, PlayerModel<AbstractClientPlayer>, CapeLayer, HumanoidMesh> {

    public PatchedCapeLayer() {
        super(null);
    }

    protected void renderLayer(AbstractClientPlayerPatch<AbstractClientPlayer> entitypatch, AbstractClientPlayer entityliving, CapeLayer vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLightIn, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        if (entityliving.isCapeLoaded() && !entityliving.m_20145_() && entityliving.m_36170_(PlayerModelPart.CAPE) && entityliving.getCloakTextureLocation() != null) {
            ItemStack itemstack = entityliving.m_6844_(EquipmentSlot.CHEST);
            if (itemstack.getItem() != Items.ELYTRA) {
                OpenMatrix4f modelMatrix = new OpenMatrix4f();
                modelMatrix.scale(new Vec3f(-1.0F, -1.0F, 1.0F)).mulFront(poses[8]);
                OpenMatrix4f transpose = OpenMatrix4f.transpose(modelMatrix, null);
                poseStack.pushPose();
                MathUtils.translateStack(poseStack, modelMatrix);
                MathUtils.rotateStack(poseStack, transpose);
                poseStack.translate(0.0, -0.4, -0.025);
                vanillaLayer.render(poseStack, buffer, packedLightIn, entityliving, entityliving.f_267362_.position(), entityliving.f_267362_.speed(), partialTicks, (float) entityliving.f_19797_, yRot, xRot);
                poseStack.popPose();
            }
        }
    }
}