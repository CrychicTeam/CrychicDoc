package journeymap.client.event.handlers;

import java.lang.invoke.StringConcatFactory;
import java.util.Collections;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.task.multi.MapPlayerTask;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.Effect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;

public class HudOverlayHandler {

    private static final String DEBUG_PREFIX = ChatFormatting.AQUA + "[JM] " + ChatFormatting.RESET;

    private static final String DEBUG_SUFFIX = "";

    private final Minecraft mc = Minecraft.getInstance();

    private long statTimerCheck;

    private List<String> statTimerReport = Collections.EMPTY_LIST;

    public boolean preOverlay(GuiGraphics graphics) {
        if (!Minecraft.getInstance().options.hideGui && Effect.getInstance().canPotionShift()) {
            Vec2 location = Effect.getInstance().getPotionEffectsLocation();
            graphics.pose().pushPose();
            graphics.pose().translate(location.x, location.y, 0.0F);
            return true;
        } else {
            return false;
        }
    }

    public void postOverlay(GuiGraphics graphics) {
        graphics.pose().popPose();
    }

    public void onRenderOverlayDebug(List<String> leftText) {
        try {
            if (this.mc.options.renderDebug && !"off".equalsIgnoreCase(JourneymapClient.getInstance().getCoreProperties().logLevel.get())) {
                leftText.add(null);
                if (JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get()) {
                    for (String line : MapPlayerTask.getDebugStats()) {
                        leftText.add(DEBUG_PREFIX + line);
                    }
                } else {
                    leftText.add(StringConcatFactory.makeConcatWithConstants < "makeConcatWithConstants", "\u0001" > (Constants.getString("jm.common.enable_mapping_false_text")));
                }
                if (this.mc.options.renderDebugCharts) {
                    if (System.currentTimeMillis() - this.statTimerCheck > 3000L) {
                        this.statTimerReport = StatTimer.getReportByTotalTime(DEBUG_PREFIX, "");
                        this.statTimerCheck = System.currentTimeMillis();
                    }
                    leftText.add(null);
                    leftText.addAll(this.statTimerReport);
                }
            }
        } catch (Throwable var6) {
            JMLogger.throwLogOnce("Unexpected error during onRenderOverlayEarly: " + var6, var6);
        }
    }

    public void onRenderOverlay(GuiGraphics graphics) {
        try {
            UIManager.INSTANCE.drawMiniMap(graphics);
        } catch (Throwable var3) {
            JMLogger.throwLogOnce("Unexpected error during onRenderOverlayEarly: " + var3, var3);
        }
    }
}