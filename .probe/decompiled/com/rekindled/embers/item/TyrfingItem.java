package com.rekindled.embers.item;

import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.api.item.ITyrfingWeapon;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.TyrfingParticleOptions;
import com.rekindled.embers.util.Misc;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TyrfingItem extends SwordItem implements ITyrfingWeapon {

    public TyrfingItem(Tier tier, int damage, float speed, Item.Properties prop) {
        super(tier, damage, speed, prop);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        tooltip.add(Component.translatable("embers.tooltip.tyrfing").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void attack(LivingHurtEvent event, double armor) {
        if (armor > 0.0) {
            event.getEntity().m_5496_(EmbersSounds.TYRFING_HIT.get(), 1.0F, 1.0F);
            if (event.getEntity().m_9236_() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(TyrfingParticleOptions.TYRFING, event.getEntity().m_20182_().x, event.getEntity().m_20182_().y + (double) (event.getEntity().m_20206_() / 2.0F), event.getEntity().m_20182_().z, 80, 0.1, 0.1, 0.1, 0.0);
            }
            event.setAmount(event.getAmount() / 4.0F * (4.0F + (float) armor * 1.0F));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ColorHandler implements ItemColor {

        @Override
        public int getColor(ItemStack stack, int tintIndex) {
            if (tintIndex == 1) {
                float timerSine = ((float) Math.sin(8.0 * Math.toRadians((double) (EmbersClientEvents.ticks % 360))) + 1.0F) / 2.0F;
                int r = (int) (64.0F * timerSine);
                int g = 16;
                int b = (int) (32.0F + 32.0F * timerSine);
                return Misc.intColor(r, g, b);
            } else {
                return 16777215;
            }
        }
    }
}