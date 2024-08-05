package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ServerboundSeenAdvancementsPacket implements Packet<ServerGamePacketListener> {

    private final ServerboundSeenAdvancementsPacket.Action action;

    @Nullable
    private final ResourceLocation tab;

    public ServerboundSeenAdvancementsPacket(ServerboundSeenAdvancementsPacket.Action serverboundSeenAdvancementsPacketAction0, @Nullable ResourceLocation resourceLocation1) {
        this.action = serverboundSeenAdvancementsPacketAction0;
        this.tab = resourceLocation1;
    }

    public static ServerboundSeenAdvancementsPacket openedTab(Advancement advancement0) {
        return new ServerboundSeenAdvancementsPacket(ServerboundSeenAdvancementsPacket.Action.OPENED_TAB, advancement0.getId());
    }

    public static ServerboundSeenAdvancementsPacket closedScreen() {
        return new ServerboundSeenAdvancementsPacket(ServerboundSeenAdvancementsPacket.Action.CLOSED_SCREEN, null);
    }

    public ServerboundSeenAdvancementsPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.action = friendlyByteBuf0.readEnum(ServerboundSeenAdvancementsPacket.Action.class);
        if (this.action == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
            this.tab = friendlyByteBuf0.readResourceLocation();
        } else {
            this.tab = null;
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.action);
        if (this.action == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
            friendlyByteBuf0.writeResourceLocation(this.tab);
        }
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSeenAdvancements(this);
    }

    public ServerboundSeenAdvancementsPacket.Action getAction() {
        return this.action;
    }

    @Nullable
    public ResourceLocation getTab() {
        return this.tab;
    }

    public static enum Action {

        OPENED_TAB, CLOSED_SCREEN
    }
}