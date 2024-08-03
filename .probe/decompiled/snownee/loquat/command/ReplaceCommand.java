package snownee.loquat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import snownee.loquat.AreaTypes;
import snownee.loquat.command.argument.AreaArgument;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.PosSelection;
import snownee.loquat.network.SSyncSelectionPacket;
import snownee.loquat.util.AABBSerializer;

public class ReplaceCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) Commands.literal("replace").requires(CommandSourceStack::m_230897_)).then(Commands.argument("area", AreaArgument.area()).executes(ctx -> {
            Area area = AreaArgument.getArea(ctx, "area");
            if (area.getType() != AreaTypes.BOX) {
                throw LoquatCommand.AREA_MUST_BE_BOX.create();
            } else {
                PosSelection selection = LoquatCommand.getSingleSelectionAndClear(ctx);
                ListTag tag = AreaManager.saveAreas(List.of(area));
                tag.getCompound(0).put("AABB", AABBSerializer.write(selection.toAABB()));
                CommandSourceStack source = (CommandSourceStack) ctx.getSource();
                AreaManager areaManager = AreaManager.of(source.getLevel());
                areaManager.remove(area.getUuid());
                areaManager.add((Area) AreaManager.loadAreas(tag).get(0));
                SSyncSelectionPacket.sync(source.getPlayerOrException());
                source.sendSuccess(() -> Component.translatable("loquat.command.replace.success"), true);
                return 1;
            }
        }));
    }
}