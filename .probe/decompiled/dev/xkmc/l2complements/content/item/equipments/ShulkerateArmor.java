package dev.xkmc.l2complements.content.item.equipments;

import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraArmorConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ShulkerateArmor extends ExtraArmorConfig {

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity) {
        return super.damageItem(stack, 1, entity);
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.SHULKERATE_ARMOR.get().withStyle(ChatFormatting.GRAY));
        super.addTooltip(stack, list);
    }

    public boolean hideWithEffect() {
        return true;
    }
}