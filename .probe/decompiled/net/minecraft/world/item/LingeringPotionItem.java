package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class LingeringPotionItem extends ThrowablePotionItem {

    public LingeringPotionItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        PotionUtils.addPotionTooltip(itemStack0, listComponent2, 0.25F);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level0.getRandom().nextFloat() * 0.4F + 0.8F));
        return super.use(level0, player1, interactionHand2);
    }
}