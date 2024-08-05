package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ImbuableChestplateArmorItem extends ExtendedArmorItem implements IPresetSpellContainer {

    public ImbuableChestplateArmorItem(IronsExtendedArmorMaterial material, ArmorItem.Type type, Item.Properties settings) {
        super(material, type, settings);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.CHESTPLATE && !ISpellContainer.isSpellContainer(itemStack)) {
                ISpellContainer spellContainer = ISpellContainer.create(1, true, true);
                spellContainer.save(itemStack);
            }
        }
    }
}