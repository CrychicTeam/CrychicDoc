package net.minecraftforge.common.util;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

public class ForgeSoundType extends SoundType {

    private final Supplier<SoundEvent> breakSound;

    private final Supplier<SoundEvent> stepSound;

    private final Supplier<SoundEvent> placeSound;

    private final Supplier<SoundEvent> hitSound;

    private final Supplier<SoundEvent> fallSound;

    public ForgeSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, (SoundEvent) null, (SoundEvent) null, (SoundEvent) null, (SoundEvent) null, (SoundEvent) null);
        this.breakSound = breakSoundIn;
        this.stepSound = stepSoundIn;
        this.placeSound = placeSoundIn;
        this.hitSound = hitSoundIn;
        this.fallSound = fallSoundIn;
    }

    @NotNull
    @Override
    public SoundEvent getBreakSound() {
        return (SoundEvent) this.breakSound.get();
    }

    @NotNull
    @Override
    public SoundEvent getStepSound() {
        return (SoundEvent) this.stepSound.get();
    }

    @NotNull
    @Override
    public SoundEvent getPlaceSound() {
        return (SoundEvent) this.placeSound.get();
    }

    @NotNull
    @Override
    public SoundEvent getHitSound() {
        return (SoundEvent) this.hitSound.get();
    }

    @NotNull
    @Override
    public SoundEvent getFallSound() {
        return (SoundEvent) this.fallSound.get();
    }
}