package dev.xkmc.l2weaponry.compat.twilightforest;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.content.item.base.GenericWeaponItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDimensionSettings;

public class IronwoodTool extends ExtraToolConfig implements LWExtraConfig {

    @Override
    public void addEnchants(List<EnchantmentInstance> list, LWToolTypes type, Item tool) {
        if (tool instanceof GenericWeaponItem weapon && !weapon.isHeavy()) {
            list.add(new EnchantmentInstance(Enchantments.BLOCK_EFFICIENCY, 2));
        }
        list.add(new EnchantmentInstance(Enchantments.UNBREAKING, 2));
    }

    private static MobEffectInstance getEffect() {
        return new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, LWConfig.COMMON.ironwoodEffectDuration.get(), 1);
    }

    @Override
    public void onShieldBlock(ItemStack stack, LivingEntity user, LivingEntity entity) {
        user.addEffect(getEffect());
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (stack.isDamaged() && entity.level().dimensionTypeId().equals(TFDimensionSettings.TWILIGHT_DIM_TYPE) && entity.tickCount % LWConfig.COMMON.ironwoodRegenDuration.get() == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.MATS_IRONWOOD.get());
        if (stack.getItem() instanceof BaseShieldItem) {
            list.add(LangData.MATS_EFFECT.get(LangData.getTooltip(getEffect())));
        }
    }
}