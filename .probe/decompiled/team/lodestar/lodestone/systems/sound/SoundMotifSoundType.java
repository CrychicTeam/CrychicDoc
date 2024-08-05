package team.lodestar.lodestone.systems.sound;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SoundMotifSoundType extends ExtendedSoundType {

    public final Supplier<SoundEvent> motifSound;

    public SoundMotifSoundType(Supplier<SoundEvent> motifSound, float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
        this.motifSound = motifSound;
    }

    public float getMotifPitch() {
        return this.m_56774_();
    }

    public float getMotifVolume() {
        return this.m_56773_();
    }

    @Override
    public void onPlayBreakSound(Level level, BlockPos pos) {
        level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, (SoundEvent) this.motifSound.get(), SoundSource.BLOCKS, (this.getMotifVolume() + 1.0F) / 4.0F, this.getMotifPitch() * 0.6F, false);
    }

    @Override
    public void onPlayStepSound(Level level, BlockPos pos, BlockState state, SoundSource category) {
        level.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (SoundEvent) this.motifSound.get(), category, this.getMotifVolume() * 0.1F, this.getMotifPitch());
    }

    @Override
    public void onPlayPlaceSound(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, (SoundEvent) this.motifSound.get(), SoundSource.BLOCKS, (this.getMotifVolume() + 1.0F) / 4.0F, this.getMotifPitch() * 0.8F);
    }

    @Override
    public void onPlayHitSound(BlockPos pos) {
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance((SoundEvent) this.motifSound.get(), SoundSource.BLOCKS, (this.getMotifVolume() + 1.0F) / 8.0F, this.getMotifPitch() * 0.5F, SoundInstance.createUnseededRandom(), pos));
    }

    @Override
    public void onPlayFallSound(Level level, BlockPos pos, SoundSource category) {
        level.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (SoundEvent) this.motifSound.get(), category, this.getMotifVolume() * 0.5F, this.getMotifPitch() * 0.75F);
    }
}