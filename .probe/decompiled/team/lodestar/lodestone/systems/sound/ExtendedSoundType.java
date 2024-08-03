package team.lodestar.lodestone.systems.sound;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ForgeSoundType;

public class ExtendedSoundType extends ForgeSoundType {

    public ExtendedSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
    }

    public void onPlayBreakSound(Level level, BlockPos pos) {
    }

    public void onPlayStepSound(Level level, BlockPos pos, BlockState state, SoundSource category) {
    }

    public void onPlayPlaceSound(Level level, BlockPos pos, Player player) {
    }

    @OnlyIn(Dist.CLIENT)
    public void onPlayHitSound(BlockPos pos) {
    }

    public void onPlayFallSound(Level level, BlockPos pos, SoundSource category) {
    }
}