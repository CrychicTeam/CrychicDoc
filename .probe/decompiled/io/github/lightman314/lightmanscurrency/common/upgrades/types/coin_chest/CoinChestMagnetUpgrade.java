package io.github.lightman314.lightmanscurrency.common.upgrades.types.coin_chest;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeData;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModSounds;
import io.github.lightman314.lightmanscurrency.common.menus.CoinChestMenu;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public class CoinChestMagnetUpgrade extends TickableCoinChestUpgrade {

    public static final String RANGE = "magnet_range";

    @Override
    public void HandleMenuMessage(@Nonnull CoinChestMenu menu, @Nonnull CoinChestUpgradeData data, @Nonnull LazyPacketData message) {
    }

    @Override
    public void addClientTabs(@Nonnull CoinChestUpgradeData data, @Nonnull Object screen, @Nonnull Consumer<Object> consumer) {
    }

    public int getRadius(CoinChestUpgradeData data) {
        return data.getUpgradeData().getIntValue("magnet_range");
    }

    @Override
    public void OnServerTick(@Nonnull CoinChestBlockEntity be, @Nonnull CoinChestUpgradeData data) {
        int radius = this.getRadius(data);
        Vector3f pos = be.m_58899_().getCenter().toVector3f();
        AABB searchBox = new AABB((double) (pos.x - (float) radius), (double) (pos.y - (float) radius), (double) (pos.z - (float) radius), (double) (pos.x + (float) radius), (double) (pos.y + (float) radius), (double) (pos.z + (float) radius));
        boolean playSound = false;
        for (Entity e : be.m_58904_().getEntities((Entity) null, searchBox, ex -> {
            if (ex instanceof ItemEntity item && CoinAPI.API.IsCoin(item.getItem(), true)) {
                return true;
            }
            return false;
        })) {
            ItemEntity ie = (ItemEntity) e;
            ItemStack coinStack = ie.getItem();
            ItemStack leftovers = InventoryUtil.TryPutItemStack(be.getStorage(), coinStack);
            if (leftovers.getCount() != coinStack.getCount()) {
                playSound = true;
                if (leftovers.isEmpty()) {
                    ie.m_146870_();
                } else {
                    ie.setItem(leftovers);
                }
            }
        }
        if (playSound) {
            be.m_58904_().playSound(null, be.m_58899_(), ModSounds.COINS_CLINKING.get(), SoundSource.PLAYERS, 0.4F, 1.0F);
        }
    }

    @Nonnull
    @Override
    protected List<String> getDataTags() {
        return ImmutableList.of("magnet_range");
    }

    @Override
    protected Object defaultTagValue(String tag) {
        return Objects.equals(tag, "magnet_range") ? 1 : null;
    }

    @Nonnull
    @Override
    public List<Component> getTooltip(@Nonnull UpgradeData data) {
        return ImmutableList.of(LCText.TOOLTIP_UPGRADE_MAGNET.get(data.getIntValue("magnet_range")));
    }
}