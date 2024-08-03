package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketOpenWalletBank extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketOpenWalletBank> HANDLER = new CPacketOpenWalletBank.H();

    private final int walletStackIndex;

    public CPacketOpenWalletBank(int walletStackIndex) {
        this.walletStackIndex = walletStackIndex;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.walletStackIndex);
    }

    private static class H extends CustomPacket.Handler<CPacketOpenWalletBank> {

        @Nonnull
        public CPacketOpenWalletBank decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketOpenWalletBank(buffer.readInt());
        }

        protected void handle(@Nonnull CPacketOpenWalletBank message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                WalletMenuBase.SafeOpenWalletBankMenu(sender, message.walletStackIndex);
            }
        }
    }
}