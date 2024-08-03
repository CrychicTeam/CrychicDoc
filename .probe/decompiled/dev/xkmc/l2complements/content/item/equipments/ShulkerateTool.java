package dev.xkmc.l2complements.content.item.equipments;

import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

public class ShulkerateTool extends ExtraToolConfig {

    private static final String NAME_R = "shulkerate_reach";

    private static final String NAME_D = "shulkerate_dist";

    public Multimap<Attribute, AttributeModifier> modify(Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot, ItemStack stack) {
        map.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(MathHelper.getUUIDFromString("shulkerate_reach"), "shulkerate_reach", 1.0, AttributeModifier.Operation.ADDITION));
        map.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(MathHelper.getUUIDFromString("shulkerate_dist"), "shulkerate_dist", 1.0, AttributeModifier.Operation.ADDITION));
        return map;
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity) {
        return super.damageItem(stack, 1, entity);
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.SHULKERATE_TOOL.get().withStyle(ChatFormatting.GRAY));
    }

    public boolean hideWithEffect() {
        return true;
    }
}