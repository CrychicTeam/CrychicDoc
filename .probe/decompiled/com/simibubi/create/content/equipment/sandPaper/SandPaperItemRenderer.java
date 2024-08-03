package com.simibubi.create.content.equipment.sandPaper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SandPaperItemRenderer extends CustomRenderedItemModelRenderer {

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        LocalPlayer player = Minecraft.getInstance().player;
        float partialTicks = AnimationTickHolder.getPartialTicks();
        boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        boolean firstPerson = leftHand || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
        CompoundTag tag = stack.getOrCreateTag();
        boolean jeiMode = tag.contains("JEI");
        ms.pushPose();
        if (tag.contains("Polishing")) {
            ms.pushPose();
            if (transformType == ItemDisplayContext.GUI) {
                ms.translate(0.0F, 0.2F, 1.0F);
                ms.scale(0.75F, 0.75F, 0.75F);
            } else {
                int modifier = leftHand ? -1 : 1;
                ms.mulPose(Axis.YP.rotationDegrees((float) (modifier * 40)));
            }
            float time = (float) (!jeiMode ? player.m_21212_() : -AnimationTickHolder.getTicks() % stack.getUseDuration()) - partialTicks + 1.0F;
            if (time / (float) stack.getUseDuration() < 0.8F) {
                float bobbing = -Mth.abs(Mth.cos(time / 4.0F * (float) Math.PI) * 0.1F);
                if (transformType == ItemDisplayContext.GUI) {
                    ms.translate(bobbing, bobbing, 0.0F);
                } else {
                    ms.translate(0.0F, bobbing, 0.0F);
                }
            }
            ItemStack toPolish = ItemStack.of(tag.getCompound("Polishing"));
            itemRenderer.renderStatic(toPolish, ItemDisplayContext.NONE, light, overlay, ms, buffer, player.m_9236_(), 0);
            ms.popPose();
        }
        if (firstPerson) {
            int itemInUseCount = player.m_21212_();
            if (itemInUseCount > 0) {
                int modifier = leftHand ? -1 : 1;
                ms.translate((float) modifier * 0.5F, 0.0F, -0.25F);
                ms.mulPose(Axis.ZP.rotationDegrees((float) (modifier * 40)));
                ms.mulPose(Axis.XP.rotationDegrees((float) (modifier * 10)));
                ms.mulPose(Axis.YP.rotationDegrees((float) (modifier * 90)));
            }
        }
        itemRenderer.render(stack, ItemDisplayContext.NONE, false, ms, buffer, light, overlay, model.getOriginalModel());
        ms.popPose();
    }
}