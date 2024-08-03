package io.github.lightman314.lightmanscurrency;

import com.google.common.base.Suppliers;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.core.ModRegistries;
import io.github.lightman314.lightmanscurrency.common.core.groups.BundleRequestFilter;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBiBundle;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBundle;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import io.github.lightman314.lightmanscurrency.common.core.variants.WoodType;
import io.github.lightman314.lightmanscurrency.common.items.TicketItem;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(bus = Bus.MOD, modid = "lightmanscurrency")
public class ModCreativeGroups {

    public static final ResourceLocation COIN_GROUP_ID = new ResourceLocation("lightmanscurrency", "coins");

    public static final ResourceLocation MACHINE_GROUP_ID = new ResourceLocation("lightmanscurrency", "machines");

    public static final ResourceLocation TRADER_GROUP_ID = new ResourceLocation("lightmanscurrency", "traders");

    public static final ResourceLocation UPGRADE_GROUP_ID = new ResourceLocation("lightmanscurrency", "upgrades");

    public static final ResourceLocation EXTRA_GROUP_ID = new ResourceLocation("lightmanscurrency", "extra");

    public static final RegistryObject<CreativeModeTab> COIN_GROUP = ModRegistries.CREATIVE_TABS.register("coins", () -> CreativeModeTab.builder().title(LCText.CREATIVE_GROUP_COINS.get()).icon(ezIcon(ModBlocks.COINPILE_GOLD)).displayItems((parameters, p) -> {
        ezPop(p, ModItems.COIN_COPPER);
        ezPop(p, ModBlocks.COINPILE_COPPER);
        ezPop(p, ModBlocks.COINBLOCK_COPPER);
        ezPop(p, ModItems.COIN_IRON);
        ezPop(p, ModBlocks.COINPILE_IRON);
        ezPop(p, ModBlocks.COINBLOCK_IRON);
        ezPop(p, ModItems.COIN_GOLD);
        ezPop(p, ModBlocks.COINPILE_GOLD);
        ezPop(p, ModBlocks.COINBLOCK_GOLD);
        ezPop(p, ModItems.COIN_EMERALD);
        ezPop(p, ModBlocks.COINPILE_EMERALD);
        ezPop(p, ModBlocks.COINBLOCK_EMERALD);
        ezPop(p, ModItems.COIN_DIAMOND);
        ezPop(p, ModBlocks.COINPILE_DIAMOND);
        ezPop(p, ModBlocks.COINBLOCK_DIAMOND);
        ezPop(p, ModItems.COIN_NETHERITE);
        ezPop(p, ModBlocks.COINPILE_NETHERITE);
        ezPop(p, ModBlocks.COINBLOCK_NETHERITE);
        ezPop(p, ModItems.WALLET_COPPER);
        ezPop(p, ModItems.WALLET_IRON);
        ezPop(p, ModItems.WALLET_GOLD);
        ezPop(p, ModItems.WALLET_EMERALD);
        ezPop(p, ModItems.WALLET_DIAMOND);
        ezPop(p, ModItems.WALLET_NETHERITE);
        ezPop(p, ModItems.TRADING_CORE);
        ezPop(p, ModItems.COIN_CHOCOLATE_COPPER);
        ezPop(p, ModBlocks.COINPILE_CHOCOLATE_COPPER);
        ezPop(p, ModBlocks.COINBLOCK_CHOCOLATE_COPPER);
        ezPop(p, ModItems.COIN_CHOCOLATE_IRON);
        ezPop(p, ModBlocks.COINPILE_CHOCOLATE_IRON);
        ezPop(p, ModBlocks.COINBLOCK_CHOCOLATE_IRON);
        ezPop(p, ModItems.COIN_CHOCOLATE_GOLD);
        ezPop(p, ModBlocks.COINPILE_CHOCOLATE_GOLD);
        ezPop(p, ModBlocks.COINBLOCK_CHOCOLATE_GOLD);
        ezPop(p, ModItems.COIN_CHOCOLATE_EMERALD);
        ezPop(p, ModBlocks.COINPILE_CHOCOLATE_EMERALD);
        ezPop(p, ModBlocks.COINBLOCK_CHOCOLATE_EMERALD);
        ezPop(p, ModItems.COIN_CHOCOLATE_DIAMOND);
        ezPop(p, ModBlocks.COINPILE_CHOCOLATE_DIAMOND);
        ezPop(p, ModBlocks.COINBLOCK_CHOCOLATE_DIAMOND);
        ezPop(p, ModItems.COIN_CHOCOLATE_NETHERITE);
        ezPop(p, ModBlocks.COINPILE_CHOCOLATE_NETHERITE);
        ezPop(p, ModBlocks.COINBLOCK_CHOCOLATE_NETHERITE);
    }).build());

