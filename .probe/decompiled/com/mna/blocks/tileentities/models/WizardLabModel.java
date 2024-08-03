package com.mna.blocks.tileentities.models;

import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;

public abstract class WizardLabModel<T extends WizardLabTile> extends GeoModel<T> {

    protected final List<WizardLabModel.GeoBoneRenderer> boneOverrides = new ArrayList();

    public void renderBoneAdditions(WizardLabTile tile, String bone, PoseStack stack, MultiBufferSource bufferIn, RenderType renderType, int packedLightIn, int packedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        this.boneOverrides.stream().filter(b -> b.bone.equals(bone) && (b.slot == -1 || tile.hasStack(b.slot))).forEach(b -> {
            if (b.poseAdjuster != null) {
                stack.pushPose();
                b.poseAdjuster.accept(stack);
            }
            if (b.model != null) {
                ModelUtils.renderModel(bufferIn.getBuffer(RenderType.solid()), tile.m_58904_(), tile.m_58899_(), tile.m_58900_(), b.model, stack, packedLightIn, packedOverlayIn);
            } else {
                ItemStack stackToRender = !b.stack.isEmpty() ? b.stack : tile.m_8020_(b.slot);
                itemRenderer.renderStatic(stackToRender, ItemDisplayContext.GROUND, packedLightIn, packedOverlayIn, stack, bufferIn, mc.level, 0);
            }
            if (b.poseAdjuster != null) {
                stack.popPose();
            }
        });
    }

    public static class GeoBoneRenderer {

        public final ResourceLocation model;

        public final ItemStack stack;

        public final int slot;

        public final String bone;

        public final Consumer<PoseStack> poseAdjuster;

        public GeoBoneRenderer(int slot, String bone, ResourceLocation model) {
            this(slot, bone, model, null);
        }

        public GeoBoneRenderer(int slot, String bone, ResourceLocation model, Consumer<PoseStack> poseAdjuster) {
            this.slot = slot;
            this.bone = bone;
            this.model = model;
            this.poseAdjuster = poseAdjuster;
            this.stack = ItemStack.EMPTY;
        }

        public GeoBoneRenderer(int slot, String bone, ItemStack renderItem) {
            this(slot, bone, renderItem, null);
        }

        public GeoBoneRenderer(int slot, String bone, ItemStack renderItem, Consumer<PoseStack> poseAdjuster) {
            this.slot = slot;
            this.bone = bone;
            this.model = null;
            this.poseAdjuster = poseAdjuster;
            this.stack = renderItem.copy();
        }
    }
}