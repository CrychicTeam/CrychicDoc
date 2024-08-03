package io.github.lightman314.lightmanscurrency.network.message.command;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncAdminList extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncAdminList> HANDLER = new SPacketSyncAdminList.H();

    List<UUID> adminList;

    public SPacketSyncAdminList(List<UUID> adminList) {
        this.adminList = adminList;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.adminList.size());
        for (UUID entry : this.adminList) {
            buffer.writeUUID(entry);
        }
    }

    private static class H extends CustomPacket.Handler<SPacketSyncAdminList> {

        @Nonnull
        public SPacketSyncAdminList decode(@Nonnull FriendlyByteBuf buffer) {
            int entryCount = buffer.readInt();
            List<UUID> entries = new ArrayList();
            for (int i = 0; i < entryCount; i++) {
                entries.add(buffer.readUUID());
            }
            return new SPacketSyncAdminList(entries);
        }

        protected void handle(@Nonnull SPacketSyncAdminList message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.loadAdminPlayers(message.adminList);
        }
    }
}