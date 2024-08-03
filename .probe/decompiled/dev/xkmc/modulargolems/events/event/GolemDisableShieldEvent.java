package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class GolemDisableShieldEvent extends GolemItemUseEvent {

    private final LivingEntity source;

    private boolean shouldDisable;

    public GolemDisableShieldEvent(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand, LivingEntity source, boolean shouldDisable) {
        super(golem, stack, hand);
        this.source = source;
        this.shouldDisable = shouldDisable;
    }

    public LivingEntity getSource() {
        return this.source;
    }

    public void setDisabled(boolean disabled) {
        this.shouldDisable = disabled;
    }

    public boolean shouldDisable() {
        return this.shouldDisable;
    }
}