package org.violetmoon.quark.content.management.module;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import org.violetmoon.quark.addons.oddities.module.BackpackModule;
import org.violetmoon.quark.api.event.GatherToolClassesEvent;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.util.InventoryIIH;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.SyncedFlagHandler;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerDestroyItem;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.RegistryUtil;

@ZetaLoadModule(category = "management", antiOverlap = { "inventorytweaks" })
public class AutomaticToolRestockModule extends ZetaModule {

    private static final Map<ToolAction, String> ACTION_TO_CLASS = new HashMap();

    private static final WeakHashMap<Player, Stack<AutomaticToolRestockModule.QueuedRestock>> replacements = new WeakHashMap();

    public List<Enchantment> importantEnchants = new ArrayList();

    public List<Item> itemsToIgnore = new ArrayList();

    @Config(name = "Important Enchantments", description = "Enchantments deemed important enough to have special priority when finding a replacement")
    private List<String> enchantNames = generateDefaultEnchantmentList();

    private static final String LOOSE_MATCHING = "automatic_restock_loose_matching";

    private static final String ENCHANT_MATCHING = "automatic_restock_enchant_matching";

    private static final String CHECK_HOTBAR = "automatic_restock_check_hotbar";

    private static final String UNSTACKABLES_ONLY = "automatic_restock_unstackables_only";

    @Config(description = "Enable replacing your tools with tools of the same type but not the same item", flag = "automatic_restock_loose_matching")
    private boolean enableLooseMatching = true;

    @Config(description = "Enable comparing enchantments to find a replacement", flag = "automatic_restock_enchant_matching")
    private boolean enableEnchantMatching = true;

    @Config(description = "Allow pulling items from one hotbar slot to another", flag = "automatic_restock_check_hotbar")
    private boolean checkHotbar = false;

    @Config(flag = "automatic_restock_unstackables_only")
    private boolean unstackablesOnly = false;

    @Config(description = "Any items you place in this list will be ignored by the restock feature")
    private List<String> ignoredItems = Lists.newArrayList(new String[] { "botania:exchange_rod", "botania:dirt_rod", "botania:skydirt_rod", "botania:cobble_rod" });

