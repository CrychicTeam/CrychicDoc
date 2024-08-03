package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class DebugValueCommand {

    public static float value = 0.0F;

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("debugValue").requires(cs -> cs.hasPermission(4))).then(Commands.argument("value", FloatArgumentType.floatArg()).executes(ctx -> {
            value = FloatArgumentType.getFloat(ctx, "value");
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Components.literal("Set value to: " + value), true);
            return 1;
        }));
    }
}