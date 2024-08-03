package net.mehvahdjukaar.supplementaries.common.commands.forge;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mehvahdjukaar.supplementaries.common.misc.songs.SongsManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.server.command.EnumArgument;

public class RecordSongCommandImpl {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("record").requires(cs -> cs.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("stop").executes(c -> stop(c, "", 0))).then(((RequiredArgumentBuilder) Commands.argument("name", StringArgumentType.word()).executes(c -> stop(c, StringArgumentType.getString(c, "name"), 0))).then(Commands.argument("speed_up_by", IntegerArgumentType.integer()).executes(c -> stop(c, StringArgumentType.getString(c, "name"), IntegerArgumentType.getInteger(c, "speed_up_by"))))))).then(Commands.literal("start").then(((RequiredArgumentBuilder) Commands.argument("source", EnumArgument.enumArgument(SongsManager.Source.class)).executes(x$0 -> start(x$0))).then(((RequiredArgumentBuilder) Commands.argument("instrument_0", EnumArgument.enumArgument(NoteBlockInstrument.class)).executes(c -> start(c, (NoteBlockInstrument) c.getArgument("instrument_0", NoteBlockInstrument.class)))).then(((RequiredArgumentBuilder) Commands.argument("instrument_1", EnumArgument.enumArgument(NoteBlockInstrument.class)).executes(c -> start(c, (NoteBlockInstrument) c.getArgument("instrument_0", NoteBlockInstrument.class), (NoteBlockInstrument) c.getArgument("instrument_1", NoteBlockInstrument.class)))).then(((RequiredArgumentBuilder) Commands.argument("instrument_2", EnumArgument.enumArgument(NoteBlockInstrument.class)).executes(c -> start(c, (NoteBlockInstrument) c.getArgument("instrument_0", NoteBlockInstrument.class), (NoteBlockInstrument) c.getArgument("instrument_1", NoteBlockInstrument.class), (NoteBlockInstrument) c.getArgument("instrument_2", NoteBlockInstrument.class)))).then(Commands.argument("instrument_3", EnumArgument.enumArgument(NoteBlockInstrument.class)).executes(c -> start(c, (NoteBlockInstrument) c.getArgument("instrument_0", NoteBlockInstrument.class), (NoteBlockInstrument) c.getArgument("instrument_1", NoteBlockInstrument.class), (NoteBlockInstrument) c.getArgument("instrument_2", NoteBlockInstrument.class), (NoteBlockInstrument) c.getArgument("instrument_3", NoteBlockInstrument.class)))))))));
    }

    public static int stop(CommandContext<CommandSourceStack> context, String name, int speedup) throws CommandSyntaxException {
        String savedName = SongsManager.stopRecording(((CommandSourceStack) context.getSource()).getLevel(), name, speedup);
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("message.supplementaries.command.record.stop", savedName), false);
        return 0;
    }

    public static int start(CommandContext<CommandSourceStack> context, NoteBlockInstrument... whitelist) throws CommandSyntaxException {
        SongsManager.Source source = (SongsManager.Source) context.getArgument("source", SongsManager.Source.class);
        SongsManager.startRecording(source, whitelist);
        ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("message.supplementaries.command.record.start"), false);
        return 0;
    }
}