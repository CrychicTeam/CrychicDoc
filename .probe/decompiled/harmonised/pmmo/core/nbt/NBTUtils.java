package harmonised.pmmo.core.nbt;

import com.mojang.datafixers.util.Pair;
import harmonised.pmmo.config.GlobalsConfig;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;

public class NBTUtils {

    private static final Map<Pair<CompoundTag, Set<LogicEntry>>, Map<String, Double>> cache = new HashMap();

    public static Map<String, Long> getExperienceAward(List<LogicEntry> logic, CompoundTag nbt) {
        return translateToLong(evaluateEntries(nbt, new LinkedHashSet(logic)));
    }

    public static Map<String, Integer> getRequirement(List<LogicEntry> logic, CompoundTag nbt) {
        return translateToInt(evaluateEntries(nbt, new LinkedHashSet(logic)));
    }

    public static Map<String, Double> getBonuses(List<LogicEntry> logic, CompoundTag nbt) {
        return evaluateEntries(nbt, new LinkedHashSet(logic));
    }

    private static Map<String, Double> evaluateEntries(CompoundTag nbt, LinkedHashSet<LogicEntry> logic) {
        Map<String, Double> output = new HashMap();
        if (nbt != null && !nbt.isEmpty()) {
            if (cache.containsKey(Pair.of(nbt, logic))) {
                return MsLoggy.DEBUG.logAndReturn((Map<String, Double>) cache.get(Pair.of(nbt, logic)), MsLoggy.LOG_CODE.DATA, "NBT Cache Used");
            } else {
                List<NBTUtils.LogicTier> logicSequence = new ArrayList();
                for (LogicEntry entry : logic) {
                    logicSequence.add(new NBTUtils.LogicTier(entry.behavior(), entry.addCases(), processCases(entry.cases(), nbt)));
                }
                List<Map<String, Double>> interMap = new ArrayList();
                for (NBTUtils.LogicTier logicTier : logicSequence) {
                    Map<String, Double> combinedMap = new HashMap();
                    List<Result> data = logicTier.results;
                    boolean isSummative = logicTier.isSummative;
                    for (Result r : data) {
                        if (r != null && r.compares()) {
                            Map<String, Double> value = r.values();
                            for (Entry<String, Double> val : value.entrySet()) {
                                combinedMap.merge((String) val.getKey(), (Double) val.getValue(), isSummative ? Double::sum : Double::max);
                            }
                        }
                    }
                    interMap.add(combinedMap);
                }
                for (int i = 0; i < logicSequence.size(); i++) {
                    switch(((NBTUtils.LogicTier) logicSequence.get(i)).behavior()) {
                        case SUB_FROM:
                            for (Entry<String, Double> value : ((Map) interMap.get(i)).entrySet()) {
                                if ((Double) output.getOrDefault(value.getKey(), 0.0) - (Double) value.getValue() <= 0.0) {
                                    output.remove(value.getKey());
                                } else {
                                    output.merge((String) value.getKey(), (Double) value.getValue(), (oldValue, newValue) -> oldValue - newValue);
                                }
                            }
                            break;
                        case HIGHEST:
                            for (Entry<String, Double> value : ((Map) interMap.get(i)).entrySet()) {
                                output.merge((String) value.getKey(), (Double) value.getValue(), Double::max);
                            }
                            break;
                        case REPLACE:
                            output.putAll((Map) interMap.get(i));
                            break;
                        default:
                            for (Entry<String, Double> valuex : ((Map) interMap.get(i)).entrySet()) {
                                output.merge((String) valuex.getKey(), (Double) valuex.getValue(), Double::sum);
                            }
                    }
                }
                cache.put(Pair.of(nbt, logic), output);
                return output;
            }
        } else {
            return output;
        }
    }

    private static List<Result> processCases(List<LogicEntry.Case> cases, CompoundTag nbt) {
        List<Result> results = new ArrayList();
        for (LogicEntry.Case caseIterant : cases) {
            for (String path : caseIterant.paths()) {
                for (LogicEntry.Criteria critObj : caseIterant.criteria()) {
                    Map<String, Double> values = critObj.skillMap();
                    Operator operator = critObj.operator();
                    for (String compare : PathReader.getNBTValues(getActualPath(path), nbt)) {
                        if (!operator.equals(Operator.EXISTS)) {
                            for (String comparators : (List) critObj.comparators().orElseGet(ArrayList::new)) {
                                String comparator = getActualConstant(comparators);
                                results.add(new Result(operator, comparator, compare, values));
                            }
                        } else {
                            results.add(new Result(operator, "", compare, values));
                        }
                    }
                }
            }
        }
        return results;
    }

    private static Map<String, Long> translateToLong(Map<String, Double> src) {
        Map<String, Long> output = new HashMap();
        src.forEach((k, v) -> output.put(k, v.longValue()));
        return output;
    }

    private static Map<String, Integer> translateToInt(Map<String, Double> src) {
        Map<String, Integer> output = new HashMap();
        src.forEach((k, v) -> output.put(k, v.intValue()));
        return output;
    }

    private static String getActualPath(String key) {
        return key.contains("#") ? (String) GlobalsConfig.PATHS.get().getOrDefault(key.replace("#", ""), "") : key;
    }

    private static String getActualConstant(String key) {
        return key.contains("#") ? (String) GlobalsConfig.CONSTANTS.get().getOrDefault(key.replace("#", ""), "") : key;
    }

    private static record LogicTier(BehaviorToPrevious behavior, boolean isSummative, List<Result> results) {
    }
}