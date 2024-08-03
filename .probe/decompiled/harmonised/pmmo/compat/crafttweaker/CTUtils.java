package harmonised.pmmo.compat.crafttweaker;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.MapData;
import com.blamejared.crafttweaker.api.data.converter.tag.TagToDataConverter;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import harmonised.pmmo.api.APIUtils;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.PerkSide;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.api.perks.Perk;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.EnhancementsData;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.core.Core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import org.apache.commons.lang3.function.TriFunction;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Document("mods/PMMO/CTUtils")
@Name("mods.pmmo.CTUtils")
public class CTUtils implements IRuntimeAction {

    public static final String MOB_HEALTH = "health";

    public static final String MOB_SPEED = "speed";

    public static final String MOB_DAMAGE = "damage";

    public void apply() {
    }

    public String describe() {
        return "Functionality for using ZenCode to access PMMO's API.";
    }

    public String systemName() {
        return "pmmo";
    }

    @Method
    public static void setReq(ObjectType objectType, ResourceLocation objectID, ReqType type, Map<String, Integer> requirements) {
        switch(objectType) {
            case ITEM:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().ITEM_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).reqs().put(type, requirements);
                break;
            case BLOCK:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().BLOCK_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).reqs().put(type, requirements);
                break;
            case ENTITY:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().ENTITY_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).reqs().put(type, requirements);
                break;
            case DIMENSION:
                {
                    LocationData data = (LocationData) Core.get(LogicalSide.SERVER).getLoader().DIMENSION_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData());
                    data.travelReq().clear();
                    data.travelReq().putAll(requirements);
                    break;
                }
            case BIOME:
                {
                    LocationData data = (LocationData) Core.get(LogicalSide.SERVER).getLoader().BIOME_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData());
                    data.travelReq().clear();
                    data.travelReq().putAll(requirements);
                }
        }
    }

    @Method
    public static void setEnchantment(ResourceLocation enchantID, int enchantLevel, Map<String, Integer> reqs) {
        EnhancementsData data = (EnhancementsData) Core.get(LogicalSide.SERVER).getLoader().ENCHANTMENT_LOADER.getData().computeIfAbsent(enchantID, rl -> new EnhancementsData());
        data.skillArray().clear();
        data.skillArray().put(enchantLevel, reqs);
    }

    @Method
    public static void setXpAward(ObjectType objectType, ResourceLocation objectID, EventType type, Map<String, Long> award) {
        switch(objectType) {
            case ITEM:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().ITEM_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).xpValues().put(type, award);
                break;
            case BLOCK:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().BLOCK_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).xpValues().put(type, award);
                break;
            case ENTITY:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().ENTITY_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).xpValues().put(type, award);
        }
    }

    @Method
    public static void setEffectXp(ResourceLocation effectID, int effectLevel, Map<String, Integer> xpGains) {
        EnhancementsData data = (EnhancementsData) Core.get(LogicalSide.SERVER).getLoader().EFFECT_LOADER.getData().computeIfAbsent(effectID, rl -> new EnhancementsData());
        data.skillArray().clear();
        data.skillArray().put(effectLevel, xpGains);
    }

    @Method
    public static void setBonus(ObjectType objectType, ResourceLocation objectID, ModifierDataType type, Map<String, Double> bonus) {
        switch(objectType) {
            case ITEM:
                ((ObjectData) Core.get(LogicalSide.SERVER).getLoader().ITEM_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData())).bonuses().put(type, bonus);
            case BLOCK:
            case ENTITY:
            default:
                break;
            case DIMENSION:
                ((LocationData) Core.get(LogicalSide.SERVER).getLoader().DIMENSION_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData())).bonusMap().put(type, bonus);
                break;
            case BIOME:
                ((LocationData) Core.get(LogicalSide.SERVER).getLoader().BIOME_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData())).bonusMap().put(type, bonus);
        }
    }

    @Method
    public static void setNegativeEffect(ObjectType objectType, ResourceLocation objectID, Map<ResourceLocation, Integer> effects) {
        switch(objectType) {
            case ITEM:
                {
                    ObjectData data = (ObjectData) Core.get(LogicalSide.SERVER).getLoader().ITEM_LOADER.getData().computeIfAbsent(objectID, rl -> new ObjectData());
                    data.negativeEffects().clear();
                    data.negativeEffects().putAll((Map) effects.entrySet().stream().collect(Collectors.toMap(entry -> (ResourceLocation) entry.getKey(), entry -> (Integer) entry.getValue())));
                }
            case BLOCK:
            case ENTITY:
            default:
                break;
            case DIMENSION:
                {
                    LocationData data = (LocationData) Core.get(LogicalSide.SERVER).getLoader().DIMENSION_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData());
                    data.negative().clear();
                    data.negative().putAll(effects);
                    break;
                }
            case BIOME:
                {
                    LocationData data = (LocationData) Core.get(LogicalSide.SERVER).getLoader().BIOME_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData());
                    data.negative().clear();
                    data.negative().putAll(effects);
                }
        }
    }

    @Method
    public static void setPositiveEffect(ObjectType objectType, ResourceLocation objectID, Map<ResourceLocation, Integer> effects) {
        switch(objectType) {
            case DIMENSION:
                {
                    LocationData data = (LocationData) Core.get(LogicalSide.SERVER).getLoader().DIMENSION_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData());
                    data.positive().clear();
                    data.positive().putAll(effects);
                    break;
                }
            case BIOME:
                {
                    LocationData data = (LocationData) Core.get(LogicalSide.SERVER).getLoader().BIOME_LOADER.getData().computeIfAbsent(objectID, rl -> new LocationData());
                    data.positive().clear();
                    data.positive().putAll(effects);
                }
        }
    }

    @Method
    public static void setSalvage(ResourceLocation item, Map<ResourceLocation, CTUtils.SalvageBuilder> salvage) {
        ObjectData data = (ObjectData) Core.get(LogicalSide.SERVER).getLoader().ITEM_LOADER.getData().computeIfAbsent(item, rl -> new ObjectData());
        data.salvage().clear();
        data.salvage().putAll((Map) salvage.entrySet().stream().collect(Collectors.toMap(entry -> (ResourceLocation) entry.getKey(), entry -> ((CTUtils.SalvageBuilder) entry.getValue()).build())));
    }

    @Method
    public static void setMobModifier(ObjectType objectType, ResourceLocation locationID, ResourceLocation mobID, Map<String, Double> modifiers) {
        switch(objectType) {
            case DIMENSION:
                {
                    Map<ResourceLocation, Map<String, Double>> data = ((LocationData) Core.get(LogicalSide.SERVER).getLoader().DIMENSION_LOADER.getData().computeIfAbsent(locationID, rl -> new LocationData())).mobModifiers();
                    data.clear();
                    data.put(mobID, modifiers);
                    break;
                }
            case BIOME:
                {
                    Map<ResourceLocation, Map<String, Double>> data = ((LocationData) Core.get(LogicalSide.SERVER).getLoader().BIOME_LOADER.getData().computeIfAbsent(locationID, rl -> new LocationData())).mobModifiers();
                    data.clear();
                    data.put(mobID, modifiers);
                }
        }
    }

    @Method
    public static void registerPerk(ResourceLocation perkID, MapData defaults, CTPerkPredicate customConditions, CTPerkFunction onStart, CTTickFunction onTick, CTPerkFunction onStop, LiteralContents description, CTDescriptionFunction status, int side) {
        BiPredicate<Player, CompoundTag> conditions = (p, c) -> customConditions.test(p, (MapData) TagToDataConverter.convert(c));
        BiFunction<Player, CompoundTag, CompoundTag> execute = (p, c) -> onStart.apply(p, (MapData) TagToDataConverter.convert(c)).getInternal();
        TriFunction<Player, CompoundTag, Integer, CompoundTag> tick = (p, c, t) -> onTick.apply(p, (MapData) TagToDataConverter.convert(c), t).getInternal();
        BiFunction<Player, CompoundTag, CompoundTag> conclude = (p, c) -> onStop.apply(p, (MapData) TagToDataConverter.convert(c)).getInternal();
        BiFunction<Player, CompoundTag, List<MutableComponent>> statusOut = (p, c) -> status.apply(p, (MapData) TagToDataConverter.convert(c)).stream().map(MutableComponent::m_237204_).toList();
        PerkSide perkSide = PerkSide.values()[side > 2 ? 2 : side];
        Perk perk = new Perk(conditions, defaults.getInternal(), execute, tick, conclude, MutableComponent.create(description), statusOut);
        APIUtils.registerPerk(perkID, perk, perkSide);
    }

    @ZenRegister
    @Document("mods/PMMO/SalvageBuilder")
    @Name("mods.pmmo.SalvageBuilder")
    public static class SalvageBuilder {

        private Map<String, Double> chancePerLevel = new HashMap();

        private Map<String, Integer> levelReq = new HashMap();

        private Map<String, Long> xpAward = new HashMap();

        private int salvageMax = 1;

        private double baseChance = 0.0;

        private double maxChance = 1.0;

        private SalvageBuilder() {
        }

        @Method
        public static CTUtils.SalvageBuilder start() {
            return new CTUtils.SalvageBuilder();
        }

        @Method
        public CTUtils.SalvageBuilder setChancePerLevel(Map<String, Double> chancePerLevel) {
            this.chancePerLevel = chancePerLevel;
            return this;
        }

        @Method
        public CTUtils.SalvageBuilder setLevelReq(Map<String, Integer> levelReq) {
            this.levelReq = levelReq;
            return this;
        }

        @Method
        public CTUtils.SalvageBuilder setXpAward(Map<String, Long> xpAward) {
            this.xpAward = xpAward;
            return this;
        }

        @Method
        public CTUtils.SalvageBuilder setSalvageMax(int max) {
            this.salvageMax = max;
            return this;
        }

        @Method
        public CTUtils.SalvageBuilder setBaseChance(double chance) {
            this.baseChance = chance;
            return this;
        }

        @Method
        public CTUtils.SalvageBuilder setMaxChance(double chance) {
            this.maxChance = chance;
            return this;
        }

        public CodecTypes.SalvageData build() {
            return new CodecTypes.SalvageData(this.chancePerLevel, this.levelReq, this.xpAward, this.salvageMax, this.baseChance, this.maxChance);
        }
    }
}