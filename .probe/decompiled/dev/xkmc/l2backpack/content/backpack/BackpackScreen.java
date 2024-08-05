package dev.xkmc.l2backpack.content.backpack;

import dev.xkmc.l2backpack.content.common.BaseOpenableScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BackpackScreen extends BaseOpenableScreen<BackpackMenu> {

    public BackpackScreen(BackpackMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }
}