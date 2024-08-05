package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class ClearCooldownCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("clearCooldowns").requires(p -> p.hasPermission(2))).executes(context -> clearCooldowns((CommandSourceStack) context.getSource(), null))).then(Commands.literal("all").executes(context -> clearCooldowns((CommandSourceStack) context.getSource(), null)))).then(Commands.literal("player").then(Commands.argument("targets", EntityArgument.players()).executes(context -> clearCooldowns((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "targets"))))));
    }

    private static int clearCooldowns(CommandSourceStack source, @Nullable Collection<ServerPlayer> targets) {
        if (targets != null && !targets.isEmpty()) {
            targets.forEach(serverPlayer -> {
                MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
                magicData.getPlayerCooldowns().clearCooldowns();
                magicData.getPlayerCooldowns().syncToPlayer(serverPlayer);
            });
            if (!targets.isEmpty()) {
                source.sendSuccess(() -> Component.translatable("commands.clearCooldown.success"), true);
            }
            return targets.size();
        } else {
            source.getServer().getAllLevels().forEach(level -> level.getPlayers(player -> true).forEach(serverPlayer -> {
                MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
                magicData.getPlayerCooldowns().clearCooldowns();
                magicData.getPlayerCooldowns().syncToPlayer(serverPlayer);
            }));
            source.sendSuccess(() -> Component.translatable("commands.clearCooldown.success"), true);
            return 1;
        }
    }
}