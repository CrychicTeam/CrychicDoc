package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class TeleportCommand {

    private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.teleport.invalidPosition"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        LiteralCommandNode<CommandSourceStack> $$1 = commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("teleport").requires(p_139039_ -> p_139039_.hasPermission(2))).then(Commands.argument("location", Vec3Argument.vec3()).executes(p_139051_ -> teleportToPos((CommandSourceStack) p_139051_.getSource(), Collections.singleton(((CommandSourceStack) p_139051_.getSource()).getEntityOrException()), ((CommandSourceStack) p_139051_.getSource()).getLevel(), Vec3Argument.getCoordinates(p_139051_, "location"), WorldCoordinates.current(), null)))).then(Commands.argument("destination", EntityArgument.entity()).executes(p_139049_ -> teleportToEntity((CommandSourceStack) p_139049_.getSource(), Collections.singleton(((CommandSourceStack) p_139049_.getSource()).getEntityOrException()), EntityArgument.getEntity(p_139049_, "destination"))))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.entities()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("location", Vec3Argument.vec3()).executes(p_139047_ -> teleportToPos((CommandSourceStack) p_139047_.getSource(), EntityArgument.getEntities(p_139047_, "targets"), ((CommandSourceStack) p_139047_.getSource()).getLevel(), Vec3Argument.getCoordinates(p_139047_, "location"), null, null))).then(Commands.argument("rotation", RotationArgument.rotation()).executes(p_139045_ -> teleportToPos((CommandSourceStack) p_139045_.getSource(), EntityArgument.getEntities(p_139045_, "targets"), ((CommandSourceStack) p_139045_.getSource()).getLevel(), Vec3Argument.getCoordinates(p_139045_, "location"), RotationArgument.getRotation(p_139045_, "rotation"), null)))).then(((LiteralArgumentBuilder) Commands.literal("facing").then(Commands.literal("entity").then(((RequiredArgumentBuilder) Commands.argument("facingEntity", EntityArgument.entity()).executes(p_139043_ -> teleportToPos((CommandSourceStack) p_139043_.getSource(), EntityArgument.getEntities(p_139043_, "targets"), ((CommandSourceStack) p_139043_.getSource()).getLevel(), Vec3Argument.getCoordinates(p_139043_, "location"), null, new TeleportCommand.LookAt(EntityArgument.getEntity(p_139043_, "facingEntity"), EntityAnchorArgument.Anchor.FEET)))).then(Commands.argument("facingAnchor", EntityAnchorArgument.anchor()).executes(p_139041_ -> teleportToPos((CommandSourceStack) p_139041_.getSource(), EntityArgument.getEntities(p_139041_, "targets"), ((CommandSourceStack) p_139041_.getSource()).getLevel(), Vec3Argument.getCoordinates(p_139041_, "location"), null, new TeleportCommand.LookAt(EntityArgument.getEntity(p_139041_, "facingEntity"), EntityAnchorArgument.getAnchor(p_139041_, "facingAnchor")))))))).then(Commands.argument("facingLocation", Vec3Argument.vec3()).executes(p_139037_ -> teleportToPos((CommandSourceStack) p_139037_.getSource(), EntityArgument.getEntities(p_139037_, "targets"), ((CommandSourceStack) p_139037_.getSource()).getLevel(), Vec3Argument.getCoordinates(p_139037_, "location"), null, new TeleportCommand.LookAt(Vec3Argument.getVec3(p_139037_, "facingLocation")))))))).then(Commands.argument("destination", EntityArgument.entity()).executes(p_139011_ -> teleportToEntity((CommandSourceStack) p_139011_.getSource(), EntityArgument.getEntities(p_139011_, "targets"), EntityArgument.getEntity(p_139011_, "destination"))))));
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("tp").requires(p_139013_ -> p_139013_.hasPermission(2))).redirect($$1));
    }

    private static int teleportToEntity(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, Entity entity2) throws CommandSyntaxException {
        for (Entity $$3 : collectionExtendsEntity1) {
            performTeleport(commandSourceStack0, $$3, (ServerLevel) entity2.level(), entity2.getX(), entity2.getY(), entity2.getZ(), EnumSet.noneOf(RelativeMovement.class), entity2.getYRot(), entity2.getXRot(), null);
        }
        if (collectionExtendsEntity1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.teleport.success.entity.single", ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName(), entity2.getDisplayName()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.teleport.success.entity.multiple", collectionExtendsEntity1.size(), entity2.getDisplayName()), true);
        }
        return collectionExtendsEntity1.size();
    }

    private static int teleportToPos(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, ServerLevel serverLevel2, Coordinates coordinates3, @Nullable Coordinates coordinates4, @Nullable TeleportCommand.LookAt teleportCommandLookAt5) throws CommandSyntaxException {
        Vec3 $$6 = coordinates3.getPosition(commandSourceStack0);
        Vec2 $$7 = coordinates4 == null ? null : coordinates4.getRotation(commandSourceStack0);
        Set<RelativeMovement> $$8 = EnumSet.noneOf(RelativeMovement.class);
        if (coordinates3.isXRelative()) {
            $$8.add(RelativeMovement.X);
        }
        if (coordinates3.isYRelative()) {
            $$8.add(RelativeMovement.Y);
        }
        if (coordinates3.isZRelative()) {
            $$8.add(RelativeMovement.Z);
        }
        if (coordinates4 == null) {
            $$8.add(RelativeMovement.X_ROT);
            $$8.add(RelativeMovement.Y_ROT);
        } else {
            if (coordinates4.isXRelative()) {
                $$8.add(RelativeMovement.X_ROT);
            }
            if (coordinates4.isYRelative()) {
                $$8.add(RelativeMovement.Y_ROT);
            }
        }
        for (Entity $$9 : collectionExtendsEntity1) {
            if (coordinates4 == null) {
                performTeleport(commandSourceStack0, $$9, serverLevel2, $$6.x, $$6.y, $$6.z, $$8, $$9.getYRot(), $$9.getXRot(), teleportCommandLookAt5);
            } else {
                performTeleport(commandSourceStack0, $$9, serverLevel2, $$6.x, $$6.y, $$6.z, $$8, $$7.y, $$7.x, teleportCommandLookAt5);
            }
        }
        if (collectionExtendsEntity1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.teleport.success.location.single", ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName(), formatDouble($$6.x), formatDouble($$6.y), formatDouble($$6.z)), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.teleport.success.location.multiple", collectionExtendsEntity1.size(), formatDouble($$6.x), formatDouble($$6.y), formatDouble($$6.z)), true);
        }
        return collectionExtendsEntity1.size();
    }

    private static String formatDouble(double double0) {
        return String.format(Locale.ROOT, "%f", double0);
    }

    private static void performTeleport(CommandSourceStack commandSourceStack0, Entity entity1, ServerLevel serverLevel2, double double3, double double4, double double5, Set<RelativeMovement> setRelativeMovement6, float float7, float float8, @Nullable TeleportCommand.LookAt teleportCommandLookAt9) throws CommandSyntaxException {
        BlockPos $$10 = BlockPos.containing(double3, double4, double5);
        if (!Level.isInSpawnableBounds($$10)) {
            throw INVALID_POSITION.create();
        } else {
            float $$11 = Mth.wrapDegrees(float7);
            float $$12 = Mth.wrapDegrees(float8);
            if (entity1.teleportTo(serverLevel2, double3, double4, double5, setRelativeMovement6, $$11, $$12)) {
                if (teleportCommandLookAt9 != null) {
                    teleportCommandLookAt9.perform(commandSourceStack0, entity1);
                }
                if (!(entity1 instanceof LivingEntity $$13) || !$$13.isFallFlying()) {
                    entity1.setDeltaMovement(entity1.getDeltaMovement().multiply(1.0, 0.0, 1.0));
                    entity1.setOnGround(true);
                }
                if (entity1 instanceof PathfinderMob $$14) {
                    $$14.m_21573_().stop();
                }
            }
        }
    }

    static class LookAt {

        private final Vec3 position;

        private final Entity entity;

        private final EntityAnchorArgument.Anchor anchor;

        public LookAt(Entity entity0, EntityAnchorArgument.Anchor entityAnchorArgumentAnchor1) {
            this.entity = entity0;
            this.anchor = entityAnchorArgumentAnchor1;
            this.position = entityAnchorArgumentAnchor1.apply(entity0);
        }

        public LookAt(Vec3 vec0) {
            this.entity = null;
            this.position = vec0;
            this.anchor = null;
        }

        public void perform(CommandSourceStack commandSourceStack0, Entity entity1) {
            if (this.entity != null) {
                if (entity1 instanceof ServerPlayer) {
                    ((ServerPlayer) entity1).lookAt(commandSourceStack0.getAnchor(), this.entity, this.anchor);
                } else {
                    entity1.lookAt(commandSourceStack0.getAnchor(), this.position);
                }
            } else {
                entity1.lookAt(commandSourceStack0.getAnchor(), this.position);
            }
        }
    }
}