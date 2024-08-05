package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class ClearRecastsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("clearRecasts").requires(p -> p.hasPermission(2))).then(Commands.literal("all").executes(context -> clearRecast((CommandSourceStack) context.getSource(), null)))).then(Commands.literal("player").then(Commands.argument("targets", EntityArgument.players()).executes(context -> clearRecast((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "targets"))))));
    }

    private static int clearRecast(CommandSourceStack source, @Nullable Collection<ServerPlayer> targets) {
        if (targets != null && !targets.isEmpty()) {
            targets.forEach(ClearRecastsCommand::removeRecastForPlayer);
            if (!targets.isEmpty()) {
                source.sendSuccess(() -> Component.translatable("commands.clearRecast.success"), true);
            }
            return targets.size();
        } else {
            source.getServer().getAllLevels().forEach(level -> level.getPlayers(player -> true).forEach(ClearRecastsCommand::removeRecastForPlayer));
            source.sendSuccess(() -> Component.translatable("commands.clearRecast.success"), true);
            return 1;
        }
    }

    private static void removeRecastForPlayer(ServerPlayer serverPlayer) {
        MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
        PlayerRecasts playerRecasts = magicData.getPlayerRecasts();
        playerRecasts.getAllRecasts().forEach(recastInstance -> playerRecasts.removeRecast(recastInstance, RecastResult.COMMAND));
    }
}