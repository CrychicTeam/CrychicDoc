package se.mickelus.tetra.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ApplyHitTargetEffectsEvent extends Event {

    private LivingEntity attacker;

    private LivingEntity target;

    private ItemStack usedItemStack;

    public ApplyHitTargetEffectsEvent(LivingEntity attacker, LivingEntity target, ItemStack usedItemStack) {
        this.attacker = attacker;
        this.target = target;
        this.usedItemStack = usedItemStack;
    }

    public LivingEntity getAttacker() {
        return this.attacker;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public ItemStack getUsedItemStack() {
        return this.usedItemStack;
    }
}