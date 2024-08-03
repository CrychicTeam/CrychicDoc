package net.minecraft.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class NameTagItem extends Item {

    public NameTagItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack0, Player player1, LivingEntity livingEntity2, InteractionHand interactionHand3) {
        if (itemStack0.hasCustomHoverName() && !(livingEntity2 instanceof Player)) {
            if (!player1.m_9236_().isClientSide && livingEntity2.isAlive()) {
                livingEntity2.m_6593_(itemStack0.getHoverName());
                if (livingEntity2 instanceof Mob) {
                    ((Mob) livingEntity2).setPersistenceRequired();
                }
                itemStack0.shrink(1);
            }
            return InteractionResult.sidedSuccess(player1.m_9236_().isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
}