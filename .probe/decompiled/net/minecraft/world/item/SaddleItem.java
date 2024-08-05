package net.minecraft.world.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

public class SaddleItem extends Item {

    public SaddleItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack0, Player player1, LivingEntity livingEntity2, InteractionHand interactionHand3) {
        if (livingEntity2 instanceof Saddleable && livingEntity2.isAlive()) {
            Saddleable $$4 = (Saddleable) livingEntity2;
            if (!$$4.isSaddled() && $$4.isSaddleable()) {
                if (!player1.m_9236_().isClientSide) {
                    $$4.equipSaddle(SoundSource.NEUTRAL);
                    livingEntity2.m_9236_().m_220400_(livingEntity2, GameEvent.EQUIP, livingEntity2.m_20182_());
                    itemStack0.shrink(1);
                }
                return InteractionResult.sidedSuccess(player1.m_9236_().isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
}