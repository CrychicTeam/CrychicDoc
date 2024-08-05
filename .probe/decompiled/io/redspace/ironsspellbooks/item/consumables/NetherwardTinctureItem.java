package io.redspace.ironsspellbooks.item.consumables;

import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NetherwardTinctureItem extends DrinkableItem {

    private static final Component description = Component.translatable("item.irons_spellbooks.netherward_tincture.desc").withStyle(ChatFormatting.GRAY);

    public NetherwardTinctureItem() {
        super(ItemPropertiesHelper.material(16), NetherwardTinctureItem::applyEffect, null, false);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(description);
    }

    private static void applyEffect(ItemStack itemStack, LivingEntity livingEntity) {
        if (livingEntity instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
            piglin.m_216990_(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
        } else if (livingEntity instanceof Hoglin hoglin) {
            hoglin.setImmuneToZombification(true);
            hoglin.m_216990_(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
        }
        livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
        livingEntity.m_216990_(SoundEvents.INK_SAC_USE);
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!(pInteractionTarget instanceof AbstractPiglin) && !(pInteractionTarget instanceof Hoglin)) {
            return super.m_6880_(pStack, pPlayer, pInteractionTarget, pUsedHand);
        } else {
            applyEffect(pStack, pInteractionTarget);
            if (!pPlayer.getAbilities().instabuild) {
                pStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
    }
}