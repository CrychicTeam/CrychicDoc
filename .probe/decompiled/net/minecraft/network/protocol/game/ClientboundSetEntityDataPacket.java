package net.minecraft.network.protocol.game;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.SynchedEntityData;

public record ClientboundSetEntityDataPacket(int f_133143_, List<SynchedEntityData.DataValue<?>> f_133144_) implements Packet<ClientGamePacketListener> {

    private final int id;

    private final List<SynchedEntityData.DataValue<?>> packedItems;

    public static final int EOF_MARKER = 255;

    public ClientboundSetEntityDataPacket(FriendlyByteBuf p_179290_) {
        this(p_179290_.readVarInt(), unpack(p_179290_));
    }

    public ClientboundSetEntityDataPacket(int f_133143_, List<SynchedEntityData.DataValue<?>> f_133144_) {
        this.id = f_133143_;
        this.packedItems = f_133144_;
    }

    private static void pack(List<SynchedEntityData.DataValue<?>> p_253940_, FriendlyByteBuf p_253901_) {
        for (SynchedEntityData.DataValue<?> $$2 : p_253940_) {
            $$2.write(p_253901_);
        }
        p_253901_.writeByte(255);
    }

    private static List<SynchedEntityData.DataValue<?>> unpack(FriendlyByteBuf p_253726_) {
        List<SynchedEntityData.DataValue<?>> $$1 = new ArrayList();
        int $$2;
        while (($$2 = p_253726_.readUnsignedByte()) != 255) {
            $$1.add(SynchedEntityData.DataValue.read(p_253726_, $$2));
        }
        return $$1;
    }

    @Override
    public void write(FriendlyByteBuf p_133158_) {
        p_133158_.writeVarInt(this.id);
        pack(this.packedItems, p_133158_);
    }

    public void handle(ClientGamePacketListener p_133155_) {
        p_133155_.handleSetEntityData(this);
    }
}