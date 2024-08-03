package fuzs.puzzleslib.api.network.v3;

public interface ClientboundMessage<T extends Record> extends MessageV3<ClientMessageListener<T>> {
}