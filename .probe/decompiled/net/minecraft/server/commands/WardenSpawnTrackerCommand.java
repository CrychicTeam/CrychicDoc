package net.minecraft.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
import net.minecraft.world.entity.player.Player;

public class WardenSpawnTrackerCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("warden_spawn_tracker").requires(p_214778_ -> p_214778_.hasPermission(2))).then(Commands.literal("clear").executes(p_214787_ -> resetTracker((CommandSourceStack) p_214787_.getSource(), ImmutableList.of(((CommandSourceStack) p_214787_.getSource()).getPlayerOrException()))))).then(Commands.literal("set").then(Commands.argument("warning_level", IntegerArgumentType.integer(0, 4)).executes(p_214776_ -> setWarningLevel((CommandSourceStack) p_214776_.getSource(), ImmutableList.of(((CommandSourceStack) p_214776_.getSource()).getPlayerOrException()), IntegerArgumentType.getInteger(p_214776_, "warning_level"))))));
    }

    private static int setWarningLevel(CommandSourceStack commandSourceStack0, Collection<? extends Player> collectionExtendsPlayer1, int int2) {
        for (Player $$3 : collectionExtendsPlayer1) {
            $$3.getWardenSpawnTracker().ifPresent(p_248188_ -> p_248188_.setWarningLevel(int2));
        }
        if (collectionExtendsPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.set.success.single", ((Player) collectionExtendsPlayer1.iterator().next()).getDisplayName()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.set.success.multiple", collectionExtendsPlayer1.size()), true);
        }
        return collectionExtendsPlayer1.size();
    }

    private static int resetTracker(CommandSourceStack commandSourceStack0, Collection<? extends Player> collectionExtendsPlayer1) {
        for (Player $$2 : collectionExtendsPlayer1) {
            $$2.getWardenSpawnTracker().ifPresent(WardenSpawnTracker::m_219593_);
        }
        if (collectionExtendsPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.clear.success.single", ((Player) collectionExtendsPlayer1.iterator().next()).getDisplayName()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.warden_spawn_tracker.clear.success.multiple", collectionExtendsPlayer1.size()), true);
        }
        return collectionExtendsPlayer1.size();
    }
}