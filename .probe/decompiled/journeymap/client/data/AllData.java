package journeymap.client.data;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import journeymap.client.JourneymapClient;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.helper.DimensionHelper;

public class AllData extends CacheLoader<Long, Map> {

    public Map<AllData.Key, Object> load(@NotNull Long since) throws Exception {
        DataCache cache = DataCache.INSTANCE;
        LinkedHashMap<AllData.Key, Object> props = new LinkedHashMap();
        props.put(AllData.Key.world, cache.getWorld(false));
        props.put(AllData.Key.player, cache.getPlayer(false));
        props.put(AllData.Key.images, new ImagesData(since));
        if (JourneymapClient.getInstance().getFullMapProperties().showWaypoints.get()) {
            String currentDimension = DimensionHelper.getDimKeyName(cache.getPlayer(false).dimension);
            Collection<Waypoint> waypoints = cache.getWaypoints(false);
            Map<String, Waypoint> wpMap = new HashMap();
            for (Waypoint waypoint : waypoints) {
                if (waypoint.getDimensions().contains(currentDimension)) {
                    wpMap.put(waypoint.getId(), waypoint);
                }
            }
            props.put(AllData.Key.waypoints, wpMap);
        } else {
            props.put(AllData.Key.waypoints, Collections.emptyMap());
        }
        if (!WorldData.isHardcoreAndMultiplayer()) {
            props.put(AllData.Key.animals, Collections.emptyMap());
            props.put(AllData.Key.mobs, Collections.emptyMap());
            props.put(AllData.Key.players, Collections.emptyMap());
            props.put(AllData.Key.villagers, Collections.emptyMap());
        }
        return ImmutableMap.copyOf(props);
    }

    public long getTTL() {
        return (long) (JourneymapClient.getInstance().getCoreProperties().renderDelay.get() * 2000);
    }

    public static enum Key {

        animals,
        images,
        mobs,
        player,
        players,
        villagers,
        waypoints,
        world
    }
}