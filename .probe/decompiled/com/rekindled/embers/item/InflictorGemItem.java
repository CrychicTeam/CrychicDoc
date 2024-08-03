package com.rekindled.embers.item;

import com.rekindled.embers.api.item.IInflictorGem;
import com.rekindled.embers.datagen.EmbersSounds;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class InflictorGemItem extends Item implements IInflictorGem {

    public InflictorGemItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide && player.isSecondaryUseActive() && stack.getOrCreateTag().contains("type")) {
            stack.getOrCreateTag().remove("type");
            if (player.m_21223_() > 1.0F) {
                player.m_21153_(Math.max(player.m_21223_() - 10.0F, 1.0F));
            }
        }
        return player.isSecondaryUseActive() ? InteractionResultHolder.consume(stack) : InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);
        if (stack.getOrCreateTag().contains("type")) {
            tooltip.add(Component.translatable("embers.tooltip.inflictor", stack.getOrCreateTag().getString("type")).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("embers.tooltip.inflictor.none").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void attuneSource(ItemStack stack, LivingEntity entity, DamageSource source) {
        String damageType = source.type().msgId();
        if (damageType.compareTo("mob") != 0 && damageType.compareTo("generic") != 0 && damageType.compareTo("player") != 0 && damageType.compareTo("arrow") != 0) {
            stack.getOrCreateTag().putString("type", damageType);
            if (entity != null) {
                entity.m_9236_().playSound(null, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), EmbersSounds.INFLICTOR_GEM.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public String getAttunedSource(ItemStack stack) {
        return !stack.isEmpty() && stack.getOrCreateTag().contains("type") ? stack.getOrCreateTag().getString("type") : null;
    }

    @Override
    public float getDamageResistance(ItemStack stack, float modifier) {
        return 0.35F;
    }
}