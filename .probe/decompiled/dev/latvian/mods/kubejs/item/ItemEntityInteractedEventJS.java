package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Info("Invoked when a player right clicks on an entity.\n")
public class ItemEntityInteractedEventJS extends PlayerEventJS {

    private final Player player;

    private final Entity entity;

    private final InteractionHand hand;

    public ItemEntityInteractedEventJS(Player player, Entity entity, InteractionHand hand) {
        this.player = player;
        this.entity = entity;
        this.hand = hand;
    }

    @Info("The player that interacted with the entity.")
    @Override
    public Player getEntity() {
        return this.player;
    }

    @Info("The hand that was used to interact with the entity.")
    public InteractionHand getHand() {
        return this.hand;
    }

    @Info("The item that was used to interact with the entity.")
    public ItemStack getItem() {
        return this.player.m_21120_(this.hand);
    }

    @Info("The entity that was interacted with.")
    public Entity getTarget() {
        return this.entity;
    }
}