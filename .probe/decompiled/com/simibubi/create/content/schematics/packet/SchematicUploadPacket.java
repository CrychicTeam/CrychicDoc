package com.simibubi.create.content.schematics.packet;

import com.simibubi.create.Create;
import com.simibubi.create.content.schematics.table.SchematicTableMenu;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SchematicUploadPacket extends SimplePacketBase {

    public static final int BEGIN = 0;

    public static final int WRITE = 1;

    public static final int FINISH = 2;

    private int code;

    private long size;

    private String schematic;

    private byte[] data;

    public SchematicUploadPacket(int code, String schematic) {
        this.code = code;
        this.schematic = schematic;
    }

    public static SchematicUploadPacket begin(String schematic, long size) {
        SchematicUploadPacket pkt = new SchematicUploadPacket(0, schematic);
        pkt.size = size;
        return pkt;
    }

    public static SchematicUploadPacket write(String schematic, byte[] data) {
        SchematicUploadPacket pkt = new SchematicUploadPacket(1, schematic);
        pkt.data = data;
        return pkt;
    }

    public static SchematicUploadPacket finish(String schematic) {
        return new SchematicUploadPacket(2, schematic);
    }

    public SchematicUploadPacket(FriendlyByteBuf buffer) {
        this.code = buffer.readInt();
        this.schematic = buffer.readUtf(256);
        if (this.code == 0) {
            this.size = buffer.readLong();
        }
        if (this.code == 1) {
            this.data = buffer.readByteArray();
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.code);
        buffer.writeUtf(this.schematic);
        if (this.code == 0) {
            buffer.writeLong(this.size);
        }
        if (this.code == 1) {
            buffer.writeByteArray(this.data);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (this.code == 0) {
                    BlockPos pos = ((SchematicTableMenu) player.f_36096_).contentHolder.m_58899_();
                    Create.SCHEMATIC_RECEIVER.handleNewUpload(player, this.schematic, this.size, pos);
                }
                if (this.code == 1) {
                    Create.SCHEMATIC_RECEIVER.handleWriteRequest(player, this.schematic, this.data);
                }
                if (this.code == 2) {
                    Create.SCHEMATIC_RECEIVER.handleFinishedUpload(player, this.schematic);
                }
            }
        });
        return true;
    }
}