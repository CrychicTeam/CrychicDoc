package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.LinearFuncConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.base.NamedEntry;

public class LinearFuncHandle extends NamedEntry<LinearFuncHandle> {

    public LinearFuncHandle() {
        super(ArtifactTypeRegistry.LINEAR);
    }

    public LinearFuncConfig.Entry getEntry() {
        LinearFuncConfig config = NetworkManager.LINEAR.getMerged();
        return (LinearFuncConfig.Entry) config.map.get(this);
    }

    public double getValue(double interpolate) {
        LinearFuncConfig.Entry entry = this.getEntry();
        return entry.base() + interpolate * entry.slope();
    }
}