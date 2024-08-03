package io.github.lightman314.lightmanscurrency.api.traders.menu.storage;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IEasyScreen;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ITraderStorageScreen extends IEasyScreen {

    @Nonnull
    ITraderStorageMenu getMenu();

    void changeTab(int var1);

    void changeTab(int var1, boolean var2, @Nullable LazyPacketData.Builder var3);

    void selfMessage(@Nonnull LazyPacketData.Builder var1);
}