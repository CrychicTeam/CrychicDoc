package journeymap.client;

import journeymap.client.feature.FeatureManager;
import journeymap.client.task.multi.RenderSpec;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.network.data.model.ClientState;
import journeymap.common.properties.GlobalProperties;
import net.minecraft.client.Minecraft;

public class InternalStateHandler {

    private boolean journeyMapServerConnection = false;

    private boolean moddedServerConnection = false;

    private boolean expandedRadarEnabled = false;

    private boolean teleportEnabled = false;

    private boolean serverAdmin = false;

    private boolean useServerFullscreenBiomes = false;

    private boolean allowDeathPoints = true;

    private boolean showInGameBeacons = true;

    private boolean waypointsAllowed = true;

    private boolean multiplayerOptionsAllowed = true;

    private boolean readOnlyServerAdmin = true;

    private int maxRenderDistance = 0;

    public void setStates(ClientState state) {
        GlobalProperties prop = new GlobalProperties().loadForClient(state.getPayload(), false);
        if (!prop.journeymapEnabled.get()) {
            JourneymapClient.getInstance().disable();
            Journeymap.getLogger().info("Journeymap is Disabled by the server.");
        } else if (!JourneymapClient.getInstance().isInitialized()) {
            Journeymap.getLogger().info("Journeymap is enabled by the server.");
            JourneymapClient.getInstance().enable();
        }
        JourneymapClient.getInstance().getStateHandler().setModdedServerConnection(state.hasServerMod());
        JourneymapClient.getInstance().getStateHandler().setJourneyMapServerConnection(state.hasServerMod());
        JourneymapClient.getInstance().getStateHandler().setTeleportEnabled(prop.teleportEnabled.get());
        JourneymapClient.getInstance().getStateHandler().setExpandedRadarEnabled(prop.worldPlayerRadar.get().enabled());
        JourneymapClient.getInstance().getStateHandler().setServerAdmin(state.isServerAdmin());
        JourneymapClient.getInstance().getStateHandler().setAllowDeathPoints(prop.allowDeathPoints.get());
        JourneymapClient.getInstance().getStateHandler().setShowInGameBeacons(prop.showInGameBeacons.get());
        JourneymapClient.getInstance().getStateHandler().setWaypointsAllowed(prop.allowWaypoints.get());
        JourneymapClient.getInstance().getStateHandler().setReadOnlyServerAdmin(prop.viewOnlyServerProperties.get());
        JourneymapClient.getInstance().getStateHandler().setMaxRenderDistance(prop.renderRange.get());
        JourneymapClient.getInstance().getStateHandler().setMultiplayerOptionsAllowed(prop.allowMultiplayerSettings.get().enabled());
        FeatureManager.getInstance().updateDimensionFeatures(prop);
    }

    public void reset() {
        this.journeyMapServerConnection = false;
        this.moddedServerConnection = false;
        this.expandedRadarEnabled = false;
        this.teleportEnabled = false;
        this.serverAdmin = false;
        this.useServerFullscreenBiomes = false;
        this.allowDeathPoints = true;
        this.showInGameBeacons = true;
        this.waypointsAllowed = true;
        this.readOnlyServerAdmin = false;
        this.multiplayerOptionsAllowed = true;
        this.maxRenderDistance = 0;
        FeatureManager.getInstance().reset();
    }

    public boolean isAllowDeathPoints() {
        return this.allowDeathPoints;
    }

    private void setAllowDeathPoints(boolean allowDeathPoints) {
        this.allowDeathPoints = allowDeathPoints;
        if (!allowDeathPoints) {
            Journeymap.getLogger().debug("Death Points disabled by the server, deleting existing.");
            WaypointStore.INSTANCE.getAll().removeIf(waypoint -> Waypoint.Type.Death.equals(waypoint.getType()));
        }
    }

    public boolean isModdedServerConnection() {
        return this.moddedServerConnection;
    }

