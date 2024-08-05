package io.github.lightman314.lightmanscurrency.common.playertrading;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.IMoneyHandler;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.menus.PlayerTradeMenu;
import io.github.lightman314.lightmanscurrency.network.message.playertrading.SPacketSyncPlayerTrade;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTrade implements IPlayerTrade, MenuProvider {

    private boolean stillPending = true;

    public final long creationTime;

    public final int tradeID;

    private boolean completed = false;

    private final UUID hostPlayerID;

    private final UUID guestPlayerID;

    private MoneyValue hostMoney = MoneyValue.empty();

    private MoneyValue guestMoney = MoneyValue.empty();

    private final SimpleContainer hostItems = new SimpleContainer(12);

    private final SimpleContainer guestItems = new SimpleContainer(12);

    private int hostState = 0;

    private int guestState = 0;

    public static boolean ignoreDimension() {
        return LCConfig.SERVER.playerTradingRange.get() < 0.0;
    }

    public static boolean ignoreDistance() {
        return LCConfig.SERVER.playerTradingRange.get() <= 0.0;
    }

    public static double enforceDistance() {
        return LCConfig.SERVER.playerTradingRange.get();
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public boolean isHost(@Nonnull Player player) {
        return player.m_20148_() == this.hostPlayerID;
    }

    @Override
    public boolean isGuest(@Nonnull Player player) {
        return player.m_20148_() == this.guestPlayerID;
    }

    @Nonnull
    @Override
    public UUID getHostID() {
        return this.hostPlayerID;
    }

    @Nonnull
    @Override
    public Component getHostName() {
        ServerPlayer hostPlayer = this.getPlayer(this.hostPlayerID);
        return (Component) (hostPlayer == null ? Component.literal("NULL") : hostPlayer.m_7755_());
    }

    @Nonnull
    @Override
    public UUID getGuestID() {
        return this.guestPlayerID;
    }

    @Nonnull
    @Override
    public Component getGuestName() {
        ServerPlayer guestPlayer = this.getPlayer(this.guestPlayerID);
        return (Component) (guestPlayer == null ? Component.literal("NULL") : guestPlayer.m_7755_());
    }

    private boolean playerMissing(@Nonnull UUID playerID) {
        return this.getPlayer(playerID) == null;
    }

    private ServerPlayer getPlayer(UUID playerID) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.getPlayerList().getPlayer(playerID) : null;
    }

    @Nonnull
    @Override
    public MoneyValue getHostMoney() {
        return this.hostMoney;
    }

    @Nonnull
    @Override
    public MoneyValue getGuestMoney() {
        return this.guestMoney;
    }

    @Nonnull
    @Override
    public Container getHostItems() {
        return this.hostItems;
    }

    @Nonnull
    @Override
    public Container getGuestItems() {
        return this.guestItems;
    }

    @Override
    public int getHostState() {
        return this.hostState;
    }

    @Override
    public int getGuestState() {
        return this.guestState;
    }

    public PlayerTrade(ServerPlayer host, ServerPlayer guest, int tradeID) {
        this.hostPlayerID = host.m_20148_();
        this.guestPlayerID = guest.m_20148_();
        this.tradeID = tradeID;
        this.creationTime = TimeUtil.getCurrentTime();
        this.hostItems.addListener(this::onContainerChange);
        this.guestItems.addListener(this::onContainerChange);
    }

    private void onContainerChange(Container container) {
        if (this.hostItems == container) {
            this.onTradeEdit(true);
        }
        if (this.guestItems == container) {
            this.onTradeEdit(false);
        }
        this.markDirty();
    }

    private void onTradeEdit(boolean wasHost) {
        if (wasHost) {
            this.guestState = 0;
            this.hostState = Math.min(this.hostState, 1);
        } else {
            this.hostState = 0;
            this.guestState = Math.min(this.guestState, 1);
        }
    }

    private boolean playerDistanceExceeded() {
        return false;
    }

    public int isGuestInRange(ServerPlayer guest) {
        ServerPlayer host = this.getPlayer(this.hostPlayerID);
        if (host == null) {
            return 1;
        } else if (ignoreDimension()) {
            return 0;
        } else if (!Objects.equals(host.m_9236_().dimension().location(), guest.m_9236_().dimension().location())) {
            return 3;
        } else if (ignoreDistance()) {
            return 0;
        } else {
            double distance = host.m_20182_().distanceTo(guest.m_20182_());
            return distance <= enforceDistance() ? 0 : 2;
        }
    }

    private boolean playerAbandonedTrade(boolean host) {
        ServerPlayer player = this.getPlayer(host ? this.hostPlayerID : this.guestPlayerID);
        if (player == null) {
            LightmansCurrency.LogWarning("The " + (host ? "host" : "guest") + " is no longer online. Flagging trade as abandoned.");
            return true;
        } else if (!this.isInMenu(player)) {
            LightmansCurrency.LogWarning("The " + (host ? "host" : "guest") + " is no longer in the trade menu. Flagging trade as abandoned.");
            return true;
        } else {
            return false;
        }
    }

    private boolean isInMenu(Player player) {
        return player != null && player.containerMenu instanceof PlayerTradeMenu menu ? menu.tradeID == this.tradeID : false;
    }

    private void takeMenuAction(Consumer<PlayerTradeMenu> action) {
        this.ifInMenu(this.hostPlayerID, action);
        this.ifInMenu(this.guestPlayerID, action);
    }

    private void ifInMenu(UUID playerID, Consumer<PlayerTradeMenu> action) {
        Player player = this.getPlayer(playerID);
        if (player != null) {
            this.ifInMenu(player, action);
        }
    }

    private void ifInMenu(Player player, Consumer<PlayerTradeMenu> action) {
        if (player.containerMenu instanceof PlayerTradeMenu menu && menu.tradeID == this.tradeID) {
            action.accept(menu);
        }
    }

    public final void tryCloseMenu(UUID playerID) {
        ServerPlayer player = this.getPlayer(playerID);
        if (player != null) {
            this.tryCloseMenu(player);
        }
    }

    public final void tryCloseMenu(Player player) {
        if (this.isInMenu(player)) {
            player.closeContainer();
        }
    }

    public final boolean requestAccepted(ServerPlayer player) {
        if (this.stillPending && this.isGuest(player)) {
            this.stillPending = false;
            ServerPlayer host = this.getPlayer(this.hostPlayerID);
            ServerPlayer guest = this.getPlayer(this.guestPlayerID);
            if (host != null && guest != null) {
                NetworkHooks.openScreen(host, this, this::writeAdditionalMenuData);
                NetworkHooks.openScreen(guest, this, this::writeAdditionalMenuData);
                LightmansCurrency.LogInfo("Trade Request accepted, and Player Trading menu should be open for both players.");
                return true;
            } else {
                LightmansCurrency.LogWarning("Trade Request accepted, but either the Host or Guest is no longer online.");
                return false;
            }
        } else {
            if (!this.stillPending) {
                LightmansCurrency.LogWarning("Trade Request accepted, but the trade is not in a pending state!");
            }
            if (!this.isGuest(player)) {
                LightmansCurrency.LogWarning("Trade Request accepted by the wrong player.");
            }
            return false;
        }
    }

    public boolean shouldCancel() {
        if (this.completed) {
            return true;
        } else if (this.stillPending) {
            if (this.playerMissing(this.hostPlayerID)) {
                LightmansCurrency.LogInfo("Cancelling pending trade as the host is missing.");
                return true;
            } else if (this.playerMissing(this.guestPlayerID)) {
                LightmansCurrency.LogInfo("Cancelling pending trade as the guest is missing.");
                return true;
            } else if (!TimeUtil.compareTime(300000L, this.creationTime)) {
                LightmansCurrency.LogInfo("Cancelling pending trade as the trade has expired.");
                return true;
            } else {
                return false;
            }
        } else {
            return this.playerAbandonedTrade(true) || this.playerAbandonedTrade(false) || this.playerDistanceExceeded();
        }
    }

    public void onCancel() {
        if (!this.stillPending && !this.completed) {
            this.tryCloseMenu(this.hostPlayerID);
            this.tryCloseMenu(this.guestPlayerID);
        }
    }

    private void tryExecuteTrade() {
        if (this.hostState >= 2 && this.guestState >= 2 && !this.stillPending) {
            ServerPlayer host = this.getPlayer(this.hostPlayerID);
            ServerPlayer guest = this.getPlayer(this.guestPlayerID);
            if (host != null && guest != null) {
                IMoneyHandler hostHandler = MoneyAPI.API.GetPlayersMoneyHandler(host);
                IMoneyHandler guestHandler = MoneyAPI.API.GetPlayersMoneyHandler(guest);
                if (hostHandler.extractMoney(this.hostMoney, true).isEmpty() && hostHandler.insertMoney(this.guestMoney, true).isEmpty() && guestHandler.extractMoney(this.guestMoney, true).isEmpty() && guestHandler.insertMoney(this.hostMoney, true).isEmpty()) {
                    this.completed = true;
                    hostHandler.extractMoney(this.hostMoney, false);
                    hostHandler.insertMoney(this.guestMoney, false);
                    for (int i = 0; i < this.guestItems.getContainerSize(); i++) {
                        ItemStack stack = this.guestItems.getItem(i);
                        if (!stack.isEmpty()) {
                            ItemHandlerHelper.giveItemToPlayer(host, stack);
                        }
                    }
                    guestHandler.extractMoney(this.guestMoney, false);
                    guestHandler.insertMoney(this.hostMoney, false);
                    for (int ix = 0; ix < this.hostItems.getContainerSize(); ix++) {
                        ItemStack stack = this.hostItems.getItem(ix);
                        if (!stack.isEmpty()) {
                            ItemHandlerHelper.giveItemToPlayer(guest, stack);
                        }
                    }
                    this.tryCloseMenu(host);
                    this.tryCloseMenu(guest);
                }
            }
        }
    }

    private ClientPlayerTrade getData() {
        return new ClientPlayerTrade(this.hostPlayerID, this.guestPlayerID, this.getHostName(), this.getGuestName(), this.hostMoney, this.guestMoney, InventoryUtil.copy(this.hostItems), InventoryUtil.copy(this.guestItems), this.hostState, this.guestState);
    }

    public void handleInteraction(Player player, CompoundTag message) {
        if (this.isHost(player) || this.isGuest(player)) {
            if (this.isHost(player)) {
                if (message.contains("TogglePropose")) {
                    if (this.hostState > 0) {
                        this.hostState = 0;
                        this.guestState = Math.min(this.guestState, 1);
                    } else {
                        this.hostState = 1;
                    }
                } else if (message.contains("ToggleActive")) {
                    if (this.hostState == 2) {
                        this.hostState = 1;
                    } else if (this.hostState == 1 && this.guestState > 0) {
                        this.hostState = 2;
                        this.tryExecuteTrade();
                    }
                } else if (message.contains("ChangeMoney")) {
                    this.hostMoney = MoneyValue.load(message.getCompound("ChangeMoney"));
                    this.onTradeEdit(true);
                }
            } else {
                if (!this.isGuest(player)) {
                    return;
                }
                if (message.contains("TogglePropose")) {
                    if (this.guestState > 0) {
                        this.guestState = 0;
                        this.hostState = Math.min(this.hostState, 1);
                    } else {
                        this.guestState = 1;
                    }
                } else if (message.contains("ToggleActive")) {
                    if (this.guestState == 2) {
                        this.guestState = 1;
                    } else if (this.guestState == 1 && this.hostState > 0) {
                        this.guestState = 2;
                        this.tryExecuteTrade();
                    }
                } else if (message.contains("ChangeMoney")) {
                    this.guestMoney = MoneyValue.load(message.getCompound("ChangeMoney"));
                    this.onTradeEdit(false);
                }
            }
            this.markDirty();
        }
    }

    public final void markDirty() {
        ClientPlayerTrade data = this.getData();
        ServerPlayer hostPlayer = this.getPlayer(this.hostPlayerID);
        ServerPlayer guestPlayer = this.getPlayer(this.guestPlayerID);
        if (hostPlayer != null) {
            new SPacketSyncPlayerTrade(data).sendTo(hostPlayer);
        }
        if (guestPlayer != null) {
            new SPacketSyncPlayerTrade(data).sendTo(guestPlayer);
        }
        this.takeMenuAction(PlayerTradeMenu::onTradeChange);
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @NotNull Inventory inventory, @NotNull Player player) {
        return new PlayerTradeMenu(windowID, inventory, this.tradeID, this);
    }

    private void writeAdditionalMenuData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.tradeID);
        this.getData().encode(buffer);
    }
}