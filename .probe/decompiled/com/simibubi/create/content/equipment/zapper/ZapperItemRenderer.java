package com.simibubi.create.content.equipment.zapper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ZapperItemRenderer extends CustomRenderedItemModelRenderer {

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (transformType == ItemDisplayContext.GUI && stack.hasTag() && stack.getTag().contains("BlockUsed")) {
            this.renderBlockUsed(stack, ms, buffer, light, overlay);
        }
    }

    private void renderBlockUsed(ItemStack stack, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), stack.getTag().getCompound("BlockUsed"));
        ms.pushPose();
        ms.translate(-0.3F, -0.45F, -0.0F);
        ms.scale(0.25F, 0.25F, 0.25F);
        BakedModel modelForState = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        if (state.m_60734_() instanceof CrossCollisionBlock) {
            modelForState = Minecraft.getInstance().getItemRenderer().getModel(new ItemStack(state.m_60734_()), null, null, 0);
        }
        Minecraft.getInstance().getItemRenderer().render(new ItemStack(state.m_60734_()), ItemDisplayContext.NONE, false, ms, buffer, light, overlay, modelForState);
        ms.popPose();
    }

    protected float getAnimationProgress(float pt, boolean leftHanded, boolean mainHand) {
        float animation = CreateClient.ZAPPER_RENDER_HANDLER.getAnimation(mainHand ^ leftHanded, pt);
        return Mth.clamp(animation * 5.0F, 0.0F, 1.0F);
    }
}