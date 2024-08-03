package journeymap.client.api.event.forge;

import javax.annotation.Nullable;
import journeymap.client.api.model.WrappedEntity;
import journeymap.client.api.util.UIState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class EntityRadarUpdateEvent extends Event {

    private final UIState activeUiState;

    private final EntityRadarUpdateEvent.EntityType entityType;

    private final WrappedEntity wrappedEntity;

    public EntityRadarUpdateEvent(UIState activeUiState, EntityRadarUpdateEvent.EntityType entityType, WrappedEntity wrappedEntity) {
        this.activeUiState = activeUiState;
        this.entityType = entityType;
        this.wrappedEntity = wrappedEntity;
    }

    @Nullable
    public UIState getActiveUiState() {
        return this.activeUiState;
    }

    public EntityRadarUpdateEvent.EntityType getType() {
        return this.entityType;
    }

    public WrappedEntity getWrappedEntity() {
        return this.wrappedEntity;
    }

    public static enum EntityType {

        MOB, PLAYER
    }
}