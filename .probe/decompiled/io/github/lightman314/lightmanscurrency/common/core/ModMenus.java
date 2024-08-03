package io.github.lightman314.lightmanscurrency.common.core;

import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinMintBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.TicketStationBlockEntity;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import io.github.lightman314.lightmanscurrency.common.menus.CoinChestMenu;
import io.github.lightman314.lightmanscurrency.common.menus.CoinManagementMenu;
import io.github.lightman314.lightmanscurrency.common.menus.EjectionRecoveryMenu;
import io.github.lightman314.lightmanscurrency.common.menus.MintMenu;
import io.github.lightman314.lightmanscurrency.common.menus.PlayerTradeMenu;
import io.github.lightman314.lightmanscurrency.common.menus.SlotMachineMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TaxCollectorMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TerminalMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TicketStationMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.common.menus.TraderStorageMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletBankMenu;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenu;
import io.github.lightman314.lightmanscurrency.common.playertrading.ClientPlayerTrade;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final RegistryObject<MenuType<ATMMenu>> ATM = ModRegistries.MENUS.register("atm", () -> CreateType((IContainerFactory<ATMMenu>) (id, inventory, data) -> new ATMMenu(id, inventory, MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<MintMenu>> MINT = ModRegistries.MENUS.register("coinmint", () -> CreateType((IContainerFactory<MintMenu>) (id, inventory, data) -> {
        CoinMintBlockEntity blockEntity = (CoinMintBlockEntity) inventory.player.m_9236_().getBlockEntity(data.readBlockPos());
        return new MintMenu(id, inventory, blockEntity);
    }));

    public static final RegistryObject<MenuType<TerminalMenu>> NETWORK_TERMINAL = ModRegistries.MENUS.register("network_terminal", () -> CreateType((IContainerFactory<TerminalMenu>) (id, inventory, data) -> new TerminalMenu(id, inventory, MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<TraderMenu>> TRADER = ModRegistries.MENUS.register("trader", () -> CreateType((IContainerFactory<TraderMenu>) (id, inventory, data) -> new TraderMenu(id, inventory, data.readLong(), MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<TraderMenu.TraderMenuBlockSource>> TRADER_BLOCK = ModRegistries.MENUS.register("trader_block", () -> CreateType((IContainerFactory<TraderMenu.TraderMenuBlockSource>) (id, inventory, data) -> new TraderMenu.TraderMenuBlockSource(id, inventory, data.readBlockPos(), MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<TraderMenu.TraderMenuAllNetwork>> TRADER_NETWORK_ALL = ModRegistries.MENUS.register("trader_network_all", () -> CreateType((IContainerFactory<TraderMenu.TraderMenuAllNetwork>) (id, inventory, data) -> new TraderMenu.TraderMenuAllNetwork(id, inventory, MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<SlotMachineMenu>> SLOT_MACHINE = ModRegistries.MENUS.register("slot_machine", () -> CreateType((IContainerFactory<SlotMachineMenu>) (id, inventory, data) -> new SlotMachineMenu(id, inventory, data.readLong(), MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<TraderStorageMenu>> TRADER_STORAGE = ModRegistries.MENUS.register("trader_storage", () -> CreateType((IContainerFactory<TraderStorageMenu>) (id, inventory, data) -> new TraderStorageMenu(id, inventory, data.readLong(), MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<WalletMenu>> WALLET = ModRegistries.MENUS.register("wallet", () -> CreateType((IContainerFactory<WalletMenu>) (id, inventory, data) -> new WalletMenu(id, inventory, data.readInt())));

    public static final RegistryObject<MenuType<WalletBankMenu>> WALLET_BANK = ModRegistries.MENUS.register("wallet_bank", () -> CreateType((IContainerFactory<WalletBankMenu>) (id, inventory, data) -> new WalletBankMenu(id, inventory, data.readInt())));

    public static final RegistryObject<MenuType<TicketStationMenu>> TICKET_MACHINE = ModRegistries.MENUS.register("ticket_machine", () -> CreateType((IContainerFactory<TicketStationMenu>) (id, inventory, data) -> {
        TicketStationBlockEntity blockEntity = (TicketStationBlockEntity) inventory.player.m_9236_().getBlockEntity(data.readBlockPos());
        return new TicketStationMenu(id, inventory, blockEntity);
    }));

    public static final RegistryObject<MenuType<TraderInterfaceMenu>> TRADER_INTERFACE = ModRegistries.MENUS.register("trader_interface", () -> CreateType((IContainerFactory<TraderInterfaceMenu>) (id, inventory, data) -> {
        TraderInterfaceBlockEntity blockEntity = (TraderInterfaceBlockEntity) inventory.player.m_9236_().getBlockEntity(data.readBlockPos());
        return new TraderInterfaceMenu(id, inventory, blockEntity);
    }));

    public static final RegistryObject<MenuType<EjectionRecoveryMenu>> EJECTION_RECOVERY = ModRegistries.MENUS.register("trader_recovery", () -> CreateType((IContainerFactory<EjectionRecoveryMenu>) (id, inventory, data) -> new EjectionRecoveryMenu(id, inventory)));

    public static final RegistryObject<MenuType<PlayerTradeMenu>> PLAYER_TRADE = ModRegistries.MENUS.register("player_trading", () -> CreateType((IContainerFactory<PlayerTradeMenu>) (id, inventory, data) -> new PlayerTradeMenu(id, inventory, data.readInt(), ClientPlayerTrade.decode(data))));

    public static final RegistryObject<MenuType<CoinChestMenu>> COIN_CHEST = ModRegistries.MENUS.register("coin_chest", () -> CreateType((IContainerFactory<CoinChestMenu>) (id, inventory, data) -> {
        CoinChestBlockEntity blockEntity = (CoinChestBlockEntity) inventory.player.m_9236_().getBlockEntity(data.readBlockPos());
        return new CoinChestMenu(id, inventory, blockEntity);
    }));

    public static final RegistryObject<MenuType<TaxCollectorMenu>> TAX_COLLECTOR = ModRegistries.MENUS.register("tax_collector", () -> CreateType((IContainerFactory<TaxCollectorMenu>) (id, inventory, data) -> new TaxCollectorMenu(id, inventory, data.readLong(), MenuValidator.decode(data))));

    public static final RegistryObject<MenuType<CoinManagementMenu>> COIN_MANAGEMENT = ModRegistries.MENUS.register("coin_management", () -> CreateType((IContainerFactory<CoinManagementMenu>) (id, inventory, data) -> new CoinManagementMenu(id, inventory)));

    public static void init() {
    }

    private static <T extends AbstractContainerMenu> MenuType<T> CreateType(MenuType.MenuSupplier<T> supplier) {
        return new MenuType<>(supplier, FeatureFlagSet.of());
    }
}