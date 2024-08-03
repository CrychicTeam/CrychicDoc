package com.github.alexthe666.iceandfire.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemHippogryphSword extends SwordItem {

    public ItemHippogryphSword() {
        super(IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL, 3, -2.4F, new Item.Properties());
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity targetEntity, LivingEntity attacker) {
        float f = (float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(attacker) * f;
        if (attacker instanceof Player player) {
            for (LivingEntity LivingEntity : attacker.m_9236_().m_45976_(LivingEntity.class, targetEntity.m_20191_().inflate(1.0, 0.25, 1.0))) {
                if (LivingEntity != player && LivingEntity != targetEntity && !attacker.m_7307_(LivingEntity) && attacker.m_20280_(LivingEntity) < 9.0) {
                    LivingEntity.knockback(0.4F, (double) Mth.sin(attacker.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(attacker.m_146908_() * (float) (Math.PI / 180.0))));
                    LivingEntity.hurt(attacker.m_9236_().damageSources().playerAttack(player), f3);
                }
            }
            player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            player.sweepAttack();
        }
        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.hippogryph_sword.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.hippogryph_sword.desc_1").withStyle(ChatFormatting.GRAY));
    }
}