package com.nameless.indestructible.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Mob;

public class AHPatchSetLookAtCommand implements Command<CommandSourceStack> {

    private static final AHPatchSetLookAtCommand COMMAND = new AHPatchSetLookAtCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("rotate_to_target").executes(COMMAND);
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (EntityArgument.getEntity(context, "living_entity") instanceof Mob mob && mob.getTarget() != null) {
            mob.lookAt(mob.getTarget(), 30.0F, 30.0F);
        }
        return 1;
    }
}