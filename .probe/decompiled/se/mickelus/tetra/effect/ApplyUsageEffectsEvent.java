package se.mickelus.tetra.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ApplyUsageEffectsEvent extends Event {

    private LivingEntity usingEntity;

    private ItemStack itemStack;

    private double originalPositiveMultiplier;

    private double positiveMultiplier;

    private double originalNegativeMultiplier;

    private double negativeMultiplier;

    public ApplyUsageEffectsEvent(LivingEntity usingEntity, ItemStack itemStack, double multiplier) {
        this.usingEntity = usingEntity;
        this.itemStack = itemStack;
        this.originalPositiveMultiplier = multiplier;
        this.positiveMultiplier = multiplier;
        this.originalNegativeMultiplier = multiplier;
        this.negativeMultiplier = multiplier;
    }

    public LivingEntity getUsingEntity() {
        return this.usingEntity;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public double getOriginalPositiveMultiplier() {
        return this.originalPositiveMultiplier;
    }

    public double getOriginalNegativeMultiplier() {
        return this.originalNegativeMultiplier;
    }

    public double getPositiveMultiplier() {
        return this.positiveMultiplier;
    }

    public void setPositiveMultiplier(double positiveMultiplier) {
        this.positiveMultiplier = positiveMultiplier;
    }

    public double getNegativeMultiplier() {
        return this.negativeMultiplier;
    }

    public void setNegativeMultiplier(double negativeMultiplier) {
        this.negativeMultiplier = negativeMultiplier;
    }
}