package com.simibubi.create.foundation.ponder;

import com.google.common.base.Strings;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.ponder.ui.NavigatableSimiScreen;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class PonderTooltipHandler {

    public static boolean enable = true;

    static LerpedFloat holdWProgress = LerpedFloat.linear().startWithValue(0.0);

    static ItemStack hoveredStack = ItemStack.EMPTY;

    static ItemStack trackingStack = ItemStack.EMPTY;

    static boolean subject = false;

    static boolean deferTick = false;

    public static final String HOLD_TO_PONDER = "ponder.hold_to_ponder";

    public static final String SUBJECT = "ponder.subject";

    public static void tick() {
        deferTick = true;
    }

    public static void deferredTick() {
        deferTick = false;
        Minecraft instance = Minecraft.getInstance();
        Screen currentScreen = instance.screen;
        if (!hoveredStack.isEmpty() && !trackingStack.isEmpty()) {
            float value = holdWProgress.getValue();
            int keyCode = ponderKeybind().getKey().getValue();
            long window = instance.getWindow().getWindow();
            if (!subject && InputConstants.isKeyDown(window, keyCode)) {
                if (value >= 1.0F) {
                    if (currentScreen instanceof NavigatableSimiScreen) {
                        ((NavigatableSimiScreen) currentScreen).centerScalingOnMouse();
                    }
                    ScreenOpener.transitionTo(PonderUI.of(trackingStack));
                    holdWProgress.startWithValue(0.0);
                    return;
                }
                holdWProgress.setValue((double) Math.min(1.0F, value + Math.max(0.25F, value) * 0.25F));
            } else {
                holdWProgress.setValue((double) Math.max(0.0F, value - 0.05F));
            }
            hoveredStack = ItemStack.EMPTY;
        } else {
            trackingStack = ItemStack.EMPTY;
            holdWProgress.startWithValue(0.0);
        }
    }

    public static void addToTooltip(ItemTooltipEvent event) {
        if (enable) {
            ItemStack stack = event.getItemStack();
            updateHovered(stack);
            if (deferTick) {
                deferredTick();
            }
            if (trackingStack == stack) {
                float renderPartialTicks = Minecraft.getInstance().getFrameTime();
                Component component = (Component) (subject ? Lang.translateDirect("ponder.subject").withStyle(ChatFormatting.GREEN) : makeProgressBar(Math.min(1.0F, holdWProgress.getValue(renderPartialTicks) * 8.0F / 7.0F)));
                List<Component> tooltip = event.getToolTip();
                if (tooltip.size() < 2) {
                    tooltip.add(component);
                } else {
                    tooltip.add(1, component);
                }
            }
        }
    }

    protected static void updateHovered(ItemStack stack) {
        Minecraft instance = Minecraft.getInstance();
        Screen currentScreen = instance.screen;
        boolean inPonderUI = currentScreen instanceof PonderUI;
        ItemStack prevStack = trackingStack;
        hoveredStack = ItemStack.EMPTY;
        subject = false;
        if (inPonderUI) {
            PonderUI ponderUI = (PonderUI) currentScreen;
            if (ItemHelper.sameItem(stack, ponderUI.getSubject())) {
                subject = true;
            }
        }
        if (!stack.isEmpty()) {
            if (PonderRegistry.ALL.containsKey(RegisteredObjects.getKeyOrThrow(stack.getItem()))) {
                if (prevStack.isEmpty() || !ItemHelper.sameItem(prevStack, stack)) {
                    holdWProgress.startWithValue(0.0);
                }
                hoveredStack = stack;
                trackingStack = stack;
            }
        }
    }

    public static void handleTooltipColor(RenderTooltipEvent.Color event) {
        if (trackingStack == event.getItemStack()) {
            if (holdWProgress.getValue() != 0.0F) {
                float renderPartialTicks = Minecraft.getInstance().getFrameTime();
                int start = event.getOriginalBorderStart();
                int end = event.getOriginalBorderEnd();
                float progress = Math.min(1.0F, holdWProgress.getValue(renderPartialTicks) * 8.0F / 7.0F);
                start = getSmoothColorForProgress(progress);
                end = getSmoothColorForProgress(progress);
                event.setBorderStart(start | -1610612736);
                event.setBorderEnd(end | -1610612736);
            }
        }
    }

    private static int getSmoothColorForProgress(float progress) {
        return progress < 0.5F ? Color.mixColors(5243135, 5592575, progress * 2.0F) : Color.mixColors(5592575, 16777215, (progress - 0.5F) * 2.0F);
    }

    private static Component makeProgressBar(float progress) {
        MutableComponent holdW = Lang.translateDirect("ponder.hold_to_ponder", ((MutableComponent) ponderKeybind().getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY);
        Font fontRenderer = Minecraft.getInstance().font;
        float charWidth = (float) fontRenderer.width("|");
        float tipWidth = (float) fontRenderer.width(holdW);
        int total = (int) (tipWidth / charWidth);
        int current = (int) (progress * (float) total);
        if (progress > 0.0F) {
            String bars = "";
            bars = bars + ChatFormatting.GRAY + Strings.repeat("|", current);
            if (progress < 1.0F) {
                bars = bars + ChatFormatting.DARK_GRAY + Strings.repeat("|", total - current);
            }
            return Components.literal(bars);
        } else {
            return holdW;
        }
    }

    protected static KeyMapping ponderKeybind() {
        return Minecraft.getInstance().options.keyUp;
    }
}