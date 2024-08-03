package dev.xkmc.l2complements.content.item.equipments;

import com.google.common.collect.Multimap;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PoseiditeTool extends ExtraToolConfig {

    private static final String KEY = "UserInWater";

    private static final String NAME_ATK = "neptonium_attack";

    private static final String NAME_SPEED = "neptonium_attack_speed";

    private static final AttributeModifier ATK = new AttributeModifier(MathHelper.getUUIDFromString("neptonium_attack"), "neptonium_attack", 0.5, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final AttributeModifier SPEED = new AttributeModifier(MathHelper.getUUIDFromString("neptonium_attack_speed"), "neptonium_attack_speed", 0.2, AttributeModifier.Operation.MULTIPLY_BASE);

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        stack.getOrCreateTag().putBoolean("UserInWater", entity.isInWaterRainOrBubble());
    }

    public float getDestroySpeed(ItemStack stack, BlockState state, float old) {
        return stack.getOrCreateTag().getBoolean("UserInWater") ? old * 1.5F : old;
    }

    public Multimap<Attribute, AttributeModifier> modify(Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot, ItemStack stack) {
        if (stack.getOrCreateTag().getBoolean("UserInWater")) {
            map.put(Attributes.ATTACK_DAMAGE, ATK);
            map.put(Attributes.ATTACK_SPEED, SPEED);
        }
        return map;
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.POSEIDITE_TOOL.get().withStyle(ChatFormatting.GRAY));
    }

    public void onDamage(AttackCache cache, ItemStack stack) {
        if (cache.getAttackTarget().isSensitiveToWater() || cache.getAttackTarget().getMobType() == MobType.WATER) {
            cache.addHurtModifier(DamageModifier.multAttr((float) (1.0 + LCConfig.COMMON.mobTypeBonus.get())));
        }
    }
}