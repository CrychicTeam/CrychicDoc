package snownee.loquat.command;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.area.Area;
import snownee.loquat.program.PlaceProgram;

public class PlaceCommand {

    public static final SimpleCommandExceptionType PROGRAM_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("loquat.command.programNotFound"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("place").then(Commands.argument("areas", AreaArgument.areas()).then(Commands.argument("program", ResourceLocationArgument.id()).suggests(PlaceProgram.LOADER.suggestionProvider).executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            PlaceProgram program = getProgram(ctx);
            Collection<? extends Area> areas = AreaArgument.getAreas(ctx, "areas");
            int count = 0;
            for (Area area : areas) {
                if (program.place(source.getLevel(), area)) {
                    count++;
                }
            }
            return LoquatCommand.countedSuccess(ctx, "place", count);
        })));
    }

    public static PlaceProgram getProgram(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        try {
            PlaceProgram program = PlaceProgram.LOADER.get(ResourceLocationArgument.getId(ctx, "program"));
            Preconditions.checkNotNull(program);
            return program;
        } catch (Exception var2) {
            throw PROGRAM_NOT_FOUND.create();
        }
    }
}