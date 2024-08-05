package snownee.loquat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.network.SSyncSelectionPacket;

public class SelectCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("select").requires(CommandSourceStack::m_230897_)).then(Commands.argument("areas", AreaArgument.areas()).executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            ServerPlayer player = source.getPlayerOrException();
            List<UUID> selectedAreas = SelectionManager.of(player).getSelectedAreas();
            int count = 0;
            for (Area area : AreaArgument.getAreas(ctx, "areas")) {
                if (!selectedAreas.contains(area.getUuid())) {
                    selectedAreas.add(area.getUuid());
                    count++;
                }
            }
            SSyncSelectionPacket.sync(player);
            return LoquatCommand.countedSuccess(ctx, "select", count);
        }));
    }
}