package me.srrapero720.embeddiumplus.foundation.fps;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FrameTimer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "embeddiumplus", bus = Bus.FORGE, value = { Dist.CLIENT })
public class DebugOverlayEvent {

    private static final FPSDisplay DISPLAY = new FPSDisplay();

    private static final Component MSG_FPS = Component.translatable("embeddium.plus.options.displayfps.fps");

    private static final Component MSG_MIN = Component.translatable("embeddium.plus.options.displayfps.min");

    private static final Component MSG_AVG = Component.translatable("embeddium.plus.options.displayfps.avg");

    private static final Component MSG_GPU = Component.translatable("embeddium.plus.options.displayfps.gpu");

    private static final Component MSG_MEM = Component.translatable("embeddium.plus.options.displayfps.mem");

    public static final AverageQueue AVERAGE = new AverageQueue();

    private static int fps = -1;

    private static int minFPS = -1;

    private static int avgFPS = -1;

    private static int gpuPercent = -1;

    private static int memUsage = -1;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderOverlayItem(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().getPath().equals("debug_text")) {
            if (Minecraft.getInstance().options.renderFpsChart) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        fps = mc.getFps();
        minFPS = minFPS(mc);
        memUsage = (int) (EmbyTools.ramUsed() * 100L / Runtime.getRuntime().maxMemory());
        gpuPercent = Math.min((int) mc.getGpuUtilization(), 100);
        avgFPS = AVERAGE.calculate();
        renderFPSChar(mc, event.getGuiGraphics(), mc.font, event.getWindow().getGuiScale());
    }

    private static void renderFPSChar(Minecraft mc, GuiGraphics graphics, Font font, double scale) {
        if (!mc.options.renderDebug && !mc.options.renderFpsChart) {
            EmbyConfig.FPSDisplayMode mode = (EmbyConfig.FPSDisplayMode) EmbyConfig.fpsDisplayMode.get();
            EmbyConfig.FPSDisplaySystemMode systemMode = (EmbyConfig.FPSDisplaySystemMode) EmbyConfig.fpsDisplaySystemMode.get();
            if (!mode.off() || !systemMode.off()) {
                DISPLAY.release();
                switch(mode) {
                    case SIMPLE:
                        DISPLAY.append(EmbyTools.colorByLow(fps)).add(fix(fps)).add(" ").add(MSG_FPS.getString()).add(ChatFormatting.RESET);
                        break;
                    case ADVANCED:
                        DISPLAY.append(EmbyTools.colorByLow(fps)).add(fix(fps)).add(ChatFormatting.RESET);
                        DISPLAY.append(EmbyTools.colorByLow(minFPS)).add(MSG_MIN).add(" ").add(fix(minFPS)).add(ChatFormatting.RESET);
                        DISPLAY.append(EmbyTools.colorByLow(avgFPS)).add(MSG_AVG).add(" ").add(fix(avgFPS)).add(ChatFormatting.RESET);
                }
                if (!DISPLAY.isEmpty()) {
                    DISPLAY.split();
                }
                switch(systemMode) {
                    case GPU:
                        DISPLAY.append(EmbyTools.colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
                        break;
                    case RAM:
                        DISPLAY.append(EmbyTools.colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
                        break;
                    case ON:
                        DISPLAY.append(EmbyTools.colorByPercent(gpuPercent)).add(MSG_GPU).add(" ").add(fix(gpuPercent)).add("%").add(ChatFormatting.RESET);
                        DISPLAY.append(EmbyTools.colorByPercent(memUsage)).add(MSG_MEM).add(" ").add(fix(memUsage)).add("%").add(ChatFormatting.RESET);
                }
                if (DISPLAY.isEmpty()) {
                    DISPLAY.add("FATAL ERROR");
                }
                float margin = scale > 0.0 ? (float) EmbyConfig.fpsDisplayMarginCache / (float) scale : (float) EmbyConfig.fpsDisplayMarginCache;
                String displayString = DISPLAY.toString();
                float maxPosX = (float) (graphics.guiWidth() - font.width(displayString));
                float posX = switch((EmbyConfig.FPSDisplayGravity) EmbyConfig.fpsDisplayGravity.get()) {
                    case LEFT ->
                        margin;
                    case CENTER ->
                        maxPosX / 2.0F;
                    case RIGHT ->
                        maxPosX - margin;
                };
                graphics.pose().pushPose();
                if (EmbyConfig.fpsDisplayShadowCache) {
                    graphics.fill((int) posX - 2, (int) margin - 2, (int) posX + font.width(displayString) + 2, (int) (margin + 9.0F) + 1, -1873784752);
                    graphics.flush();
                }
                graphics.drawString(font, displayString, posX, margin, -1, true);
                DISPLAY.release();
                graphics.pose().popPose();
            }
        }
    }

    private static String fix(int value) {
        return value == -1 ? "--" : value + "";
    }

    private static int minFPS(Minecraft mc) {
        FrameTimer timer = mc.getFrameTimer();
        int start = timer.getLogStart();
        int end = timer.getLogEnd();
        if (end == start) {
            return minFPS;
        } else {
            int fps = mc.getFps();
            if (fps <= 0) {
                fps = 1;
            }
            long[] frames = timer.getLog();
            long maxNS = (long) (1.0 / (double) fps * 1.0E9);
            long totalNS = 0L;
            for (int index = Math.floorMod(end - 1, frames.length); index != start && (double) totalNS < 1.0E9; index = Math.floorMod(index - 1, frames.length)) {
                long timeNs = frames[index];
                if (timeNs > maxNS) {
                    maxNS = timeNs;
                }
                totalNS += timeNs;
            }
            return (int) (1.0 / ((double) maxNS / 1.0E9));
        }
    }
}