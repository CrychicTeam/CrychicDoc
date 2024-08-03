package harmonised.pmmo.api;

import com.google.common.base.Preconditions;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.PerkSide;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.api.perks.Perk;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.config.codecs.PlayerData;
import harmonised.pmmo.config.codecs.VeinData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.core.IDataStorage;
import harmonised.pmmo.util.MsLoggy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class APIUtils {

    public static final String MOB_HEALTH = "health";

    public static final String MOB_SPEED = "speed";

    public static final String MOB_DAMAGE = "damage";

    public static final String IS_CANCELLED = "is_cancelled";

    public static final String DENY_ITEM_USE = "deny_item";

    public static final String DENY_BLOCK_USE = "deny_block";

    public static final String PER_LEVEL = "per_level";

    public static final String MAX_BOOST = "max_boost";

    public static final String RATIO = "ratio";

    public static final String MODIFIER = "modifier";

    public static final String MIN_LEVEL = "min_level";

    public static final String MAX_LEVEL = "max_level";

    public static final String MILESTONES = "milestones";

    public static final String MODULUS = "per_x_level";

    public static final String CHANCE = "chance";

    public static final String COOLDOWN = "cooldown";

    public static final String DURATION = "duration";

    public static final String TARGET = "target";

    public static final String ENTITY_ID = "entity_id";

    public static final String BLOCK_POS = "block_pos";

    public static final String SKILLNAME = "skill";

    public static final String SKILL_LEVEL = "level";

    public static final String BREAK_SPEED_INPUT_VALUE = "speedIn";

    public static final String BREAK_SPEED_OUTPUT_VALUE = "speed";

    public static final String DAMAGE_TYPE_IN = "for_damage";

    public static final String DAMAGE_TYPE = "damage_type";

    public static final String DAMAGE_IN = "damageIn";

    public static final String DAMAGE_OUT = "damage";

    public static final String ATTRIBUTE = "attribute";

    public static final String JUMP_OUT = "jump_boost_output";

    public static final String STACK = "stack";

    public static final String PLAYER_ID = "player_id";

    public static final String ENCHANT_LEVEL = "enchant_level";

    public static final String ENCHANT_NAME = "enchant_name";

    public static final String AMBIENT = "ambient";

    public static final String VISIBLE = "visible";

    public static final String SHOW_ICON = "show_icon";

    public static final String EFFECTS = "effects";

    public static final String MULTIPLICATIVE = "multiplicative";

    public static final String BASE = "base";

    public static final String CHANCE_SUCCESS_MSG = "chance_message";

    public static final String SERIALIZED_AWARD_MAP = "serialized_award_map";

    public static int getLevel(String skill, Player player) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);
        return Core.get(player.m_9236_()).getData().getPlayerSkillLevel(skill, player.m_20148_());
    }

    public static void setLevel(String skill, Player player, int level) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);
        Core.get(player.m_9236_()).getData().setPlayerSkillLevel(skill, player.m_20148_(), level);
    }

    public static boolean addLevel(String skill, Player player, int levelChange) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);
        return Core.get(player.m_9236_()).getData().changePlayerSkillLevel(skill, player.m_20148_(), levelChange);
    }

    public static long getXp(String skill, Player player) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);
        return Core.get(player.m_9236_()).getData().getXpRaw(player.m_20148_(), skill);
    }

    public static void setXp(String skill, Player player, long xpRaw) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);
        Core.get(player.m_9236_()).getData().setXpRaw(player.m_20148_(), skill, xpRaw);
    }

    public static boolean addXp(String skill, Player player, long change) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);
        IDataStorage data = Core.get(player.m_9236_()).getData();
        return CoreUtils.processSkillGroupXP(Map.of(skill, change)).entrySet().stream().allMatch(entry -> data.setXpDiff(player.m_20148_(), (String) entry.getKey(), (Long) entry.getValue()));
    }

    public static Map<String, Long> getRawXpMap(Player player) {
        return Core.get(player.m_9236_()).getData().getXpMap(player.m_20148_());
    }

    public static Map<String, Integer> getAllLevels(Player player) {
        IDataStorage data = Core.get(player.m_9236_()).getData();
        return (Map<String, Integer>) getRawXpMap(player).entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> data.getLevelFromXP((Long) e.getValue())));
    }

    public static Map<String, Long> getXpAwardMap(ItemStack item, EventType type, LogicalSide side, @Nullable Player player) {
        Preconditions.checkNotNull(item);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(side);
        return Core.get(side).getExperienceAwards(type, item, player, new CompoundTag());
    }

    public static Map<String, Long> getXpAwardMap(Level level, BlockPos pos, EventType type, @Nullable Player player) {
        Preconditions.checkNotNull(level);
        Preconditions.checkNotNull(pos);
        Preconditions.checkNotNull(type);
        return Core.get(level).getExperienceAwards(type, pos, level, player, new CompoundTag());
    }

    public static Map<String, Long> getXpAwardMap(Entity entity, EventType type, LogicalSide side, @Nullable Player player) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(side);
        return Core.get(side).getExperienceAwards(type, entity, player, new CompoundTag());
    }

    public static Map<String, Long> getXpAwardMap(ObjectType oType, EventType type, ResourceLocation objectID, LogicalSide side, @Nullable Player player) {
        Preconditions.checkNotNull(oType);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(objectID);
        Preconditions.checkNotNull(side);
        return Core.get(side).getCommonXpAwardData(new HashMap(), type, objectID, player, oType, new CompoundTag());
    }

    public static Map<String, Integer> getRequirementMap(ItemStack item, ReqType type, LogicalSide side) {
        Preconditions.checkNotNull(item);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(side);
        return Core.get(side).getReqMap(type, item, true);
    }

    public static Map<String, Integer> getRequirementMap(BlockPos pos, Level level, ReqType type) {
        Preconditions.checkNotNull(pos);
        Preconditions.checkNotNull(level);
        Preconditions.checkNotNull(type);
        return Core.get(level).getReqMap(type, pos, level);
    }

    public static Map<String, Integer> getRequirementMap(Entity entity, ReqType type, LogicalSide side) {
        Preconditions.checkNotNull(entity);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(side);
        return Core.get(side).getReqMap(type, entity);
    }

    public static Map<String, Integer> getRequirementMap(ObjectType oType, ResourceLocation objectID, ReqType type, LogicalSide side) {
        Preconditions.checkNotNull(oType);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(objectID);
        Preconditions.checkNotNull(side);
        return Core.get(side).getCommonReqData(new HashMap(), oType, objectID, type, new CompoundTag());
    }

    public static void registerRequirement(ObjectType oType, ResourceLocation objectID, ReqType type, Map<String, Integer> requirements, boolean asOverride) {
        DataSource<?> raw;
        switch(oType) {
            case BIOME:
            case DIMENSION:
                raw = new LocationData();
                break;
            case ITEM:
            case BLOCK:
            case ENTITY:
                raw = new ObjectData();
                break;
            default:
                return;
        }
        raw.setReqs(type, requirements);
        registerConfiguration(asOverride, oType, objectID, raw);
    }

    public static void registerXpAward(ObjectType oType, ResourceLocation objectID, EventType type, Map<String, Long> award, boolean asOverride) {
        DataSource<?> raw;
        switch(oType) {
            case BIOME:
            case DIMENSION:
                raw = new LocationData();
                break;
            case ITEM:
            case BLOCK:
            case ENTITY:
                raw = new ObjectData();
                break;
            default:
                return;
        }
        raw.setXpValues(type, award);
        registerConfiguration(asOverride, oType, objectID, raw);
    }

    public static void registerDamageXpAward(ObjectType oType, ResourceLocation objectID, boolean isDealt, String damageType, Map<String, Long> award, boolean asOverride) {
        if (oType == ObjectType.ENTITY || oType == ObjectType.ITEM) {
            ObjectData raw = new ObjectData();
            raw.damageXpValues().put(isDealt ? EventType.DEAL_DAMAGE : EventType.RECEIVE_DAMAGE, Map.of(damageType, award));
            registerConfiguration(asOverride, oType, objectID, raw);
        }
    }

    public static void registerBonus(ObjectType oType, ResourceLocation objectID, ModifierDataType type, Map<String, Double> bonus, boolean asOverride) {
        DataSource<?> raw;
        switch(oType) {
            case BIOME:
            case DIMENSION:
                raw = new LocationData();
                break;
            case ITEM:
                raw = new ObjectData();
                break;
            case BLOCK:
            case ENTITY:
            default:
                return;
            case PLAYER:
                raw = new PlayerData();
        }
        raw.setBonuses(type, bonus);
        registerConfiguration(asOverride, oType, objectID, raw);
    }

    public static void registerNegativeEffect(ObjectType oType, ResourceLocation objectID, Map<ResourceLocation, Integer> effects, boolean asOverride) {
        DataSource<?> raw;
        switch(oType) {
            case BIOME:
            case DIMENSION:
                raw = new LocationData();
                break;
            case ITEM:
                raw = new ObjectData();
                break;
            default:
                return;
        }
        raw.setNegativeEffects(effects);
        registerConfiguration(asOverride, oType, objectID, raw);
    }

    public static void registerPositiveEffect(ObjectType oType, ResourceLocation objectID, Map<ResourceLocation, Integer> effects, boolean asOverride) {
        DataSource<?> raw;
        switch(oType) {
            case BIOME:
            case DIMENSION:
                raw = new LocationData();
                break;
            case ITEM:
                raw = new ObjectData();
                break;
            default:
                return;
        }
        raw.setPositiveEffects(effects);
        registerConfiguration(asOverride, oType, objectID, raw);
    }

    public static void registerSalvage(ResourceLocation item, Map<ResourceLocation, APIUtils.SalvageBuilder> salvage, boolean asOverride) {
        ObjectData raw = new ObjectData();
        raw.salvage().putAll((Map) salvage.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> ((APIUtils.SalvageBuilder) entry.getValue()).build())));
        registerConfiguration(asOverride, ObjectType.ITEM, item, raw);
    }

    public static void registerVeinData(ObjectType oType, ResourceLocation objectID, Optional<Integer> chargeCap, Optional<Double> chargeRate, Optional<Integer> consumeAmount, boolean asOverride) {
        if (oType == ObjectType.ITEM || oType == ObjectType.BLOCK) {
            VeinData data = new VeinData(chargeCap, chargeRate, consumeAmount);
            ObjectData raw = new ObjectData();
            raw.veinData().combine(data);
            registerConfiguration(asOverride, oType, objectID, raw);
        }
    }

    public static void registerMobModifier(ObjectType oType, ResourceLocation locationID, Map<ResourceLocation, Map<String, Double>> mob_modifiers, boolean asOverride) {
        if (oType == ObjectType.BIOME || oType == ObjectType.DIMENSION) {
            LocationData raw = new LocationData();
            raw.mobModifiers().putAll(mob_modifiers);
            registerConfiguration(asOverride, oType, locationID, raw);
        }
    }

    private static void registerConfiguration(boolean asOverride, ObjectType oType, ResourceLocation objectID, DataSource<?> data) {
        if (asOverride) {
            Core.get(LogicalSide.SERVER).getLoader().getLoader(oType).registerOverride(objectID, data);
        } else {
            Core.get(LogicalSide.SERVER).getLoader().getLoader(oType).registerDefault(objectID, data);
        }
    }

    public static void registerActionPredicate(ResourceLocation res, ReqType reqType, BiPredicate<Player, ItemStack> pred) {
        Core.get(LogicalSide.SERVER).getPredicateRegistry().registerPredicate(res, reqType, pred);
    }

    public static void registerBreakPredicate(ResourceLocation res, ReqType reqType, BiPredicate<Player, BlockEntity> pred) {
        Core.get(LogicalSide.SERVER).getPredicateRegistry().registerBreakPredicate(res, reqType, pred);
    }

    public static void registerEntityPredicate(ResourceLocation res, ReqType reqType, BiPredicate<Player, Entity> pred) {
        Core.get(LogicalSide.SERVER).getPredicateRegistry().registerEntityPredicate(res, reqType, pred);
    }

    public static void registerItemRequirementTooltipData(ResourceLocation res, ReqType reqType, Function<ItemStack, Map<String, Integer>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerItemRequirementTooltipData(res, reqType, func);
    }

    public static void registerBlockRequirementTooltipData(ResourceLocation res, ReqType reqType, Function<BlockEntity, Map<String, Integer>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerBlockRequirementTooltipData(res, reqType, func);
    }

    public static void registerEntityRequirementTooltipData(ResourceLocation res, ReqType reqType, Function<Entity, Map<String, Integer>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerEntityRequirementTooltipData(res, reqType, func);
    }

    public static void registerItemXpGainTooltipData(ResourceLocation res, EventType eventType, Function<ItemStack, Map<String, Long>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerItemXpGainTooltipData(res, eventType, func);
    }

    public static void registerBlockXpGainTooltipData(ResourceLocation res, EventType eventType, Function<BlockEntity, Map<String, Long>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerBlockXpGainTooltipData(res, eventType, func);
    }

    public static void registerEntityXpGainTooltipData(ResourceLocation res, EventType eventType, Function<Entity, Map<String, Long>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerEntityXpGainTooltipData(res, eventType, func);
    }

    public static void registerItemBonusData(ResourceLocation res, ModifierDataType type, Function<ItemStack, Map<String, Double>> func) {
        Core.get(LogicalSide.SERVER).getTooltipRegistry().registerItemBonusTooltipData(res, type, func);
    }

    public static void registerLevelProvider(BiFunction<String, Integer, Integer> provider, FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Core.get(LogicalSide.SERVER).getLevelProvider().registerLevelProvider(provider);
            Core.get(LogicalSide.CLIENT).getLevelProvider().registerLevelProvider(provider);
        });
    }

    public static void registerListener(@NonNull ResourceLocation listenerID, @NonNull EventType eventType, @NonNull BiFunction<? super Event, CompoundTag, CompoundTag> executeOnTrigger) {
        Core.get(LogicalSide.SERVER).getEventTriggerRegistry().registerListener(listenerID, eventType, executeOnTrigger);
    }

    public static void registerPerk(@NonNull ResourceLocation perkID, @NonNull Perk perk, @NonNull PerkSide side) {
        switch(side) {
            case SERVER:
                Core.get(LogicalSide.SERVER).getPerkRegistry().registerPerk(perkID, perk);
                Core.get(LogicalSide.CLIENT).getPerkRegistry().registerClientClone(perkID, perk);
                break;
            case CLIENT:
                Core.get(LogicalSide.CLIENT).getPerkRegistry().registerPerk(perkID, perk);
                break;
            case BOTH:
                Core.get(LogicalSide.SERVER).getPerkRegistry().registerPerk(perkID, perk);
                Core.get(LogicalSide.CLIENT).getPerkRegistry().registerPerk(perkID, perk);
        }
    }

    public static CompoundTag serializeAwardMap(Map<String, Long> awardMap) {
        return (CompoundTag) CodecTypes.LONG_CODEC.encodeStart(NbtOps.INSTANCE, awardMap).resultOrPartial(str -> MsLoggy.ERROR.log(MsLoggy.LOG_CODE.API, "Error Serializing Award Map Via API: {}", str)).orElse(new CompoundTag());
    }

    public static class SalvageBuilder {

        private Map<String, Double> chancePerLevel = new HashMap();

        private Map<String, Integer> levelReq = new HashMap();

        private Map<String, Long> xpAward = new HashMap();

        private int salvageMax = 1;

        private double baseChance = 0.0;

        private double maxChance = 1.0;

        private SalvageBuilder() {
        }

        public static APIUtils.SalvageBuilder start() {
            return new APIUtils.SalvageBuilder();
        }

        public APIUtils.SalvageBuilder setChancePerLevel(Map<String, Double> chancePerLevel) {
            this.chancePerLevel = chancePerLevel;
            return this;
        }

        public APIUtils.SalvageBuilder setLevelReq(Map<String, Integer> levelReq) {
            this.levelReq = levelReq;
            return this;
        }

        public APIUtils.SalvageBuilder setXpAward(Map<String, Long> xpAward) {
            this.xpAward = xpAward;
            return this;
        }

        public APIUtils.SalvageBuilder setSalvageMax(int max) {
            this.salvageMax = max;
            return this;
        }

        public APIUtils.SalvageBuilder setBaseChance(double chance) {
            this.baseChance = chance;
            return this;
        }

        public APIUtils.SalvageBuilder setMaxChance(double chance) {
            this.maxChance = chance;
            return this;
        }

        public CodecTypes.SalvageData build() {
            return new CodecTypes.SalvageData(this.chancePerLevel, this.levelReq, this.xpAward, this.salvageMax, this.baseChance, this.maxChance);
        }
    }
}