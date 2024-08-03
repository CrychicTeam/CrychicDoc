package journeymap.client.data;

import com.google.common.cache.CacheLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import journeymap.client.JourneymapClient;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.EntityHelper;
import net.minecraft.world.entity.LivingEntity;

public class AnimalsData extends CacheLoader<Class, Map<String, EntityDTO>> {

    public Map<String, EntityDTO> load(Class aClass) throws Exception {
        if (!FeatureManager.getInstance().isAllowed(Feature.RadarAnimals)) {
            return new HashMap();
        } else {
            List<EntityDTO> list = EntityHelper.getAnimalsNearby();
            List<EntityDTO> finalList = new ArrayList(list);
            for (EntityDTO entityDTO : list) {
                LivingEntity entityLiving = (LivingEntity) entityDTO.entityLivingRef.get();
                if (entityLiving == null) {
                    finalList.remove(entityDTO);
                } else if (entityLiving.m_20160_()) {
                    finalList.remove(entityDTO);
                }
            }
            return EntityHelper.buildEntityIdMap(finalList, true);
        }
    }

    public long getTTL() {
        return (long) Math.max(1000, JourneymapClient.getInstance().getCoreProperties().cacheAnimalsData.get());
    }
}