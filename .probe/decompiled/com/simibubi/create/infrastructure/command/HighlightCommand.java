package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.foundation.utility.Components;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;

public class HighlightCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("highlight").then(((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("players", EntityArgument.players()).executes(ctx -> {
            Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
            BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
            for (ServerPlayer p : players) {
                AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> p), new HighlightPacket(pos));
            }
            return players.size();
        }))).executes(ctx -> {
            BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) ((CommandSourceStack) ctx.getSource()).getEntity()), new HighlightPacket(pos));
            return 1;
        }))).executes(ctx -> {
            ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
            return highlightAssemblyExceptionFor(player, (CommandSourceStack) ctx.getSource());
        });
    }

    private static void sendMissMessage(CommandSourceStack source) {
        source.sendSuccess(() -> Components.literal("Try looking at a Block that has failed to assemble a Contraption and try again."), true);
    }

    private static int highlightAssemblyExceptionFor(ServerPlayer player, CommandSourceStack source) {
        double distance = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue();
        Vec3 start = player.m_20299_(1.0F);
        Vec3 look = player.m_20252_(1.0F);
        Vec3 end = start.add(look.x * distance, look.y * distance, look.z * distance);
        Level world = player.m_9236_();
        BlockHitResult ray = world.m_45547_(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (ray.getType() == HitResult.Type.MISS) {
            sendMissMessage(source);
            return 0;
        } else {
            BlockPos pos = ray.getBlockPos();
            if (!(world.getBlockEntity(pos) instanceof IDisplayAssemblyExceptions display)) {
                sendMissMessage(source);
                return 0;
            } else {
                AssemblyException exception = display.getLastAssemblyException();
                if (exception == null) {
                    sendMissMessage(source);
                    return 0;
                } else if (!exception.hasPosition()) {
                    source.sendSuccess(() -> Components.literal("Can't highlight a specific position for this issue"), true);
                    return 1;
                } else {
                    BlockPos p = exception.getPosition();
                    String command = "/create highlight " + p.m_123341_() + " " + p.m_123342_() + " " + p.m_123343_();
                    return player.server.getCommands().performPrefixedCommand(source, command);
                }
            }
        }
    }
}