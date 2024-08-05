package com.rekindled.embers.util.sound;

public interface ISoundController {

    void playSound(int var1);

    void stopSound(int var1);

    boolean isSoundPlaying(int var1);

    int[] getSoundIDs();

    default void handleSound() {
        for (int id : this.getSoundIDs()) {
            if (this.shouldPlaySound(id) && !this.isSoundPlaying(id)) {
                this.playSound(id);
            }
            if (!this.shouldPlaySound(id) && this.isSoundPlaying(id)) {
                this.stopSound(id);
            }
        }
    }

    default boolean shouldPlaySound(int id) {
        return false;
    }

    default float getCurrentVolume(int id, float volume) {
        return volume;
    }

    default float getCurrentPitch(int id, float pitch) {
        return pitch;
    }
}