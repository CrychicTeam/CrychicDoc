package snownee.lychee.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.util.Color;
import snownee.lychee.util.VecHelper;

public class GuiGameElement {

    public static GuiGameElement.GuiRenderBuilder of(ItemStack stack) {
        return new GuiGameElement.GuiItemRenderBuilder(stack);
    }

    public static GuiGameElement.GuiRenderBuilder of(ItemLike itemProvider) {
        return new GuiGameElement.GuiItemRenderBuilder(itemProvider);
    }

    public static GuiGameElement.GuiRenderBuilder of(BlockState state) {
        if (state.m_60799_() != RenderShape.MODEL && state.m_60819_().isEmpty()) {
            return of(state.m_60734_());
        } else {
            if (state.m_60734_() instanceof StairBlock) {
                state = (BlockState) state.m_61124_(StairBlock.FACING, ((Direction) state.m_61143_(StairBlock.FACING)).getOpposite());
            }
            return new GuiGameElement.GuiBlockStateRenderBuilder(state);
        }
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
            RenderType renderType = this.blockState.m_60734_() == Blocks.AIR ? Sheets.translucentCullBlockSheet() : ItemBlockRenderTypes.getRenderType(this.blockState, true);
            VertexConsumer vb = buffer.getBuffer(renderType);
            this.transformMatrix(matrixStack);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            this.renderModel(blockRenderer, buffer, renderType, vb, matrixStack);
            this.cleanUpMatrix(matrixStack);
        }

        protected void renderModel(BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource buffer, RenderType renderType, VertexConsumer vb, PoseStack ms) {
            Minecraft mc = Minecraft.getInstance();
            int color = mc.getBlockColors().getColor(this.blockState, mc.level, mc.cameraEntity != null ? mc.cameraEntity.blockPosition() : null, 0);
            Color rgb = new Color(color == -1 ? this.color : color);
            blockRenderer.getModelRenderer().renderModel(ms.last(), vb, this.blockState, this.blockModel, rgb.getRedAsFloat(), rgb.getGreenAsFloat(), rgb.getBlueAsFloat(), 15728880, OverlayTexture.NO_OVERLAY);
            buffer.endBatch();
        }
    }

    public static class GuiBlockStateRenderBuilder extends GuiGameElement.GuiBlockModelRenderBuilder {

        public GuiBlockStateRenderBuilder(BlockState blockstate) {
            super(Minecraft.getInstance().getBlockRenderer().getBlockModel(blockstate), blockstate);
        }

        @Override
        protected void renderModel(BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource buffer, RenderType renderType, VertexConsumer vb, PoseStack ms) {
            if (this.blockState.m_60734_() instanceof FireBlock) {
                Lighting.setupForFlatItems();
                blockRenderer.renderSingleBlock(this.blockState, ms, buffer, 15728880, OverlayTexture.NO_OVERLAY);
                buffer.endBatch();
                Lighting.setupFor3DItems();
            } else {
                super.renderModel(blockRenderer, buffer, renderType, vb, ms);
                if (!this.blockState.m_60819_().isEmpty()) {
                    float min = 0.001F;
                    float max = 0.999F;
                    FluidRenderer.renderFluidBox(this.blockState.m_60819_(), min, min, min, max, max * 0.8888889F, max, buffer, ms, 15728880, false);
                    buffer.endBatch();
                }
            }
        }
    }

    public static class GuiItemRenderBuilder extends GuiGameElement.GuiRenderBuilder {

        private final ItemStack stack;

        public GuiItemRenderBuilder(ItemStack stack) {
            this.stack = stack;
            this.scale = 10.0;
        }

        public GuiItemRenderBuilder(ItemLike provider) {
            this(provider.asItem().getDefaultInstance());
        }

        @Override
        public void render(GuiGraphics graphics) {
            PoseStack matrixStack = graphics.pose();
            this.prepareMatrix(matrixStack);
            this.transformMatrix(matrixStack);
            renderItemIntoGUI(matrixStack, this.stack, this.customLighting == null);
            this.cleanUpMatrix(matrixStack);
        }

        @Override
        protected void transformMatrix(PoseStack matrixStack) {
            matrixStack.translate(this.x, this.y, this.z);
            matrixStack.translate(this.xLocal * this.scale, this.yLocal * this.scale, this.zLocal * this.scale);
            UIRenderHelper.flipForGuiRender(matrixStack);
        }

        public static void renderItemIntoGUI(PoseStack matrixStack, ItemStack stack, boolean useDefaultLighting) {
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            BakedModel bakedModel = renderer.getModel(stack, null, null, 0);
            Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
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

        @Override
        public GuiGameElement.GuiRenderBuilder lighting(ILightingSettings lighting) {
            return this;
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
            matrixStack.translate(this.x + 3.0F, this.y + 13.0F, this.z);
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