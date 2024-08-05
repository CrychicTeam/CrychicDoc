package net.blay09.mods.waystones.compat;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import journeymap.client.api.ClientPlugin;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.display.WaypointGroup;
import journeymap.client.api.event.ClientEvent;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.KnownWaystonesEvent;
import net.blay09.mods.waystones.api.WaystoneUpdateReceivedEvent;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.config.WaystonesConfigData;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.Nullable;

@ClientPlugin
public class JourneyMapIntegration implements IClientPlugin {

    private static final UUID WAYSTONE_GROUP_ID = UUID.fromString("005bdf11-2dbb-4a27-8aa4-0184e86fa33c");

    private IClientAPI api;

    private boolean journeyMapReady;

    private final List<Runnable> scheduledJobsWhenReady = new ArrayList();

    private static JourneyMapIntegration instance;

    public JourneyMapIntegration() {
        instance = this;
        Balm.getEvents().onEvent(KnownWaystonesEvent.class, this::onKnownWaystones);
        Balm.getEvents().onEvent(WaystoneUpdateReceivedEvent.class, this::onWaystoneUpdateReceived);
    }

    @Override
    public void initialize(IClientAPI iClientAPI) {
        this.api = iClientAPI;
        this.api.subscribe("waystones", EnumSet.of(ClientEvent.Type.MAPPING_STARTED));
    }

    @Nullable
    public static JourneyMapIntegration getInstance() {
        return instance;
    }

    @Override
    public String getModId() {
        return "waystones";
    }

    @Override
    public void onEvent(ClientEvent clientEvent) {
        if (clientEvent.type == ClientEvent.Type.MAPPING_STARTED) {
            this.journeyMapReady = true;
            for (Runnable scheduledJob : this.scheduledJobsWhenReady) {
                scheduledJob.run();
            }
            this.scheduledJobsWhenReady.clear();
        }
    }

    public void onKnownWaystones(KnownWaystonesEvent event) {
        if (shouldManageWaypoints()) {
            this.runWhenJourneyMapIsReady(() -> this.updateAllWaypoints(event.getWaystones()));
        }
    }

    private static boolean shouldManageWaypoints() {
        WaystonesConfigData config = WaystonesConfig.getActive();
        return config.compatibility.preferJourneyMapIntegration && Balm.isModLoaded("jmi") ? false : config.compatibility.displayWaystonesOnJourneyMap;
    }

    public void onWaystoneUpdateReceived(WaystoneUpdateReceivedEvent event) {
        if (shouldManageWaypoints()) {
            this.runWhenJourneyMapIsReady(() -> this.updateWaypoint(event.getWaystone()));
        }
    }

    private void runWhenJourneyMapIsReady(Runnable runnable) {
        if (this.journeyMapReady) {
            runnable.run();
        } else {
            this.scheduledJobsWhenReady.add(runnable);
        }
    }

    private void updateAllWaypoints(List<IWaystone> waystones) {
        Set<String> stillExistingIds = new HashSet();
        for (IWaystone waystone : waystones) {
            stillExistingIds.add(waystone.getWaystoneUid().toString());
            this.updateWaypoint(waystone);
        }
        for (Waypoint waypoint : this.api.getWaypoints("waystones")) {
            if (!stillExistingIds.contains(waypoint.getId())) {
                this.api.remove(waypoint);
            }
        }
    }

    private void updateWaypoint(IWaystone waystone) {
        try {
            String waystoneName = waystone.hasName() ? waystone.getName() : I18n.get("waystones.map.untitled_waystone");
            Waypoint oldWaypoint = this.api.getWaypoint("waystones", waystone.getWaystoneUid().toString());
            Waypoint waypoint = new Waypoint("waystones", waystone.getWaystoneUid().toString(), waystoneName, waystone.getDimension(), waystone.getPos());
            waypoint.setName(waystoneName);
            waypoint.setGroup(new WaypointGroup("waystones", WAYSTONE_GROUP_ID.toString(), "Waystones"));
            if (oldWaypoint != null) {
                waypoint.setEnabled(oldWaypoint.isEnabled());
                if (oldWaypoint.hasColor()) {
                    waypoint.setColor(oldWaypoint.getColor());
                }
                if (oldWaypoint.hasBackgroundColor()) {
                    waypoint.setBackgroundColor(oldWaypoint.getBackgroundColor());
                }
                this.api.remove(oldWaypoint);
            }
            this.api.show(waypoint);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }
}