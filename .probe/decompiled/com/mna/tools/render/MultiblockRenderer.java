package com.mna.tools.render;

import com.mna.ManaAndArtifice;
import com.mna.api.blocks.tile.IMultiblockDefinition;
import com.mna.tools.DidYouKnowHelper;
import com.mna.tools.RotationUtils;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.awt.Color;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.mutable.MutableInt;

@EventBusSubscriber({ Dist.CLIENT })
public class MultiblockRenderer {

    public static boolean hasMultiblock;

    private static IMultiblockDefinition multiblock;

    private static Component name;

    private static BlockPos renderPos;

    private static boolean posLocked;

    private static Rotation facingRotation;

    private static int blocks;

    private static int blocksDone;

    private static boolean syncWait = true;

    private static int timeComplete;

    private static BlockState lookingState;

    private static BlockPos lookingPos;

    private static MultiBufferSource.BufferSource buffers = null;

    private static boolean lowestLayerMode = true;

    public static void setMultiblock(IMultiblockDefinition multiblock, Component name, boolean flip) {
        if (flip && hasMultiblock) {
            hasMultiblock = false;
        } else {
            MultiblockRenderer.multiblock = multiblock;
            MultiblockRenderer.name = name;
            renderPos = null;
            hasMultiblock = multiblock != null;
            posLocked = false;
        }
    }

