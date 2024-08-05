package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.Components;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;

public class KillTrainCommand {

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("killTrain").requires(cs -> cs.hasPermission(2))).then(Commands.argument("train", UuidArgument.uuid()).executes(ctx -> {
            CommandSourceStack source = (CommandSourceStack) ctx.getSource();
            run(source, UuidArgument.getUuid(ctx, "train"));
            return 1;
        }));
    }

    private static void run(CommandSourceStack source, UUID argument) {
        Train train = (Train) Create.RAILWAYS.trains.get(argument);
        if (train == null) {
            source.sendFailure(Components.literal("No Train with id " + argument.toString().substring(0, 5) + "[...] was found"));
        } else {
            train.invalid = true;
            source.sendSuccess(() -> Components.literal("Train '").append(train.name).append("' removed successfully"), true);
        }
    }
}