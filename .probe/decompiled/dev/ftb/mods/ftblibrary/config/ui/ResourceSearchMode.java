package dev.ftb.mods.ftblibrary.config.ui;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface ResourceSearchMode<T> {

    ResourceSearchMode<ItemStack> ALL_ITEMS = new ResourceSearchMode<ItemStack>() {

        private List<SelectableResource<ItemStack>> allItemsCache = null;

        @Override
        public Icon getIcon() {
            return Icons.COMPASS;
        }

        @Override
        public MutableComponent getDisplayName() {
            return Component.translatable("ftblibrary.select_item.list_mode.all");
        }

        @Override
        public Collection<? extends SelectableResource<ItemStack>> getAllResources() {
            if (this.allItemsCache == null) {
                CreativeModeTabs.tryRebuildTabContents(FeatureFlags.DEFAULT_FLAGS, false, ClientUtils.registryAccess());
                this.allItemsCache = CreativeModeTabs.searchTab().getDisplayItems().stream().map(SelectableResource::item).toList();
            }
            return this.allItemsCache;
        }
    };

    ResourceSearchMode<ItemStack> INVENTORY = new ResourceSearchMode<ItemStack>() {

        @Override
        public Icon getIcon() {
            return ItemIcon.getItemIcon(Items.CHEST);
        }

        @Override
        public MutableComponent getDisplayName() {
            return Component.translatable("ftblibrary.select_item.list_mode.inv");
        }

        @Override
        public Collection<? extends SelectableResource<ItemStack>> getAllResources() {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return Collections.emptySet();
            } else {
                int invSize = player.getInventory().getContainerSize();
                List<SelectableResource<ItemStack>> items = new ArrayList(invSize);
                for (int i = 0; i < invSize; i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty()) {
                        items.add(SelectableResource.item(stack));
                    }
                }
                return items;
            }
        }
    };

    ResourceSearchMode<FluidStack> ALL_FLUIDS = new ResourceSearchMode<FluidStack>() {

        private List<SelectableResource<FluidStack>> allFluidsCache = null;

        @Override
        public Icon getIcon() {
            return ItemIcon.getItemIcon(Items.COMPASS);
        }

        @Override
        public MutableComponent getDisplayName() {
            return Component.translatable("ftblibrary.select_fluid.list_mode.all");
        }

        @Override
        public Collection<? extends SelectableResource<FluidStack>> getAllResources() {
            if (this.allFluidsCache == null) {
                List<SelectableResource<FluidStack>> fluidstacks = new ArrayList();
                BuiltInRegistries.FLUID.forEach(f -> {
                    if (f.isSource(f.defaultFluidState())) {
                        fluidstacks.add(SelectableResource.fluid(FluidStack.create(f, FluidStackHooks.bucketAmount())));
                    }
                });
                this.allFluidsCache = List.copyOf(fluidstacks);
            }
            return this.allFluidsCache;
        }
    };

    Icon getIcon();

    MutableComponent getDisplayName();

    Collection<? extends SelectableResource<T>> getAllResources();
}