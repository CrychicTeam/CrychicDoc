package net.minecraft.world.item;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ItemLike;

public class CreativeModeTab {

    private final Component displayName;

    String backgroundSuffix = "items.png";

    boolean canScroll = true;

    boolean showTitle = true;

    boolean alignedRight = false;

    private final CreativeModeTab.Row row;

    private final int column;

    private final CreativeModeTab.Type type;

    @Nullable
    private ItemStack iconItemStack;

    private Collection<ItemStack> displayItems = ItemStackLinkedSet.createTypeAndTagSet();

    private Set<ItemStack> displayItemsSearchTab = ItemStackLinkedSet.createTypeAndTagSet();

    @Nullable
    private Consumer<List<ItemStack>> searchTreeBuilder;

    private final Supplier<ItemStack> iconGenerator;

    private final CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;

    CreativeModeTab(CreativeModeTab.Row creativeModeTabRow0, int int1, CreativeModeTab.Type creativeModeTabType2, Component component3, Supplier<ItemStack> supplierItemStack4, CreativeModeTab.DisplayItemsGenerator creativeModeTabDisplayItemsGenerator5) {
        this.row = creativeModeTabRow0;
        this.column = int1;
        this.displayName = component3;
        this.iconGenerator = supplierItemStack4;
        this.displayItemsGenerator = creativeModeTabDisplayItemsGenerator5;
        this.type = creativeModeTabType2;
    }

