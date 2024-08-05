package net.minecraft.server.commands;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.Set;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.Entity;

public class TagCommand {

    private static final SimpleCommandExceptionType ERROR_ADD_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.tag.add.failed"));

    private static final SimpleCommandExceptionType ERROR_REMOVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.tag.remove.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("tag").requires(p_138844_ -> p_138844_.hasPermission(2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.entities()).then(Commands.literal("add").then(Commands.argument("name", StringArgumentType.word()).executes(p_138861_ -> addTag((CommandSourceStack) p_138861_.getSource(), EntityArgument.getEntities(p_138861_, "targets"), StringArgumentType.getString(p_138861_, "name")))))).then(Commands.literal("remove").then(Commands.argument("name", StringArgumentType.word()).suggests((p_138841_, p_138842_) -> SharedSuggestionProvider.suggest(getTags(EntityArgument.getEntities(p_138841_, "targets")), p_138842_)).executes(p_138855_ -> removeTag((CommandSourceStack) p_138855_.getSource(), EntityArgument.getEntities(p_138855_, "targets"), StringArgumentType.getString(p_138855_, "name")))))).then(Commands.literal("list").executes(p_138839_ -> listTags((CommandSourceStack) p_138839_.getSource(), EntityArgument.getEntities(p_138839_, "targets"))))));
    }

    private static Collection<String> getTags(Collection<? extends Entity> collectionExtendsEntity0) {
        Set<String> $$1 = Sets.newHashSet();
        for (Entity $$2 : collectionExtendsEntity0) {
            $$1.addAll($$2.getTags());
        }
        return $$1;
    }

    private static int addTag(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, String string2) throws CommandSyntaxException {
        int $$3 = 0;
        for (Entity $$4 : collectionExtendsEntity1) {
            if ($$4.addTag(string2)) {
                $$3++;
            }
        }
        if ($$3 == 0) {
            throw ERROR_ADD_FAILED.create();
        } else {
            if (collectionExtendsEntity1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.add.success.single", string2, ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.add.success.multiple", string2, collectionExtendsEntity1.size()), true);
            }
            return $$3;
        }
    }

    private static int removeTag(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, String string2) throws CommandSyntaxException {
        int $$3 = 0;
        for (Entity $$4 : collectionExtendsEntity1) {
            if ($$4.removeTag(string2)) {
                $$3++;
            }
        }
        if ($$3 == 0) {
            throw ERROR_REMOVE_FAILED.create();
        } else {
            if (collectionExtendsEntity1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.remove.success.single", string2, ((Entity) collectionExtendsEntity1.iterator().next()).getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.remove.success.multiple", string2, collectionExtendsEntity1.size()), true);
            }
            return $$3;
        }
    }

    private static int listTags(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1) {
        Set<String> $$2 = Sets.newHashSet();
        for (Entity $$3 : collectionExtendsEntity1) {
            $$2.addAll($$3.getTags());
        }
        if (collectionExtendsEntity1.size() == 1) {
            Entity $$4 = (Entity) collectionExtendsEntity1.iterator().next();
            if ($$2.isEmpty()) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.list.single.empty", $$4.getDisplayName()), false);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.list.single.success", $$4.getDisplayName(), $$2.size(), ComponentUtils.formatList($$2)), false);
            }
        } else if ($$2.isEmpty()) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.list.multiple.empty", collectionExtendsEntity1.size()), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.tag.list.multiple.success", collectionExtendsEntity1.size(), $$2.size(), ComponentUtils.formatList($$2)), false);
        }
        return $$2.size();
    }
}