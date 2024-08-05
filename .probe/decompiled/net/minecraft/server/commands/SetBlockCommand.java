package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class SetBlockCommand {

    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.setblock.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("setblock").requires(p_138606_ -> p_138606_.hasPermission(2))).then(Commands.argument("pos", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("block", BlockStateArgument.block(commandBuildContext1)).executes(p_138618_ -> setBlock((CommandSourceStack) p_138618_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138618_, "pos"), BlockStateArgument.getBlock(p_138618_, "block"), SetBlockCommand.Mode.REPLACE, null))).then(Commands.literal("destroy").executes(p_138616_ -> setBlock((CommandSourceStack) p_138616_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138616_, "pos"), BlockStateArgument.getBlock(p_138616_, "block"), SetBlockCommand.Mode.DESTROY, null)))).then(Commands.literal("keep").executes(p_138614_ -> setBlock((CommandSourceStack) p_138614_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138614_, "pos"), BlockStateArgument.getBlock(p_138614_, "block"), SetBlockCommand.Mode.REPLACE, p_180517_ -> p_180517_.getLevel().isEmptyBlock(p_180517_.getPos()))))).then(Commands.literal("replace").executes(p_138604_ -> setBlock((CommandSourceStack) p_138604_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138604_, "pos"), BlockStateArgument.getBlock(p_138604_, "block"), SetBlockCommand.Mode.REPLACE, null))))));
    }

    private static int setBlock(CommandSourceStack commandSourceStack0, BlockPos blockPos1, BlockInput blockInput2, SetBlockCommand.Mode setBlockCommandMode3, @Nullable Predicate<BlockInWorld> predicateBlockInWorld4) throws CommandSyntaxException {
        ServerLevel $$5 = commandSourceStack0.getLevel();
        if (predicateBlockInWorld4 != null && !predicateBlockInWorld4.test(new BlockInWorld($$5, blockPos1, true))) {
            throw ERROR_FAILED.create();
        } else {
            boolean $$6;
            if (setBlockCommandMode3 == SetBlockCommand.Mode.DESTROY) {
                $$5.m_46961_(blockPos1, true);
                $$6 = !blockInput2.getState().m_60795_() || !$$5.m_8055_(blockPos1).m_60795_();
            } else {
                BlockEntity $$7 = $$5.m_7702_(blockPos1);
                Clearable.tryClear($$7);
                $$6 = true;
            }
            if ($$6 && !blockInput2.place($$5, blockPos1, 2)) {
                throw ERROR_FAILED.create();
            } else {
                $$5.blockUpdated(blockPos1, blockInput2.getState().m_60734_());
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.setblock.success", blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_()), true);
                return 1;
            }
        }
    }

    public interface Filter {

        @Nullable
        BlockInput filter(BoundingBox var1, BlockPos var2, BlockInput var3, ServerLevel var4);
    }

    public static enum Mode {

        REPLACE, DESTROY
    }
}