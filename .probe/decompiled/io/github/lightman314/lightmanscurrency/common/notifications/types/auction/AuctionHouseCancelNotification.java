package io.github.lightman314.lightmanscurrency.common.notifications.types.auction;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.common.notifications.data.ItemData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AuctionHouseCancelNotification extends AuctionHouseNotification {

    public static final NotificationType<AuctionHouseCancelNotification> TYPE = new NotificationType<>(new ResourceLocation("lightmanscurrency", "auction_house_canceled"), AuctionHouseCancelNotification::new);

    List<ItemData> items;

    private AuctionHouseCancelNotification() {
    }

    public AuctionHouseCancelNotification(AuctionTradeData trade) {
        this.items = new ArrayList();
        for (int i = 0; i < trade.getAuctionItems().size(); i++) {
            this.items.add(new ItemData((ItemStack) trade.getAuctionItems().get(i)));
        }
    }

    @Nonnull
    @Override
    protected NotificationType<AuctionHouseCancelNotification> getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public MutableComponent getMessage() {
        Component itemText = ItemData.getItemNames(this.items);
        return LCText.NOTIFICATION_AUCTION_CANCEL.get(itemText);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        ListTag itemList = new ListTag();
        for (ItemData item : this.items) {
            itemList.add(item.save());
        }
        compound.put("Items", itemList);
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag compound) {
        ListTag itemList = compound.getList("Items", 10);
        this.items = new ArrayList();
        for (int i = 0; i < itemList.size(); i++) {
            this.items.add(ItemData.load(itemList.getCompound(i)));
        }
    }
}