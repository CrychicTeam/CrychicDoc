package dev.latvian.mods.kubejs.client.painter.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.unit.FixedBooleanUnit;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ItemObject extends BoxObject {

    public ItemStack itemStack = ItemStack.EMPTY;

    public Unit overlay = FixedBooleanUnit.TRUE;

    public String customText = "";

    public Unit rotation = FixedNumberUnit.ZERO;

    public ItemObject(Painter painter) {
        super(painter);
        this.z = FixedNumberUnit.of(100.0);
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        if (properties.hasAny("item")) {
            this.itemStack = ItemStackJS.of(properties.tag.get("item"));
        }
        this.overlay = properties.getUnit("overlay", this.overlay);
        this.customText = properties.getString("customText", this.customText);
        this.rotation = properties.getUnit("rotation", this.rotation);
    }

    @Override
    public void draw(PaintScreenEventJS event) {
        if (!this.itemStack.isEmpty()) {
            float aw = this.w.getFloat(event);
            float ah = this.h.getFloat(event);
            float ax = event.alignX(this.x.getFloat(event), aw, this.alignX);
            float ay = event.alignY(this.y.getFloat(event), ah, this.alignY);
            float az = this.z.getFloat(event);
            event.push();
            event.translate((double) ax, (double) ay, (double) az);
            if (this.rotation != FixedNumberUnit.ZERO) {
                event.rotateRad(this.rotation.getFloat(event));
            }
            event.scale(aw / 16.0F, ah / 16.0F, 1.0F);
            event.blend(true);
            drawItem(event.matrices, this.itemStack, 0, this.overlay.getBoolean(event), this.customText.isEmpty() ? null : this.customText);
            event.pop();
        }
    }

    public static void drawItem(PoseStack poseStack, ItemStack stack, int hash, boolean renderOverlay, @Nullable String text) {
        if (!stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(stack, null, mc.player, hash);
            Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.mulPoseMatrix(poseStack.last().pose());
            modelViewStack.scale(1.0F, -1.0F, 1.0F);
            modelViewStack.scale(16.0F, 16.0F, 16.0F);
            RenderSystem.applyModelViewMatrix();
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean flatLight = !bakedModel.usesBlockLight();
            if (flatLight) {
                Lighting.setupForFlatItems();
            }
            itemRenderer.render(stack, ItemDisplayContext.GUI, false, new PoseStack(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (flatLight) {
                Lighting.setupFor3DItems();
            }
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
            if (renderOverlay) {
                Tesselator t = Tesselator.getInstance();
                Font font = mc.font;
                if (stack.getCount() != 1 || text != null) {
                    String s = text == null ? String.valueOf(stack.getCount()) : text;
                    poseStack.pushPose();
                    poseStack.translate(9.0 - (double) font.width(s), 1.0, 20.0);
                    font.drawInBatch(s, 0.0F, 0.0F, 16777215, true, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    bufferSource.endBatch();
                    poseStack.popPose();
                }
                if (stack.isBarVisible()) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.disableBlend();
                    int barWidth = stack.getBarWidth();
                    int barColor = stack.getBarColor();
                    draw(poseStack, t, -6, 5, 13, 2, 0, 0, 0, 255);
                    draw(poseStack, t, -6, 5, barWidth, 1, barColor >> 16 & 0xFF, barColor >> 8 & 0xFF, barColor & 0xFF, 255);
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                }
                float cooldown = mc.player == null ? 0.0F : mc.player.m_36335_().getCooldownPercent(stack.getItem(), mc.getFrameTime());
                if (cooldown > 0.0F) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    draw(poseStack, t, -8, Mth.floor(16.0F * (1.0F - cooldown)) - 8, 16, Mth.ceil(16.0F * cooldown), 255, 255, 255, 127);
                    RenderSystem.enableDepthTest();
                }
            }
        }
    }

    private static void draw(PoseStack matrixStack, Tesselator t, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        if (width > 0 && height > 0) {
            RenderSystem.setShader(GameRenderer::m_172811_);
            Matrix4f m = matrixStack.last().pose();
            BufferBuilder renderer = t.getBuilder();
            renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            renderer.m_252986_(m, (float) x, (float) y, 0.0F).color(red, green, blue, alpha).endVertex();
            renderer.m_252986_(m, (float) x, (float) (y + height), 0.0F).color(red, green, blue, alpha).endVertex();
            renderer.m_252986_(m, (float) (x + width), (float) (y + height), 0.0F).color(red, green, blue, alpha).endVertex();
            renderer.m_252986_(m, (float) (x + width), (float) y, 0.0F).color(red, green, blue, alpha).endVertex();
            t.end();
        }
    }
}