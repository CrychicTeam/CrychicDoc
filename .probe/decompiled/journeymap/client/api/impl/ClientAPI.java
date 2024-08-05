package journeymap.client.api.impl;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.JourneymapClient;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.display.Displayable;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.util.PluginHelper;
import journeymap.client.api.util.UIState;
import journeymap.client.feature.FeatureManager;
import journeymap.client.io.FileHandler;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.OverlayDrawStep;
import journeymap.client.task.multi.ApiImageTask;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

@ParametersAreNonnullByDefault
public enum ClientAPI implements IClientAPI {

    INSTANCE;

    private final Logger LOGGER = Journeymap.getLogger();

    private final List<OverlayDrawStep> lastDrawSteps = new ArrayList();

    private HashMap<String, PluginWrapper> plugins = new HashMap();

    private ClientEventManager clientEventManager = new ClientEventManager(this.plugins.values());

    private boolean drawStepsUpdateNeeded = true;

    private Context.UI lastUi = Context.UI.Any;

    private Context.MapType lastMapType = Context.MapType.Any;

    private ResourceKey<Level> lastDimension = Level.OVERWORLD;

    private File addonDataPath;

    private UIState lastUiState;

    private ClientAPI() {
        this.log("built with JourneyMap API 1.9-SNAPSHOT");
    }

