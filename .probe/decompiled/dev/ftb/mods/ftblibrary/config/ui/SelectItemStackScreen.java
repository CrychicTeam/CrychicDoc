package dev.ftb.mods.ftblibrary.config.ui;

import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.FTBLibraryClientConfig;
import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.util.ModUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class SelectItemStackScreen extends ResourceSelectorScreen<ItemStack> {

    public static final SearchModeIndex<ResourceSearchMode<ItemStack>> KNOWN_MODES = new SearchModeIndex<>();

    public SelectItemStackScreen(ItemStackConfig config, ConfigCallback callback) {
        super(config, callback);
    }

    @Override
    protected SearchModeIndex<ResourceSearchMode<ItemStack>> getSearchModeIndex() {
        return KNOWN_MODES;
    }

    @Override
    protected ResourceSelectorScreen<ItemStack>.ResourceButton makeResourceButton(Panel panel, SelectableResource<ItemStack> resource) {
        return new SelectItemStackScreen.ItemStackButton(panel, (SelectableResource<ItemStack>) Objects.requireNonNullElse(resource, SelectableResource.item(ItemStack.EMPTY)));
    }

    static {
        KNOWN_MODES.appendMode(ResourceSearchMode.ALL_ITEMS);
        KNOWN_MODES.appendMode(ResourceSearchMode.INVENTORY);
    }

    private class ItemStackButton extends ResourceSelectorScreen<ItemStack>.ResourceButton {

        private ItemStackButton(Panel panel, SelectableResource<ItemStack> resource) {
            super(panel, resource);
        }

        @Override
        public boolean shouldAdd(String search) {
            search = search.toLowerCase();
            if (search.isEmpty()) {
                return true;
            } else if (search.startsWith("@")) {
                return RegistrarManager.getId(((ItemStack) this.getStack()).getItem(), Registries.ITEM).getNamespace().contains(search.substring(1));
            } else {
                return search.startsWith("#") && ResourceLocation.isValidResourceLocation(search.substring(1)) ? ((ItemStack) this.getStack()).is(TagKey.create(Registries.ITEM, new ResourceLocation(search.substring(1)))) : ((ItemStack) this.getStack()).getHoverName().getString().toLowerCase().contains(search);
            }
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (!((ItemStack) this.getStack()).isEmpty()) {
                TooltipFlag flag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL;
                ((ItemStack) this.getStack()).getTooltipLines(Minecraft.getInstance().player, flag).forEach(list::add);
                if (FTBLibraryClientConfig.ITEM_MODNAME.get()) {
                    ModUtils.getModName(((ItemStack) this.getStack()).getItem()).ifPresent(name -> list.add(Component.literal(name).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC)));
                }
            }
        }
    }
}