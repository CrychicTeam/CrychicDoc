package dev.xkmc.l2complements.content.item.equipments;

import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraArmorConfig;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class PoseiditeArmor extends ExtraArmorConfig {

    private static final String KEY = "UserInWater";

    private static final String NAME_ARMOR = "neptunium_armor";

    private static final String NAME_TOUGH = "neptunium_toughness";

    private static final String NAME_SPEED = "neptunium_speed";

    private static final String NAME_SWIM = "neptunium_swim";

    private static final AttributeModifier[] ARMOR = makeModifiers("neptunium_armor", 4.0, 6.0, AttributeModifier.Operation.ADDITION);

    private static final AttributeModifier[] TOUGH = makeModifiers("neptunium_toughness", 2.0, 3.0, AttributeModifier.Operation.ADDITION);

    private static final AttributeModifier[] SPEED = makeModifiers("neptunium_speed", 0.1, 0.15, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final AttributeModifier[] SWIM = makeModifiers("neptunium_swim", 0.1, 0.15, AttributeModifier.Operation.MULTIPLY_BASE);

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

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        stack.getOrCreateTag().putBoolean("UserInWater", player.m_20071_());
        if (player.m_20071_()) {
            EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(stack);
            if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST) {
                EffectUtil.refreshEffect(player, new MobEffectInstance(MobEffects.CONDUIT_POWER, 200), EffectUtil.AddReason.SELF, player);
            }
            if (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                EffectUtil.refreshEffect(player, new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 200), EffectUtil.AddReason.SELF, player);
            }
        }
    }

    public Multimap<Attribute, AttributeModifier> modify(Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot, ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean("UserInWater")) {
            map.put(Attributes.ARMOR, ARMOR[slot.getIndex()]);
            map.put(Attributes.ARMOR_TOUGHNESS, TOUGH[slot.getIndex()]);
            map.put(Attributes.MOVEMENT_SPEED, SPEED[slot.getIndex()]);
        }
        map.put(ForgeMod.SWIM_SPEED.get(), SWIM[slot.getIndex()]);
        return map;
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.POSEIDITE_ARMOR.get().withStyle(ChatFormatting.GRAY));
        super.addTooltip(stack, list);
    }
}