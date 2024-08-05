package dev.xkmc.l2hostility.compat.curios;

import dev.xkmc.l2tabs.compat.BaseCuriosListScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EntityCuriosListScreen extends BaseCuriosListScreen<EntityCuriosListMenu> {

    public EntityCuriosListScreen(EntityCuriosListMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }
}