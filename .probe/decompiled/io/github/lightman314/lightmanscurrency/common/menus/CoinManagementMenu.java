package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.impl.CoinAPIImpl;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CoinManagementMenu extends LazyMessageMenu {

    public CoinManagementMenu(int id, @Nonnull Inventory inventory) {
        super(ModMenus.COIN_MANAGEMENT.get(), id, inventory);
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (!this.isClient()) {
            if (message.contains("SaveData") && this.player.m_20310_(2)) {
                if (this.player.m_20310_(2)) {
                    String data = message.getString("SaveData");
                    CoinAPIImpl.LoadEditedData(data);
                } else {
                    LightmansCurrency.LogError("Non-admin player " + this.player.getDisplayName().getString() + " attempted to edit the coin data!");
                }
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onValidationTick(@Nonnull Player player) {
        if (!player.m_20310_(2)) {
            this.player.closeContainer();
        }
    }
}