    @Override
    public UIState getUIState(Context.UI ui) {
        switch(ui) {
            case Minimap:
                return MiniMap.uiState();
            case Fullscreen:
                return Fullscreen.uiState();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public File getDataPath(String modId) {
        return this.addonDataPath != null && Minecraft.getInstance().level != null ? new File(this.addonDataPath, modId) : null;
    }

    public void refreshDataPathCache(boolean flush) {
        this.addonDataPath = flush ? null : FileHandler.getAddonDataPath(Minecraft.getInstance());
    }

    @Override
    public List<Waypoint> getAllWaypoints() {
        return (List<Waypoint>) WaypointStore.INSTANCE.getAll().stream().map(journeymap.client.waypoint.Waypoint::toModWaypoint).collect(Collectors.toList());
    }

    @Override
    public List<Waypoint> getAllWaypoints(ResourceKey<Level> dim) {
        return (List<Waypoint>) WaypointStore.INSTANCE.getAll().stream().filter(wp -> wp.getDimensions().contains(dim.location().toString())).map(journeymap.client.waypoint.Waypoint::toModWaypoint).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Waypoint getWaypoint(String modId, String displayId) {
        return this.getPlugin(modId).getWaypoint(displayId);
    }

    @Override
    public List<Waypoint> getWaypoints(String modId) {
        return this.getPlugin(modId).getWaypoints();
    }

    @Override
    public void setWorldId(String identifier) {
        JourneymapClient.getInstance().setCurrentWorldId(identifier);
    }

    @Override
    public String getWorldId() {
        return JourneymapClient.getInstance().getCurrentWorldId();
    }

    @Override
    public void subscribe(String modId, EnumSet<ClientEvent.Type> enumSet) {
        try {
            this.getPlugin(modId).subscribe(enumSet);
            this.clientEventManager.updateSubscribedTypes();
        } catch (Throwable var4) {
            this.logError("Error subscribing: " + var4, var4);
        }
    }

    @Override
    public void show(Displayable displayable) {
        try {
            if (this.playerAccepts(displayable)) {
                this.getPlugin(displayable.getModId()).show(displayable);
                this.drawStepsUpdateNeeded = true;
            }
        } catch (Throwable var3) {
            this.logError("Error showing displayable: " + displayable, var3);
        }
    }

    @Override
    public void remove(Displayable displayable) {
        try {
            if (this.playerAccepts(displayable)) {
                this.getPlugin(displayable.getModId()).remove(displayable);
                this.drawStepsUpdateNeeded = true;
            }
        } catch (Throwable var3) {
            this.logError("Error removing displayable: " + displayable, var3);
        }
    }

    @Override
    public void removeAll(String modId, DisplayType displayType) {
        try {
            if (this.playerAccepts(modId, displayType)) {
                this.getPlugin(modId).removeAll(displayType);
                this.drawStepsUpdateNeeded = true;
            }
        } catch (Throwable var4) {
            this.logError("Error removing all displayables: " + displayType, var4);
        }
    }

    @Override
    public void removeAll(String modId) {
        try {
            for (DisplayType displayType : DisplayType.values()) {
                this.removeAll(modId, displayType);
                this.drawStepsUpdateNeeded = true;
            }
            this.getPlugin(modId).removeAll();
        } catch (Throwable var6) {
            this.logError("Error removing all displayables for mod: " + modId, var6);
        }
    }

    public void purge() {
        try {
            this.drawStepsUpdateNeeded = true;
            this.lastDrawSteps.clear();
            this.plugins.clear();
            this.clientEventManager.purge();
        } catch (Throwable var2) {
            this.logError("Error purging: " + var2, var2);
        }
    }

    @Override
    public boolean exists(Displayable displayable) {
        try {
            if (this.playerAccepts(displayable)) {
                return this.getPlugin(displayable.getModId()).exists(displayable);
            }
        } catch (Throwable var3) {
            this.logError("Error checking exists: " + displayable, var3);
        }
        return false;
    }

    @Override
    public boolean playerAccepts(String modId, DisplayType displayType) {
        return true;
    }

    @Override
    public void requestMapTile(String modId, ResourceKey<Level> dimension, Context.MapType apiMapType, ChunkPos startChunk, ChunkPos endChunk, @Nullable Integer chunkY, int zoom, boolean showGrid, Consumer<NativeImage> callback) {
        this.log("requestMapTile");
        boolean honorRequest = true;
        File worldDir = FileHandler.getJMWorldDir(Minecraft.getInstance());
        boolean validModId = Objects.equals("jmitems", modId) || Objects.equals("skymap", modId);
        if (!validModId) {
            honorRequest = false;
            this.logError("requestMapTile not supported");
        } else if (worldDir == null || !worldDir.exists() || !worldDir.isDirectory()) {
            honorRequest = false;
            this.logError("world directory not found: " + worldDir);
        }
        try {
            if (honorRequest) {
                JourneymapClient.getInstance().queueOneOff(new ApiImageTask(modId, dimension, apiMapType, startChunk, endChunk, chunkY, zoom, showGrid, callback));
            } else {
                Minecraft.getInstance().m_18707_(() -> callback.accept(null));
            }
        } catch (Exception var14) {
            callback.accept(null);
        }
    }

    private boolean playerAccepts(Displayable displayable) {
        return this.playerAccepts(displayable.getModId(), displayable.getDisplayType());
    }

    public ClientEventManager getClientEventManager() {
        return this.clientEventManager;
    }

    public void getDrawSteps(List<? super OverlayDrawStep> list, UIState uiState) {
        if (this.drawStepsUpdateNeeded) {
            this.lastDrawSteps.clear();
            for (PluginWrapper pluginWrapper : this.plugins.values()) {
                pluginWrapper.getDrawSteps(this.lastDrawSteps, uiState);
            }
            this.lastDrawSteps.sort(Comparator.comparingInt(DrawStep::getDisplayOrder));
            this.drawStepsUpdateNeeded = false;
        }
        if (uiState.ui != this.lastUi || !uiState.dimension.equals(this.lastDimension) || uiState.mapType != this.lastMapType) {
            this.drawStepsUpdateNeeded = true;
            this.lastUi = uiState.ui;
            this.lastUiState = uiState;
            this.lastDimension = uiState.dimension;
            this.lastMapType = uiState.mapType;
        }
        list.addAll(this.lastDrawSteps);
    }

    @Override
    public void toggleDisplay(@Nullable ResourceKey<Level> dimension, Context.MapType mapType, Context.UI mapUI, boolean enable) {
        if (!enable) {
            FeatureManager.getInstance().disableFeature(mapType, mapUI, dimension);
        } else {
            throw new UnsupportedOperationException("Enabling is currently unsupported.");
        }
    }

    @Override
    public void toggleWaypoints(@Nullable ResourceKey<Level> dimension, Context.MapType mapType, Context.UI mapUI, boolean enable) {
        this.log(String.format("Toggled waypoints in %s:%s:%s:%s", dimension, mapType, mapUI, enable));
    }

    @Override
    public boolean isDisplayEnabled(@Nullable ResourceKey<Level> dimension, Context.MapType mapType, Context.UI mapUI) {
        return false;
    }

    @Override
    public boolean isWaypointsEnabled(@Nullable ResourceKey<Level> dimension, Context.MapType mapType, Context.UI mapUI) {
        return false;
    }

    private PluginWrapper getPlugin(String modId) {
        if (Strings.isEmpty(modId)) {
            throw new IllegalArgumentException("Invalid modId: " + modId);
        } else {
            PluginWrapper pluginWrapper = (PluginWrapper) this.plugins.get(modId);
            if (pluginWrapper == null) {
                IClientPlugin plugin = (IClientPlugin) PluginHelper.INSTANCE.getPlugins().get(modId);
                if (plugin == null) {
                    if (!modId.equals("journeymap")) {
                        throw new IllegalArgumentException("No plugin found for modId: " + modId);
                    }
                    plugin = new IClientPlugin() {

                        @Override
                        public void initialize(IClientAPI jmClientApi) {
                        }

                        @Override
                        public String getModId() {
                            return "journeymap";
                        }

                        @Override
                        public void onEvent(ClientEvent event) {
                        }
                    };
                }
                pluginWrapper = new PluginWrapper(plugin);
                this.plugins.put(modId, pluginWrapper);
            }
            return pluginWrapper;
        }
    }

    public boolean isDrawStepsUpdateNeeded() {
        return this.drawStepsUpdateNeeded;
    }

    void log(String message) {
        this.LOGGER.info(String.format("[%s] %s", this.getClass().getSimpleName(), message));
    }

    private void logError(String message) {
        this.LOGGER.error(String.format("[%s] %s", this.getClass().getSimpleName(), message));
    }

    void logError(String message, Throwable t) {
        this.LOGGER.error(String.format("[%s] %s", this.getClass().getSimpleName(), message), t);
    }

    public void flagOverlaysForRerender() {
        for (OverlayDrawStep overlayDrawStep : this.lastDrawSteps) {
            overlayDrawStep.getOverlay().flagForRerender();
        }
    }

    public UIState getLastUIState() {
        return this.lastUiState;
    }
}