package journeymap.client.task.main;

import journeymap.client.JourneymapClient;
import journeymap.client.log.ChatLog;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import org.apache.logging.log4j.Logger;

public class MappingMonitorTask implements IMainThreadTask {

    private static String NAME = "Tick." + MappingMonitorTask.class.getSimpleName();

    Logger logger = Journeymap.getLogger();

    private Holder<DimensionType> lastDimension = null;

    @Override
    public IMainThreadTask perform(Minecraft mc, JourneymapClient jm) {
        try {
            if (!jm.isInitialized()) {
                return this;
            }
            boolean isDead = mc.screen != null && mc.screen instanceof DeathScreen;
            if (mc.level == null) {
                if (jm.isMapping()) {
                    jm.stopMapping();
                }
                Screen guiScreen = mc.screen;
                if ((guiScreen instanceof TitleScreen || guiScreen instanceof SelectWorldScreen || guiScreen instanceof JoinMultiplayerScreen) && jm.getCurrentWorldId() != null) {
                    this.logger.info("World ID has been reset.");
                    jm.setCurrentWorldId(null);
                }
                return this;
            }
            if (this.lastDimension == null || this.lastDimension.unwrapKey().isPresent() && !mc.player.m_9236_().dimensionTypeRegistration().is((ResourceKey<DimensionType>) this.lastDimension.unwrapKey().get())) {
                this.lastDimension = mc.player.m_9236_().dimensionTypeRegistration();
                if (jm.isMapping()) {
                    jm.stopMapping();
                }
            } else if (!jm.isMapping() && !isDead && JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get()) {
                jm.startMapping();
            }
            boolean isGamePaused = mc.screen != null && !(mc.screen instanceof Fullscreen);
            if (isGamePaused && !jm.isMapping()) {
                return this;
            }
            if (!isGamePaused) {
                ChatLog.showChatAnnouncements(mc);
            }
            if (!jm.isMapping() && JourneymapClient.getInstance().getCoreProperties().mappingEnabled.get()) {
                jm.startMapping();
            }
        } catch (Throwable var5) {
            this.logger.error("Error in JourneyMap.performMainThreadTasks(): " + LogFormatter.toString(var5));
        }
        return this;
    }

    @Override
    public String getName() {
        return NAME;
    }
}