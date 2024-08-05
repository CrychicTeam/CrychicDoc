package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.inventory.MenuType;

public class ClientboundOpenScreenPacket implements Packet<ClientGamePacketListener> {

    private final int containerId;

    private final MenuType<?> type;

    private final Component title;

    public ClientboundOpenScreenPacket(int int0, MenuType<?> menuType1, Component component2) {
        this.containerId = int0;
        this.type = menuType1;
        this.title = component2;
    }

    public ClientboundOpenScreenPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readVarInt();
        this.type = friendlyByteBuf0.readById(BuiltInRegistries.MENU);
        this.title = friendlyByteBuf0.readComponent();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.containerId);
        friendlyByteBuf0.writeId(BuiltInRegistries.MENU, this.type);
        friendlyByteBuf0.writeComponent(this.title);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleOpenScreen(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    @Nullable
    public MenuType<?> getType() {
        return this.type;
    }

    public Component getTitle() {
        return this.title;
    }
}