package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketWalletExchangeCoins extends ClientToServerPacket.Simple {

    private static final CPacketWalletExchangeCoins INSTANCE = new CPacketWalletExchangeCoins();

    public static final CustomPacket.Handler<CPacketWalletExchangeCoins> HANDLER = new CPacketWalletExchangeCoins.H();

    public static void sendToServer() {
        INSTANCE.send();
    }

    private CPacketWalletExchangeCoins() {
    }

    private static class H extends CustomPacket.SimpleHandler<CPacketWalletExchangeCoins> {

        protected H() {
            super(CPacketWalletExchangeCoins.INSTANCE);
        }

        protected void handle(@Nonnull CPacketWalletExchangeCoins message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof WalletMenuBase menu) {
                menu.ExchangeCoints();
            }
        }
    }
}