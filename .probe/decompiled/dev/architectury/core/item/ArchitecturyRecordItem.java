package dev.architectury.core.item;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;

public class ArchitecturyRecordItem extends RecordItem {

    private final RegistrySupplier<SoundEvent> sound;

    public ArchitecturyRecordItem(int analogOutput, RegistrySupplier<SoundEvent> sound, Item.Properties properties, int lengthInSeconds) {
        super(analogOutput, sound.orElse(null), properties, lengthInSeconds);
        this.sound = sound;
        if (!sound.isPresent()) {
            RecordItem.BY_NAME.remove(null);
            sound.listen(soundEvent -> RecordItem.BY_NAME.put(soundEvent, this));
        }
    }

    @Override
    public SoundEvent getSound() {
        return (SoundEvent) this.sound.get();
    }
}