    public static void toggleLowestLayerMode() {
        lowestLayerMode = !lowestLayerMode;
    }

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiOverlayEvent.Post event) {
        if (hasMultiblock) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            Minecraft mc = Minecraft.getInstance();
            int waitTime = 40;
            int fadeOutSpeed = 4;
            int fullAnimTime = waitTime + 10;
            float animTime = (float) timeComplete + (timeComplete == 0 ? 0.0F : event.getPartialTick());
            if (animTime > (float) fullAnimTime) {
                hasMultiblock = false;
                return;
            }
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, -Math.max(0.0F, animTime - (float) waitTime) * (float) fadeOutSpeed, 0.0F);
            int x = event.getWindow().getGuiScaledWidth() / 2;
            int y = 16;
            int width = 180;
            int height = 9;
            int left = x - width / 2;
            int top = y + 10;
            if (timeComplete > 0) {
                String s = I18n.get("gui.mna:structure_complete");
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0.0F, Math.min((float) (height + 5), animTime), 0.0F);
                guiGraphics.drawString(mc.font, s, x - mc.font.width(s) / 2, top + height - 10, 65280, true);
                guiGraphics.pose().popPose();
            }
            guiGraphics.fill(left - 1, top - 1, left + width + 1, top + height + 1, -16777216);
            guiGraphics.fillGradient(left, top, left + width, top + height, -10066330, -11184811);
            float pct = MathUtils.clamp01((float) blocksDone / (float) Math.max(1, blocks));
            int progressWidth = (int) ((float) width * pct);
            int color = Mth.hsvToRgb(pct / 3.0F, 1.0F, 1.0F) | 0xFF000000;
            int color2 = new Color(color).darker().getRGB();
            guiGraphics.fillGradient(left, top, left + progressWidth, top + height, color, color2);
            if (mc.options.forceUnicodeFont.get()) {
                guiGraphics.drawString(mc.font, name.getVisualOrderText(), x - mc.font.width(name) / 2, y + 10, 16777215, true);
            } else {
                guiGraphics.drawString(mc.font, name.getVisualOrderText(), x - mc.font.width(name) / 2, y + 11, 16777215, true);
            }
            if (!posLocked) {
                String s = I18n.get("gui.mna:not_anchored");
                guiGraphics.drawString(mc.font, s, x - mc.font.width(s) / 2, top + height + 8, 16777215, true);
            } else {
                if (lookingState != null) {
                    try {
                        Block block = lookingState.m_60734_();
                        ItemStack stack = block.getCloneItemStack(lookingState, mc.hitResult, mc.level, lookingPos, mc.player);
                        if (!stack.isEmpty()) {
                            guiGraphics.drawString(mc.font, stack.getHoverName().getVisualOrderText(), left + 20, top + height + 8, 16777215, true);
                            guiGraphics.renderItem(stack, left, top + height + 2);
                        }
                    } catch (Exception var21) {
                    }
                }
                if (timeComplete == 0) {
                    color = 16777215;
                    int posx = left + width;
                    int posy = top + height + 2;
                    int mult = 1;
                    String progress = blocksDone + "/" + blocks;
                    guiGraphics.drawString(mc.font, progress, posx - mc.font.width(progress) / mult, posy, color, true);
                }
            }
            guiGraphics.pose().popPose();
        }
    }

    @SubscribeEvent
    public static void onWorldRenderLast(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES && hasMultiblock && multiblock != null) {
            renderMultiblock(ManaAndArtifice.instance.proxy.getClientWorld(), event.getPoseStack());
        }
    }

    public static void anchorTo(BlockPos target, Rotation rot) {
        renderPos = target;
        facingRotation = rot;
        posLocked = true;
        DidYouKnowHelper.CheckAndShowDidYouKnow(ManaAndArtifice.instance.proxy.getClientPlayer(), "helptip.mna.visualized_multiblock");
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (hasMultiblock && !posLocked && event.getEntity() == Minecraft.getInstance().player) {
            anchorTo(event.getPos().relative(event.getFace()), getRotation(event.getEntity()));
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (ManaAndArtifice.instance.proxy.getClientWorld() == null) {
            hasMultiblock = false;
        } else if (posLocked && !syncWait && blocks == blocksDone) {
            timeComplete++;
            if (timeComplete == 16) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F));
            }
            if (timeComplete == 60) {
                setMultiblock(null, null, false);
            }
        } else {
            timeComplete = 0;
        }
    }

    public static void renderMultiblock(Level world, PoseStack ms) {
        Minecraft mc = Minecraft.getInstance();
        if (!posLocked) {
            facingRotation = getRotation(mc.player);
            if (mc.hitResult instanceof BlockHitResult) {
                renderPos = ((BlockHitResult) mc.hitResult).getBlockPos().relative(((BlockHitResult) mc.hitResult).getDirection());
            }
        } else if (renderPos.m_203193_(mc.player.m_20182_()) > 4096.0) {
            return;
        }
        if (renderPos != null) {
            if (multiblock.isSymmetrical()) {
                facingRotation = Rotation.NONE;
            }
            EntityRenderDispatcher erm = mc.getEntityRenderDispatcher();
            double renderPosX = erm.camera.getPosition().x();
            double renderPosY = erm.camera.getPosition().y();
            double renderPosZ = erm.camera.getPosition().z();
            ms.translate(-renderPosX, -renderPosY, -renderPosZ);
            if (buffers == null) {
                buffers = initBuffers(mc.renderBuffers().bufferSource());
            }
            BlockPos checkPos = null;
            if (mc.hitResult instanceof BlockHitResult blockRes) {
                checkPos = blockRes.getBlockPos().relative(blockRes.getDirection());
            }
            blocks = 0;
            blocksDone = 0;
            lookingState = null;
            lookingPos = checkPos;
            MutableInt lowestY = new MutableInt(Integer.MAX_VALUE);
            HashMap<BlockPos, BlockState> missingBlocks = multiblock.getMissingBlocks(world, getStartPos(), getFacingRotation(), true);
            if (missingBlocks != null) {
                blocksDone = multiblock.getBlockCount() - missingBlocks.size();
                blocks = multiblock.getBlockCount();
                float alpha = 0.2F;
                if (lowestLayerMode) {
                    missingBlocks.entrySet().stream().forEach(e -> {
                        if (lowestY.getValue() > ((BlockPos) e.getKey()).m_123342_()) {
                            lowestY.setValue(((BlockPos) e.getKey()).m_123342_());
                        }
                    });
                }
                missingBlocks.entrySet().forEach(e -> {
                    if (!posLocked() || !lowestLayerMode || ((BlockPos) e.getKey()).m_123342_() == lowestY.getValue()) {
                        renderBlock(world, (BlockState) e.getValue(), (BlockPos) e.getKey(), alpha, ms);
                    }
                });
                syncWait = false;
            } else {
                syncWait = true;
            }
            buffers.endBatch();
            if (!posLocked) {
                blocksDone = 0;
                blocks = 0;
            }
        }
    }

    public static void renderBlock(Level world, BlockState state, BlockPos pos, float alpha, PoseStack ms) {
        if (pos != null) {
            ms.pushPose();
            ms.translate((float) pos.m_123341_(), (float) pos.m_123342_(), (float) pos.m_123343_());
            if (state.m_60734_() == Blocks.AIR) {
                float scale = 0.3F;
                float off = (1.0F - scale) / 2.0F;
                ms.translate(off, off, -off);
                ms.scale(scale, scale, scale);
                state = Blocks.RED_CONCRETE.defaultBlockState();
            }
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, ms, buffers, 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            ms.popPose();
        }
    }

    public static IMultiblockDefinition getMultiblock() {
        return multiblock;
    }

    public static boolean posLocked() {
        return posLocked;
    }

    public static Rotation getFacingRotation() {
        return multiblock.isSymmetrical() ? Rotation.NONE : facingRotation;
    }

    public static BlockPos getStartPos() {
        return renderPos;
    }

    private static Rotation getRotation(Entity entity) {
        return RotationUtils.rotationFromFacing(entity.getDirection());
    }

    private static MultiBufferSource.BufferSource initBuffers(MultiBufferSource.BufferSource original) {
        BufferBuilder fallback = (BufferBuilder) ObfuscationReflectionHelper.getPrivateValue(MultiBufferSource.BufferSource.class, original, "f_109904_");
        Map<RenderType, BufferBuilder> layerBuffers = (Map<RenderType, BufferBuilder>) ObfuscationReflectionHelper.getPrivateValue(MultiBufferSource.BufferSource.class, original, "f_109905_");
        Map<RenderType, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap();
        for (Entry<RenderType, BufferBuilder> e : layerBuffers.entrySet()) {
            remapped.put(MultiblockRenderer.GhostRenderType.remap((RenderType) e.getKey()), (BufferBuilder) e.getValue());
        }
        return new MultiblockRenderer.GhostBuffers(fallback, remapped);
    }

    private static class GhostBuffers extends MultiBufferSource.BufferSource {

        protected GhostBuffers(BufferBuilder fallback, Map<RenderType, BufferBuilder> layerBuffers) {
            super(fallback, layerBuffers);
        }

        @Override
        public VertexConsumer getBuffer(RenderType type) {
            return super.getBuffer(MultiblockRenderer.GhostRenderType.remap(type));
        }
    }

    private static class GhostRenderType extends RenderType {

        private static Map<RenderType, RenderType> remappedTypes = new IdentityHashMap();

        private GhostRenderType(RenderType original) {
            super(String.format("%s_%s_ghost", original.toString(), "mna"), original.format(), original.mode(), original.bufferSize(), original.affectsCrumbling(), true, () -> {
                original.m_110185_();
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.blendEquation(32774);
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.6F);
            }, () -> {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                RenderSystem.enableDepthTest();
                original.m_110188_();
            });
        }

        public boolean equals(@Nullable Object other) {
            return this == other;
        }

        public int hashCode() {
            return System.identityHashCode(this);
        }

        public static RenderType remap(RenderType in) {
            return in instanceof MultiblockRenderer.GhostRenderType ? in : (RenderType) remappedTypes.computeIfAbsent(in, MultiblockRenderer.GhostRenderType::new);
        }
    }
}