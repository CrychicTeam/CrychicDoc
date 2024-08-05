package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.ConstructWorkbenchTile;
import com.mna.blocks.tileentities.models.ConstructGantryModel;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class ConstructWorkbenchRenderer extends GeoBlockRenderer<ConstructWorkbenchTile> {

    private static final float triangle_height = (float) (Math.sqrt(3.0) / 2.0);

    public static final ResourceLocation hook_head = RLoc.create("block/construct_gantry/hook_head");

    public static final ResourceLocation hook_torso = RLoc.create("block/construct_gantry/hook_torso");

    public static final ResourceLocation hook_arm = RLoc.create("block/construct_gantry/hook_arm");

    public static final ResourceLocation hook_leg = RLoc.create("block/construct_gantry/hook_leg");

    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    protected IConstructConstruction construction;

    protected Minecraft mc = Minecraft.getInstance();

    protected BlockPos pos;

    protected BlockState state;

    public ConstructWorkbenchRenderer(BlockEntityRendererProvider.Context context) {
        super(new ConstructGantryModel());
    }

    public void actuallyRender(PoseStack poseStack, ConstructWorkbenchTile animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.pos = animatable.m_58899_();
        this.state = animatable.m_58900_();
        this.construction = animatable.getConstruct();
        this.renderVerticalRadiants(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderRecursively(PoseStack stack, ConstructWorkbenchTile animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.mc != null) {
            stack.pushPose();
            RenderUtils.translateMatrixToBone(stack, bone);
            RenderUtils.translateToPivotPoint(stack, bone);
            RenderUtils.rotateMatrixAroundBone(stack, bone);
            RenderUtils.scaleMatrixForBone(stack, bone);
            if (!bone.isHidden()) {
                stack.pushPose();
                String var15 = bone.getName();
                switch(var15) {
                    case "HOOK_HEAD":
                        stack.translate(0.0, 0.0625, 0.0);
                        ModelUtils.renderModel(bufferSource, this.mc.level, this.pos, this.state, hook_head, stack, packedLightIn, packedOverlayIn);
                        break;
                    case "HOOK_TORSO":
                        stack.translate(0.0, 0.0625, 0.0);
                        ModelUtils.renderModel(bufferSource, this.mc.level, this.pos, this.state, hook_torso, stack, packedLightIn, packedOverlayIn);
                        break;
                    case "HOOK_LEGS_RIGHT":
                        stack.translate(0.0, 0.0625, 0.0);
                        if (this.construction.getPart(ConstructSlot.LEGS).isEmpty()) {
                            stack.mulPose(Axis.ZP.rotationDegrees(5.0F));
                        }
                        ModelUtils.renderModel(bufferSource, this.mc.level, this.pos, this.state, hook_leg, stack, packedLightIn, packedOverlayIn);
                        break;
                    case "HOOK_LEGS_LEFT":
                        stack.translate(0.0, 0.0625, 0.0);
                        if (this.construction.getPart(ConstructSlot.LEGS).isEmpty()) {
                            stack.mulPose(Axis.ZP.rotationDegrees(-5.0F));
                        }
                        ModelUtils.renderModel(bufferSource, this.mc.level, this.pos, this.state, hook_leg, stack, packedLightIn, packedOverlayIn);
                        break;
                    case "HOOK_ARMS_RIGHT":
                    case "HOOK_ARMS_LEFT":
                        stack.translate(0.0, 0.0625, 0.0);
                        ModelUtils.renderModel(bufferSource, this.mc.level, this.pos, this.state, hook_arm, stack, packedLightIn, packedOverlayIn);
                        break;
                    case "HEAD":
                        stack.translate(0.0, 0.25, 0.025);
                        this.renderConstructPart(stack, bufferSource, ConstructSlot.HEAD, packedLightIn);
                        break;
                    case "TORSO":
                        stack.translate(0.0, 0.195, 0.0665);
                        this.renderConstructPart(stack, bufferSource, ConstructSlot.TORSO, packedLightIn);
                        break;
                    case "ARM_R":
                        stack.translate(0.125, 0.0415, 0.0525);
                        stack.mulPose(Axis.XP.rotationDegrees(180.0F));
                        this.renderConstructPart(stack, bufferSource, ConstructSlot.RIGHT_ARM, packedLightIn);
                        break;
                    case "ARM_L":
                        stack.translate(-0.156, 0.0415, 0.0525);
                        stack.mulPose(Axis.XP.rotationDegrees(180.0F));
                        this.renderConstructPart(stack, bufferSource, ConstructSlot.LEFT_ARM, packedLightIn);
                        break;
                    case "LEGS":
                        stack.translate(0.0, -0.45, 0.1);
                        this.renderConstructPart(stack, bufferSource, ConstructSlot.LEGS, packedLightIn);
                }
                stack.popPose();
                for (GeoBone childBone : bone.getChildBones()) {
                    this.renderRecursively(stack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                }
            }
            stack.popPose();
        }
    }

    private void renderConstructPart(PoseStack stack, MultiBufferSource bufferIn, ConstructSlot slot, int packedLightIn) {
        this.construction.getPart(slot).ifPresent(part -> {
            ItemStack partStack = new ItemStack(part);
            this.itemRenderer.renderStatic(partStack, ItemDisplayContext.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, stack, bufferIn, this.mc.level, 0);
        });
    }

    private void renderVerticalRadiants(ConstructWorkbenchTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.getIsCrafting()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.25, 0.5);
            int craftTicks = (int) ManaAndArtifice.instance.proxy.getGameTicks();
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
            renderHorizontalRadiant(craftTicks, matrixStackIn, bufferIn);
            renderVerticalRadiant(craftTicks, matrixStackIn, bufferIn, partialTicks);
            matrixStackIn.popPose();
        }
    }

    private static void renderVerticalRadiant(int ticks, PoseStack matrixStackIn, MultiBufferSource bufferIn, float partialTicks) {
        Random random = new Random(1234L);
        VertexConsumer lightingBuilder = bufferIn.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        matrixStackIn.pushPose();
        Random rand = new Random(333L);
        for (int i = 0; i < 200; i++) {
            matrixStackIn.pushPose();
            int[] colorStart = new int[] { 255, 255, 255 };
            int[] colorEnd = new int[] { rand.nextBoolean() ? rand.nextInt(255) : 0, rand.nextBoolean() ? rand.nextInt(128) : 0, rand.nextBoolean() ? rand.nextInt(255) : 0 };
            rand = new Random((long) i);
            matrixStackIn.scale(5.0F, 15.0F, 5.0F);
            Matrix4f currentMatrix = matrixStackIn.last().pose();
            int alpha = 20 - (int) Math.round(Math.sin((double) (((float) ticks + partialTicks + (float) i) / 3.0F)) * 20.0);
            float width = 0.015F;
            matrixStackIn.translate(-0.15F + random.nextFloat() * 0.3F, 0.0F, -0.15F + random.nextFloat() * 0.3F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F + 90.0F));
            addVertex(lightingBuilder, currentMatrix, -width, 0.0F, -width, alpha, colorStart);
            addVertex(lightingBuilder, currentMatrix, width, 0.0F, width, alpha, colorStart);
            addVertex(lightingBuilder, currentMatrix, width, -0.2F, width, 0, colorEnd);
            addVertex(lightingBuilder, currentMatrix, -width, -0.2F, -width, 0, colorEnd);
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    private static void renderHorizontalRadiant(int ticks, PoseStack matrixStackIn, MultiBufferSource bufferIn) {
        float rotationByAge = (float) ticks / 220.0F;
        Random random = new Random(1234L);
        VertexConsumer lightingBuilder = bufferIn.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        matrixStackIn.pushPose();
        matrixStackIn.scale(8.0F, 8.0F, 8.0F);
        Random rand = new Random(333L);
        for (int i = 0; i < 20; i++) {
            int[] colorStart = new int[] { rand.nextBoolean() ? rand.nextInt(128) : 0, rand.nextBoolean() ? rand.nextInt(255) : 0, rand.nextBoolean() ? rand.nextInt(255) : 0 };
            int[] colorEnd = new int[] { rand.nextBoolean() ? rand.nextInt(255) : 0, rand.nextBoolean() ? rand.nextInt(128) : 0, rand.nextBoolean() ? rand.nextInt(255) : 0 };
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F + 90.0F * rotationByAge));
            float hOffset = 0.0F;
            float vOffset = 0.15F + random.nextFloat() * 0.25F;
            Matrix4f currentMatrix = matrixStackIn.last().pose();
            int alpha = 40;
            addStartVertices(lightingBuilder, currentMatrix, alpha, colorStart);
            addVertexNegativeOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addVertexPositiveOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addStartVertices(lightingBuilder, currentMatrix, alpha, colorStart);
            addVertexPositiveOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addVertexNoOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addStartVertices(lightingBuilder, currentMatrix, alpha, colorStart);
            addVertexNoOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addVertexNegativeOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
        }
        matrixStackIn.popPose();
    }

    private static void addStartVertices(VertexConsumer vertexBuilder, Matrix4f renderMatrix, int alpha, int[] colors) {
        vertexBuilder.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
        vertexBuilder.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(colors[0], colors[1], colors[2], alpha).endVertex();
    }

    private static void addVertex(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float x, float y, float z, int alpha, int[] colors) {
        vertexBuilder.vertex(renderMatrix, x, y, z).color(colors[0], colors[1], colors[2], alpha).endVertex();
    }

    private static void addVertexNegativeOffset(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float vOffset, float hOffset, int[] colors) {
        vertexBuilder.vertex(renderMatrix, -triangle_height * hOffset, vOffset, -0.5F * hOffset).color(colors[0], colors[1], colors[2], 0).endVertex();
    }

    private static void addVertexPositiveOffset(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float vOffset, float hOffset, int[] colors) {
        vertexBuilder.vertex(renderMatrix, triangle_height * hOffset, vOffset, -0.5F * hOffset).color(colors[0], colors[1], colors[2], 0).endVertex();
    }

    private static void addVertexNoOffset(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float vOffset, float hOffset, int[] colors) {
        vertexBuilder.vertex(renderMatrix, 0.0F, vOffset, hOffset).color(colors[0], colors[1], colors[2], 0).endVertex();
    }
}