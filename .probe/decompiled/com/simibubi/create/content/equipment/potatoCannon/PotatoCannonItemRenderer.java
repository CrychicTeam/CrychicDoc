package com.simibubi.create.content.equipment.potatoCannon;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PotatoCannonItemRenderer extends CustomRenderedItemModelRenderer {

    protected static final PartialModel COG = new PartialModel(Create.asResource("item/potato_cannon/cog"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        renderer.render(model.getOriginalModel(), light);
        LocalPlayer player = mc.player;
        boolean mainHand = player.m_21205_() == stack;
        boolean offHand = player.m_21206_() == stack;
        boolean leftHanded = player.m_5737_() == HumanoidArm.LEFT;
        float offset = 0.03125F;
        float worldTime = AnimationTickHolder.getRenderTime() / 10.0F;
        float angle = worldTime * -25.0F;
        float speed = CreateClient.POTATO_CANNON_RENDER_HANDLER.getAnimation(mainHand ^ leftHanded, AnimationTickHolder.getPartialTicks());
        if (mainHand || offHand) {
            angle += 360.0F * Mth.clamp(speed * 5.0F, 0.0F, 1.0F);
        }
        angle %= 360.0F;
        ms.pushPose();
        ms.translate(0.0F, offset, 0.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(angle));
        ms.translate(0.0F, -offset, 0.0F);
        renderer.render(COG.get(), light);
        ms.popPose();
        if (transformType == ItemDisplayContext.GUI) {
            PotatoCannonItem.getAmmoforPreview(stack).ifPresent(ammo -> {
                PoseStack localMs = new PoseStack();
                localMs.translate(-0.25F, -0.25F, 1.0F);
                localMs.scale(0.5F, 0.5F, 0.5F);
                TransformStack.cast(localMs).rotateY(-34.0);
                itemRenderer.renderStatic(ammo, ItemDisplayContext.GUI, light, OverlayTexture.NO_OVERLAY, localMs, buffer, mc.level, 0);
            });
        }
    }
}