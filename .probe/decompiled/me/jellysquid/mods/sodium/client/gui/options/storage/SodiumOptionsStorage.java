package me.jellysquid.mods.sodium.client.gui.options.storage;

import java.io.IOException;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;

public class SodiumOptionsStorage implements OptionStorage<SodiumGameOptions> {

    private final SodiumGameOptions options = SodiumClientMod.options();

    public SodiumGameOptions getData() {
        return this.options;
    }

    @Override
    public void save() {
        try {
            this.options.writeChanges();
        } catch (IOException var2) {
            throw new RuntimeException("Couldn't save configuration changes", var2);
        }
        SodiumClientMod.logger().info("Flushed changes to Embeddium configuration");
    }
}