package io.github.lightman314.lightmanscurrency.network.message.bank;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.BankAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountAdvancedMenu;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.PlayerBankReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketBankTransferPlayer extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketBankTransferPlayer> HANDLER = new CPacketBankTransferPlayer.H();

    String playerName;

    MoneyValue amount;

    public CPacketBankTransferPlayer(String playerName, MoneyValue amount) {
        this.playerName = playerName;
        this.amount = amount;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(this.playerName);
        this.amount.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<CPacketBankTransferPlayer> {

        @Nonnull
        public CPacketBankTransferPlayer decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketBankTransferPlayer(buffer.readUtf(), MoneyValue.decode(buffer));
        }

        protected void handle(@Nonnull CPacketBankTransferPlayer message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof IBankAccountAdvancedMenu menu) {
                BankReference destination = PlayerBankReference.of(PlayerReference.of(false, message.playerName));
                MutableComponent response = BankAPI.API.BankTransfer(menu, message.amount, destination.get());
                if (response != null) {
                    new SPacketBankTransferResponse(response).sendTo(sender);
                }
            }
        }
    }
}