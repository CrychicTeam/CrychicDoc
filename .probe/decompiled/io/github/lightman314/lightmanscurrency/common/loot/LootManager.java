package io.github.lightman314.lightmanscurrency.common.loot;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.StringListOption;
import io.github.lightman314.lightmanscurrency.api.events.DroplistConfigGenerator;
import io.github.lightman314.lightmanscurrency.common.loot.modifier.ILootModifier;
import io.github.lightman314.lightmanscurrency.common.loot.tiers.ChestPoolLevel;
import io.github.lightman314.lightmanscurrency.common.loot.tiers.EntityPoolLevel;
import io.github.lightman314.lightmanscurrency.integration.alexsmobs.LCAlexsMobs;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber
public class LootManager {

    public static final LootContextParamSet ENTITY_PARAMS = new LootContextParamSet.Builder().optional(LootContextParams.KILLER_ENTITY).build();

    private static final List<ILootModifier> LOOT_MODIFIERS = new ArrayList();

    public static void init() {
        LCConfig.COMMON.addListener(LootManager::debugLootConfigs);
    }

    public static void addLootModifier(@Nonnull ILootModifier modifier) {
        if (!LOOT_MODIFIERS.contains(modifier)) {
            LOOT_MODIFIERS.add(modifier);
        }
    }

    public static void registerDroplistListeners() {
        DroplistConfigGenerator.registerEntityListener(LootManager::AddDefaultEntityEntries);
        DroplistConfigGenerator.registerChestListener(LootManager::AddDefaultChestEntries);
        LCAlexsMobs.registerDroplistListeners();
    }

    public static MobSpawnType deserializeSpawnReason(String reasonString) {
        return deserializeSpawnReason(reasonString, MobSpawnType.NATURAL);
    }

    public static MobSpawnType deserializeSpawnReason(String reasonString, MobSpawnType defaultReason) {
        for (MobSpawnType reason : MobSpawnType.values()) {
            if (reason.toString().contentEquals(reasonString)) {
                return reason;
            }
        }
        LightmansCurrency.LogWarning("Reason string \"" + reasonString + "\" could not be properly deserialized. Returning the default spawn reason.");
        return defaultReason;
    }

    public static void AddDefaultEntityEntries(DroplistConfigGenerator.Entity event) {
        switch(event.getTier()) {
            case T1:
                event.addVanillaEntry("slime");
                event.addVanillaEntry("silverfish");
                break;
            case T2:
                event.addVanillaEntry("zombie");
                event.addVanillaEntry("skeleton");
                event.addVanillaEntry("creeper");
                event.addVanillaEntry("spider");
                event.addVanillaEntry("cave_spider");
                event.addVanillaEntry("husk");
                event.addVanillaEntry("stray");
                event.addVanillaEntry("zombie_villager");
                event.addVanillaEntry("drowned");
                break;
            case T3:
                event.addVanillaEntry("guardian");
                event.addVanillaEntry("elder_guardian");
                event.addVanillaEntry("phantom");
                event.addVanillaEntry("blaze");
                event.addVanillaEntry("ghast");
                event.addVanillaEntry("hoglin");
                event.addVanillaEntry("piglin_brute");
                event.addVanillaEntry("piglin");
                event.addVanillaEntry("zombified_piglin");
                break;
            case T4:
                event.addVanillaEntry("enderman");
                event.addVanillaEntry("shulker");
                event.addTag("raiders");
                break;
            case T5:
                event.addVanillaEntry("wither_skeleton");
                break;
            case BOSS_T4:
                event.addVanillaEntry("warden");
                break;
            case BOSS_T5:
                event.addVanillaEntry("ender_dragon");
                break;
            case BOSS_T6:
                event.addVanillaEntry("wither");
        }
    }

    public static void AddDefaultChestEntries(DroplistConfigGenerator.Chest event) {
        LightmansCurrency.LogDebug("Adding default vanilla chest entries of tier '" + event.getTier().toString() + "'");
        switch(event.getTier()) {
            case T1:
                event.addVanillaEntry("underwater_ruin_small");
                event.addVanillaEntry("underwater_ruin_big");
                break;
            case T3:
                event.addVanillaEntry("jungle_temple");
                event.addVanillaEntry("nether_bridge");
                event.addVanillaEntry("simple_dungeon");
                event.addVanillaEntry("ruined_portal");
                break;
            case T4:
                event.addVanillaEntry("stronghold_crossing");
                event.addVanillaEntry("stronghold_corridor");
                event.addVanillaEntry("stronghold_library");
                event.addVanillaEntry("ancient_city");
                break;
            case T5:
                event.addVanillaEntry("buried_treasure");
                event.addVanillaEntry("bastion_hoglin_stable");
                event.addVanillaEntry("bastion_bridge");
                event.addVanillaEntry("bastion_other");
                event.addVanillaEntry("bastion_treasure");
                event.addVanillaEntry("end_city_treasure");
        }
    }

