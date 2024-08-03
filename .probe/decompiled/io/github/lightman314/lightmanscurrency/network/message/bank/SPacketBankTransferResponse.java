package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountAdvancedMenu;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketBankTransferResponse extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketBankTransferResponse> HANDLER = new SPacketBankTransferResponse.H();

    MutableComponent responseMessage;

    public SPacketBankTransferResponse(MutableComponent responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        String json = Component.Serializer.toJson(this.responseMessage);
        buffer.writeInt(json.length());
        buffer.writeUtf(json);
    }

    private static class H extends CustomPacket.Handler<SPacketBankTransferResponse> {

        @Nonnull
        public SPacketBankTransferResponse decode(@Nonnull FriendlyByteBuf buffer) {
            int length = buffer.readInt();
            return new SPacketBankTransferResponse(Component.Serializer.fromJson(buffer.readUtf(length)));
        }

        protected void handle(@Nonnull SPacketBankTransferResponse message, @Nullable ServerPlayer sender) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player != null && player.f_36096_ instanceof IBankAccountAdvancedMenu menu) {
                menu.setTransferMessage(message.responseMessage);
            }
        }
    }
}