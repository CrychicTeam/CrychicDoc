package io.github.lightman314.lightmanscurrency.network.message.walletslot;

import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncWallet extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncWallet> HANDLER = new SPacketSyncWallet.H();

    int entityID;

    ItemStack walletItem;

    boolean visible;

    public SPacketSyncWallet(int entityID, ItemStack wallet, boolean visible) {
        this.entityID = entityID;
        this.walletItem = wallet;
        this.visible = visible;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeItemStack(this.walletItem, false);
        buffer.writeBoolean(this.visible);
    }

    private static class H extends CustomPacket.Handler<SPacketSyncWallet> {

        @Nonnull
        public SPacketSyncWallet decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncWallet(buffer.readInt(), buffer.readItem(), buffer.readBoolean());
        }

        protected void handle(@Nonnull SPacketSyncWallet message, @Nullable ServerPlayer sender) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft != null && minecraft.level.getEntity(message.entityID) instanceof LivingEntity livingEntity) {
                IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(livingEntity);
                if (walletHandler != null) {
                    walletHandler.syncWallet(message.walletItem);
                    walletHandler.setVisible(message.visible);
                }
            }
        }
    }
}