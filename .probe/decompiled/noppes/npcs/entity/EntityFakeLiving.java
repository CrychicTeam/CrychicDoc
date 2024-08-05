package noppes.npcs.entity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomEntities;

public class EntityFakeLiving extends LivingEntity {

    public EntityFakeLiving(Level par1Level) {
        super(CustomEntities.entityCustomNpc, par1Level);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }
}