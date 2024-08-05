package com.simibubi.create.foundation.gui.element;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.gui.ILightingSettings;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.fluids.FluidStack;

public class GuiGameElement {

    public static GuiGameElement.GuiRenderBuilder of(ItemStack stack) {
        return new GuiGameElement.GuiItemRenderBuilder(stack);
    }

    public static GuiGameElement.GuiRenderBuilder of(ItemLike itemProvider) {
        return new GuiGameElement.GuiItemRenderBuilder(itemProvider);
    }

    public static GuiGameElement.GuiRenderBuilder of(BlockState state) {
        return new GuiGameElement.GuiBlockStateRenderBuilder(state);
    }

    public static GuiGameElement.GuiRenderBuilder of(PartialModel partial) {
        return new GuiGameElement.GuiBlockPartialRenderBuilder(partial);
    }

    public static GuiGameElement.GuiRenderBuilder of(Fluid fluid) {
        return new GuiGameElement.GuiBlockStateRenderBuilder((BlockState) fluid.defaultFluidState().createLegacyBlock().m_61124_(LiquidBlock.LEVEL, 0));
    }

    private static class GuiBlockModelRenderBuilder extends GuiGameElement.GuiRenderBuilder {

        protected BakedModel blockModel;

        protected BlockState blockState;

        public GuiBlockModelRenderBuilder(BakedModel blockmodel, @Nullable BlockState blockState) {
            this.blockState = blockState == null ? Blocks.AIR.defaultBlockState() : blockState;
            this.blockModel = blockmodel;
        }

        @Override
        public void render(GuiGraphics graphics) {
            PoseStack matrixStack = graphics.pose();
            this.prepareMatrix(matrixStack);
            Minecraft mc = Minecraft.getInstance();
            BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
            MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
            this.transformMatrix(matrixStack);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            this.renderModel(blockRenderer, buffer, matrixStack);
            this.cleanUpMatrix(matrixStack);
        }

        protected void renderModel(BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource buffer, PoseStack ms) {
            if (this.blockState.m_60734_() == Blocks.AIR) {
                RenderType renderType = Sheets.translucentCullBlockSheet();
                blockRenderer.getModelRenderer().renderModel(ms.last(), buffer.getBuffer(renderType), this.blockState, this.blockModel, 1.0F, 1.0F, 1.0F, 15728880, OverlayTexture.NO_OVERLAY, ModelUtil.VIRTUAL_DATA, null);
            } else {
                int color = Minecraft.getInstance().getBlockColors().getColor(this.blockState, null, null, 0);
                Color rgb = new Color(color == -1 ? this.color : color);
                for (RenderType chunkType : this.blockModel.getRenderTypes(this.blockState, RandomSource.create(42L), ModelUtil.VIRTUAL_DATA)) {
                    RenderType renderType = RenderTypeHelper.getEntityRenderType(chunkType, true);
                    blockRenderer.getModelRenderer().renderModel(ms.last(), buffer.getBuffer(renderType), this.blockState, this.blockModel, rgb.getRedAsFloat(), rgb.getGreenAsFloat(), rgb.getBlueAsFloat(), 15728880, OverlayTexture.NO_OVERLAY, ModelUtil.VIRTUAL_DATA, chunkType);
                }
            }
            buffer.endBatch();
        }
    }

    public static class GuiBlockPartialRenderBuilder extends GuiGameElement.GuiBlockModelRenderBuilder {

        public GuiBlockPartialRenderBuilder(PartialModel partial) {
            super(partial.get(), null);
        }
    }

    public static class GuiBlockStateRenderBuilder extends GuiGameElement.GuiBlockModelRenderBuilder {

        public GuiBlockStateRenderBuilder(BlockState blockstate) {
            super(Minecraft.getInstance().getBlockRenderer().getBlockModel(blockstate), blockstate);
        }

        @Override
        protected void renderModel(BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource buffer, PoseStack ms) {
            if (this.blockState.m_60734_() instanceof BaseFireBlock) {
                Lighting.setupForFlatItems();
                super.renderModel(blockRenderer, buffer, ms);
                Lighting.setupFor3DItems();
            } else {
                super.renderModel(blockRenderer, buffer, ms);
                if (!this.blockState.m_60819_().isEmpty()) {
                    FluidRenderer.renderFluidBox(new FluidStack(this.blockState.m_60819_().getType(), 1000), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, buffer, ms, 15728880, false);
                    buffer.endBatch();
                }
            }
        }
    }

    public static class GuiItemRenderBuilder extends GuiGameElement.GuiRenderBuilder {

