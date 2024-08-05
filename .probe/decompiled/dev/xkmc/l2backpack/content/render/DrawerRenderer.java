package dev.xkmc.l2backpack.content.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2backpack.content.drawer.IDrawerBlockEntity;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DrawerRenderer implements BlockEntityRenderer<IDrawerBlockEntity> {

    public DrawerRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(IDrawerBlockEntity entity, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        float time = (mc.getPartialTick() + (float) Proxy.getPlayer().f_19797_) % 80.0F;
        ItemStack stack = new ItemStack(entity.getItem(), 1);
        if (!stack.isEmpty()) {
            pose.pushPose();
            pose.translate(0.5, entity.getItem() instanceof BlockItem ? 0.5 : 0.625, 0.5);
            pose.scale(2.0F, 2.0F, 2.0F);
            pose.translate(0.0F, -0.2F, 0.0F);
            pose.mulPose(Axis.YP.rotationDegrees(time * 4.5F));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, pose, buffer, entity.m_58904_(), 0);
            pose.popPose();
        }
    }
}