package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class PlayLevelSoundEvents {

    public static final EventInvoker<PlayLevelSoundEvents.AtPosition> POSITION = EventInvoker.lookup(PlayLevelSoundEvents.AtPosition.class);

    public static final EventInvoker<PlayLevelSoundEvents.AtEntity> ENTITY = EventInvoker.lookup(PlayLevelSoundEvents.AtEntity.class);

    private PlayLevelSoundEvents() {
    }

    @FunctionalInterface
    public interface AtEntity {

        EventResult onPlaySoundAtEntity(Level var1, Entity var2, MutableValue<Holder<SoundEvent>> var3, MutableValue<SoundSource> var4, DefaultedFloat var5, DefaultedFloat var6);
    }

    @FunctionalInterface
    public interface AtPosition {

        EventResult onPlaySoundAtPosition(Level var1, Vec3 var2, MutableValue<Holder<SoundEvent>> var3, MutableValue<SoundSource> var4, DefaultedFloat var5, DefaultedFloat var6);
    }
}