package net.minecraft.world;

import javax.annotation.Nullable;

public interface Clearable {

    void clearContent();

    static void tryClear(@Nullable Object object0) {
        if (object0 instanceof Clearable) {
            ((Clearable) object0).clearContent();
        }
    }
}