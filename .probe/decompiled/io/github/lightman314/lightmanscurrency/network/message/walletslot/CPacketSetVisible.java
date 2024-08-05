package io.github.lightman314.lightmanscurrency.network.message.walletslot;

import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class CPacketSetVisible extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketSetVisible> HANDLER = new CPacketSetVisible.H();

    int entityID;

    boolean visible;

    public CPacketSetVisible(int entityID, boolean visible) {
        this.entityID = entityID;
        this.visible = visible;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeBoolean(this.visible);
    }

    private static class H extends CustomPacket.Handler<CPacketSetVisible> {

        @Nonnull
        public CPacketSetVisible decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketSetVisible(buffer.readInt(), buffer.readBoolean());
        }

        protected void handle(@Nonnull CPacketSetVisible message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                Entity entity = sender.m_9236_().getEntity(message.entityID);
                if (entity != null) {
                    IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(entity);
                    if (walletHandler != null) {
                        walletHandler.setVisible(message.visible);
                    }
                }
            }
        }
    }
}