package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class KillTPSCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("killtps").requires(cs -> cs.hasPermission(2))).executes(ctx -> {
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.slowed_by.0", Create.LAGGER.isLagging() ? Create.LAGGER.getTickTime() : 0), true);
            if (Create.LAGGER.isLagging()) {
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.usage.0"), true);
            } else {
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.usage.1"), true);
            }
            return 1;
        })).then(((LiteralArgumentBuilder) Commands.literal("start").executes(ctx -> {
            int tickTime = Create.LAGGER.getTickTime();
            if (tickTime > 0) {
                Create.LAGGER.setLagging(true);
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.slowed_by.1", tickTime), true);
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.usage.0"), true);
            } else {
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.usage.1"), true);
            }
            return 1;
        })).then(Commands.argument(Lang.translateDirect("command.killTPSCommand.argument.tickTime").getString(), IntegerArgumentType.integer(1)).executes(ctx -> {
            int tickTime = IntegerArgumentType.getInteger(ctx, Lang.translateDirect("command.killTPSCommand.argument.tickTime").getString());
            Create.LAGGER.setTickTime(tickTime);
            Create.LAGGER.setLagging(true);
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.slowed_by.1", tickTime), true);
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.usage.0"), true);
            return 1;
        })))).then(Commands.literal("stop").executes(ctx -> {
            Create.LAGGER.setLagging(false);
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Lang.translateDirect("command.killTPSCommand.status.slowed_by.2"), false);
            return 1;
        }));
    }
}