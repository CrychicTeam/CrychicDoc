package dev.xkmc.l2weaponry.compat.dragons;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class IceDragonBoneTool extends ExtraToolConfig {

    public void onHit(ItemStack stack, LivingEntity target, LivingEntity user) {
        super.onHit(stack, target, user);
        if (IafConfig.dragonWeaponIceAbility) {
            if (target instanceof EntityFireDragon) {
                target.hurt(user.m_9236_().damageSources().drown(), 13.5F);
            }
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, 200));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
            target.knockback(1.0, user.m_20185_() - target.m_20185_(), user.m_20189_() - target.m_20189_());
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(Component.translatable("dragon_sword_ice.hurt1").withStyle(ChatFormatting.GREEN));
        if (IafConfig.dragonWeaponIceAbility) {
            list.add(Component.translatable("dragon_sword_ice.hurt2").withStyle(ChatFormatting.AQUA));
        }
    }
}