    public static final RegistryObject<CreativeModeTab> MACHINE_GROUP = ModRegistries.CREATIVE_TABS.register("machines", () -> CreativeModeTab.builder().withTabsBefore(new ResourceLocation[] { COIN_GROUP_ID }).title(LCText.CREATIVE_GROUP_MACHINES.get()).icon(ezIcon(ModBlocks.COIN_MINT)).displayItems((parameters, p) -> {
        ezPop(p, ModBlocks.COIN_MINT);
        ezPop(p, ModBlocks.ATM);
        ezPop(p, ModItems.PORTABLE_ATM);
        ezPop(p, ModBlocks.CASH_REGISTER);
        ezPop(p, ModBlocks.TERMINAL);
        ezPop(p, ModBlocks.GEM_TERMINAL);
        ezPop(p, ModItems.PORTABLE_TERMINAL);
        ezPop(p, ModItems.PORTABLE_GEM_TERMINAL);
        ezPop(p, ModBlocks.ITEM_TRADER_INTERFACE);
        ezPop(p, ModBlocks.TAX_COLLECTOR);
        ezPop(p, ModBlocks.AUCTION_STAND, BundleRequestFilter.VANILLA);
        ezPop(p, ModBlocks.TICKET_STATION);
        p.accept(TicketItem.CreateTicket(ModItems.TICKET_MASTER.get(), -1L));
        p.accept(TicketItem.CreateTicket(ModItems.TICKET_PASS.get(), -1L));
        p.accept(TicketItem.CreateTicket(ModItems.TICKET.get(), -1L));
        ezPop(p, ModItems.TICKET_STUB);
        p.accept(TicketItem.CreateTicket(ModItems.GOLDEN_TICKET_MASTER.get(), -2L));
        p.accept(TicketItem.CreateTicket(ModItems.GOLDEN_TICKET_PASS.get(), -2L));
        p.accept(TicketItem.CreateTicket(ModItems.GOLDEN_TICKET.get(), -2L));
        ezPop(p, ModItems.GOLDEN_TICKET_STUB);
        ezPop(p, ModBlocks.COIN_CHEST);
        ezPop(p, ModBlocks.PIGGY_BANK);
        ezPop(p, ModBlocks.COINJAR_BLUE);
    }).build());

    public static final RegistryObject<CreativeModeTab> TRADER_GROUP = ModRegistries.CREATIVE_TABS.register("traders", () -> CreativeModeTab.builder().withTabsBefore(new ResourceLocation[] { MACHINE_GROUP_ID }).title(LCText.CREATIVE_GROUP_TRADING.get()).icon(ezIcon(ModBlocks.DISPLAY_CASE)).displayItems((parameters, p) -> {
        ezPop(p, ModBlocks.DISPLAY_CASE);
        ezPop(p, ModBlocks.SHELF, BundleRequestFilter.VANILLA);
        ezPop(p, ModBlocks.SHELF_2x2, BundleRequestFilter.VANILLA);
        ezPop(p, ModBlocks.CARD_DISPLAY, BundleRequestFilter.VANILLA);
        ezPop(p, ModBlocks.VENDING_MACHINE);
        ezPop(p, ModBlocks.FREEZER);
        ezPop(p, ModBlocks.VENDING_MACHINE_LARGE);
        ezPop(p, ModBlocks.ARMOR_DISPLAY);
        ezPop(p, ModBlocks.TICKET_KIOSK);
        ezPop(p, ModBlocks.BOOKSHELF_TRADER, BundleRequestFilter.VANILLA);
        ezPop(p, ModBlocks.SLOT_MACHINE);
        ezPop(p, ModBlocks.ITEM_NETWORK_TRADER_1);
        ezPop(p, ModBlocks.ITEM_NETWORK_TRADER_2);
        ezPop(p, ModBlocks.ITEM_NETWORK_TRADER_3);
        ezPop(p, ModBlocks.ITEM_NETWORK_TRADER_4);
        ezPop(p, ModBlocks.PAYGATE);
    }).build());

    public static final RegistryObject<CreativeModeTab> UPGRADE_GROUP = ModRegistries.CREATIVE_TABS.register("upgrades", () -> CreativeModeTab.builder().withTabsBefore(new ResourceLocation[] { TRADER_GROUP_ID }).title(LCText.CREATIVE_GROUP_UPGRADES.get()).icon(ezIcon(ModItems.ITEM_CAPACITY_UPGRADE_1)).displayItems((parameters, p) -> {
        ezPop(p, ModItems.UPGRADE_SMITHING_TEMPLATE);
        ezPop(p, ModItems.ITEM_CAPACITY_UPGRADE_1);
        ezPop(p, ModItems.ITEM_CAPACITY_UPGRADE_2);
        ezPop(p, ModItems.ITEM_CAPACITY_UPGRADE_3);
        ezPop(p, ModItems.ITEM_CAPACITY_UPGRADE_4);
        ezPop(p, ModItems.SPEED_UPGRADE_1);
        ezPop(p, ModItems.SPEED_UPGRADE_2);
        ezPop(p, ModItems.SPEED_UPGRADE_3);
        ezPop(p, ModItems.SPEED_UPGRADE_4);
        ezPop(p, ModItems.SPEED_UPGRADE_5);
        ezPop(p, ModItems.NETWORK_UPGRADE);
        ezPop(p, ModItems.HOPPER_UPGRADE);
        ezPop(p, ModItems.COIN_CHEST_EXCHANGE_UPGRADE);
        ezPop(p, ModItems.COIN_CHEST_MAGNET_UPGRADE_1);
        ezPop(p, ModItems.COIN_CHEST_MAGNET_UPGRADE_2);
        ezPop(p, ModItems.COIN_CHEST_MAGNET_UPGRADE_3);
        ezPop(p, ModItems.COIN_CHEST_MAGNET_UPGRADE_4);
        ezPop(p, ModItems.COIN_CHEST_SECURITY_UPGRADE);
    }).build());