    private Object mutex = new Object();

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        this.importantEnchants = RegistryUtil.massRegistryGet(this.enchantNames, BuiltInRegistries.ENCHANTMENT);
        this.itemsToIgnore = RegistryUtil.massRegistryGet(this.ignoredItems, BuiltInRegistries.ITEM);
    }

    @PlayEvent
    public void onToolBreak(ZPlayerDestroyItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getOriginal();
        Item item = stack.getItem();
        if (player instanceof ServerPlayer serverPlayer) {
            if (!SyncedFlagHandler.getFlagForPlayer(serverPlayer, "automatic_tool_restock")) {
                return;
            }
            boolean onlyUnstackables = SyncedFlagHandler.getFlagForPlayer(serverPlayer, "automatic_restock_unstackables_only");
            if (!stack.isEmpty() && !(item instanceof ArmorItem) && (!onlyUnstackables || !stack.isStackable())) {
                boolean hotbar = SyncedFlagHandler.getFlagForPlayer(serverPlayer, "automatic_restock_check_hotbar");
                int currSlot = player.getInventory().selected;
                if (event.getHand() == InteractionHand.OFF_HAND) {
                    currSlot = player.getInventory().getContainerSize() - 1;
                }
                List<Enchantment> enchantmentsOnStack = this.getImportantEnchantments(stack);
                Predicate<ItemStack> itemPredicate = other -> other.getItem() == item;
                if (!stack.isDamageableItem()) {
                    itemPredicate = itemPredicate.and(other -> other.getDamageValue() == stack.getDamageValue());
                }
                Predicate<ItemStack> enchantmentPredicate = other -> !new ArrayList(enchantmentsOnStack).retainAll(this.getImportantEnchantments(other));
                Set<String> classes = this.getItemClasses(stack);
                Optional<Predicate<ItemStack>> toolPredicate = Optional.empty();
                if (!classes.isEmpty()) {
                    toolPredicate = Optional.of((Predicate) other -> {
                        Set<String> otherClasses = this.getItemClasses(other);
                        return !otherClasses.isEmpty() && !otherClasses.retainAll(classes);
                    });
                }
                AutomaticToolRestockModule.RestockContext ctx = new AutomaticToolRestockModule.RestockContext(serverPlayer, currSlot, enchantmentsOnStack, itemPredicate, enchantmentPredicate, toolPredicate);
                int lower = hotbar ? 0 : 9;
                int upper = player.getInventory().items.size();
                boolean foundInInv = this.crawlInventory(new PlayerInvWrapper(player.getInventory()), lower, upper, ctx);
                if (!foundInInv && Quark.ZETA.modules.isEnabled(BackpackModule.class)) {
                    ItemStack backpack = player.getInventory().armor.get(2);
                    if (backpack.getItem() == BackpackModule.backpack) {
                        InventoryIIH inv = new InventoryIIH(backpack);
                        this.crawlInventory(inv, 0, inv.getSlots(), ctx);
                    }
                }
            }
        }
    }

    private boolean crawlInventory(IItemHandler inv, int lowerBound, int upperBound, AutomaticToolRestockModule.RestockContext ctx) {
        ServerPlayer player = ctx.player;
        int currSlot = ctx.currSlot;
        List<Enchantment> enchantmentsOnStack = ctx.enchantmentsOnStack;
        Predicate<ItemStack> itemPredicate = ctx.itemPredicate;
        Predicate<ItemStack> enchantmentPredicate = ctx.enchantmentPredicate;
        Optional<Predicate<ItemStack>> toolPredicateOpt = ctx.toolPredicate;
        boolean enchantMatching = SyncedFlagHandler.getFlagForPlayer(player, "automatic_restock_enchant_matching");
        boolean looseMatching = SyncedFlagHandler.getFlagForPlayer(player, "automatic_restock_loose_matching");
        if (enchantMatching && this.findReplacement(inv, player, lowerBound, upperBound, currSlot, itemPredicate.and(enchantmentPredicate))) {
            return true;
        } else if (this.findReplacement(inv, player, lowerBound, upperBound, currSlot, itemPredicate)) {
            return true;
        } else if (looseMatching && toolPredicateOpt.isPresent()) {
            Predicate<ItemStack> toolPredicate = (Predicate<ItemStack>) toolPredicateOpt.get();
            return enchantMatching && !enchantmentsOnStack.isEmpty() && this.findReplacement(inv, player, lowerBound, upperBound, currSlot, toolPredicate.and(enchantmentPredicate)) ? true : this.findReplacement(inv, player, lowerBound, upperBound, currSlot, toolPredicate);
        } else {
            return false;
        }
    }

    @PlayEvent
    public void onPlayerTick(ZPlayerTick.End event) {
        if (!event.getPlayer().m_9236_().isClientSide && replacements.containsKey(event.getPlayer())) {
            Stack<AutomaticToolRestockModule.QueuedRestock> replacementStack = (Stack<AutomaticToolRestockModule.QueuedRestock>) replacements.get(event.getPlayer());
            synchronized (this.mutex) {
                while (!replacementStack.isEmpty()) {
                    AutomaticToolRestockModule.QueuedRestock restock = (AutomaticToolRestockModule.QueuedRestock) replacementStack.pop();
                    this.switchItems(event.getPlayer(), restock);
                }
            }
        }
    }

    private HashSet<String> getItemClasses(ItemStack stack) {
        Item item = stack.getItem();
        HashSet<String> classes = new HashSet();
        if (item instanceof BowItem) {
            classes.add("bow");
        } else if (item instanceof CrossbowItem) {
            classes.add("crossbow");
        }
        for (ToolAction action : ACTION_TO_CLASS.keySet()) {
            if (item.canPerformAction(stack, action)) {
                classes.add((String) ACTION_TO_CLASS.get(action));
            }
        }
        GatherToolClassesEvent event = new GatherToolClassesEvent(stack, classes);
        MinecraftForge.EVENT_BUS.post(event);
        return classes;
    }

    private boolean findReplacement(IItemHandler inv, Player player, int lowerBound, int upperBound, int currSlot, Predicate<ItemStack> match) {
        synchronized (this.mutex) {
            for (int i = lowerBound; i < upperBound; i++) {
                if (i != currSlot) {
                    ItemStack stackAt = inv.getStackInSlot(i);
                    if (!stackAt.isEmpty() && match.test(stackAt)) {
                        this.pushReplace(player, inv, i, currSlot);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private void pushReplace(Player player, IItemHandler inv, int slot1, int slot2) {
        if (!replacements.containsKey(player)) {
            replacements.put(player, new Stack());
        }
        ((Stack) replacements.get(player)).push(new AutomaticToolRestockModule.QueuedRestock(inv, slot1, slot2));
    }

    private void switchItems(Player player, AutomaticToolRestockModule.QueuedRestock restock) {
        Inventory playerInv = player.getInventory();
        IItemHandler providingInv = restock.providingInv;
        int providingSlot = restock.providingSlot;
        int playerSlot = restock.playerSlot;
        if (providingSlot < providingInv.getSlots() && playerSlot < playerInv.items.size()) {
            ItemStack stackAtPlayerSlot = playerInv.getItem(playerSlot).copy();
            ItemStack stackProvidingSlot = providingInv.getStackInSlot(providingSlot).copy();
            if (!this.itemIgnored(stackAtPlayerSlot) && !this.itemIgnored(stackProvidingSlot)) {
                providingInv.extractItem(providingSlot, stackProvidingSlot.getCount(), false);
                providingInv.insertItem(providingSlot, stackAtPlayerSlot, false);
                playerInv.setItem(playerSlot, stackProvidingSlot);
            }
        }
    }

    private boolean itemIgnored(ItemStack stack) {
        return stack != null && !stack.is(Items.AIR) && this.itemsToIgnore.contains(stack.getItem());
    }

    private List<Enchantment> getImportantEnchantments(ItemStack stack) {
        List<Enchantment> enchantsOnStack = new ArrayList();
        for (Enchantment ench : this.importantEnchants) {
            if (EnchantmentHelper.getItemEnchantmentLevel(ench, stack) > 0) {
                enchantsOnStack.add(ench);
            }
        }
        return enchantsOnStack;
    }

    private static List<String> generateDefaultEnchantmentList() {
        Enchantment[] enchants = new Enchantment[] { Enchantments.SILK_TOUCH, Enchantments.BLOCK_FORTUNE, Enchantments.INFINITY_ARROWS, Enchantments.FISHING_LUCK, Enchantments.MOB_LOOTING };
        List<String> strings = new ArrayList();
        for (Enchantment e : enchants) {
            strings.add(BuiltInRegistries.ENCHANTMENT.getKey(e).toString());
        }
        return strings;
    }

    static {
        ACTION_TO_CLASS.put(ToolActions.AXE_DIG, "axe");
        ACTION_TO_CLASS.put(ToolActions.HOE_DIG, "hoe");
        ACTION_TO_CLASS.put(ToolActions.SHOVEL_DIG, "shovel");
        ACTION_TO_CLASS.put(ToolActions.PICKAXE_DIG, "pickaxe");
        ACTION_TO_CLASS.put(ToolActions.SWORD_SWEEP, "sword");
        ACTION_TO_CLASS.put(ToolActions.SHEARS_HARVEST, "shears");
        ACTION_TO_CLASS.put(ToolActions.FISHING_ROD_CAST, "fishing_rod");
    }

    private static record QueuedRestock(IItemHandler providingInv, int providingSlot, int playerSlot) {
    }

    private static record RestockContext(ServerPlayer player, int currSlot, List<Enchantment> enchantmentsOnStack, Predicate<ItemStack> itemPredicate, Predicate<ItemStack> enchantmentPredicate, Optional<Predicate<ItemStack>> toolPredicate) {
    }
}