package net.minecraftforge.event;

import net.minecraft.core.RegistryAccess;
import net.minecraftforge.eventbus.api.Event;

public class TagsUpdatedEvent extends Event {

    private final RegistryAccess registryAccess;

    private final TagsUpdatedEvent.UpdateCause updateCause;

    private final boolean integratedServer;

    public TagsUpdatedEvent(RegistryAccess registryAccess, boolean fromClientPacket, boolean isIntegratedServerConnection) {
        this.registryAccess = registryAccess;
        this.updateCause = fromClientPacket ? TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED : TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD;
        this.integratedServer = isIntegratedServerConnection;
    }

    public RegistryAccess getRegistryAccess() {
        return this.registryAccess;
    }

    public TagsUpdatedEvent.UpdateCause getUpdateCause() {
        return this.updateCause;
    }

    public boolean shouldUpdateStaticData() {
        return this.updateCause == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD || !this.integratedServer;
    }

    public static enum UpdateCause {

        SERVER_DATA_LOAD, CLIENT_PACKET_RECEIVED
    }
}