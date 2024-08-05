package noppes.npcs.containers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.roles.RoleCompanion;

public class SlotCompanionArmor extends Slot {

    public static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[] { InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET };

    final EquipmentSlot armorType;

    final RoleCompanion role;

    public SlotCompanionArmor(RoleCompanion role, Container iinventory, int id, int x, int y, EquipmentSlot type) {
        super(iinventory, id, x, y);
        this.armorType = type;
        this.role = role;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[this.armorType.getIndex()]);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ArmorItem && this.role.canWearArmor(itemstack)) {
            return ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == this.armorType;
        } else {
            return itemstack.getItem() instanceof BlockItem ? this.armorType == EquipmentSlot.HEAD : false;
        }
    }
}