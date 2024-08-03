package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketWalletQuickCollect extends ClientToServerPacket.Simple {

    private static final CPacketWalletQuickCollect INSTANCE = new CPacketWalletQuickCollect();

    public static final CustomPacket.Handler<CPacketWalletQuickCollect> HANDLER = new CPacketWalletQuickCollect.H();

    private CPacketWalletQuickCollect() {
    }

    public static void sendToServer() {
        INSTANCE.send();
    }

    private static class H extends CustomPacket.SimpleHandler<CPacketWalletQuickCollect> {

        protected H() {
            super(CPacketWalletQuickCollect.INSTANCE);
        }

        protected void handle(@Nonnull CPacketWalletQuickCollect message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof WalletMenu menu) {
                menu.QuickCollectCoins();
            }
        }
    }
}