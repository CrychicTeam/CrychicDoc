package journeymap.client.api;

import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.event.ClientEvent;

@ParametersAreNonnullByDefault
public interface IClientPlugin {

    void initialize(IClientAPI var1);

    String getModId();

    void onEvent(ClientEvent var1);
}