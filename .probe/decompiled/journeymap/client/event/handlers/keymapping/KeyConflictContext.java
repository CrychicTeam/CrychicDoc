package journeymap.client.event.handlers.keymapping;

import net.minecraftforge.client.settings.IKeyConflictContext;

public enum KeyConflictContext {

    UNIVERSAL, GUI, IN_GAME;

    public boolean isActive() {
        return this.getForge().isActive();
    }

    public IKeyConflictContext getForge() {
        return switch(this) {
            case UNIVERSAL ->
                net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL;
            case GUI ->
                net.minecraftforge.client.settings.KeyConflictContext.GUI;
            case IN_GAME ->
                net.minecraftforge.client.settings.KeyConflictContext.IN_GAME;
        };
    }
}