package journeymap.client.ui;

import journeymap.client.JourneymapClient;
import journeymap.client.data.WaypointsData;
import journeymap.client.log.ChatLog;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.dialog.AboutDialog;
import journeymap.client.ui.dialog.AddonOptionsManager;
import journeymap.client.ui.dialog.GridEditor;
import journeymap.client.ui.dialog.MinimapOptions;
import journeymap.client.ui.dialog.MultiplayerOptionsManager;
import journeymap.client.ui.dialog.OptionsManager;
import journeymap.client.ui.dialog.ServerOptionsManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.client.ui.waypoint.WaypointEditor;
import journeymap.client.ui.waypoint.WaypointManager;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public enum UIManager {

    INSTANCE;

    private final Logger logger = Journeymap.getLogger();

    private final MiniMap miniMap;

    Minecraft minecraft = Minecraft.getInstance();

    private UIManager() {
        MiniMap tmp;
        try {
            int preset = JourneymapClient.getInstance().getMiniMapProperties1().isActive() ? 1 : 2;
            tmp = new MiniMap(JourneymapClient.getInstance().getMiniMapProperties(preset));
        } catch (Throwable var5) {
            this.logger.error("Unexpected error: " + LogFormatter.toString(var5));
            if (var5 instanceof LinkageError) {
                ChatLog.announceError(var5.getMessage() + " : JourneyMap is not compatible with this build of Forge!");
            }
            tmp = new MiniMap(new MiniMapProperties(1));
        }
        this.miniMap = tmp;
    }

    public static void handleLinkageError(LinkageError error) {
        Journeymap.getLogger().error(LogFormatter.toString(error));
        try {
            ChatLog.announceError(error.getMessage() + " : JourneyMap is not compatible with this build of Forge!");
        } catch (Throwable var2) {
            var2.printStackTrace();
        }
    }

    public void closeAll() {
        try {
            this.closeCurrent();
        } catch (LinkageError var2) {
            handleLinkageError(var2);
        } catch (Throwable var3) {
            this.logger.error("Unexpected error: " + LogFormatter.toString(var3));
        }
        this.minecraft.setScreen(null);
        this.minecraft.setWindowActive(true);
    }

    public void closeCurrent() {
        try {
            if (this.minecraft.screen != null && this.minecraft.screen instanceof JmUI) {
                this.logger.debug("Closing " + this.minecraft.screen.getClass());
                ((JmUI) this.minecraft.screen).close();
            }
        } catch (LinkageError var2) {
            handleLinkageError(var2);
        } catch (Throwable var3) {
            this.logger.error("Unexpected error: " + LogFormatter.toString(var3));
        }
    }

    public void openInventory() {
        this.logger.debug("Opening inventory");
        this.closeAll();
        this.minecraft.setScreen(new InventoryScreen(this.minecraft.player));
    }

    public <T extends JmUI> T open(Class<T> uiClass, JmUI returnDisplay) {
        try {
            return this.open((T) uiClass.getConstructor(JmUI.class).newInstance(returnDisplay));
        } catch (LinkageError var6) {
            handleLinkageError(var6);
            return null;
        } catch (Throwable var7) {
            try {
                return this.open((T) uiClass.getConstructor().newInstance());
            } catch (Throwable var5) {
                this.logger.log(Level.ERROR, "1st unexpected exception creating UI: " + LogFormatter.toString(var7));
                this.logger.log(Level.ERROR, "2nd unexpected exception creating UI: " + LogFormatter.toString(var5));
                this.closeCurrent();
                return null;
            }
        }
    }

    public <T extends JmUI> T open(Class<T> uiClass) {
        try {
            if (MiniMap.uiState().active) {
                MiniMap.updateUIState(false);
            }
            T ui = (T) uiClass.newInstance();
            return this.open(ui);
        } catch (LinkageError var3) {
            handleLinkageError(var3);
            return null;
        } catch (Throwable var4) {
            this.logger.log(Level.ERROR, "Unexpected exception creating UI: " + LogFormatter.toString(var4));
            this.closeCurrent();
            return null;
        }
    }

    public <T extends Screen> T open(T ui) {
        this.closeCurrent();
        this.logger.debug("Opening UI " + ui.getClass().getSimpleName());
        try {
            if (ui instanceof Fullscreen) {
                ((Fullscreen) ui).reset();
            }
            if (ui instanceof WaypointManager) {
                ui = (T) (new WaypointManager((JmUI) ((WaypointManager) ui).getReturnDisplay()));
            }
            this.minecraft.setScreen(ui);
            KeyMapping.releaseAll();
        } catch (LinkageError var3) {
            handleLinkageError(var3);
            return null;
        } catch (Throwable var4) {
            this.logger.error(String.format("Unexpected exception opening UI %s: %s", ui.getClass(), LogFormatter.toString(var4)));
        }
        return ui;
    }

    public void toggleMinimap() {
        try {
            this.setMiniMapEnabled(!this.isMiniMapEnabled());
        } catch (LinkageError var2) {
            handleLinkageError(var2);
        } catch (Throwable var3) {
            this.logger.error(String.format("Unexpected exception in toggleMinimap: %s", LogFormatter.toString(var3)));
        }
    }

    public boolean isMiniMapEnabled() {
        try {
            return this.miniMap.getCurrentMinimapProperties().enabled.get();
        } catch (LinkageError var2) {
            handleLinkageError(var2);
        } catch (Throwable var3) {
            this.logger.error(String.format("Unexpected exception in isMiniMapEnabled: %s", LogFormatter.toString(var3)));
        }
        return false;
    }

    public void setMiniMapEnabled(boolean enable) {
        try {
            this.miniMap.getCurrentMinimapProperties().enabled.set(enable);
            this.miniMap.getCurrentMinimapProperties().save();
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            this.logger.error(String.format("Unexpected exception in setMiniMapEnabled: %s", LogFormatter.toString(var4)));
        }
    }

    public void drawMiniMap(GuiGraphics graphics) {
        this.minecraft.getProfiler().push("journeymap");
        try {
            boolean doDraw = false;
            if (!this.miniMap.getCurrentMinimapProperties().enabled.get()) {
                if (!this.miniMap.getCurrentMinimapProperties().enabled.get() && MiniMap.uiState().active) {
                    MiniMap.updateUIState(false);
                }
            } else {
                Screen currentScreen = this.minecraft.screen;
                doDraw = currentScreen == null || currentScreen instanceof ChatScreen;
                if (doDraw) {
                    if (!MiniMap.uiState().active) {
                        if (MiniMap.state().getLastMapTypeChange() == 0L) {
                            this.miniMap.reset();
                        } else {
                            MiniMap.state().requireRefresh();
                        }
                    }
                    this.miniMap.drawMap(graphics);
                }
            }
            if (doDraw && !MiniMap.uiState().active) {
                MiniMap.updateUIState(true);
            }
        } catch (LinkageError var8) {
            handleLinkageError(var8);
        } catch (Throwable var9) {
            Journeymap.getLogger().error("Error drawing minimap: " + LogFormatter.toString(var9));
        } finally {
            this.minecraft.getProfiler().pop();
        }
    }

    public MiniMap getMiniMap() {
        return this.miniMap;
    }

    public Fullscreen openFullscreenMap() {
        if (this.minecraft.screen instanceof Fullscreen) {
            return (Fullscreen) this.minecraft.screen;
        } else if (!(this.minecraft.screen instanceof JmUI) && this.minecraft.screen != null) {
            return null;
        } else {
            KeyMapping.releaseAll();
            return this.open(Fullscreen.class);
        }
    }

    public void openFullscreenMap(Waypoint waypoint) {
        try {
            if (waypoint.isInPlayerDimension()) {
                Fullscreen map = this.open(Fullscreen.class);
                map.centerOn(waypoint);
            }
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error opening map on waypoint: " + LogFormatter.toString(var4));
        }
    }

    public void openOptionsManager() {
        this.open(OptionsManager.class);
    }

    public void openOptionsManager(JmUI returnDisplay, Category... initialCategories) {
        try {
            this.open(new OptionsManager(returnDisplay, initialCategories));
        } catch (LinkageError var4) {
            handleLinkageError(var4);
        } catch (Throwable var5) {
            this.logger.log(Level.ERROR, "Unexpected exception creating MasterOptions with return class: " + LogFormatter.toString(var5));
        }
    }

    public void openSplash(JmUI returnDisplay) {
        this.open(AboutDialog.class, returnDisplay);
    }

    public void openWaypointManager(Waypoint waypoint, JmUI returnDisplay) {
        if (WaypointsData.isManagerEnabled()) {
            try {
                WaypointManager manager = new WaypointManager(waypoint, returnDisplay);
                this.open(manager);
            } catch (LinkageError var4) {
                handleLinkageError(var4);
            } catch (Throwable var5) {
                Journeymap.getLogger().error("Error opening waypoint manager: " + LogFormatter.toString(var5));
            }
        }
    }

    public void openWaypointEditor(Waypoint waypoint, boolean isNew, JmUI returnDisplay, boolean openedWithHotkey) {
        if (WaypointsData.isManagerEnabled()) {
            try {
                WaypointEditor editor = new WaypointEditor(waypoint, isNew, returnDisplay, openedWithHotkey);
                this.open(editor);
            } catch (LinkageError var6) {
                handleLinkageError(var6);
            } catch (Throwable var7) {
                Journeymap.getLogger().error("Error opening waypoint editor: " + LogFormatter.toString(var7));
            }
        }
    }

    public void openWaypointEditor(Waypoint waypoint, boolean isNew, JmUI returnDisplay) {
        this.openWaypointEditor(waypoint, isNew, returnDisplay, false);
    }

    public void openGridEditor(JmUI returnDisplay) {
        try {
            GridEditor editor = new GridEditor(returnDisplay);
            this.open(editor);
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error opening grid editor: " + LogFormatter.toString(var4));
        }
    }

    public void openMinimapPosition(JmUI returnDisplay, MiniMapProperties properties) {
        try {
            MinimapOptions editor = new MinimapOptions(returnDisplay, properties);
            this.open(editor);
        } catch (LinkageError var4) {
            handleLinkageError(var4);
        } catch (Throwable var5) {
            Journeymap.getLogger().error("Error opening grid editor: " + LogFormatter.toString(var5));
        }
    }

    public void openServerEditor(JmUI returnDisplay) {
        try {
            JmUI editor = new ServerOptionsManager(returnDisplay);
            this.open(editor);
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error opening server manager: " + LogFormatter.toString(var4));
        }
    }

    public void openMultiplayerEditor(JmUI returnDisplay) {
        try {
            JmUI editor = new MultiplayerOptionsManager(returnDisplay);
            this.open(editor);
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error opening multiplayer options manager: " + LogFormatter.toString(var4));
        }
    }

    public void openAddonOptionsEditor(JmUI returnDisplay) {
        try {
            JmUI editor = new AddonOptionsManager(returnDisplay);
            this.open(editor);
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error opening Addon options manager: " + LogFormatter.toString(var4));
        }
    }

    public ServerOptionsManager getServerEditor() {
        return this.minecraft.screen instanceof ServerOptionsManager ? (ServerOptionsManager) this.minecraft.screen : null;
    }

    public MultiplayerOptionsManager getMultiplayerOptions() {
        return this.minecraft.screen instanceof MultiplayerOptionsManager ? (MultiplayerOptionsManager) this.minecraft.screen : null;
    }

    public void reset() {
        try {
            Fullscreen.state().requireRefresh();
            this.miniMap.reset();
        } catch (LinkageError var2) {
            handleLinkageError(var2);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("Error during reset: " + LogFormatter.toString(var3));
        }
    }

    public void switchMiniMapPreset() {
        try {
            int currentPreset = this.miniMap.getCurrentMinimapProperties().getId();
            this.switchMiniMapPreset(currentPreset == 1 ? 2 : 1);
        } catch (LinkageError var2) {
            handleLinkageError(var2);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("Error during switchMiniMapPreset: " + LogFormatter.toString(var3));
        }
    }

    public void switchMiniMapPreset(int which) {
        try {
            this.miniMap.setMiniMapProperties(JourneymapClient.getInstance().getMiniMapProperties(which));
            MiniMap.state().requireRefresh();
        } catch (LinkageError var3) {
            handleLinkageError(var3);
        } catch (Throwable var4) {
            Journeymap.getLogger().error("Error during switchMiniMapPreset: " + LogFormatter.toString(var4));
        }
    }
}