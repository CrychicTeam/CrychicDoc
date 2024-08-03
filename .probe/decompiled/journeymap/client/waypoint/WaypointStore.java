package journeymap.client.waypoint;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;

@ParametersAreNonnullByDefault
public enum WaypointStore {

    INSTANCE;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Cache<String, Waypoint> cache = CacheBuilder.newBuilder().build();

    private final Cache<Long, Waypoint> groupCache = CacheBuilder.newBuilder().build();

    private final Set<String> dimensions = new HashSet();

    private boolean loaded = false;

    private boolean writeToFile(Waypoint waypoint) {
        if (waypoint.isPersistent()) {
            File waypointFile = null;
            try {
                waypointFile = new File(FileHandler.getWaypointDir(), waypoint.getFileName());
                Files.write(this.gson.toJson(waypoint), waypointFile, Charset.forName("UTF-8"));
                return true;
            } catch (Exception var4) {
                Journeymap.getLogger().error(String.format("Can't save waypoint file %s: %s", waypointFile, LogFormatter.toString(var4)));
                return false;
            }
        } else {
            return false;
        }
    }

    public Collection<Waypoint> getAll() {
        return this.cache.asMap().values();
    }

    public Collection<Waypoint> getAll(WaypointGroup group) {
        return Maps.filterEntries(this.cache.asMap(), input -> input != null && Objects.equals(group, ((Waypoint) input.getValue()).getGroup())).values();
    }

    public void add(Waypoint waypoint) {
        if (this.cache.getIfPresent(waypoint.getId()) == null) {
            this.cache.put(waypoint.getId(), waypoint);
        }
    }

    public void save(Waypoint waypoint, boolean isNew) {
        this.cache.put(waypoint.getId(), waypoint);
        boolean saved = this.writeToFile(waypoint);
        if (saved) {
            if (isNew) {
                WaypointEventManager.createWaypointEvent(waypoint);
            } else {
                WaypointEventManager.updateWaypointEvent(waypoint);
            }
            waypoint.setDirty(false);
        }
    }

    public void bulkSave() {
        for (Waypoint waypoint : this.cache.asMap().values()) {
            if (waypoint.isDirty()) {
                boolean saved = this.writeToFile(waypoint);
                if (saved) {
                    WaypointEventManager.updateWaypointEvent(waypoint);
                    waypoint.setDirty(false);
                }
            }
        }
    }

    public Waypoint get(String id) {
        return (Waypoint) this.cache.asMap().get(id);
    }

    public void remove(Waypoint waypoint, boolean fireEvent) {
        this.cache.invalidate(waypoint.getId());
        if (waypoint.isPersistent()) {
            File waypointFile = new File(FileHandler.getWaypointDir(), waypoint.getFileName());
            if (waypointFile.exists()) {
                if (fireEvent) {
                    WaypointEventManager.deleteWaypointEvent(waypoint);
                }
                this.remove(waypointFile);
            }
        }
    }

    private void remove(File waypointFile) {
        try {
            waypointFile.delete();
        } catch (Exception var3) {
            Journeymap.getLogger().warn(String.format("Can't delete waypoint file %s: %s", waypointFile, var3.getMessage()));
            waypointFile.deleteOnExit();
        }
    }

    public void reset() {
        this.cache.invalidateAll();
        this.dimensions.clear();
        this.loaded = false;
        if (JourneymapClient.getInstance().getWaypointProperties().managerEnabled.get()) {
            this.load();
        }
    }

    private void load() {
        synchronized (this.cache) {
            ArrayList<Waypoint> waypoints = new ArrayList();
            File waypointDir = null;
            try {
                this.cache.invalidateAll();
                waypointDir = FileHandler.getWaypointDir();
                waypoints.addAll(new JmReader().loadWaypoints(waypointDir));
                this.load(waypoints, false);
                Journeymap.getLogger().info(String.format("Loaded %s waypoints from %s", this.cache.size(), waypointDir));
            } catch (Exception var6) {
                Journeymap.getLogger().error(String.format("Error loading waypoints from %s: %s", waypointDir, LogFormatter.toString(var6)));
            }
        }
    }

    public void load(Collection<Waypoint> waypoints, boolean forceSave) {
        for (Waypoint waypoint : waypoints) {
            waypoint.validateName();
            if (!waypoint.isPersistent() || !forceSave && !waypoint.isDirty()) {
                this.cache.put(waypoint.getId(), waypoint);
            } else {
                this.save(waypoint, false);
            }
            WaypointEventManager.readWaypointEvent(waypoint);
            this.dimensions.addAll(waypoint.getDimensions());
        }
        this.loaded = true;
    }

    public boolean hasLoaded() {
        return this.loaded;
    }

    public List<String> getLoadedDimensions() {
        return new ArrayList(this.dimensions);
    }
}