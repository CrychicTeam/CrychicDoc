package harmonised.pmmo.config.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.core.nbt.LogicEntry;
import harmonised.pmmo.core.nbt.NBTUtils;
import harmonised.pmmo.util.Functions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public record ObjectData(boolean override, Set<String> tagValues, Map<ReqType, Map<String, Integer>> reqs, Map<ReqType, List<LogicEntry>> nbtReqs, Map<ResourceLocation, Integer> negativeEffects, Map<EventType, Map<String, Long>> xpValues, Map<EventType, Map<String, Map<String, Long>>> damageXpValues, Map<EventType, List<LogicEntry>> nbtXpValues, Map<ModifierDataType, Map<String, Double>> bonuses, Map<ModifierDataType, List<LogicEntry>> nbtBonuses, Map<ResourceLocation, CodecTypes.SalvageData> salvage, VeinData veinData) implements DataSource<ObjectData> {

    public static final Codec<ObjectData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("override").forGetter(od -> Optional.of(od.override())), Codec.STRING.listOf().optionalFieldOf("isTagFor").forGetter(od -> Optional.of(new ArrayList(od.tagValues))), Codec.optionalField("requirements", Codec.simpleMap(ReqType.CODEC, CodecTypes.INTEGER_CODEC, StringRepresentable.keys(ReqType.values())).codec()).forGetter(od -> Optional.of(od.reqs())), Codec.optionalField("nbt_requirements", Codec.simpleMap(ReqType.CODEC, Codec.list(LogicEntry.CODEC), StringRepresentable.keys(ReqType.values())).codec()).forGetter(od -> Optional.of(od.nbtReqs())), Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).optionalFieldOf("negative_effect").forGetter(od -> Optional.of(od.negativeEffects())), Codec.optionalField("xp_values", Codec.simpleMap(EventType.CODEC, CodecTypes.LONG_CODEC, StringRepresentable.keys(EventType.values())).codec()).forGetter(od -> Optional.of(od.xpValues())), Codec.optionalField("nbt_xp_values", Codec.simpleMap(EventType.CODEC, Codec.list(LogicEntry.CODEC), StringRepresentable.keys(EventType.values())).codec()).forGetter(od -> Optional.of(od.nbtXpValues())), Codec.optionalField("dealt_damage_xp", CodecTypes.DAMAGE_XP_CODEC).forGetter(od -> Optional.of((Map) od.damageXpValues().getOrDefault(EventType.DEAL_DAMAGE, new HashMap()))), Codec.optionalField("received_damage_xp", CodecTypes.DAMAGE_XP_CODEC).forGetter(od -> Optional.of((Map) od.damageXpValues().getOrDefault(EventType.RECEIVE_DAMAGE, new HashMap()))), Codec.optionalField("bonuses", Codec.simpleMap(ModifierDataType.CODEC, CodecTypes.DOUBLE_CODEC, StringRepresentable.keys(ModifierDataType.values())).codec()).forGetter(od -> Optional.of(od.bonuses())), Codec.optionalField("nbt_bonuses", Codec.simpleMap(ModifierDataType.CODEC, Codec.list(LogicEntry.CODEC), StringRepresentable.keys(ModifierDataType.values())).codec()).forGetter(od -> Optional.of(od.nbtBonuses())), Codec.unboundedMap(ResourceLocation.CODEC, CodecTypes.SALVAGE_CODEC).optionalFieldOf("salvage").forGetter(od -> Optional.of(od.salvage())), VeinData.VEIN_DATA_CODEC.optionalFieldOf("vein_data").forGetter(od -> Optional.of(od.veinData()))).apply(instance, (override, tags, reqs, nbtreqs, effects, xp, nbtXp, dealt, received, bonus, nbtbonus, salvage, vein) -> new ObjectData((Boolean) override.orElse(false), new HashSet((Collection) tags.orElse(List.of())), DataSource.<ReqType, Map<String, Integer>>clearEmptyValues((Map<ReqType, Map<String, Integer>>) reqs.orElse(new HashMap())), DataSource.<ReqType, List<LogicEntry>>clearEmptyValues((Map<ReqType, List<LogicEntry>>) nbtreqs.orElse(new HashMap())), DataSource.<ResourceLocation, Integer>clearEmptyValues((Map<ResourceLocation, Integer>) effects.orElse(new HashMap())), DataSource.<EventType, Map<String, Long>>clearEmptyValues((Map<EventType, Map<String, Long>>) xp.orElse(new HashMap())), Map.of(EventType.DEAL_DAMAGE, DataSource.clearEmptyValues((Map) dealt.orElse(new HashMap())), EventType.RECEIVE_DAMAGE, DataSource.clearEmptyValues((Map) received.orElse(new HashMap()))), DataSource.<EventType, List<LogicEntry>>clearEmptyValues((Map<EventType, List<LogicEntry>>) nbtXp.orElse(new HashMap())), DataSource.<ModifierDataType, Map<String, Double>>clearEmptyValues((Map<ModifierDataType, Map<String, Double>>) bonus.orElse(new HashMap())), DataSource.<ModifierDataType, List<LogicEntry>>clearEmptyValues((Map<ModifierDataType, List<LogicEntry>>) nbtbonus.orElse(new HashMap())), DataSource.<ResourceLocation, CodecTypes.SalvageData>clearEmptyValues((Map<ResourceLocation, CodecTypes.SalvageData>) salvage.orElse(new HashMap())), (VeinData) vein.orElse(VeinData.EMPTY))));

    public ObjectData() {
        this(false, new HashSet(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), VeinData.EMPTY);
    }

    public Map<ResourceLocation, CodecTypes.SalvageData> salvage() {
        return (Map<ResourceLocation, CodecTypes.SalvageData>) this.salvage.entrySet().stream().filter(entry -> !ForgeRegistries.ITEMS.getValue((ResourceLocation) entry.getKey()).equals(Items.AIR)).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public Map<String, Long> getXpValues(EventType type, CompoundTag nbt) {
        if (this.nbtXpValues().get(type) == null) {
            return switch(type) {
                case RECEIVE_DAMAGE, DEAL_DAMAGE ->
                    (Map) ((Map) this.damageXpValues().getOrDefault(type, new HashMap())).getOrDefault(nbt.getString("damage_type"), new HashMap());
                default ->
                    (Map) this.xpValues().getOrDefault(type, new HashMap());
            };
        } else {
            return NBTUtils.getExperienceAward((List<LogicEntry>) this.nbtXpValues().get(type), nbt);
        }
    }

    @Override
    public void setXpValues(EventType type, Map<String, Long> award) {
        this.xpValues().put(type, award);
    }

    @Override
    public Map<String, Double> getBonuses(ModifierDataType type, CompoundTag nbt) {
        return this.nbtBonuses().get(type) == null ? (Map) this.bonuses().getOrDefault(type, new HashMap()) : NBTUtils.getBonuses((List<LogicEntry>) this.nbtBonuses().get(type), nbt);
    }

    @Override
    public void setBonuses(ModifierDataType type, Map<String, Double> bonuses) {
        this.bonuses().put(type, bonuses);
    }

    @Override
    public Map<String, Integer> getReqs(ReqType type, CompoundTag nbt) {
        return this.nbtReqs().get(type) == null ? (Map) this.reqs().getOrDefault(type, new HashMap()) : NBTUtils.getRequirement((List<LogicEntry>) this.nbtReqs().get(type), nbt);
    }

    @Override
    public void setReqs(ReqType type, Map<String, Integer> reqs) {
        this.reqs().put(type, reqs);
    }

    @Override
    public Map<ResourceLocation, Integer> getNegativeEffect() {
        return this.negativeEffects();
    }

    @Override
    public void setNegativeEffects(Map<ResourceLocation, Integer> neg) {
        this.negativeEffects().clear();
        this.negativeEffects().putAll(neg);
    }

    @Override
    public Set<String> getTagValues() {
        return this.tagValues();
    }

    public ObjectData combine(ObjectData two) {
        Set<String> tagValues = new HashSet();
        Map<EventType, Map<String, Long>> xpValues = new HashMap();
        Map<EventType, List<LogicEntry>> nbtXp = new HashMap();
        Map<EventType, Map<String, Map<String, Long>>> damageXP = new HashMap();
        Map<ModifierDataType, Map<String, Double>> bonuses = new HashMap();
        Map<ModifierDataType, List<LogicEntry>> nbtBonus = new HashMap();
        Map<ReqType, Map<String, Integer>> reqs = new HashMap();
        Map<ReqType, List<LogicEntry>> nbtReq = new HashMap();
        Map<ResourceLocation, Integer> reqEffects = new HashMap();
        Map<ResourceLocation, CodecTypes.SalvageData> salvage = new HashMap();
        VeinData[] combinedVein = new VeinData[] { this.veinData() };
        BiConsumer<ObjectData, ObjectData> bothOrNeither = (o, t) -> {
            nbtXp.putAll(o.nbtXpValues());
            t.nbtXpValues().forEach((event, logic) -> nbtXp.merge(event, logic, (a, b) -> {
                ArrayList<LogicEntry> list = new ArrayList(a);
                list.addAll(b);
                return list;
            }));
            nbtBonus.putAll(o.nbtBonuses());
            t.nbtBonuses().forEach((modifier, logic) -> nbtBonus.merge(modifier, logic, (a, b) -> {
                ArrayList<LogicEntry> list = new ArrayList(a);
                list.addAll(b);
                return list;
            }));
            nbtReq.putAll(o.nbtReqs());
            t.nbtReqs().forEach((req, logic) -> nbtReq.merge(req, logic, (a, b) -> {
                ArrayList<LogicEntry> list = new ArrayList(a);
                list.addAll(b);
                return list;
            }));
            tagValues.addAll(o.tagValues());
            t.tagValues.forEach(rl -> {
                if (!tagValues.contains(rl)) {
                    tagValues.add(rl);
                }
            });
            xpValues.putAll(o.xpValues());
            t.xpValues().forEach((event, map) -> xpValues.merge(event, map, (oMap, nMap) -> {
                Map<String, Long> mergedMap = new HashMap(oMap);
                nMap.forEach((k, v) -> mergedMap.merge(k, v, (o1, n1) -> o1 > n1 ? o1 : n1));
                return mergedMap;
            }));
            damageXP.putAll(o.damageXpValues());
            t.damageXpValues().forEach((event, map) -> map.forEach((dmg, xp) -> ((Map) damageXP.computeIfAbsent(event, e -> new HashMap())).merge(dmg, xp, (oMap, nMap) -> {
                Map<String, Long> mergedMap = new HashMap(oMap);
                nMap.forEach((k, v) -> mergedMap.merge(k, v, (o1, n1) -> o1 > n1 ? o1 : n1));
                return mergedMap;
            })));
            bonuses.putAll(o.bonuses());
            t.bonuses().forEach((event, map) -> bonuses.merge(event, map, (oMap, nMap) -> {
                Map<String, Double> mergedMap = new HashMap(oMap);
                nMap.forEach((k, v) -> mergedMap.merge(k, v, (o1, n1) -> o1 > n1 ? o1 : n1));
                return mergedMap;
            }));
            reqs.putAll(o.reqs());
            t.reqs().forEach((event, map) -> reqs.merge(event, map, (oMap, nMap) -> {
                Map<String, Integer> mergedMap = new HashMap(oMap);
                nMap.forEach((k, v) -> mergedMap.merge(k, v, (o1, n1) -> o1 > n1 ? o1 : n1));
                return mergedMap;
            }));
            reqEffects.putAll(o.negativeEffects());
            t.negativeEffects().forEach((skill, level) -> reqEffects.merge(skill, level, (o1, n1) -> o1 > n1 ? o1 : n1));
            salvage.putAll(o.salvage());
            t.salvage().forEach((rl, data) -> salvage.merge(rl, data, (oD, nD) -> CodecTypes.SalvageData.combine(oD, nD, o.override(), t.override())));
            combinedVein[0] = combinedVein[0].combine(t.veinData());
        };
        Functions.biPermutation(this, two, this.override(), two.override(), (o, t) -> {
            tagValues.addAll(o.tagValues().isEmpty() ? t.tagValues() : o.tagValues());
            xpValues.putAll(o.xpValues().isEmpty() ? t.xpValues() : o.xpValues());
            nbtXp.putAll(o.nbtXpValues().isEmpty() ? t.nbtXpValues() : o.nbtXpValues());
            damageXP.putAll(o.damageXpValues().isEmpty() ? t.damageXpValues() : o.damageXpValues());
            bonuses.putAll(o.bonuses().isEmpty() ? t.bonuses() : o.bonuses());
            nbtBonus.putAll(o.nbtBonuses().isEmpty() ? t.nbtBonuses() : o.nbtBonuses());
            reqs.putAll(o.reqs().isEmpty() ? t.reqs() : o.reqs());
            nbtReq.putAll(o.nbtReqs().isEmpty() ? t.nbtReqs() : o.nbtReqs());
            reqEffects.putAll(o.negativeEffects().isEmpty() ? t.negativeEffects() : o.negativeEffects());
            salvage.putAll(o.salvage().isEmpty() ? t.salvage() : o.salvage());
            combinedVein[0] = o.veinData().isUnconfigured() ? t.veinData() : o.veinData();
        }, bothOrNeither, bothOrNeither);
        return new ObjectData(this.override() || two.override(), tagValues, reqs, nbtReq, reqEffects, xpValues, damageXP, nbtXp, bonuses, nbtBonus, salvage, combinedVein[0]);
    }

    @Override
    public boolean isUnconfigured() {
        return this.reqs().values().stream().allMatch(Map::isEmpty) && this.nbtReqs().values().stream().allMatch(List::isEmpty) && this.negativeEffects().isEmpty() && this.xpValues().values().stream().allMatch(Map::isEmpty) && this.nbtXpValues().values().stream().allMatch(List::isEmpty) && this.damageXpValues().values().stream().allMatch(Map::isEmpty) && this.bonuses().values().stream().allMatch(Map::isEmpty) && this.nbtBonuses().values().stream().allMatch(List::isEmpty) && this.salvage().keySet().stream().allMatch(rl -> rl.equals(new ResourceLocation("item"))) && this.veinData().isUnconfigured();
    }
}