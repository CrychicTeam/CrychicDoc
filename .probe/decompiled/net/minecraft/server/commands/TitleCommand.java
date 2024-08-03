package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;

public class TitleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("title").requires(p_139107_ -> p_139107_.hasPermission(2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).then(Commands.literal("clear").executes(p_139134_ -> clearTitle((CommandSourceStack) p_139134_.getSource(), EntityArgument.getPlayers(p_139134_, "targets"))))).then(Commands.literal("reset").executes(p_139132_ -> resetTitle((CommandSourceStack) p_139132_.getSource(), EntityArgument.getPlayers(p_139132_, "targets"))))).then(Commands.literal("title").then(Commands.argument("title", ComponentArgument.textComponent()).executes(p_139130_ -> showTitle((CommandSourceStack) p_139130_.getSource(), EntityArgument.getPlayers(p_139130_, "targets"), ComponentArgument.getComponent(p_139130_, "title"), "title", ClientboundSetTitleTextPacket::new))))).then(Commands.literal("subtitle").then(Commands.argument("title", ComponentArgument.textComponent()).executes(p_139128_ -> showTitle((CommandSourceStack) p_139128_.getSource(), EntityArgument.getPlayers(p_139128_, "targets"), ComponentArgument.getComponent(p_139128_, "title"), "subtitle", ClientboundSetSubtitleTextPacket::new))))).then(Commands.literal("actionbar").then(Commands.argument("title", ComponentArgument.textComponent()).executes(p_139123_ -> showTitle((CommandSourceStack) p_139123_.getSource(), EntityArgument.getPlayers(p_139123_, "targets"), ComponentArgument.getComponent(p_139123_, "title"), "actionbar", ClientboundSetActionBarTextPacket::new))))).then(Commands.literal("times").then(Commands.argument("fadeIn", TimeArgument.time()).then(Commands.argument("stay", TimeArgument.time()).then(Commands.argument("fadeOut", TimeArgument.time()).executes(p_139105_ -> setTimes((CommandSourceStack) p_139105_.getSource(), EntityArgument.getPlayers(p_139105_, "targets"), IntegerArgumentType.getInteger(p_139105_, "fadeIn"), IntegerArgumentType.getInteger(p_139105_, "stay"), IntegerArgumentType.getInteger(p_139105_, "fadeOut")))))))));
    }

    private static int clearTitle(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1) {
        ClientboundClearTitlesPacket $$2 = new ClientboundClearTitlesPacket(false);
        for (ServerPlayer $$3 : collectionServerPlayer1) {
            $$3.connection.send($$2);
        }
        if (collectionServerPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.cleared.single", ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.cleared.multiple", collectionServerPlayer1.size()), true);
        }
        return collectionServerPlayer1.size();
    }

    private static int resetTitle(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1) {
        ClientboundClearTitlesPacket $$2 = new ClientboundClearTitlesPacket(true);
        for (ServerPlayer $$3 : collectionServerPlayer1) {
            $$3.connection.send($$2);
        }
        if (collectionServerPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.reset.single", ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.reset.multiple", collectionServerPlayer1.size()), true);
        }
        return collectionServerPlayer1.size();
    }

    private static int showTitle(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, Component component2, String string3, Function<Component, Packet<?>> functionComponentPacket4) throws CommandSyntaxException {
        for (ServerPlayer $$5 : collectionServerPlayer1) {
            $$5.connection.send((Packet<?>) functionComponentPacket4.apply(ComponentUtils.updateForEntity(commandSourceStack0, component2, $$5, 0)));
        }
        if (collectionServerPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.show." + string3 + ".single", ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.show." + string3 + ".multiple", collectionServerPlayer1.size()), true);
        }
        return collectionServerPlayer1.size();
    }

    private static int setTimes(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, int int2, int int3, int int4) {
        ClientboundSetTitlesAnimationPacket $$5 = new ClientboundSetTitlesAnimationPacket(int2, int3, int4);
        for (ServerPlayer $$6 : collectionServerPlayer1) {
            $$6.connection.send($$5);
        }
        if (collectionServerPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.times.single", ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.title.times.multiple", collectionServerPlayer1.size()), true);
        }
        return collectionServerPlayer1.size();
    }
}