package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketWalletToggleAutoExchange extends ClientToServerPacket.Simple {

    private static final CPacketWalletToggleAutoExchange INSTANCE = new CPacketWalletToggleAutoExchange();

    public static final CustomPacket.Handler<CPacketWalletToggleAutoExchange> HANDLER = new CPacketWalletToggleAutoExchange.H();

    private CPacketWalletToggleAutoExchange() {
    }

    public static void sendToServer() {
        INSTANCE.send();
    }

    private static class H extends CustomPacket.SimpleHandler<CPacketWalletToggleAutoExchange> {

        protected H() {
            super(CPacketWalletToggleAutoExchange.INSTANCE);
        }

        protected void handle(@Nonnull CPacketWalletToggleAutoExchange message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof WalletMenuBase menu) {
                menu.ToggleAutoExchange();
            }
        }
    }
}