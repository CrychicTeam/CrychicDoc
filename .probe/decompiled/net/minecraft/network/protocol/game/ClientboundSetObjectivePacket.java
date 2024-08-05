package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class ClientboundSetObjectivePacket implements Packet<ClientGamePacketListener> {

    public static final int METHOD_ADD = 0;

    public static final int METHOD_REMOVE = 1;

    public static final int METHOD_CHANGE = 2;

    private final String objectiveName;

    private final Component displayName;

    private final ObjectiveCriteria.RenderType renderType;

    private final int method;

    public ClientboundSetObjectivePacket(Objective objective0, int int1) {
        this.objectiveName = objective0.getName();
        this.displayName = objective0.getDisplayName();
        this.renderType = objective0.getRenderType();
        this.method = int1;
    }

    public ClientboundSetObjectivePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.objectiveName = friendlyByteBuf0.readUtf();
        this.method = friendlyByteBuf0.readByte();
        if (this.method != 0 && this.method != 2) {
            this.displayName = CommonComponents.EMPTY;
            this.renderType = ObjectiveCriteria.RenderType.INTEGER;
        } else {
            this.displayName = friendlyByteBuf0.readComponent();
            this.renderType = friendlyByteBuf0.readEnum(ObjectiveCriteria.RenderType.class);
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUtf(this.objectiveName);
        friendlyByteBuf0.writeByte(this.method);
        if (this.method == 0 || this.method == 2) {
            friendlyByteBuf0.writeComponent(this.displayName);
            friendlyByteBuf0.writeEnum(this.renderType);
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleAddObjective(this);
    }

    public String getObjectiveName() {
        return this.objectiveName;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public int getMethod() {
        return this.method;
    }

    public ObjectiveCriteria.RenderType getRenderType() {
        return this.renderType;
    }
}