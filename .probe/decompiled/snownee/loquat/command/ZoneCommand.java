package snownee.loquat.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import org.apache.commons.compress.utils.Lists;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.area.Zone;
import snownee.loquat.core.select.PosSelection;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.network.SSyncSelectionPacket;

public class ZoneCommand {

    public static final SimpleCommandExceptionType INVALID_ZONE_NAME = new SimpleCommandExceptionType(Component.translatable("loquat.command.invalidZoneName"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("zone").then(((RequiredArgumentBuilder) Commands.argument("area", AreaArgument.area()).then(((LiteralArgumentBuilder) Commands.literal("add").executes(ctx -> addZone(ctx, "0"))).then(Commands.argument("name", StringArgumentType.string()).suggests(LoquatCommand.SUGGEST_ADD_ZONE).executes(ctx -> addZone(ctx, StringArgumentType.getString(ctx, "name")))))).then(Commands.literal("remove").then(Commands.argument("name", StringArgumentType.string()).suggests(LoquatCommand.SUGGEST_REMOVE_ZONE).executes(ctx -> removeZone(ctx, StringArgumentType.getString(ctx, "name"))))));
    }

    private static int removeZone(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException {
        Area area = AreaArgument.getArea(ctx, "area");
        Zone removed = (Zone) area.getZones().remove(name);
        CommandSourceStack source = (CommandSourceStack) ctx.getSource();
        if (removed == null) {
            source.sendFailure(Component.translatable("loquat.command.zoneNotFound"));
            return 0;
        } else {
            AreaManager manager = AreaManager.of(source.getLevel());
            manager.setChanged(List.of(area));
            source.sendSuccess(() -> Component.translatable("loquat.command.zone.remove.success"), true);
            return 1;
        }
    }

    private static int addZone(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException {
        if (!name.contains(",") && !name.contains(" ")) {
            Area area = AreaArgument.getArea(ctx, "area");
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            List<PosSelection> selections = SelectionManager.of(source.getPlayerOrException()).getSelections();
            if (selections.isEmpty()) {
                throw LoquatCommand.EMPTY_SELECTION.create();
            } else {
                List<AABB> aabbs = Lists.newArrayList();
                Optional.ofNullable((Zone) area.getZones().get(name)).ifPresent($ -> aabbs.addAll($.aabbs()));
                aabbs.addAll(selections.stream().map(PosSelection::toAABB).map($ -> $.getYsize() > 1.0 ? $ : $.move(0.0, 1.0, 0.0)).toList());
                area.getZones().put(name, new Zone(aabbs));
                AreaManager manager = AreaManager.of(source.getLevel());
                manager.setChanged(List.of(area));
                selections.clear();
                SSyncSelectionPacket.sync(source.getPlayerOrException(), name);
                source.sendSuccess(() -> Component.translatable("loquat.command.zone.add.success"), true);
                return 1;
            }
        } else {
            throw INVALID_ZONE_NAME.create();
        }
    }
}