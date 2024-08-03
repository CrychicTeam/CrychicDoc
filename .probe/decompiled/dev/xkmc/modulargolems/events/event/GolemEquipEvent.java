package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class GolemEquipEvent extends HumanoidGolemEvent {

    private final ItemStack stack;

    private EquipmentSlot slot;

    private boolean canEquip;

    private int amount;

    public GolemEquipEvent(HumanoidGolemEntity golem, ItemStack stack) {
        super(golem);
        this.stack = stack;
        this.slot = LivingEntity.getEquipmentSlotForItem(stack);
        this.canEquip = stack.canEquip(this.slot, golem);
        this.amount = 1;
    }

    public void setSlot(EquipmentSlot slot, int amount) {
        this.slot = slot;
        this.canEquip = true;
        this.amount = amount;
    }

    public boolean canEquip() {
        return this.canEquip;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int getAmount() {
        return this.amount;
    }
}