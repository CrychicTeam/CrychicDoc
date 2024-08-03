package net.minecraftforge.event.entity.player;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import net.minecraft.network.Connection;
import net.minecraftforge.eventbus.api.Event;

public class PlayerNegotiationEvent extends Event {

    private final Connection connection;

    private final GameProfile profile;

    private final List<Future<Void>> futures;

    public PlayerNegotiationEvent(Connection connection, GameProfile profile, List<Future<Void>> futures) {
        this.connection = connection;
        this.profile = profile;
        this.futures = futures;
    }

    public void enqueueWork(Runnable runnable) {
        this.enqueueWork(CompletableFuture.runAsync(runnable));
    }

    public void enqueueWork(Future<Void> future) {
        this.futures.add(future);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public GameProfile getProfile() {
        return this.profile;
    }
}