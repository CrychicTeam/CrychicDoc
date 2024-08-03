package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.function.Predicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class LocateBlock {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.irons_spellbooks.create_spell_book.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher, CommandBuildContext pContext) {
        pDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("locateBlock").requires(p_138819_ -> p_138819_.hasPermission(2))).then(Commands.argument("block", BlockPredicateArgument.blockPredicate(pContext)).executes(commandContext -> locateBlock((CommandSourceStack) commandContext.getSource(), BlockPredicateArgument.getBlockPredicate(commandContext, "block")))));
    }

    private static int locateBlock(CommandSourceStack source, Predicate<BlockInWorld> predicate) throws CommandSyntaxException {
        BlockPos startingPosition = source.getPlayer().m_20183_();
        ServerLevel level = source.getLevel();
        int xFrom = startingPosition.m_123341_() - 200;
        int xTo = startingPosition.m_123341_() + 200;
        int yFrom = -64;
        int yTo = startingPosition.m_123342_();
        int zFrom = startingPosition.m_123343_() - 200;
        int zTo = startingPosition.m_123343_() + 200;
        IronsSpellbooks.LOGGER.debug("Starting locateBlock from: {}, xFrom:{} xTo:{} yFrom:{} yTo:{} zFrom:{} zTo:{}", new Object[] { startingPosition, xFrom, xTo, yFrom, yTo, zFrom, zTo });
        for (int i = yFrom; i < yTo; i++) {
            for (int j = xFrom; j < xTo; j++) {
                for (int k = zFrom; k < zTo; k++) {
                    BlockPos blockPos = new BlockPos(j, i, k);
                    if (predicate.test(new BlockInWorld(level, blockPos, true))) {
                        IronsSpellbooks.LOGGER.debug("Located x:{} y:{} z:{}", new Object[] { j, i, k });
                    }
                }
            }
        }
        IronsSpellbooks.LOGGER.debug("Finished locateBlock");
        return 1;
    }
}