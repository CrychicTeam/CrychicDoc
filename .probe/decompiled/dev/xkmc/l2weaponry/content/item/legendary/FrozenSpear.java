package dev.xkmc.l2weaponry.content.item.legendary;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2weaponry.content.item.types.SpearItem;
import dev.xkmc.l2weaponry.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FrozenSpear extends SpearItem implements LegendaryWeapon {

    public FrozenSpear(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config) {
        super(tier, damage, speed, prop, config);
    }

    @Override
    public void onHurt(AttackCache event, LivingEntity le, ItemStack stack) {
        int time = LCConfig.COMMON.iceEnchantDuration.get();
        EffectUtil.addEffect(event.getAttackTarget(), new MobEffectInstance((MobEffect) LCEffects.ICE.get(), time), EffectUtil.AddReason.NONE, le);
    }

    @Override
    public boolean isImmuneTo(DamageSource source) {
        return source.is(DamageTypeTags.IS_FREEZING);
    }

    @Override
    public boolean cancelFreeze() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        list.add(LangData.FROZEN_SPEAR.get());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide && selected && entity instanceof LivingEntity le && le.hasEffect((MobEffect) LCEffects.ICE.get())) {
            le.removeEffect((MobEffect) LCEffects.ICE.get());
        }
    }
}