package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class SetSpawnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("spawnpoint").requires(p_138648_ -> p_138648_.hasPermission(2))).executes(p_274828_ -> setSpawn((CommandSourceStack) p_274828_.getSource(), Collections.singleton(((CommandSourceStack) p_274828_.getSource()).getPlayerOrException()), BlockPos.containing(((CommandSourceStack) p_274828_.getSource()).getPosition()), 0.0F))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(p_274829_ -> setSpawn((CommandSourceStack) p_274829_.getSource(), EntityArgument.getPlayers(p_274829_, "targets"), BlockPos.containing(((CommandSourceStack) p_274829_.getSource()).getPosition()), 0.0F))).then(((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_138655_ -> setSpawn((CommandSourceStack) p_138655_.getSource(), EntityArgument.getPlayers(p_138655_, "targets"), BlockPosArgument.getSpawnablePos(p_138655_, "pos"), 0.0F))).then(Commands.argument("angle", AngleArgument.angle()).executes(p_138646_ -> setSpawn((CommandSourceStack) p_138646_.getSource(), EntityArgument.getPlayers(p_138646_, "targets"), BlockPosArgument.getSpawnablePos(p_138646_, "pos"), AngleArgument.getAngle(p_138646_, "angle")))))));
    }

    private static int setSpawn(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, BlockPos blockPos2, float float3) {
        ResourceKey<Level> $$4 = commandSourceStack0.getLevel().m_46472_();
        for (ServerPlayer $$5 : collectionServerPlayer1) {
            $$5.setRespawnPosition($$4, blockPos2, float3, true, false);
        }
        String $$6 = $$4.location().toString();
        if (collectionServerPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.single", blockPos2.m_123341_(), blockPos2.m_123342_(), blockPos2.m_123343_(), float3, $$6, ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.multiple", blockPos2.m_123341_(), blockPos2.m_123342_(), blockPos2.m_123343_(), float3, $$6, collectionServerPlayer1.size()), true);
        }
        return collectionServerPlayer1.size();
    }
}