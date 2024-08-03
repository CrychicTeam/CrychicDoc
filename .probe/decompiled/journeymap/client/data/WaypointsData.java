package journeymap.client.data;

import com.google.common.cache.CacheLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import journeymap.client.JourneymapClient;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;

public class WaypointsData extends CacheLoader<Class, Collection<Waypoint>> {

    public static boolean isManagerEnabled() {
        return JourneymapClient.getInstance().getWaypointProperties().managerEnabled.get() && JourneymapClient.getInstance().getStateHandler().isWaypointsAllowed();
    }

    protected static List<Waypoint> getWaypoints() {
        ArrayList<Waypoint> list = new ArrayList(0);
        if (isManagerEnabled()) {
            list.addAll(WaypointStore.INSTANCE.getAll());
        }
        return list;
    }

    private static boolean waypointClassesFound(String... names) throws Exception {
        boolean loaded = true;
        for (String name : names) {
            if (!loaded) {
                break;
            }
            try {
                loaded = false;
                Class.forName(name);
                loaded = true;
                Journeymap.getLogger().debug("Class found: " + name);
            } catch (NoClassDefFoundError var7) {
                throw new Exception("Class detected, but is obsolete: " + var7.getMessage());
            } catch (ClassNotFoundException var8) {
                Journeymap.getLogger().debug("Class not found: " + name);
            } catch (VerifyError var9) {
                throw new Exception("Class detected, but is obsolete: " + var9.getMessage());
            } catch (Throwable var10) {
                throw new Exception("Class detected, but produced errors.", var10);
            }
        }
        return loaded;
    }

    public Collection<Waypoint> load(Class aClass) throws Exception {
        return getWaypoints();
    }

    public long getTTL() {
        return 5000L;
    }
}