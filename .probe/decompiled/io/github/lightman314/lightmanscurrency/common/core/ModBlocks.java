package io.github.lightman314.lightmanscurrency.common.core;

import com.google.common.base.Supplier;
import io.github.lightman314.lightmanscurrency.common.blocks.ATMBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.CashRegisterBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.CoinBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.CoinChestBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.CoinJarBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.CoinMintBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.CoinpileBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.PaygateBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.TaxCollectorBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.TerminalBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.TicketStationBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.tradeinterface.ItemTraderInterfaceBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.ArmorDisplayBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.BookTraderBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.CardDisplayBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.DisplayCaseBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.FreezerBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.NetworkItemTraderBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.ShelfBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.SlotMachineBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.TicketKioskBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.VendingMachineBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.VendingMachineLargeBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.reference.AuctionStandBlock;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBiBundle;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBundle;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import io.github.lightman314.lightmanscurrency.common.core.variants.WoodType;
import io.github.lightman314.lightmanscurrency.common.items.CashRegisterItem;
import io.github.lightman314.lightmanscurrency.common.items.CoinJarItem;
import io.github.lightman314.lightmanscurrency.common.items.CustomBlockModelItem;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    private static final SoundType CHOCOLATE_SOUND = SoundType.MUD_BRICKS;

    public static final RegistryObject<Block> COINPILE_COPPER = register("coinpile_copper", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_COPPER));

    public static final RegistryObject<Block> COINPILE_IRON = register("coinpile_iron", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_IRON));

    public static final RegistryObject<Block> COINPILE_GOLD = register("coinpile_gold", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_GOLD));

    public static final RegistryObject<Block> COINPILE_DIAMOND = register("coinpile_diamond", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_DIAMOND));

    public static final RegistryObject<Block> COINPILE_EMERALD = register("coinpile_emerald", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_EMERALD));

    public static final RegistryObject<Block> COINPILE_NETHERITE = register("coinpile_netherite", getCoinGenerator(true), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_NETHERITE));

    public static final RegistryObject<Block> COINPILE_CHOCOLATE_COPPER = register("coinpile_chocolate_copper", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).sound(SoundType.MUD), ModItems.COIN_CHOCOLATE_COPPER));

    public static final RegistryObject<Block> COINPILE_CHOCOLATE_IRON = register("coinpile_chocolate_iron", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_IRON));

    public static final RegistryObject<Block> COINPILE_CHOCOLATE_GOLD = register("coinpile_chocolate_gold", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_GOLD));

    public static final RegistryObject<Block> COINPILE_CHOCOLATE_DIAMOND = register("coinpile_chocolate_diamond", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_DIAMOND));

    public static final RegistryObject<Block> COINPILE_CHOCOLATE_EMERALD = register("coinpile_chocolate_emerald", getCoinGenerator(false), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_EMERALD));

    public static final RegistryObject<Block> COINPILE_CHOCOLATE_NETHERITE = register("coinpile_chocolate_netherite", getCoinGenerator(true), () -> new CoinpileBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_NETHERITE));

    public static final RegistryObject<Block> COINBLOCK_COPPER = register("coinblock_copper", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_COPPER));

    public static final RegistryObject<Block> COINBLOCK_IRON = register("coinblock_iron", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_IRON));

    public static final RegistryObject<Block> COINBLOCK_GOLD = register("coinblock_gold", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_GOLD));

    public static final RegistryObject<Block> COINBLOCK_EMERALD = register("coinblock_emerald", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_EMERALD));

    public static final RegistryObject<Block> COINBLOCK_DIAMOND = register("coinblock_diamond", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_DIAMOND));

    public static final RegistryObject<Block> COINBLOCK_NETHERITE = register("coinblock_netherite", getCoinGenerator(true), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(3.0F, 6.0F).sound(SoundType.METAL), ModItems.COIN_NETHERITE));

    public static final RegistryObject<Block> COINBLOCK_CHOCOLATE_COPPER = register("coinblock_chocolate_copper", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_COPPER));

    public static final RegistryObject<Block> COINBLOCK_CHOCOLATE_IRON = register("coinblock_chocolate_iron", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_IRON));

    public static final RegistryObject<Block> COINBLOCK_CHOCOLATE_GOLD = register("coinblock_chocolate_gold", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_GOLD));

    public static final RegistryObject<Block> COINBLOCK_CHOCOLATE_EMERALD = register("coinblock_chocolate_emerald", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_EMERALD));

    public static final RegistryObject<Block> COINBLOCK_CHOCOLATE_DIAMOND = register("coinblock_chocolate_diamond", getCoinGenerator(false), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_DIAMOND));

    public static final RegistryObject<Block> COINBLOCK_CHOCOLATE_NETHERITE = register("coinblock_chocolate_netherite", getCoinGenerator(true), () -> new CoinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(3.0F, 6.0F).sound(CHOCOLATE_SOUND), ModItems.COIN_CHOCOLATE_NETHERITE));

    public static final RegistryObject<Block> ATM = register("atm", () -> new ATMBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(3.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> COIN_MINT = register("coinmint", () -> new CoinMintBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).strength(2.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObject<Block> DISPLAY_CASE = register("display_case", () -> new DisplayCaseBlock(BlockBehaviour.Properties.of().strength(2.0F, Float.POSITIVE_INFINITY).sound(SoundType.GLASS)));

    public static final RegistryObjectBundle<Block, Color> VENDING_MACHINE = registerColored("vending_machine", c -> new VendingMachineBlock(BlockBehaviour.Properties.of().mapColor(c.mapColor).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)), Color.WHITE);

    public static final RegistryObjectBundle<Block, Color> VENDING_MACHINE_LARGE = registerColored("vending_machine_large", c -> new VendingMachineLargeBlock(BlockBehaviour.Properties.of().mapColor(c.mapColor).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)), Color.WHITE);

    public static final RegistryObjectBundle<Block, WoodType> SHELF = registerWooden("shelf", WoodType.Attributes.needsSlab, w -> new ShelfBlock(BlockBehaviour.Properties.of().mapColor(w.mapColor).strength(2.0F, Float.POSITIVE_INFINITY)));

    public static final RegistryObjectBundle<Block, WoodType> SHELF_2x2 = registerWooden("shelf_2x2", WoodType.Attributes.needsSlab, w -> new ShelfBlock(BlockBehaviour.Properties.of().mapColor(w.mapColor).strength(2.0F, Float.POSITIVE_INFINITY), 4));

    public static final RegistryObjectBiBundle<Block, WoodType, Color> CARD_DISPLAY = registerWoodenAndColored("card_display", WoodType.Attributes.needsLog, (w, c) -> new CardDisplayBlock(BlockBehaviour.Properties.of().mapColor(w.mapColor).strength(2.0F, Float.POSITIVE_INFINITY).sound(SoundType.WOOD), w.generateID("block.lightmanscurrency.card_display"), c), Color.RED);

    public static final RegistryObject<Block> ARMOR_DISPLAY = register("armor_display", () -> new ArmorDisplayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObjectBundle<FreezerBlock, Color> FREEZER = registerColored("freezer", c -> new FreezerBlock(BlockBehaviour.Properties.of().mapColor(c.mapColor).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL), FreezerBlock.GenerateDoorModel(c)), Color.BLACK);

    public static final RegistryObject<Block> ITEM_NETWORK_TRADER_1 = register("item_trader_server_sml", () -> new NetworkItemTraderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL), 4));

    public static final RegistryObject<Block> ITEM_NETWORK_TRADER_2 = register("item_trader_server_med", () -> new NetworkItemTraderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL), 8));

    public static final RegistryObject<Block> ITEM_NETWORK_TRADER_3 = register("item_trader_server_lrg", () -> new NetworkItemTraderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL), 12));

    public static final RegistryObject<Block> ITEM_NETWORK_TRADER_4 = register("item_trader_server_xlrg", () -> new NetworkItemTraderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL), 16));

    public static final RegistryObject<Block> ITEM_TRADER_INTERFACE = register("item_trader_interface", () -> new ItemTraderInterfaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObject<Block> CASH_REGISTER = register("cash_register", block -> new CashRegisterItem(block, new Item.Properties()), () -> new CashRegisterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(3.0F, 6.0F).sound(SoundType.METAL), Block.box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0)));

    public static final RegistryObject<Block> TERMINAL = register("terminal", () -> new TerminalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(SoundType.METAL), Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0)));

    public static final RegistryObject<Block> GEM_TERMINAL = register("gem_terminal", () -> new TerminalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(3.0F, 6.0F).sound(SoundType.AMETHYST_CLUSTER), Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0)));

    public static final RegistryObject<Block> PAYGATE = register("paygate", () -> new PaygateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObject<Block> TICKET_KIOSK = register("ticket_kiosk", () -> new TicketKioskBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObjectBundle<Block, WoodType> BOOKSHELF_TRADER = registerWooden("bookshelf_trader", WoodType.Attributes.needsPlanksAndSlab, w -> new BookTraderBlock(BlockBehaviour.Properties.of().mapColor(w.mapColor).strength(3.0F, Float.POSITIVE_INFINITY).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> SLOT_MACHINE = register("slot_machine", () -> new SlotMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(3.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObject<Block> TICKET_STATION = register("ticket_machine", () -> new TicketStationBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> COIN_CHEST = register("coin_chest", getCustomRendererGenerator(), () -> new CoinChestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5F, Float.POSITIVE_INFINITY).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> TAX_COLLECTOR = register("tax_block", () -> new TaxCollectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, Float.POSITIVE_INFINITY).sound(SoundType.METAL)));

    public static final RegistryObject<Block> PIGGY_BANK = register("piggy_bank", getCoinJarGenerator(), () -> new CoinJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).strength(0.1F, 2.0F).sound(SoundType.STONE), Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)));

    public static final RegistryObject<Block> COINJAR_BLUE = register("coinjar_blue", getCoinJarGenerator(), () -> new CoinJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(0.1F, 2.0F).sound(SoundType.STONE), Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)));

    public static final RegistryObject<Block> SUS_JAR = register("sus_jar", getColoredCoinJarGenerator(), () -> new CoinJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).strength(0.1F, 2.0F).sound(SoundType.STONE), Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0)));

    public static final RegistryObjectBundle<Block, WoodType> AUCTION_STAND = registerWooden("auction_stand", WoodType.Attributes.needsLog, w -> new AuctionStandBlock(BlockBehaviour.Properties.of().mapColor(w.mapColor).strength(2.0F)));

    public static void init() {
    }

    private static Function<Block, Item> getDefaultGenerator() {
        return block -> new BlockItem(block, new Item.Properties());
    }

    private static Function<Block, Item> getCoinGenerator(boolean fireResistant) {
        return block -> {
            Item.Properties properties = new Item.Properties();
            if (fireResistant) {
                properties.fireResistant();
            }
            return new BlockItem(block, properties);
        };
    }

    private static Function<Block, Item> getCoinJarGenerator() {
        return block -> new CoinJarItem(block, new Item.Properties());
    }

    private static Function<Block, Item> getColoredCoinJarGenerator() {
        return block -> new CoinJarItem.Colored(block, new Item.Properties());
    }

    private static Function<Block, Item> getCustomRendererGenerator() {
        return block -> new CustomBlockModelItem(block, new Item.Properties());
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> sup) {
        return register(name, getDefaultGenerator(), sup);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Function<Block, Item> itemGenerator, Supplier<T> sup) {
        RegistryObject<T> block = ModRegistries.BLOCKS.register(name, sup);
        if (block != null) {
            ModRegistries.ITEMS.register(name, () -> (Item) itemGenerator.apply(block.get()));
        }
        return block;
    }

    private static <T extends Block> RegistryObjectBundle<T, Color> registerColored(String name, Function<Color, T> block, @Nullable Color dontNameThisColor) {
        return registerColored(name, getDefaultGenerator(), block, dontNameThisColor);
    }

    private static <T extends Block> RegistryObjectBundle<T, Color> registerColored(String name, Function<Block, Item> itemGenerator, Function<Color, T> block, @Nullable Color dontNameThisColor) {
        RegistryObjectBundle<T, Color> bundle = new RegistryObjectBundle<>(Color::sortByColor);
        for (Color color : Color.values()) {
            String thisName = name;
            if (color != dontNameThisColor) {
                thisName = name + "_" + color.getResourceSafeName();
            }
            bundle.put(color, register(thisName, itemGenerator, () -> (Block) block.apply(color)));
        }
        return bundle.lock();
    }

    private static <T extends Block> RegistryObjectBundle<T, WoodType> registerWooden(String name, Predicate<WoodType.Attributes> check, Function<WoodType, T> block) {
        return registerWooden(name, check, getDefaultGenerator(), block);
    }

    private static <T extends Block> RegistryObjectBundle<T, WoodType> registerWooden(String name, Predicate<WoodType.Attributes> check, Function<Block, Item> itemGenerator, Function<WoodType, T> block) {
        RegistryObjectBundle<T, WoodType> bundle = new RegistryObjectBundle<>(WoodType::sortByWood);
        for (WoodType woodType : WoodType.validValues()) {
            if (check.test(woodType.attributes)) {
                String thisName = woodType.generateID(name);
                bundle.put(woodType, register(thisName, itemGenerator, () -> (Block) block.apply(woodType)));
            }
        }
        return bundle.lock();
    }

    private static <T extends Block> RegistryObjectBiBundle<T, WoodType, Color> registerWoodenAndColored(String name, Predicate<WoodType.Attributes> check, BiFunction<WoodType, Color, T> block) {
        return registerWoodenAndColored(name, check, block, null);
    }

    private static <T extends Block> RegistryObjectBiBundle<T, WoodType, Color> registerWoodenAndColored(String name, Predicate<WoodType.Attributes> check, BiFunction<WoodType, Color, T> block, @Nullable Color ignoreColor) {
        return registerWoodenAndColored(name, check, getDefaultGenerator(), block, ignoreColor);
    }

    private static <T extends Block> RegistryObjectBiBundle<T, WoodType, Color> registerWoodenAndColored(String name, Predicate<WoodType.Attributes> check, Function<Block, Item> itemGenerator, BiFunction<WoodType, Color, T> block) {
        return registerWoodenAndColored(name, check, itemGenerator, block, null);
    }

    private static <T extends Block> RegistryObjectBiBundle<T, WoodType, Color> registerWoodenAndColored(String name, Predicate<WoodType.Attributes> check, Function<Block, Item> itemGenerator, BiFunction<WoodType, Color, T> block, @Nullable Color ignoreColor) {
        RegistryObjectBiBundle<T, WoodType, Color> bundle = new RegistryObjectBiBundle<>(WoodType::sortByWood, Color::sortByColor);
        for (WoodType woodType : WoodType.validValues()) {
            if (check.test(woodType.attributes)) {
                for (Color color : Color.values()) {
                    String thisName;
                    if (color == ignoreColor) {
                        thisName = woodType.generateID(name);
                    } else {
                        thisName = woodType.generateID(name) + "_" + color.getResourceSafeName();
                    }
                    bundle.put(woodType, color, register(thisName, itemGenerator, () -> (Block) block.apply(woodType, color)));
                }
            }
        }
        return bundle.lock();
    }
}