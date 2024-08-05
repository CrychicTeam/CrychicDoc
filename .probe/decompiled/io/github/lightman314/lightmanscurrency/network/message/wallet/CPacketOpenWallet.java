package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketOpenWallet extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketOpenWallet> HANDLER = new CPacketOpenWallet.H();

    private final int walletStackIndex;

    public CPacketOpenWallet(int walletStackIndex) {
        this.walletStackIndex = walletStackIndex;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.walletStackIndex);
    }

    private static class H extends CustomPacket.Handler<CPacketOpenWallet> {

        @Nonnull
        public CPacketOpenWallet decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketOpenWallet(buffer.readInt());
        }

        protected void handle(@Nonnull CPacketOpenWallet message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                WalletMenuBase.SafeOpenWalletMenu(sender, message.walletStackIndex);
            }
        }
    }
}