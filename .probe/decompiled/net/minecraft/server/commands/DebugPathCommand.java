package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

public class DebugPathCommand {

    private static final SimpleCommandExceptionType ERROR_NOT_MOB = new SimpleCommandExceptionType(Component.literal("Source is not a mob"));

    private static final SimpleCommandExceptionType ERROR_NO_PATH = new SimpleCommandExceptionType(Component.literal("Path not found"));

    private static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.literal("Target not reached"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("debugpath").requires(p_180128_ -> p_180128_.hasPermission(2))).then(Commands.argument("to", BlockPosArgument.blockPos()).executes(p_180126_ -> fillBlocks((CommandSourceStack) p_180126_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180126_, "to")))));
    }

    private static int fillBlocks(CommandSourceStack commandSourceStack0, BlockPos blockPos1) throws CommandSyntaxException {
        if (!(commandSourceStack0.getEntity() instanceof Mob $$3)) {
            throw ERROR_NOT_MOB.create();
        } else {
            PathNavigation $$4 = new GroundPathNavigation($$3, commandSourceStack0.getLevel());
            Path $$5 = $$4.createPath(blockPos1, 0);
            DebugPackets.sendPathFindingPacket(commandSourceStack0.getLevel(), $$3, $$5, $$4.getMaxDistanceToWaypoint());
            if ($$5 == null) {
                throw ERROR_NO_PATH.create();
            } else if (!$$5.canReach()) {
                throw ERROR_NOT_COMPLETE.create();
            } else {
                commandSourceStack0.sendSuccess(() -> Component.literal("Made path"), true);
                return 1;
            }
        }
    }
}