package noppes.npcs.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.controllers.data.MarkData;

public class CmdMark {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> command = (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("mark").requires(source -> source.hasPermission(2));
        command.then(Commands.argument("clear", EntityArgument.entities()).executes(context -> {
            for (Entity entity : EntityArgument.getEntities(context, "entities")) {
                if (entity instanceof LivingEntity) {
                    MarkData data = MarkData.get((LivingEntity) entity);
                    data.marks.clear();
                    data.syncClients();
                }
            }
            return 1;
        }));
        command.then(Commands.argument("entities", EntityArgument.entities()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("type", IntegerArgumentType.integer(0)).executes(context -> {
            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
            if (entities.isEmpty()) {
                return 1;
            } else {
                int type = IntegerArgumentType.getInteger(context, "type");
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        MarkData data = MarkData.get((LivingEntity) entity);
                        data.marks.clear();
                        data.addMark(type, 16777215);
                    }
                }
                return 1;
            }
        })).then(Commands.argument("color", StringArgumentType.word()))).executes(context -> {
            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
            if (entities.isEmpty()) {
                return 1;
            } else {
                int type = IntegerArgumentType.getInteger(context, "type");
                int color = 16777215;
                try {
                    color = Integer.parseInt(StringArgumentType.getString(context, "color"), 16);
                } catch (Exception var7) {
                }
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        MarkData data = MarkData.get((LivingEntity) entity);
                        data.marks.clear();
                        data.addMark(type, color);
                    }
                }
                return 1;
            }
        })));
        return command;
    }
}