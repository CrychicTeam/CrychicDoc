package io.github.lightman314.lightmanscurrency.api.money.coins;

import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.common.impl.CoinAPIImpl;
import io.github.lightman314.lightmanscurrency.network.message.data.SPacketSyncCoinData;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public abstract class CoinAPI {

    public static final CoinAPI API = CoinAPIImpl.INSTANCE;

    public static final Comparator<ItemStack> COIN_SORTER = CoinAPIImpl.SORTER;

    public static final String MONEY_FILE_LOCATION = "config/lightmanscurrency/MasterCoinList.json";

    public static final String MAIN_CHAIN = "main";

    @Deprecated(since = "2.2.0.4")
    public static boolean DataNotReady() {
        return API.NoDataAvailable();
    }

    public abstract boolean NoDataAvailable();

    public abstract void Setup();

    @Deprecated(since = "2.2.0.4")
    public static void reloadMoneyDataFromFile() {
        API.ReloadCoinDataFromFile();
    }

    public abstract void ReloadCoinDataFromFile();

    @Deprecated(since = "2.2.0.4")
    @Nonnull
    public static ItemStack getWalletStack(@Nonnull Player player) {
        return API.getEquippedWallet(player);
    }

    @Nonnull
    public abstract ItemStack getEquippedWallet(@Nonnull Player var1);

    @Deprecated(since = "2.2.0.4")
    @Nullable
    public static ChainData getChainData(@Nonnull String chain) {
        return API.ChainData(chain);
    }

    @Nullable
    public abstract ChainData ChainData(@Nonnull String var1);

    @Nonnull
    @Deprecated(since = "2.2.0.4")
    public static List<ChainData> getAllChainData() {
        return API.AllChainData();
    }

    @Nonnull
    public abstract List<ChainData> AllChainData();

    @Deprecated(since = "2.2.0.4")
    @Nullable
    public static ChainData chainForCoin(@Nonnull ItemStack coin) {
        return API.ChainDataOfCoin(coin);
    }

    @Nullable
    public abstract ChainData ChainDataOfCoin(@Nonnull ItemStack var1);

    @Deprecated(since = "2.2.0.4")
    @Nullable
    public static ChainData chainForCoin(@Nonnull Item coin) {
        return API.ChainDataOfCoin(coin);
    }

    @Nullable
    public abstract ChainData ChainDataOfCoin(@Nonnull Item var1);

    @Deprecated(since = "2.2.0.4")
    public static boolean isCoin(@Nonnull ItemStack coin, boolean allowSideChains) {
        return API.IsCoin(coin, allowSideChains);
    }

    public abstract boolean IsCoin(@Nonnull ItemStack var1, boolean var2);

    @Deprecated(since = "2.2.0.4")
    public static boolean isCoin(@Nonnull Item coin, boolean allowSideChains) {
        return API.IsCoin(coin, allowSideChains);
    }

    public abstract boolean IsCoin(@Nonnull Item var1, boolean var2);

    @Deprecated(since = "2.2.0.4")
    public static void ExchangeAllCoinsUp(@Nonnull Container container) {
        API.CoinExchangeAllUp(container);
    }

    public abstract void CoinExchangeAllUp(@Nonnull Container var1);

    @Deprecated(since = "2.2.0.4")
    public static void ExchangeCoinsUp(@Nonnull Container container, @Nonnull Item smallCoin) {
        API.CoinExchangeUp(container, smallCoin);
    }

    public abstract void CoinExchangeUp(@Nonnull Container var1, @Nonnull Item var2);

    @Deprecated(since = "2.2.0.4")
    public static void ExchangeAllCoinsDown(@Nonnull Container container) {
        API.CoinExchangeAllDown(container);
    }

    public abstract void CoinExchangeAllDown(@Nonnull Container var1);

    @Deprecated(since = "2.2.0.4")
    public static void ExchangeCoinsDown(@Nonnull Container container, @Nonnull Item largeCoin) {
        API.CoinExchangeDown(container, largeCoin);
    }

    public abstract void CoinExchangeDown(@Nonnull Container var1, @Nonnull Item var2);

    @Deprecated(since = "2.2.0.4")
    public static void SortCoins(@Nonnull Container container) {
        API.SortCoinsByValue(container);
    }

    public abstract void SortCoinsByValue(@Nonnull Container var1);

    @Deprecated(since = "2.2.0.4")
    public static void syncDataWith(@Nonnull PacketDistributor.PacketTarget target) {
        API.SyncCoinDataWith(target);
    }

    public abstract void SyncCoinDataWith(@Nonnull PacketDistributor.PacketTarget var1);

    @Deprecated(since = "2.2.0.4")
    public static void handleSyncPacket(@Nonnull SPacketSyncCoinData packet) {
        API.HandleSyncPacket(packet);
    }

    public abstract void HandleSyncPacket(@Nonnull SPacketSyncCoinData var1);
}