package com.nameless.indestructible.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public class AHPatchSetPhaseCommand implements Command<CommandSourceStack> {

    private static final AHPatchSetPhaseCommand COMMAND = new AHPatchSetPhaseCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("set_phase").then(Commands.argument("custom_phase", IntegerArgumentType.integer(0, 20)).executes(COMMAND));
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity living = EntityArgument.getEntity(context, "living_entity");
        int phase = IntegerArgumentType.getInteger(context, "custom_phase");
        AdvancedCustomHumanoidMobPatch<?> AHPatch = EpicFightCapabilities.getEntityPatch(living, AdvancedCustomHumanoidMobPatch.class);
        if (AHPatch != null) {
            AHPatch.setPhase(phase);
        }
        return 1;
    }
}