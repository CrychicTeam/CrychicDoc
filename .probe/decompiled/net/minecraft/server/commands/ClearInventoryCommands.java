package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ClearInventoryCommands {

    private static final DynamicCommandExceptionType ERROR_SINGLE = new DynamicCommandExceptionType(p_136717_ -> Component.translatable("clear.failed.single", p_136717_));

    private static final DynamicCommandExceptionType ERROR_MULTIPLE = new DynamicCommandExceptionType(p_136711_ -> Component.translatable("clear.failed.multiple", p_136711_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("clear").requires(p_136704_ -> p_136704_.hasPermission(2))).executes(p_136721_ -> clearInventory((CommandSourceStack) p_136721_.getSource(), Collections.singleton(((CommandSourceStack) p_136721_.getSource()).getPlayerOrException()), p_180029_ -> true, -1))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(p_136719_ -> clearInventory((CommandSourceStack) p_136719_.getSource(), EntityArgument.getPlayers(p_136719_, "targets"), p_180027_ -> true, -1))).then(((RequiredArgumentBuilder) Commands.argument("item", ItemPredicateArgument.itemPredicate(commandBuildContext1)).executes(p_136715_ -> clearInventory((CommandSourceStack) p_136715_.getSource(), EntityArgument.getPlayers(p_136715_, "targets"), ItemPredicateArgument.getItemPredicate(p_136715_, "item"), -1))).then(Commands.argument("maxCount", IntegerArgumentType.integer(0)).executes(p_136702_ -> clearInventory((CommandSourceStack) p_136702_.getSource(), EntityArgument.getPlayers(p_136702_, "targets"), ItemPredicateArgument.getItemPredicate(p_136702_, "item"), IntegerArgumentType.getInteger(p_136702_, "maxCount")))))));
    }

    private static int clearInventory(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, Predicate<ItemStack> predicateItemStack2, int int3) throws CommandSyntaxException {
        int $$4 = 0;
        for (ServerPlayer $$5 : collectionServerPlayer1) {
            $$4 += $$5.m_150109_().clearOrCountMatchingItems(predicateItemStack2, int3, $$5.f_36095_.getCraftSlots());
            $$5.f_36096_.broadcastChanges();
            $$5.f_36095_.slotsChanged($$5.m_150109_());
        }
        if ($$4 == 0) {
            if (collectionServerPlayer1.size() == 1) {
                throw ERROR_SINGLE.create(((ServerPlayer) collectionServerPlayer1.iterator().next()).m_7755_());
            } else {
                throw ERROR_MULTIPLE.create(collectionServerPlayer1.size());
            }
        } else {
            int $$6 = $$4;
            if (int3 == 0) {
                if (collectionServerPlayer1.size() == 1) {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.clear.test.single", $$6, ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
                } else {
                    commandSourceStack0.sendSuccess(() -> Component.translatable("commands.clear.test.multiple", $$6, collectionServerPlayer1.size()), true);
                }
            } else if (collectionServerPlayer1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.clear.success.single", $$6, ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.clear.success.multiple", $$6, collectionServerPlayer1.size()), true);
            }
            return $$4;
        }
    }
}