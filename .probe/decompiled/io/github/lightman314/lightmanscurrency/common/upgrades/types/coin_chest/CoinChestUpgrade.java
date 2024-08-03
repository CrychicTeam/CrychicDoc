package io.github.lightman314.lightmanscurrency.common.upgrades.types.coin_chest;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.menus.CoinChestMenu;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public abstract class CoinChestUpgrade extends UpgradeType {

    public boolean allowsDuplicates() {
        return false;
    }

    public abstract void HandleMenuMessage(@Nonnull CoinChestMenu var1, @Nonnull CoinChestUpgradeData var2, @Nonnull LazyPacketData var3);

    public void OnStorageChanged(@Nonnull CoinChestBlockEntity be, @Nonnull CoinChestUpgradeData data) {
    }

    public void OnEquip(@Nonnull CoinChestBlockEntity be, @Nonnull CoinChestUpgradeData data) {
    }

    public boolean BlockAccess(@Nonnull CoinChestBlockEntity be, @Nonnull CoinChestUpgradeData data, @Nonnull Player player) {
        return false;
    }

    public void OnValidBlockRemoval(@Nonnull CoinChestBlockEntity be, @Nonnull CoinChestUpgradeData data) {
    }

    public void OnBlockRemoval(@Nonnull CoinChestBlockEntity be, @Nonnull CoinChestUpgradeData data) {
    }

    public abstract void addClientTabs(@Nonnull CoinChestUpgradeData var1, @Nonnull Object var2, @Nonnull Consumer<Object> var3);

    @Nonnull
    @Override
    protected List<String> getDataTags() {
        return ImmutableList.of();
    }

    @Override
    protected Object defaultTagValue(String tag) {
        return null;
    }

    @Nonnull
    @Override
    protected List<Component> getBuiltInTargets() {
        return ImmutableList.of(formatTarget(ModBlocks.COIN_CHEST));
    }

    protected final boolean clearTags(CompoundTag itemTag, String... tags) {
        AtomicBoolean flag = new AtomicBoolean(false);
        for (String tag : tags) {
            if (itemTag.contains(tag)) {
                flag.set(true);
            }
            itemTag.remove(tag);
        }
        return flag.get();
    }
}