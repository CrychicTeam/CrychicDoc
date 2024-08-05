package journeymap.client.api.event;

import java.util.function.Supplier;

public class RegistryEvent extends ClientEvent {

    private final RegistryEvent.RegistryType registryType;

    private RegistryEvent(RegistryEvent.RegistryType registryType) {
        super(ClientEvent.Type.REGISTRY);
        this.registryType = registryType;
    }

    public RegistryEvent.RegistryType getRegistryType() {
        return this.registryType;
    }

    public static class InfoSlotRegistryEvent extends RegistryEvent {

        private final RegistryEvent.InfoSlotRegistryEvent.InfoSlotRegistrar registrar;

        public InfoSlotRegistryEvent(RegistryEvent.InfoSlotRegistryEvent.InfoSlotRegistrar registrar) {
            super(RegistryEvent.RegistryType.INFO_SLOT);
            this.registrar = registrar;
        }

        public void register(String modId, String key, long updateTime, Supplier<String> supplier) {
            this.registrar.register(modId, key, updateTime, supplier);
        }

        public interface InfoSlotRegistrar {

            void register(String var1, String var2, long var3, Supplier<String> var5);
        }
    }

    public static class OptionsRegistryEvent extends RegistryEvent {

        public OptionsRegistryEvent() {
            super(RegistryEvent.RegistryType.OPTIONS);
        }
    }

    public static enum RegistryType {

        OPTIONS, INFO_SLOT
    }
}