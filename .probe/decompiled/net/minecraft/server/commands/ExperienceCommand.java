package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ExperienceCommand {

    private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType(Component.translatable("commands.experience.set.points.invalid"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        LiteralCommandNode<CommandSourceStack> $$1 = commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("experience").requires(p_137324_ -> p_137324_.hasPermission(2))).then(Commands.literal("add").then(Commands.argument("targets", EntityArgument.players()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("amount", IntegerArgumentType.integer()).executes(p_137341_ -> addExperience((CommandSourceStack) p_137341_.getSource(), EntityArgument.getPlayers(p_137341_, "targets"), IntegerArgumentType.getInteger(p_137341_, "amount"), ExperienceCommand.Type.POINTS))).then(Commands.literal("points").executes(p_137339_ -> addExperience((CommandSourceStack) p_137339_.getSource(), EntityArgument.getPlayers(p_137339_, "targets"), IntegerArgumentType.getInteger(p_137339_, "amount"), ExperienceCommand.Type.POINTS)))).then(Commands.literal("levels").executes(p_137337_ -> addExperience((CommandSourceStack) p_137337_.getSource(), EntityArgument.getPlayers(p_137337_, "targets"), IntegerArgumentType.getInteger(p_137337_, "amount"), ExperienceCommand.Type.LEVELS))))))).then(Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("amount", IntegerArgumentType.integer(0)).executes(p_137335_ -> setExperience((CommandSourceStack) p_137335_.getSource(), EntityArgument.getPlayers(p_137335_, "targets"), IntegerArgumentType.getInteger(p_137335_, "amount"), ExperienceCommand.Type.POINTS))).then(Commands.literal("points").executes(p_137333_ -> setExperience((CommandSourceStack) p_137333_.getSource(), EntityArgument.getPlayers(p_137333_, "targets"), IntegerArgumentType.getInteger(p_137333_, "amount"), ExperienceCommand.Type.POINTS)))).then(Commands.literal("levels").executes(p_137331_ -> setExperience((CommandSourceStack) p_137331_.getSource(), EntityArgument.getPlayers(p_137331_, "targets"), IntegerArgumentType.getInteger(p_137331_, "amount"), ExperienceCommand.Type.LEVELS))))))).then(Commands.literal("query").then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.player()).then(Commands.literal("points").executes(p_137322_ -> queryExperience((CommandSourceStack) p_137322_.getSource(), EntityArgument.getPlayer(p_137322_, "targets"), ExperienceCommand.Type.POINTS)))).then(Commands.literal("levels").executes(p_137309_ -> queryExperience((CommandSourceStack) p_137309_.getSource(), EntityArgument.getPlayer(p_137309_, "targets"), ExperienceCommand.Type.LEVELS))))));
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("xp").requires(p_137311_ -> p_137311_.hasPermission(2))).redirect($$1));
    }

    private static int queryExperience(CommandSourceStack commandSourceStack0, ServerPlayer serverPlayer1, ExperienceCommand.Type experienceCommandType2) {
        int $$3 = experienceCommandType2.query.applyAsInt(serverPlayer1);
        commandSourceStack0.sendSuccess(() -> Component.translatable("commands.experience.query." + experienceCommandType2.name, serverPlayer1.m_5446_(), $$3), false);
        return $$3;
    }

    private static int addExperience(CommandSourceStack commandSourceStack0, Collection<? extends ServerPlayer> collectionExtendsServerPlayer1, int int2, ExperienceCommand.Type experienceCommandType3) {
        for (ServerPlayer $$4 : collectionExtendsServerPlayer1) {
            experienceCommandType3.add.accept($$4, int2);
        }
        if (collectionExtendsServerPlayer1.size() == 1) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.experience.add." + experienceCommandType3.name + ".success.single", int2, ((ServerPlayer) collectionExtendsServerPlayer1.iterator().next()).m_5446_()), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.experience.add." + experienceCommandType3.name + ".success.multiple", int2, collectionExtendsServerPlayer1.size()), true);
        }
        return collectionExtendsServerPlayer1.size();
    }

    private static int setExperience(CommandSourceStack commandSourceStack0, Collection<? extends ServerPlayer> collectionExtendsServerPlayer1, int int2, ExperienceCommand.Type experienceCommandType3) throws CommandSyntaxException {
        int $$4 = 0;
        for (ServerPlayer $$5 : collectionExtendsServerPlayer1) {
            if (experienceCommandType3.set.test($$5, int2)) {
                $$4++;
            }
        }
        if ($$4 == 0) {
            throw ERROR_SET_POINTS_INVALID.create();
        } else {
            if (collectionExtendsServerPlayer1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.experience.set." + experienceCommandType3.name + ".success.single", int2, ((ServerPlayer) collectionExtendsServerPlayer1.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.experience.set." + experienceCommandType3.name + ".success.multiple", int2, collectionExtendsServerPlayer1.size()), true);
            }
            return collectionExtendsServerPlayer1.size();
        }
    }

    static enum Type {

        POINTS("points", Player::m_6756_, (p_289274_, p_289275_) -> {
            if (p_289275_ >= p_289274_.m_36323_()) {
                return false;
            } else {
                p_289274_.setExperiencePoints(p_289275_);
                return true;
            }
        }, p_289273_ -> Mth.floor(p_289273_.f_36080_ * (float) p_289273_.m_36323_())), LEVELS("levels", ServerPlayer::m_6749_, (p_137360_, p_137361_) -> {
            p_137360_.setExperienceLevels(p_137361_);
            return true;
        }, p_287335_ -> p_287335_.f_36078_);

        public final BiConsumer<ServerPlayer, Integer> add;

        public final BiPredicate<ServerPlayer, Integer> set;

        public final String name;

        final ToIntFunction<ServerPlayer> query;

        private Type(String p_137353_, BiConsumer<ServerPlayer, Integer> p_137354_, BiPredicate<ServerPlayer, Integer> p_137355_, ToIntFunction<ServerPlayer> p_137356_) {
            this.add = p_137354_;
            this.name = p_137353_;
            this.set = p_137355_;
            this.query = p_137356_;
        }
    }
}