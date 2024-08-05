package net.minecraft.client.resources.sounds;

import java.util.List;
import javax.annotation.Nullable;

public class SoundEventRegistration {

    private final List<Sound> sounds;

    private final boolean replace;

    @Nullable
    private final String subtitle;

    public SoundEventRegistration(List<Sound> listSound0, boolean boolean1, @Nullable String string2) {
        this.sounds = listSound0;
        this.replace = boolean1;
        this.subtitle = string2;
    }

    public List<Sound> getSounds() {
        return this.sounds;
    }

    public boolean isReplace() {
        return this.replace;
    }

    @Nullable
    public String getSubtitle() {
        return this.subtitle;
    }
}