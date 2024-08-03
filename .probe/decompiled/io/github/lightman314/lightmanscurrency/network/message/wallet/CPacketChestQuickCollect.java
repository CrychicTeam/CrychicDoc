package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ChestMenu;
import org.jetbrains.annotations.Nullable;

public class CPacketChestQuickCollect extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketChestQuickCollect> HANDLER = new CPacketChestQuickCollect.H();

    private final boolean allowSideChains;

    private CPacketChestQuickCollect(boolean allowSideChains) {
        this.allowSideChains = allowSideChains;
    }

    public static void sendToServer() {
        new CPacketChestQuickCollect(LCConfig.CLIENT.chestButtonAllowSideChains.get()).send();
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.allowSideChains);
    }

    private static class H extends CustomPacket.Handler<CPacketChestQuickCollect> {

        @Nonnull
        public CPacketChestQuickCollect decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketChestQuickCollect(buffer.readBoolean());
        }

        protected void handle(@Nonnull CPacketChestQuickCollect message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof ChestMenu menu) {
                WalletItem.QuickCollect(sender, menu.getContainer(), message.allowSideChains);
            }
        }
    }
}