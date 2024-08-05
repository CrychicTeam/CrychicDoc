package noppes.npcs.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Map.Entry;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import noppes.npcs.entity.data.DataScenes;

public class CmdScene {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("scene").requires(source -> source.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("time").executes(context -> {
            ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.literal("Active scenes:"), false);
            for (Entry<String, DataScenes.SceneState> entry : DataScenes.StartedScenes.entrySet()) {
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("Scene %s time is %s", entry.getKey(), ((DataScenes.SceneState) entry.getValue()).ticks), false);
            }
            return 1;
        })).then(((RequiredArgumentBuilder) Commands.argument("time", IntegerArgumentType.integer(0)).executes(context -> {
            int ticks = IntegerArgumentType.getInteger(context, "time");
            for (DataScenes.SceneState state : DataScenes.StartedScenes.values()) {
                state.ticks = ticks;
            }
            return 1;
        })).then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context, "name");
            DataScenes.SceneState state = (DataScenes.SceneState) DataScenes.StartedScenes.get(name.toLowerCase());
            if (state == null) {
                throw new CommandRuntimeException(Component.translatable("Unknown scene name %s", name));
            } else {
                state.ticks = IntegerArgumentType.getInteger(context, "time");
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("Scene %s set to %s", name, state.ticks), false);
                return 1;
            }
        }))))).then(((LiteralArgumentBuilder) Commands.literal("reset").executes(context -> {
            DataScenes.Reset((CommandSourceStack) context.getSource(), null);
            return 1;
        })).then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
            DataScenes.Reset((CommandSourceStack) context.getSource(), StringArgumentType.getString(context, "name"));
            return 1;
        })))).then(Commands.literal("start").then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
            DataScenes.Start(((CommandSourceStack) context.getSource()).getServer(), StringArgumentType.getString(context, "name"));
            return 1;
        })))).then(((LiteralArgumentBuilder) Commands.literal("pause").executes(context -> {
            DataScenes.Pause((CommandSourceStack) context.getSource(), null);
            return 1;
        })).then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
            DataScenes.Pause((CommandSourceStack) context.getSource(), StringArgumentType.getString(context, "name"));
            return 1;
        })));
    }
}