package net.minecraft.world.level.storage;

import net.minecraft.network.chat.Component;

public class LevelStorageException extends RuntimeException {

    private final Component messageComponent;

    public LevelStorageException(Component component0) {
        super(component0.getString());
        this.messageComponent = component0;
    }

    public Component getMessageComponent() {
        return this.messageComponent;
    }
}