package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.AllPackets;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class FabulousWarningCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("dismissFabulousWarning").requires(AllCommands.SOURCE_IS_PLAYER)).executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new SConfigureConfigPacket(SConfigureConfigPacket.Actions.fabulousWarning.name(), ""));
            return 1;
        });
    }
}