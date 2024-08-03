package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LivingEquipmentChangeEvent extends LivingEvent {

    private final EquipmentSlot slot;

    private final ItemStack from;

    private final ItemStack to;

    public LivingEquipmentChangeEvent(LivingEntity entity, EquipmentSlot slot, @NotNull ItemStack from, @NotNull ItemStack to) {
        super(entity);
        this.slot = slot;
        this.from = from;
        this.to = to;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    @NotNull
    public ItemStack getFrom() {
        return this.from;
    }

    @NotNull
    public ItemStack getTo() {
        return this.to;
    }
}