package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.network.PacketDistributor;

public class ConfigCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("config").executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new SConfigureConfigPacket(SConfigureConfigPacket.Actions.configScreen.name(), ""));
            return 1;
        })).then(((RequiredArgumentBuilder) Commands.argument("path", StringArgumentType.string()).executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new SConfigureConfigPacket(SConfigureConfigPacket.Actions.configScreen.name(), StringArgumentType.getString(ctx, "path")));
            return 1;
        })).then(((LiteralArgumentBuilder) Commands.literal("set").requires(cs -> cs.hasPermission(2))).then(Commands.argument("value", StringArgumentType.string()).executes(ctx -> {
            String path = StringArgumentType.getString(ctx, "path");
            String value = StringArgumentType.getString(ctx, "value");
            ConfigHelper.ConfigPath configPath;
            try {
                configPath = ConfigHelper.ConfigPath.parse(path);
            } catch (IllegalArgumentException var7) {
                ((CommandSourceStack) ctx.getSource()).sendFailure(Components.literal(var7.getMessage()));
                return 0;
            }
            if (configPath.getType() == Type.CLIENT) {
                ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
                AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new SConfigureConfigPacket("SET" + path, value));
                return 1;
            } else {
                try {
                    ConfigHelper.setConfigValue(configPath, value);
                    ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Components.literal("Great Success!"), false);
                    return 1;
                } catch (ConfigHelper.InvalidValueException var5) {
                    ((CommandSourceStack) ctx.getSource()).sendFailure(Components.literal("Config could not be set the the specified value!"));
                    return 0;
                } catch (Exception var6) {
                    ((CommandSourceStack) ctx.getSource()).sendFailure(Components.literal("Something went wrong while trying to set config value. Check the server logs for more information"));
                    Create.LOGGER.warn("Exception during server-side config value set:", var6);
                    return 0;
                }
            }
        }))));
    }
}