package net.minecraft.network.protocol.game;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.ServerScoreboard;

public class ClientboundSetScorePacket implements Packet<ClientGamePacketListener> {

    private final String owner;

    @Nullable
    private final String objectiveName;

    private final int score;

    private final ServerScoreboard.Method method;

    public ClientboundSetScorePacket(ServerScoreboard.Method serverScoreboardMethod0, @Nullable String string1, String string2, int int3) {
        if (serverScoreboardMethod0 != ServerScoreboard.Method.REMOVE && string1 == null) {
            throw new IllegalArgumentException("Need an objective name");
        } else {
            this.owner = string2;
            this.objectiveName = string1;
            this.score = int3;
            this.method = serverScoreboardMethod0;
        }
    }

    public ClientboundSetScorePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.owner = friendlyByteBuf0.readUtf();
        this.method = friendlyByteBuf0.readEnum(ServerScoreboard.Method.class);
        String $$1 = friendlyByteBuf0.readUtf();
        this.objectiveName = Objects.equals($$1, "") ? null : $$1;
        if (this.method != ServerScoreboard.Method.REMOVE) {
            this.score = friendlyByteBuf0.readVarInt();
        } else {
            this.score = 0;
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUtf(this.owner);
        friendlyByteBuf0.writeEnum(this.method);
        friendlyByteBuf0.writeUtf(this.objectiveName == null ? "" : this.objectiveName);
        if (this.method != ServerScoreboard.Method.REMOVE) {
            friendlyByteBuf0.writeVarInt(this.score);
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetScore(this);
    }

    public String getOwner() {
        return this.owner;
    }

    @Nullable
    public String getObjectiveName() {
        return this.objectiveName;
    }

    public int getScore() {
        return this.score;
    }

    public ServerScoreboard.Method getMethod() {
        return this.method;
    }
}