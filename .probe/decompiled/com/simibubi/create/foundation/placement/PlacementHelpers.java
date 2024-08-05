package com.simibubi.create.foundation.placement;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Matrix4f;

@EventBusSubscriber
public class PlacementHelpers {

    private static final List<IPlacementHelper> helpers = new ArrayList();

    private static int animationTick = 0;

    private static final LerpedFloat angle = LerpedFloat.angular().chase(0.0, 0.25, LerpedFloat.Chaser.EXP);

    private static BlockPos target = null;

    private static BlockPos lastTarget = null;

    public static int register(IPlacementHelper helper) {
        helpers.add(helper);
        return helpers.size() - 1;
    }

    public static IPlacementHelper get(int id) {
        if (id >= 0 && id < helpers.size()) {
            return (IPlacementHelper) helpers.get(id);
        } else {
            throw new ArrayIndexOutOfBoundsException("id " + id + " for placement helper not known");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void tick() {
        setTarget(null);
        checkHelpers();
        if (target == null) {
            if (animationTick > 0) {
                animationTick = Math.max(animationTick - 2, 0);
            }
        } else {
            if (animationTick < 10) {
                animationTick++;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void checkHelpers() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        if (world != null) {
            if (mc.hitResult instanceof BlockHitResult ray) {
                if (mc.player != null) {
                    if (!mc.player.isShiftKeyDown()) {
                        for (InteractionHand hand : InteractionHand.values()) {
                            ItemStack heldItem = mc.player.m_21120_(hand);
                            List<IPlacementHelper> filteredForHeldItem = (List<IPlacementHelper>) helpers.stream().filter(helper -> helper.matchesItem(heldItem)).collect(Collectors.toList());
                            if (!filteredForHeldItem.isEmpty()) {
                                BlockPos pos = ray.getBlockPos();
                                BlockState state = world.m_8055_(pos);
                                List<IPlacementHelper> filteredForState = (List<IPlacementHelper>) filteredForHeldItem.stream().filter(helper -> helper.matchesState(state)).collect(Collectors.toList());
                                if (!filteredForState.isEmpty()) {
                                    boolean atLeastOneMatch = false;
                                    for (IPlacementHelper h : filteredForState) {
                                        PlacementOffset offset = h.getOffset(mc.player, world, state, pos, ray, heldItem);
                                        if (offset.isSuccessful()) {
                                            h.renderAt(pos, state, ray, offset);
                                            setTarget(offset.getBlockPos());
                                            atLeastOneMatch = true;
                                            break;
                                        }
                                    }
                                    if (atLeastOneMatch) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static void setTarget(BlockPos target) {
        PlacementHelpers.target = target;
        if (target != null) {
            if (lastTarget == null) {
                lastTarget = target;
            } else {
                if (!lastTarget.equals(target)) {
                    lastTarget = target;
                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void afterRenderOverlayLayer(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null && animationTick > 0) {
                Window res = event.getWindow();
                float screenY = (float) res.getGuiScaledHeight() / 2.0F;
                float screenX = (float) res.getGuiScaledWidth() / 2.0F;
                float progress = getCurrentAlpha();
                drawDirectionIndicator(event.getGuiGraphics(), event.getPartialTick(), screenX, screenY, progress);
            }
        }
    }

    public static float getCurrentAlpha() {
        return Math.min((float) animationTick / 10.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawDirectionIndicator(GuiGraphics graphics, float partialTicks, float centerX, float centerY, float progress) {
        float r = 0.8F;
        float g = 0.8F;
        float b = 0.8F;
        float a = progress * progress;
        Vec3 projTarget = VecHelper.projectToPlayerView(VecHelper.getCenterOf(lastTarget), partialTicks);
        Vec3 target = new Vec3(projTarget.x, projTarget.y, 0.0);
        if (projTarget.z > 0.0) {
            target = target.reverse();
        }
        Vec3 norm = target.normalize();
        Vec3 ref = new Vec3(0.0, 1.0, 0.0);
        float targetAngle = AngleHelper.deg(Math.acos(norm.dot(ref)));
        if (norm.x < 0.0) {
            targetAngle = 360.0F - targetAngle;
        }
        if (animationTick < 10) {
            angle.setValue((double) targetAngle);
        }
        angle.chase((double) targetAngle, 0.25, LerpedFloat.Chaser.EXP);
        angle.tickChaser();
        float snapSize = 22.5F;
        float snappedAngle = snapSize * (float) Math.round(angle.getValue(0.0F) / snapSize) % 360.0F;
        float length = 10.0F;
        CClient.PlacementIndicatorSetting mode = (CClient.PlacementIndicatorSetting) AllConfigs.client().placementIndicator.get();
        PoseStack ms = graphics.pose();
        if (mode == CClient.PlacementIndicatorSetting.TRIANGLE) {
            fadedArrow(ms, centerX, centerY, r, g, b, a, length, snappedAngle);
        } else if (mode == CClient.PlacementIndicatorSetting.TEXTURE) {
            textured(ms, centerX, centerY, a, snappedAngle);
        }
    }

    private static void fadedArrow(PoseStack ms, float centerX, float centerY, float r, float g, float b, float a, float length, float snappedAngle) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172811_);
        ms.pushPose();
        ms.translate(centerX, centerY, 5.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(angle.getValue(0.0F)));
        double scale = AllConfigs.client().indicatorScale.get();
        ms.scale((float) scale, (float) scale, 1.0F);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f mat = ms.last().pose();
        bufferbuilder.m_252986_(mat, 0.0F, -(10.0F + length), 0.0F).color(r, g, b, a).endVertex();
        bufferbuilder.m_252986_(mat, -9.0F, -3.0F, 0.0F).color(r, g, b, 0.0F).endVertex();
        bufferbuilder.m_252986_(mat, -6.0F, -6.0F, 0.0F).color(r, g, b, 0.0F).endVertex();
        bufferbuilder.m_252986_(mat, -3.0F, -8.0F, 0.0F).color(r, g, b, 0.0F).endVertex();
        bufferbuilder.m_252986_(mat, 0.0F, -8.5F, 0.0F).color(r, g, b, 0.0F).endVertex();
        bufferbuilder.m_252986_(mat, 3.0F, -8.0F, 0.0F).color(r, g, b, 0.0F).endVertex();
        bufferbuilder.m_252986_(mat, 6.0F, -6.0F, 0.0F).color(r, g, b, 0.0F).endVertex();
        bufferbuilder.m_252986_(mat, 9.0F, -3.0F, 0.0F).color(r, g, b, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        ms.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static void textured(PoseStack ms, float centerX, float centerY, float alpha, float snappedAngle) {
        AllGuiTextures.PLACEMENT_INDICATOR_SHEET.bind();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172814_);
        ms.pushPose();
        ms.translate(centerX, centerY, 50.0F);
        float scale = AllConfigs.client().indicatorScale.get().floatValue() * 0.75F;
        ms.scale(scale, scale, 1.0F);
        ms.scale(12.0F, 12.0F, 1.0F);
        float index = snappedAngle / 22.5F;
        float tex_size = 0.0625F;
        float tx = 0.0F;
        float ty = index * tex_size;
        float tw = 1.0F;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        Matrix4f mat = ms.last().pose();
        buffer.m_252986_(mat, -1.0F, -1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(tx, ty).endVertex();
        buffer.m_252986_(mat, -1.0F, 1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(tx, ty + tex_size).endVertex();
        buffer.m_252986_(mat, 1.0F, 1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(tx + tw, ty + tex_size).endVertex();
        buffer.m_252986_(mat, 1.0F, -1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(tx + tw, ty).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
        ms.popPose();
    }
}