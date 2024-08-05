package fuzs.puzzleslib.api.capability.v2.data;

public enum PlayerRespawnCopyStrategy {

    ALWAYS {

        @Override
        public void copy(CapabilityComponent oldCapability, CapabilityComponent newCapability, boolean returningFromEnd, boolean keepInventory) {
            PlayerRespawnCopyStrategy.actuallyCopy(oldCapability, newCapability);
        }
    }
    , KEEP_INVENTORY {

        @Override
        public void copy(CapabilityComponent oldCapability, CapabilityComponent newCapability, boolean returningFromEnd, boolean keepInventory) {
            if (returningFromEnd || keepInventory) {
                PlayerRespawnCopyStrategy.actuallyCopy(oldCapability, newCapability);
            }
        }
    }
    , RETURNING_FROM_END {

        @Override
        public void copy(CapabilityComponent oldCapability, CapabilityComponent newCapability, boolean returningFromEnd, boolean keepInventory) {
            if (returningFromEnd) {
                PlayerRespawnCopyStrategy.actuallyCopy(oldCapability, newCapability);
            }
        }
    }
    , NEVER {

        @Override
        public void copy(CapabilityComponent oldCapability, CapabilityComponent newCapability, boolean returningFromEnd, boolean keepInventory) {
        }
    }
    ;

    public abstract void copy(CapabilityComponent var1, CapabilityComponent var2, boolean var3, boolean var4);

    private static void actuallyCopy(CapabilityComponent oldCapability, CapabilityComponent newCapability) {
        newCapability.read(oldCapability.toCompoundTag());
    }
}