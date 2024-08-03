package com.illusivesoulworks.polymorph.api.common.capability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IPlayerRecipeData extends IRecipeData<Player> {

    void setContainerMenu(AbstractContainerMenu var1);

    AbstractContainerMenu getContainerMenu();
}