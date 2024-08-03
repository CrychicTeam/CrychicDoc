package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class RideCommand {

    private static final DynamicCommandExceptionType ERROR_NOT_RIDING = new DynamicCommandExceptionType(p_265076_ -> Component.translatable("commands.ride.not_riding", p_265076_));

    private static final Dynamic2CommandExceptionType ERROR_ALREADY_RIDING = new Dynamic2CommandExceptionType((p_265488_, p_265072_) -> Component.translatable("commands.ride.already_riding", p_265488_, p_265072_));

    private static final Dynamic2CommandExceptionType ERROR_MOUNT_FAILED = new Dynamic2CommandExceptionType((p_265321_, p_265603_) -> Component.translatable("commands.ride.mount.failure.generic", p_265321_, p_265603_));

    private static final SimpleCommandExceptionType ERROR_MOUNTING_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.ride.mount.failure.cant_ride_players"));

    private static final SimpleCommandExceptionType ERROR_MOUNTING_LOOP = new SimpleCommandExceptionType(Component.translatable("commands.ride.mount.failure.loop"));

    private static final SimpleCommandExceptionType ERROR_WRONG_DIMENSION = new SimpleCommandExceptionType(Component.translatable("commands.ride.mount.failure.wrong_dimension"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("ride").requires(p_265326_ -> p_265326_.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("target", EntityArgument.entity()).then(Commands.literal("mount").then(Commands.argument("vehicle", EntityArgument.entity()).executes(p_265139_ -> mount((CommandSourceStack) p_265139_.getSource(), EntityArgument.getEntity(p_265139_, "target"), EntityArgument.getEntity(p_265139_, "vehicle")))))).then(Commands.literal("dismount").executes(p_265418_ -> dismount((CommandSourceStack) p_265418_.getSource(), EntityArgument.getEntity(p_265418_, "target"))))));
    }

    private static int mount(CommandSourceStack commandSourceStack0, Entity entity1, Entity entity2) throws CommandSyntaxException {
        Entity $$3 = entity1.getVehicle();
        if ($$3 != null) {
            throw ERROR_ALREADY_RIDING.create(entity1.getDisplayName(), $$3.getDisplayName());
        } else if (entity2.getType() == EntityType.PLAYER) {
            throw ERROR_MOUNTING_PLAYER.create();
        } else if (entity1.getSelfAndPassengers().anyMatch(p_265501_ -> p_265501_ == entity2)) {
            throw ERROR_MOUNTING_LOOP.create();
        } else if (entity1.level() != entity2.level()) {
            throw ERROR_WRONG_DIMENSION.create();
        } else if (!entity1.startRiding(entity2, true)) {
            throw ERROR_MOUNT_FAILED.create(entity1.getDisplayName(), entity2.getDisplayName());
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.ride.mount.success", entity1.getDisplayName(), entity2.getDisplayName()), true);
            return 1;
        }
    }

    private static int dismount(CommandSourceStack commandSourceStack0, Entity entity1) throws CommandSyntaxException {
        Entity $$2 = entity1.getVehicle();
        if ($$2 == null) {
            throw ERROR_NOT_RIDING.create(entity1.getDisplayName());
        } else {
            entity1.stopRiding();
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.ride.dismount.success", entity1.getDisplayName(), $$2.getDisplayName()), true);
            return 1;
        }
    }
}