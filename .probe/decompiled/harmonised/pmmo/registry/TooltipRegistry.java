package harmonised.pmmo.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TooltipRegistry {

    private Map<ReqType, HashMultimap<ResourceLocation, Function<ItemStack, Map<String, Integer>>>> itemReqTooltips = new HashMap();

    private Map<ReqType, HashMultimap<ResourceLocation, Function<BlockEntity, Map<String, Integer>>>> blockReqTooltips = new HashMap();

    private Map<ReqType, HashMultimap<ResourceLocation, Function<Entity, Map<String, Integer>>>> entityReqTooltips = new HashMap();

    private Map<EventType, HashMultimap<ResourceLocation, Function<ItemStack, Map<String, Long>>>> itemXpGainTooltips = new HashMap();

    private Map<EventType, HashMultimap<ResourceLocation, Function<BlockEntity, Map<String, Long>>>> blockXpGainTooltips = new HashMap();

    private Map<EventType, HashMultimap<ResourceLocation, Function<Entity, Map<String, Long>>>> entityXpGainTooltips = new HashMap();

    private Map<ModifierDataType, HashMultimap<ResourceLocation, Function<ItemStack, Map<String, Double>>>> itemBonusTooltips = new HashMap();

    public void clearRegistry() {
        this.itemReqTooltips = new HashMap();
        this.blockReqTooltips = new HashMap();
        this.entityReqTooltips = new HashMap();
        this.itemXpGainTooltips = new HashMap();
        this.blockXpGainTooltips = new HashMap();
        this.entityXpGainTooltips = new HashMap();
        this.itemBonusTooltips = new HashMap();
    }

    public void registerItemRequirementTooltipData(ResourceLocation res, ReqType reqType, Function<ItemStack, Map<String, Integer>> func) {
        if (func == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied Function Null");
        } else if (reqType == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ReqType Null");
        } else if (res == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ResourceLocation Null");
        } else {
            if (!this.itemReqTooltips.containsKey(reqType)) {
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + reqType.toString());
                this.itemReqTooltips.put(reqType, HashMultimap.create());
            }
            ((HashMultimap) this.itemReqTooltips.get(reqType)).get(res).add(func);
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip registered for: " + reqType.toString() + " " + res.toString());
        }
    }

    public void registerBlockRequirementTooltipData(ResourceLocation res, ReqType reqType, Function<BlockEntity, Map<String, Integer>> func) {
        if (func == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied Function Null");
        } else if (reqType == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ReqType Null");
        } else if (res == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ResourceLocation Null");
        } else {
            if (!this.blockReqTooltips.containsKey(reqType)) {
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + reqType.toString());
                this.blockReqTooltips.put(reqType, HashMultimap.create());
            }
            ((HashMultimap) this.blockReqTooltips.get(reqType)).get(res).add(func);
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip registered for: " + reqType.toString() + " " + res.toString());
        }
    }

    public void registerEntityRequirementTooltipData(ResourceLocation res, ReqType reqType, Function<Entity, Map<String, Integer>> func) {
        if (func == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied Function Null");
        } else if (reqType == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ReqType Null");
        } else if (res == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ResourceLocation Null");
        } else {
            if (!this.blockReqTooltips.containsKey(reqType)) {
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + reqType.toString() + " " + res.toString());
                this.entityReqTooltips.put(reqType, HashMultimap.create());
            }
            ((HashMultimap) this.entityReqTooltips.get(reqType)).get(res).add(func);
        }
    }

    public boolean requirementTooltipExists(ResourceLocation res, ReqType reqType) {
        Preconditions.checkNotNull(res);
        Preconditions.checkNotNull(reqType);
        return ((HashMultimap) this.blockReqTooltips.getOrDefault(reqType, HashMultimap.create())).containsKey(res) || ((HashMultimap) this.entityReqTooltips.getOrDefault(reqType, HashMultimap.create())).containsKey(res) || ((HashMultimap) this.itemReqTooltips.getOrDefault(reqType, HashMultimap.create())).containsKey(res);
    }

    public Map<String, Integer> getItemRequirementTooltipData(ResourceLocation res, ReqType reqType, ItemStack stack) {
        if (!this.requirementTooltipExists(res, reqType)) {
            return new HashMap();
        } else {
            Map<String, Integer> suppliedData = new HashMap();
            List<Map<String, Integer>> rawData = new ArrayList();
            for (Function<ItemStack, Map<String, Integer>> func : ((HashMultimap) this.itemReqTooltips.get(reqType)).get(res)) {
                rawData.add((Map) func.apply(stack));
            }
            for (int i = 0; i < rawData.size(); i++) {
                for (Entry<String, Integer> entry : ((Map) rawData.get(i)).entrySet()) {
                    suppliedData.merge((String) entry.getKey(), (Integer) entry.getValue(), Integer::max);
                }
            }
            return suppliedData;
        }
    }

    public Map<String, Integer> getBlockRequirementTooltipData(ResourceLocation res, ReqType reqType, BlockEntity tile) {
        if (!this.requirementTooltipExists(res, reqType)) {
            return new HashMap();
        } else {
            Map<String, Integer> suppliedData = new HashMap();
            List<Map<String, Integer>> rawData = new ArrayList();
            for (Function<BlockEntity, Map<String, Integer>> func : ((HashMultimap) this.blockReqTooltips.get(reqType)).get(res)) {
                rawData.add((Map) func.apply(tile));
            }
            for (int i = 0; i < rawData.size(); i++) {
                for (Entry<String, Integer> entry : ((Map) rawData.get(i)).entrySet()) {
                    suppliedData.merge((String) entry.getKey(), (Integer) entry.getValue(), Integer::max);
                }
            }
            return suppliedData;
        }
    }

    public Map<String, Integer> getEntityRequirementTooltipData(ResourceLocation res, ReqType reqType, Entity entity) {
        if (!this.requirementTooltipExists(res, reqType)) {
            return new HashMap();
        } else {
            Map<String, Integer> suppliedData = new HashMap();
            List<Map<String, Integer>> rawData = new ArrayList();
            for (Function<Entity, Map<String, Integer>> func : ((HashMultimap) this.entityReqTooltips.get(reqType)).get(res)) {
                rawData.add((Map) func.apply(entity));
            }
            for (int i = 0; i < rawData.size(); i++) {
                for (Entry<String, Integer> entry : ((Map) rawData.get(i)).entrySet()) {
                    suppliedData.merge((String) entry.getKey(), (Integer) entry.getValue(), Integer::max);
                }
            }
            return suppliedData;
        }
    }

    public void registerItemXpGainTooltipData(ResourceLocation res, EventType eventType, Function<ItemStack, Map<String, Long>> func) {
        Preconditions.checkNotNull(res);
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(func);
        if (!this.itemXpGainTooltips.containsKey(eventType)) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + eventType.toString() + " " + res.toString());
            this.itemXpGainTooltips.put(eventType, HashMultimap.create());
        }
        ((HashMultimap) this.itemXpGainTooltips.get(eventType)).get(res).add(func);
    }

    public void registerBlockXpGainTooltipData(ResourceLocation res, EventType eventType, Function<BlockEntity, Map<String, Long>> func) {
        Preconditions.checkNotNull(res);
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(func);
        if (!this.blockXpGainTooltips.containsKey(eventType)) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + eventType.toString() + " " + res.toString());
            this.blockXpGainTooltips.put(eventType, HashMultimap.create());
        }
        ((HashMultimap) this.blockXpGainTooltips.get(eventType)).get(res).add(func);
    }

    public void registerEntityXpGainTooltipData(ResourceLocation res, EventType eventType, Function<Entity, Map<String, Long>> func) {
        Preconditions.checkNotNull(res);
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(func);
        if (!this.entityXpGainTooltips.containsKey(eventType)) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + eventType.toString() + " " + res.toString());
            this.entityXpGainTooltips.put(eventType, HashMultimap.create());
        }
        ((HashMultimap) this.entityXpGainTooltips.get(eventType)).get(res).add(func);
    }

    public boolean xpGainTooltipExists(ResourceLocation res, EventType eventType) {
        Preconditions.checkNotNull(res);
        Preconditions.checkNotNull(eventType);
        return ((HashMultimap) this.blockXpGainTooltips.getOrDefault(eventType, HashMultimap.create())).containsKey(res) || ((HashMultimap) this.entityXpGainTooltips.getOrDefault(eventType, HashMultimap.create())).containsKey(res) || ((HashMultimap) this.itemXpGainTooltips.getOrDefault(eventType, HashMultimap.create())).containsKey(res);
    }

    public Map<String, Long> getItemXpGainTooltipData(ResourceLocation itemID, EventType eventType, ItemStack stack) {
        List<Map<String, Long>> rawData = new ArrayList();
        Map<String, Long> outData = new HashMap();
        for (Function<ItemStack, Map<String, Long>> func : ((HashMultimap) this.itemXpGainTooltips.getOrDefault(eventType, HashMultimap.create())).get(itemID)) {
            rawData.add((Map) func.apply(stack));
        }
        for (Map<String, Long> map : rawData) {
            for (Entry<String, Long> entry : map.entrySet()) {
                outData.merge((String) entry.getKey(), (Long) entry.getValue(), Long::max);
            }
        }
        return outData;
    }

    public Map<String, Long> getBlockXpGainTooltipData(ResourceLocation blockID, EventType eventType, BlockEntity tile) {
        List<Map<String, Long>> rawData = new ArrayList();
        Map<String, Long> outData = new HashMap();
        for (Function<BlockEntity, Map<String, Long>> func : ((HashMultimap) this.blockXpGainTooltips.getOrDefault(eventType, HashMultimap.create())).get(blockID)) {
            rawData.add((Map) func.apply(tile));
        }
        for (Map<String, Long> map : rawData) {
            for (Entry<String, Long> entry : map.entrySet()) {
                outData.merge((String) entry.getKey(), (Long) entry.getValue(), Long::max);
            }
        }
        return outData;
    }

    public Map<String, Long> getEntityXpGainTooltipData(ResourceLocation entityID, EventType eventType, Entity entity) {
        List<Map<String, Long>> rawData = new ArrayList();
        Map<String, Long> outData = new HashMap();
        for (Function<Entity, Map<String, Long>> func : ((HashMultimap) this.entityXpGainTooltips.getOrDefault(eventType, HashMultimap.create())).get(entityID)) {
            rawData.add((Map) func.apply(entity));
        }
        for (Map<String, Long> map : rawData) {
            for (Entry<String, Long> entry : map.entrySet()) {
                outData.merge((String) entry.getKey(), (Long) entry.getValue(), Long::max);
            }
        }
        return outData;
    }

    public void registerItemBonusTooltipData(ResourceLocation res, ModifierDataType type, Function<ItemStack, Map<String, Double>> func) {
        if (func == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied Function Null");
        } else if (type == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ModifierType Null");
        } else if (res == null) {
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Supplied ResourceLocation Null");
        } else {
            if (!this.itemBonusTooltips.containsKey(type)) {
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip category created for: " + type.toString());
                this.itemBonusTooltips.put(type, HashMultimap.create());
            }
            ((HashMultimap) this.itemBonusTooltips.get(type)).get(res).add(func);
            MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "New tooltip registered for: " + type.toString() + " " + res.toString());
        }
    }

    public boolean bonusTooltipExists(ResourceLocation res, ModifierDataType type) {
        if (res == null) {
            return false;
        } else if (type == null) {
            return false;
        } else {
            return this.itemBonusTooltips.containsKey(type) ? ((HashMultimap) this.itemBonusTooltips.get(type)).containsKey(res) : false;
        }
    }

    public Map<String, Double> getBonusTooltipData(ResourceLocation res, ModifierDataType type, ItemStack stack) {
        if (!this.bonusTooltipExists(res, type)) {
            return new HashMap();
        } else {
            Map<String, Double> suppliedData = new HashMap();
            List<Map<String, Double>> rawData = new ArrayList();
            for (Function<ItemStack, Map<String, Double>> func : ((HashMultimap) this.itemBonusTooltips.get(type)).get(res)) {
                rawData.add((Map) func.apply(stack));
            }
            for (int i = 0; i < rawData.size(); i++) {
                for (Entry<String, Double> entry : ((Map) rawData.get(i)).entrySet()) {
                    suppliedData.merge((String) entry.getKey(), (Double) entry.getValue(), Double::max);
                }
            }
            return suppliedData;
        }
    }
}