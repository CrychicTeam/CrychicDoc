package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public abstract class ConfigureConfigCommand {

    protected final String commandLiteral;

    ConfigureConfigCommand(String commandLiteral) {
        this.commandLiteral = commandLiteral;
    }

    ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal(this.commandLiteral).requires(cs -> cs.hasPermission(0))).then(Commands.literal("on").executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            this.sendPacket(player, String.valueOf(true));
            return 1;
        }))).then(Commands.literal("off").executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            this.sendPacket(player, String.valueOf(false));
            return 1;
        }))).executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            this.sendPacket(player, "info");
            return 1;
        });
    }

    protected abstract void sendPacket(ServerPlayer var1, String var2);
}