package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm.SelectionTab;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketATMPlayerAccountResponse extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketATMPlayerAccountResponse> HANDLER = new SPacketATMPlayerAccountResponse.H();

    final MutableComponent message;

    public SPacketATMPlayerAccountResponse(MutableComponent message) {
        this.message = message;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(Component.Serializer.toJson(this.message));
    }

    private static class H extends CustomPacket.Handler<SPacketATMPlayerAccountResponse> {

        @Nonnull
        public SPacketATMPlayerAccountResponse decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketATMPlayerAccountResponse(Component.Serializer.fromJson(buffer.readUtf()));
        }

        protected void handle(@Nonnull SPacketATMPlayerAccountResponse message, @Nullable ServerPlayer sender) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof ATMScreen screen && screen.currentTab() instanceof SelectionTab tab) {
                tab.ReceiveSelectPlayerResponse(message.message);
            }
        }
    }
}