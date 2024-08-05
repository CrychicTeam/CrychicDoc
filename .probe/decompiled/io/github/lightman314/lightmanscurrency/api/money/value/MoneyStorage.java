package io.github.lightman314.lightmanscurrency.api.money.value;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.MoneyHolder;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public final class MoneyStorage extends MoneyHolder {

    private final Map<String, MoneyValue> values = new HashMap();

    private final Runnable markDirty;

    private final int priority;

    private boolean cacheChanged = true;

    public MoneyStorage(@Nonnull Runnable markDirty) {
        this(markDirty, 0);
    }

    public MoneyStorage(@Nonnull Runnable markDirty, int priority) {
        this.markDirty = (Runnable) Objects.requireNonNull(markDirty);
        this.priority = priority;
    }

    private void markDirty() {
        this.cacheChanged = true;
        this.markDirty.run();
    }

    @Override
    public int priority() {
        return this.priority;
    }

    @Nonnull
    public List<MoneyValue> allValues() {
        return ImmutableList.copyOf(this.values.values());
    }

    @Nonnull
    public MoneyValue valueOf(@Nonnull String uniqueName) {
        return (MoneyValue) this.values.getOrDefault(uniqueName, MoneyValue.empty());
    }

    @Nonnull
    @Override
    public MoneyValue insertMoney(@Nonnull MoneyValue insertAmount, boolean simulation) {
        if (!simulation) {
            this.addValue(insertAmount);
        }
        return MoneyValue.empty();
    }

    public void addValue(@Nonnull MoneyValue value) {
        if (!value.isEmpty() && !value.isFree()) {
            String name = value.getUniqueName();
            if (this.values.containsKey(name) && !((MoneyValue) this.values.get(name)).isInvalid()) {
                MoneyValue oldValue = (MoneyValue) this.values.get(name);
                MoneyValue addedValue = oldValue.addValue(value);
                if (addedValue == null) {
                    LightmansCurrency.LogError("Error adding money values of type '" + oldValue.getType() + "' and '" + value.getType() + "' together.");
                    return;
                }
                this.values.put(name, addedValue);
            } else {
                this.values.put(name, value);
            }
            this.markDirty();
        }
    }

    public void addValues(@Nonnull Collection<MoneyValue> values) {
        for (MoneyValue val : values) {
            if (val != null) {
                this.addValue(val);
            }
        }
    }

    public boolean containsValue(@Nonnull MoneyValue value) {
        return this.valueOf(value.getUniqueName()).containsValue(value);
    }

    @Nonnull
    public MoneyValue capValue(@Nonnull MoneyValue value) {
        return this.containsValue(value) ? value : this.valueOf(value.getUniqueName());
    }

    public boolean isEmpty() {
        return this.values.isEmpty() || this.values.values().stream().allMatch(MoneyValue::isEmpty);
    }

    public void removeValue(@Nonnull MoneyValue value) {
        if (!value.isFree() && !value.isEmpty()) {
            if (!this.containsValue(value)) {
                throw new IllegalArgumentException("Cannot remove more money than is stored in the holder!");
            } else {
                String name = value.getUniqueName();
                MoneyValue oldValue = (MoneyValue) this.values.get(name);
                MoneyValue newValue = oldValue.subtractValue(value);
                if (newValue == null) {
                    LightmansCurrency.LogError("Error subtracting money values of type '" + oldValue.getType() + "' and '" + value.getType() + "' together.");
                } else {
                    if (!newValue.isEmpty() && !newValue.isFree()) {
                        this.values.put(name, newValue);
                    } else {
                        this.values.remove(name);
                    }
                    this.markDirty();
                }
            }
        }
    }

    @Nonnull
    @Override
    public MoneyValue extractMoney(@Nonnull MoneyValue extractAmount, boolean simulation) {
        MoneyValue actualRemoved = this.capValue(extractAmount);
        if (!simulation) {
            this.removeValue(actualRemoved);
        }
        return extractAmount.subtractValue(actualRemoved);
    }

    @Override
    public boolean isMoneyTypeValid(@Nonnull MoneyValue value) {
        return true;
    }

    @Nonnull
    public ListTag save() {
        ListTag list = new ListTag();
        this.values.forEach((key, value) -> {
            if (!value.isEmpty()) {
                list.add(value.save());
            }
        });
        return list;
    }

    public void load(@Nonnull ListTag list) {
        this.values.clear();
        for (int i = 0; i < list.size(); i++) {
            MoneyValue value = MoneyValue.load(list.getCompound(i));
            this.values.put(value.getUniqueName(), value);
        }
        this.cacheChanged = true;
    }

    public void safeLoad(@Nonnull CompoundTag tag, @Nonnull String tagName) {
        if (tag.contains(tagName, 9)) {
            this.load(tag.getList(tagName, 10));
        } else {
            this.values.clear();
            MoneyValue value = MoneyValue.safeLoad(tag, tagName);
            if (value != null && !value.isEmpty() && !value.isFree()) {
                this.values.put(value.getUniqueName(), value);
            }
        }
    }

    public void clear() {
        this.values.clear();
        this.markDirty();
    }

    @Nonnull
    public Component getRandomValueText() {
        return this.getRandomValueText(LCText.GUI_MONEY_STORAGE_EMPTY.get());
    }

    @Nonnull
    public Component getRandomValueText(@Nonnull String emptyText) {
        return this.getRandomValueText(EasyText.literal(emptyText));
    }

    @Nonnull
    public Component getRandomValueText(@Nonnull Component emptyText) {
        if (this.values.isEmpty()) {
            return emptyText;
        } else {
            List<MoneyValue> values = this.values.values().stream().toList();
            int displayIndex = (int) (TimeUtil.getCurrentTime() / 2000L % (long) values.size());
            return ((MoneyValue) values.get(displayIndex)).getText();
        }
    }

    public Component getAllValueText() {
        MutableComponent text = EasyText.empty();
        for (MoneyValue value : this.values.values()) {
            if (!value.isEmpty()) {
                if (!text.getString().isEmpty()) {
                    text.append(LCText.GUI_SEPERATOR.get());
                }
                text.append(value.getText());
            }
        }
        return text;
    }

    @Override
    protected void collectStoredMoney(@Nonnull MoneyView.Builder builder) {
        for (MoneyValue value : this.values.values()) {
            builder.add(value);
        }
    }

    @Override
    public boolean hasStoredMoneyChanged() {
        return this.cacheChanged;
    }

    @Override
    public Component getTooltipTitle() {
        return LCText.TOOLTIP_MONEY_SOURCE_STORAGE.get();
    }

    public void GiveToPlayer(@Nonnull Player player) {
        IMoneyHolder handler = MoneyAPI.API.GetPlayersMoneyHandler(player);
        List<MoneyValue> extra = new ArrayList();
        for (MoneyValue value : this.allValues()) {
            MoneyValue e = handler.insertMoney(value, false);
            if (!e.isEmpty()) {
                extra.add(e);
            }
        }
        this.clear();
        for (MoneyValue e : extra) {
            this.addValue(e);
        }
    }
}