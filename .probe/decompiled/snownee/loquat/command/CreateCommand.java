package snownee.loquat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.AABBArea;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.PosSelection;
import snownee.loquat.network.SSyncSelectionPacket;

public class CreateCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("create").requires(CommandSourceStack::m_230897_)).executes(ctx -> {
            PosSelection selection = LoquatCommand.getSingleSelectionAndClear(ctx);
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            SSyncSelectionPacket.sync(source.getPlayerOrException());
            AABBArea area = new AABBArea(selection.toAABB());
            return addArea(source, area);
        })).then(Commands.literal("box").then(Commands.argument("begin", Vec3Argument.vec3(false)).then(Commands.argument("end", Vec3Argument.vec3(false)).executes(ctx -> {
            Vec3 begin = Vec3Argument.getVec3(ctx, "begin");
            Vec3 end = Vec3Argument.getVec3(ctx, "end");
            AABBArea area = AABBArea.of(begin, end);
            return addArea((CommandSourceStack) ctx.getSource(), area);
        }))));
    }

    private static int addArea(CommandSourceStack source, Area area) {
        area.setUuid(UUID.randomUUID());
        AreaManager manager = AreaManager.of(source.getLevel());
        if (manager.contains(area)) {
            source.sendFailure(Component.translatable("loquat.command.areaAlreadyExists"));
            return 0;
        } else {
            manager.add(area);
            source.sendSuccess(() -> Component.translatable("loquat.command.create.success"), true);
            return 1;
        }
    }
}