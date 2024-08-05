package org.violetmoon.quark.content.tools.module;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.api.QuarkCapabilities;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.experimental.module.EnchantmentsBegoneModule;
import org.violetmoon.quark.content.tools.base.RuneColor;
import org.violetmoon.quark.content.tools.item.AncientTomeItem;
import org.violetmoon.quark.content.tools.loot.EnchantTome;
import org.violetmoon.quark.content.world.module.MonsterBoxModule;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.ZAnvilRepair;
import org.violetmoon.zeta.event.play.ZAnvilUpdate;
import org.violetmoon.zeta.event.play.entity.player.ZPlayer;
import org.violetmoon.zeta.event.play.loading.ZAttachCapabilities;
import org.violetmoon.zeta.event.play.loading.ZLootTableLoad;
import org.violetmoon.zeta.event.play.loading.ZVillagerTrades;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tools")
public class AncientTomesModule extends ZetaModule {

    private static final Object mutex = new Object();

    @Config(description = "Format is lootTable,weight. i.e. \"minecraft:chests/stronghold_library,30\"")
    public static List<String> lootTables = Lists.newArrayList(new String[] { loot(BuiltInLootTables.STRONGHOLD_LIBRARY, 20), loot(BuiltInLootTables.SIMPLE_DUNGEON, 20), loot(BuiltInLootTables.BASTION_TREASURE, 25), loot(BuiltInLootTables.WOODLAND_MANSION, 15), loot(BuiltInLootTables.NETHER_BRIDGE, 0), loot(BuiltInLootTables.UNDERWATER_RUIN_BIG, 0), loot(BuiltInLootTables.UNDERWATER_RUIN_SMALL, 0), loot(BuiltInLootTables.ANCIENT_CITY, 4), loot(MonsterBoxModule.MONSTER_BOX_LOOT_TABLE, 5) });

    private static final Object2IntMap<ResourceLocation> lootTableWeights = new Object2IntArrayMap();

    @Config
    public static int itemQuality = 2;

    @Config
    public static int normalUpgradeCost = 10;

    @Config
    public static int limitBreakUpgradeCost = 30;

    public static LootItemFunctionType tomeEnchantType;

    @Config(name = "Valid Enchantments")
    public static List<String> enchantNames = generateDefaultEnchantmentList();

    @Config
    public static boolean overleveledBooksGlowRainbow = true;

    @Config(description = "When enabled, Efficiency VI Diamond and Netherite pickaxes can instamine Deepslate when under Haste 2", flag = "deepslate_tweak")
    public static boolean deepslateTweak = true;

    @Config
    public static boolean deepslateTweakNeedsHaste2 = true;

    @Config(description = "Master Librarians will offer to exchange Ancient Tomes, provided you give them a max-level Enchanted Book of the Tome's enchantment too.")
    public static boolean librariansExchangeAncientTomes = true;

    @Config(description = "Applying a tome will also randomly curse your item")
    public static boolean curseGear = false;

    @Config(description = "Allows combining tomes with normal books")
    public static boolean combineWithBooks = true;

    @Config(description = "Whether a sanity check is performed on the valid enchantments. If this is turned off, enchantments such as Silk Touch will be allowed to generate Ancient Tomes, if explicitly added to the Valid Enchantments.")
    public static boolean sanityCheck = true;

    @Hint
    public static Item ancient_tome;

    public static final List<Enchantment> validEnchants = new ArrayList();

    private static boolean initialized = false;

    public static ManualTrigger overlevelTrigger;

    public static ManualTrigger instamineDeepslateTrigger;

    private static final ResourceLocation OVERLEVEL_COLOR_HANDLER = new ResourceLocation("quark", "overlevel_rune");

    private final List<Enchantment> curses = new ArrayList();

    private static String loot(ResourceLocation lootLoc, int defaultWeight) {
        return lootLoc.toString() + "," + defaultWeight;
    }

