package net.mehvahdjukaar.moonlight.api.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ILeftClickReact {

    boolean onLeftClick(ItemStack var1, Player var2, InteractionHand var3);
}