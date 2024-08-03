package journeymap.client.waypoint;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.io.Files;
import com.google.gson.internal.LinkedTreeMap;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.io.FileHandler;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;

@ParametersAreNonnullByDefault
public enum WaypointGroupStore {

    INSTANCE;

    public static final String KEY_PATTERN = "%s:%s";

    public static final String FILENAME = "waypoint_groups.json";

    public final LoadingCache<String, WaypointGroup> cache = this.createCache();

    public WaypointGroup get(String name) {
        return this.get("journeymap", name);
    }

    public WaypointGroup get(String origin, String name) {
        this.ensureLoaded();
        return (WaypointGroup) this.cache.getUnchecked(String.format("%s:%s", origin, name));
    }

    public WaypointGroup getFromKey(String key) {
        this.ensureLoaded();
        return (WaypointGroup) this.cache.getUnchecked(key);
    }

    public boolean exists(WaypointGroup waypointGroup) {
        this.ensureLoaded();
        return this.cache.getIfPresent(waypointGroup.getKey()) != null;
    }

    public void put(WaypointGroup waypointGroup) {
        this.ensureLoaded();
        this.cache.put(waypointGroup.getKey(), waypointGroup);
        this.save(true);
    }

    public boolean putIfNew(WaypointGroup waypointGroup) {
        if (this.exists(waypointGroup)) {
            return false;
        } else {
            this.put(waypointGroup);
            return true;
        }
    }

    public void remove(WaypointGroup waypointGroup) {
        this.ensureLoaded();
        this.cache.invalidate(waypointGroup.getKey());
        waypointGroup.setDirty(false);
        this.save();
    }

    private void ensureLoaded() {
        if (this.cache.size() == 0L) {
            this.load();
        }
    }

    private void load() {
        File groupFile = new File(FileHandler.getWaypointDir(), "waypoint_groups.json");
        if (groupFile.exists()) {
            Map<String, WaypointGroup> map = new HashMap(0);
            try {
                String groupsString = Files.toString(groupFile, Charset.forName("UTF-8"));
                map = (Map<String, WaypointGroup>) WaypointGroup.GSON.fromJson(groupsString, map.getClass());
            } catch (Exception var6) {
                Journeymap.getLogger().error(String.format("Error reading WaypointGroups file %s: %s", groupFile, LogFormatter.toPartialString(var6)));
                try {
                    groupFile.renameTo(new File(groupFile.getParentFile(), groupFile.getName() + ".bad"));
                } catch (Exception var5) {
                    Journeymap.getLogger().error(String.format("Error renaming bad WaypointGroups file %s: %s", groupFile, LogFormatter.toPartialString(var6)));
                }
            }
            if (!map.isEmpty()) {
                this.cache.invalidateAll();
                this.cache.putAll(map);
                Journeymap.getLogger().info(String.format("Loaded WaypointGroups file %s", groupFile));
                this.cache.put(WaypointGroup.DEFAULT.getKey(), WaypointGroup.DEFAULT);
                return;
            }
        }
        this.cache.put(WaypointGroup.DEFAULT.getKey(), WaypointGroup.DEFAULT);
        this.save(true);
    }

    public void save() {
        this.save(true);
    }

    public void save(boolean force) {
        boolean doWrite = force;
        if (!force) {
            for (WaypointGroup group : this.cache.asMap().values()) {
                if (group.isDirty()) {
                    doWrite = true;
                    break;
                }
            }
        }
        if (doWrite) {
            TreeMap<String, WaypointGroup> map = null;
            try {
                map = new TreeMap(new Comparator<String>() {

                    final String defaultKey = WaypointGroup.DEFAULT.getKey();

                    public int compare(String o1, String o2) {
                        if (o1.equals(this.defaultKey)) {
                            return -1;
                        } else {
                            return o2.equals(this.defaultKey) ? 1 : o1.compareTo(o2);
                        }
                    }
                });
                map.putAll(this.cache.asMap());
            } catch (Exception var9) {
                Journeymap.getLogger().error(String.format("Error preparing WaypointGroups: %s", LogFormatter.toPartialString(var9)));
                return;
            }
            File groupFile = null;
            try {
                File waypointDir = FileHandler.getWaypointDir();
                if (!waypointDir.exists()) {
                    waypointDir.mkdirs();
                }
                groupFile = new File(waypointDir, "waypoint_groups.json");
                boolean isNew = groupFile.exists();
                Files.write(WaypointGroup.GSON.toJson(map), groupFile, Charset.forName("UTF-8"));
                for (WaypointGroup groupx : this.cache.asMap().values()) {
                    groupx.setDirty(false);
                }
                if (isNew) {
                    Journeymap.getLogger().info("Created WaypointGroups file: " + groupFile);
                }
            } catch (Exception var10) {
                Journeymap.getLogger().error(String.format("Error writing WaypointGroups file %s: %s", groupFile, LogFormatter.toPartialString(var10)));
            }
        }
    }

    private LoadingCache<String, WaypointGroup> createCache() {
        return CacheBuilder.newBuilder().concurrencyLevel(1).removalListener(new RemovalListener<String, Object>() {

            @ParametersAreNonnullByDefault
            public void onRemoval(RemovalNotification<String, Object> notification) {
                if (notification.getValue() != null && notification.getValue() instanceof LinkedTreeMap) {
                    LinkedTreeMap<Object, Object> value = (LinkedTreeMap<Object, Object>) notification.getValue();
                    WaypointGroup group = WaypointGroup.from(value);
                    for (Waypoint orphan : WaypointStore.INSTANCE.getAll(group)) {
                        orphan.setGroupName(WaypointGroup.DEFAULT.getName());
                        orphan.setGroup(WaypointGroup.DEFAULT);
                    }
                    WaypointGroupStore.this.save();
                }
            }
        }).build(new CacheLoader<String, WaypointGroup>() {

            @ParametersAreNonnullByDefault
            public WaypointGroup load(String key) throws Exception {
                int index = key.indexOf(":");
                String name;
                String origin;
                if (index < 1) {
                    origin = "Unknown";
                    name = key;
                    Journeymap.getLogger().warn("Problematic waypoint group key: " + key);
                } else {
                    origin = key.substring(0, index);
                    name = key.substring(index, key.length());
                }
                return new WaypointGroup(origin, name);
            }
        });
    }
}