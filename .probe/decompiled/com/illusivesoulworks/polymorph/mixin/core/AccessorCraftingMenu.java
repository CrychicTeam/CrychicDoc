package com.illusivesoulworks.polymorph.mixin.core;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ CraftingMenu.class })
public interface AccessorCraftingMenu {

    @Accessor
    CraftingContainer getCraftSlots();

    @Accessor
    ResultContainer getResultSlots();

    @Accessor
    Player getPlayer();
}