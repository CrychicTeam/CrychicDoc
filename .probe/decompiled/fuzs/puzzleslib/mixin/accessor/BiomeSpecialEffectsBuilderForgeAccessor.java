package fuzs.puzzleslib.mixin.accessor;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ BiomeSpecialEffects.Builder.class })
public interface BiomeSpecialEffectsBuilderForgeAccessor {

    @Accessor("foliageColorOverride")
    void puzzleslib$setFoliageColorOverride(Optional<Integer> var1);

    @Accessor("grassColorOverride")
    void puzzleslib$setGrassColorOverride(Optional<Integer> var1);

    @Accessor("ambientParticle")
    void puzzleslib$setAmbientParticle(Optional<AmbientParticleSettings> var1);

    @Accessor("ambientLoopSoundEvent")
    void puzzleslib$setAmbientLoopSoundEvent(Optional<Holder<SoundEvent>> var1);

    @Accessor("ambientMoodSettings")
    void puzzleslib$setAmbientMoodSettings(Optional<AmbientMoodSettings> var1);

    @Accessor("ambientAdditionsSettings")
    void puzzleslib$setAmbientAdditionsSettings(Optional<AmbientAdditionsSettings> var1);

    @Accessor("backgroundMusic")
    void puzzleslib$setBackgroundMusic(Optional<Music> var1);
}