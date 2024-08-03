package snownee.loquat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.area.Area;
import snownee.loquat.util.LoquatUtil;

public class EmptyCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("empty").then(Commands.argument("areas", AreaArgument.areas()).executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            int count = 0;
            for (Area area : AreaArgument.getAreas(ctx, "areas")) {
                LoquatUtil.emptyBlocks(source.getLevel(), area::allBlockPosIn);
                count++;
            }
            return LoquatCommand.countedSuccess(ctx, "empty", count);
        }));
    }
}