    @Nullable
    public static RegistryObject<CreativeModeTab> EXTRA_GROUP;

    public static void init() {
    }

    private static CreativeModeTab getExtraGroup() {
        return EXTRA_GROUP.get();
    }

    @SubscribeEvent
    public static void buildVanillaTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.PIGGY_BANK);
            event.accept(ModBlocks.COINJAR_BLUE);
        }
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.PAYGATE);
        }
        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
            event.m_246601_(convertToStack(ModBlocks.VENDING_MACHINE.getAllSorted()));
            event.m_246601_(convertToStack(ModBlocks.VENDING_MACHINE_LARGE.getAllSorted()));
            event.m_246601_(convertToStack(ModBlocks.FREEZER.getAllSorted()));
            event.m_246601_(convertToStack(ModBlocks.CARD_DISPLAY.getAllSorted()));
            if (ModBlocks.SUS_JAR.get().asItem() instanceof DyeableLeatherItem susItem) {
                for (Color c : Color.values()) {
                    ItemStack stack = new ItemStack(ModBlocks.SUS_JAR.get());
                    if (c != Color.WHITE) {
                        susItem.setColor(stack, c.hexColor);
                    }
                    event.m_246342_(stack);
                }
            }
        }
    }

    private static Supplier<ItemStack> ezIcon(RegistryObject<? extends ItemLike> item) {
        return Suppliers.memoize(() -> new ItemStack(item.get()));
    }

    private static Supplier<ItemStack> ezRandomIcon(Supplier<CreativeModeTab> tabSource) {
        return () -> {
            CreativeModeTab tab = (CreativeModeTab) tabSource.get();
            List<ItemStack> items = tab.getDisplayItems().stream().toList();
            return (ItemStack) items.get((int) (TimeUtil.getCurrentTime() / 1000L % (long) items.size()));
        };
    }

    public static void ezPop(CreativeModeTab.Output populator, RegistryObject<? extends ItemLike> item) {
        populator.accept(item.get());
    }

    public static void ezPop(CreativeModeTab.Output populator, RegistryObjectBundle<? extends ItemLike, ?> bundle) {
        bundle.getAllSorted().forEach(populator::m_246326_);
    }

    public static void ezPop(CreativeModeTab.Output populator, RegistryObjectBundle<? extends ItemLike, ?> bundle, BundleRequestFilter filter) {
        bundle.getAllSorted(filter).forEach(populator::m_246326_);
    }

    public static void ezPop(CreativeModeTab.Output populator, RegistryObjectBiBundle<? extends ItemLike, ?, ?> bundle) {
        bundle.getAllSorted().forEach(populator::m_246326_);
    }

    public static void ezPop(CreativeModeTab.Output populator, RegistryObjectBiBundle<? extends ItemLike, ?, ?> bundle, BundleRequestFilter filter) {
        bundle.getAllSorted(filter).forEach(populator::m_246326_);
    }

    private static Collection<ItemStack> convertToStack(Collection<? extends ItemLike> list) {
        List<ItemStack> result = new ArrayList();
        for (ItemLike item : list) {
            result.add(new ItemStack(item));
        }
        return result;
    }

    static {
        if (WoodType.hasModdedValues()) {
            EXTRA_GROUP = ModRegistries.CREATIVE_TABS.register("extra", () -> CreativeModeTab.builder().withTabsBefore(new ResourceLocation[] { TRADER_GROUP_ID }).withTabsAfter(new ResourceLocation[] { UPGRADE_GROUP_ID }).title(LCText.CREATIVE_GROUP_EXTRA.get()).icon(ezRandomIcon(ModCreativeGroups::getExtraGroup)).displayItems((parameters, p) -> {
                ezPop(p, ModBlocks.AUCTION_STAND, BundleRequestFilter.MODDED);
                ezPop(p, ModBlocks.SHELF, BundleRequestFilter.MODDED);
                ezPop(p, ModBlocks.SHELF_2x2, BundleRequestFilter.MODDED);
                ezPop(p, ModBlocks.CARD_DISPLAY, BundleRequestFilter.MODDED);
                ezPop(p, ModBlocks.BOOKSHELF_TRADER, BundleRequestFilter.MODDED);
            }).build());
        } else {
            EXTRA_GROUP = null;
        }
    }
}