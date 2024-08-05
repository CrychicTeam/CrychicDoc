package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.debugInfo.ServerDebugInfoPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class DebugInfoCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("debuginfo").executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            ServerPlayer player = source.getPlayerOrException();
            Lang.translate("command.debuginfo.sending").sendChat(player);
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new ServerDebugInfoPacket(player));
            return 1;
        });
    }
}