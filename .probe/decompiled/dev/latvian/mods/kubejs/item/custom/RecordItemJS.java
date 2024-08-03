package dev.latvian.mods.kubejs.item.custom;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;

public class RecordItemJS extends RecordItem {

    private final RecordItemJS.Builder builder;

    public RecordItemJS(RecordItemJS.Builder b, int analogOutput, SoundEvent song, Item.Properties properties) {
        super(analogOutput, song, properties, b.length);
        this.builder = b;
    }

    @Override
    public SoundEvent getSound() {
        return this.builder.getSoundEvent();
    }

    @Override
    public int getLengthInTicks() {
        return this.builder.length * 20;
    }

    public static class Builder extends ItemBuilder {

        public transient ResourceLocation song = new ResourceLocation("minecraft:music_disc.11");

        public transient SoundEvent songSoundEvent;

        public transient int length = 71;

        public transient int analogOutput = 1;

        public Builder(ResourceLocation i) {
            super(i);
            this.maxStackSize(1);
            this.rarity(Rarity.RARE);
        }

        @Info(value = "Sets the song that will play when this record is played.\n", params = { @Param(name = "s", value = "The location of sound event."), @Param(name = "seconds", value = "The length of the song in seconds.") })
        public RecordItemJS.Builder song(ResourceLocation s, int seconds) {
            this.song = s;
            this.length = seconds;
            this.songSoundEvent = null;
            return this;
        }

        @Info("Sets the redstone output of the jukebox when this record is played.")
        public RecordItemJS.Builder analogOutput(int o) {
            this.analogOutput = o;
            return this;
        }

        public Item createObject() {
            return new RecordItemJS(this, this.analogOutput, SoundEvents.ITEM_PICKUP, this.createItemProperties());
        }

        public SoundEvent getSoundEvent() {
            if (this.songSoundEvent == null) {
                this.songSoundEvent = RegistryInfo.SOUND_EVENT.getValue(this.song);
                if (this.songSoundEvent == null || this.songSoundEvent == SoundEvents.ITEM_PICKUP) {
                    this.songSoundEvent = SoundEvents.MUSIC_DISC_11;
                }
            }
            return this.songSoundEvent;
        }
    }
}