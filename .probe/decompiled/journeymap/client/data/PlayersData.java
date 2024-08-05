package journeymap.client.data;

import com.google.common.cache.CacheLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import journeymap.client.JourneymapClient;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.EntityHelper;

public class PlayersData extends CacheLoader<Class, Map<String, EntityDTO>> {

    public Map<String, EntityDTO> load(Class aClass) throws Exception {
        if (!FeatureManager.getInstance().isAllowed(Feature.RadarPlayers)) {
            return new HashMap();
        } else {
            List<EntityDTO> list = EntityHelper.getPlayersNearby();
            return EntityHelper.buildEntityIdMap(list, true);
        }
    }

    public long getTTL() {
        return (long) JourneymapClient.getInstance().getCoreProperties().cachePlayersData.get().intValue();
    }
}