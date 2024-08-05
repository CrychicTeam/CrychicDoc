package se.mickelus.tetra;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@ParametersAreNonnullByDefault
public class TetraSounds {

    public static final SoundEvent scannerLoop = SoundEvent.createVariableRangeEvent(new ResourceLocation("tetra", "scanner"));

    public static final SoundEvent scanMiss = SoundEvent.createVariableRangeEvent(new ResourceLocation("tetra", "scan_miss"));

    public static final SoundEvent scanHit = SoundEvent.createVariableRangeEvent(new ResourceLocation("tetra", "scan_hit"));

    public static final SoundEvent honeGain = SoundEvent.createVariableRangeEvent(new ResourceLocation("tetra", "hone_gain"));

    public static final SoundEvent settle = SoundEvent.createVariableRangeEvent(new ResourceLocation("tetra", "settle"));
}