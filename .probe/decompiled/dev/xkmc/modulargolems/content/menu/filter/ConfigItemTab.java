package dev.xkmc.modulargolems.content.menu.filter;

import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.menu.registry.ConfigGroup;
import dev.xkmc.modulargolems.content.menu.registry.OpenConfigMenuToServer;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabBase;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabToken;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ConfigItemTab extends GolemTabBase<ConfigGroup, ConfigItemTab> {

    public ConfigItemTab(int index, GolemTabToken<ConfigGroup, ConfigItemTab> token, GolemTabManager<ConfigGroup> manager, ItemStack stack, Component title) {
        super(index, token, manager, stack, title);
    }

    @Override
    public void onTabClicked() {
        GolemConfigEntry entry = this.manager.token.editor.entry();
        ModularGolems.HANDLER.toServer(new OpenConfigMenuToServer(entry.getID(), entry.getColor(), OpenConfigMenuToServer.Type.ITEM));
    }
}