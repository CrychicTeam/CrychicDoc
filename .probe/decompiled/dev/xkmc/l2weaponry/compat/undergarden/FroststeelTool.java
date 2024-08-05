package dev.xkmc.l2weaponry.compat.undergarden;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import quek.undergarden.registry.UGEffects;

public class FroststeelTool extends ExtraToolConfig implements LWExtraConfig {

    @Override
    public void onHurt(AttackCache cache, LivingEntity attacker, ItemStack stack) {
        cache.getAttackTarget().addEffect(new MobEffectInstance((MobEffect) UGEffects.CHILLY.get(), 600, 2, false, false));
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("tooltip.froststeel_sword").withStyle(ChatFormatting.AQUA));
    }
}