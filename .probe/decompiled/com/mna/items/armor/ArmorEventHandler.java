package com.mna.items.armor;

import com.mna.advancements.CustomAdvancementTriggers;
import java.util.ArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.FORGE)
public class ArmorEventHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        EquipmentSlot newSlot = event.getSlot();
        ItemStack newStack = event.getTo();
        ItemStack oldStack = event.getFrom();
        if (!(newStack.getItem() instanceof ISetItem) || !(oldStack.getItem() instanceof ISetItem) || !((ISetItem) newStack.getItem()).getSetIdentifier().equals(((ISetItem) oldStack.getItem()).getSetIdentifier())) {
            if (oldStack.getItem() instanceof ISetItem) {
                int oldSetCount = 1;
                ISetItem oldGear = (ISetItem) oldStack.getItem();
                ArrayList<EquipmentSlot> equippedSlots = new ArrayList();
                equippedSlots.add(newSlot);
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot != newSlot) {
                        ItemStack gear = event.getEntity().getItemBySlot(slot);
                        if (gear.getItem() instanceof ISetItem && ((ISetItem) gear.getItem()).getSetIdentifier().equals(oldGear.getSetIdentifier())) {
                            equippedSlots.add(slot);
                            oldSetCount++;
                        }
                    }
                }
                boolean wasSetApplied = oldGear.itemsForSetBonus() <= oldSetCount;
                boolean isSetApplied = oldGear.itemsForSetBonus() <= oldSetCount - 1;
                if (wasSetApplied && !isSetApplied) {
                    oldGear.removeSetBonus(event.getEntity(), (EquipmentSlot[]) equippedSlots.toArray(new EquipmentSlot[0]));
                }
            }
            if (newStack.getItem() instanceof ISetItem) {
                int newSetCount = 1;
                ISetItem newGear = (ISetItem) newStack.getItem();
                ArrayList<EquipmentSlot> equippedSlots = new ArrayList();
                equippedSlots.add(newSlot);
                for (EquipmentSlot slotx : EquipmentSlot.values()) {
                    if (slotx != newSlot) {
                        ItemStack gear = event.getEntity().getItemBySlot(slotx);
                        if (gear.getItem() instanceof ISetItem && ((ISetItem) gear.getItem()).getSetIdentifier().equals(newGear.getSetIdentifier())) {
                            equippedSlots.add(slotx);
                            newSetCount++;
                        }
                    }
                }
                boolean wasSetApplied = newGear.itemsForSetBonus() <= newSetCount - 1;
                boolean isSetApplied = newGear.itemsForSetBonus() <= newSetCount;
                if (!wasSetApplied && isSetApplied) {
                    newGear.applySetBonus(event.getEntity(), (EquipmentSlot[]) equippedSlots.toArray(new EquipmentSlot[0]));
                    if (event.getEntity() instanceof ServerPlayer) {
                        CustomAdvancementTriggers.SET_BONUS.trigger((ServerPlayer) event.getEntity(), newGear.getSetIdentifier());
                    }
                }
            }
        }
    }
}