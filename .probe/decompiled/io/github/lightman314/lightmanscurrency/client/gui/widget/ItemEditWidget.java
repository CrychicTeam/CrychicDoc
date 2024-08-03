package io.github.lightman314.lightmanscurrency.client.gui.widget;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreenHelper;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.restrictions.ItemTradeRestriction;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemEditWidget extends EasyWidgetWithChildren implements IScrollable, ITooltipSource {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/item_edit.png");

    private static ItemEditWidget latestInstance = null;

    private static boolean rebuilding = false;

    private static final List<Function<CreativeModeTab, Boolean>> ITEM_GROUP_BLACKLIST = new ArrayList();

    private static final List<Predicate<ItemStack>> ITEM_BLACKLIST = Lists.newArrayList(new Predicate[] { s -> s.getItem() instanceof TicketItem });

    private static final List<ItemEditWidget.ItemInsertRule> ITEM_ADDITIONS = new ArrayList();

    private int scroll = 0;

    private int stackCount = 1;

    private final int columns;

    private final int rows;

    public int searchOffX;

    public int searchOffY;

    public int stackSizeOffX;

    public int stackSizeOffY;

    private static final List<ItemStack> allItems = new ArrayList();

    private static final Map<ResourceLocation, List<ItemStack>> preFilteredItems = new HashMap();

    private List<ItemStack> searchResultItems = new ArrayList();

    private String searchString;

    EditBox searchInput;

    ScrollListener stackScrollListener;

    private final ItemEditWidget.IItemEditListener listener;

    private final Font font;

    private final ItemEditWidget oldItemEdit;

    public static void BlacklistCreativeTabs(CreativeModeTab... tabs) {
        for (CreativeModeTab tab : tabs) {
            BlacklistCreativeTab(t -> tab == t);
        }
    }

    @SafeVarargs
    public static void BlacklistCreativeTabs(ResourceKey<CreativeModeTab>... tabs) {
        for (ResourceKey<CreativeModeTab> tab : tabs) {
            BlacklistCreativeTab(t -> BuiltInRegistries.CREATIVE_MODE_TAB.get(tab) == t);
        }
    }

    @SafeVarargs
    public static void BlacklistCreativeTabs(RegistryObject<CreativeModeTab>... tabs) {
        for (RegistryObject<CreativeModeTab> tab : tabs) {
            BlacklistCreativeTab(t -> tab.get() == t);
        }
    }

    public static void BlacklistCreativeTab(Function<CreativeModeTab, Boolean> tabMatcher) {
        if (!ITEM_GROUP_BLACKLIST.contains(tabMatcher)) {
            ITEM_GROUP_BLACKLIST.add(tabMatcher);
        }
    }

    public static boolean IsCreativeTabAllowed(CreativeModeTab tab) {
        for (Function<CreativeModeTab, Boolean> test : ITEM_GROUP_BLACKLIST) {
            if ((Boolean) test.apply(tab)) {
                return false;
            }
        }
        return true;
    }

    public static void BlacklistItem(RegistryObject<? extends ItemLike> item) {
        BlacklistItem(item.get());
    }

    public static void BlacklistItem(ItemLike item) {
        BlacklistItem(s -> s.getItem() == item.asItem());
    }

    public static void BlacklistItem(Predicate<ItemStack> itemFilter) {
        if (!ITEM_BLACKLIST.contains(itemFilter)) {
            ITEM_BLACKLIST.add(itemFilter);
        }
    }

    public static void AddExtraItem(ItemStack item) {
        ITEM_ADDITIONS.add(ItemEditWidget.ItemInsertRule.atEnd(item));
    }

    public static void AddExtraItemAfter(ItemStack item, @Nonnull Item afterItem) {
        ITEM_ADDITIONS.add(ItemEditWidget.ItemInsertRule.afterItem(item, afterItem));
    }

    public static void AddExtraItemAfter(ItemStack item, @Nonnull Predicate<ItemStack> afterItem) {
        ITEM_ADDITIONS.add(ItemEditWidget.ItemInsertRule.afterCheck(item, afterItem));
    }

    public static void AddExtraItemBefore(ItemStack item, @Nonnull Item beforeItem) {
        ITEM_ADDITIONS.add(ItemEditWidget.ItemInsertRule.beforeItem(item, beforeItem));
    }

    public static void AddExtraItemBefore(ItemStack item, @Nonnull Predicate<ItemStack> beforeItem) {
        ITEM_ADDITIONS.add(ItemEditWidget.ItemInsertRule.beforeCheck(item, beforeItem));
    }

    public static boolean isItemAllowed(ItemStack item) {
        for (Predicate<ItemStack> blacklist : ITEM_BLACKLIST) {
            if (blacklist.test(item)) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private EditBox getOldSearchInput() {
        return this.oldItemEdit != null ? this.oldItemEdit.searchInput : null;
    }

    private String getOldSearchString() {
        return this.oldItemEdit != null ? this.oldItemEdit.searchString : "";
    }

    public ItemEditWidget(ScreenPosition pos, int columns, int rows, @Nullable ItemEditWidget oldItemEdit, ItemEditWidget.IItemEditListener listener) {
        this(pos.x, pos.y, columns, rows, oldItemEdit, listener);
    }

    public ItemEditWidget(int x, int y, int columns, int rows, @Nullable ItemEditWidget oldItemEdit, ItemEditWidget.IItemEditListener listener) {
        super(x, y, columns * 18, rows * 18);
        latestInstance = this;
        this.listener = listener;
        this.oldItemEdit = oldItemEdit;
        this.columns = columns;
        this.rows = rows;
        this.searchOffX = this.f_93618_ - 90;
        this.searchOffY = -13;
        this.stackSizeOffX = this.f_93618_ + 13;
        this.stackSizeOffY = 0;
        Minecraft mc = Minecraft.getInstance();
        this.font = mc.font;
        ConfirmItemListLoaded();
        this.modifySearch(this.getOldSearchString());
        if (this.oldItemEdit != null) {
            this.setScroll(this.oldItemEdit.scroll);
        }
    }

    public ItemEditWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    public static void ConfirmItemListLoaded() {
        if (allItems.isEmpty()) {
            rebuilding = true;
        }
        new Thread(ItemEditWidget::safeInitItemList).start();
    }

    public static void safeInitItemList() {
        try {
            initItemList();
        } catch (Throwable var1) {
            LightmansCurrency.LogError("Error occurred while attempting to set up the Item List!\nPlease report this error to the relevant mod author (if another mod is mentioned in the error), not to the Lightman's Currency Dev!", var1);
        }
        rebuilding = false;
    }

    private static void initItemList() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null) {
            LocalPlayer player = mc.player;
            if (player != null) {
                FeatureFlagSet flagSet = player.connection.enabledFeatures();
                boolean hasPermissions = mc.options.operatorItemsTab().get() && player.m_36337_();
                RegistryAccess lookup = mc.player.m_9236_().registryAccess();
                if (!CreativeModeTabs.tryRebuildTabContents(flagSet, hasPermissions, lookup) && !allItems.isEmpty()) {
                    LightmansCurrency.LogDebug("Creative Tab Contents have not changed. Used existing filtered results.");
                } else {
                    LightmansCurrency.LogInfo("Pre-filtering item list for Item Edit items.");
                    rebuilding = true;
                    allItems.clear();
                    for (CreativeModeTab creativeTab : CreativeModeTabs.allTabs()) {
                        if (IsCreativeTabAllowed(creativeTab)) {
                            try {
                                for (ItemStack stack : creativeTab.getDisplayItems()) {
                                    if (isItemAllowed(stack)) {
                                        addToList(stack);
                                        if (stack.getItem() == Items.ENCHANTED_BOOK) {
                                            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                                            enchantments.forEach((enchantment, level) -> {
                                                for (int newLevel = level - 1; newLevel > 0; newLevel--) {
                                                    ItemStack newBook = new ItemStack(Items.ENCHANTED_BOOK);
                                                    EnchantmentHelper.setEnchantments(ImmutableMap.of(enchantment, newLevel), newBook);
                                                    if (isItemAllowed(newBook)) {
                                                        addToList(newBook);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            } catch (Throwable var10) {
                                LightmansCurrency.LogError("Error getting display items from the '" + creativeTab.getDisplayName().getString() + "' tab!\nThis tab will be ignored!", var10);
                            }
                        }
                    }
                    for (ItemEditWidget.ItemInsertRule extraItemRule : ITEM_ADDITIONS) {
                        if (extraItemRule.shouldInsertAtEnd()) {
                            ItemStack extraItem = extraItemRule.insertStack.copy();
                            if (isItemAllowed(extraItem) && notYetInList(extraItem)) {
                                allItems.add(extraItem.copy());
                            }
                        }
                    }
                    preFilteredItems.clear();
                    ItemTradeRestriction.forEach((type, restriction) -> preFilteredItems.put(type, (List) allItems.stream().filter(restriction::allowItemSelectItem).collect(Collectors.toList())));
                    if (latestInstance != null) {
                        latestInstance.refreshSearch();
                    }
                }
            }
        }
    }

    private static void addToList(ItemStack stack) {
        stack = stack.copy();
        if (notYetInList(stack)) {
            for (ItemEditWidget.ItemInsertRule insertRule : ITEM_ADDITIONS) {
                if (insertRule.shouldInsertBefore(stack)) {
                    ItemStack extraItem = insertRule.insertStack.copy();
                    if (isItemAllowed(extraItem) && notYetInList(extraItem)) {
                        allItems.add(extraItem);
                    }
                }
            }
            allItems.add(stack);
            for (ItemEditWidget.ItemInsertRule insertRulex : ITEM_ADDITIONS) {
                if (insertRulex.shouldInsertAfter(stack)) {
                    ItemStack extraItem = insertRulex.insertStack.copy();
                    if (isItemAllowed(extraItem) && notYetInList(extraItem)) {
                        allItems.add(extraItem);
                    }
                }
            }
        }
    }

    private static boolean notYetInList(ItemStack stack) {
        return allItems.stream().noneMatch(s -> InventoryUtil.ItemMatches(s, stack));
    }

    @Nonnull
    private List<ItemStack> getFilteredItems() {
        if (this.listener.restrictItemEditItems()) {
            ItemTradeData trade = this.listener.getTrade();
            ItemTradeRestriction restriction = trade == null ? ItemTradeRestriction.NONE : trade.getRestriction();
            return this.getFilteredItems(restriction);
        } else {
            return new ArrayList(allItems);
        }
    }

    @Nonnull
    private List<ItemStack> getFilteredItems(ItemTradeRestriction restriction) {
        if (rebuilding) {
            return new ArrayList();
        } else {
            ResourceLocation type = ItemTradeRestriction.getId(restriction);
            if (type == ItemTradeRestriction.NO_RESTRICTION_KEY && restriction != ItemTradeRestriction.NONE) {
                LightmansCurrency.LogWarning("Item Trade Restriction of class '" + restriction.getClass().getSimpleName() + "' was not registered, and is now being used to filter items.\nPlease register during the common setup so that this filtering can be done before the screen is opened to prevent in-game lag.");
                return (List<ItemStack>) new ArrayList(allItems).stream().filter(restriction::allowItemSelectItem).collect(Collectors.toList());
            } else {
                if (!preFilteredItems.containsKey(type)) {
                    LightmansCurrency.LogWarning("Item Trade Restriction of type '" + type + "' was registered AFTER the Player logged-in to the world. Please ensure that they're registered during the common setup phase so that filtering can be done at a less critical time.");
                    preFilteredItems.put(type, (List) new ArrayList(allItems).stream().filter(restriction::allowItemSelectItem).collect(Collectors.toList()));
                }
                return (List<ItemStack>) preFilteredItems.get(type);
            }
        }
    }

    @Override
    public int getMaxScroll() {
        return Math.max((this.searchResultItems.size() - 1) / this.columns - this.rows + 1, 0);
    }

    public void refreshPage() {
        this.validateScroll();
        int startIndex = this.scroll * this.columns;
        for (int i = 0; i < this.rows * this.columns; i++) {
            int thisIndex = startIndex + i;
            if (thisIndex < this.searchResultItems.size()) {
                ItemStack stack = ((ItemStack) this.searchResultItems.get(thisIndex)).copy();
                stack.setCount(MathUtil.clamp(this.stackCount, 1, stack.getMaxStackSize()));
            }
        }
    }

    public void refreshSearch() {
        this.modifySearch(this.searchString);
    }

    public void modifySearch(@Nonnull String newSearch) {
        this.searchString = newSearch.toLowerCase();
        if (!this.searchString.isEmpty()) {
            this.searchResultItems = new ArrayList();
            for (ItemStack stack : this.getFilteredItems()) {
                if (stack.getHoverName().getString().toLowerCase().contains(this.searchString)) {
                    this.searchResultItems.add(stack);
                } else if (ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().contains(this.searchString)) {
                    this.searchResultItems.add(stack);
                } else {
                    AtomicReference<Boolean> enchantmentMatch = new AtomicReference(false);
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                    enchantments.forEach((enchantment, level) -> {
                        if (ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString().contains(this.searchString)) {
                            enchantmentMatch.set(true);
                        } else if (enchantment.getFullname(level).getString().toLowerCase().contains(this.searchString)) {
                            enchantmentMatch.set(true);
                        }
                    });
                    if ((Boolean) enchantmentMatch.get()) {
                        this.searchResultItems.add(stack);
                    }
                }
            }
        } else {
            this.searchResultItems = this.getFilteredItems();
        }
        this.refreshPage();
    }

    @Override
    public void addChildren() {
        this.searchInput = this.addChild(new EditBox(this.font, this.m_252754_() + this.searchOffX + 2, this.m_252907_() + this.searchOffY + 2, 79, 9, this.getOldSearchInput(), LCText.GUI_ITEM_EDIT_SEARCH.get()));
        this.searchInput.setBordered(false);
        this.searchInput.setMaxLength(32);
        this.searchInput.setTextColor(16777215);
        this.searchInput.setResponder(this::modifySearch);
        this.stackScrollListener = this.addChild(new ScrollListener(this.m_252754_() + this.stackSizeOffX, this.m_252907_() + this.stackSizeOffY, 18, 18, this::stackCountScroll));
    }

    @Override
    protected void renderTick() {
        this.searchInput.f_93624_ = this.f_93624_;
        this.stackScrollListener.active = this.f_93624_;
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        int index = this.scroll * this.columns;
        for (int y = 0; y < this.rows && index < this.searchResultItems.size(); y++) {
            int yPos = y * 18;
            for (int x = 0; x < this.columns && index < this.searchResultItems.size(); x++) {
                int xPos = x * 18;
                gui.resetColor();
                gui.blit(GUI_TEXTURE, xPos, yPos, 0, 0, 18, 18);
                gui.renderItem(this.getQuantityFixedStack((ItemStack) this.searchResultItems.get(index)), xPos + 1, yPos + 1);
                index++;
            }
        }
        gui.resetColor();
        gui.blit(GUI_TEXTURE, this.searchOffX, this.searchOffY, 18, 0, 90, 12);
        gui.blit(GUI_TEXTURE, this.stackSizeOffX, this.stackSizeOffY, 108, 0, 18, 18);
    }

    private ItemStack getQuantityFixedStack(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(Math.min(stack.getMaxStackSize(), this.stackCount));
        return copy;
    }

    @Override
    public List<Component> getTooltipText(int mouseX, int mouseY) {
        if (!this.isVisible()) {
            return null;
        } else {
            int hoveredSlot = this.isMouseOverSlot((double) mouseX, (double) mouseY);
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * this.columns;
                if (hoveredSlot < this.searchResultItems.size()) {
                    return EasyScreenHelper.getTooltipFromItem((ItemStack) this.searchResultItems.get(hoveredSlot));
                }
            }
            return this.isMouseOverStackSizeScroll(mouseX, mouseY) ? LCText.TOOLTIP_ITEM_EDIT_SCROLL.getAsList() : null;
        }
    }

    private boolean isMouseOverStackSizeScroll(int mouseX, int mouseY) {
        return mouseX >= this.m_252754_() + this.stackSizeOffX && mouseX < this.m_252754_() + this.stackSizeOffX + 18 && mouseY >= this.m_252907_() + this.stackSizeOffY && mouseY < this.m_252907_() + this.stackSizeOffY + 18;
    }

    private int isMouseOverSlot(double mouseX, double mouseY) {
        if (!this.isVisible()) {
            return -1;
        } else {
            int foundColumn = -1;
            int foundRow = -1;
            for (int x = 0; x < this.columns && foundColumn < 0; x++) {
                if (mouseX >= (double) (this.m_252754_() + x * 18) && mouseX < (double) (this.m_252754_() + x * 18 + 18)) {
                    foundColumn = x;
                }
            }
            for (int y = 0; y < this.rows && foundRow < 0; y++) {
                if (mouseY >= (double) (this.m_252907_() + y * 18) && mouseY < (double) (this.m_252907_() + y * 18 + 18)) {
                    foundRow = y;
                }
            }
            return foundColumn >= 0 && foundRow >= 0 ? foundRow * this.columns + foundColumn : -1;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int hoveredSlot = this.isMouseOverSlot(mouseX, mouseY);
        if (hoveredSlot >= 0) {
            hoveredSlot += this.scroll * this.columns;
            if (hoveredSlot < this.searchResultItems.size()) {
                ItemStack stack = this.getQuantityFixedStack((ItemStack) this.searchResultItems.get(hoveredSlot));
                this.listener.onItemClicked(stack);
                return true;
            }
        }
        return false;
    }

    public boolean stackCountScroll(double delta) {
        if (delta > 0.0) {
            if (this.stackCount < 64) {
                this.stackCount++;
            }
        } else if (delta < 0.0 && this.stackCount > 1) {
            this.stackCount--;
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta < 0.0) {
            if (this.scroll >= this.getMaxScroll()) {
                return false;
            }
            this.scroll++;
        } else if (delta > 0.0) {
            if (this.scroll <= 0) {
                return false;
            }
            this.scroll--;
        }
        return true;
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
        this.refreshPage();
    }

    public interface IItemEditListener {

        ItemTradeData getTrade();

        boolean restrictItemEditItems();

        void onItemClicked(ItemStack var1);
    }

    private static class ItemInsertRule {

        public final ItemStack insertStack;

        private final Predicate<ItemStack> afterItemCheck;

        private final Predicate<ItemStack> beforeItemCheck;

        private final Predicate<ItemStack> NULLCHECK = s -> false;

        private ItemInsertRule(ItemStack insertStack, @Nullable Predicate<ItemStack> afterItemCheck, @Nullable Predicate<ItemStack> beforeItemCheck) {
            this.insertStack = insertStack;
            this.afterItemCheck = afterItemCheck == null ? this.NULLCHECK : afterItemCheck;
            this.beforeItemCheck = beforeItemCheck == null ? this.NULLCHECK : beforeItemCheck;
        }

        public static ItemEditWidget.ItemInsertRule atEnd(ItemStack insertStack) {
            return new ItemEditWidget.ItemInsertRule(insertStack, null, null);
        }

        public static ItemEditWidget.ItemInsertRule afterItem(ItemStack insertStack, @Nonnull Item item) {
            return new ItemEditWidget.ItemInsertRule(insertStack, s -> s.getItem() == item, null);
        }

        public static ItemEditWidget.ItemInsertRule afterCheck(ItemStack insertStack, @Nonnull Predicate<ItemStack> check) {
            return new ItemEditWidget.ItemInsertRule(insertStack, check, null);
        }

        public static ItemEditWidget.ItemInsertRule beforeItem(ItemStack insertStack, @Nonnull Item item) {
            return new ItemEditWidget.ItemInsertRule(insertStack, null, s -> s.getItem() == item);
        }

        public static ItemEditWidget.ItemInsertRule beforeCheck(ItemStack insertStack, @Nonnull Predicate<ItemStack> check) {
            return new ItemEditWidget.ItemInsertRule(insertStack, null, check);
        }

        public boolean shouldInsertBefore(ItemStack insertedItem) {
            return this.beforeItemCheck.test(insertedItem);
        }

        public boolean shouldInsertAfter(ItemStack insertedItem) {
            return this.afterItemCheck.test(insertedItem);
        }

        public boolean shouldInsertAtEnd() {
            return this.afterItemCheck == this.NULLCHECK && this.beforeItemCheck == null;
        }
    }
}