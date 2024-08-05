package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.google.common.collect.Multimap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public interface DragonSteelOverrides<T extends TieredItem> {

    @Deprecated
    Multimap<Attribute, AttributeModifier> bakeDragonsteel();

    default float getAttackDamage(T item) {
        if (item instanceof SwordItem) {
            return ((SwordItem) item).getDamage();
        } else {
            return item instanceof DiggerItem ? ((DiggerItem) item).getAttackDamage() : item.getTier().getAttackDamageBonus();
        }
    }

    default boolean isDragonsteel(Tier tier) {
        return tier.getTag() == DragonSteelTier.DRAGONSTEEL_TIER_TAG;
    }

    default boolean isDragonsteelFire(Tier tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_FIRE;
    }

    default boolean isDragonsteelIce(Tier tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_ICE;
    }

    default boolean isDragonsteelLightning(Tier tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING;
    }

    default void hurtEnemy(T item, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (item.getTier() == IafItemRegistry.SILVER_TOOL_MATERIAL && target.getMobType() == MobType.UNDEAD) {
            target.hurt(attacker.m_9236_().damageSources().magic(), this.getAttackDamage(item) + 3.0F);
        }
        if (item.getTier() == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            if (target.getMobType() != MobType.ARTHROPOD) {
                target.hurt(attacker.m_9236_().damageSources().generic(), this.getAttackDamage(item) + 5.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.hurt(attacker.m_9236_().damageSources().generic(), this.getAttackDamage(item) + 5.0F);
            }
        }
        if (this.isDragonsteelFire(item.getTier()) && IafConfig.dragonWeaponFireAbility) {
            target.m_20254_(15);
            target.knockback(1.0, attacker.m_20185_() - target.m_20185_(), attacker.m_20189_() - target.m_20189_());
        }
        if (this.isDragonsteelIce(item.getTier()) && IafConfig.dragonWeaponIceAbility) {
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, 300));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2));
            target.knockback(1.0, attacker.m_20185_() - target.m_20185_(), attacker.m_20189_() - target.m_20189_());
        }
        if (this.isDragonsteelLightning(item.getTier()) && IafConfig.dragonWeaponLightningAbility) {
            boolean flag = true;
            if (attacker instanceof Player && (double) attacker.attackAnim > 0.2) {
                flag = false;
            }
            if (!attacker.m_9236_().isClientSide && flag) {
                LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(target.m_9236_());
                lightningboltentity.m_19880_().add(ServerEvents.BOLT_DONT_DESTROY_LOOT);
                lightningboltentity.m_19880_().add(attacker.m_20149_());
                lightningboltentity.m_20219_(target.m_20182_());
                if (!target.m_9236_().isClientSide) {
                    target.m_9236_().m_7967_(lightningboltentity);
                }
            }
            target.knockback(1.0, attacker.m_20185_() - target.m_20185_(), attacker.m_20189_() - target.m_20189_());
        }
    }

    default void appendHoverText(Tier tier, ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (tier == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            tooltip.add(Component.translatable("silvertools.hurt").withStyle(ChatFormatting.GREEN));
        }
        if (tier == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            tooltip.add(Component.translatable("myrmextools.hurt").withStyle(ChatFormatting.GREEN));
        }
        if (this.isDragonsteelFire(tier) && IafConfig.dragonWeaponFireAbility) {
            tooltip.add(Component.translatable("dragon_sword_fire.hurt2").withStyle(ChatFormatting.DARK_RED));
        }
        if (this.isDragonsteelIce(tier) && IafConfig.dragonWeaponIceAbility) {
            tooltip.add(Component.translatable("dragon_sword_ice.hurt2").withStyle(ChatFormatting.AQUA));
        }
        if (this.isDragonsteelLightning(tier) && IafConfig.dragonWeaponLightningAbility) {
            tooltip.add(Component.translatable("dragon_sword_lightning.hurt2").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}