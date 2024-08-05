package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.HotbarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.inventory.Hotbar;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

public class CreativeModeInventoryScreen extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    private static final ResourceLocation CREATIVE_TABS_LOCATION = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    private static final String GUI_CREATIVE_TAB_PREFIX = "textures/gui/container/creative_inventory/tab_";

    private static final String CUSTOM_SLOT_LOCK = "CustomCreativeLock";

    private static final int NUM_ROWS = 5;

    private static final int NUM_COLS = 9;

    private static final int TAB_WIDTH = 26;

    private static final int TAB_HEIGHT = 32;

    private static final int SCROLLER_WIDTH = 12;

    private static final int SCROLLER_HEIGHT = 15;

    static final SimpleContainer CONTAINER = new SimpleContainer(45);

    private static final Component TRASH_SLOT_TOOLTIP = Component.translatable("inventory.binSlot");

    private static final int TEXT_COLOR = 16777215;

    private static CreativeModeTab selectedTab = CreativeModeTabs.getDefaultTab();

    private float scrollOffs;

    private boolean scrolling;

    private EditBox searchBox;

    @Nullable
    private List<Slot> originalSlots;

    @Nullable
    private Slot destroyItemSlot;

    private CreativeInventoryListener listener;

    private boolean ignoreTextInput;

    private boolean hasClickedOutside;

    private final Set<TagKey<Item>> visibleTags = new HashSet();

    private final boolean displayOperatorCreativeTab;

    public CreativeModeInventoryScreen(Player player0, FeatureFlagSet featureFlagSet1, boolean boolean2) {
        super(new CreativeModeInventoryScreen.ItemPickerMenu(player0), player0.getInventory(), CommonComponents.EMPTY);
        player0.containerMenu = this.f_97732_;
        this.f_97727_ = 136;
        this.f_97726_ = 195;
        this.displayOperatorCreativeTab = boolean2;
        CreativeModeTabs.tryRebuildTabContents(featureFlagSet1, this.hasPermissions(player0), player0.m_9236_().registryAccess());
    }

    private boolean hasPermissions(Player player0) {
        return player0.canUseGameMasterBlocks() && this.displayOperatorCreativeTab;
    }

    private void tryRefreshInvalidatedTabs(FeatureFlagSet featureFlagSet0, boolean boolean1, HolderLookup.Provider holderLookupProvider2) {
        if (CreativeModeTabs.tryRebuildTabContents(featureFlagSet0, boolean1, holderLookupProvider2)) {
            for (CreativeModeTab $$3 : CreativeModeTabs.allTabs()) {
                Collection<ItemStack> $$4 = $$3.getDisplayItems();
                if ($$3 == selectedTab) {
                    if ($$3.getType() == CreativeModeTab.Type.CATEGORY && $$4.isEmpty()) {
                        this.selectTab(CreativeModeTabs.getDefaultTab());
                    } else {
                        this.refreshCurrentTabContents($$4);
                    }
                }
            }
        }
    }

    private void refreshCurrentTabContents(Collection<ItemStack> collectionItemStack0) {
        int $$1 = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getRowIndexForScroll(this.scrollOffs);
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.clear();
        if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
            this.refreshSearchResults();
        } else {
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.addAll(collectionItemStack0);
        }
        this.scrollOffs = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getScrollForRowIndex($$1);
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(this.scrollOffs);
    }

    @Override
    public void containerTick() {
        super.m_181908_();
        if (this.f_96541_ != null) {
            if (this.f_96541_.player != null) {
                this.tryRefreshInvalidatedTabs(this.f_96541_.player.connection.enabledFeatures(), this.hasPermissions(this.f_96541_.player), this.f_96541_.player.m_9236_().registryAccess());
            }
            if (!this.f_96541_.gameMode.hasInfiniteItems()) {
                this.f_96541_.setScreen(new InventoryScreen(this.f_96541_.player));
            } else {
                this.searchBox.tick();
            }
        }
    }

    @Override
    protected void slotClicked(@Nullable Slot slot0, int int1, int int2, ClickType clickType3) {
        if (this.isCreativeSlot(slot0)) {
            this.searchBox.moveCursorToEnd();
            this.searchBox.setHighlightPos(0);
        }
        boolean $$4 = clickType3 == ClickType.QUICK_MOVE;
        clickType3 = int1 == -999 && clickType3 == ClickType.PICKUP ? ClickType.THROW : clickType3;
        if (slot0 == null && selectedTab.getType() != CreativeModeTab.Type.INVENTORY && clickType3 != ClickType.QUICK_CRAFT) {
            if (!((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried().isEmpty() && this.hasClickedOutside) {
                if (int2 == 0) {
                    this.f_96541_.player.m_36176_(((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried(), true);
                    this.f_96541_.gameMode.handleCreativeModeItemDrop(((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried());
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).setCarried(ItemStack.EMPTY);
                }
                if (int2 == 1) {
                    ItemStack $$18 = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried().split(1);
                    this.f_96541_.player.m_36176_($$18, true);
                    this.f_96541_.gameMode.handleCreativeModeItemDrop($$18);
                }
            }
        } else {
            if (slot0 != null && !slot0.mayPickup(this.f_96541_.player)) {
                return;
            }
            if (slot0 == this.destroyItemSlot && $$4) {
                for (int $$5 = 0; $$5 < this.f_96541_.player.f_36095_.m_38927_().size(); $$5++) {
                    this.f_96541_.gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, $$5);
                }
            } else if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
                if (slot0 == this.destroyItemSlot) {
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).setCarried(ItemStack.EMPTY);
                } else if (clickType3 == ClickType.THROW && slot0 != null && slot0.hasItem()) {
                    ItemStack $$6 = slot0.remove(int2 == 0 ? 1 : slot0.getItem().getMaxStackSize());
                    ItemStack $$7 = slot0.getItem();
                    this.f_96541_.player.m_36176_($$6, true);
                    this.f_96541_.gameMode.handleCreativeModeItemDrop($$6);
                    this.f_96541_.gameMode.handleCreativeModeItemAdd($$7, ((CreativeModeInventoryScreen.SlotWrapper) slot0).target.index);
                } else if (clickType3 == ClickType.THROW && !((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried().isEmpty()) {
                    this.f_96541_.player.m_36176_(((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried(), true);
                    this.f_96541_.gameMode.handleCreativeModeItemDrop(((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried());
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).setCarried(ItemStack.EMPTY);
                } else {
                    this.f_96541_.player.f_36095_.m_150399_(slot0 == null ? int1 : ((CreativeModeInventoryScreen.SlotWrapper) slot0).target.index, int2, clickType3, this.f_96541_.player);
                    this.f_96541_.player.f_36095_.m_38946_();
                }
            } else if (clickType3 != ClickType.QUICK_CRAFT && slot0.container == CONTAINER) {
                ItemStack $$8 = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried();
                ItemStack $$9 = slot0.getItem();
                if (clickType3 == ClickType.SWAP) {
                    if (!$$9.isEmpty()) {
                        this.f_96541_.player.m_150109_().setItem(int2, $$9.copyWithCount($$9.getMaxStackSize()));
                        this.f_96541_.player.f_36095_.m_38946_();
                    }
                    return;
                }
                if (clickType3 == ClickType.CLONE) {
                    if (((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried().isEmpty() && slot0.hasItem()) {
                        ItemStack $$10 = slot0.getItem();
                        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).setCarried($$10.copyWithCount($$10.getMaxStackSize()));
                    }
                    return;
                }
                if (clickType3 == ClickType.THROW) {
                    if (!$$9.isEmpty()) {
                        ItemStack $$11 = $$9.copyWithCount(int2 == 0 ? 1 : $$9.getMaxStackSize());
                        this.f_96541_.player.m_36176_($$11, true);
                        this.f_96541_.gameMode.handleCreativeModeItemDrop($$11);
                    }
                    return;
                }
                if (!$$8.isEmpty() && !$$9.isEmpty() && ItemStack.isSameItemSameTags($$8, $$9)) {
                    if (int2 == 0) {
                        if ($$4) {
                            $$8.setCount($$8.getMaxStackSize());
                        } else if ($$8.getCount() < $$8.getMaxStackSize()) {
                            $$8.grow(1);
                        }
                    } else {
                        $$8.shrink(1);
                    }
                } else if (!$$9.isEmpty() && $$8.isEmpty()) {
                    int $$12 = $$4 ? $$9.getMaxStackSize() : $$9.getCount();
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).setCarried($$9.copyWithCount($$12));
                } else if (int2 == 0) {
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).setCarried(ItemStack.EMPTY);
                } else if (!((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried().isEmpty()) {
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getCarried().shrink(1);
                }
            } else if (this.f_97732_ != null) {
                ItemStack $$13 = slot0 == null ? ItemStack.EMPTY : ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).m_38853_(slot0.index).getItem();
                ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).m_150399_(slot0 == null ? int1 : slot0.index, int2, clickType3, this.f_96541_.player);
                if (AbstractContainerMenu.getQuickcraftHeader(int2) == 2) {
                    for (int $$14 = 0; $$14 < 9; $$14++) {
                        this.f_96541_.gameMode.handleCreativeModeItemAdd(((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).m_38853_(45 + $$14).getItem(), 36 + $$14);
                    }
                } else if (slot0 != null) {
                    ItemStack $$15 = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).m_38853_(slot0.index).getItem();
                    this.f_96541_.gameMode.handleCreativeModeItemAdd($$15, slot0.index - ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.size() + 9 + 36);
                    int $$16 = 45 + int2;
                    if (clickType3 == ClickType.SWAP) {
                        this.f_96541_.gameMode.handleCreativeModeItemAdd($$13, $$16 - ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.size() + 9 + 36);
                    } else if (clickType3 == ClickType.THROW && !$$13.isEmpty()) {
                        ItemStack $$17 = $$13.copyWithCount(int2 == 0 ? 1 : $$13.getMaxStackSize());
                        this.f_96541_.player.m_36176_($$17, true);
                        this.f_96541_.gameMode.handleCreativeModeItemDrop($$17);
                    }
                    this.f_96541_.player.f_36095_.m_38946_();
                }
            }
        }
    }

    private boolean isCreativeSlot(@Nullable Slot slot0) {
        return slot0 != null && slot0.container == CONTAINER;
    }

    @Override
    protected void init() {
        if (this.f_96541_.gameMode.hasInfiniteItems()) {
            super.m_7856_();
            this.searchBox = new EditBox(this.f_96547_, this.f_97735_ + 82, this.f_97736_ + 6, 80, 9, Component.translatable("itemGroup.search"));
            this.searchBox.setMaxLength(50);
            this.searchBox.setBordered(false);
            this.searchBox.setVisible(false);
            this.searchBox.setTextColor(16777215);
            this.m_7787_(this.searchBox);
            CreativeModeTab $$0 = selectedTab;
            selectedTab = CreativeModeTabs.getDefaultTab();
            this.selectTab($$0);
            this.f_96541_.player.f_36095_.m_38943_(this.listener);
            this.listener = new CreativeInventoryListener(this.f_96541_);
            this.f_96541_.player.f_36095_.m_38893_(this.listener);
            if (!selectedTab.shouldDisplay()) {
                this.selectTab(CreativeModeTabs.getDefaultTab());
            }
        } else {
            this.f_96541_.setScreen(new InventoryScreen(this.f_96541_.player));
        }
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        int $$3 = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getRowIndexForScroll(this.scrollOffs);
        String $$4 = this.searchBox.getValue();
        this.m_6575_(minecraft0, int1, int2);
        this.searchBox.setValue($$4);
        if (!this.searchBox.getValue().isEmpty()) {
            this.refreshSearchResults();
        }
        this.scrollOffs = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).getScrollForRowIndex($$3);
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(this.scrollOffs);
    }

    @Override
    public void removed() {
        super.m_7861_();
        if (this.f_96541_.player != null && this.f_96541_.player.m_150109_() != null) {
            this.f_96541_.player.f_36095_.m_38943_(this.listener);
        }
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        if (this.ignoreTextInput) {
            return false;
        } else if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
            return false;
        } else {
            String $$2 = this.searchBox.getValue();
            if (this.searchBox.charTyped(char0, int1)) {
                if (!Objects.equals($$2, this.searchBox.getValue())) {
                    this.refreshSearchResults();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        this.ignoreTextInput = false;
        if (selectedTab.getType() != CreativeModeTab.Type.SEARCH) {
            if (this.f_96541_.options.keyChat.matches(int0, int1)) {
                this.ignoreTextInput = true;
                this.selectTab(CreativeModeTabs.searchTab());
                return true;
            } else {
                return super.m_7933_(int0, int1, int2);
            }
        } else {
            boolean $$3 = !this.isCreativeSlot(this.f_97734_) || this.f_97734_.hasItem();
            boolean $$4 = InputConstants.getKey(int0, int1).getNumericKeyValue().isPresent();
            if ($$3 && $$4 && this.m_97805_(int0, int1)) {
                this.ignoreTextInput = true;
                return true;
            } else {
                String $$5 = this.searchBox.getValue();
                if (this.searchBox.keyPressed(int0, int1, int2)) {
                    if (!Objects.equals($$5, this.searchBox.getValue())) {
                        this.refreshSearchResults();
                    }
                    return true;
                } else {
                    return this.searchBox.m_93696_() && this.searchBox.isVisible() && int0 != 256 ? true : super.m_7933_(int0, int1, int2);
                }
            }
        }
    }

    @Override
    public boolean keyReleased(int int0, int int1, int int2) {
        this.ignoreTextInput = false;
        return super.m_7920_(int0, int1, int2);
    }

    private void refreshSearchResults() {
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.clear();
        this.visibleTags.clear();
        String $$0 = this.searchBox.getValue();
        if ($$0.isEmpty()) {
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.addAll(selectedTab.getDisplayItems());
        } else {
            SearchTree<ItemStack> $$1;
            if ($$0.startsWith("#")) {
                $$0 = $$0.substring(1);
                $$1 = this.f_96541_.getSearchTree(SearchRegistry.CREATIVE_TAGS);
                this.updateVisibleTags($$0);
            } else {
                $$1 = this.f_96541_.getSearchTree(SearchRegistry.CREATIVE_NAMES);
            }
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.addAll($$1.search($$0.toLowerCase(Locale.ROOT)));
        }
        this.scrollOffs = 0.0F;
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(0.0F);
    }

    private void updateVisibleTags(String string0) {
        int $$1 = string0.indexOf(58);
        Predicate<ResourceLocation> $$2;
        if ($$1 == -1) {
            $$2 = p_98609_ -> p_98609_.getPath().contains(string0);
        } else {
            String $$3 = string0.substring(0, $$1).trim();
            String $$4 = string0.substring($$1 + 1).trim();
            $$2 = p_98606_ -> p_98606_.getNamespace().contains($$3) && p_98606_.getPath().contains($$4);
        }
        BuiltInRegistries.ITEM.m_203613_().filter(p_205410_ -> $$2.test(p_205410_.location())).forEach(this.visibleTags::add);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        if (selectedTab.showTitle()) {
            guiGraphics0.drawString(this.f_96547_, selectedTab.getDisplayName(), 8, 6, 4210752, false);
        }
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (int2 == 0) {
            double $$3 = double0 - (double) this.f_97735_;
            double $$4 = double1 - (double) this.f_97736_;
            for (CreativeModeTab $$5 : CreativeModeTabs.tabs()) {
                if (this.checkTabClicked($$5, $$3, $$4)) {
                    return true;
                }
            }
            if (selectedTab.getType() != CreativeModeTab.Type.INVENTORY && this.insideScrollbar(double0, double1)) {
                this.scrolling = this.canScroll();
                return true;
            }
        }
        return super.m_6375_(double0, double1, int2);
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        if (int2 == 0) {
            double $$3 = double0 - (double) this.f_97735_;
            double $$4 = double1 - (double) this.f_97736_;
            this.scrolling = false;
            for (CreativeModeTab $$5 : CreativeModeTabs.tabs()) {
                if (this.checkTabClicked($$5, $$3, $$4)) {
                    this.selectTab($$5);
                    return true;
                }
            }
        }
        return super.m_6348_(double0, double1, int2);
    }

    private boolean canScroll() {
        return selectedTab.canScroll() && ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).canScroll();
    }

    private void selectTab(CreativeModeTab creativeModeTab0) {
        CreativeModeTab $$1 = selectedTab;
        selectedTab = creativeModeTab0;
        this.f_97737_.clear();
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.clear();
        this.m_238391_();
        if (selectedTab.getType() == CreativeModeTab.Type.HOTBAR) {
            HotbarManager $$2 = this.f_96541_.getHotbarManager();
            for (int $$3 = 0; $$3 < 9; $$3++) {
                Hotbar $$4 = $$2.get($$3);
                if ($$4.isEmpty()) {
                    for (int $$5 = 0; $$5 < 9; $$5++) {
                        if ($$5 == $$3) {
                            ItemStack $$6 = new ItemStack(Items.PAPER);
                            $$6.getOrCreateTagElement("CustomCreativeLock");
                            Component $$7 = this.f_96541_.options.keyHotbarSlots[$$3].getTranslatedKeyMessage();
                            Component $$8 = this.f_96541_.options.keySaveHotbarActivator.getTranslatedKeyMessage();
                            $$6.setHoverName(Component.translatable("inventory.hotbarInfo", $$8, $$7));
                            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.add($$6);
                        } else {
                            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.add(ItemStack.EMPTY);
                        }
                    }
                } else {
                    ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.addAll($$4);
                }
            }
        } else if (selectedTab.getType() == CreativeModeTab.Type.CATEGORY) {
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).items.addAll(selectedTab.getDisplayItems());
        }
        if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
            AbstractContainerMenu $$9 = this.f_96541_.player.f_36095_;
            if (this.originalSlots == null) {
                this.originalSlots = ImmutableList.copyOf(((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_);
            }
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.clear();
            for (int $$10 = 0; $$10 < $$9.slots.size(); $$10++) {
                int $$14;
                int $$15;
                if ($$10 >= 5 && $$10 < 9) {
                    int $$11 = $$10 - 5;
                    int $$12 = $$11 / 2;
                    int $$13 = $$11 % 2;
                    $$14 = 54 + $$12 * 54;
                    $$15 = 6 + $$13 * 27;
                } else if ($$10 >= 0 && $$10 < 5) {
                    $$14 = -2000;
                    $$15 = -2000;
                } else if ($$10 == 45) {
                    $$14 = 35;
                    $$15 = 20;
                } else {
                    int $$20 = $$10 - 9;
                    int $$21 = $$20 % 9;
                    int $$22 = $$20 / 9;
                    $$14 = 9 + $$21 * 18;
                    if ($$10 >= 36) {
                        $$15 = 112;
                    } else {
                        $$15 = 54 + $$22 * 18;
                    }
                }
                Slot $$26 = new CreativeModeInventoryScreen.SlotWrapper($$9.slots.get($$10), $$10, $$14, $$15);
                ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.add($$26);
            }
            this.destroyItemSlot = new Slot(CONTAINER, 0, 173, 112);
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.add(this.destroyItemSlot);
        } else if ($$1.getType() == CreativeModeTab.Type.INVENTORY) {
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.clear();
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).f_38839_.addAll(this.originalSlots);
            this.originalSlots = null;
        }
        if (selectedTab.getType() == CreativeModeTab.Type.SEARCH) {
            this.searchBox.setVisible(true);
            this.searchBox.setCanLoseFocus(false);
            this.searchBox.setFocused(true);
            if ($$1 != creativeModeTab0) {
                this.searchBox.setValue("");
            }
            this.refreshSearchResults();
        } else {
            this.searchBox.setVisible(false);
            this.searchBox.setCanLoseFocus(true);
            this.searchBox.setFocused(false);
            this.searchBox.setValue("");
        }
        this.scrollOffs = 0.0F;
        ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(0.0F);
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        if (!this.canScroll()) {
            return false;
        } else {
            this.scrollOffs = ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).subtractInputFromScroll(this.scrollOffs, double2);
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(this.scrollOffs);
            return true;
        }
    }

    @Override
    protected boolean hasClickedOutside(double double0, double double1, int int2, int int3, int int4) {
        boolean $$5 = double0 < (double) int2 || double1 < (double) int3 || double0 >= (double) (int2 + this.f_97726_) || double1 >= (double) (int3 + this.f_97727_);
        this.hasClickedOutside = $$5 && !this.checkTabClicked(selectedTab, double0, double1);
        return this.hasClickedOutside;
    }

    protected boolean insideScrollbar(double double0, double double1) {
        int $$2 = this.f_97735_;
        int $$3 = this.f_97736_;
        int $$4 = $$2 + 175;
        int $$5 = $$3 + 18;
        int $$6 = $$4 + 14;
        int $$7 = $$5 + 112;
        return double0 >= (double) $$4 && double1 >= (double) $$5 && double0 < (double) $$6 && double1 < (double) $$7;
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        if (this.scrolling) {
            int $$5 = this.f_97736_ + 18;
            int $$6 = $$5 + 112;
            this.scrollOffs = ((float) double1 - (float) $$5 - 7.5F) / ((float) ($$6 - $$5) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            ((CreativeModeInventoryScreen.ItemPickerMenu) this.f_97732_).scrollTo(this.scrollOffs);
            return true;
        } else {
            return super.m_7979_(double0, double1, int2, double3, double4);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        for (CreativeModeTab $$4 : CreativeModeTabs.tabs()) {
            if (this.checkTabHovering(guiGraphics0, $$4, int1, int2)) {
                break;
            }
        }
        if (this.destroyItemSlot != null && selectedTab.getType() == CreativeModeTab.Type.INVENTORY && this.m_6774_(this.destroyItemSlot.x, this.destroyItemSlot.y, 16, 16, (double) int1, (double) int2)) {
            guiGraphics0.renderTooltip(this.f_96547_, TRASH_SLOT_TOOLTIP, int1, int2);
        }
        this.m_280072_(guiGraphics0, int1, int2);
    }

    @Override
    public List<Component> getTooltipFromContainerItem(ItemStack itemStack0) {
        boolean $$1 = this.f_97734_ != null && this.f_97734_ instanceof CreativeModeInventoryScreen.CustomCreativeSlot;
        boolean $$2 = selectedTab.getType() == CreativeModeTab.Type.CATEGORY;
        boolean $$3 = selectedTab.getType() == CreativeModeTab.Type.SEARCH;
        TooltipFlag.Default $$4 = this.f_96541_.options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_;
        TooltipFlag $$5 = $$1 ? $$4.asCreative() : $$4;
        List<Component> $$6 = itemStack0.getTooltipLines(this.f_96541_.player, $$5);
        if ($$2 && $$1) {
            return $$6;
        } else {
            List<Component> $$7 = Lists.newArrayList($$6);
            if ($$3 && $$1) {
                this.visibleTags.forEach(p_205407_ -> {
                    if (itemStack0.is(p_205407_)) {
                        $$7.add(1, Component.literal("#" + p_205407_.location()).withStyle(ChatFormatting.DARK_PURPLE));
                    }
                });
            }
            int $$8 = 1;
            for (CreativeModeTab $$9 : CreativeModeTabs.tabs()) {
                if ($$9.getType() != CreativeModeTab.Type.SEARCH && $$9.contains(itemStack0)) {
                    $$7.add($$8++, $$9.getDisplayName().copy().withStyle(ChatFormatting.BLUE));
                }
            }
            return $$7;
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        for (CreativeModeTab $$4 : CreativeModeTabs.tabs()) {
            if ($$4 != selectedTab) {
                this.renderTabButton(guiGraphics0, $$4);
            }
        }
        guiGraphics0.blit(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + selectedTab.getBackgroundSuffix()), this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        this.searchBox.m_88315_(guiGraphics0, int2, int3, float1);
        int $$5 = this.f_97735_ + 175;
        int $$6 = this.f_97736_ + 18;
        int $$7 = $$6 + 112;
        if (selectedTab.canScroll()) {
            guiGraphics0.blit(CREATIVE_TABS_LOCATION, $$5, $$6 + (int) ((float) ($$7 - $$6 - 17) * this.scrollOffs), 232 + (this.canScroll() ? 0 : 12), 0, 12, 15);
        }
        this.renderTabButton(guiGraphics0, selectedTab);
        if (selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics0, this.f_97735_ + 88, this.f_97736_ + 45, 20, (float) (this.f_97735_ + 88 - int2), (float) (this.f_97736_ + 45 - 30 - int3), this.f_96541_.player);
        }
    }

    private int getTabX(CreativeModeTab creativeModeTab0) {
        int $$1 = creativeModeTab0.column();
        int $$2 = 27;
        int $$3 = 27 * $$1;
        if (creativeModeTab0.isAlignedRight()) {
            $$3 = this.f_97726_ - 27 * (7 - $$1) + 1;
        }
        return $$3;
    }

    private int getTabY(CreativeModeTab creativeModeTab0) {
        int $$1 = 0;
        if (creativeModeTab0.row() == CreativeModeTab.Row.TOP) {
            $$1 -= 32;
        } else {
            $$1 += this.f_97727_;
        }
        return $$1;
    }

    protected boolean checkTabClicked(CreativeModeTab creativeModeTab0, double double1, double double2) {
        int $$3 = this.getTabX(creativeModeTab0);
        int $$4 = this.getTabY(creativeModeTab0);
        return double1 >= (double) $$3 && double1 <= (double) ($$3 + 26) && double2 >= (double) $$4 && double2 <= (double) ($$4 + 32);
    }

    protected boolean checkTabHovering(GuiGraphics guiGraphics0, CreativeModeTab creativeModeTab1, int int2, int int3) {
        int $$4 = this.getTabX(creativeModeTab1);
        int $$5 = this.getTabY(creativeModeTab1);
        if (this.m_6774_($$4 + 3, $$5 + 3, 21, 27, (double) int2, (double) int3)) {
            guiGraphics0.renderTooltip(this.f_96547_, creativeModeTab1.getDisplayName(), int2, int3);
            return true;
        } else {
            return false;
        }
    }

    protected void renderTabButton(GuiGraphics guiGraphics0, CreativeModeTab creativeModeTab1) {
        boolean $$2 = creativeModeTab1 == selectedTab;
        boolean $$3 = creativeModeTab1.row() == CreativeModeTab.Row.TOP;
        int $$4 = creativeModeTab1.column();
        int $$5 = $$4 * 26;
        int $$6 = 0;
        int $$7 = this.f_97735_ + this.getTabX(creativeModeTab1);
        int $$8 = this.f_97736_;
        int $$9 = 32;
        if ($$2) {
            $$6 += 32;
        }
        if ($$3) {
            $$8 -= 28;
        } else {
            $$6 += 64;
            $$8 += this.f_97727_ - 4;
        }
        guiGraphics0.blit(CREATIVE_TABS_LOCATION, $$7, $$8, $$5, $$6, 26, 32);
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
        $$7 += 5;
        $$8 += 8 + ($$3 ? 1 : -1);
        ItemStack $$10 = creativeModeTab1.getIconItem();
        guiGraphics0.renderItem($$10, $$7, $$8);
        guiGraphics0.renderItemDecorations(this.f_96547_, $$10, $$7, $$8);
        guiGraphics0.pose().popPose();
    }

    public boolean isInventoryOpen() {
        return selectedTab.getType() == CreativeModeTab.Type.INVENTORY;
    }

    public static void handleHotbarLoadOrSave(Minecraft minecraft0, int int1, boolean boolean2, boolean boolean3) {
        LocalPlayer $$4 = minecraft0.player;
        HotbarManager $$5 = minecraft0.getHotbarManager();
        Hotbar $$6 = $$5.get(int1);
        if (boolean2) {
            for (int $$7 = 0; $$7 < Inventory.getSelectionSize(); $$7++) {
                ItemStack $$8 = (ItemStack) $$6.get($$7);
                ItemStack $$9 = $$8.isItemEnabled($$4.m_9236_().m_246046_()) ? $$8.copy() : ItemStack.EMPTY;
                $$4.m_150109_().setItem($$7, $$9);
                minecraft0.gameMode.handleCreativeModeItemAdd($$9, 36 + $$7);
            }
            $$4.f_36095_.m_38946_();
        } else if (boolean3) {
            for (int $$10 = 0; $$10 < Inventory.getSelectionSize(); $$10++) {
                $$6.set($$10, $$4.m_150109_().getItem($$10).copy());
            }
            Component $$11 = minecraft0.options.keyHotbarSlots[int1].getTranslatedKeyMessage();
            Component $$12 = minecraft0.options.keyLoadHotbarActivator.getTranslatedKeyMessage();
            Component $$13 = Component.translatable("inventory.hotbarSaved", $$12, $$11);
            minecraft0.gui.setOverlayMessage($$13, false);
            minecraft0.getNarrator().sayNow($$13);
            $$5.save();
        }
    }

    static class CustomCreativeSlot extends Slot {

        public CustomCreativeSlot(Container container0, int int1, int int2, int int3) {
            super(container0, int1, int2, int3);
        }

        @Override
        public boolean mayPickup(Player player0) {
            ItemStack $$1 = this.m_7993_();
            return super.mayPickup(player0) && !$$1.isEmpty() ? $$1.isItemEnabled(player0.m_9236_().m_246046_()) && $$1.getTagElement("CustomCreativeLock") == null : $$1.isEmpty();
        }
    }

    public static class ItemPickerMenu extends AbstractContainerMenu {

        public final NonNullList<ItemStack> items = NonNullList.create();

        private final AbstractContainerMenu inventoryMenu;

        public ItemPickerMenu(Player player0) {
            super(null, 0);
            this.inventoryMenu = player0.inventoryMenu;
            Inventory $$1 = player0.getInventory();
            for (int $$2 = 0; $$2 < 5; $$2++) {
                for (int $$3 = 0; $$3 < 9; $$3++) {
                    this.m_38897_(new CreativeModeInventoryScreen.CustomCreativeSlot(CreativeModeInventoryScreen.CONTAINER, $$2 * 9 + $$3, 9 + $$3 * 18, 18 + $$2 * 18));
                }
            }
            for (int $$4 = 0; $$4 < 9; $$4++) {
                this.m_38897_(new Slot($$1, $$4, 9 + $$4 * 18, 112));
            }
            this.scrollTo(0.0F);
        }

        @Override
        public boolean stillValid(Player player0) {
            return true;
        }

        protected int calculateRowCount() {
            return Mth.positiveCeilDiv(this.items.size(), 9) - 5;
        }

        protected int getRowIndexForScroll(float float0) {
            return Math.max((int) ((double) (float0 * (float) this.calculateRowCount()) + 0.5), 0);
        }

        protected float getScrollForRowIndex(int int0) {
            return Mth.clamp((float) int0 / (float) this.calculateRowCount(), 0.0F, 1.0F);
        }

        protected float subtractInputFromScroll(float float0, double double1) {
            return Mth.clamp(float0 - (float) (double1 / (double) this.calculateRowCount()), 0.0F, 1.0F);
        }

        public void scrollTo(float float0) {
            int $$1 = this.getRowIndexForScroll(float0);
            for (int $$2 = 0; $$2 < 5; $$2++) {
                for (int $$3 = 0; $$3 < 9; $$3++) {
                    int $$4 = $$3 + ($$2 + $$1) * 9;
                    if ($$4 >= 0 && $$4 < this.items.size()) {
                        CreativeModeInventoryScreen.CONTAINER.setItem($$3 + $$2 * 9, this.items.get($$4));
                    } else {
                        CreativeModeInventoryScreen.CONTAINER.setItem($$3 + $$2 * 9, ItemStack.EMPTY);
                    }
                }
            }
        }

        public boolean canScroll() {
            return this.items.size() > 45;
        }

        @Override
        public ItemStack quickMoveStack(Player player0, int int1) {
            if (int1 >= this.f_38839_.size() - 9 && int1 < this.f_38839_.size()) {
                Slot $$2 = (Slot) this.f_38839_.get(int1);
                if ($$2 != null && $$2.hasItem()) {
                    $$2.setByPlayer(ItemStack.EMPTY);
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canTakeItemForPickAll(ItemStack itemStack0, Slot slot1) {
            return slot1.container != CreativeModeInventoryScreen.CONTAINER;
        }

        @Override
        public boolean canDragTo(Slot slot0) {
            return slot0.container != CreativeModeInventoryScreen.CONTAINER;
        }

        @Override
        public ItemStack getCarried() {
            return this.inventoryMenu.getCarried();
        }

        @Override
        public void setCarried(ItemStack itemStack0) {
            this.inventoryMenu.setCarried(itemStack0);
        }
    }

    static class SlotWrapper extends Slot {

        final Slot target;

        public SlotWrapper(Slot slot0, int int1, int int2, int int3) {
            super(slot0.container, int1, int2, int3);
            this.target = slot0;
        }

        @Override
        public void onTake(Player player0, ItemStack itemStack1) {
            this.target.onTake(player0, itemStack1);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack0) {
            return this.target.mayPlace(itemStack0);
        }

        @Override
        public ItemStack getItem() {
            return this.target.getItem();
        }

        @Override
        public boolean hasItem() {
            return this.target.hasItem();
        }

        @Override
        public void setByPlayer(ItemStack itemStack0) {
            this.target.setByPlayer(itemStack0);
        }

        @Override
        public void set(ItemStack itemStack0) {
            this.target.set(itemStack0);
        }

        @Override
        public void setChanged() {
            this.target.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return this.target.getMaxStackSize();
        }

        @Override
        public int getMaxStackSize(ItemStack itemStack0) {
            return this.target.getMaxStackSize(itemStack0);
        }

        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return this.target.getNoItemIcon();
        }

        @Override
        public ItemStack remove(int int0) {
            return this.target.remove(int0);
        }

        @Override
        public boolean isActive() {
            return this.target.isActive();
        }

        @Override
        public boolean mayPickup(Player player0) {
            return this.target.mayPickup(player0);
        }
    }
}