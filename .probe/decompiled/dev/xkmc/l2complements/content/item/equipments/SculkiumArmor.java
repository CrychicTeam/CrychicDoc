package dev.xkmc.l2complements.content.item.equipments;

import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraArmorConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class SculkiumArmor extends ExtraArmorConfig {

    private static final String NAME_SPEED = "sculkium_speed";

    private static final String NAME_HASTE = "sculkium_haste";

    private static final String NAME_HEALTH = "sculkium_health";

    private static final AttributeModifier[] HASTE = makeModifiers("sculkium_haste", 0.06, 0.09, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final AttributeModifier[] SPEED = makeModifiers("sculkium_speed", 0.1, 0.15, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final AttributeModifier[] HEALTH = makeModifiers("sculkium_health", 4.0, 6.0, AttributeModifier.Operation.ADDITION);

    private static AttributeModifier[] makeModifiers(String name, double val, double val2, AttributeModifier.Operation op) {
        AttributeModifier[] ans = new AttributeModifier[4];
        for (int i = 0; i < 4; i++) {
            EquipmentSlot slot = EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, i);
            double v = slot != EquipmentSlot.CHEST && slot != EquipmentSlot.LEGS ? val : val2;
            String str = name + "/" + slot.getName().toLowerCase(Locale.ROOT);
            ans[i] = new AttributeModifier(MathHelper.getUUIDFromString(str), str, v, op);
        }
        return ans;
    }

    public Multimap<Attribute, AttributeModifier> modify(Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.HEAD && slot != EquipmentSlot.CHEST) {
            map.put(Attributes.MOVEMENT_SPEED, SPEED[slot.getIndex()]);
        } else {
            map.put(Attributes.ATTACK_SPEED, HASTE[slot.getIndex()]);
        }
        map.put(Attributes.MAX_HEALTH, HEALTH[slot.getIndex()]);
        return map;
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.SCULKIUM_ARMOR.get().withStyle(ChatFormatting.GRAY));
        super.addTooltip(stack, list);
    }

    public boolean dampenVibration() {
        return true;
    }
}