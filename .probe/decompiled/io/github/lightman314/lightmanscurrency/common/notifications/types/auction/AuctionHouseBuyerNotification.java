package io.github.lightman314.lightmanscurrency.common.notifications.types.auction;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
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

public class AuctionHouseBuyerNotification extends AuctionHouseNotification {

    public static final NotificationType<AuctionHouseBuyerNotification> TYPE = new NotificationType<>(new ResourceLocation("lightmanscurrency", "auction_house_buyer"), AuctionHouseBuyerNotification::new);

    List<ItemData> items;

    MoneyValue cost = MoneyValue.empty();

    private AuctionHouseBuyerNotification() {
    }

    public AuctionHouseBuyerNotification(AuctionTradeData trade) {
        this.cost = trade.getLastBidAmount();
        this.items = new ArrayList();
        for (int i = 0; i < trade.getAuctionItems().size(); i++) {
            this.items.add(new ItemData((ItemStack) trade.getAuctionItems().get(i)));
        }
    }

    @Nonnull
    @Override
    protected NotificationType<AuctionHouseBuyerNotification> getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public MutableComponent getMessage() {
        Component itemText = ItemData.getItemNames(this.items);
        Component cost = this.cost.getText("0");
        return LCText.NOTIFICATION_AUCTION_BUYER.get(itemText, cost);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        ListTag itemList = new ListTag();
        for (ItemData item : this.items) {
            itemList.add(item.save());
        }
        compound.put("Items", itemList);
        compound.put("Price", this.cost.save());
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag compound) {
        ListTag itemList = compound.getList("Items", 10);
        this.items = new ArrayList();
        for (int i = 0; i < itemList.size(); i++) {
            this.items.add(ItemData.load(itemList.getCompound(i)));
        }
        this.cost = MoneyValue.safeLoad(compound, "Price");
    }
}