package com.rekindled.embers.item;

import com.rekindled.embers.compat.curios.CuriosCompat;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NonbeleiverAmuletItem extends Item implements IEmbersCurioItem {

    public NonbeleiverAmuletItem(Item.Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.WITCH_RESISTANT_TO) && !(event.getAmount() < 0.5F)) {
            CuriosCompat.checkForCurios(event.getEntity(), stack -> {
                if (stack.getItem() == this) {
                    event.setAmount(Math.max(event.getAmount() * 0.1F, 0.5F));
                    return true;
                } else {
                    return false;
                }
            });
        }
    }
}