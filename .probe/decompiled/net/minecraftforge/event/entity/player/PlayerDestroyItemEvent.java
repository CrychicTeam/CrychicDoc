package net.minecraftforge.event.entity.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDestroyItemEvent extends PlayerEvent {

    @NotNull
    private final ItemStack original;

    @Nullable
    private final InteractionHand hand;

    public PlayerDestroyItemEvent(Player player, @NotNull ItemStack original, @Nullable InteractionHand hand) {
        super(player);
        this.original = original;
        this.hand = hand;
    }

    @NotNull
    public ItemStack getOriginal() {
        return this.original;
    }

    @Nullable
    public InteractionHand getHand() {
        return this.hand;
    }
}