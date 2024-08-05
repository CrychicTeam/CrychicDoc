package io.github.lightman314.lightmanscurrency.network;

import io.github.lightman314.lightmanscurrency.network.message.CPacketRequestNBT;
import io.github.lightman314.lightmanscurrency.network.message.auction.CPacketSubmitBid;
import io.github.lightman314.lightmanscurrency.network.message.auction.SPacketStartBid;
import io.github.lightman314.lightmanscurrency.network.message.auction.SPacketSyncAuctionStandDisplay;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketATMSetPlayerAccount;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketBankInteraction;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketBankTransferPlayer;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketBankTransferTeam;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketOpenATM;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketSelectBankAccount;
import io.github.lightman314.lightmanscurrency.network.message.bank.SPacketATMPlayerAccountResponse;
import io.github.lightman314.lightmanscurrency.network.message.bank.SPacketBankTransferResponse;
import io.github.lightman314.lightmanscurrency.network.message.command.SPacketDebugTrader;
import io.github.lightman314.lightmanscurrency.network.message.command.SPacketSyncAdminList;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketEditConfig;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketEditListConfig;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketReloadConfig;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketResetConfig;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketSyncConfig;
import io.github.lightman314.lightmanscurrency.network.message.config.SPacketViewConfig;
import io.github.lightman314.lightmanscurrency.network.message.data.SPacketSyncCoinData;
import io.github.lightman314.lightmanscurrency.network.message.data.SPacketSyncNotifications;
import io.github.lightman314.lightmanscurrency.network.message.data.bank.SPacketClearClientBank;
import io.github.lightman314.lightmanscurrency.network.message.data.bank.SPacketSyncSelectedBankAccount;
import io.github.lightman314.lightmanscurrency.network.message.data.bank.SPacketUpdateClientBank;
import io.github.lightman314.lightmanscurrency.network.message.data.team.SPacketClearClientTeams;
import io.github.lightman314.lightmanscurrency.network.message.data.team.SPacketRemoveClientTeam;
import io.github.lightman314.lightmanscurrency.network.message.data.team.SPacketUpdateClientTeam;
import io.github.lightman314.lightmanscurrency.network.message.data.trader.SPacketClearClientTraders;
import io.github.lightman314.lightmanscurrency.network.message.data.trader.SPacketMessageRemoveClientTrader;
import io.github.lightman314.lightmanscurrency.network.message.data.trader.SPacketUpdateClientTrader;
import io.github.lightman314.lightmanscurrency.network.message.emergencyejection.CPacketOpenEjectionMenu;
import io.github.lightman314.lightmanscurrency.network.message.emergencyejection.SPacketSyncEjectionData;
import io.github.lightman314.lightmanscurrency.network.message.enchantments.SPacketMoneyMendingClink;
import io.github.lightman314.lightmanscurrency.network.message.event.SPacketSyncEventUnlocks;
import io.github.lightman314.lightmanscurrency.network.message.interfacebe.CPacketInterfaceHandlerMessage;
import io.github.lightman314.lightmanscurrency.network.message.menu.CPacketLazyMenu;
import io.github.lightman314.lightmanscurrency.network.message.menu.SPacketLazyMenu;
import io.github.lightman314.lightmanscurrency.network.message.notifications.CPacketFlagNotificationsSeen;
import io.github.lightman314.lightmanscurrency.network.message.notifications.SPacketChatNotification;
import io.github.lightman314.lightmanscurrency.network.message.paygate.CPacketCollectTicketStubs;
import io.github.lightman314.lightmanscurrency.network.message.persistentdata.CPacketCreatePersistentAuction;
import io.github.lightman314.lightmanscurrency.network.message.persistentdata.CPacketCreatePersistentTrader;
import io.github.lightman314.lightmanscurrency.network.message.playertrading.CPacketPlayerTradeInteraction;
import io.github.lightman314.lightmanscurrency.network.message.playertrading.SPacketSyncPlayerTrade;
import io.github.lightman314.lightmanscurrency.network.message.tax.SPacketRemoveTax;
import io.github.lightman314.lightmanscurrency.network.message.tax.SPacketSyncClientTax;
import io.github.lightman314.lightmanscurrency.network.message.teams.CPacketCreateTeam;
import io.github.lightman314.lightmanscurrency.network.message.teams.CPacketEditTeam;
import io.github.lightman314.lightmanscurrency.network.message.teams.SPacketCreateTeamResponse;
import io.github.lightman314.lightmanscurrency.network.message.time.SPacketSyncTime;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketAddOrRemoveTrade;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketCollectCoins;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketExecuteTrade;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenNetworkTerminal;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenStorage;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketOpenTrades;
import io.github.lightman314.lightmanscurrency.network.message.trader.CPacketStoreCoins;
import io.github.lightman314.lightmanscurrency.network.message.trader.SPacketSyncUsers;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketChestQuickCollect;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketOpenWallet;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketOpenWalletBank;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketWalletExchangeCoins;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketWalletQuickCollect;
import io.github.lightman314.lightmanscurrency.network.message.wallet.CPacketWalletToggleAutoExchange;
import io.github.lightman314.lightmanscurrency.network.message.wallet.SPacketPlayPickupSound;
import io.github.lightman314.lightmanscurrency.network.message.walletslot.CPacketCreativeWalletEdit;
import io.github.lightman314.lightmanscurrency.network.message.walletslot.CPacketSetVisible;
import io.github.lightman314.lightmanscurrency.network.message.walletslot.SPacketSyncWallet;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class LightmansCurrencyPacketHandler {

    public static final String PROTOCOL_VERSION = "1";

    public static SimpleChannel instance;

    private static int nextId = 0;

    public static void init() {
        instance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("lightmanscurrency", "network")).networkProtocolVersion(() -> "1").clientAcceptedVersions("1"::equals).serverAcceptedVersions("1"::equals).simpleChannel();
        register(CPacketOpenATM.class, CPacketOpenATM.HANDLER);
        register(CPacketSelectBankAccount.class, CPacketSelectBankAccount.HANDLER);
        register(CPacketBankInteraction.class, CPacketBankInteraction.HANDLER);
        register(SPacketClearClientBank.class, SPacketClearClientBank.HANDLER);
        register(SPacketUpdateClientBank.class, SPacketUpdateClientBank.HANDLER);
        register(CPacketBankTransferTeam.class, CPacketBankTransferTeam.HANDLER);
        register(CPacketBankTransferPlayer.class, CPacketBankTransferPlayer.HANDLER);
        register(SPacketBankTransferResponse.class, SPacketBankTransferResponse.HANDLER);
        register(CPacketATMSetPlayerAccount.class, CPacketATMSetPlayerAccount.HANDLER);
        register(SPacketATMPlayerAccountResponse.class, SPacketATMPlayerAccountResponse.HANDLER);
        register(SPacketSyncSelectedBankAccount.class, SPacketSyncSelectedBankAccount.HANDLER);
        register(CPacketExecuteTrade.class, CPacketExecuteTrade.HANDLER);
        register(CPacketCollectCoins.class, CPacketCollectCoins.HANDLER);
        register(CPacketStoreCoins.class, CPacketStoreCoins.HANDLER);
        register(CPacketOpenStorage.class, CPacketOpenStorage.HANDLER);
        register(CPacketOpenTrades.class, CPacketOpenTrades.HANDLER);
        register(CPacketOpenNetworkTerminal.class, CPacketOpenNetworkTerminal.HANDLER);
        register(SPacketSyncUsers.class, SPacketSyncUsers.HANDLER);
        register(CPacketAddOrRemoveTrade.class, CPacketAddOrRemoveTrade.HANDLER);
        register(CPacketCollectTicketStubs.class, CPacketCollectTicketStubs.HANDLER);
        register(SPacketPlayPickupSound.class, SPacketPlayPickupSound.HANDLER);
        register(CPacketWalletExchangeCoins.class, CPacketWalletExchangeCoins.HANDLER);
        register(CPacketWalletToggleAutoExchange.class, CPacketWalletToggleAutoExchange.HANDLER);
        register(CPacketOpenWallet.class, CPacketOpenWallet.HANDLER);
        register(CPacketOpenWalletBank.class, CPacketOpenWalletBank.HANDLER);
        register(CPacketWalletQuickCollect.class, CPacketWalletQuickCollect.HANDLER);
        register(CPacketChestQuickCollect.class, CPacketChestQuickCollect.HANDLER);
        register(SPacketSyncWallet.class, SPacketSyncWallet.HANDLER);
        register(CPacketSetVisible.class, CPacketSetVisible.HANDLER);
        register(CPacketCreativeWalletEdit.class, CPacketCreativeWalletEdit.HANDLER);
        register(SPacketClearClientTraders.class, SPacketClearClientTraders.HANDLER);
        register(SPacketUpdateClientTrader.class, SPacketUpdateClientTrader.HANDLER);
        register(SPacketMessageRemoveClientTrader.class, SPacketMessageRemoveClientTrader.HANDLER);
        register(SPacketStartBid.class, SPacketStartBid.HANDLER);
        register(CPacketSubmitBid.class, CPacketSubmitBid.HANDLER);
        register(SPacketSyncAuctionStandDisplay.class, SPacketSyncAuctionStandDisplay.HANDLER);
        register(CPacketInterfaceHandlerMessage.class, CPacketInterfaceHandlerMessage.HANDLER);
        register(SPacketClearClientTeams.class, SPacketClearClientTeams.HANDLER);
        register(SPacketRemoveClientTeam.class, SPacketRemoveClientTeam.HANDLER);
        register(SPacketUpdateClientTeam.class, SPacketUpdateClientTeam.HANDLER);
        register(CPacketEditTeam.class, CPacketEditTeam.HANDLER);
        register(CPacketCreateTeam.class, CPacketCreateTeam.HANDLER);
        register(SPacketCreateTeamResponse.class, SPacketCreateTeamResponse.HANDLER);
        register(SPacketLazyMenu.class, SPacketLazyMenu.HANDLER);
        register(CPacketLazyMenu.class, CPacketLazyMenu.HANDLER);
        register(SPacketSyncNotifications.class, SPacketSyncNotifications.HANDLER);
        register(CPacketFlagNotificationsSeen.class, CPacketFlagNotificationsSeen.HANDLER);
        register(SPacketChatNotification.class, SPacketChatNotification.HANDLER);
        register(SPacketSyncClientTax.class, SPacketSyncClientTax.HANDLER);
        register(SPacketRemoveTax.class, SPacketRemoveTax.HANDLER);
        register(CPacketRequestNBT.class, CPacketRequestNBT.HANDLER);
        register(SPacketSyncTime.class, SPacketSyncTime.HANDLER);
        register(SPacketSyncAdminList.class, SPacketSyncAdminList.HANDLER);
        register(SPacketDebugTrader.class, SPacketDebugTrader.HANDLER);
        register(SPacketSyncCoinData.class, SPacketSyncCoinData.HANDLER);
        register(SPacketMoneyMendingClink.class, SPacketMoneyMendingClink.HANDLER);
        register(CPacketCreatePersistentTrader.class, CPacketCreatePersistentTrader.HANDLER);
        register(CPacketCreatePersistentAuction.class, CPacketCreatePersistentAuction.HANDLER);
        register(SPacketSyncEjectionData.class, SPacketSyncEjectionData.HANDLER);
        register(CPacketOpenEjectionMenu.class, CPacketOpenEjectionMenu.HANDLER);
        register(SPacketSyncPlayerTrade.class, SPacketSyncPlayerTrade.HANDLER);
        register(CPacketPlayerTradeInteraction.class, CPacketPlayerTradeInteraction.HANDLER);
        register(SPacketSyncEventUnlocks.class, SPacketSyncEventUnlocks.HANDLER);
        register(SPacketSyncConfig.class, SPacketSyncConfig.HANDLER);
        register(SPacketReloadConfig.class, SPacketReloadConfig.HANDLER);
        register(SPacketEditConfig.class, SPacketEditConfig.HANDLER);
        register(SPacketEditListConfig.class, SPacketEditListConfig.HANDLER);
        register(SPacketResetConfig.class, SPacketResetConfig.HANDLER);
        register(SPacketViewConfig.class, SPacketViewConfig.HANDLER);
    }

    private static <T extends CustomPacket> void register(@Nonnull Class<T> clazz, @Nonnull CustomPacket.Handler<T> handler) {
        instance.registerMessage(nextId++, clazz, CustomPacket::encode, handler::decode, handler::handlePacket);
    }

    public static PacketDistributor.PacketTarget getTarget(Player player) {
        return player instanceof ServerPlayer sp ? getTarget(sp) : null;
    }

    public static PacketDistributor.PacketTarget getTarget(ServerPlayer player) {
        return PacketDistributor.PLAYER.with(() -> player);
    }
}