package net.mehvahdjukaar.supplementaries.reg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.BambooSpikesTippedItem;
import net.mehvahdjukaar.supplementaries.common.items.SignPostItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class ModCreativeTabs {

    private static final Set<Item> HIDDEN_ITEMS = new HashSet();

    private static final List<ItemStack> NON_HIDDEN_ITEMS = new ArrayList();

    public static final RegSupplier<CreativeModeTab> MOD_TAB = !CommonConfigs.General.CREATIVE_TAB.get() ? null : RegHelper.registerCreativeModeTab(Supplementaries.res("supplementaries"), c -> c.title(Component.translatable("itemGroup.supplementaries")).icon(() -> ((Item) ModRegistry.GLOBE_ITEM.get()).getDefaultInstance()));

    public static final RegSupplier<CreativeModeTab> JAR_TAB = !CommonConfigs.General.JAR_TAB.get() ? null : RegHelper.registerCreativeModeTab(Supplementaries.res("jars"), c -> SuppPlatformStuff.searchBar(c).title(Component.translatable("itemGroup.jars")).icon(() -> ((Item) ModRegistry.JAR_ITEM.get()).getDefaultInstance()));

    private static boolean isRunningSetup = false;

    public static final List<Consumer<RegHelper.ItemToTabEvent>> SYNCED_ADD_TO_TABS = new ArrayList();

    public static void init() {
        RegHelper.addItemsToTabsRegistration(ModCreativeTabs::registerItemsToTabs);
    }

    public static void setup() {
        isRunningSetup = true;
        List<Item> all = new ArrayList(BuiltInRegistries.ITEM.m_6579_().stream().filter(e -> ((ResourceKey) e.getKey()).location().getNamespace().equals("supplementaries")).map(Entry::getValue).toList());
        Map<ResourceKey<CreativeModeTab>, List<ItemStack>> map = new HashMap();
        CreativeModeTabs.tabs().forEach(t -> map.putIfAbsent((ResourceKey) BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(t).get(), new ArrayList()));
        RegHelper.ItemToTabEvent dummy = new RegHelper.ItemToTabEvent((creativeModeTab, itemStackPredicate, reverse, itemStacks) -> {
            List<ItemStack> l = (List<ItemStack>) map.computeIfAbsent(creativeModeTab, t -> new ArrayList());
            if (reverse) {
                ArrayList<ItemStack> vx = new ArrayList(itemStacks);
                Collections.reverse(vx);
                l.addAll(vx);
            } else {
                l.addAll(itemStacks);
            }
        });
        registerItemsToTabs(dummy);
        for (List<ItemStack> e : map.values()) {
            NON_HIDDEN_ITEMS.addAll(e);
        }
        for (ItemStack v : NON_HIDDEN_ITEMS) {
            all.remove(v.getItem());
        }
        HIDDEN_ITEMS.addAll(all);
        isRunningSetup = false;
    }

    public static boolean isHidden(Item item) {
        return HIDDEN_ITEMS.contains(item);
    }

    public static void registerItemsToTabs(RegHelper.ItemToTabEvent e) {
        if (JAR_TAB != null && !isRunningSetup && (Boolean) CommonConfigs.Functional.JAR_ENABLED.get()) {
            e.addAfter((ResourceKey<CreativeModeTab>) JAR_TAB.getHolder().unwrapKey().get(), null, getJars());
        }
        if (MOD_TAB != null && !isRunningSetup) {
            e.add((ResourceKey<CreativeModeTab>) MOD_TAB.getHolder().unwrapKey().get(), (ItemStack[]) NON_HIDDEN_ITEMS.toArray(ItemStack[]::new));
        } else {
            List<Supplier<? extends ItemLike>> sconces = new ArrayList(ModRegistry.SCONCES);
            sconces.add(ModRegistry.SCONCE_LEVER);
            before(e, Items.LANTERN, CreativeModeTabs.FUNCTIONAL_BLOCKS, "sconce", (Supplier<?>[]) sconces.toArray(Supplier[]::new));
            before(e, Items.CHAIN, CreativeModeTabs.FUNCTIONAL_BLOCKS, "rope", ModRegistry.ROPE);
            after(e, Items.PEARLESCENT_FROGLIGHT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "end_stone_lamp", ModRegistry.END_STONE_LAMP);
            after(e, Items.PEARLESCENT_FROGLIGHT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "deepslate_lamp", ModRegistry.DEEPSLATE_LAMP);
            after(e, Items.PEARLESCENT_FROGLIGHT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "blackstone_lamp", ModRegistry.BLACKSTONE_LAMP);
            after(e, Items.PEARLESCENT_FROGLIGHT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "stone_lamp", ModRegistry.STONE_LAMP);
            after(e, Items.DAMAGED_ANVIL, CreativeModeTabs.FUNCTIONAL_BLOCKS, "pedestal", ModRegistry.PEDESTAL);
            before(e, Items.COMPOSTER, CreativeModeTabs.FUNCTIONAL_BLOCKS, "blackboard", ModRegistry.BLACKBOARD);
            before(e, Items.COMPOSTER, CreativeModeTabs.FUNCTIONAL_BLOCKS, "notice_board", ModRegistry.NOTICE_BOARD);
            before(e, Items.COMPOSTER, CreativeModeTabs.FUNCTIONAL_BLOCKS, "clock_block", ModRegistry.CLOCK_BLOCK);
            before(e, Items.COMPOSTER, CreativeModeTabs.FUNCTIONAL_BLOCKS, "pulley_block", ModRegistry.PULLEY_BLOCK);
            after(e, Items.JUKEBOX, CreativeModeTabs.FUNCTIONAL_BLOCKS, "speaker_block", ModRegistry.SPEAKER_BLOCK);
            after(e, Items.CAULDRON, CreativeModeTabs.FUNCTIONAL_BLOCKS, "cage", ModRegistry.CAGE);
            after(e, Items.CAULDRON, CreativeModeTabs.FUNCTIONAL_BLOCKS, "globe_sepia", ModRegistry.GLOBE_SEPIA);
            after(e, Items.CAULDRON, CreativeModeTabs.FUNCTIONAL_BLOCKS, "globe", ModRegistry.GLOBE);
            after(e, Items.CAULDRON, CreativeModeTabs.FUNCTIONAL_BLOCKS, "hourglass", ModRegistry.HOURGLASS);
            after(e, Items.CAULDRON, CreativeModeTabs.FUNCTIONAL_BLOCKS, "jar", ModRegistry.JAR);
            after(e, Items.CAULDRON, CreativeModeTabs.FUNCTIONAL_BLOCKS, "goblet", ModRegistry.GOBLET);
            after(e, Items.ARMOR_STAND, CreativeModeTabs.FUNCTIONAL_BLOCKS, "statue", ModRegistry.STATUE);
            after(e, Items.FLOWER_POT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "urn", ModRegistry.URN);
            after(e, Items.FLOWER_POT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "planter", ModRegistry.PLANTER);
            after(e, Items.FLOWER_POT, CreativeModeTabs.FUNCTIONAL_BLOCKS, "flower_box", ModRegistry.FLOWER_BOX);
            before(e, Items.DRAGON_HEAD, CreativeModeTabs.FUNCTIONAL_BLOCKS, "enderman_head", ModRegistry.ENDERMAN_SKULL_ITEM);
            before(e, Items.BOOKSHELF, CreativeModeTabs.FUNCTIONAL_BLOCKS, "timber_frame", ModRegistry.TIMBER_FRAME, ModRegistry.TIMBER_BRACE, ModRegistry.TIMBER_CROSS_BRACE);
            if ((Boolean) CommonConfigs.Building.SIGN_POST_ENABLED.get()) {
                for (Entry<WoodType, SignPostItem> v : ModRegistry.SIGN_POST_ITEMS.entrySet()) {
                    WoodType w = (WoodType) v.getKey();
                    e.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> {
                        if (!i.is(ItemTags.HANGING_SIGNS)) {
                            return false;
                        } else {
                            Block b = w.getBlockOfThis("hanging_sign");
                            return b != null && i.is(b.asItem());
                        }
                    }, (ItemLike) v.getValue());
                }
            }
            before(e, Items.CHEST, CreativeModeTabs.FUNCTIONAL_BLOCKS, "doormat", ModRegistry.DOORMAT);
            before(e, Items.CHEST, CreativeModeTabs.FUNCTIONAL_BLOCKS, "item_shelf", ModRegistry.ITEM_SHELF);
            after(e, ItemTags.CANDLES, CreativeModeTabs.FUNCTIONAL_BLOCKS, "candle_holder", (Supplier<?>[]) ModRegistry.CANDLE_HOLDERS.values().toArray(Supplier[]::new));
            after(e, ItemTags.CANDLES, CreativeModeTabs.COLORED_BLOCKS, "candle_holder", (Supplier<?>[]) ModRegistry.CANDLE_HOLDERS.values().toArray(Supplier[]::new));
            after(e, Items.ENDER_CHEST, CreativeModeTabs.FUNCTIONAL_BLOCKS, "safe", ModRegistry.SAFE);
            before(e, Items.SHULKER_BOX, CreativeModeTabs.FUNCTIONAL_BLOCKS, "sack", ModRegistry.SACK);
            after(e, Items.PINK_SHULKER_BOX, CreativeModeTabs.FUNCTIONAL_BLOCKS, "trapped_present", (Supplier<?>[]) ModRegistry.TRAPPED_PRESENTS.values().toArray(Supplier[]::new));
            after(e, Items.PINK_SHULKER_BOX, CreativeModeTabs.FUNCTIONAL_BLOCKS, "present", (Supplier<?>[]) ModRegistry.PRESENTS.values().toArray(Supplier[]::new));
            after(e, Items.PINK_SHULKER_BOX, CreativeModeTabs.COLORED_BLOCKS, "trapped_present", (Supplier<?>[]) ModRegistry.TRAPPED_PRESENTS.values().toArray(Supplier[]::new));
            after(e, Items.PINK_SHULKER_BOX, CreativeModeTabs.COLORED_BLOCKS, "present", (Supplier<?>[]) ModRegistry.PRESENTS.values().toArray(Supplier[]::new));
            e.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(Items.INFESTED_DEEPSLATE), getSpikeItems());
            after(e, Items.INFESTED_DEEPSLATE, CreativeModeTabs.FUNCTIONAL_BLOCKS, "fodder", ModRegistry.FODDER);
            after(e, Items.INFESTED_DEEPSLATE, CreativeModeTabs.FUNCTIONAL_BLOCKS, "sugar_cube", ModRegistry.SUGAR_CUBE);
            after(e, Items.INFESTED_DEEPSLATE, CreativeModeTabs.FUNCTIONAL_BLOCKS, "feather_block", ModRegistry.FEATHER_BLOCK);
            after(e, Items.INFESTED_DEEPSLATE, CreativeModeTabs.FUNCTIONAL_BLOCKS, "flint_block", ModRegistry.FLINT_BLOCK);
            after(e, ItemTags.BANNERS, CreativeModeTabs.FUNCTIONAL_BLOCKS, "flag", (Supplier<?>[]) ModRegistry.FLAGS.values().toArray(Supplier[]::new));
            after(e, ItemTags.BANNERS, CreativeModeTabs.COLORED_BLOCKS, "flag", (Supplier<?>[]) ModRegistry.FLAGS.values().toArray(Supplier[]::new));
            after(e, Items.TNT_MINECART, CreativeModeTabs.REDSTONE_BLOCKS, "dispenser_minecart", ModRegistry.DISPENSER_MINECART_ITEM);
            after(e, Items.REDSTONE_TORCH, CreativeModeTabs.REDSTONE_BLOCKS, "sconce_lever", ModRegistry.SCONCE_LEVER);
            before(e, Items.LEVER, CreativeModeTabs.REDSTONE_BLOCKS, "crank", ModRegistry.CRANK);
            before(e, Items.PISTON, CreativeModeTabs.REDSTONE_BLOCKS, "turn_table", ModRegistry.TURN_TABLE);
            before(e, Items.PISTON, CreativeModeTabs.REDSTONE_BLOCKS, "spring_launcher", ModRegistry.SPRING_LAUNCHER);
            after(e, Items.NOTE_BLOCK, CreativeModeTabs.REDSTONE_BLOCKS, "speaker_block", ModRegistry.SPEAKER_BLOCK);
            after(e, Items.NOTE_BLOCK, CreativeModeTabs.REDSTONE_BLOCKS, "speaker_block", ModRegistry.SPEAKER_BLOCK);
            after(e, Items.HOPPER, CreativeModeTabs.REDSTONE_BLOCKS, "faucet", ModRegistry.FAUCET);
            before(e, Items.TARGET, CreativeModeTabs.REDSTONE_BLOCKS, "cog_block", ModRegistry.COG_BLOCK);
            before(e, Items.NOTE_BLOCK, CreativeModeTabs.REDSTONE_BLOCKS, "bellows", ModRegistry.BELLOWS);
            after(e, Items.OBSERVER, CreativeModeTabs.REDSTONE_BLOCKS, "crystal_display", ModRegistry.CRYSTAL_DISPLAY);
            after(e, Items.OBSERVER, CreativeModeTabs.REDSTONE_BLOCKS, "relayer", ModRegistry.RELAYER);
            after(e, Items.LIGHTNING_ROD, CreativeModeTabs.REDSTONE_BLOCKS, "wind_vane", ModRegistry.WIND_VANE);
            after(e, Items.IRON_DOOR, CreativeModeTabs.REDSTONE_BLOCKS, "netherite_door", ModRegistry.NETHERITE_DOOR);
            after(e, Items.IRON_DOOR, CreativeModeTabs.REDSTONE_BLOCKS, "gold_door", ModRegistry.GOLD_DOOR);
            after(e, Items.IRON_TRAPDOOR, CreativeModeTabs.REDSTONE_BLOCKS, "netherite_trapdoor", ModRegistry.NETHERITE_TRAPDOOR);
            after(e, Items.IRON_TRAPDOOR, CreativeModeTabs.REDSTONE_BLOCKS, "gold_trapdoor", ModRegistry.GOLD_TRAPDOOR);
            before(e, Items.OAK_DOOR, CreativeModeTabs.REDSTONE_BLOCKS, "lock_block", ModRegistry.LOCK_BLOCK);
            before(e, Items.REDSTONE_LAMP, CreativeModeTabs.REDSTONE_BLOCKS, "redstone_illuminator", ModRegistry.REDSTONE_ILLUMINATOR);
            after(e, Items.END_CRYSTAL, CreativeModeTabs.COMBAT, "bomb", ModRegistry.BOMB_ITEM, ModRegistry.BOMB_BLUE_ITEM);
            afterML(e, (Item) ModRegistry.BOMB_BLUE_ITEM.get(), CreativeModeTabs.COMBAT, "oreganized", "bomb", ModRegistry.BOMB_SPIKY_ITEM);
            before(e, Items.BOW, CreativeModeTabs.COMBAT, "quiver", ModRegistry.QUIVER_ITEM);
            after(e, Items.CLOCK, CreativeModeTabs.TOOLS_AND_UTILITIES, "altimeter", ModRegistry.DEPTH_METER_ITEM);
            after(e, Items.MAP, CreativeModeTabs.TOOLS_AND_UTILITIES, "slice_map", ModRegistry.SLICE_MAP);
            before(e, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, CreativeModeTabs.BUILDING_BLOCKS, "gold_door", ModRegistry.GOLD_DOOR);
            before(e, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, CreativeModeTabs.BUILDING_BLOCKS, "gold_trapdoor", ModRegistry.GOLD_TRAPDOOR);
            after(e, Items.NETHERITE_BLOCK, CreativeModeTabs.BUILDING_BLOCKS, "netherite_trapdoor", ModRegistry.NETHERITE_TRAPDOOR);
            after(e, Items.NETHERITE_BLOCK, CreativeModeTabs.BUILDING_BLOCKS, "netherite_door", ModRegistry.NETHERITE_DOOR);
            after(e, Items.SMALL_DRIPLEAF, CreativeModeTabs.NATURAL_BLOCKS, "flax", ModRegistry.FLAX_WILD);
            after(e, Items.BEETROOT_SEEDS, CreativeModeTabs.NATURAL_BLOCKS, "flax", ModRegistry.FLAX_SEEDS_ITEM);
            after(e, Items.HAY_BLOCK, CreativeModeTabs.NATURAL_BLOCKS, "flax", ModRegistry.FLAX_BLOCK);
            after(e, Items.GRAVEL, CreativeModeTabs.NATURAL_BLOCKS, "raked_gravel", ModRegistry.RAKED_GRAVEL);
            after(e, Items.PUMPKIN_PIE, CreativeModeTabs.FOOD_AND_DRINKS, "candy", ModRegistry.CANDY_ITEM);
            after(e, Items.PUMPKIN_PIE, CreativeModeTabs.FOOD_AND_DRINKS, "pancake", ModRegistry.PANCAKE);
            after(e, Items.NETHER_BRICK, CreativeModeTabs.INGREDIENTS, "ash_bricks", ModRegistry.ASH_BRICK_ITEM);
            after(e, Items.GLOW_INK_SAC, CreativeModeTabs.INGREDIENTS, "antique_ink", ModRegistry.ANTIQUE_INK);
            after(e, Items.WHEAT, CreativeModeTabs.INGREDIENTS, "flax", ModRegistry.FLAX_ITEM);
            before(e, Items.PAPER, CreativeModeTabs.INGREDIENTS, "ash", ModRegistry.ASH_BLOCK);
            add(e, CreativeModeTabs.SPAWN_EGGS, "red_merchant", ModRegistry.RED_MERCHANT_SPAWN_EGG_ITEM);
            before(e, Items.BRICKS, CreativeModeTabs.BUILDING_BLOCKS, "ash_bricks", (Supplier<?>[]) ModRegistry.ASH_BRICKS_BLOCKS.values().toArray(Supplier[]::new));
            before(e, Items.FLINT_AND_STEEL, CreativeModeTabs.TOOLS_AND_UTILITIES, "wrench", ModRegistry.WRENCH);
            before(e, Items.FLINT_AND_STEEL, CreativeModeTabs.TOOLS_AND_UTILITIES, "key", ModRegistry.KEY_ITEM);
            before(e, Items.FLINT_AND_STEEL, CreativeModeTabs.TOOLS_AND_UTILITIES, "slingshot", ModRegistry.SLINGSHOT_ITEM);
            before(e, Items.FLINT_AND_STEEL, CreativeModeTabs.TOOLS_AND_UTILITIES, "rope_arrow", ModRegistry.ROPE_ARROW_ITEM);
            before(e, Items.FLINT_AND_STEEL, CreativeModeTabs.TOOLS_AND_UTILITIES, "soap", ModRegistry.SOAP);
            before(e, Items.FLINT_AND_STEEL, CreativeModeTabs.TOOLS_AND_UTILITIES, "bubble_blower", ModRegistry.BUBBLE_BLOWER);
            after(e, i -> i.getItem() instanceof DyeItem, CreativeModeTabs.INGREDIENTS, "soap", ModRegistry.SOAP);
            after(e, Items.SPECTRAL_ARROW, CreativeModeTabs.COMBAT, "rope_arrow", ModRegistry.ROPE_ARROW_ITEM);
            after(e, Items.LEAD, CreativeModeTabs.TOOLS_AND_UTILITIES, "flute", ModRegistry.FLUTE_ITEM);
            after(e, Items.MOSSY_STONE_BRICK_WALL, CreativeModeTabs.BUILDING_BLOCKS, "stone_tile", (Supplier<?>[]) ModRegistry.STONE_TILE_BLOCKS.values().toArray(Supplier[]::new));
            after(e, Items.POLISHED_BLACKSTONE_BRICK_WALL, CreativeModeTabs.BUILDING_BLOCKS, "blackstone_tile", (Supplier<?>[]) ModRegistry.BLACKSTONE_TILE_BLOCKS.values().toArray(Supplier[]::new));
            add(e, CreativeModeTabs.BUILDING_BLOCKS, "lapis_bricks", (Supplier<?>[]) ModRegistry.LAPIS_BRICKS_BLOCKS.values().toArray(Supplier[]::new));
            add(e, CreativeModeTabs.BUILDING_BLOCKS, "checker_block", ModRegistry.CHECKER_BLOCK, ModRegistry.CHECKER_SLAB);
            add(e, CreativeModeTabs.BUILDING_BLOCKS, "daub", ModRegistry.DAUB);
            add(e, CreativeModeTabs.BUILDING_BLOCKS, "wattle_and_daub", ModRegistry.DAUB_FRAME, ModRegistry.DAUB_BRACE, ModRegistry.DAUB_CROSS_BRACE);
            after(e, Items.IRON_BARS, CreativeModeTabs.BUILDING_BLOCKS, "iron_gate", ModRegistry.IRON_GATE);
            afterML(e, "quark:gold_bars", CreativeModeTabs.BUILDING_BLOCKS, "iron_gate", ModRegistry.GOLD_GATE);
            before(e, Items.COAL_BLOCK, CreativeModeTabs.BUILDING_BLOCKS, "soap", ModRegistry.SOAP_BLOCK);
            before(e, Items.OAK_FENCE_GATE, CreativeModeTabs.REDSTONE_BLOCKS, "iron_gate", ModRegistry.IRON_GATE);
            after(e, Items.ARMOR_STAND, CreativeModeTabs.FUNCTIONAL_BLOCKS, "hat_stand", ModRegistry.HAT_STAND);
            CompatHandler.addItemsToTabs(e);
            SYNCED_ADD_TO_TABS.forEach(o -> o.accept(e));
        }
    }

    private static void after(RegHelper.ItemToTabEvent event, TagKey<Item> target, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        after(event, i -> i.is(target), tab, key, items);
    }

    private static void after(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        after(event, i -> i.is(target), tab, key, items);
    }

    private static void after(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        if (CommonConfigs.isEnabled(key)) {
            ItemLike[] entries = (ItemLike[]) Arrays.stream(items).map(s -> (ItemLike) s.get()).toArray(ItemLike[]::new);
            event.addAfter(tab, targetPred, entries);
        }
    }

    private static void before(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        before(event, i -> i.is(target), tab, key, items);
    }

    private static void before(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        if (CommonConfigs.isEnabled(key)) {
            ItemLike[] entries = (ItemLike[]) Arrays.stream(items).map(s -> (ItemLike) s.get()).toArray(ItemLike[]::new);
            event.addBefore(tab, targetPred, entries);
        }
    }

    private static void add(RegHelper.ItemToTabEvent event, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        if (CommonConfigs.isEnabled(key)) {
            ItemLike[] entries = (ItemLike[]) Arrays.stream(items).map(s -> (ItemLike) s.get()).toArray(ItemLike[]::new);
            event.add(tab, entries);
        }
    }

    private static void afterML(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, String key, String modLoaded, Supplier<?>... items) {
        if (PlatHelper.isModLoaded(modLoaded)) {
            after(event, target, tab, key, items);
        }
    }

    private static void afterML(RegHelper.ItemToTabEvent event, String modTarget, ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        ResourceLocation id = new ResourceLocation(modTarget);
        Item target = (Item) BuiltInRegistries.ITEM.m_6612_(id).orElse(null);
        if (target != null) {
            after(event, target, tab, key, items);
        }
    }

    private static void afterTL(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, String key, List<String> tags, Supplier<?>... items) {
        if (isTagOn((String[]) tags.toArray(String[]::new))) {
            after(event, target, tab, key, items);
        }
    }

    private static void beforeML(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, String key, String modLoaded, Supplier<?>... items) {
        if (PlatHelper.isModLoaded(modLoaded)) {
            before(event, target, tab, key, items);
        }
    }

    private static void beforeTL(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, String key, List<String> tags, Supplier<?>... items) {
        if (isTagOn((String[]) tags.toArray(String[]::new))) {
            after(event, target, tab, key, items);
        }
    }

    private static boolean isTagOn(String... tags) {
        for (String t : tags) {
            if (BuiltInRegistries.ITEM.m_203431_(TagKey.create(Registries.ITEM, new ResourceLocation(t))).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack[] getSpikeItems() {
        ArrayList<ItemStack> items = new ArrayList();
        if ((Boolean) CommonConfigs.Functional.BAMBOO_SPIKES_ENABLED.get()) {
            items.add(((Item) ModRegistry.BAMBOO_SPIKES_ITEM.get()).getDefaultInstance());
            if ((Boolean) CommonConfigs.Functional.TIPPED_SPIKES_ENABLED.get() && (Boolean) CommonConfigs.Functional.TIPPED_SPIKES_TAB.get()) {
                items.add(BambooSpikesTippedItem.makeSpikeItem(Potions.POISON));
                items.add(BambooSpikesTippedItem.makeSpikeItem(Potions.LONG_POISON));
                items.add(BambooSpikesTippedItem.makeSpikeItem(Potions.STRONG_POISON));
                for (Potion potion : BuiltInRegistries.POTION) {
                    if (potion != Potions.POISON && potion != Potions.LONG_POISON && potion != Potions.STRONG_POISON && !potion.getEffects().isEmpty() && potion != Potions.EMPTY && BambooSpikesTippedItem.isPotionValid(potion)) {
                        items.add(BambooSpikesTippedItem.makeSpikeItem(potion));
                    }
                }
            }
        }
        return (ItemStack[]) items.toArray(ItemStack[]::new);
    }

    private static ItemStack[] getJars() {
        List<ItemStack> items = new ArrayList();
        items.add(((Item) ModRegistry.JAR_ITEM.get()).getDefaultInstance());
        JarBlockTile tempTile = new JarBlockTile(BlockPos.ZERO, ((Block) ModRegistry.JAR.get()).defaultBlockState());
        SoftFluidTank fluidHolder = SoftFluidTank.create(tempTile.m_6893_());
        if ((Boolean) CommonConfigs.Functional.JAR_COOKIES.get()) {
            for (Holder<Item> i : BuiltInRegistries.ITEM.m_206058_(ModTags.COOKIES)) {
                ItemStack regItem = new ItemStack(i);
                CompoundTag com = new CompoundTag();
                if (tempTile.canPlaceItem(0, regItem)) {
                    regItem.setCount(tempTile.m_6893_());
                    ContainerHelper.saveAllItems(com, NonNullList.withSize(1, regItem));
                    tryAddJar(items, com);
                }
            }
        }
        if ((Boolean) CommonConfigs.Functional.JAR_LIQUIDS.get()) {
            for (Holder.Reference<SoftFluid> h : SoftFluidRegistry.getHolders()) {
                SoftFluid s = h.value();
                if (PlatHelper.isModLoaded(s.getFromMod()) && s != BuiltInSoftFluids.POTION.get() && !s.isEmpty()) {
                    CompoundTag com = new CompoundTag();
                    fluidHolder.clear();
                    fluidHolder.setFluid(new SoftFluidStack(h, 100));
                    fluidHolder.capCapacity();
                    fluidHolder.save(com);
                    tryAddJar(items, com);
                }
            }
            for (ResourceLocation potion : BuiltInRegistries.POTION.m_6566_()) {
                CompoundTag com = new CompoundTag();
                com.putString("Potion", potion.toString());
                fluidHolder.setFluid(new SoftFluidStack(BuiltInSoftFluids.POTION.getHolder(), 100, com));
                fluidHolder.capCapacity();
                CompoundTag com2 = new CompoundTag();
                fluidHolder.save(com2);
                tryAddJar(items, com2);
            }
        }
        return (ItemStack[]) items.toArray(ItemStack[]::new);
    }

    private static void tryAddJar(List<ItemStack> items, CompoundTag com) {
        if (!com.isEmpty()) {
            ItemStack returnStack = new ItemStack((ItemLike) ModRegistry.JAR_ITEM.get());
            returnStack.addTagElement("BlockEntityTag", com);
            for (ItemStack i : items) {
                if (i.equals(returnStack)) {
                    return;
                }
            }
            items.add(returnStack);
        }
    }
}