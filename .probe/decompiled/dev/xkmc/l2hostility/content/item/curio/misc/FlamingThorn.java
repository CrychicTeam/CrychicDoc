package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FlamingThorn extends CurseCurioItem {

    public FlamingThorn(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        LivingEntity target = cache.getAttackTarget();
        int size = target.getActiveEffectsMap().size();
        if (size != 0) {
            int time = LHConfig.COMMON.flameThornTime.get();
            EffectUtil.addEffect(target, new MobEffectInstance((MobEffect) LCEffects.FLAME.get(), time, size - 1), EffectUtil.AddReason.FORCE, user);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_FLAME_THORN.get(Math.round((float) LHConfig.COMMON.flameThornTime.get().intValue() * 0.05F)).withStyle(ChatFormatting.GOLD));
    }
}