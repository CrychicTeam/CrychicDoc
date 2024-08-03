package io.github.lightman314.lightmanscurrency.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.ITraderBlock;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.CapabilityEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.capability.event_unlocks.IEventUnlocks;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.commands.arguments.TraderArgument;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.SimpleValidator;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.rules.TradeRule;
import io.github.lightman314.lightmanscurrency.common.traders.rules.types.PlayerListing;
import io.github.lightman314.lightmanscurrency.network.message.command.SPacketDebugTrader;
import java.util.Collection;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

public class CommandLCAdmin {

    private static final SimpleCommandExceptionType ERROR_BLOCK_NOT_FOUND = new SimpleCommandExceptionType(LCText.COMMAND_ADMIN_PREPARE_FOR_STRUCTURE_ERROR.get());

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> lcAdminCommand = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("lcadmin").requires(stack -> stack.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("toggleadmin").requires(CommandSourceStack::m_230897_)).executes(CommandLCAdmin::toggleAdmin))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("traderdata").then(Commands.literal("list").executes(CommandLCAdmin::listTraderData))).then(Commands.literal("search").then(Commands.argument("searchText", StringArgumentType.greedyString()).executes(CommandLCAdmin::searchTraderData)))).then(Commands.literal("delete").then(Commands.argument("traderID", TraderArgument.trader()).executes(CommandLCAdmin::deleteTraderData)))).then(Commands.literal("debug").then(Commands.argument("traderID", TraderArgument.trader()).executes(CommandLCAdmin::debugTraderData)))).then(Commands.literal("addToWhitelist").then(Commands.argument("traderID", TraderArgument.traderWithPersistent()).then(Commands.argument("player", EntityArgument.players()).executes(CommandLCAdmin::addToTraderWhitelist)))))).then(Commands.literal("prepareForStructure").then(Commands.argument("traderPos", BlockPosArgument.blockPos()).executes(CommandLCAdmin::setCustomTrader)))).then(((LiteralArgumentBuilder) Commands.literal("replaceWallet").requires(c -> !LightmansCurrency.isCuriosLoaded())).then(Commands.argument("entity", EntityArgument.entities()).then(((RequiredArgumentBuilder) Commands.argument("wallet", ItemArgument.item(context)).executes(CommandLCAdmin::replaceWalletSlotWithDefault)).then(Commands.argument("keepWalletContents", BoolArgumentType.bool()).executes(CommandLCAdmin::replaceWalletSlot)))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("taxes").then(Commands.literal("list").executes(CommandLCAdmin::listTaxCollectors))).then(Commands.literal("delete").then(Commands.argument("taxCollectorID", LongArgumentType.longArg(0L)).executes(CommandLCAdmin::deleteTaxCollector)))).then(((LiteralArgumentBuilder) Commands.literal("openServerTax").requires(CommandSourceStack::m_230897_)).executes(CommandLCAdmin::openServerTax))).then(Commands.literal("forceDisableTaxCollectors").executes(CommandLCAdmin::forceDisableTaxCollectors)))).then(Commands.literal("events").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("player", EntityArgument.player()).then(Commands.literal("reward").then(Commands.argument("item", ItemArgument.item(context)).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes(CommandLCAdmin::giveEventReward))))).then(Commands.literal("list").executes(CommandLCAdmin::listUnlockedEvents))).then(Commands.literal("unlock").then(Commands.argument("event", StringArgumentType.word()).executes(CommandLCAdmin::unlockEvent)))).then(Commands.literal("lock").then(Commands.argument("event", StringArgumentType.word()).executes(CommandLCAdmin::lockEvent)))));
        dispatcher.register(lcAdminCommand);
    }

    static int toggleAdmin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        ServerPlayer sourcePlayer = source.getPlayerOrException();
        LCAdminMode.ToggleAdminPlayer(sourcePlayer);
        Component enabledDisabled = LCAdminMode.isAdminPlayer(sourcePlayer) ? LCText.COMMAND_ADMIN_TOGGLE_ADMIN_ENABLED.getWithStyle(ChatFormatting.GREEN) : LCText.COMMAND_ADMIN_TOGGLE_ADMIN_DISABLED.getWithStyle(ChatFormatting.RED);
        EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TOGGLE_ADMIN.get(enabledDisabled), true);
        return 1;
    }

    static int setCustomTrader(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(commandContext, "traderPos");
        Level level = source.getLevel();
        BlockState state = level.getBlockState(pos);
        BlockEntity be;
        if (state.m_60734_() instanceof ITraderBlock) {
            be = ((ITraderBlock) state.m_60734_()).getBlockEntity(state, level, pos);
        } else {
            be = level.getBlockEntity(pos);
        }
        if (be instanceof TraderBlockEntity<?> t) {
            t.saveCurrentTraderAsCustomTrader();
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_PREPARE_FOR_STRUCTURE_SUCCESS.get(), true);
            return 1;
        } else {
            throw ERROR_BLOCK_NOT_FOUND.create();
        }
    }

    static int listTraderData(CommandContext<CommandSourceStack> commandContext) {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        List<TraderData> allTraders = TraderSaveData.GetAllTraders(false);
        if (!allTraders.isEmpty()) {
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_TITLE.get(), true);
            for (int i = 0; i < allTraders.size(); i++) {
                TraderData thisTrader = (TraderData) allTraders.get(i);
                if (i > 0) {
                    EasyText.sendCommandSucess(source, EasyText.empty(), true);
                }
                sendTraderDataFeedback(thisTrader, source);
            }
        } else {
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_NONE.get(), true);
        }
        return 1;
    }

    static int searchTraderData(CommandContext<CommandSourceStack> commandContext) {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        String searchText = StringArgumentType.getString(commandContext, "searchText");
        List<TraderData> results = TraderSaveData.GetAllTraders(false).stream().filter(trader -> TraderAPI.filterTrader(trader, searchText)).toList();
        if (!results.isEmpty()) {
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_TITLE.get(), true);
            for (int i = 0; i < results.size(); i++) {
                TraderData thisTrader = (TraderData) results.get(i);
                if (i > 0) {
                    EasyText.sendCommandSucess(source, EasyText.empty(), true);
                }
                sendTraderDataFeedback(thisTrader, source);
            }
        } else {
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_SEARCH_NONE.get(), true);
        }
        return 1;
    }

    private static void sendTraderDataFeedback(TraderData thisTrader, CommandSourceStack source) {
        String traderID = String.valueOf(thisTrader.getID());
        EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_TRADER_ID.get(EasyText.literal(traderID).withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, traderID)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, LCText.COMMAND_ADMIN_TRADERDATA_LIST_TRADER_ID_TOOLTIP.get())))), false);
        if (thisTrader.isPersistent()) {
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_PERSISTENT_ID.get(thisTrader.getPersistentID()), false);
        }
        EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_TYPE.get(thisTrader.type), false);
        if (!(thisTrader instanceof AuctionHouseTrader)) {
            EasyText.sendCommandSucess(source, thisTrader.getOwner().getValidOwner().getCommandLabel(), false);
            if (!thisTrader.isPersistent()) {
                String dimension = thisTrader.getLevel().location().toString();
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_DIMENSION.get(dimension), false);
                BlockPos pos = thisTrader.getPos();
                String position = pos.m_123341_() + " " + pos.m_123342_() + " " + pos.m_123343_();
                String teleportPosition = pos.m_123341_() + " " + (pos.m_123342_() + 1) + " " + pos.m_123343_();
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_POSITION.get(EasyText.literal(position).withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/execute in " + dimension + " run tp @s " + teleportPosition)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, LCText.COMMAND_ADMIN_TRADERDATA_LIST_POSITION_TOOLTIP.get())))), true);
            }
            if (thisTrader.hasCustomName()) {
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_LIST_NAME.get(thisTrader.getName()), true);
            }
        }
    }

    static int deleteTraderData(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        TraderData trader = TraderArgument.getTrader(commandContext, "traderID");
        TraderSaveData.DeleteTrader(trader.getID());
        EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_DELETE_SUCCESS.get(trader.getName()), true);
        return 1;
    }

    static int debugTraderData(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        TraderData trader = TraderArgument.getTrader(commandContext, "traderID");
        EasyText.sendCommandSucess(source, EasyText.literal(trader.save().m_7916_()), false);
        if (((CommandSourceStack) commandContext.getSource()).isPlayer()) {
            new SPacketDebugTrader(trader.getID()).sendTo(((CommandSourceStack) commandContext.getSource()).getPlayerOrException());
        }
        return 1;
    }

    static int addToTraderWhitelist(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        TraderData trader = TraderArgument.getTrader(commandContext, "traderID");
        if (TradeRule.getRule(PlayerListing.TYPE.type, trader.getRules()) instanceof PlayerListing whitelist) {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(commandContext, "player");
            int count = 0;
            for (ServerPlayer player : players) {
                if (whitelist.addToWhitelist(player)) {
                    count++;
                }
            }
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TRADERDATA_ADD_TO_WHITELIST_SUCCESS.get(count, trader.getName()), true);
            if (count > 0) {
                trader.markTradeRulesDirty();
            }
            return count;
        } else {
            EasyText.sendCommandFail(source, LCText.COMMAND_ADMIN_TRADERDATA_ADD_TO_WHITELIST_MISSING.get());
            return 0;
        }
    }

    static int replaceWalletSlotWithDefault(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return replaceWalletSlotInternal(commandContext, true);
    }

    static int replaceWalletSlot(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return replaceWalletSlotInternal(commandContext, BoolArgumentType.getBool(commandContext, "keepWalletContents"));
    }

    static int replaceWalletSlotInternal(CommandContext<CommandSourceStack> commandContext, boolean keepWalletContents) throws CommandSyntaxException {
        int count = 0;
        ItemInput input = ItemArgument.getItem(commandContext, "wallet");
        if (!(input.getItem() instanceof WalletItem) && input.getItem() != Items.AIR) {
            throw new CommandSyntaxException(new CommandExceptionType() {
            }, LCText.COMMAND_ADMIN_REPLACE_WALLET_NOT_A_WALLET.get());
        } else {
            for (Entity entity : EntityArgument.getEntities(commandContext, "entity")) {
                IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(entity);
                if (walletHandler != null) {
                    ItemStack newWallet = input.createItemStack(1, true);
                    if (!newWallet.isEmpty() || !walletHandler.getWallet().isEmpty()) {
                        if (keepWalletContents) {
                            ItemStack oldWallet = walletHandler.getWallet();
                            if (WalletItem.isWallet(oldWallet)) {
                                newWallet.setTag(oldWallet.getOrCreateTag().copy());
                            }
                        }
                        walletHandler.setWallet(newWallet);
                    }
                }
            }
            return count;
        }
    }

    static int openServerTax(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        TaxEntry entry = TaxSaveData.GetServerTaxEntry(false);
        if (entry != null) {
            entry.openMenu(((CommandSourceStack) commandContext.getSource()).getPlayerOrException(), SimpleValidator.NULL);
            return 1;
        } else {
            EasyText.sendCommandFail((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_TAXES_OPEN_SERVER_TAX_ERROR.get());
            return 0;
        }
    }

    static int listTaxCollectors(CommandContext<CommandSourceStack> commandContext) {
        CommandSourceStack source = (CommandSourceStack) commandContext.getSource();
        EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_TITLE.get(), false);
        for (TaxEntry entry : TaxSaveData.GetAllTaxEntries(false)) {
            sendTaxDataFeedback(entry, source);
        }
        return 1;
    }

    private static void sendTaxDataFeedback(TaxEntry taxEntry, CommandSourceStack source) {
        if (!taxEntry.isServerEntry()) {
            String id = String.valueOf(taxEntry.getID());
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_ID.get(EasyText.literal(id).withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, LCText.COMMAND_ADMIN_TAXES_LIST_ID_TOOLTIP.get())))), false);
            if (!taxEntry.isServerEntry()) {
                EasyText.sendCommandSucess(source, taxEntry.getOwner().getValidOwner().getCommandLabel(), false);
            }
            String dimension = taxEntry.getArea().getCenter().getDimension().location().toString();
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_DIMENSION.get(dimension), false);
            BlockPos pos = taxEntry.getArea().getCenter().getPos();
            String position = pos.m_123341_() + " " + pos.m_123342_() + " " + pos.m_123343_();
            String teleportPosition = pos.m_123341_() + " " + (pos.m_123342_() + 1) + " " + pos.m_123343_();
            EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_POSITION.get(EasyText.literal(position).withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/execute in " + dimension + " run tp @s " + teleportPosition)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, LCText.COMMAND_ADMIN_TAXES_LIST_POSITION_TOOLTIP.get())))), true);
            if (taxEntry.isInfiniteRange()) {
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_INFINITE_RANGE.get(), false);
            } else {
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_RADIUS.get(taxEntry.getRadius()), false);
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_HEIGHT.get(taxEntry.getHeight()), false);
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_OFFSET.get(taxEntry.getVertOffset()), false);
            }
            if (taxEntry.forcesAcceptance()) {
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_FORCE_ACCEPTANCE.get(), false);
            }
            if (taxEntry.hasCustomName()) {
                EasyText.sendCommandSucess(source, LCText.COMMAND_ADMIN_TAXES_LIST_NAME.get(taxEntry.getName()), true);
            }
        }
    }

    static int deleteTaxCollector(CommandContext<CommandSourceStack> commandContext) {
        long taxCollectorID = LongArgumentType.getLong(commandContext, "taxCollectorID");
        TaxEntry entry = TaxSaveData.GetTaxEntry(taxCollectorID, false);
        if (entry != null && !entry.isServerEntry()) {
            TaxSaveData.RemoveEntry(taxCollectorID);
            EasyText.sendCommandSucess((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_TAXES_DELETE_SUCCESS.get(entry.getName()), true);
            return 1;
        } else {
            EasyText.sendCommandFail((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_TAXES_DELETE_FAIL.get());
            return 0;
        }
    }

    static int forceDisableTaxCollectors(CommandContext<CommandSourceStack> commandContext) {
        int count = 0;
        for (TaxEntry entry : TaxSaveData.GetAllTaxEntries(false)) {
            if (entry.isActive()) {
                entry.setActive(false, null);
                count++;
            }
        }
        if (count > 0) {
            EasyText.sendCommandSucess((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_TAXES_FORCE_DISABLE_SUCCESS.get(count), true);
        } else {
            EasyText.sendCommandFail((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_TAXES_FORCE_DISABLE_FAIL.get());
        }
        return count;
    }

    static int giveEventReward(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        if (!LCConfig.COMMON.eventAdvancementRewards.get()) {
            return 0;
        } else {
            int success = 0;
            int count = IntegerArgumentType.getInteger(commandContext, "count");
            ItemStack reward = ItemArgument.getItem(commandContext, "item").createItemStack(count, false);
            for (ServerPlayer player : EntityArgument.getPlayers(commandContext, "player")) {
                ItemHandlerHelper.giveItemToPlayer(player, reward.copy());
                success++;
            }
            return success;
        }
    }

    static int listUnlockedEvents(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(commandContext, "player");
        IEventUnlocks eventUnlocks = CapabilityEventUnlocks.getCapability(player);
        if (eventUnlocks == null) {
            return 0;
        } else {
            List<String> unlocks = eventUnlocks.getUnlockedList();
            if (!unlocks.isEmpty()) {
                StringBuilder list = new StringBuilder();
                for (String v : eventUnlocks.getUnlockedList()) {
                    if (!list.isEmpty()) {
                        list.append(", ");
                    }
                    list.append(v);
                }
                EasyText.sendCommandSucess((CommandSourceStack) commandContext.getSource(), EasyText.literal(list.toString()), false);
            } else {
                EasyText.sendCommandSucess((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_EVENT_LIST_NONE.get(), false);
            }
            return 1;
        }
    }

    static int unlockEvent(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(commandContext, "player");
        String event = StringArgumentType.getString(commandContext, "event");
        if (CapabilityEventUnlocks.isUnlocked(player, event)) {
            EasyText.sendCommandFail((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_EVENT_UNLOCK_FAIL.get(event));
            return 0;
        } else {
            CapabilityEventUnlocks.unlock(player, event);
            EasyText.sendCommandSucess((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_EVENT_UNLOCK_SUCCESS.get(event), false);
            return 1;
        }
    }

    static int lockEvent(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(commandContext, "player");
        String event = StringArgumentType.getString(commandContext, "event");
        if (!CapabilityEventUnlocks.isUnlocked(player, event)) {
            EasyText.sendCommandFail((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_EVENT_LOCK_FAIL.get(event));
            return 0;
        } else {
            CapabilityEventUnlocks.lock(player, event);
            EasyText.sendCommandSucess((CommandSourceStack) commandContext.getSource(), LCText.COMMAND_ADMIN_EVENT_LOCK_SUCCESS.get(event), false);
            return 1;
        }
    }
}