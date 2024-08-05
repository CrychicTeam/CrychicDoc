package dev.xkmc.l2weaponry.compat.aerial;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class VoluciteTool extends ExtraToolConfig implements LWExtraConfig {

    @Nullable
    @Override
    public DamageSource getReflectSource(Player player) {
        return player.m_9236_().damageSources().thorns(player);
    }

    @Override
    public double onShieldReflect(ItemStack stack, LivingEntity user, LivingEntity entity, double original, double reflect) {
        int time = reflect > 0.0 ? 200 : 100;
        EffectUtil.addEffect(entity, new MobEffectInstance(MobEffects.LEVITATION, time), EffectUtil.AddReason.NONE, user);
        return reflect + 5.0;
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity le && (le.getMainHandItem() == stack || le.getOffhandItem() == stack) && le.f_19789_ > 3.0F) {
            EffectUtil.refreshEffect(le, new MobEffectInstance(MobEffects.SLOW_FALLING, 40), EffectUtil.AddReason.SELF, le);
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.MATS_AH_VOLUCITE.get());
        if (stack.getItem() instanceof BaseShieldItem) {
            list.add(LangData.MATS_AH_VOLUCITE_SHIELD.get(5));
        }
    }
}