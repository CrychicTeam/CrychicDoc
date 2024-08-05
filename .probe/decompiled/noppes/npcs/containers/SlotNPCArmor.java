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

class SlotNPCArmor extends Slot {

    final EquipmentSlot armorType;

    SlotNPCArmor(Container iinventory, int i, int j, int k, EquipmentSlot l) {
        super(iinventory, i, j, k);
        this.armorType = l;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, SlotCompanionArmor.ARMOR_SLOT_TEXTURES[this.armorType.getIndex()]);
    }

    @Override
    public boolean mayPlace(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ArmorItem) {
            return ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == this.armorType;
        } else {
            return itemstack.getItem() instanceof BlockItem ? this.armorType == EquipmentSlot.HEAD : false;
        }
    }
}