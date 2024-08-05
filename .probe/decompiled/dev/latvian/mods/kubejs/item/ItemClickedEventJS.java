package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.entity.RayTraceResultJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Info("Invoked when a player right clicks with an item **without targeting anything**.\n\nNot to be confused with `BlockEvents.rightClick` or `ItemEvents.entityInteracted`.\n")
public class ItemClickedEventJS extends PlayerEventJS {

    private final Player player;

    private final InteractionHand hand;

    private final ItemStack item;

    private RayTraceResultJS target;

    public ItemClickedEventJS(Player player, InteractionHand hand, ItemStack item) {
        this.player = player;
        this.hand = hand;
        this.item = item;
    }

    @Info("The player that clicked with the item.")
    @Override
    public Player getEntity() {
        return this.player;
    }

    @Info("The hand that the item was clicked with.")
    public InteractionHand getHand() {
        return this.hand;
    }

    @Info("The item that was clicked with.")
    public ItemStack getItem() {
        return this.item;
    }

    @Info("The ray trace result of the click.")
    public RayTraceResultJS getTarget() {
        if (this.target == null) {
            this.target = this.player.kjs$rayTrace();
        }
        return this.target;
    }

    @Nullable
    @Override
    protected Object defaultExitValue() {
        return this.item;
    }

    @Nullable
    @Override
    protected Object mapExitValue(@Nullable Object value) {
        return ItemStackJS.of(value);
    }
}