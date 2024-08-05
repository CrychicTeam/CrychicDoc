package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class GolemSweepEvent extends GolemItemUseEvent {

    private final Entity target;

    private final double range;

    private AABB box;

    public GolemSweepEvent(HumanoidGolemEntity golem, ItemStack stack, Entity target, double range) {
        super(golem, stack, InteractionHand.MAIN_HAND);
        this.target = target;
        this.range = range;
        this.box = target.getBoundingBox().inflate(range);
    }

    public Entity getTarget() {
        return this.target;
    }

    public double getRange() {
        return this.range;
    }

    public AABB getBox() {
        return this.box;
    }

    public void setBox(AABB box) {
        this.box = box;
    }
}