    public void setModdedServerConnection(boolean moddedServerConnection) {
        this.moddedServerConnection = moddedServerConnection;
    }

    public boolean isJourneyMapServerConnection() {
        return this.journeyMapServerConnection;
    }

    public void setJourneyMapServerConnection(boolean journeyMapServerConnection) {
        Journeymap.getLogger().debug("Connection initiated with Journeymap Server: " + journeyMapServerConnection);
        this.journeyMapServerConnection = journeyMapServerConnection;
    }

    public boolean isExpandedRadarEnabled() {
        return this.expandedRadarEnabled;
    }

    private void setExpandedRadarEnabled(boolean expandedRadarEnabled) {
        if (Minecraft.getInstance().hasSingleplayerServer() && Minecraft.getInstance().getCurrentServer() != null && Minecraft.getInstance().getCurrentServer().isLan()) {
            this.expandedRadarEnabled = false;
        } else {
            Journeymap.getLogger().debug("Expanded Radar Enabled:" + expandedRadarEnabled);
            this.expandedRadarEnabled = expandedRadarEnabled;
        }
    }

    public boolean isTeleportEnabled() {
        return this.teleportEnabled;
    }

    public void setTeleportEnabled(boolean teleportEnabled) {
        Journeymap.getLogger().debug("Teleport Enabled:" + teleportEnabled);
        this.teleportEnabled = teleportEnabled;
    }

    public boolean isServerAdmin() {
        return this.serverAdmin;
    }

    public boolean canServerAdmin() {
        return this.serverAdmin || Minecraft.getInstance().hasSingleplayerServer();
    }

    private void setServerAdmin(boolean serverAdmin) {
        Journeymap.getLogger().debug("Server Admin Enabled:" + serverAdmin);
        this.serverAdmin = serverAdmin;
    }

    public boolean useServerFullscreenBiomes() {
        return this.useServerFullscreenBiomes;
    }

    private void setUseServerFullscreenBiomes(boolean useServerFullscreenBiomes) {
        Journeymap.getLogger().debug("Server fullscreen biomes:" + useServerFullscreenBiomes);
        this.useServerFullscreenBiomes = useServerFullscreenBiomes;
    }

    public boolean canShowInGameBeacons() {
        return this.showInGameBeacons;
    }

    public void setShowInGameBeacons(boolean showInGameBeacons) {
        Journeymap.getLogger().debug("Server set show in-game beacons:" + showInGameBeacons);
        this.showInGameBeacons = showInGameBeacons;
    }

    public boolean isWaypointsAllowed() {
        return this.waypointsAllowed;
    }

    public void setWaypointsAllowed(boolean waypointsAllowed) {
        Journeymap.getLogger().debug("Server set waypoints allowed:" + waypointsAllowed);
        this.waypointsAllowed = waypointsAllowed;
    }

    public boolean isReadOnlyServerAdmin() {
        return this.readOnlyServerAdmin;
    }

    public void setReadOnlyServerAdmin(boolean readOnlyServerAdmin) {
        Journeymap.getLogger().debug("Server set Server Admin read only mode:" + readOnlyServerAdmin);
        this.readOnlyServerAdmin = readOnlyServerAdmin;
    }

    public int getMaxRenderDistance() {
        return this.maxRenderDistance;
    }

    public void setMaxRenderDistance(int maxRenderDistance) {
        Journeymap.getLogger().debug("Server set maximum map render range to:" + maxRenderDistance);
        this.maxRenderDistance = maxRenderDistance;
        RenderSpec.resetRenderSpecs();
    }

    public boolean isMultiplayerOptionsAllowed() {
        return this.multiplayerOptionsAllowed;
    }

    public void setMultiplayerOptionsAllowed(boolean multiplayerOptionsAllowed) {
        Journeymap.getLogger().debug("Server set allow mutliplayer options:" + multiplayerOptionsAllowed);
        this.multiplayerOptionsAllowed = multiplayerOptionsAllowed;
    }
}