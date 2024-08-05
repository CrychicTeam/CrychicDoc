package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SplashPotionItem extends ThrowablePotionItem {

    public SplashPotionItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level0.getRandom().nextFloat() * 0.4F + 0.8F));
        return super.use(level0, player1, interactionHand2);
    }
}