package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class AmbientMoodSettings {

    public static final Codec<AmbientMoodSettings> CODEC = RecordCodecBuilder.create(p_47402_ -> p_47402_.group(SoundEvent.CODEC.fieldOf("sound").forGetter(p_151650_ -> p_151650_.soundEvent), Codec.INT.fieldOf("tick_delay").forGetter(p_151648_ -> p_151648_.tickDelay), Codec.INT.fieldOf("block_search_extent").forGetter(p_151646_ -> p_151646_.blockSearchExtent), Codec.DOUBLE.fieldOf("offset").forGetter(p_151644_ -> p_151644_.soundPositionOffset)).apply(p_47402_, AmbientMoodSettings::new));

    public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS = new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0);

    private final Holder<SoundEvent> soundEvent;

    private final int tickDelay;

    private final int blockSearchExtent;

    private final double soundPositionOffset;

    public AmbientMoodSettings(Holder<SoundEvent> holderSoundEvent0, int int1, int int2, double double3) {
        this.soundEvent = holderSoundEvent0;
        this.tickDelay = int1;
        this.blockSearchExtent = int2;
        this.soundPositionOffset = double3;
    }

    public Holder<SoundEvent> getSoundEvent() {
        return this.soundEvent;
    }

    public int getTickDelay() {
        return this.tickDelay;
    }

    public int getBlockSearchExtent() {
        return this.blockSearchExtent;
    }

    public double getSoundPositionOffset() {
        return this.soundPositionOffset;
    }
}