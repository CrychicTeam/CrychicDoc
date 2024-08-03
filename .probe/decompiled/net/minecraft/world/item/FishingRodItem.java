package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class FishingRodItem extends Item implements Vanishable {

    public FishingRodItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if (player1.fishing != null) {
            if (!level0.isClientSide) {
                int $$4 = player1.fishing.retrieve($$3);
                $$3.hurtAndBreak($$4, player1, p_41288_ -> p_41288_.m_21190_(interactionHand2));
            }
            level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level0.getRandom().nextFloat() * 0.4F + 0.8F));
            player1.m_146850_(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level0.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level0.isClientSide) {
                int $$5 = EnchantmentHelper.getFishingSpeedBonus($$3);
                int $$6 = EnchantmentHelper.getFishingLuckBonus($$3);
                level0.m_7967_(new FishingHook(player1, level0, $$6, $$5));
            }
            player1.awardStat(Stats.ITEM_USED.get(this));
            player1.m_146850_(GameEvent.ITEM_INTERACT_START);
        }
        return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}