package snownee.loquat.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;

public class TagCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("tag").then(((RequiredArgumentBuilder) Commands.argument("areas", AreaArgument.areas()).then(Commands.literal("add").then(Commands.argument("tag", StringArgumentType.string()).executes(ctx -> forEachArea(ctx, (area, tag) -> area.getTags().add(tag)))))).then(Commands.literal("remove").then(Commands.argument("tag", StringArgumentType.string()).executes(ctx -> forEachArea(ctx, (area, tag) -> area.getTags().remove(tag))))));
    }

    private static int forEachArea(CommandContext<CommandSourceStack> ctx, BiPredicate<Area, String> func) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) ctx.getSource();
        String tag = StringArgumentType.getString(ctx, "tag");
        List<Area> areas = Lists.newArrayList();
        for (Area area : AreaArgument.getAreas(ctx, "areas")) {
            if (func.test(area, tag)) {
                areas.add(area);
            }
        }
        AreaManager manager = AreaManager.of(source.getLevel());
        manager.setChanged(areas);
        return LoquatCommand.countedSuccess(ctx, "tag", areas.size());
    }
}