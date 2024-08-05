package journeymap.client.api.event;

import com.google.common.base.MoreObjects;
import journeymap.client.api.util.UIState;

public class DisplayUpdateEvent extends ClientEvent {

    public final UIState uiState;

    public DisplayUpdateEvent(UIState uiState) {
        super(ClientEvent.Type.DISPLAY_UPDATE, uiState.dimension);
        this.uiState = uiState;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("uiState", this.uiState).toString();
    }
}