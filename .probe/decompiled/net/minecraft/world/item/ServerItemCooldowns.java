package net.minecraft.world.item;

import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
import net.minecraft.server.level.ServerPlayer;

public class ServerItemCooldowns extends ItemCooldowns {

    private final ServerPlayer player;

    public ServerItemCooldowns(ServerPlayer serverPlayer0) {
        this.player = serverPlayer0;
    }

    @Override
    protected void onCooldownStarted(Item item0, int int1) {
        super.onCooldownStarted(item0, int1);
        this.player.connection.send(new ClientboundCooldownPacket(item0, int1));
    }

    @Override
    protected void onCooldownEnded(Item item0) {
        super.onCooldownEnded(item0);
        this.player.connection.send(new ClientboundCooldownPacket(item0, 0));
    }
}