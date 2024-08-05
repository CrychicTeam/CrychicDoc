package com.simibubi.create.content.equipment.goggles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.trains.entity.TrainRelocator;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.outliner.Outline;
import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class GoggleOverlayRenderer {

    public static final IGuiOverlay OVERLAY = GoggleOverlayRenderer::renderOverlay;

    private static final Map<Object, Outliner.OutlineEntry> outlines = CreateClient.OUTLINER.getOutlines();

    public static int hoverTicks = 0;

    public static BlockPos lastHovered = null;

    public static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            HitResult objectMouseOver = mc.hitResult;
            if (!(objectMouseOver instanceof BlockHitResult)) {
                lastHovered = null;
                hoverTicks = 0;
            } else {
                for (Outliner.OutlineEntry entry : outlines.values()) {
                    if (entry.isAlive()) {
                        Outline outline = entry.getOutline();
                        if (outline instanceof ValueBox && !((ValueBox) outline).isPassive) {
                            return;
                        }
                    }
                }
                BlockHitResult result = (BlockHitResult) objectMouseOver;
                ClientLevel world = mc.level;
                BlockPos pos = result.getBlockPos();
                int prevHoverTicks = hoverTicks++;
                lastHovered = pos;
                pos = proxiedOverlayPosition(world, pos);
                BlockEntity be = world.m_7702_(pos);
                boolean wearingGoggles = GogglesItem.isWearingGoggles(mc.player);
                boolean hasGoggleInformation = be instanceof IHaveGoggleInformation;
                boolean hasHoveringInformation = be instanceof IHaveHoveringInformation;
                boolean goggleAddedInformation = false;
                boolean hoverAddedInformation = false;
                List<Component> tooltip = new ArrayList();
                if (hasGoggleInformation && wearingGoggles) {
                    IHaveGoggleInformation gte = (IHaveGoggleInformation) be;
                    goggleAddedInformation = gte.addToGoggleTooltip(tooltip, mc.player.isShiftKeyDown());
                }
                if (hasHoveringInformation) {
                    if (!tooltip.isEmpty()) {
                        tooltip.add(Components.immutableEmpty());
                    }
                    IHaveHoveringInformation hte = (IHaveHoveringInformation) be;
                    hoverAddedInformation = hte.addToTooltip(tooltip, mc.player.isShiftKeyDown());
                    if (goggleAddedInformation && !hoverAddedInformation) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                }
                if (be instanceof IDisplayAssemblyExceptions) {
                    boolean exceptionAdded = ((IDisplayAssemblyExceptions) be).addExceptionToTooltip(tooltip);
                    if (exceptionAdded) {
                        hasHoveringInformation = true;
                        hoverAddedInformation = true;
                    }
                }
                if (!hasHoveringInformation && (hasHoveringInformation = hoverAddedInformation = TrainRelocator.addToTooltip(tooltip, mc.player.isShiftKeyDown()))) {
                    hoverTicks = prevHoverTicks + 1;
                }
                if (hasGoggleInformation && !goggleAddedInformation && hasHoveringInformation && !hoverAddedInformation) {
                    hoverTicks = 0;
                } else {
                    BlockState state = world.m_8055_(pos);
                    if (wearingGoggles && AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
                        Direction[] directions = Iterate.directionsInAxis(((Direction) state.m_61143_(PistonExtensionPoleBlock.f_52588_)).getAxis());
                        int poles = 1;
                        boolean pistonFound = false;
                        for (Direction dir : directions) {
                            int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get().attachedPoles(world, pos, dir);
                            poles += attachedPoles;
                            pistonFound |= world.m_8055_(pos.relative(dir, attachedPoles + 1)).m_60734_() instanceof MechanicalPistonBlock;
                        }
                        if (!pistonFound) {
                            hoverTicks = 0;
                            return;
                        }
                        if (!tooltip.isEmpty()) {
                            tooltip.add(Components.immutableEmpty());
                        }
                        tooltip.add(IHaveGoggleInformation.componentSpacing.plainCopy().append(Lang.translateDirect("gui.goggles.pole_length")).append(Components.literal(" " + poles)));
                    }
                    if (tooltip.isEmpty()) {
                        hoverTicks = 0;
                    } else {
                        PoseStack poseStack = graphics.pose();
                        poseStack.pushPose();
                        int tooltipTextWidth = 0;
                        for (FormattedText textLine : tooltip) {
                            int textLineWidth = mc.font.width(textLine);
                            if (textLineWidth > tooltipTextWidth) {
                                tooltipTextWidth = textLineWidth;
                            }
                        }
                        int tooltipHeight = 8;
                        if (tooltip.size() > 1) {
                            tooltipHeight += 2;
                            tooltipHeight += (tooltip.size() - 1) * 10;
                        }
                        CClient cfg = AllConfigs.client();
                        int posX = width / 2 + cfg.overlayOffsetX.get();
                        int posY = height / 2 + cfg.overlayOffsetY.get();
                        posX = Math.min(posX, width - tooltipTextWidth - 20);
                        posY = Math.min(posY, height - tooltipHeight - 20);
                        float fade = Mth.clamp(((float) hoverTicks + partialTicks) / 24.0F, 0.0F, 1.0F);
                        Boolean useCustom = cfg.overlayCustomColor.get();
                        Color colorBackground = useCustom ? new Color(cfg.overlayBackgroundColor.get()) : Theme.c(Theme.Key.VANILLA_TOOLTIP_BACKGROUND).scaleAlpha(0.75F);
                        Color colorBorderTop = useCustom ? new Color(cfg.overlayBorderColorTop.get()) : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, true).copy();
                        Color colorBorderBot = useCustom ? new Color(cfg.overlayBorderColorBot.get()) : Theme.c(Theme.Key.VANILLA_TOOLTIP_BORDER, false).copy();
                        if (fade < 1.0F) {
                            poseStack.translate(Math.pow((double) (1.0F - fade), 3.0) * (double) Math.signum((float) cfg.overlayOffsetX.get().intValue() + 0.5F) * 8.0, 0.0, 0.0);
                            colorBackground.scaleAlpha(fade);
                            colorBorderTop.scaleAlpha(fade);
                            colorBorderBot.scaleAlpha(fade);
                        }
                        RemovedGuiUtils.drawHoveringText(graphics, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(), colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);
                        ItemStack item = AllItems.GOGGLES.asStack();
                        GuiGameElement.of(item).<RenderElement>at((float) (posX + 10), (float) (posY - 16), 450.0F).render(graphics);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    public static BlockPos proxiedOverlayPosition(Level level, BlockPos pos) {
        BlockState targetedState = level.getBlockState(pos);
        return targetedState.m_60734_() instanceof IProxyHoveringInformation proxy ? proxy.getInformationSource(level, pos, targetedState) : pos;
    }
}