package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class FixLightingCommand {

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("fixLighting").requires(cs -> cs.hasPermission(0))).executes(ctx -> {
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) ((CommandSourceStack) ctx.getSource()).getEntity()), new SConfigureConfigPacket(SConfigureConfigPacket.Actions.fixLighting.name(), String.valueOf(true)));
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Components.literal("Forge's experimental block rendering pipeline is now enabled."), true);
            return 1;
        });
    }
}