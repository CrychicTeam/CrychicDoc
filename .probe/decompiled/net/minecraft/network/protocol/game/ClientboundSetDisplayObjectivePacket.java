package net.minecraft.network.protocol.game;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.scores.Objective;

public class ClientboundSetDisplayObjectivePacket implements Packet<ClientGamePacketListener> {

    private final int slot;

    private final String objectiveName;

    public ClientboundSetDisplayObjectivePacket(int int0, @Nullable Objective objective1) {
        this.slot = int0;
        if (objective1 == null) {
            this.objectiveName = "";
        } else {
            this.objectiveName = objective1.getName();
        }
    }

    public ClientboundSetDisplayObjectivePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.slot = friendlyByteBuf0.readByte();
        this.objectiveName = friendlyByteBuf0.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.slot);
        friendlyByteBuf0.writeUtf(this.objectiveName);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetDisplayObjective(this);
    }

    public int getSlot() {
        return this.slot;
    }

    @Nullable
    public String getObjectiveName() {
        return Objects.equals(this.objectiveName, "") ? null : this.objectiveName;
    }
}