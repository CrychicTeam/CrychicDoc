package com.sihenzhang.crockpot.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;

public class CrockPotSeedsItem extends ItemNameBlockItem {

    public CrockPotSeedsItem(Block block) {
        super(block, new Item.Properties());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof Chicken chicken) {
            int age = chicken.m_146764_();
            if (!chicken.m_9236_().isClientSide && age == 0 && chicken.m_5957_()) {
                if (!pPlayer.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
                chicken.m_27595_(pPlayer);
                chicken.m_146850_(GameEvent.ENTITY_INTERACT);
                return InteractionResult.SUCCESS;
            }
            if (chicken.m_6162_()) {
                if (!pPlayer.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
                chicken.m_146740_((int) ((float) (-age / 20) * 0.1F), true);
                chicken.m_146850_(GameEvent.ENTITY_INTERACT);
                return InteractionResult.sidedSuccess(chicken.m_9236_().isClientSide);
            }
            if (chicken.m_9236_().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        if (pInteractionTarget instanceof Parrot parrot && !parrot.m_21824_()) {
            RandomSource rand = parrot.m_217043_();
            if (!pPlayer.getAbilities().instabuild) {
                pStack.shrink(1);
            }
            if (!parrot.m_20067_()) {
                parrot.m_9236_().playSound(null, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), SoundEvents.PARROT_EAT, parrot.getSoundSource(), 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
            }
            if (!parrot.m_9236_().isClientSide) {
                if (rand.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(parrot, pPlayer)) {
                    parrot.m_21828_(pPlayer);
                    parrot.m_9236_().broadcastEntityEvent(parrot, (byte) 7);
                } else {
                    parrot.m_9236_().broadcastEntityEvent(parrot, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(parrot.m_9236_().isClientSide);
        }
        return super.m_6880_(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}