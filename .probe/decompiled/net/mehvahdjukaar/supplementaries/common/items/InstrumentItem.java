package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.supplementaries.common.misc.songs.SongsManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public abstract class InstrumentItem extends Item {

    protected InstrumentItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        player.m_6672_(hand);
        return InteractionResultHolder.consume(player.m_21120_(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide) {
            SongsManager.playRandomSong(stack, this, entity, (long) (this.getUseDuration(stack) - remainingUseDuration));
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity entity, int pTimeCharged) {
        SongsManager.clearCurrentlyPlaying(entity.m_20148_());
    }

    public float getPitch(int note) {
        return (float) Math.pow(2.0, (double) (note - 1 - 12) / 12.0);
    }

    public float getVolume() {
        return 1.0F;
    }

    public SoundEvent getSound() {
        return SoundEvents.NOTE_BLOCK_FLUTE.value();
    }

    public void spawnNoteParticle(Level level, LivingEntity entity, int note) {
    }
}