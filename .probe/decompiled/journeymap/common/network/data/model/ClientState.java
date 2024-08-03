package journeymap.common.network.data.model;

public interface ClientState {

    String getPayload();

    boolean isServerAdmin();

    boolean hasServerMod();
}