    private static String getValueList(StringListOption config) {
        StringBuilder buffer = new StringBuilder();
        for (String value : (List) config.get()) {
            if (!buffer.isEmpty()) {
                buffer.append(", ");
            }
            buffer.append("\"").append(value).append("\"");
        }
        return buffer.toString();
    }

    public static void debugLootConfigs() {
        LightmansCurrency.LogDebug("Lightman's Currency common configs have been loaded. Coin loot values are as follows.");
        LightmansCurrency.LogDebug("Chest T1: " + getValueList(LCConfig.COMMON.chestDropsT1));
        LightmansCurrency.LogDebug("Chest T2: " + getValueList(LCConfig.COMMON.chestDropsT2));
        LightmansCurrency.LogDebug("Chest T3: " + getValueList(LCConfig.COMMON.chestDropsT3));
        LightmansCurrency.LogDebug("Chest T4: " + getValueList(LCConfig.COMMON.chestDropsT4));
        LightmansCurrency.LogDebug("Chest T5: " + getValueList(LCConfig.COMMON.chestDropsT5));
        LightmansCurrency.LogDebug("Chest T6: " + getValueList(LCConfig.COMMON.chestDropsT6));
        LightmansCurrency.LogDebug("Entity T1: " + getValueList(LCConfig.COMMON.entityDropsT1));
        LightmansCurrency.LogDebug("Entity T2: " + getValueList(LCConfig.COMMON.entityDropsT2));
        LightmansCurrency.LogDebug("Entity T3: " + getValueList(LCConfig.COMMON.entityDropsT3));
        LightmansCurrency.LogDebug("Entity T4: " + getValueList(LCConfig.COMMON.entityDropsT4));
        LightmansCurrency.LogDebug("Entity T5: " + getValueList(LCConfig.COMMON.entityDropsT5));
        LightmansCurrency.LogDebug("Entity T6: " + getValueList(LCConfig.COMMON.entityDropsT6));
        LightmansCurrency.LogDebug("Boss Entity T1: " + getValueList(LCConfig.COMMON.bossEntityDropsT1));
        LightmansCurrency.LogDebug("Boss Entity T2: " + getValueList(LCConfig.COMMON.bossEntityDropsT2));
        LightmansCurrency.LogDebug("Boss Entity T3: " + getValueList(LCConfig.COMMON.bossEntityDropsT3));
        LightmansCurrency.LogDebug("Boss Entity T4: " + getValueList(LCConfig.COMMON.bossEntityDropsT4));
        LightmansCurrency.LogDebug("Boss Entity T5: " + getValueList(LCConfig.COMMON.bossEntityDropsT5));
        LightmansCurrency.LogDebug("Boss Entity T6: " + getValueList(LCConfig.COMMON.bossEntityDropsT6));
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.m_9236_().isClientSide) {
            if (!LCConfig.COMMON.allowSpawnerEntityDrops.get() && entity instanceof Mob mob && mob.getSpawnType() == MobSpawnType.SPAWNER) {
                LightmansCurrency.LogDebug(entity.m_7755_().getString() + " did not drop coins, as it was spawned by a spawner.");
                return;
            }
            Player player = null;
            if (event.getSource().getDirectEntity() instanceof Player || event.getSource().getEntity() instanceof Player) {
                player = event.getSource().getDirectEntity() instanceof Player p ? p : (Player) event.getSource().getEntity();
                if (player instanceof FakePlayer && !LCConfig.COMMON.allowFakePlayerCoinDrops.get()) {
                    return;
                }
            }
            EntityPoolLevel poolLevel = GetEntityPoolLevel(entity.m_6095_());
            if (poolLevel != null) {
                DropEntityLoot(entity, player, poolLevel);
            }
        }
    }

    private static String getSafeId(@Nonnull Entity entity) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return id == null ? "null" : id.toString().replace(':', '_');
    }

    public static List<ItemStack> safelyGetLoot(LootTable table, LootContext context) {
        List<ItemStack> results = new ArrayList();
        table.getRandomItemsRaw(context, results::add);
        return results;
    }

    public static List<ItemStack> getLoot(@Nonnull ResourceLocation lootTable, @Nonnull LootContext context) {
        List<ItemStack> results = new ArrayList();
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return results;
        } else {
            LootTable table = server.getLootData().m_278676_(lootTable);
            List<ItemStack> loot = safelyGetLoot(table, context);
            checkForEventReplacements(server, loot);
            return loot;
        }
    }

    public static void checkForEventReplacements(@Nonnull MinecraftServer server, @Nonnull List<ItemStack> loot) {
        RandomSource random = server.overworld().m_213780_();
        for (ILootModifier modifier : LOOT_MODIFIERS) {
            if (modifier.tryModifyLoot(random, loot)) {
                return;
            }
        }
    }

    private static void DropEntityLoot(@Nonnull Entity entity, @Nullable Player player, @Nonnull EntityPoolLevel coinPool) {
        if (LCConfig.COMMON.enableEntityDrops.get()) {
            if (coinPool.isBoss || player != null) {
                LootContext context = generateEntityContext(entity, player);
                InventoryUtil.dumpContents(entity.level(), entity.blockPosition(), getLoot(coinPool.lootTable, context));
            }
        }
    }

    public static LootContext generateEntityContext(@Nonnull Entity entity, @Nullable Player player) {
        if (entity.level() instanceof ServerLevel level) {
            LootParams.Builder var5 = new LootParams.Builder(level);
            if (player != null) {
                var5.withParameter(LootContextParams.KILLER_ENTITY, player);
            }
            LootParams params = var5.create(ENTITY_PARAMS);
            return new LootContext.Builder(params).create(new ResourceLocation("lightmanscurrency", "generated_entity_loot/" + getSafeId(entity)));
        } else {
            throw new IllegalArgumentException("Function must be run on the server side!");
        }
    }

    @Nullable
    public static ChestPoolLevel GetChestPoolLevel(@Nonnull String lootTable) {
        if (((List) LCConfig.COMMON.chestDropsT1.get()).contains(lootTable)) {
            return ChestPoolLevel.T1;
        } else if (((List) LCConfig.COMMON.chestDropsT2.get()).contains(lootTable)) {
            return ChestPoolLevel.T2;
        } else if (((List) LCConfig.COMMON.chestDropsT3.get()).contains(lootTable)) {
            return ChestPoolLevel.T3;
        } else if (((List) LCConfig.COMMON.chestDropsT4.get()).contains(lootTable)) {
            return ChestPoolLevel.T4;
        } else if (((List) LCConfig.COMMON.chestDropsT5.get()).contains(lootTable)) {
            return ChestPoolLevel.T5;
        } else {
            return ((List) LCConfig.COMMON.chestDropsT6.get()).contains(lootTable) ? ChestPoolLevel.T6 : null;
        }
    }

    @Nullable
    public static EntityPoolLevel GetEntityPoolLevel(@Nonnull EntityType<?> entity) {
        if (ConfigContainsEntity(LCConfig.COMMON.entityDropsT1, entity)) {
            return EntityPoolLevel.T1;
        } else if (ConfigContainsEntity(LCConfig.COMMON.entityDropsT2, entity)) {
            return EntityPoolLevel.T2;
        } else if (ConfigContainsEntity(LCConfig.COMMON.entityDropsT3, entity)) {
            return EntityPoolLevel.T3;
        } else if (ConfigContainsEntity(LCConfig.COMMON.entityDropsT4, entity)) {
            return EntityPoolLevel.T4;
        } else if (ConfigContainsEntity(LCConfig.COMMON.entityDropsT5, entity)) {
            return EntityPoolLevel.T5;
        } else if (ConfigContainsEntity(LCConfig.COMMON.entityDropsT6, entity)) {
            return EntityPoolLevel.T6;
        } else if (ConfigContainsEntity(LCConfig.COMMON.bossEntityDropsT1, entity)) {
            return EntityPoolLevel.BOSS_T1;
        } else if (ConfigContainsEntity(LCConfig.COMMON.bossEntityDropsT2, entity)) {
            return EntityPoolLevel.BOSS_T2;
        } else if (ConfigContainsEntity(LCConfig.COMMON.bossEntityDropsT3, entity)) {
            return EntityPoolLevel.BOSS_T3;
        } else if (ConfigContainsEntity(LCConfig.COMMON.bossEntityDropsT4, entity)) {
            return EntityPoolLevel.BOSS_T4;
        } else if (ConfigContainsEntity(LCConfig.COMMON.bossEntityDropsT5, entity)) {
            return EntityPoolLevel.BOSS_T5;
        } else {
            return ConfigContainsEntity(LCConfig.COMMON.bossEntityDropsT6, entity) ? EntityPoolLevel.BOSS_T6 : null;
        }
    }

    public static boolean ConfigContainsEntity(@Nonnull StringListOption configOption, @Nonnull EntityType<?> entityType) {
        ResourceLocation entityID = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (entityID == null) {
            return false;
        } else {
            Stream<TagKey<EntityType<?>>> entityTags = entityType.getTags();
            for (String option : (List) configOption.get()) {
                try {
                    if (option.startsWith("#")) {
                        ResourceLocation tagKey = new ResourceLocation(option.substring(1));
                        if (entityTags.anyMatch(tag -> tag.location().equals(tagKey))) {
                            return true;
                        }
                    } else if (option.endsWith(":*")) {
                        if (new ResourceLocation(option.replace(":*", ":null")).getNamespace().equals(entityID.getNamespace())) {
                            return true;
                        }
                    } else if (new ResourceLocation(option).equals(entityID)) {
                        return true;
                    }
                } catch (ResourceLocationException var7) {
                }
            }
            return false;
        }
    }
}