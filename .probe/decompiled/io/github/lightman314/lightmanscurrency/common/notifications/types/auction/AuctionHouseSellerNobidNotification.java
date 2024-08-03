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

public class AuctionHouseSellerNobidNotification extends AuctionHouseNotification {

    public static final NotificationType<AuctionHouseSellerNobidNotification> TYPE = new NotificationType<>(new ResourceLocation("lightmanscurrency", "auction_house_seller_nobid"), AuctionHouseSellerNobidNotification::new);

    List<ItemData> items;

    private AuctionHouseSellerNobidNotification() {
    }

    public AuctionHouseSellerNobidNotification(AuctionTradeData trade) {
        this.items = new ArrayList();
        for (int i = 0; i < trade.getAuctionItems().size(); i++) {
            this.items.add(new ItemData((ItemStack) trade.getAuctionItems().get(i)));
        }
    }

    @Nonnull
    @Override
    protected NotificationType<AuctionHouseSellerNobidNotification> getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public MutableComponent getMessage() {
        Component itemText = ItemData.getItemNames(this.items);
        return LCText.NOTIFICATION_AUCTION_SELLER_NO_BID.get(itemText);
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