    @LoadEvent
    public void register(ZRegister event) {
        ancient_tome = new AncientTomeItem(this);
        tomeEnchantType = new LootItemFunctionType(new EnchantTome.Serializer());
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation("quark", "tome_enchant"), tomeEnchantType);
        overlevelTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("overlevel");
        instamineDeepslateTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("instamine_deepslate");
    }

    @PlayEvent
    public void onTradesLoaded(ZVillagerTrades event) {
        if (event.getType() == VillagerProfession.LIBRARIAN && librariansExchangeAncientTomes) {
            synchronized (mutex) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ((List) trades.get(5)).add(new AncientTomesModule.ExchangeAncientTomesTrade());
            }
        }
    }

    @LoadEvent
    public void configChanged(ZConfigChanged event) {
        lootTableWeights.clear();
        for (String table : lootTables) {
            String[] split = table.split(",");
            if (split.length == 2) {
                ResourceLocation loc = new ResourceLocation(split[0]);
                int weight;
                try {
                    weight = Integer.parseInt(split[1]);
                } catch (NumberFormatException var8) {
                    continue;
                }
                if (weight > 0) {
                    lootTableWeights.put(loc, weight);
                }
            }
        }
        if (initialized) {
            this.setupEnchantList();
        }
    }

    @LoadEvent
    public void setup(ZCommonSetup event) {
        this.setupEnchantList();
        this.setupCursesList();
        initialized = true;
    }

    @PlayEvent
    public void onLootTableLoad(ZLootTableLoad event) {
        ResourceLocation res = event.getName();
        int weight = lootTableWeights.getOrDefault(res, 0);
        if (weight > 0) {
            LootPoolEntryContainer entry = LootItem.lootTableItem(ancient_tome).setWeight(weight).setQuality(itemQuality).apply(() -> new EnchantTome(new LootItemCondition[0])).m_7512_();
            event.add(entry);
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

    @PlayEvent
    public void onAnvilUpdate(ZAnvilUpdate.Highest event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        String name = event.getName();
        if (!left.isEmpty() && !right.isEmpty() && left.getCount() == 1 && right.getCount() == 1) {
            if (right.is(ancient_tome)) {
                if (!combineWithBooks && left.is(Items.ENCHANTED_BOOK)) {
                    return;
                }
                Enchantment ench = getTomeEnchantment(right);
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(left);
                if (ench != null && enchants.containsKey(ench) && (Integer) enchants.get(ench) <= ench.getMaxLevel()) {
                    int lvl = (Integer) enchants.get(ench) + 1;
                    enchants.put(ench, lvl);
                    ItemStack out = left.copy();
                    EnchantmentHelper.setEnchantments(enchants, out);
                    int cost = lvl > ench.getMaxLevel() ? limitBreakUpgradeCost : normalUpgradeCost;
                    if (name != null && !name.isEmpty() && (!out.hasCustomHoverName() || !out.getHoverName().getString().equals(name))) {
                        out.setHoverName(Component.literal(name));
                        cost++;
                    }
                    event.setOutput(out);
                    event.setCost(cost);
                }
            } else if (combineWithBooks && right.is(Items.ENCHANTED_BOOK)) {
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(right);
                Map<Enchantment, Integer> currentEnchants = EnchantmentHelper.getEnchantments(left);
                boolean hasOverLevel = false;
                boolean hasMatching = false;
                for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                    Enchantment enchantment = (Enchantment) entry.getKey();
                    if (enchantment != null) {
                        int level = (Integer) entry.getValue();
                        if (level > enchantment.getMaxLevel()) {
                            hasOverLevel = true;
                            if (enchantment.canEnchant(left) || left.is(Items.ENCHANTED_BOOK)) {
                                hasMatching = true;
                                Iterator<Enchantment> iterator = currentEnchants.keySet().iterator();
                                while (iterator.hasNext()) {
                                    Enchantment comparingEnchantment = (Enchantment) iterator.next();
                                    if (comparingEnchantment != enchantment && !comparingEnchantment.isCompatibleWith(enchantment)) {
                                        iterator.remove();
                                    }
                                }
                                currentEnchants.put(enchantment, level);
                            }
                        } else if (enchantment.canEnchant(left)) {
                            boolean compatible = true;
                            Iterator comparingEnchantment = currentEnchants.keySet().iterator();
                            while (true) {
                                if (comparingEnchantment.hasNext()) {
                                    Enchantment comparingEnchantmentx = (Enchantment) comparingEnchantment.next();
                                    if (comparingEnchantmentx == enchantment || comparingEnchantmentx == null || comparingEnchantmentx.isCompatibleWith(enchantment)) {
                                        continue;
                                    }
                                    compatible = false;
                                }
                                if (compatible) {
                                    currentEnchants.put(enchantment, level);
                                }
                                break;
                            }
                        }
                    }
                }
                if (hasOverLevel && hasMatching) {
                    ItemStack out = left.copy();
                    EnchantmentHelper.setEnchantments(currentEnchants, out);
                    int cost = normalUpgradeCost;
                    if (name != null && !name.isEmpty() && (!out.hasCustomHoverName() || !out.getHoverName().getString().equals(name))) {
                        out.setHoverName(Component.literal(name));
                        cost++;
                    }
                    event.setOutput(out);
                    event.setCost(cost);
                }
            }
        }
    }

    @PlayEvent
    public void onAnvilUse(ZAnvilRepair event) {
        ItemStack output = event.getOutput();
        ItemStack right = event.getRight();
        if (curseGear && (right.is(ancient_tome) || event.getLeft().is(ancient_tome))) {
            event.getOutput().enchant((Enchantment) this.curses.get(event.getEntity().m_9236_().random.nextInt(this.curses.size())), 1);
        }
        if (isOverlevel(output) && (right.getItem() == Items.ENCHANTED_BOOK || right.getItem() == ancient_tome) && event.getEntity() instanceof ServerPlayer sp) {
            overlevelTrigger.trigger(sp);
        }
    }

    @PlayEvent
    public void onGetSpeed(ZPlayer.BreakSpeed event) {
        if (deepslateTweak) {
            Player player = event.getPlayer();
            ItemStack stack = player.m_21205_();
            BlockState state = event.getState();
            if (state.m_60713_(Blocks.DEEPSLATE) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, stack) >= 6 && event.getOriginalSpeed() >= 45.0F && (!deepslateTweakNeedsHaste2 || this.playerHasHaste2(player))) {
                event.setNewSpeed(100.0F);
                if (player instanceof ServerPlayer sp) {
                    instamineDeepslateTrigger.trigger(sp);
                }
            }
        }
    }

    private boolean playerHasHaste2(Player player) {
        MobEffectInstance inst = player.m_21124_(MobEffects.DIG_SPEED);
        return inst != null && inst.getAmplifier() > 0;
    }

    private static boolean isOverlevel(ItemStack stack) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        for (Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchantment = (Enchantment) entry.getKey();
            if (enchantment != null) {
                int level = (Integer) entry.getValue();
                if (level > enchantment.getMaxLevel()) {
                    return true;
                }
            }
        }
        return false;
    }

    @PlayEvent
    public void attachRuneCapability(ZAttachCapabilities.ItemStackCaps event) {
        if (((ItemStack) event.getObject()).getItem() == Items.ENCHANTED_BOOK) {
            event.addCapability(OVERLEVEL_COLOR_HANDLER, QuarkCapabilities.RUNE_COLOR, stack -> overleveledBooksGlowRainbow && isOverlevel(stack) ? RuneColor.RAINBOW : null);
        }
    }

    public static Rarity shiftRarity(ItemStack itemStack, Rarity returnValue) {
        return Quark.ZETA.modules.isEnabled(AncientTomesModule.class) && overleveledBooksGlowRainbow && itemStack.getItem() == Items.ENCHANTED_BOOK && isOverlevel(itemStack) ? Rarity.EPIC : returnValue;
    }

    private static List<String> generateDefaultEnchantmentList() {
        Enchantment[] enchants = new Enchantment[] { Enchantments.FALL_PROTECTION, Enchantments.THORNS, Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.MOB_LOOTING, Enchantments.SWEEPING_EDGE, Enchantments.BLOCK_EFFICIENCY, Enchantments.UNBREAKING, Enchantments.BLOCK_FORTUNE, Enchantments.POWER_ARROWS, Enchantments.PUNCH_ARROWS, Enchantments.FISHING_LUCK, Enchantments.FISHING_SPEED, Enchantments.LOYALTY, Enchantments.RIPTIDE, Enchantments.IMPALING, Enchantments.PIERCING };
        List<String> strings = new ArrayList();
        for (Enchantment e : enchants) {
            ResourceLocation regname = BuiltInRegistries.ENCHANTMENT.getKey(e);
            if (e != null && regname != null) {
                strings.add(regname.toString());
            }
        }
        return strings;
    }

    private void setupEnchantList() {
        initializeEnchantmentList(enchantNames, validEnchants);
        if (sanityCheck) {
            validEnchants.removeIf(ench -> ench.getMaxLevel() == 1);
        }
    }

    public static void initializeEnchantmentList(Iterable<String> enchantNames, List<Enchantment> enchants) {
        enchants.clear();
        for (String s : enchantNames) {
            Enchantment enchant = BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(s));
            if (enchant != null && !EnchantmentsBegoneModule.shouldBegone(enchant)) {
                enchants.add(enchant);
            }
        }
    }

    public void setupCursesList() {
        for (Enchantment e : BuiltInRegistries.ENCHANTMENT) {
            if (e.isCurse()) {
                this.curses.add(e);
            }
        }
    }

    public static Enchantment getTomeEnchantment(ItemStack stack) {
        if (stack.getItem() != ancient_tome) {
            return null;
        } else {
            ListTag list = EnchantedBookItem.getEnchantments(stack);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag nbt = list.getCompound(i);
                Enchantment enchant = BuiltInRegistries.ENCHANTMENT.get(ResourceLocation.tryParse(nbt.getString("id")));
                if (enchant != null) {
                    return enchant;
                }
            }
            return null;
        }
    }

    private static boolean isAncientTomeOffer(MerchantOffer offer) {
        return offer.getCostA().is(ancient_tome) && offer.getCostB().is(Items.ENCHANTED_BOOK) && offer.getResult().is(ancient_tome);
    }

    public static void moveVillagerItems(MerchantMenu menu, MerchantContainer container, MerchantOffer offer) {
        if (isAncientTomeOffer(offer) && container.getItem(0).isEmpty() && container.getItem(1).isEmpty()) {
            ItemStack costA = offer.getCostA();
            moveFromInventoryToPaymentSlot(menu, container, offer, 0, costA);
            ItemStack costB = offer.getCostB();
            moveFromInventoryToPaymentSlot(menu, container, offer, 1, costB);
        }
    }

    private static void moveFromInventoryToPaymentSlot(MerchantMenu menu, MerchantContainer container, MerchantOffer offer, int tradeSlot, ItemStack targetStack) {
        menu.moveFromInventoryToPaymentSlot(tradeSlot, targetStack);
        if (container.getItem(tradeSlot).isEmpty() && !targetStack.isEmpty()) {
            for (int slot = 3; slot < 39; slot++) {
                ItemStack inSlot = ((Slot) menu.f_38839_.get(slot)).getItem();
                ItemStack currentStack = container.getItem(tradeSlot);
                if (!ItemStack.isSameItemSameTags(inSlot, offer.getResult()) && !inSlot.isEmpty() && (currentStack.isEmpty() ? offer.isRequiredItem(inSlot, targetStack) : ItemStack.isSameItemSameTags(targetStack, inSlot))) {
                    int currentCount = currentStack.isEmpty() ? 0 : currentStack.getCount();
                    int amountToTake = Math.min(targetStack.getMaxStackSize() - currentCount, inSlot.getCount());
                    ItemStack newStack = inSlot.copy();
                    int newCount = currentCount + amountToTake;
                    inSlot.shrink(amountToTake);
                    newStack.setCount(newCount);
                    container.setItem(tradeSlot, newStack);
                    if (newCount >= targetStack.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }
    }

    public static boolean matchWildcardEnchantedBook(MerchantOffer offer, ItemStack comparing, ItemStack reference) {
        if (isAncientTomeOffer(offer) && comparing.is(Items.ENCHANTED_BOOK) && reference.is(Items.ENCHANTED_BOOK)) {
            Map<Enchantment, Integer> referenceEnchants = EnchantmentHelper.getEnchantments(reference);
            if (referenceEnchants.size() == 1) {
                Enchantment enchantment = (Enchantment) referenceEnchants.keySet().iterator().next();
                int level = (Integer) referenceEnchants.get(enchantment);
                Map<Enchantment, Integer> comparingEnchants = EnchantmentHelper.getEnchantments(comparing);
                for (Entry<Enchantment, Integer> entry : comparingEnchants.entrySet()) {
                    if (entry.getKey() == enchantment && (Integer) entry.getValue() >= level) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private class ExchangeAncientTomesTrade implements VillagerTrades.ItemListing {

        @Nullable
        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource random) {
            if (!AncientTomesModule.validEnchants.isEmpty() && AncientTomesModule.this.enabled) {
                Enchantment target = (Enchantment) AncientTomesModule.validEnchants.get(random.nextInt(AncientTomesModule.validEnchants.size()));
                ItemStack anyTome = new ItemStack(AncientTomesModule.ancient_tome);
                ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(target, target.getMaxLevel()));
                ItemStack outputTome = AncientTomeItem.getEnchantedItemStack(target);
                return new MerchantOffer(anyTome, enchantedBook, outputTome, 3, 3, 0.2F);
            } else {
                return null;
            }
        }
    }
}