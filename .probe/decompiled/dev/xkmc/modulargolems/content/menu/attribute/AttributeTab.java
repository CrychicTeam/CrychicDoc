package dev.xkmc.modulargolems.content.menu.attribute;

import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabBase;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabToken;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AttributeTab extends GolemTabBase<EquipmentGroup, AttributeTab> {

    public AttributeTab(int index, GolemTabToken<EquipmentGroup, AttributeTab> token, GolemTabManager<EquipmentGroup> manager, ItemStack stack, Component title) {
        super(index, token, manager, stack, title);
    }

    @Override
    public void onTabClicked() {
        Minecraft.getInstance().setScreen(new AttributeScreen(this.manager.token.golem, MGLangData.TAB_ATTRIBUTE.get()));
    }
}