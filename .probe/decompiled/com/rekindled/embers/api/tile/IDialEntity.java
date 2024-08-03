package com.rekindled.embers.api.tile;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public interface IDialEntity {

    Packet<ClientGamePacketListener> getUpdatePacket(int var1);
}