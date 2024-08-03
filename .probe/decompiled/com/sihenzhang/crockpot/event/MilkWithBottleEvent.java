package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "crockpot")
public class MilkWithBottleEvent {

    @SubscribeEvent
    public static void onCowAndGoatInteract(PlayerInteractEvent.EntityInteract event) {
        Entity targetEntity = event.getTarget();
        if (targetEntity instanceof Cow || targetEntity instanceof Goat) {
            ItemStack stack = event.getItemStack();
            if (stack.is(Items.GLASS_BOTTLE) && !((Animal) targetEntity).m_6162_()) {
                Player player = event.getEntity();
                if (targetEntity instanceof Goat goat) {
                    player.playSound(goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_MILK : SoundEvents.GOAT_MILK, 1.0F, 1.0F);
                } else {
                    player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
                }
                ItemStack filledResult = ItemUtils.createFilledResult(stack, player, CrockPotItems.MILK_BOTTLE.get().getDefaultInstance(), false);
                player.m_21008_(event.getHand(), filledResult);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getSide().isClient()));
                event.setCanceled(true);
            }
        }
    }
}