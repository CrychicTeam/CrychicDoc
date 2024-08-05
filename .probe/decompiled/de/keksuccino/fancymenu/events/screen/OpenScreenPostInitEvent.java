package de.keksuccino.fancymenu.events.screen;

import de.keksuccino.fancymenu.util.event.acara.EventBase;
import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class OpenScreenPostInitEvent extends EventBase {

    private final Screen screen;

    public OpenScreenPostInitEvent(@NotNull Screen screen) {
        this.screen = (Screen) Objects.requireNonNull(screen);
    }

    @NotNull
    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}