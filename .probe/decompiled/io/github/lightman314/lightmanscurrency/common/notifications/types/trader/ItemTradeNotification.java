package io.github.lightman314.lightmanscurrency.common.notifications.types.trader;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationType;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeDirection;
import io.github.lightman314.lightmanscurrency.common.notifications.categories.TraderCategory;
import io.github.lightman314.lightmanscurrency.common.notifications.data.ItemData;
import io.github.lightman314.lightmanscurrency.common.notifications.types.TaxableNotification;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.NonNullSupplier;

public class ItemTradeNotification extends TaxableNotification {

    public static final NotificationType<ItemTradeNotification> TYPE = new NotificationType<>(new ResourceLocation("lightmanscurrency", "item_trade"), ItemTradeNotification::new);

    TraderCategory traderData;

    TradeDirection tradeType;

    List<ItemData> items;

    MoneyValue cost = MoneyValue.empty();

    String customer;

    private ItemTradeNotification() {
    }

    public ItemTradeNotification(ItemTradeData trade, MoneyValue cost, PlayerReference customer, TraderCategory traderData, MoneyValue taxesPaid) {
        super(taxesPaid);
        this.traderData = traderData;
        this.tradeType = trade.getTradeDirection();
        this.items = new ArrayList();
        this.items.add(new ItemData(trade.getSellItem(0), trade.isPurchase() ? "" : trade.getCustomName(0)));
        this.items.add(new ItemData(trade.getSellItem(1), trade.isPurchase() ? "" : trade.getCustomName(1)));
        if (trade.isBarter()) {
            this.items.add(new ItemData(trade.getBarterItem(0), ""));
            this.items.add(new ItemData(trade.getBarterItem(1), ""));
        } else {
            this.cost = cost;
            LightmansCurrency.LogDebug("Created Item Trade Notification of cost " + this.cost.getString("NADA"));
        }
        this.customer = customer.getName(false);
    }

    public static NonNullSupplier<Notification> create(ItemTradeData trade, MoneyValue cost, PlayerReference customer, TraderCategory trader, MoneyValue taxesPaid) {
        return () -> new ItemTradeNotification(trade, cost, customer, trader, taxesPaid);
    }

    @Nonnull
    @Override
    protected NotificationType<ItemTradeNotification> getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public NotificationCategory getCategory() {
        return this.traderData;
    }

    @Nonnull
    @Override
    public MutableComponent getNormalMessage() {
        Component action = this.tradeType.getActionPhrase();
        Component itemText = ItemData.format((ItemData) this.items.get(0), (ItemData) this.items.get(1));
        Component cost;
        if (this.tradeType == TradeDirection.BARTER) {
            cost = itemText;
            itemText = ItemData.format((ItemData) this.items.get(2), (ItemData) this.items.get(3));
        } else {
            cost = this.cost.getText("NULL");
        }
        return LCText.NOTIFICATION_TRADE_ITEM.get(this.customer, action, itemText, cost);
    }

    @Override
    protected void saveNormal(CompoundTag compound) {
        compound.put("TraderInfo", this.traderData.save());
        compound.putInt("TradeType", this.tradeType.index);
        ListTag itemList = new ListTag();
        for (ItemData item : this.items) {
            itemList.add(item.save());
        }
        compound.put("Items", itemList);
        if (this.tradeType != TradeDirection.BARTER) {
            compound.put("Price", this.cost.save());
        }
        compound.putString("Customer", this.customer);
    }

    @Override
    protected void loadNormal(CompoundTag compound) {
        this.traderData = new TraderCategory(compound.getCompound("TraderInfo"));
        this.tradeType = TradeDirection.fromIndex(compound.getInt("TradeType"));
        ListTag itemList = compound.getList("Items", 10);
        this.items = new ArrayList();
        for (int i = 0; i < itemList.size(); i++) {
            this.items.add(ItemData.load(itemList.getCompound(i)));
        }
        if (this.tradeType != TradeDirection.BARTER) {
            this.cost = MoneyValue.safeLoad(compound, "Price");
        }
        this.customer = compound.getString("Customer");
    }

    @Override
    protected boolean canMerge(@Nonnull Notification other) {
        if (other instanceof ItemTradeNotification itn) {
            if (!itn.traderData.matches(this.traderData)) {
                return false;
            } else if (itn.tradeType != this.tradeType) {
                return false;
            } else if (itn.items.size() != this.items.size()) {
                return false;
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    if (!((ItemData) this.items.get(i)).matches((ItemData) itn.items.get(i))) {
                        return false;
                    }
                }
                if (itn.cost.equals(this.cost)) {
                    return false;
                } else {
                    return !itn.customer.equals(this.customer) ? false : this.TaxesMatch(itn);
                }
            }
        } else {
            return false;
        }
    }
}