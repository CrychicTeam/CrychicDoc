package com.yungnickyoung.minecraft.yungsapi.api.autoregister;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterEntry;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AutoRegisterCreativeTab extends AutoRegisterEntry<CreativeModeTab> {

    private final Component displayName;

    private final Supplier<ItemStack> iconGenerator;

    private final CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;

    private final boolean canScroll;

    private final boolean showTitle;

    private final boolean alignedRight;

    private final CreativeModeTab.Type type;

    private final String backgroundSuffix;

    private AutoRegisterCreativeTab(AutoRegisterCreativeTab.Builder builder) {
        super(() -> null);
        this.displayName = builder.displayName;
        this.iconGenerator = builder.iconGenerator;
        this.displayItemsGenerator = builder.displayItemsGenerator;
        this.canScroll = builder.canScroll;
        this.showTitle = builder.showTitle;
        this.alignedRight = builder.alignedRight;
        this.type = builder.type;
        this.backgroundSuffix = builder.backgroundSuffix;
    }

    public static AutoRegisterCreativeTab.Builder builder() {
        return new AutoRegisterCreativeTab.Builder();
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public Supplier<ItemStack> getIconItemStackSupplier() {
        return this.iconGenerator;
    }

    public CreativeModeTab.DisplayItemsGenerator getDisplayItemsGenerator() {
        return this.displayItemsGenerator;
    }

    public boolean canScroll() {
        return this.canScroll;
    }

    public boolean showTitle() {
        return this.showTitle;
    }

    public boolean alignedRight() {
        return this.alignedRight;
    }

    public CreativeModeTab.Type getType() {
        return this.type;
    }

    public String getBackgroundSuffix() {
        return this.backgroundSuffix;
    }

    public static class Builder {

        private Component displayName = Component.empty();

        private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;

        private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = (itemDisplayParameters, output) -> {
        };

        private boolean canScroll = true;

        private boolean showTitle = true;

        private boolean alignedRight = false;

        private final CreativeModeTab.Type type = CreativeModeTab.Type.CATEGORY;

        private String backgroundSuffix = "items.png";

        private Builder() {
        }

        public AutoRegisterCreativeTab.Builder iconItem(Supplier<ItemStack> iconItemStack) {
            this.iconGenerator = iconItemStack;
            return this;
        }

        public AutoRegisterCreativeTab.Builder title(Component title) {
            this.displayName = title;
            return this;
        }

        public AutoRegisterCreativeTab.Builder entries(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator) {
            this.displayItemsGenerator = displayItemsGenerator;
            return this;
        }

        public AutoRegisterCreativeTab.Builder alignedRight() {
            this.alignedRight = true;
            return this;
        }

        public AutoRegisterCreativeTab.Builder hideTitle() {
            this.showTitle = false;
            return this;
        }

        public AutoRegisterCreativeTab.Builder noScrollBar() {
            this.canScroll = false;
            return this;
        }

        public AutoRegisterCreativeTab.Builder backgroundSuffix(String string) {
            this.backgroundSuffix = string;
            return this;
        }

        public AutoRegisterCreativeTab build() {
            return new AutoRegisterCreativeTab(this);
        }
    }
}