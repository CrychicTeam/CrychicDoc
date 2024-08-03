package se.mickelus.tetra.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ModularItemDamageEvent extends Event {

    private LivingEntity usingEntity;

    private ItemStack itemStack;

    private int originalAmount;

    private int amount;

    public ModularItemDamageEvent(LivingEntity usingEntity, ItemStack itemStack, int amount) {
        this.usingEntity = usingEntity;
        this.itemStack = itemStack;
        this.originalAmount = amount;
        this.amount = amount;
    }

    public LivingEntity getUsingEntity() {
        return this.usingEntity;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getOriginalAmount() {
        return this.originalAmount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}