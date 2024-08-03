package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class GolemItemUseEvent extends HumanoidGolemEvent {

    private final ItemStack stack;

    private final InteractionHand hand;

    public GolemItemUseEvent(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand) {
        super(golem);
        this.stack = stack;
        this.hand = hand;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public InteractionHand getHand() {
        return this.hand;
    }
}