        private final ItemStack stack;

        public GuiItemRenderBuilder(ItemStack stack) {
            this.stack = stack;
        }

        public GuiItemRenderBuilder(ItemLike provider) {
            this(new ItemStack(provider));
        }

        @Override
        public void render(GuiGraphics graphics) {
            PoseStack matrixStack = graphics.pose();
            this.prepareMatrix(matrixStack);
            this.transformMatrix(matrixStack);
            renderItemIntoGUI(matrixStack, this.stack, this.customLighting == null);
            this.cleanUpMatrix(matrixStack);
        }

        public static void renderItemIntoGUI(PoseStack matrixStack, ItemStack stack, boolean useDefaultLighting) {
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel bakedModel = renderer.getModel(stack, null, null, 0);
            renderer.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            RenderSystem.enableBlend();
            RenderSystem.enableCull();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, 100.0F);
            matrixStack.translate(8.0F, -8.0F, 0.0F);
            matrixStack.scale(16.0F, 16.0F, 16.0F);
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean flatLighting = !bakedModel.usesBlockLight();
            if (useDefaultLighting && flatLighting) {
                Lighting.setupForFlatItems();
            }
            renderer.render(stack, ItemDisplayContext.GUI, false, matrixStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
            RenderSystem.disableDepthTest();
            buffer.endBatch();
            RenderSystem.enableDepthTest();
            if (useDefaultLighting && flatLighting) {
                Lighting.setupFor3DItems();
            }
            matrixStack.popPose();
        }
    }

    public abstract static class GuiRenderBuilder extends RenderElement {

        protected double xLocal;

        protected double yLocal;

        protected double zLocal;

        protected double xRot;

        protected double yRot;

        protected double zRot;

        protected double scale = 1.0;

        protected int color = 16777215;

        protected Vec3 rotationOffset = Vec3.ZERO;

        protected ILightingSettings customLighting = null;

        public GuiGameElement.GuiRenderBuilder atLocal(double x, double y, double z) {
            this.xLocal = x;
            this.yLocal = y;
            this.zLocal = z;
            return this;
        }

        public GuiGameElement.GuiRenderBuilder rotate(double xRot, double yRot, double zRot) {
            this.xRot = xRot;
            this.yRot = yRot;
            this.zRot = zRot;
            return this;
        }

        public GuiGameElement.GuiRenderBuilder rotateBlock(double xRot, double yRot, double zRot) {
            return this.rotate(xRot, yRot, zRot).withRotationOffset(VecHelper.getCenterOf(BlockPos.ZERO));
        }

        public GuiGameElement.GuiRenderBuilder scale(double scale) {
            this.scale = scale;
            return this;
        }

        public GuiGameElement.GuiRenderBuilder color(int color) {
            this.color = color;
            return this;
        }

        public GuiGameElement.GuiRenderBuilder withRotationOffset(Vec3 offset) {
            this.rotationOffset = offset;
            return this;
        }

        public GuiGameElement.GuiRenderBuilder lighting(ILightingSettings lighting) {
            this.customLighting = lighting;
            return this;
        }

        protected void prepareMatrix(PoseStack matrixStack) {
            matrixStack.pushPose();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.prepareLighting(matrixStack);
        }

        protected void transformMatrix(PoseStack matrixStack) {
            matrixStack.translate(this.x, this.y, this.z);
            matrixStack.scale((float) this.scale, (float) this.scale, (float) this.scale);
            matrixStack.translate(this.xLocal, this.yLocal, this.zLocal);
            UIRenderHelper.flipForGuiRender(matrixStack);
            matrixStack.translate(this.rotationOffset.x, this.rotationOffset.y, this.rotationOffset.z);
            matrixStack.mulPose(Axis.ZP.rotationDegrees((float) this.zRot));
            matrixStack.mulPose(Axis.XP.rotationDegrees((float) this.xRot));
            matrixStack.mulPose(Axis.YP.rotationDegrees((float) this.yRot));
            matrixStack.translate(-this.rotationOffset.x, -this.rotationOffset.y, -this.rotationOffset.z);
        }

        protected void cleanUpMatrix(PoseStack matrixStack) {
            matrixStack.popPose();
            this.cleanUpLighting(matrixStack);
        }

        protected void prepareLighting(PoseStack matrixStack) {
            if (this.customLighting != null) {
                this.customLighting.applyLighting();
            } else {
                Lighting.setupFor3DItems();
            }
        }

        protected void cleanUpLighting(PoseStack matrixStack) {
            if (this.customLighting != null) {
                Lighting.setupFor3DItems();
            }
        }
    }
}