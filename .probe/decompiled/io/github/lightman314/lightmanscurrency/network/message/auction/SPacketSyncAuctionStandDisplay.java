package io.github.lightman314.lightmanscurrency.network.message.auction;

import io.github.lightman314.lightmanscurrency.common.blockentity.AuctionStandBlockEntity;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncAuctionStandDisplay extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncAuctionStandDisplay> HANDLER = new SPacketSyncAuctionStandDisplay.H();

    private final List<ItemStack> items;

    public SPacketSyncAuctionStandDisplay(List<ItemStack> items) {
        this.items = InventoryUtil.copyList(items);
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.items.size());
        for (ItemStack item : this.items) {
            buffer.writeItem(item);
        }
    }

    private static class H extends CustomPacket.Handler<SPacketSyncAuctionStandDisplay> {

        @Nonnull
        public SPacketSyncAuctionStandDisplay decode(@Nonnull FriendlyByteBuf buffer) {
            List<ItemStack> items = new ArrayList();
            int count = buffer.readInt();
            for (int i = 0; i < count; i++) {
                items.add(buffer.readItem());
            }
            return new SPacketSyncAuctionStandDisplay(items);
        }

        protected void handle(@Nonnull SPacketSyncAuctionStandDisplay message, @Nullable ServerPlayer sender) {
            AuctionStandBlockEntity.syncItemsFromServer(message.items);
        }
    }
}