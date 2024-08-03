package io.github.lightman314.lightmanscurrency.network.message.data.bank;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncSelectedBankAccount extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncSelectedBankAccount> HANDLER = new SPacketSyncSelectedBankAccount.H();

    final BankReference selectedAccount;

    public SPacketSyncSelectedBankAccount(BankReference selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        this.selectedAccount.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<SPacketSyncSelectedBankAccount> {

        @Nonnull
        public SPacketSyncSelectedBankAccount decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncSelectedBankAccount(BankReference.decode(buffer).flagAsClient());
        }

        protected void handle(@Nonnull SPacketSyncSelectedBankAccount message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.receiveSelectedBankAccount(message.selectedAccount);
        }
    }
}