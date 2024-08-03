package net.mehvahdjukaar.supplementaries.reg;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class LootTablesInjects {

    private static final List<BiConsumer<Consumer<ResourceLocation>, LootTablesInjects.TableType>> LOOT_INJECTS = new ArrayList();

    private static final boolean RS = CompatHandler.REPURPOSED_STRUCTURES;

    private static final Pattern RS_SHIPWRECK = Pattern.compile("repurposed_structures:chests/shipwreck/\\w*/treasure_chest");

    private static final Pattern RS_SHIPWRECK_STORAGE = Pattern.compile("repurposed_structures:chests/shipwreck/\\w*/supply_chest");

    private static final Pattern RS_TEMPLE = Pattern.compile("repurposed_structures:chests/temple/\\w*_chest");

    private static final Pattern RS_TEMPLE_DISPENSER = Pattern.compile("repurposed_structures:chests/temple/\\w*_dispenser");

    public static void init() {
        RegHelper.addLootTableInjects(LootTablesInjects::injectLootPools);
    }

    private static ResourceLocation injectLootPools(RegHelper.LootInjectEvent event) {
        String nameSpace = event.getTable().getNamespace();
        if (nameSpace.equals("minecraft") || nameSpace.equals("repurposed_structures")) {
            String location = event.getTable().toString();
            LootTablesInjects.TableType type = getType(location);
            if (type != LootTablesInjects.TableType.OTHER) {
                LOOT_INJECTS.forEach(i -> i.accept(event::addTableReference, type));
            }
        }
        return null;
    }

    public static void setup() {
        if ((Boolean) CommonConfigs.Building.GLOBE_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectGlobe);
        }
        if ((Boolean) CommonConfigs.Tools.QUIVER_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectQuiver);
        }
        if ((Boolean) CommonConfigs.Functional.ROPE_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectRope);
        }
        if ((Boolean) CommonConfigs.Functional.FLAX_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectFlax);
        }
        if ((Boolean) CommonConfigs.Tools.BOMB_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectBlueBomb);
        }
        if ((Boolean) CommonConfigs.Tools.BOMB_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectBomb);
        }
        if (CommonConfigs.stasisEnabled()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectStasis);
        }
        if ((Boolean) CommonConfigs.Functional.BAMBOO_SPIKES_ENABLED.get() && (Boolean) CommonConfigs.Functional.TIPPED_SPIKES_ENABLED.get()) {
            LOOT_INJECTS.add(LootTablesInjects::tryInjectSpikes);
        }
    }

    public static LootTablesInjects.TableType getType(String name) {
        if (isShipwreck(name)) {
            return LootTablesInjects.TableType.SHIPWRECK_TREASURE;
        } else if (isShipwreckStorage(name)) {
            return LootTablesInjects.TableType.SHIPWRECK_STORAGE;
        } else if (isMineshaft(name)) {
            return LootTablesInjects.TableType.MINESHAFT;
        } else if (isDungeon(name)) {
            return LootTablesInjects.TableType.DUNGEON;
        } else if (isTemple(name)) {
            return LootTablesInjects.TableType.TEMPLE;
        } else if (isTempleDispenser(name)) {
            return LootTablesInjects.TableType.TEMPLE_DISPENSER;
        } else if (isOutpost(name)) {
            return LootTablesInjects.TableType.PILLAGER;
        } else if (isStronghold(name)) {
            return LootTablesInjects.TableType.STRONGHOLD;
        } else if (isFortress(name)) {
            return LootTablesInjects.TableType.FORTRESS;
        } else if (isEndCity(name)) {
            return LootTablesInjects.TableType.END_CITY;
        } else if (isMansion(name)) {
            return LootTablesInjects.TableType.MANSION;
        } else {
            return isFishTreasure(name) ? LootTablesInjects.TableType.FISHING_TREASURE : LootTablesInjects.TableType.OTHER;
        }
    }

    private static boolean isFishTreasure(String name) {
        return name.equals(BuiltInLootTables.FISHING_TREASURE.toString());
    }

    private static boolean isMansion(String name) {
        return name.equals(BuiltInLootTables.WOODLAND_MANSION.toString()) || RS && name.contains("repurposed_structures:chests/mansion");
    }

    private static boolean isShipwreck(String s) {
        return s.equals(BuiltInLootTables.SHIPWRECK_TREASURE.toString()) || RS && RS_SHIPWRECK.matcher(s).matches();
    }

    private static boolean isShipwreckStorage(String s) {
        return s.equals(BuiltInLootTables.SHIPWRECK_SUPPLY.toString()) || RS && RS_SHIPWRECK_STORAGE.matcher(s).matches();
    }

    private static boolean isMineshaft(String s) {
        return s.equals(BuiltInLootTables.ABANDONED_MINESHAFT.toString()) || RS && s.contains("repurposed_structures:chests/mineshaft");
    }

    private static boolean isOutpost(String s) {
        return s.equals(BuiltInLootTables.PILLAGER_OUTPOST.toString()) || RS && s.contains("repurposed_structures:chests/outpost");
    }

    private static boolean isDungeon(String s) {
        return s.equals(BuiltInLootTables.SIMPLE_DUNGEON.toString()) || RS && s.contains("repurposed_structures:chests/dungeon");
    }

    private static boolean isTemple(String s) {
        return s.equals(BuiltInLootTables.JUNGLE_TEMPLE.toString()) || RS && RS_TEMPLE.matcher(s).matches();
    }

    private static boolean isTempleDispenser(String s) {
        return s.equals(BuiltInLootTables.JUNGLE_TEMPLE.toString()) || RS && RS_TEMPLE_DISPENSER.matcher(s).matches();
    }

    private static boolean isStronghold(String s) {
        return s.equals(BuiltInLootTables.STRONGHOLD_CROSSING.toString()) || RS && s.contains("repurposed_structures:chests/stronghold/nether_storage_room");
    }

    private static boolean isFortress(String s) {
        return s.equals(BuiltInLootTables.NETHER_BRIDGE.toString()) || RS && s.contains("repurposed_structures:chests/fortress");
    }

    private static boolean isEndCity(String s) {
        return s.equals(BuiltInLootTables.END_CITY_TREASURE.toString());
    }

    private static void injectPool(Consumer<ResourceLocation> consumer, LootTablesInjects.TableType type, String name) {
        String id = type.toString().toLowerCase(Locale.ROOT) + "_" + name;
        consumer.accept(Supplementaries.res("inject/" + id));
    }

    private static void tryInjectGlobe(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.SHIPWRECK_TREASURE) {
            injectPool(e, type, "globe");
        }
    }

    private static void tryInjectQuiver(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.DUNGEON || type == LootTablesInjects.TableType.MANSION) {
            injectPool(e, type, "quiver");
        }
    }

    private static void tryInjectRope(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.MINESHAFT) {
            injectPool(e, type, "rope");
        }
    }

    private static void tryInjectFlax(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.MINESHAFT || type == LootTablesInjects.TableType.DUNGEON || type == LootTablesInjects.TableType.SHIPWRECK_STORAGE || type == LootTablesInjects.TableType.PILLAGER) {
            injectPool(e, type, "flax");
        }
    }

    private static void tryInjectBlueBomb(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.STRONGHOLD || type == LootTablesInjects.TableType.MINESHAFT || type == LootTablesInjects.TableType.TEMPLE || type == LootTablesInjects.TableType.FORTRESS || type == LootTablesInjects.TableType.DUNGEON) {
            injectPool(e, type, "blue_bomb");
        }
    }

    private static void tryInjectBomb(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.STRONGHOLD || type == LootTablesInjects.TableType.MINESHAFT || type == LootTablesInjects.TableType.TEMPLE || type == LootTablesInjects.TableType.FORTRESS) {
            injectPool(e, type, "bomb");
        }
    }

    private static void tryInjectSpikes(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.TEMPLE) {
            injectPool(e, type, "spikes");
        }
    }

    private static void tryInjectStasis(Consumer<ResourceLocation> e, LootTablesInjects.TableType type) {
        if (type == LootTablesInjects.TableType.END_CITY) {
            injectPool(e, type, "stasis");
        }
    }

    private static enum TableType {

        OTHER,
        MINESHAFT,
        SHIPWRECK_TREASURE,
        PILLAGER,
        DUNGEON,
        PYRAMID,
        STRONGHOLD,
        TEMPLE,
        TEMPLE_DISPENSER,
        IGLOO,
        MANSION,
        FORTRESS,
        BASTION,
        RUIN,
        SHIPWRECK_STORAGE,
        END_CITY,
        FISHING_TREASURE
    }
}