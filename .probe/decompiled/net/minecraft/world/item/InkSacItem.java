package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class InkSacItem extends Item implements SignApplicator {

    public InkSacItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean tryApplyToSign(Level level0, SignBlockEntity signBlockEntity1, boolean boolean2, Player player3) {
        if (signBlockEntity1.updateText(p_277425_ -> p_277425_.setHasGlowingText(false), boolean2)) {
            level0.playSound(null, signBlockEntity1.m_58899_(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }
}