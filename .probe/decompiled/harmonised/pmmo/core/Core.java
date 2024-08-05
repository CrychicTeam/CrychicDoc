package harmonised.pmmo.core;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.client.utils.DataMirror;
import harmonised.pmmo.compat.curios.CurioCompat;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.EnhancementsData;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.config.readers.CoreLoader;
import harmonised.pmmo.features.anticheese.CheeseTracker;
import harmonised.pmmo.features.autovalues.AutoValueConfig;
import harmonised.pmmo.features.autovalues.AutoValues;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_ClearData;
import harmonised.pmmo.registry.EventTriggerRegistry;
import harmonised.pmmo.registry.LevelRegistry;
import harmonised.pmmo.registry.PerkRegistry;
import harmonised.pmmo.registry.PredicateRegistry;
import harmonised.pmmo.registry.TooltipRegistry;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.storage.PmmoSavedData;
import harmonised.pmmo.util.Functions;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Core {

    private static final Map<LogicalSide, Function<LogicalSide, Core>> INSTANCES = Map.of(LogicalSide.CLIENT, Functions.memoize(Core::new), LogicalSide.SERVER, Functions.memoize(Core::new));

    private final CoreLoader loader;

    private final PredicateRegistry predicates;

    private final EventTriggerRegistry eventReg;

    private final TooltipRegistry tooltips;

    private final PerkRegistry perks;

    private final LevelRegistry lvlProvider;

    private final IDataStorage data;

    private final LogicalSide side;

    private ResourceLocation playerID = new ResourceLocation("player");

    private final Map<UUID, BlockPos> markers = new HashMap();

    private Core(LogicalSide side) {
        this.loader = new CoreLoader();
        this.predicates = new PredicateRegistry();
        this.eventReg = new EventTriggerRegistry();
        this.tooltips = new TooltipRegistry();
        this.perks = new PerkRegistry();
        this.lvlProvider = new LevelRegistry();
        this.data = (IDataStorage) (side.equals(LogicalSide.SERVER) ? new PmmoSavedData() : new DataMirror());
        this.side = side;
    }

    public static Core get(LogicalSide side) {
        return (Core) ((Function) INSTANCES.get(side)).apply(side);
    }

    public static Core get(Level level) {
        return get(level.isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER);
    }

    public void resetDataForReload() {
        this.tooltips.clearRegistry();
        if (this.side.equals(LogicalSide.SERVER)) {
            if (ServerLifecycleHooks.getCurrentServer() == null) {
                return;
            }
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                Networking.sendToClient(new CP_ClearData(), player);
            }
        }
    }

    public CoreLoader getLoader() {
        return this.loader;
    }

    public PredicateRegistry getPredicateRegistry() {
        return this.predicates;
    }

    public EventTriggerRegistry getEventTriggerRegistry() {
        return this.eventReg;
    }

    public TooltipRegistry getTooltipRegistry() {
        return this.tooltips;
    }

    public PerkRegistry getPerkRegistry() {
        return this.perks;
    }

    public LevelRegistry getLevelProvider() {
        return this.lvlProvider;
    }

    public IDataStorage getData() {
        return this.data.get();
    }

    public LogicalSide getSide() {
        return this.side;
    }

    public void awardXP(List<ServerPlayer> players, Map<String, Long> xpValues) {
        CoreUtils.processSkillGroupXP(xpValues);
        new HashMap(xpValues).forEach((skill, value) -> xpValues.put(skill, (long) ((double) value.longValue() * (Double) Config.SKILL_MODIFIERS.get().getOrDefault(skill, Config.GLOBAL_MODIFIER.get()))));
        for (int i = 0; i < players.size(); i++) {
            if (!(players.get(i) instanceof FakePlayer) && !((ServerPlayer) players.get(i)).m_21224_()) {
                for (Entry<String, Long> award : xpValues.entrySet()) {
                    long xpAward = (Long) award.getValue();
                    if (players.size() > 1) {
                        xpAward = Double.valueOf((double) xpAward * Config.PARTY_BONUS.get() * (double) players.size()).longValue();
                    }
                    this.getData().setXpDiff(((ServerPlayer) players.get(i)).m_20148_(), (String) award.getKey(), xpAward / (long) players.size());
                }
            }
        }
    }

    public Map<String, Long> getExperienceAwards(EventType type, ItemStack stack, Player player, CompoundTag dataIn) {
        ResourceLocation itemID = RegistryUtil.getId(stack);
        dataIn.merge(TagUtils.stackTag(stack));
        Map<String, Long> xpGains = (Map<String, Long>) (this.tooltips.xpGainTooltipExists(itemID, type) ? this.tooltips.getItemXpGainTooltipData(itemID, type, stack) : new HashMap());
        return this.getCommonXpAwardData(xpGains, type, itemID, player, ObjectType.ITEM, dataIn);
    }

    public Map<String, Long> getExperienceAwards(MobEffectInstance mei, Player player, CompoundTag dataIn) {
        ResourceLocation effectID = RegistryUtil.getId(mei.getEffect());
        EnhancementsData data = (EnhancementsData) this.loader.getLoader(ObjectType.EFFECT).getData().get(effectID);
        Map<String, Long> xpGains = new HashMap();
        if (data != null) {
            ((Map) data.skillArray().getOrDefault(mei.getAmplifier() + 1, new HashMap())).forEach((skill, value) -> xpGains.put(skill, value.longValue()));
        }
        return this.getCommonXpAwardData(xpGains, EventType.EFFECT, effectID, player, ObjectType.EFFECT, dataIn);
    }

    public Map<String, Long> getExperienceAwards(EventType type, BlockPos pos, Level level, Player player, CompoundTag dataIn) {
        ResourceLocation res = RegistryUtil.getId(level.getBlockState(pos));
        BlockEntity tile = level.getBlockEntity(pos);
        dataIn.merge(TagUtils.tileTag(tile));
        dataIn.put("state", TagUtils.stateTag(level.getBlockState(pos)));
        Map<String, Long> xpGains = (Map<String, Long>) (tile != null && this.tooltips.xpGainTooltipExists(res, type) ? this.tooltips.getBlockXpGainTooltipData(res, type, tile) : new HashMap());
        return this.getCommonXpAwardData(xpGains, type, res, player, ObjectType.BLOCK, dataIn);
    }

    public Map<String, Long> getExperienceAwards(EventType type, Entity entity, Player player, CompoundTag dataIn) {
        ResourceLocation entityID = entity.getType().equals(EntityType.PLAYER) ? this.playerID : RegistryUtil.getId(entity);
        dataIn.merge(TagUtils.entityTag(entity));
        Map<String, Long> xpGains = (Map<String, Long>) (this.tooltips.xpGainTooltipExists(entityID, type) ? this.tooltips.getEntityXpGainTooltipData(entityID, type, entity) : new HashMap());
        return this.getCommonXpAwardData(xpGains, type, entityID, player, ObjectType.ENTITY, dataIn);
    }

    public Map<String, Long> getCommonXpAwardData(Map<String, Long> xpGains, EventType type, ResourceLocation objectID, Player player, ObjectType oType, CompoundTag tag) {
        String[] loggables = new String[4];
        if (xpGains.isEmpty()) {
            xpGains = CoreUtils.mergeXpMapsWithSummateCondition((Map<String, Long>) (tag.contains("serialized_award_map") ? CoreUtils.deserializeAwardMap(tag.getCompound("serialized_award_map")) : new HashMap()), this.getObjectExperienceMap(oType, objectID, type, tag));
            if (AutoValueConfig.ENABLE_AUTO_VALUES.get() && xpGains.isEmpty()) {
                xpGains = AutoValues.getExperienceAward(type, objectID, oType);
            }
        }
        loggables[0] = type.name();
        loggables[1] = MsLoggy.mapToString(xpGains);
        if (player != null && !(player instanceof FakePlayer)) {
            CoreUtils.applyXpModifiers(xpGains, this.getConsolidatedModifierMap(player));
        }
        loggables[2] = MsLoggy.mapToString(xpGains);
        CheeseTracker.applyAntiCheese(type, objectID, player, xpGains);
        loggables[3] = MsLoggy.mapToString(xpGains);
        MsLoggy.INFO.log(MsLoggy.LOG_CODE.XP, "XP: {} base:{}, afterBonus:{}, afterAntiCheese:{}", loggables);
        return xpGains;
    }

    public Map<String, Long> getObjectExperienceMap(ObjectType type, ResourceLocation objectID, EventType eventType, CompoundTag tag) {
        DataSource<?> data = (DataSource<?>) this.loader.getLoader(type).getData().get(objectID);
        return new HashMap((Map) (data != null ? MsLoggy.DEBUG.logAndReturn(data.getXpValues(eventType, tag), MsLoggy.LOG_CODE.DATA, "getObjectExperienceMap= {}") : new HashMap()));
    }

    public Map<String, Double> getObjectModifierMap(ObjectType type, ResourceLocation objectID, ModifierDataType modifierType, CompoundTag tag) {
        DataSource<?> data = (DataSource<?>) this.loader.getLoader(type).getData().get(objectID);
        return new HashMap((Map) (data != null ? data.getBonuses(modifierType, tag) : new HashMap()));
    }

    public Map<String, Double> getConsolidatedModifierMap(Player player) {
        Map<String, Double> mapOut = new HashMap();
        if (player instanceof FakePlayer) {
            return mapOut;
        } else {
            ResourceLocation biomeID = RegistryUtil.getId(player.m_9236_().m_204166_(player.m_20183_()));
            for (Entry<String, Double> modMap : this.getObjectModifierMap(ObjectType.BIOME, biomeID, ModifierDataType.BIOME, new CompoundTag()).entrySet()) {
                mapOut.merge((String) modMap.getKey(), (Double) modMap.getValue(), (o, n) -> o + (n - 1.0));
            }
            ResourceLocation dimensionID = player.m_9236_().dimension().location();
            for (Entry<String, Double> modMap : this.getObjectModifierMap(ObjectType.DIMENSION, dimensionID, ModifierDataType.DIMENSION, new CompoundTag()).entrySet()) {
                mapOut.merge((String) modMap.getKey(), (Double) modMap.getValue(), (o, n) -> o + (n - 1.0));
            }
            new HashMap();
            for (ItemStack stack : List.of(player.m_21206_(), player.m_21205_())) {
                ResourceLocation itemID = RegistryUtil.getId(stack);
                Map<String, Double> var13 = this.tooltips.bonusTooltipExists(itemID, ModifierDataType.HELD) ? this.tooltips.getBonusTooltipData(itemID, ModifierDataType.HELD, stack) : this.getObjectModifierMap(ObjectType.ITEM, itemID, ModifierDataType.HELD, TagUtils.stackTag(stack));
                for (Entry<String, Double> modMap : var13.entrySet()) {
                    mapOut.merge((String) modMap.getKey(), (Double) modMap.getValue(), (o, n) -> o + (n - 1.0));
                }
            }
            List<ItemStack> wornItems = new ArrayList();
            player.getArmorSlots().forEach(wornItems::add);
            if (CurioCompat.hasCurio) {
                CurioCompat.getItems(player).forEach(wornItems::add);
            }
            wornItems.forEach(stackx -> {
                ResourceLocation itemIDx = RegistryUtil.getId(stackx);
                Map<String, Double> modifers = this.tooltips.bonusTooltipExists(itemIDx, ModifierDataType.WORN) ? this.tooltips.getBonusTooltipData(itemIDx, ModifierDataType.WORN, stackx) : this.getObjectModifierMap(ObjectType.ITEM, itemIDx, ModifierDataType.WORN, stackx.getTag() == null ? new CompoundTag() : stackx.getTag());
                for (Entry<String, Double> modMapx : modifers.entrySet()) {
                    mapOut.merge((String) modMapx.getKey(), (Double) modMapx.getValue(), (o, n) -> o + (n - 1.0));
                }
            });
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.XP, "Modifier Map: {}", MsLoggy.mapToString(mapOut));
            return this.loader.PLAYER_LOADER.getData(new ResourceLocation(player.m_20148_().toString())).mergeWithPlayerBonuses(CoreUtils.processSkillGroupBonus(mapOut));
        }
    }

    public boolean isActionPermitted(ReqType type, ItemStack stack, Player player) {
        if (!(player instanceof FakePlayer) && Config.reqEnabled(type).get() && !this.getLoader().PLAYER_LOADER.getData(new ResourceLocation(player.m_20148_().toString())).ignoreReq()) {
            ResourceLocation itemID = RegistryUtil.getId(stack.getItem());
            return this.predicates.predicateExists(itemID, type) ? this.predicates.checkPredicateReq(player, stack, type) : this.doesPlayerMeetReq(player.m_20148_(), this.getReqMap(type, stack, false));
        } else {
            return true;
        }
    }

    public boolean isActionPermitted(ReqType type, BlockPos pos, Player player) {
        if (!(player instanceof FakePlayer) && Config.reqEnabled(type).get() && !this.getLoader().PLAYER_LOADER.getData(new ResourceLocation(player.m_20148_().toString())).ignoreReq()) {
            BlockEntity tile = player.m_9236_().getBlockEntity(pos);
            ResourceLocation res = RegistryUtil.getId(player.m_9236_().getBlockState(pos));
            return tile != null && this.predicates.predicateExists(res, type) ? this.predicates.checkPredicateReq(player, tile, type) : this.doesPlayerMeetReq(player.m_20148_(), this.getReqMap(type, pos, player.m_9236_()));
        } else {
            return true;
        }
    }

    public boolean isActionPermitted(ReqType type, Entity entity, Player player) {
        if (!(player instanceof FakePlayer) && Config.reqEnabled(type).get() && !this.getLoader().PLAYER_LOADER.getData(new ResourceLocation(player.m_20148_().toString())).ignoreReq()) {
            ResourceLocation entityID = entity.getType().equals(EntityType.PLAYER) ? this.playerID : RegistryUtil.getId(entity);
            return this.predicates.predicateExists(entityID, type) ? this.predicates.checkPredicateReq(player, entity, type) : this.doesPlayerMeetReq(player.m_20148_(), this.getReqMap(type, entity));
        } else {
            return true;
        }
    }

    public boolean isActionPermitted(ReqType type, Holder<Biome> biome, Player player) {
        if (type != ReqType.TRAVEL) {
            return false;
        } else {
            return !(player instanceof FakePlayer) && Config.reqEnabled(type).get() && !this.getLoader().PLAYER_LOADER.getData(new ResourceLocation(player.m_20148_().toString())).ignoreReq() ? this.doesPlayerMeetReq(player.m_20148_(), this.getObjectSkillMap(ObjectType.BIOME, RegistryUtil.getId(biome), type, new CompoundTag())) : true;
        }
    }

    public boolean isActionPermitted(ReqType type, ResourceKey<Level> dimension, Player player) {
        if (type != ReqType.TRAVEL) {
            return false;
        } else {
            return !(player instanceof FakePlayer) && Config.reqEnabled(type).get() && !this.getLoader().PLAYER_LOADER.getData(new ResourceLocation(player.m_20148_().toString())).ignoreReq() ? this.doesPlayerMeetReq(player.m_20148_(), this.getObjectSkillMap(ObjectType.DIMENSION, dimension.location(), type, new CompoundTag())) : true;
        }
    }

    public boolean doesPlayerMeetReq(UUID playerID, Map<String, Integer> requirements) {
        CoreUtils.processSkillGroupReqs(requirements);
        for (Entry<String, Integer> req : requirements.entrySet()) {
            int skillLevel = this.getData().getPlayerSkillLevel((String) req.getKey(), playerID);
            if (((SkillData) SkillsConfig.SKILLS.get().getOrDefault(req.getKey(), SkillData.Builder.getDefault())).isSkillGroup()) {
                SkillData skillData = (SkillData) SkillsConfig.SKILLS.get().get(req.getKey());
                if (skillData.getUseTotalLevels()) {
                    int total = (Integer) skillData.getGroup().keySet().stream().map(skill -> this.getData().getPlayerSkillLevel(skill, playerID)).collect(Collectors.summingInt(Integer::intValue));
                    if ((Integer) req.getValue() > total) {
                        return false;
                    }
                }
            } else if ((Integer) req.getValue() > skillLevel) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Integer> getObjectSkillMap(ObjectType type, ResourceLocation objectID, ReqType reqType, CompoundTag nbt) {
        DataSource<?> data = (DataSource<?>) this.loader.getLoader(type).getData().get(objectID);
        return new HashMap((Map) (data != null ? data.getReqs(reqType, nbt) : new HashMap()));
    }

    public Map<String, Integer> getReqMap(ReqType reqType, ItemStack stack, boolean ignoreEnchants) {
        ResourceLocation itemID = RegistryUtil.getId(stack);
        Map<String, Integer> reqMap = (Map<String, Integer>) (ignoreEnchants ? new HashMap() : this.getEnchantReqs(stack));
        if (this.tooltips.requirementTooltipExists(itemID, reqType)) {
            this.tooltips.getItemRequirementTooltipData(itemID, reqType, stack).forEach((skill, lvl) -> reqMap.merge(skill, lvl, Integer::max));
        }
        return this.getCommonReqData(reqMap, ObjectType.ITEM, itemID, reqType, TagUtils.stackTag(stack));
    }

    public Map<String, Integer> getEnchantReqs(ItemStack stack) {
        Map<String, Integer> outMap = new HashMap();
        if (stack.isEnchanted() && Config.reqEnabled(ReqType.USE_ENCHANTMENT).get()) {
            for (Entry<Enchantment, Integer> enchant : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                this.getEnchantmentReqs(RegistryUtil.getId((Enchantment) enchant.getKey()), (Integer) enchant.getValue() - 1).forEach((skill, level) -> outMap.merge(skill, level, Integer::max));
            }
            return outMap;
        } else {
            return outMap;
        }
    }

    public Map<String, Integer> getReqMap(ReqType reqType, Entity entity) {
        ResourceLocation entityID = entity.getType().equals(EntityType.PLAYER) ? new ResourceLocation("minecraft:player") : RegistryUtil.getId(entity);
        Map<String, Integer> reqMap = (Map<String, Integer>) (this.tooltips.requirementTooltipExists(entityID, reqType) ? this.tooltips.getEntityRequirementTooltipData(entityID, reqType, entity) : new HashMap());
        return this.getCommonReqData(reqMap, ObjectType.ENTITY, entityID, reqType, TagUtils.entityTag(entity));
    }

    public Map<String, Integer> getReqMap(ReqType reqType, BlockPos pos, Level level) {
        BlockEntity tile = level.getBlockEntity(pos);
        ResourceLocation blockID = RegistryUtil.getId(level.getBlockState(pos));
        Map<String, Integer> reqMap = (Map<String, Integer>) (tile != null && this.tooltips.requirementTooltipExists(blockID, reqType) ? this.tooltips.getBlockRequirementTooltipData(blockID, reqType, tile) : new HashMap());
        CompoundTag dataIn = TagUtils.tileTag(tile);
        dataIn.put("state", TagUtils.stateTag(level.getBlockState(pos)));
        return this.getCommonReqData(reqMap, ObjectType.BLOCK, blockID, reqType, dataIn);
    }

    public Map<String, Integer> getCommonReqData(Map<String, Integer> reqsIn, ObjectType oType, ResourceLocation objectID, ReqType type, CompoundTag tag) {
        if (reqsIn.isEmpty()) {
            reqsIn = this.getObjectSkillMap(oType, objectID, type, tag);
            if (AutoValueConfig.ENABLE_AUTO_VALUES.get() && reqsIn.isEmpty()) {
                reqsIn = AutoValues.getRequirements(type, objectID, oType);
            }
        }
        return CoreUtils.processSkillGroupReqs(reqsIn);
    }

    public Map<String, Integer> getEnchantmentReqs(ResourceLocation enchantID, int enchantLvl) {
        return (Map<String, Integer>) ((EnhancementsData) this.loader.getLoader(ObjectType.ENCHANTMENT).getData(enchantID)).skillArray().getOrDefault(enchantLvl, new HashMap());
    }

    public void getSalvage(ServerPlayer player) {
        boolean salvageMainHand = !player.m_21205_().isEmpty();
        boolean salvageOffHand = !salvageMainHand && !player.m_21206_().isEmpty();
        ItemStack salvageItem = salvageMainHand ? player.m_21205_() : (salvageOffHand ? player.m_21206_() : ItemStack.EMPTY);
        if (salvageItem != ItemStack.EMPTY && !salvageItem.is(Items.AIR)) {
            if (this.loader.ITEM_LOADER.getData().containsKey(RegistryUtil.getId(salvageItem))) {
                Map<String, Long> playerXp = this.getData().getXpMap(player.m_20148_());
                Map<String, Long> xpAwards = new HashMap();
                boolean validAttempt = false;
                label87: for (Entry<ResourceLocation, CodecTypes.SalvageData> result : this.loader.ITEM_LOADER.getData(RegistryUtil.getId(salvageItem)).salvage().entrySet()) {
                    for (Entry<String, Integer> skill : ((CodecTypes.SalvageData) result.getValue()).levelReq().entrySet()) {
                        if ((Integer) skill.getValue() > get(LogicalSide.SERVER).getData().getLevelFromXP((Long) playerXp.getOrDefault(skill.getKey(), 0L))) {
                            continue label87;
                        }
                    }
                    validAttempt = true;
                    double base = ((CodecTypes.SalvageData) result.getValue()).baseChance();
                    double max = ((CodecTypes.SalvageData) result.getValue()).maxChance();
                    double bonus = 0.0;
                    for (Entry<String, Double> skillx : ((CodecTypes.SalvageData) result.getValue()).chancePerLevel().entrySet()) {
                        bonus += skillx.getValue() * (double) get(LogicalSide.SERVER).getData().getLevelFromXP((Long) playerXp.getOrDefault(skillx.getKey(), 0L));
                    }
                    for (int i = 0; i < ((CodecTypes.SalvageData) result.getValue()).salvageMax(); i++) {
                        if (player.m_217043_().nextDouble() < Math.min(max, base + bonus)) {
                            player.drop(new ItemStack(ForgeRegistries.ITEMS.getValue((ResourceLocation) result.getKey())), false, true);
                            for (Entry<String, Long> award : ((CodecTypes.SalvageData) result.getValue()).xpAward().entrySet()) {
                                xpAwards.merge((String) award.getKey(), (Long) award.getValue(), (o, n) -> o + n);
                            }
                        }
                    }
                }
                if (validAttempt) {
                    if (salvageMainHand) {
                        player.m_21205_().shrink(1);
                    }
                    if (salvageOffHand) {
                        player.m_21206_().shrink(1);
                    }
                    List<ServerPlayer> party = PartyUtils.getPartyMembersInRange(player);
                    this.awardXP(party, xpAwards);
                } else {
                    player.sendSystemMessage(LangProvider.DENIAL_SALVAGE.asComponent(salvageItem.getDisplayName()));
                }
            }
        }
    }

    public void setMarkedPos(UUID playerID, BlockPos pos) {
        BlockPos finalPos = pos.equals(this.getMarkedPos(playerID)) ? BlockPos.ZERO : pos;
        this.markers.put(playerID, finalPos);
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Player " + playerID.toString() + " Marked Pos: " + finalPos.toString());
    }

    public BlockPos getMarkedPos(UUID playerID) {
        return (BlockPos) this.markers.getOrDefault(playerID, BlockPos.ZERO);
    }

    public int getBlockConsume(Block block) {
        return (Integer) this.loader.BLOCK_LOADER.getData(RegistryUtil.getId(block)).veinData().consumeAmount.orElseGet(() -> Config.REQUIRE_SETTING.get() ? -1 : Config.DEFAULT_CONSUME.get());
    }
}