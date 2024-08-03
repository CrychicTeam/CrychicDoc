package journeymap.client.api.impl;

import java.util.function.Supplier;
import journeymap.client.api.event.RegistryEvent;
import journeymap.client.ui.theme.ThemeLabelSource;

public class InfoSlotFactory implements RegistryEvent.InfoSlotRegistryEvent.InfoSlotRegistrar {

    @Override
    public void register(String modId, String key, long cacheMillis, Supplier<String> supplier) {
        ThemeLabelSource.create(modId, key, cacheMillis, 1L, supplier);
    }
}