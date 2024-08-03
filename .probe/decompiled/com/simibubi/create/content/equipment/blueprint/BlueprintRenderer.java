package com.simibubi.create.content.equipment.blueprint;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.joml.Matrix3f;

public class BlueprintRenderer extends EntityRenderer<BlueprintEntity> {

    public BlueprintRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(BlueprintEntity entity, float yaw, float pt, PoseStack ms, MultiBufferSource buffer, int light) {
        PartialModel partialModel = entity.size == 3 ? AllPartialModels.CRAFTING_BLUEPRINT_3x3 : (entity.size == 2 ? AllPartialModels.CRAFTING_BLUEPRINT_2x2 : AllPartialModels.CRAFTING_BLUEPRINT_1x1);
        SuperByteBuffer sbb = CachedBufferer.partial(partialModel, Blocks.AIR.defaultBlockState());
        ((SuperByteBuffer) ((SuperByteBuffer) sbb.rotateY((double) (-yaw))).rotateX((double) (90.0F + entity.m_146909_()))).translate(-0.5, -0.03125, -0.5);
        if (entity.size == 2) {
            sbb.translate(0.5, 0.0, -0.5);
        }
        sbb.forEntityRender().light(light).renderInto(ms, buffer.getBuffer(Sheets.solidBlockSheet()));
        super.render(entity, yaw, pt, ms, buffer, light);
        ms.pushPose();
        float fakeNormalXRotation = -15.0F;
        int bl = light >> 4 & 15;
        int sl = light >> 20 & 15;
        boolean vertical = entity.m_146909_() != 0.0F;
        if (entity.m_146909_() == -90.0F) {
            fakeNormalXRotation = -45.0F;
        } else if (entity.m_146909_() == 90.0F || yaw % 180.0F != 0.0F) {
            bl = (int) ((double) bl / 1.35);
            sl = (int) ((double) sl / 1.35);
        }
        int itemLight = Mth.floor((double) sl + 0.5) << 20 | (Mth.floor((double) bl + 0.5) & 15) << 4;
        ((TransformStack) TransformStack.cast(ms).rotateY(vertical ? 0.0 : (double) (-yaw))).rotateX((double) fakeNormalXRotation);
        Matrix3f copy = new Matrix3f(ms.last().normal());
        ms.popPose();
        ms.pushPose();
        ((TransformStack) ((TransformStack) TransformStack.cast(ms).rotateY((double) (-yaw))).rotateX((double) entity.m_146909_())).translate(0.0, 0.0, 0.03225);
        if (entity.size == 3) {
            ms.translate(-1.0F, -1.0F, 0.0F);
        }
        PoseStack squashedMS = new PoseStack();
        squashedMS.last().pose().mul(ms.last().pose());
        for (int x = 0; x < entity.size; x++) {
            squashedMS.pushPose();
            for (int y = 0; y < entity.size; y++) {
                BlueprintEntity.BlueprintSection section = entity.getSection(x * entity.size + y);
                Couple<ItemStack> displayItems = section.getDisplayItems();
                squashedMS.pushPose();
                squashedMS.scale(0.5F, 0.5F, 9.765625E-4F);
                displayItems.forEachWithContext((stack, primary) -> {
                    if (!stack.isEmpty()) {
                        squashedMS.pushPose();
                        if (!primary) {
                            squashedMS.translate(0.325F, -0.325F, 1.0F);
                            squashedMS.scale(0.625F, 0.625F, 1.0F);
                        }
                        squashedMS.last().normal().set(copy);
                        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GUI, itemLight, OverlayTexture.NO_OVERLAY, squashedMS, buffer, entity.m_9236_(), 0);
                        squashedMS.popPose();
                    }
                });
                squashedMS.popPose();
                squashedMS.translate(1.0F, 0.0F, 0.0F);
            }
            squashedMS.popPose();
            squashedMS.translate(0.0F, 1.0F, 0.0F);
        }
        ms.popPose();
    }

    public ResourceLocation getTextureLocation(BlueprintEntity entity) {
        return null;
    }
}