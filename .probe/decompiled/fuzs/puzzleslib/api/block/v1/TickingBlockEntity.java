package fuzs.puzzleslib.api.block.v1;

public interface TickingBlockEntity {

    default void clientTick() {
    }

    default void serverTick() {
    }
}