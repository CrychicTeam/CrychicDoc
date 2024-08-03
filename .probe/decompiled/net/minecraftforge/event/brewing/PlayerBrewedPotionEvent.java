package net.minecraftforge.event.brewing;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerBrewedPotionEvent extends PlayerEvent {

    private final ItemStack stack;

    public PlayerBrewedPotionEvent(Player player, @NotNull ItemStack stack) {
        super(player);
        this.stack = stack;
    }

    @NotNull
    public ItemStack getStack() {
        return this.stack;
    }
}