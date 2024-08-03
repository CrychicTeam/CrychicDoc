package io.github.lightman314.lightmanscurrency.network.message.data.bank;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;

public class SPacketClearClientBank extends ServerToClientPacket.Simple {

    public static final SPacketClearClientBank INSTANCE = new SPacketClearClientBank();

    public static final CustomPacket.Handler<SPacketClearClientBank> HANDLER = new SPacketClearClientBank.H();

    private SPacketClearClientBank() {
    }

    private static class H extends CustomPacket.SimpleHandler<SPacketClearClientBank> {

        protected H() {
            super(SPacketClearClientBank.INSTANCE);
        }

        protected void handle(@Nonnull SPacketClearClientBank message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.clearBankAccounts();
        }
    }
}