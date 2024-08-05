package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.level.Level;

public class ExperienceBottleItem extends Item {

    public ExperienceBottleItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.EXPERIENCE_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level0.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level0.isClientSide) {
            ThrownExperienceBottle $$4 = new ThrownExperienceBottle(level0, player1);
            $$4.m_37446_($$3);
            $$4.m_37251_(player1, player1.m_146909_(), player1.m_146908_(), -20.0F, 0.7F, 1.0F);
            level0.m_7967_($$4);
        }
        player1.awardStat(Stats.ITEM_USED.get(this));
        if (!player1.getAbilities().instabuild) {
            $$3.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
    }
}