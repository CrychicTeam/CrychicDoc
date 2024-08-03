package harmonised.pmmo.features.autovalues;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.resources.ResourceLocation;

public class AutoValues {

    private static ConcurrentMap<ReqType, Map<ResourceLocation, Map<String, Integer>>> reqValues = new ConcurrentHashMap();

    private static ConcurrentMap<EventType, Map<ResourceLocation, Map<String, Long>>> xpGainValues = new ConcurrentHashMap();

    private static Map<String, Integer> cacheRequirement(ReqType reqType, ResourceLocation objectID, Map<String, Integer> requirementMap) {
        ((Map) reqValues.computeIfAbsent(reqType, s -> new HashMap())).put(objectID, requirementMap);
        return requirementMap;
    }

    private static Map<String, Long> cacheXpGainValue(EventType eventType, ResourceLocation objectID, Map<String, Long> xpGainMap) {
        ((Map) xpGainValues.computeIfAbsent(eventType, s -> new HashMap())).put(objectID, xpGainMap);
        return xpGainMap;
    }

    public static void resetCache() {
        reqValues = new ConcurrentHashMap();
        xpGainValues = new ConcurrentHashMap();
    }

    public static Map<String, Integer> getRequirements(ReqType reqType, ResourceLocation objectID, ObjectType autoValueType) {
        if (!AutoValueConfig.isReqEnabled(reqType)) {
            ((Map) reqValues.computeIfAbsent(reqType, s -> new HashMap())).remove(objectID);
            return new HashMap();
        } else {
            Map<String, Integer> requirements = new HashMap();
            if (((Map) reqValues.computeIfAbsent(reqType, s -> new HashMap())).containsKey(objectID) && !(requirements = new HashMap((Map) ((Map) reqValues.get(reqType)).get(objectID))).isEmpty()) {
                return requirements;
            } else {
                requirements = (Map<String, Integer>) (switch(autoValueType) {
                    case ITEM ->
                        AutoItem.processReqs(reqType, objectID);
                    case BLOCK ->
                        AutoBlock.processReqs(reqType, objectID);
                    case ENTITY ->
                        AutoEntity.processReqs(reqType, objectID);
                    default ->
                        requirements;
                });
                Map<String, Integer> finalReqs = new HashMap();
                requirements.forEach((skill, level) -> {
                    if (level > 0) {
                        finalReqs.put(skill, level);
                    }
                });
                return cacheRequirement(reqType, objectID, finalReqs);
            }
        }
    }

    public static Map<String, Long> getExperienceAward(EventType eventType, ResourceLocation objectID, ObjectType autoValueType) {
        if (!AutoValueConfig.isXpGainEnabled(eventType)) {
            ((Map) xpGainValues.computeIfAbsent(eventType, s -> new HashMap())).remove(objectID);
            return new HashMap();
        } else {
            Map<String, Long> awards = new HashMap();
            if (((Map) xpGainValues.computeIfAbsent(eventType, s -> new HashMap())).containsKey(objectID) && !(awards = new HashMap((Map) ((Map) xpGainValues.get(eventType)).get(objectID))).isEmpty()) {
                return awards;
            } else {
                awards = (Map<String, Long>) (switch(autoValueType) {
                    case ITEM ->
                        AutoItem.processXpGains(eventType, objectID);
                    case BLOCK ->
                        AutoBlock.processXpGains(eventType, objectID);
                    case ENTITY ->
                        AutoEntity.processXpGains(eventType, objectID);
                    default ->
                        awards;
                });
                Map<String, Long> finalAwards = new HashMap();
                awards.forEach((skill, value) -> {
                    if (value > 0L) {
                        finalAwards.put(skill, value);
                    }
                });
                return cacheXpGainValue(eventType, objectID, finalAwards);
            }
        }
    }
}