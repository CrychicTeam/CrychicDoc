package com.simibubi.create.content.decoration.placard;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class PlacardRenderer extends SafeBlockEntityRenderer<PlacardBlockEntity> {

    public PlacardRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(PlacardBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemStack heldItem = be.getHeldItem();
        if (!heldItem.isEmpty()) {
            BlockState blockState = be.m_58900_();
            Direction facing = (Direction) blockState.m_61143_(PlacardBlock.f_54117_);
            AttachFace face = (AttachFace) blockState.m_61143_(PlacardBlock.f_53179_);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            boolean blockItem = itemRenderer.getModel(heldItem, null, null, 0).isGui3d();
            ms.pushPose();
            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).centre()).rotate(Direction.UP, (face == AttachFace.CEILING ? (float) Math.PI : 0.0F) + AngleHelper.rad((double) (180.0F + AngleHelper.horizontalAngle(facing))))).rotate(Direction.EAST, face == AttachFace.CEILING ? (float) (-Math.PI / 2) : (face == AttachFace.FLOOR ? (float) (Math.PI / 2) : 0.0F))).translate(0.0, 0.0, 0.28125)).scale(blockItem ? 0.5F : 0.375F);
            itemRenderer.renderStatic(heldItem, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.m_58904_(), 0);
            ms.popPose();
        }
    }
}