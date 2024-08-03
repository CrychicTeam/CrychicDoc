package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeCommand {

    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.recipe.give.failed"));

    private static final SimpleCommandExceptionType ERROR_TAKE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.recipe.take.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("recipe").requires(p_138205_ -> p_138205_.hasPermission(2))).then(Commands.literal("give").then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES).executes(p_138219_ -> giveRecipes((CommandSourceStack) p_138219_.getSource(), EntityArgument.getPlayers(p_138219_, "targets"), Collections.singleton(ResourceLocationArgument.getRecipe(p_138219_, "recipe")))))).then(Commands.literal("*").executes(p_138217_ -> giveRecipes((CommandSourceStack) p_138217_.getSource(), EntityArgument.getPlayers(p_138217_, "targets"), ((CommandSourceStack) p_138217_.getSource()).getServer().getRecipeManager().getRecipes())))))).then(Commands.literal("take").then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES).executes(p_138211_ -> takeRecipes((CommandSourceStack) p_138211_.getSource(), EntityArgument.getPlayers(p_138211_, "targets"), Collections.singleton(ResourceLocationArgument.getRecipe(p_138211_, "recipe")))))).then(Commands.literal("*").executes(p_138203_ -> takeRecipes((CommandSourceStack) p_138203_.getSource(), EntityArgument.getPlayers(p_138203_, "targets"), ((CommandSourceStack) p_138203_.getSource()).getServer().getRecipeManager().getRecipes()))))));
    }

    private static int giveRecipes(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, Collection<Recipe<?>> collectionRecipe2) throws CommandSyntaxException {
        int $$3 = 0;
        for (ServerPlayer $$4 : collectionServerPlayer1) {
            $$3 += $$4.awardRecipes(collectionRecipe2);
        }
        if ($$3 == 0) {
            throw ERROR_GIVE_FAILED.create();
        } else {
            if (collectionServerPlayer1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.recipe.give.success.single", collectionRecipe2.size(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.recipe.give.success.multiple", collectionRecipe2.size(), collectionServerPlayer1.size()), true);
            }
            return $$3;
        }
    }

    private static int takeRecipes(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, Collection<Recipe<?>> collectionRecipe2) throws CommandSyntaxException {
        int $$3 = 0;
        for (ServerPlayer $$4 : collectionServerPlayer1) {
            $$3 += $$4.resetRecipes(collectionRecipe2);
        }
        if ($$3 == 0) {
            throw ERROR_TAKE_FAILED.create();
        } else {
            if (collectionServerPlayer1.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.recipe.take.success.single", collectionRecipe2.size(), ((ServerPlayer) collectionServerPlayer1.iterator().next()).m_5446_()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.recipe.take.success.multiple", collectionRecipe2.size(), collectionServerPlayer1.size()), true);
            }
            return $$3;
        }
    }
}