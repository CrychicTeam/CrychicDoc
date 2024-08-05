package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class GolemDamageShieldEvent extends GolemItemUseEvent {

    private final double damage;

    private int cost;

    public GolemDamageShieldEvent(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand, double damage, int cost) {
        super(golem, stack, hand);
        this.damage = damage;
        this.cost = cost;
    }

    public double getDamage() {
        return this.damage;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}