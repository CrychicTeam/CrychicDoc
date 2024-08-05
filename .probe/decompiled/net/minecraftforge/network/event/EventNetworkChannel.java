package net.minecraftforge.network.event;

import java.util.function.Consumer;
import net.minecraft.network.Connection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkInstance;

public class EventNetworkChannel {

    private final NetworkInstance instance;

    public EventNetworkChannel(NetworkInstance instance) {
        this.instance = instance;
    }

    public <T extends NetworkEvent> void addListener(Consumer<T> eventListener) {
        this.instance.addListener(eventListener);
    }

    public void registerObject(Object object) {
        this.instance.registerObject(object);
    }

    public void unregisterObject(Object object) {
        this.instance.unregisterObject(object);
    }

    public boolean isRemotePresent(Connection manager) {
        return this.instance.isRemotePresent(manager);
    }
}