package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;

public class PassengerCommand {

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("passenger").requires(cs -> cs.hasPermission(2))).then(Commands.argument("rider", EntityArgument.entity()).then(((RequiredArgumentBuilder) Commands.argument("vehicle", EntityArgument.entity()).executes(ctx -> {
            run((CommandSourceStack) ctx.getSource(), EntityArgument.getEntity(ctx, "vehicle"), EntityArgument.getEntity(ctx, "rider"), 0);
            return 1;
        })).then(Commands.argument("seatIndex", IntegerArgumentType.integer(0)).executes(ctx -> {
            run((CommandSourceStack) ctx.getSource(), EntityArgument.getEntity(ctx, "vehicle"), EntityArgument.getEntity(ctx, "rider"), IntegerArgumentType.getInteger(ctx, "seatIndex"));
            return 1;
        }))));
    }

    private static void run(CommandSourceStack source, Entity vehicle, Entity rider, int index) {
        if (vehicle != rider) {
            if (!(rider instanceof CarriageContraptionEntity)) {
                if (!(rider instanceof ControlledContraptionEntity)) {
                    if (vehicle instanceof AbstractContraptionEntity ace) {
                        if (ace.getContraption().getSeats().size() > index) {
                            ace.addSittingPassenger(rider, index);
                        }
                    } else {
                        rider.startRiding(vehicle, true);
                    }
                }
            }
        }
    }
}