package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.common.items.PortableATMItem;
import io.github.lightman314.lightmanscurrency.common.menus.validation.EasyMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkHooks;

public class CPacketOpenATM extends ClientToServerPacket.Simple {

    private static final CPacketOpenATM INSTANCE = new CPacketOpenATM();

    public static CustomPacket.Handler<CPacketOpenATM> HANDLER = new CPacketOpenATM.H();

    public static void sendToServer() {
        INSTANCE.send();
    }

    private CPacketOpenATM() {
    }

    private static class H extends CustomPacket.SimpleHandler<CPacketOpenATM> {

        protected H() {
            super(CPacketOpenATM.INSTANCE);
        }

        public void handle(@Nonnull CPacketOpenATM message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                NetworkHooks.openScreen(sender, PortableATMItem.getMenuProvider(), EasyMenu.nullEncoder());
            }
        }
    }
}