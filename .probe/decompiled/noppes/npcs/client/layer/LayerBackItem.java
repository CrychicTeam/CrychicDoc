package noppes.npcs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.ItemStackWrapper;

public class LayerBackItem extends LayerInterface {

    public LayerBackItem(LivingEntityRenderer render) {
        super(render);
    }

    @Override
    public void render(PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, float limbSwing, float limbSwingAmount, float partialTicks, float age, float netHeadYaw, float headPitch) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack itemstack = ItemStackWrapper.MCItem(this.npc.inventory.getRightHand());
        if (!NoppesUtilServer.IsItemStackNull(itemstack) && !this.npc.isAttacking()) {
            Item item = itemstack.getItem();
            if (!(item instanceof BlockItem)) {
                mStack.pushPose();
                this.base.body.translateAndRotate(mStack);
                mStack.translate(0.0F, 0.36F, 0.14F);
                mStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                if (item instanceof SwordItem) {
                    mStack.mulPose(Axis.XN.rotationDegrees(180.0F));
                }
                BakedModel model = minecraft.getItemRenderer().getItemModelShaper().getItemModel(itemstack);
                ItemTransform p_175034_1_ = model.getTransforms().thirdPersonRightHand;
                mStack.scale(p_175034_1_.scale.x(), p_175034_1_.scale.y(), p_175034_1_.scale.z());
                minecraft.getItemRenderer().renderStatic(this.npc, itemstack, ItemDisplayContext.NONE, false, mStack, typeBuffer, this.npc.m_9236_(), lightmapUV, LivingEntityRenderer.getOverlayCoords(this.npc, 0.0F), 0);
                mStack.popPose();
            }
        }
    }

    @Override
    public void rotate(PoseStack matrixStack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}