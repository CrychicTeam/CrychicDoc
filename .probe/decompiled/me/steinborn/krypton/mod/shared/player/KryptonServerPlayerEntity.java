package me.steinborn.krypton.mod.shared.player;

public interface KryptonServerPlayerEntity {

    void setNeedsChunksReloaded(boolean var1);

    int getPlayerViewDistance();

    boolean getNeedsChunksReloaded();
}