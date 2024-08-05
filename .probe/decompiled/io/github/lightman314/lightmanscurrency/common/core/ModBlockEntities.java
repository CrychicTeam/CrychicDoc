package io.github.lightman314.lightmanscurrency.common.core;

import io.github.lightman314.lightmanscurrency.common.blockentity.AuctionStandBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CapabilityInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CashRegisterBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinJarBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinMintBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.ItemTraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.TaxBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.TicketStationBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.ArmorDisplayTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.BookTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.FreezerTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.ItemTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.PaygateBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.SlotMachineTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.TicketTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.util.BlockEntityBlockHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final RegistryObject<BlockEntityType<ItemTraderBlockEntity>> ITEM_TRADER = ModRegistries.BLOCK_ENTITIES.register("item_trader", () -> BlockEntityType.Builder.of(ItemTraderBlockEntity::new, BlockEntityBlockHelper.getBlocksForBlockEntity(BlockEntityBlockHelper.ITEM_TRADER_TYPE)).build(null));

    public static final RegistryObject<BlockEntityType<ArmorDisplayTraderBlockEntity>> ARMOR_TRADER = ModRegistries.BLOCK_ENTITIES.register("armor_trader", () -> BlockEntityType.Builder.of(ArmorDisplayTraderBlockEntity::new, ModBlocks.ARMOR_DISPLAY.get()).build(null));

    public static final RegistryObject<BlockEntityType<FreezerTraderBlockEntity>> FREEZER_TRADER = ModRegistries.BLOCK_ENTITIES.register("freezer_trader", () -> BlockEntityType.Builder.of(FreezerTraderBlockEntity::new, BlockEntityBlockHelper.getBlocksForBlockEntity(BlockEntityBlockHelper.FREEZER_TRADER_TYPE)).build(null));

    public static final RegistryObject<BlockEntityType<TicketTraderBlockEntity>> TICKET_TRADER = ModRegistries.BLOCK_ENTITIES.register("ticket_trader", () -> BlockEntityType.Builder.of(TicketTraderBlockEntity::new, ModBlocks.TICKET_KIOSK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BookTraderBlockEntity>> BOOK_TRADER = ModRegistries.BLOCK_ENTITIES.register("book_trader", () -> BlockEntityType.Builder.of(BookTraderBlockEntity::new, BlockEntityBlockHelper.getBlocksForBlockEntity(BlockEntityBlockHelper.BOOKSHELF_TRADER_TYPE)).build(null));

    public static final RegistryObject<BlockEntityType<SlotMachineTraderBlockEntity>> SLOT_MACHINE_TRADER = ModRegistries.BLOCK_ENTITIES.register("slot_machine_trader", () -> BlockEntityType.Builder.of(SlotMachineTraderBlockEntity::new, BlockEntityBlockHelper.getBlocksForBlockEntity(BlockEntityBlockHelper.SLOT_MACHINE_TRADER_TYPE)).build(null));

    public static final RegistryObject<BlockEntityType<CapabilityInterfaceBlockEntity>> CAPABILITY_INTERFACE = ModRegistries.BLOCK_ENTITIES.register("capability_interface", () -> BlockEntityType.Builder.of(CapabilityInterfaceBlockEntity::new, BlockEntityBlockHelper.getBlocksForBlockEntity(BlockEntityBlockHelper.CAPABILITY_INTERFACE_TYPE)).build(null));

    public static final RegistryObject<BlockEntityType<ItemTraderInterfaceBlockEntity>> TRADER_INTERFACE_ITEM = ModRegistries.BLOCK_ENTITIES.register("trader_interface_item", () -> BlockEntityType.Builder.of(ItemTraderInterfaceBlockEntity::new, ModBlocks.ITEM_TRADER_INTERFACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<CashRegisterBlockEntity>> CASH_REGISTER = ModRegistries.BLOCK_ENTITIES.register("cash_register", () -> BlockEntityType.Builder.of(CashRegisterBlockEntity::new, ModBlocks.CASH_REGISTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CoinMintBlockEntity>> COIN_MINT = ModRegistries.BLOCK_ENTITIES.register("coin_mint", () -> BlockEntityType.Builder.of(CoinMintBlockEntity::new, ModBlocks.COIN_MINT.get()).build(null));

    public static final RegistryObject<BlockEntityType<TicketStationBlockEntity>> TICKET_MACHINE = ModRegistries.BLOCK_ENTITIES.register("ticket_machine", () -> BlockEntityType.Builder.of(TicketStationBlockEntity::new, ModBlocks.TICKET_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<PaygateBlockEntity>> PAYGATE = ModRegistries.BLOCK_ENTITIES.register("paygate", () -> BlockEntityType.Builder.of(PaygateBlockEntity::new, ModBlocks.PAYGATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TaxBlockEntity>> TAX_BLOCK = ModRegistries.BLOCK_ENTITIES.register("tax_block", () -> BlockEntityType.Builder.of(TaxBlockEntity::new, ModBlocks.TAX_COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<CoinJarBlockEntity>> COIN_JAR = ModRegistries.BLOCK_ENTITIES.register("coin_jar", () -> BlockEntityType.Builder.of(CoinJarBlockEntity::new, ModBlocks.PIGGY_BANK.get(), ModBlocks.COINJAR_BLUE.get()).build(null));

    public static final RegistryObject<BlockEntityType<AuctionStandBlockEntity>> AUCTION_STAND = ModRegistries.BLOCK_ENTITIES.register("auction_stand", () -> BlockEntityType.Builder.of(AuctionStandBlockEntity::new, BlockEntityBlockHelper.getBlocksForBlockEntity(BlockEntityBlockHelper.AUCTION_STAND_TYPE)).build(null));

    public static final RegistryObject<BlockEntityType<CoinChestBlockEntity>> COIN_CHEST = ModRegistries.BLOCK_ENTITIES.register("coin_chest", () -> BlockEntityType.Builder.of(CoinChestBlockEntity::new, ModBlocks.COIN_CHEST.get()).build(null));

    public static void init() {
    }
}