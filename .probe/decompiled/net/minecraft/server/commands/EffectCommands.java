package net.minecraft.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectCommands {

    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.give.failed"));

    private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.clear.everything.failed"));

    private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.clear.specific.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("effect").requires(p_136958_ -> p_136958_.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("clear").executes(p_136984_ -> clearEffects((CommandSourceStack) p_136984_.getSource(), ImmutableList.of(((CommandSourceStack) p_136984_.getSource()).getEntityOrException())))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.entities()).executes(p_136982_ -> clearEffects((CommandSourceStack) p_136982_.getSource(), EntityArgument.getEntities(p_136982_, "targets")))).then(Commands.argument("effect", ResourceArgument.resource(commandBuildContext1, Registries.MOB_EFFECT)).executes(p_248126_ -> clearEffect((CommandSourceStack) p_248126_.getSource(), EntityArgument.getEntities(p_248126_, "targets"), ResourceArgument.getMobEffect(p_248126_, "effect"))))))).then(Commands.literal("give").then(Commands.argument("targets", EntityArgument.entities()).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("effect", ResourceArgument.resource(commandBuildContext1, Registries.MOB_EFFECT)).executes(p_248127_ -> giveEffect((CommandSourceStack) p_248127_.getSource(), EntityArgument.getEntities(p_248127_, "targets"), ResourceArgument.getMobEffect(p_248127_, "effect"), null, 0, true))).then(((RequiredArgumentBuilder) Commands.argument("seconds", IntegerArgumentType.integer(1, 1000000)).executes(p_248124_ -> giveEffect((CommandSourceStack) p_248124_.getSource(), EntityArgument.getEntities(p_248124_, "targets"), ResourceArgument.getMobEffect(p_248124_, "effect"), IntegerArgumentType.getInteger(p_248124_, "seconds"), 0, true))).then(((RequiredArgumentBuilder) Commands.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes(p_248123_ -> giveEffect((CommandSourceStack) p_248123_.getSource(), EntityArgument.getEntities(p_248123_, "targets"), ResourceArgument.getMobEffect(p_248123_, "effect"), IntegerArgumentType.getInteger(p_248123_, "seconds"), IntegerArgumentType.getInteger(p_248123_, "amplifier"), true))).then(Commands.argument("hideParticles", BoolArgumentType.bool()).executes(p_248125_ -> giveEffect((CommandSourceStack) p_248125_.getSource(), EntityArgument.getEntities(p_248125_, "targets"), ResourceArgument.getMobEffect(p_248125_, "effect"), IntegerArgumentType.getInteger(p_248125_, "seconds"), IntegerArgumentType.getInteger(p_248125_, "amplifier"), !BoolArgumentType.getBool(p_248125_, "hideParticles"))))))).then(((LiteralArgumentBuilder) Commands.literal("infinite").executes(p_267907_ -> giveEffect((CommandSourceStack) p_267907_.getSource(), EntityArgument.getEntities(p_267907_, "targets"), ResourceArgument.getMobEffect(p_267907_, "effect"), -1, 0, true))).then(((RequiredArgumentBuilder) Commands.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes(p_267908_ -> giveEffect((CommandSourceStack) p_267908_.getSource(), EntityArgument.getEntities(p_267908_, "targets"), ResourceArgument.getMobEffect(p_267908_, "effect"), -1, IntegerArgumentType.getInteger(p_267908_, "amplifier"), true))).then(Commands.argument("hideParticles", BoolArgumentType.bool()).executes(p_267909_ -> giveEffect((CommandSourceStack) p_267909_.getSource(), EntityArgument.getEntities(p_267909_, "targets"), ResourceArgument.getMobEffect(p_267909_, "effect"), -1, IntegerArgumentType.getInteger(p_267909_, "amplifier"), !BoolArgumentType.getBool(p_267909_, "hideParticles"))))))))));
    }

    private static int giveEffect(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, Holder<MobEffect> holderMobEffect2, @Nullable Integer integer3, int int4, boolean boolean5) throws CommandSyntaxException {
        MobEffect $$6 = holderMobEffect2.value();
        int $$7 = 0;
        int $$8;
        if (integer3 != null) {
            if ($$6.isInstantenous()) {
                $$8 = integer3;
            } else if (integer3 == -1) {
                $$8 = -1;
            } else {
                $$8 = integer3 * 20;
            }
        } else if ($$6.isInstantenous()) {
            $$8 = 1;
        } else {
            $$8 = 600;
        }
        for (Entity $$13 : collectionExtendsEntity1) {
            if ($$13 instanceof LivingEntity) {
                MobEffectInstance $$14 = new MobEffectInstance($$6, $$8, int4, false, boolean5);
                if (((LivingEntity) $$13).addEffect($$14, commandSourceStack0.getEntity())) {
                    $$7++;
                }
            }
        }
        if ($$7 == 0) {
            throw ERROR_GIVE_FAILED.create();
        } else {
            if (collectionExtendsEntity1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.effect.give.success.single", $$6.getDisplayName(), ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName(), $$8 / 20), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.effect.give.success.multiple", $$6.getDisplayName(), collectionExtendsEntity1.size(), $$8 / 20), true);
            }
            return $$7;
        }
    }

    private static int clearEffects(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1) throws CommandSyntaxException {
        int $$2 = 0;
        for (Entity $$3 : collectionExtendsEntity1) {
            if ($$3 instanceof LivingEntity && ((LivingEntity) $$3).removeAllEffects()) {
                $$2++;
            }
        }
        if ($$2 == 0) {
            throw ERROR_CLEAR_EVERYTHING_FAILED.create();
        } else {
            if (collectionExtendsEntity1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.single", ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.multiple", collectionExtendsEntity1.size()), true);
            }
            return $$2;
        }
    }

    private static int clearEffect(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, Holder<MobEffect> holderMobEffect2) throws CommandSyntaxException {
        MobEffect $$3 = holderMobEffect2.value();
        int $$4 = 0;
        for (Entity $$5 : collectionExtendsEntity1) {
            if ($$5 instanceof LivingEntity && ((LivingEntity) $$5).removeEffect($$3)) {
                $$4++;
            }
        }
        if ($$4 == 0) {
            throw ERROR_CLEAR_SPECIFIC_FAILED.create();
        } else {
            if (collectionExtendsEntity1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.single", $$3.getDisplayName(), ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.multiple", $$3.getDisplayName(), collectionExtendsEntity1.size()), true);
            }
            return $$4;
        }
    }
}