    public static CreativeModeTab.Builder builder(CreativeModeTab.Row creativeModeTabRow0, int int1) {
        return new CreativeModeTab.Builder(creativeModeTabRow0, int1);
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public ItemStack getIconItem() {
        if (this.iconItemStack == null) {
            this.iconItemStack = (ItemStack) this.iconGenerator.get();
        }
        return this.iconItemStack;
    }

    public String getBackgroundSuffix() {
        return this.backgroundSuffix;
    }

    public boolean showTitle() {
        return this.showTitle;
    }

    public boolean canScroll() {
        return this.canScroll;
    }

    public int column() {
        return this.column;
    }

    public CreativeModeTab.Row row() {
        return this.row;
    }

    public boolean hasAnyItems() {
        return !this.displayItems.isEmpty();
    }

    public boolean shouldDisplay() {
        return this.type != CreativeModeTab.Type.CATEGORY || this.hasAnyItems();
    }

    public boolean isAlignedRight() {
        return this.alignedRight;
    }

    public CreativeModeTab.Type getType() {
        return this.type;
    }

    public void buildContents(CreativeModeTab.ItemDisplayParameters creativeModeTabItemDisplayParameters0) {
        CreativeModeTab.ItemDisplayBuilder $$1 = new CreativeModeTab.ItemDisplayBuilder(this, creativeModeTabItemDisplayParameters0.enabledFeatures);
        ResourceKey<CreativeModeTab> $$2 = (ResourceKey<CreativeModeTab>) BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(this).orElseThrow(() -> new IllegalStateException("Unregistered creative tab: " + this));
        this.displayItemsGenerator.accept(creativeModeTabItemDisplayParameters0, $$1);
        this.displayItems = $$1.tabContents;
        this.displayItemsSearchTab = $$1.searchTabContents;
        this.rebuildSearchTree();
    }

    public Collection<ItemStack> getDisplayItems() {
        return this.displayItems;
    }

    public Collection<ItemStack> getSearchTabDisplayItems() {
        return this.displayItemsSearchTab;
    }

    public boolean contains(ItemStack itemStack0) {
        return this.displayItemsSearchTab.contains(itemStack0);
    }

    public void setSearchTreeBuilder(Consumer<List<ItemStack>> consumerListItemStack0) {
        this.searchTreeBuilder = consumerListItemStack0;
    }

    public void rebuildSearchTree() {
        if (this.searchTreeBuilder != null) {
            this.searchTreeBuilder.accept(Lists.newArrayList(this.displayItemsSearchTab));
        }
    }

    public static class Builder {

        private static final CreativeModeTab.DisplayItemsGenerator EMPTY_GENERATOR = (p_270422_, p_259433_) -> {
        };

        private final CreativeModeTab.Row row;

        private final int column;

        private Component displayName = Component.empty();

        private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;

        private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = EMPTY_GENERATOR;

        private boolean canScroll = true;

        private boolean showTitle = true;

        private boolean alignedRight = false;

        private CreativeModeTab.Type type = CreativeModeTab.Type.CATEGORY;

        private String backgroundSuffix = "items.png";

        public Builder(CreativeModeTab.Row creativeModeTabRow0, int int1) {
            this.row = creativeModeTabRow0;
            this.column = int1;
        }

        public CreativeModeTab.Builder title(Component component0) {
            this.displayName = component0;
            return this;
        }

        public CreativeModeTab.Builder icon(Supplier<ItemStack> supplierItemStack0) {
            this.iconGenerator = supplierItemStack0;
            return this;
        }

        public CreativeModeTab.Builder displayItems(CreativeModeTab.DisplayItemsGenerator creativeModeTabDisplayItemsGenerator0) {
            this.displayItemsGenerator = creativeModeTabDisplayItemsGenerator0;
            return this;
        }

        public CreativeModeTab.Builder alignedRight() {
            this.alignedRight = true;
            return this;
        }

        public CreativeModeTab.Builder hideTitle() {
            this.showTitle = false;
            return this;
        }

        public CreativeModeTab.Builder noScrollBar() {
            this.canScroll = false;
            return this;
        }

        protected CreativeModeTab.Builder type(CreativeModeTab.Type creativeModeTabType0) {
            this.type = creativeModeTabType0;
            return this;
        }

        public CreativeModeTab.Builder backgroundSuffix(String string0) {
            this.backgroundSuffix = string0;
            return this;
        }

        public CreativeModeTab build() {
            if ((this.type == CreativeModeTab.Type.HOTBAR || this.type == CreativeModeTab.Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR) {
                throw new IllegalStateException("Special tabs can't have display items");
            } else {
                CreativeModeTab $$0 = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
                $$0.alignedRight = this.alignedRight;
                $$0.showTitle = this.showTitle;
                $$0.canScroll = this.canScroll;
                $$0.backgroundSuffix = this.backgroundSuffix;
                return $$0;
            }
        }
    }

    @FunctionalInterface
    public interface DisplayItemsGenerator {

        void accept(CreativeModeTab.ItemDisplayParameters var1, CreativeModeTab.Output var2);
    }

    static class ItemDisplayBuilder implements CreativeModeTab.Output {

        public final Collection<ItemStack> tabContents = ItemStackLinkedSet.createTypeAndTagSet();

        public final Set<ItemStack> searchTabContents = ItemStackLinkedSet.createTypeAndTagSet();

        private final CreativeModeTab tab;

        private final FeatureFlagSet featureFlagSet;

        public ItemDisplayBuilder(CreativeModeTab creativeModeTab0, FeatureFlagSet featureFlagSet1) {
            this.tab = creativeModeTab0;
            this.featureFlagSet = featureFlagSet1;
        }

        @Override
        public void accept(ItemStack itemStack0, CreativeModeTab.TabVisibility creativeModeTabTabVisibility1) {
            if (itemStack0.getCount() != 1) {
                throw new IllegalArgumentException("Stack size must be exactly 1");
            } else {
                boolean $$2 = this.tabContents.contains(itemStack0) && creativeModeTabTabVisibility1 != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
                if ($$2) {
                    throw new IllegalStateException("Accidentally adding the same item stack twice " + itemStack0.getDisplayName().getString() + " to a Creative Mode Tab: " + this.tab.getDisplayName().getString());
                } else {
                    if (itemStack0.getItem().m_245993_(this.featureFlagSet)) {
                        switch(creativeModeTabTabVisibility1) {
                            case PARENT_AND_SEARCH_TABS:
                                this.tabContents.add(itemStack0);
                                this.searchTabContents.add(itemStack0);
                                break;
                            case PARENT_TAB_ONLY:
                                this.tabContents.add(itemStack0);
                                break;
                            case SEARCH_TAB_ONLY:
                                this.searchTabContents.add(itemStack0);
                        }
                    }
                }
            }
        }
    }

    public static record ItemDisplayParameters(FeatureFlagSet f_268709_, boolean f_268429_, HolderLookup.Provider f_268485_) {

        private final FeatureFlagSet enabledFeatures;

        private final boolean hasPermissions;

        private final HolderLookup.Provider holders;

        public ItemDisplayParameters(FeatureFlagSet f_268709_, boolean f_268429_, HolderLookup.Provider f_268485_) {
            this.enabledFeatures = f_268709_;
            this.hasPermissions = f_268429_;
            this.holders = f_268485_;
        }

        public boolean needsUpdate(FeatureFlagSet p_270338_, boolean p_270835_, HolderLookup.Provider p_270575_) {
            return !this.enabledFeatures.equals(p_270338_) || this.hasPermissions != p_270835_ || this.holders != p_270575_;
        }
    }

    public interface Output {

        void accept(ItemStack var1, CreativeModeTab.TabVisibility var2);

        default void accept(ItemStack itemStack0) {
            this.accept(itemStack0, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void accept(ItemLike itemLike0, CreativeModeTab.TabVisibility creativeModeTabTabVisibility1) {
            this.accept(new ItemStack(itemLike0), creativeModeTabTabVisibility1);
        }

        default void accept(ItemLike itemLike0) {
            this.accept(new ItemStack(itemLike0), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptAll(Collection<ItemStack> collectionItemStack0, CreativeModeTab.TabVisibility creativeModeTabTabVisibility1) {
            collectionItemStack0.forEach(p_252337_ -> this.accept(p_252337_, creativeModeTabTabVisibility1));
        }

        default void acceptAll(Collection<ItemStack> collectionItemStack0) {
            this.acceptAll(collectionItemStack0, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    public static enum Row {

        TOP, BOTTOM
    }

    protected static enum TabVisibility {

        PARENT_AND_SEARCH_TABS, PARENT_TAB_ONLY, SEARCH_TAB_ONLY
    }

    public static enum Type {

        CATEGORY, INVENTORY, HOTBAR, SEARCH
    }
}