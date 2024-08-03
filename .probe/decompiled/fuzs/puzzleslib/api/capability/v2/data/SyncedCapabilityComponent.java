package fuzs.puzzleslib.api.capability.v2.data;

public interface SyncedCapabilityComponent extends CapabilityComponent {

    boolean isDirty();

    void markDirty();

    void markClean();
}