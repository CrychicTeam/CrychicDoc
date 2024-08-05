package snownee.loquat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;

public class DeleteCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("delete").then(Commands.argument("areas", AreaArgument.areas()).executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            int count = 0;
            AreaManager areaManager = AreaManager.of(source.getLevel());
            for (Area area : AreaArgument.getAreas(ctx, "areas")) {
                areaManager.remove(area.getUuid());
                count++;
            }
            return LoquatCommand.countedSuccess(ctx, "delete", count);
        }));
    }
}